package com.example.administrator.audioplayer.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.example.administrator.audioplayer.Ipresenter.INewAlbumPresenter;
import com.example.administrator.audioplayer.Iview.INewAlbumView;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.adapter.PopUpWindowMenuAdapter;
import com.example.administrator.audioplayer.bean.LeftMenuItem;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.db.SongCollectionDB;
import com.example.administrator.audioplayer.fragment.ChooseCollectionFragment;
import com.example.administrator.audioplayer.presenterImp.NewAlbumPresenter;
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

/**
 * Created by on 2017/2/23 0023.
 */

public class NewAlbumActivity extends BaseActivity implements ObservableScrollViewCallbacks,INewAlbumView {

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
    private FrameLayout newalbum_header;
    //头部内容框
    private RelativeLayout newalbum_header_detail;
    //headerView的背景图片
    private ImageView albumart;
    //专辑封面图片
    private SimpleDraweeView photo;
    //专辑名
    private TextView newAlbum_title;
    //歌手名
    private TextView newAlbum_author;
    //发布时间
    private TextView newAlbum_publishtime;
    
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

    private INewAlbumPresenter presenter;


    //创建activity时需要传入的数据
    //专辑id
    private String albumid;
    //图片地址
    private String pic;
    //专辑名
    private String title;
    //歌手名
    private String author;
    //发布时间
    private String publishtime;


    /**
     * 创建Activity，要传入需要的数据
     * @param context
     * @param albumid
     * @param pic
     * @param title
     * @param author
     * @param publishtime
     */
    public static void startActivity(Context context, String albumid, String pic,
                                     String title, String author, String publishtime) {
        Intent intent = new Intent(context, NewAlbumActivity.class);
        intent.putExtra("albumid", albumid);
        intent.putExtra("pic", pic);
        intent.putExtra("title", title);
        intent.putExtra("author", author);
        intent.putExtra("publishtime", publishtime);
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

        setContentView(R.layout.activity_new_album);

        mInflater = LayoutInflater.from(this);

        //初始化底部播放栏，由父类BaseActivity在onStart()中显示
        bottom_container_framelayout = (FrameLayout) findViewById(R.id.bottom_container);

        //读取传入的数据
        if(getIntent().getExtras() != null) {
            albumid = getIntent().getStringExtra("albumid");
            pic = getIntent().getStringExtra("pic");
            title = getIntent().getStringExtra("title");
            author = getIntent().getStringExtra("author");
            publishtime = getIntent().getStringExtra("publishtime");
        }

        toolbar = (Toolbar) findViewById(R.id.tb_newalbum);

        newalbum_header = (FrameLayout) findViewById(R.id.fl_newalbum_header);
        newalbum_header_detail = (RelativeLayout) findViewById(R.id.rl_newalbum_header_detail);

        albumart = (ImageView) findViewById(R.id.img_album_art);
        
        photo = (SimpleDraweeView) findViewById(R.id.sdv_newalbum_photo);

        newAlbum_title = (TextView) findViewById(R.id.tv_newalbum_title);
        newAlbum_author = (TextView) findViewById(R.id.tv_newalbum_author);
        newAlbum_publishtime = (TextView) findViewById(R.id.tv_newalbum_publistime);


        collection_layout = (LinearLayout) findViewById(R.id.ll_newalbum_collect);
        //share_layout = (LinearLayout) findViewById(R.id.ll_newalbum_share);
        dowm_layout = (LinearLayout) findViewById(R.id.ll_newalbum_down);

        collection_text = (TextView) findViewById(R.id.tv_newalbum_collect_state);

        recyclerView = (ObserableRecyclerViewWithEmptyView) findViewById(R.id.orv_newalbum);
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
                presenter.onCreate(albumid);
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
        setCollection();

        presenter = new NewAlbumPresenter(this);
        presenter.onCreate(albumid);

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
        toolbar.setTitle("专辑");
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
     * 初始化头部的视图，专辑名，专辑图片，专辑收听人数，专辑详情
     */
    public void setHeaderView() {
        
        //若有专辑图片，则设置图片
        if(pic != null) {
            photo.setImageURI(Uri.parse(pic));
        }

        newAlbum_title.setText(title);
        newAlbum_author.setText("歌手：" + author);
        newAlbum_publishtime.setText("发布时间：" + publishtime);
        
    }


    /**
     * 初始化头部的监听事件，收藏，分享，评论，下载
     */
    public void setHeaderViewListener() {

        //收藏按钮监听
        collection_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(presenter.peformCollect(albumid, title, adapter.getList().size(), pic, author, publishtime)) {
                    //收藏
                    //文字变已收藏
                    collection_text.setText("已收藏");
                    Toast.makeText(NewAlbumActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                } else {
                    //取消收藏
                    //文字变收藏
                    collection_text.setText("收藏");
                    Toast.makeText(NewAlbumActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //分享按钮监听
        /*
        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        //下载按钮监听
        dowm_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NewAlbumActivity.this).setTitle("下载该歌单内所有歌曲？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //下载所有歌曲
                                presenter.performDownLoadAllClick();
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
        });
    }


    public void setCollection() {
        int result = SongCollectionDB.getInstance(MyApplication.getContext()).isCollected(albumid);
        if(result != -1) {
            //已经收藏，则显示已收藏
            collection_text.setText("已收藏");
        }
    }

    protected void updateViews(int scrollY, boolean animated) {
        // Translate header
        ViewHelper.setTranslationY(newalbum_header, getHeaderTranslationY(scrollY));
    }



    protected float getHeaderTranslationY(int scrollY) {
        final int headerHeight = newalbum_header.getHeight();
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
            toolbar.setSubtitle(author);
            actionbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_background));
        }
        if (scrollY == 0) {
            toolbar.setTitle("专辑");
            toolbar.setSubtitle("");
            actionbar.setBackgroundDrawable(null);
        }
        if (scrollY > mFlexibleSpaceImageHeight - mActionBarSize - mStatusSize) {

        }

        float a = (float) scrollY / (mFlexibleSpaceImageHeight - mActionBarSize - mStatusSize);
        newalbum_header_detail.setAlpha(1f - a);

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
                PopUpWindowMenuAdapter adapter = new PopUpWindowMenuAdapter(NewAlbumActivity.this,
                        Arrays.asList( new LeftMenuItem(R.drawable.icon_music_name, "歌曲 —— " + musicInfo.getMusicName()),
                                new LeftMenuItem(R.drawable.icon_music_artist, "歌手 —— " + musicInfo.getArtist()),
                                new LeftMenuItem(R.drawable.icon_music_album, "专辑 —— " + albumname),
                                new LeftMenuItem(R.drawable.icon_music_collect, "收藏到歌单"),
                                new LeftMenuItem(R.drawable.icon_music_download, "下载歌曲" )
                        ));
                popuplistview.setAdapter(adapter);
                //添加弹窗菜单点击事件

                popuplistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View childview, int childposition, long id) {


                        //点击第4个按钮，收藏到歌单，弹出fragment选择添加到的歌单
                        if(childposition == 3) {
                            //TODO 收藏
                            if (popupWindow != null) {
                                popupWindow.dismiss();
                            }
                            ChooseCollectionFragment chooseCollectionFragment = ChooseCollectionFragment.newInstance(musicInfo);
                            chooseCollectionFragment.show(getSupportFragmentManager(), "chooseCollection");
                        }

                        //点击第5个按钮，下载按钮，弹框确认是否下载
                        if(childposition == 4) {
                            new AlertDialog.Builder(NewAlbumActivity.this).setTitle("确定下载该歌曲吗？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //下载该歌曲
                                            presenter.performDownLoadMusicClick(position - 1);
                                            if (popupWindow != null) {
                                                popupWindow.dismiss();
                                            }
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            if (popupWindow != null) {
                                                popupWindow.dismiss();
                                            }
                                        }
                                    })
                                    .show();

                        }
                    }
                });
                //初始化并弹出popupWindow
                popupWindow = CommonUtils.ShowPopUpWindow(NewAlbumActivity.this, popupWindow, view, layout);
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
