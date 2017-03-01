package com.example.administrator.audioplayer.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.service.MusicPlayer;
import com.example.administrator.audioplayer.widget.DividerItemDecoration;
import com.example.administrator.audioplayer.widget.RecycleViewWithEmptyView;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络搜索后第一个子fragment，显示搜索单曲
 */
public class AfterSearchMusicFragment extends BaseFragment {

    private Context mContext;


    private RecycleViewWithEmptyView recyclerView;

    private List mList;

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
            public void onMoreClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);


        return view;
    }



}
