package com.example.administrator.audioplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.audioplayer.Ipresenter.IRecentPresenter;
import com.example.administrator.audioplayer.Iview.IRecentView;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.adapter.PopUpWindowMenuAdapter;
import com.example.administrator.audioplayer.bean.LeftMenuItem;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.presenterImp.RencentPresenter;
import com.example.administrator.audioplayer.utils.ActivityManager;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.example.administrator.audioplayer.widget.DividerItemDecoration;
import com.example.administrator.audioplayer.widget.RecycleViewWithEmptyView;

import java.util.Arrays;


/**
 * 最近播放界面
 */
public class RecentActivity extends BaseActivity implements IRecentView {

    private ActionBar ab;
    private Toolbar toolbar;
    private RecycleViewWithEmptyView rv;
    private IRecentPresenter presenter;

    private LayoutInflater mInflater;
    private PopupWindow popupWindow;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RecentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //把Activity放入ActivityManager中进行管理，方便一键退出所有
        ActivityManager.getInstance().pushOneActivity(this);

        setContentView(R.layout.activity_recent);

        mInflater = LayoutInflater.from(this);

        //初始化底部播放栏，由父类BaseActivity在onStart()中显示
        bottom_container_framelayout = (FrameLayout) findViewById(R.id.bottom_container);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar();


        rv = (RecycleViewWithEmptyView) findViewById(R.id.rv_recent_activity);

        View emptyview = findViewById(R.id.id_empty_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));


        rv.setEmptyView(emptyview);
        rv.setAdapter(new MusicAdapter(this, null));


        presenter = new RencentPresenter(this);
        presenter.onCreate();
    }


    /**
     * 初始化toolbar
     */
    private void setToolbar() {
        toolbar.setTitle("最近播放");
        setSupportActionBar(toolbar);

        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return true;
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
                Toast.makeText(RecentActivity.this, "更多", Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnMusicItemClickListener(new MusicAdapter.OnMusicItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //获取点击的MusicInfo实体类((LocalMusicAdapter)rv.getAdapter()).getItem(position)
                //调用presenter的方法，播放该实体代表的歌曲

                presenter.peformMusicClick(position);

                Toast.makeText(RecentActivity.this, ((MusicAdapter)rv.getAdapter()).getItem(position).getMusicName(), Toast.LENGTH_SHORT).show();
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
                //判断专辑名，如果是没有专辑名字的 在下载之后专辑会变成文件夹名字，所以这里多一步判断是否是文件夹名字
                if(albumname == null || albumname.length() == 0 || albumname.equals("audioplayer")) {
                    albumname = "暂无信息";
                }

                PopUpWindowMenuAdapter adapter = new PopUpWindowMenuAdapter(RecentActivity.this,
                        Arrays.asList( new LeftMenuItem(R.drawable.icon_music_name, "歌曲 —— " + musicInfo.getMusicName()),
                                new LeftMenuItem(R.drawable.icon_music_artist, "歌手 —— " + musicInfo.getArtist()),
                                new LeftMenuItem(R.drawable.icon_music_album, "专辑 —— " + albumname)
                        ));
                popuplistview.setAdapter(adapter);
                //添加弹窗菜单点击事件

                //初始化并弹出popupWindow
                popupWindow = CommonUtils.ShowPopUpWindow(RecentActivity.this, popupWindow, view, layout);

            }
        });

        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.onCreate();
    }

    @Override
    public void onMetaChange() {
        presenter.onCreate();
    }
}
