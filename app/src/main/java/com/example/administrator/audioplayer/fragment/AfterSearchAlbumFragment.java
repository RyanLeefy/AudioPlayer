package com.example.administrator.audioplayer.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.activity.NewAlbumActivity;
import com.example.administrator.audioplayer.adapter.SearchAlbumAdapter;
import com.example.administrator.audioplayer.widget.DividerItemDecoration;
import com.example.administrator.audioplayer.widget.RecycleViewWithEmptyView;

import java.util.ArrayList;
import java.util.List;


/**
 * 网络搜索后第二个子fragment，显示搜索专辑
 */
public class AfterSearchAlbumFragment extends BaseFragment {

    private Context mContext;

    private RecycleViewWithEmptyView recyclerView;

    private List mList;

    private SearchAlbumAdapter adapter;

    //recyclerView的排列方式
    private LinearLayoutManager linearLayoutManager;

    //recyclerView可见的最后一项，用来监听加载更多数据
    private int lastVisibleItem;

    private SearchMoreCallBack callBack;

    public void setCallBack(SearchMoreCallBack callBack) {
        this.callBack = callBack;
    }



    public static AfterSearchAlbumFragment newInstance(ArrayList albumList) {
        AfterSearchAlbumFragment fragment = new AfterSearchAlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("searchAlbum", albumList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    public void updateAdapter(List albumList) {
        //刷新adapter里面的List
        //然后adapter.notyfichange
        adapter.updateAdapterWithMoreList(albumList);
        adapter.notifyDataSetChanged();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_after_search_album, container, false);

        if (getArguments() != null) {
            mList = getArguments().getParcelableArrayList("searchAlbum");
        }


        recyclerView = (RecycleViewWithEmptyView) view.findViewById(R.id.rv_after_search_album_fragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);

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
                        int pageNow = adapter.getItemCount() / 10;
                        //读取下一页
                        callBack.searchMoreAlbum(pageNow + 1);
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

        adapter = new SearchAlbumAdapter(mContext, mList);
        adapter.setOnItemClickListener(new SearchAlbumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, int position) {
                SearchAlbumAdapter.CommonItemViewHolder holder = ((SearchAlbumAdapter.CommonItemViewHolder)viewHolder);
                NewAlbumActivity.startActivity(MyApplication.getContext(),
                        holder.albumid, holder.pic, holder.title, holder.author, holder.publishtime);
            }
        });
        recyclerView.setAdapter(adapter);


        return view;
    }


}
