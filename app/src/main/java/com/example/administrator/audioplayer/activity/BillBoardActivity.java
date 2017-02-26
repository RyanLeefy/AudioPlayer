package com.example.administrator.audioplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.audioplayer.Ipresenter.IBillBoardPresenter;
import com.example.administrator.audioplayer.Ipresenter.INewAlbumPresenter;
import com.example.administrator.audioplayer.Iview.IBillBoardView;
import com.example.administrator.audioplayer.Iview.INewAlbumView;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.presenterImp.BillBoardPresenter;
import com.example.administrator.audioplayer.presenterImp.NewAlbumPresenter;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.example.administrator.audioplayer.utils.ImageUtils;
import com.example.administrator.audioplayer.widget.DividerItemDecoration;
import com.example.administrator.audioplayer.widget.ObserableRecyclerViewWithEmptyView;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;

import java.io.File;

/**
 * Created by on 2017/2/23 0023.
 */

public class BillBoardActivity extends BaseActivity implements ObservableScrollViewCallbacks,IBillBoardView {

    //状态栏高度
    private int mStatusSize;
    //导航栏高度
    private int mActionBarSize;
    //伸缩区域高度，即头部框的高度
    private int mFlexibleSpaceImageHeight;

    private Toolbar toolbar;
    private ActionBar actionbar;
    private ObserableRecyclerViewWithEmptyView recyclerView;
    //头部框
    private FrameLayout billboard_header;
    //头部内容框
    private RelativeLayout billboard_header_detail;
    //headerView的背景图片
    private ImageView albumart;
    //标题
    private TextView billBoard_title;
    //更新时间
    private TextView billBoard_updatetime;
    
    //三个框，收藏框，分享框，下载框（暂没有评论）
    private LinearLayout collection_layout, share_layout, dowm_layout;
    //收藏textView，显示是否已经收藏
    private TextView collection_text;
    //加载中动画视图
    private View loadingView;
    //加载动画视图中的动态ImageView
    private ImageView anim_image;
    //网络失败重试TextView
    private TextView try_again;

    private MusicAdapter adapter;

    private IBillBoardPresenter presenter;


    //创建activity时需要传入的数据
    //榜单类型
    private int type;
    //榜单名
    private  String name;
    //更新时间
    private  String update_time;



    /**
     * 创建Activity，要传入需要的数据
     * @param context
     * @param type
     */
    public static void startActivity(Context context, int type, String name, String update_time) {
        Intent intent = new Intent(context, BillBoardActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("name", name);
        intent.putExtra("update_time", update_time);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 重写该方法，当歌曲进行变换的时候刷新界面
     */
    @Override
    public void onMetaChange() {
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //让布局覆盖状态栏显示
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_billboard);

        //读取传入的数据
        if(getIntent().getExtras() != null) {
            type = getIntent().getIntExtra("type", 0);
            name = getIntent().getStringExtra("name");
            update_time = getIntent().getStringExtra("update_time");
        }

        toolbar = (Toolbar) findViewById(R.id.tb_billboard);

        billboard_header = (FrameLayout) findViewById(R.id.fl_billboard_header);
        billboard_header_detail = (RelativeLayout) findViewById(R.id.rl_billboard_header_detail);

        albumart = (ImageView) findViewById(R.id.img_album_art);


        billBoard_title = (TextView) findViewById(R.id.tv_billboard_title);
        billBoard_updatetime = (TextView) findViewById(R.id.tv_billboard_updatetime);


        collection_layout = (LinearLayout) findViewById(R.id.ll_billboard_collect);
        share_layout = (LinearLayout) findViewById(R.id.ll_billboard_share);
        dowm_layout = (LinearLayout) findViewById(R.id.ll_billboard_down);

        recyclerView = (ObserableRecyclerViewWithEmptyView) findViewById(R.id.orv_billboard);
        recyclerView.setScrollViewCallbacks(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));


        loadingView = findViewById(R.id.layout_loading_view);
        anim_image = (ImageView) findViewById(R.id.anim_image);

        //设置重试文本的格式，把重试两个字设为点击事件，点击开始重新读取列表
        SpannableString spanString = new SpannableString("请连接网络后点击重试");
        spanString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                loadingView.setVisibility(View.VISIBLE);
                try_again.setVisibility(View.INVISIBLE);
                presenter.onCreate(type);
            }
        }, 8, 10, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        try_again = (TextView) findViewById(R.id.tv_try_again);
        try_again.setText(spanString);
        try_again.setMovementMethod(LinkMovementMethod.getInstance());


        mActionBarSize = CommonUtils.getActionBarHeight(this);
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.FlexibleSpaceImageHeight);

        setToolbar();
        setHeaderBlurBackground();
        setHeaderView();
        setHeaderViewListener();

        presenter = new BillBoardPresenter(this);
        presenter.onCreate(type);

    }

    /**
     * 在窗口加载完之后，加载动画才可以运行
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        anim_image.setBackgroundResource(R.drawable.loading_animation);
        AnimationDrawable anim = (AnimationDrawable) anim_image.getBackground();
        anim.start();
    }


    /**
     * 初始化toolbar，设置paddingTop
     */
    private void setToolbar() {
        toolbar.setTitle("");
        //计算状态栏的高度，然后给toolbar设置paddingTop
        mStatusSize = CommonUtils.getStatusHeight(this);
        toolbar.setPadding(0, mStatusSize, 0, 0);
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 初始化头部的虚化背景
     */
    public void setHeaderBlurBackground() {
        if(type == 2) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ranklist_hotmusic);
            albumart.setImageDrawable(ImageUtils.createBlurredImageFromBitmap(bitmap, this));
        } else if(type == 1) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ranklist_newmusic);
            albumart.setImageDrawable(ImageUtils.createBlurredImageFromBitmap(bitmap, this));
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ranklist_origin);
            albumart.setImageDrawable(ImageUtils.createBlurredImageFromBitmap(bitmap, this));
        }
    }


    /**
     * 初始化头部的视图，榜单名字，更新时间
     */
    public void setHeaderView() {
        billBoard_title.setText(name);
        billBoard_updatetime.setText("更新时间：" + update_time);
    }


    /**
     * 初始化头部的监听事件，收藏，分享，评论，下载
     */
    public void setHeaderViewListener() {

        //收藏按钮监听
        collection_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //分享按钮监听
        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //下载按钮监听
        dowm_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    protected void updateViews(int scrollY, boolean animated) {
        // Translate header
        ViewHelper.setTranslationY(billboard_header, getHeaderTranslationY(scrollY));
    }

    protected float getHeaderTranslationY(int scrollY) {
        final int headerHeight = billboard_header.getHeight();
        int headerTranslationY = mActionBarSize + mStatusSize - headerHeight;
        if (mActionBarSize + mStatusSize <= -scrollY + headerHeight) {
            headerTranslationY = -scrollY;
        }
        return headerTranslationY;
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        updateViews(scrollY, false);

        if (scrollY > 0 && scrollY < mFlexibleSpaceImageHeight - mActionBarSize - mStatusSize) {
            toolbar.setTitle(name);
            actionbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_background));
        }
        if (scrollY == 0) {
            toolbar.setTitle("");
            toolbar.setSubtitle("");
            actionbar.setBackgroundDrawable(null);
        }
        if (scrollY > mFlexibleSpaceImageHeight - mActionBarSize - mStatusSize) {

        }

        float a = (float) scrollY / (mFlexibleSpaceImageHeight - mActionBarSize - mStatusSize);
        billboard_header_detail.setAlpha(1f - a);

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    public void setAdapter(MusicAdapter adapter) {
        //获取回来的adapter先设置监听事件，然后再设置给recycleView
        adapter.setOnPlayAllItemClickListener(new MusicAdapter.OnPlayAllItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                presenter.peformMusicClick(0);
            }

            @Override
            public void onMoreClick(View view, int position) {

            }
        });

        adapter.setOnMusicItemClickListener(new MusicAdapter.OnMusicItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                presenter.peformMusicClick(position);
            }

            @Override
            public void onMoreClick(View view, int position) {

            }
        });

        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showTryAgain() {
        loadingView.setVisibility(View.GONE);
        try_again.setVisibility(View.VISIBLE);
    }


}
