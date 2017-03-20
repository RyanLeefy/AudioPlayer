package com.example.administrator.audioplayer.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.activity.NewAlbumActivity;
import com.example.administrator.audioplayer.activity.SongCollectionActivity;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.adapter.PopUpWindowMenuAdapter;
import com.example.administrator.audioplayer.bean.LeftMenuItem;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.download.DownloadService;
import com.example.administrator.audioplayer.service.MusicPlayer;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.example.administrator.audioplayer.utils.GetDownloadLink;
import com.example.administrator.audioplayer.utils.PrintLog;
import com.example.administrator.audioplayer.utils.RequestThreadPool;
import com.example.administrator.audioplayer.widget.DividerItemDecoration;
import com.example.administrator.audioplayer.widget.RecycleViewWithEmptyView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * 网络搜索后第一个子fragment，显示搜索单曲
 */
public class AfterSearchMusicFragment extends BaseFragment {

    private Context mContext;


    private RecycleViewWithEmptyView recyclerView;

    private List mList;

    private LayoutInflater mInflater;

    private PopupWindow popupWindow;

    private MusicAdapter adapter;

    //recyclerView的排列方式
    private LinearLayoutManager linearLayoutManager;

    //recyclerView可见的最后一项，用来监听加载更多数据
    private int lastVisibleItem;

    private SearchMoreCallBack callBack;

    public void setCallBack(SearchMoreCallBack callBack) {
        this.callBack = callBack;
    }



    public static AfterSearchMusicFragment newInstance(ArrayList musicList) {
        AfterSearchMusicFragment fragment = new AfterSearchMusicFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("searchMusic", musicList);
        fragment.setArguments(bundle);
        return fragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }


    public void updateAdapter(List musicList) {
        //刷新adapter里面的List
        //然后adapter.notyfichange
        adapter.updateAdapterWithMoreList(musicList);
        adapter.notifyDataSetChanged();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_after_search_music, container, false);

        if (getArguments() != null) {
            mList = getArguments().getParcelableArrayList("searchMusic");
        }



        recyclerView = (RecycleViewWithEmptyView) view.findViewById(R.id.rv_after_search_music_fragment);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        View emptyView = view.findViewById(R.id.id_empty_view);
        recyclerView.setEmptyView(emptyView);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //滑动停下来的时候，而且可见的最后一项是最后一项，即FooterItem，则加载下一页数据
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    //如果item数量和list数量相同，则表示没有footeritem了，即没有更多的数据了，这时不用再请求
                    //不等于的话就可以请求下一页
                    if(!(adapter.getItemCount() == adapter.getList().size())) {
                        //当前页数
                        int pageNow = adapter.getItemCount()/10;
                        //读取下一页
                        callBack.searchMoreMusic(pageNow + 1);
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //滑动的时候记录最后一项的位置
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        adapter = new MusicAdapter(mContext, mList);
        adapter.setHasFooter(true);
        adapter.setOnMusicItemClickListener(new MusicAdapter.OnMusicItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //播放当前歌曲
                List playList = new ArrayList();
                playList.add(mList.get(position));
                MusicPlayer.playAll(playList, 0, false);
            }

            @Override
            public void onMoreClick(View view, final int position) {
                LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.popupwindow_menu_songlistmore, null);

                TextView popuptitle = (TextView) layout.findViewById(R.id.tv_title_popupwindow);
                ListView popuplistview = (ListView) layout.findViewById(R.id.ls_songlistmore_popupwindow);

                //添加弹窗菜单数据源
                //获取当前点击的歌曲数据
                final MusicInfo musicInfo = (MusicInfo) adapter.getList().get(position);
                popuptitle.setText("歌曲信息:");

                String albumname = musicInfo.getAlbumName();
                if(albumname == null || albumname.length() == 0) {
                    albumname = "暂无信息";
                }
                PopUpWindowMenuAdapter adapter = new PopUpWindowMenuAdapter(getActivity(),
                        Arrays.asList( new LeftMenuItem(R.drawable.icon_music_name, "歌曲 —— " + musicInfo.getMusicName()),
                                new LeftMenuItem(R.drawable.icon_music_artist, "歌手 —— " + musicInfo.getArtist()),
                                new LeftMenuItem(R.drawable.icon_music_album, "专辑 —— " + albumname),
                                new LeftMenuItem(R.drawable.icon_music_download, "下载歌曲" )
                        ));
                popuplistview.setAdapter(adapter);
                //添加弹窗菜单点击事件

                popuplistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View childview, int childposition, long id) {

                        //点击第四个按钮，下载按钮，弹框确认是否下载
                        if(childposition == 3) {
                            new AlertDialog.Builder(getActivity()).setTitle("确定下载该歌曲吗？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //下载该歌曲
                                            performDownLoadMusicClick(position);
                                            if (popupWindow != null) {
                                                popupWindow.dismiss();
                                            }
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
                popupWindow = CommonUtils.ShowPopUpWindow(getActivity(), popupWindow, view, layout);
            }
        });
        recyclerView.setAdapter(adapter);


        return view;
    }


    /**
     * 下载单首歌曲
     * @param position
     */
    public void performDownLoadMusicClick(final int position) {
        final List list = adapter.getList();
        final String[] names = new String[list.size()];
        final String[] artists = new String[list.size()];
        final ArrayList<String> urls = new ArrayList<String>();


        this.addSubscription(
                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {

                        RequestThreadPool.post(new GetDownloadLink(list, position, names, artists, urls, 1));


                        //等待所有请求结束
                        int tryCount = 0;
                        while (urls.size() != 1 && tryCount < 1000) {
                            tryCount++;
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        PrintLog.e("urls.size() = " + urls.size());

                        subscriber.onNext("onNext");
                        subscriber.onCompleted();
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {}

                            @Override
                            public void onError(Throwable e) {
                                Logger.e(e.toString());
                            }

                            @Override
                            public void onNext(String s) {
                                //调用后台服务开始下载
                                Intent intent = new Intent();
                                intent.setAction(DownloadService.ADD_DOWNTASK);
                                intent.putExtra("name", names[0]);
                                intent.putExtra("artist", artists[0]);
                                intent.putExtra("url", urls.get(0));
                                intent.setPackage(DownloadService.PACKAGE);
                                getActivity().startService(intent);
                            }
                        })
        );
    }


}
