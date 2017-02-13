package com.example.administrator.audioplayer.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.activity.LocalMusicActivity;
import com.example.administrator.audioplayer.activity.RecentActivity;
import com.example.administrator.audioplayer.adapter.PopUpWindowMenuAdapter;
import com.example.administrator.audioplayer.adapter.SongListAdapter;
import com.example.administrator.audioplayer.bean.LeftMenuItem;
import com.example.administrator.audioplayer.bean.MusicFragmengSongCollectionItem;
import com.example.administrator.audioplayer.bean.MusicFragmentExpandItem;
import com.example.administrator.audioplayer.bean.MusicFragmentHeaderItem;
import com.example.administrator.audioplayer.db.RecentMusicDB;
import com.example.administrator.audioplayer.modelImp.LocalMusicModel;
import com.example.administrator.audioplayer.widget.DividerItemDecoration;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 主界面第二个fragment界面，本地音乐信息
 * Created on 2017/1/22.
 */

public class MusicFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Context mContext;
    private LayoutInflater mInflater;
    private PopupWindow popupWindow;


    //所有数据源，即需要放入recycleview中的所有item
    private List allItems;


    //收藏歌单栏展开否
    public boolean collectExpanded = true;



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

        //TODO 获取并填充数据
        //先获取Header数据，Expand数据，create歌单数据和collect歌单数据
        //初始化SongListAdapter,
        //recyclerView.setAdapter();
        LocalMusicModel model = new LocalMusicModel();
        int num_local_music = model.getLocalMusic().size();

        //TODO 在子线程中读取
        //int num_recent_music = RecentMusicDB.getInstance(MyApplication.getContext()).queryRecentIds(null).size();

        //模拟数据
        List<MusicFragmentHeaderItem> headerItemList = new ArrayList<>(
                Arrays.asList(new MusicFragmentHeaderItem(R.drawable.music_icn_local, "本地播放", num_local_music),
                        new MusicFragmentHeaderItem(R.drawable.music_icn_recent, "最近播放", 0),
                        new MusicFragmentHeaderItem(R.drawable.music_icn_download, "下载管理", 0),
                        new MusicFragmentHeaderItem(R.drawable.music_icn_artist, "我的歌手", 0)));



        final List<MusicFragmengSongCollectionItem> create_songCollectionItems = new ArrayList<>();
        create_songCollectionItems.add(new MusicFragmengSongCollectionItem(R.drawable.cover_faveriate_songcollection, "我喜欢的音乐", 0));
        create_songCollectionItems.add(new MusicFragmengSongCollectionItem(R.drawable.cover_faveriate_songcollection, "粤语歌单", 0));

        final List<MusicFragmengSongCollectionItem> collect_songCollectionItems = new ArrayList<>();
        collect_songCollectionItems.add(new MusicFragmengSongCollectionItem(R.drawable.cover_faveriate_songcollection, "我收藏的音乐", 0));
        collect_songCollectionItems.add(new MusicFragmengSongCollectionItem(R.drawable.cover_faveriate_songcollection, "英文歌", 0));

        final List<MusicFragmentExpandItem> expandItemList = new ArrayList<>(
                Arrays.asList(new MusicFragmentExpandItem(MusicFragmentExpandItem.TYPE_CREATE, "创建的歌单", create_songCollectionItems.size()),
                        new MusicFragmentExpandItem(MusicFragmentExpandItem.TYPE_COLLECT, "收藏的歌单", collect_songCollectionItems.size())));

        allItems = new ArrayList();
        allItems.addAll(headerItemList);
        allItems.add(expandItemList.get(0));
        allItems.addAll(create_songCollectionItems);
        allItems.add(expandItemList.get(1));
        allItems.addAll(collect_songCollectionItems);


        final SongListAdapter adapter = new SongListAdapter(mContext, allItems);

        //给headeritem添加点击事件
        adapter.setOnHeaderItemClickListener(new SongListAdapter.OnHeaderItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        //跳转到LocalMusicActivity
                        LocalMusicActivity.startActivity(mContext);
                        Toast.makeText(mContext, "本地播放", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        //跳转到RecentActivity
                        RecentActivity.startActivity(mContext);
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
        //给expanditem添加点击事件
        adapter.setOnExpandItemClickListener(new SongListAdapter.OnExpandItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {

                SongListAdapter.ItemViewTag itemViewTag = (SongListAdapter.ItemViewTag)holder;
                ObjectAnimator anim = ObjectAnimator.ofFloat(itemViewTag.arrow, "rotation", 90, 0);
                anim.setDuration(100);
                anim.setRepeatCount(0);
                anim.setInterpolator(new LinearInterpolator());

                if (itemViewTag.getItemViewType() == 2) {            //判断类型，创建的歌单
                        //已展开则合上
                    if (itemViewTag.createdExpanded) {
                        anim.start();
                        //TODO 刷新列表
                        allItems.removeAll(create_songCollectionItems);
                        //adapter.removeCollecteSongList(collect_songCollectionItems);
                        itemViewTag.createdExpanded = false;
                    } else {
                        //未展开则展开
                        //TODO 刷新列表
                        anim.reverse();
                        //这里要分情况，分收藏的歌单是否打开
                        //若打开了，则要先去掉收藏的歌单，最后再添加上  否则不用处理收藏的歌单
                        //这里根据本地的collectExpanded变量来进行判断，因为无法获取到下一个expanditem里面的collectExpanded状态
                        if(collectExpanded) {
                            allItems.removeAll(collect_songCollectionItems);
                            allItems.remove(expandItemList.get(1));
                            allItems.addAll(create_songCollectionItems);
                            allItems.add(expandItemList.get(1));
                            allItems.addAll(collect_songCollectionItems);
                        } else {
                            allItems.remove(expandItemList.get(1));
                            allItems.addAll(create_songCollectionItems);
                            allItems.add(expandItemList.get(1));
                        }
                        itemViewTag.createdExpanded = true;
                    }
                } else if (itemViewTag.getItemViewType() == 3) {     //判断类型，收藏的歌单
                    //已展开则合上
                    //此处把collectExpanded状态存至本地变量中
                    collectExpanded = itemViewTag.collectExpanded;
                    if (collectExpanded) {
                        anim.start();
                        //TODO 刷新列表
                        allItems.removeAll(collect_songCollectionItems);
                        itemViewTag.collectExpanded = false;
                        //同步本地状态
                        collectExpanded = false;
                    } else {
                        //未展开则展开
                        anim.reverse();
                        //TODO 刷新列表
                        allItems.addAll(collect_songCollectionItems);
                        itemViewTag.collectExpanded = true;
                        //同步本地状态
                        collectExpanded = true;
                    }
                }
                adapter.updateAdapter(allItems);   //更新数据
                adapter.notifyDataSetChanged();    //刷新界面
            }

            @Override
            public void onMoreClick(View view, int position) {
                LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.popupwindow_menu_songlistmore, null);

                TextView popuptitle = (TextView) layout.findViewById(R.id.tv_title_popupwindow);
                ListView popuplistview = (ListView) layout.findViewById(R.id.ls_songlistmore_popupwindow);

                //添加弹窗菜单数据源
                MusicFragmentExpandItem musicFragmentExpandItem = (MusicFragmentExpandItem) allItems.get(position);
                popuptitle.setText(musicFragmentExpandItem.getTitle());
                final PopUpWindowMenuAdapter adapter = new PopUpWindowMenuAdapter(mContext,
                        Arrays.asList(new LeftMenuItem(R.drawable.popupwindow_menu_createlist, "创建新歌单")));
                popuplistview.setAdapter(adapter);
                //添加弹窗菜单点击事件
                popuplistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //点击创建新歌单
                        if(position == 0) {
                            Toast.makeText(mContext, "创建新歌单", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //初始化并弹出popupWindow
                ShowPopUpWindow(view, layout);
            }
        });
        //给歌单添加点击事件
        adapter.setOnSongCollectionItemClickListener(new SongListAdapter.OnSongCollectionItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //跳转到歌单里面
                MusicFragmengSongCollectionItem songCollectionItem = (MusicFragmengSongCollectionItem)allItems.get(position);
                Toast.makeText(mContext, "进入歌单:" + songCollectionItem.getName(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onMoreClick(View view, final int position){
                LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.popupwindow_menu_songlistmore, null);

                TextView popuptitle = (TextView) layout.findViewById(R.id.tv_title_popupwindow);
                ListView popuplistview = (ListView) layout.findViewById(R.id.ls_songlistmore_popupwindow);

                //添加弹窗菜单数据源
                MusicFragmengSongCollectionItem create_songCollectionItems = (MusicFragmengSongCollectionItem) allItems.get(position);
                popuptitle.setText("歌单:  " + create_songCollectionItems.getName());
                PopUpWindowMenuAdapter adapter = new PopUpWindowMenuAdapter(mContext,
                        Arrays.asList( new LeftMenuItem(R.drawable.popupwindow_menu_delete, "删除"),
                                new LeftMenuItem(R.drawable.popupwindow_menu_manage, "编辑歌单信息")));
                popuplistview.setAdapter(adapter);
                //添加弹窗菜单点击事件
                popuplistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View childview, int childposition, long id) {
                        //点击创建新歌单
                        MusicFragmengSongCollectionItem songCollectionItem = (MusicFragmengSongCollectionItem)allItems.get(position);
                        if(childposition == 0) {
                            Toast.makeText(mContext, "删除:" + songCollectionItem.getName(), Toast.LENGTH_SHORT).show();
                        } else if (childposition == 1) {
                            Toast.makeText(mContext, "编辑歌单信息:" + songCollectionItem.getName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //初始化并弹出popupWindow
                ShowPopUpWindow(view, layout);
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
        //popupWindow.setOutsideTouchable(true);

        popupWindow.setBackgroundDrawable(new ColorDrawable());


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //popupwindow消失的时候恢复成原来的透明度
                backgroundAlpha(1f);
            }
        });

        backgroundAlpha(0.7f);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
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

}
