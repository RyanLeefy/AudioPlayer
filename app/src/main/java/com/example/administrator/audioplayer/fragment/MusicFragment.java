package com.example.administrator.audioplayer.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.PopUpWindowMenuAdapter;
import com.example.administrator.audioplayer.adapter.SongListAdapter;
import com.example.administrator.audioplayer.bean.LeftMenuItem;
import com.example.administrator.audioplayer.bean.MusicFragmengSongCollectionItem;
import com.example.administrator.audioplayer.bean.MusicFragmentExpandItem;
import com.example.administrator.audioplayer.bean.MusicFragmentHeaderItem;
import com.example.administrator.audioplayer.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 主界面第二个fragment界面，本地音乐信息
 * Created on 2017/1/22.
 */

public class MusicFragment extends MyFragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Context mContext;
    private LayoutInflater mInflater;
    private PopupWindow popupWindow;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_music, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_musicfragment);


        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //模拟请求，两秒后取消
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));


        //先获取Header数据，Expand数据，create歌单数据和collect歌单数据
        //初始化SongListAdapter,
        //recyclerView.setAdapter();


        //模拟数据
        List<MusicFragmentHeaderItem> headerItemList = new ArrayList<>(
                Arrays.asList(new MusicFragmentHeaderItem(R.drawable.music_icn_local, "本地播放", 0),
                        new MusicFragmentHeaderItem(R.drawable.music_icn_recent, "最近播放", 0),
                        new MusicFragmentHeaderItem(R.drawable.music_icn_download, "下载管理", 0),
                        new MusicFragmentHeaderItem(R.drawable.music_icn_artist, "我的歌手", 0)));

        List<MusicFragmentExpandItem> expandItemList = new ArrayList<>(
                Arrays.asList(new MusicFragmentExpandItem(1, "创建的歌单",1),
                        new MusicFragmentExpandItem(2, "收藏的歌单",0)));

        List<MusicFragmengSongCollectionItem> songCollectionItems = new ArrayList<>();
        songCollectionItems.add(new MusicFragmengSongCollectionItem(R.drawable.cover_faveriate_songcollection, "我喜欢的音乐", 0));

        final List allItems = new ArrayList();
        allItems.addAll(headerItemList);
        allItems.add(expandItemList.get(0));
        allItems.addAll(songCollectionItems);
        allItems.add(expandItemList.get(1));


        SongListAdapter adapter = new SongListAdapter(mContext, headerItemList,expandItemList,songCollectionItems,null);
        adapter.setOnHeaderItemClickListener(new SongListAdapter.OnHeaderItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        //跳转到LocalMusicActivity
                        Toast.makeText(mContext, "本地播放", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        //跳转到RecentActivity
                        Toast.makeText(mContext, "最近播放", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        //跳转到DownActivity
                        Toast.makeText(mContext, "下载管理", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        //跳转到MyArtistActivity
                        Toast.makeText(mContext, "我的歌手", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        adapter.setOnExpandItemClickListener(new SongListAdapter.OnExpandItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onMoreClick(View view, int position) {
                LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.popupwindow_menu_songlistmore, null);
                ShowPopUpWindow(view, layout);
                TextView popuptitle = (TextView) layout.findViewById(R.id.tv_title_popupwindow);
                ListView popuplistview = (ListView) layout.findViewById(R.id.ls_songlistmore_popupwindow);
                MusicFragmentExpandItem musicFragmentExpandItem = (MusicFragmentExpandItem) allItems.get(position);
                popuptitle.setText(musicFragmentExpandItem.getTitle());
                PopUpWindowMenuAdapter adapter = new PopUpWindowMenuAdapter(mContext,
                        Arrays.asList(new LeftMenuItem(R.drawable.popupwindow_menu_createlist, "创建新歌单")));
                popuplistview.setAdapter(adapter);
            }
        });

        adapter.setOnSongCollectionItemClickListener(new SongListAdapter.OnSongCollectionItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //跳转到歌单里面
                Toast.makeText(mContext, "进入歌单",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onMoreClick(View view, int position){
                LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.popupwindow_menu_songlistmore, null);
                ShowPopUpWindow(view, layout);
                TextView popuptitle = (TextView) layout.findViewById(R.id.tv_title_popupwindow);
                ListView popuplistview = (ListView) layout.findViewById(R.id.ls_songlistmore_popupwindow);
                MusicFragmengSongCollectionItem create_songCollectionItems = (MusicFragmengSongCollectionItem) allItems.get(position);
                popuptitle.setText("歌单:  " + create_songCollectionItems.getName());
                PopUpWindowMenuAdapter adapter = new PopUpWindowMenuAdapter(mContext,
                        Arrays.asList( new LeftMenuItem(R.drawable.popupwindow_menu_delete, "删除"),
                                new LeftMenuItem(R.drawable.popupwindow_menu_manage, "编辑歌单信息")));
                popuplistview.setAdapter(adapter);

            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));


        return v;
    }


    /**
     * @param view   要出现的位置相关的view
     * @param layout popupwindow的样式
     */
    public void ShowPopUpWindow(View view, LinearLayout layout) {
        //初始化并弹出popupWindow
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }

        popupWindow = new PopupWindow(layout,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.Popupwindow);//包括进入和退出两个动画

        //popupwindow获取焦点，点击其他地方消失
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        backgroundAlpha(0.7f);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //popupwindow消失的时候恢复成原来的透明度
                backgroundAlpha(1f);
            }
        });
    }


    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }


    //在activity中的onBackPress()中调用
    @Override
    public boolean onBackPressed() {
        //可以关闭popupwindow的话返回true，否则返回false
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
            return true;
        } else {
            return false;
        }
    }
}
