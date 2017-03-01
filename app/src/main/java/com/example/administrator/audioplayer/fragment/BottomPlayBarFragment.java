package com.example.administrator.audioplayer.fragment;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.activity.PlayingActivity;
import com.example.administrator.audioplayer.service.MusicPlayer;
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


/**
 * 底部播放栏，在BaseActivity中初始化加入
 * 包括点击状态栏时间，刷新状态栏的操作
 */
public class BottomPlayBarFragment extends BaseFragment {


    private ProgressBar mProgress;
    public Runnable mUpdateProgress = new Runnable() {

        @Override
        public void run() {

            long position = MusicPlayer.position();
            long duration = MusicPlayer.duration();
            if (duration > 0 && duration < 627080716) {
                mProgress.setProgress((int) (1000 * position / duration));
            }

            if (MusicPlayer.isPlaying()) {
                mProgress.postDelayed(mUpdateProgress, 50);
            } else {
                mProgress.removeCallbacks(mUpdateProgress);
            }

        }
    };
    private ImageView mPlayPause;
    private TextView mTitle;
    private TextView mArtist;
    private SimpleDraweeView mAlbumArt;
    private View rootView;
    private ImageView playQueue, next;
    private String TAG = "QuickControlsFragment";



    public static BottomPlayBarFragment newInstance() {
        return new BottomPlayBarFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bottom_playbar, container, false);
        this.rootView = rootView;
        mPlayPause = (ImageView) rootView.findViewById(R.id.control);
        mProgress = (ProgressBar) rootView.findViewById(R.id.song_progress_normal);
        mTitle = (TextView) rootView.findViewById(R.id.playbar_info);
        mArtist = (TextView) rootView.findViewById(R.id.playbar_singer);
        mAlbumArt = (SimpleDraweeView) rootView.findViewById(R.id.playbar_img);
        next = (ImageView) rootView.findViewById(R.id.play_next);
        playQueue = (ImageView) rootView.findViewById(R.id.play_list);


        mPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPlayPause.setImageResource(MusicPlayer.isPlaying() ? R.drawable.playbar_btn_pause
                        : R.drawable.playbar_btn_play);

                if (MusicPlayer.getQueueSize() == 0) {
                    //Toast.makeText(MyApplication.getContext(), getResources().getString(R.string.queue_is_empty),
                    //        Toast.LENGTH_SHORT).show();
                } else {
                    MusicPlayer.playOrPause();
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicPlayer.next();
            }
        });

        playQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayQueueFragment playQueueFragment = new PlayQueueFragment();
                playQueueFragment.show(getFragmentManager(), "playqueuefragment");
            }
        });

        //点击整个PlayBar跳转到播放详情页面
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayingActivity.startActivity(getActivity());
                //设置activity进入动画
                getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
        });


        return rootView;
    }

    public void updateNowplayingCard() {
        mTitle.setText(MusicPlayer.getTrackName());
        mArtist.setText(MusicPlayer.getArtistName());
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
            uri = Uri.parse(MusicPlayer.getAlbumPath());
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
            mAlbumArt.setImageURI(Uri.parse("content://" + MusicPlayer.getAlbumPath()));
        }

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        mProgress.removeCallbacks(mUpdateProgress);
    }

    @Override
    public void onResume() {
        super.onResume();
        //在重新显示的时候刷新ui
        mProgress.setMax(1000);
        mProgress.postDelayed(mUpdateProgress, 10);
        updateNowplayingCard();
        updateState();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void updateState() {
        if (MusicPlayer.isPlaying()) {
            mPlayPause.setImageResource(R.drawable.playbar_btn_pause);
            mProgress.postDelayed(mUpdateProgress, 50);
        } else {
            mPlayPause.setImageResource(R.drawable.playbar_btn_play);
            mProgress.removeCallbacks(mUpdateProgress);
        }
    }

    /**
     * 重写该方法，当歌曲信息变更的时候调用
     * 唱针动画和唱片动画
     */
    @Override
    public void onMetaChange() {
        updateNowplayingCard();
    }


    /**
     * 重写该方法，当歌曲播放状态变更的时候调用
     */
    @Override
    public void onPlayStateChange() {
        updateState();
    }




}