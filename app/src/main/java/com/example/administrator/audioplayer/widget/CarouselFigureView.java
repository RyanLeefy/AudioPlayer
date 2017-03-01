package com.example.administrator.audioplayer.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.activity.MainActivity;
import com.example.administrator.audioplayer.http.HttpMethods;
import com.example.administrator.audioplayer.jsonbean.CarouselFigure;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class CarouselFigureView extends FrameLayout {
    //轮播图图片数量
    private final static int IMAGE_COUNT = 7;
    //自动轮播时间间隔
    private final static int TIME_INTERVAL = 4;
    //自动轮播启用开关
    private final static boolean isAutoPlay = true;

    private FPagerAdapter fPagerAdapter;

    private List<ImageView> imageViewList = new ArrayList<>();
    private List<View> dotViewList = new ArrayList<>();

    private ViewPager viewPager;
    private boolean isFromCache = true;

    private Context mContext;
    //当前轮播页面
    private int currentItem = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;

    private LayoutInflater mInflater;
    private View view;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }
    };


    public CarouselFigureView(Context context) {
        this(context, null);
        mContext = context;
    }

    public CarouselFigureView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
        mContext = context;
    }

    public CarouselFigureView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

        //初始化布局
        initUI(context);
        if (isAutoPlay) {
            startPlay();
        }

    }

    public void onDestroy() {
        scheduledExecutorService.shutdownNow();
        scheduledExecutorService = null;
        destoryBitmaps();
    }

    /**
     * 开始轮播图切换
     */

    public void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new LoopTask(), TIME_INTERVAL, TIME_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * 停止切换
     */
    private void stopPlay() {
        scheduledExecutorService.shutdown();
    }

    /**
     * 初始化UI
     *
     * @param context
     */
    private void initUI(Context context) {
        //初始化布局
        view = mInflater.inflate(R.layout.layout_carousel_figure, this, true);


        dotViewList.add(view.findViewById(R.id.v_dot1));
        dotViewList.add(view.findViewById(R.id.v_dot2));
        dotViewList.add(view.findViewById(R.id.v_dot3));
        dotViewList.add(view.findViewById(R.id.v_dot4));
        dotViewList.add(view.findViewById(R.id.v_dot5));
        dotViewList.add(view.findViewById(R.id.v_dot6));
        dotViewList.add(view.findViewById(R.id.v_dot7));


        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setFocusable(true);
        viewPager.addOnPageChangeListener(new MyPageChangeListener());

            Subscriber<CarouselFigure> subscriber = new Subscriber<CarouselFigure>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Logger.e(e.toString());
                }

                @Override
                public void onNext(CarouselFigure carouselFigure) {
                    for(int i = 0; i < carouselFigure.getPic().size(); i++) {
                        String imagesID = carouselFigure.getPic().get(i).getRandpic();

                        final SimpleDraweeView mAlbumArt = new SimpleDraweeView(mContext);

                        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
                            @Override
                            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                                if (imageInfo == null) {
                                    return;
                                }
                                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                                FLog.d("Final image received! " +
                                                "Size %d x %d",
                                        "Quality level %d, good enough: %s, full quality: %s",
                                        imageInfo.getWidth(),
                                        imageInfo.getHeight(),
                                        qualityInfo.getQuality(),
                                        qualityInfo.isOfGoodEnoughQuality(),
                                        qualityInfo.isOfFullQuality());
                            }

                            @Override
                            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                                //FLog.d("Intermediate image received");
                            }

                            @Override
                            public void onFailure(String id, Throwable throwable) {
                                mAlbumArt.setImageURI(Uri.parse("res:/" + R.drawable.placeholder_disk));
                            }
                        };
                        Uri uri = null;
                        try {
                            uri = Uri.parse(imagesID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (uri != null) {
                            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).build();

                            DraweeController controller = Fresco.newDraweeControllerBuilder()
                                    .setOldController(mAlbumArt.getController())
                                    .setImageRequest(request)
                                    .setControllerListener(controllerListener)
                                    .build();

                            mAlbumArt.setController(controller);
                        } else {
                            mAlbumArt.setImageURI(Uri.parse("res:/" + R.drawable.placeholder_disk));
                        }

                        mAlbumArt.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageViewList.add(mAlbumArt);

                        fPagerAdapter = new FPagerAdapter();
                        viewPager.setAdapter(fPagerAdapter);
                    }
                }
            };

        Observable observable = HttpMethods.getInstance().focusPic(7);


        ((MainActivity)mContext).addSubscription(
                observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));

    }


    private class FPagerAdapter extends PagerAdapter {


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViewList.get(position));
            return imageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewList.get(position));
        }

        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            super.restoreState(state, loader);
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        boolean isAutoPlay = false;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            for (int i = 0; i < dotViewList.size(); i++) {
                if (i == position) {
                    dotViewList.get(i).setBackgroundResource(R.drawable.red_point);
                } else {
                    dotViewList.get(i).setBackgroundResource(R.drawable.grey_point);
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                //手动拖动
                case ViewPager.SCROLL_STATE_DRAGGING:
                    //PrintLog.e("ViewPager:", "SCROLL_STATE_DRAGGING");
                    //标志位设为false
                    isAutoPlay = false;
                    stopPlay();
                    startPlay();
                    break;
                //界面切换中
                case ViewPager.SCROLL_STATE_SETTLING:
                    //切换过程中把自动isAutoPlay设为true;
                    //PrintLog.e("ViewPager:", "SCROLL_STATE_SETTLING");
                    isAutoPlay = true;
                    break;
                //切换完成（页面定住）
                //滑动完毕，继续回到第一张图
                //因为在第一页向左，最后一页向右时，拖动状态后没有切换，是直接定住的，所以isAutoPlay = false;
                case ViewPager.SCROLL_STATE_IDLE:
                    //PrintLog.e("ViewPager:", "SCROLL_STATE_IDLE");
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                    }
                    //当前为第一张，从左向右滑
                    else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }

        }
    }


    /**
     * 解决滑动冲突，滑到最右边不会滑去右边的fragment界面
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }



    /**
     * 循播任务
     */
    private class LoopTask implements Runnable {
        @Override
        public void run() {
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViewList.size();
                handler.obtainMessage().sendToTarget();

            }

        }
    }

    /**
     * 销毁ImageView回收资源
     */
    private void destoryBitmaps() {
        for (int i = 0; i < IMAGE_COUNT; i++) {
            ImageView imageView = imageViewList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null && drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        }
    }


}

