package com.example.administrator.audioplayer.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.audioplayer.Ipresenter.ICreatedCollectionPresenter;
import com.example.administrator.audioplayer.Ipresenter.ISongCollectionPresenter;
import com.example.administrator.audioplayer.Iview.ICreatedCollectionView;
import com.example.administrator.audioplayer.Iview.ISongCollectionView;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.adapter.PopUpWindowMenuAdapter;
import com.example.administrator.audioplayer.bean.LeftMenuItem;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.db.SongCollectionDB;
import com.example.administrator.audioplayer.presenterImp.CreatedCollectionPresenter;
import com.example.administrator.audioplayer.presenterImp.SongCollectionPresenter;
import com.example.administrator.audioplayer.utils.ActivityManager;
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
import java.util.Arrays;


public class CreatedCollectionActivity extends BaseActivity implements ObservableScrollViewCallbacks,ICreatedCollectionView {

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
    private FrameLayout songcollection_header;
    //头部内容框
    private RelativeLayout songcollection_header_detail;
    //headerView的背景图片
    private ImageView albumart;
    //歌单封面图片
    private SimpleDraweeView photo;
    //歌单名
    private TextView songCollection_title;
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

    private LayoutInflater mInflater;

    private PopupWindow popupWindow;

    private MusicAdapter adapter;

    private ICreatedCollectionPresenter presenter;

    //创建activity时需要传入的数据
    //歌单在数据库中的id
    private int id;
    //图片地址
    private String pic;
    //歌单名
    private String title;



    /**
     * 创建Activity，要传入需要的数据
     * @param context
     * @param pic
     * @param title
     */
    public static void startActivity(Context context, int id, String pic, String title) {
        Intent intent = new Intent(context, CreatedCollectionActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("pic", pic);
        intent.putExtra("title", title);
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
        //把Activity放入ActivityManager中进行管理，方便一键退出所有
        ActivityManager.getInstance().pushOneActivity(this);

        setContentView(R.layout.activity_created_collection);

        mInflater = LayoutInflater.from(this);

        //初始化底部播放栏，由父类BaseActivity在onStart()中显示
        bottom_container_framelayout = (FrameLayout) findViewById(R.id.bottom_container);

        //读取传入的数据
        if(getIntent().getExtras() != null) {
            id = getIntent().getIntExtra("id", -1);
            pic = getIntent().getStringExtra("pic");
            title = getIntent().getStringExtra("title");
        }

        toolbar = (Toolbar) findViewById(R.id.tb_songcollection);

        songcollection_header = (FrameLayout) findViewById(R.id.fl_songcollection_header);
        songcollection_header_detail = (RelativeLayout) findViewById(R.id.rl_songcollection_header_detail);

        albumart = (ImageView) findViewById(R.id.img_album_art);

        photo = (SimpleDraweeView) findViewById(R.id.sdv_songcollection_photo);

        songCollection_title = (TextView) findViewById(R.id.tv_songcollection_title);

        collection_layout = (LinearLayout) findViewById(R.id.ll_songcollection_collect);
        //share_layout = (LinearLayout) findViewById(R.id.ll_songcollection_share);
        dowm_layout = (LinearLayout) findViewById(R.id.ll_songcollection_down);

        collection_text = (TextView) findViewById(R.id.tv_songcollection_collect_state);

        recyclerView = (ObserableRecyclerViewWithEmptyView) findViewById(R.id.orv_songcollection);
        recyclerView.setScrollViewCallbacks(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        View emptyView = findViewById(R.id.id_empty_view);
        recyclerView.setEmptyView(emptyView);


        loadingView = findViewById(R.id.layout_loading_view);
        anim_image = (ImageView) findViewById(R.id.anim_image);

        //设置重试文本的格式，把重试两个字设为点击事件，点击开始重新读取列表
        SpannableString spanString = new SpannableString("请连接网络后点击重试");
        spanString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                loadingView.setVisibility(View.VISIBLE);
                try_again.setVisibility(View.INVISIBLE);
                presenter.onCreate(id);
            }
        }, 8, 10, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        try_again = (TextView) findViewById(R.id.tv_try_again);
        try_again.setText(spanString);
        try_again.setMovementMethod(LinkMovementMethod.getInstance());


        mActionBarSize = CommonUtils.getActionBarHeight(this);
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.FlexibleSpaceImageHeight);

        setToolbar();
        if(pic != null && pic.length() != 0) {
            setHeaderBlurBackground();
        }
        setHeaderView();


        presenter = new CreatedCollectionPresenter(this);
        presenter.onCreate(id);

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
        toolbar.setTitle("歌单");
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
        ImageRequest request = ImageRequest.fromUri(pic);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(request);
        BinaryResource binaryResource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
        File file = ((FileBinaryResource) binaryResource).getFile();

        if(file != null) {
            Bitmap HeaderBackground = ImageUtils.getCompassImage(file, 300, 300);
            albumart.setImageDrawable(ImageUtils.createBlurredImageFromBitmap(HeaderBackground, this));
        }

    }


    /**
     * 初始化头部的视图，歌单名，歌单图片，歌单收听人数，歌单详情
     */
    public void setHeaderView() {
        //若有歌单图片，则设置图片
        if(pic != null) {
            photo.setImageURI(Uri.parse(pic));
        }

        songCollection_title.setText(title);
    }



    protected void updateViews(int scrollY, boolean animated) {
        // Translate header
        ViewHelper.setTranslationY(songcollection_header, getHeaderTranslationY(scrollY));
    }

    protected float getHeaderTranslationY(int scrollY) {
        final int headerHeight = songcollection_header.getHeight();
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
            toolbar.setTitle(title);
            actionbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_background));
        }
        if (scrollY == 0) {
            toolbar.setTitle("歌单");
            toolbar.setSubtitle("");
            actionbar.setBackgroundDrawable(null);
        }
        if (scrollY > mFlexibleSpaceImageHeight - mActionBarSize - mStatusSize) {

        }

        float a = (float) scrollY / (mFlexibleSpaceImageHeight - mActionBarSize - mStatusSize);
        songcollection_header_detail.setAlpha(1f - a);

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }



    @Override
    public void setAdapter(final MusicAdapter adapter) {
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
            public void onMoreClick(View view, final int position) {
                LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.popupwindow_menu_songlistmore, null);

                TextView popuptitle = (TextView) layout.findViewById(R.id.tv_title_popupwindow);
                ListView popuplistview = (ListView) layout.findViewById(R.id.ls_songlistmore_popupwindow);

                //添加弹窗菜单数据源
                //获取当前点击的歌曲数据
                final MusicInfo musicInfo = (MusicInfo) adapter.getList().get(position - 1);
                popuptitle.setText("歌曲信息:");

                String albumname = musicInfo.getAlbumName();
                if(albumname == null || albumname.length() == 0) {
                    albumname = "暂无信息";
                }
                final PopUpWindowMenuAdapter adapter = new PopUpWindowMenuAdapter(CreatedCollectionActivity.this,
                        Arrays.asList( new LeftMenuItem(R.drawable.icon_music_name, "歌曲 —— " + musicInfo.getMusicName()),
                                new LeftMenuItem(R.drawable.icon_music_artist, "歌手 —— " + musicInfo.getArtist()),
                                new LeftMenuItem(R.drawable.icon_music_album, "专辑 —— " + albumname),
                                new LeftMenuItem(R.drawable.icon_music_cancelcollection, "取消收藏" )
                                ));
                popuplistview.setAdapter(adapter);
                //添加弹窗菜单点击事件


                popuplistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View childview, int childposition, long id) {

                        //点击第四个按钮，取消收藏按钮，弹框确认是否下载
                        if(childposition == 3) {
                            new AlertDialog.Builder(CreatedCollectionActivity.this).setTitle("确定取消收藏该歌曲吗？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //下载该歌曲
                                            presenter.peformCancelCollection(CreatedCollectionActivity.this.id, musicInfo);
                                            if (popupWindow != null) {
                                                popupWindow.dismiss();
                                            }
                                            //刷新界面
                                            presenter.onCreate(CreatedCollectionActivity.this.id);
                                            Toast.makeText(CreatedCollectionActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();

                        }
                    }
                });
                //初始化并弹出popupWindow
                popupWindow = CommonUtils.ShowPopUpWindow(CreatedCollectionActivity.this, popupWindow, view, layout);
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
