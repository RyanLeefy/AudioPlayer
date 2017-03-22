package com.example.administrator.audioplayer.fragment;


import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.audioplayer.Ipresenter.IRecommendPresenter;
import com.example.administrator.audioplayer.Iview.IRecommendView;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.activity.NewAlbumActivity;
import com.example.administrator.audioplayer.activity.SongCollectionActivity;
import com.example.administrator.audioplayer.adapter.RecommendNewAlbumAdapter;
import com.example.administrator.audioplayer.adapter.RecommendSongCollectionAdapter;
import com.example.administrator.audioplayer.presenterImp.RecommendPresenter;
import com.example.administrator.audioplayer.widget.CarouselFigureView;

/**
 * Netfragment下的新曲推荐fragment
 *
 */
public class RecommendFragment extends BaseFragment implements IRecommendView {

    private Context mContext;
    private LayoutInflater mInflater;


    //放轮播图的layout
    private LinearLayout cfvlayout;

    //轮播图
    private CarouselFigureView cfv;

    //添加内容的layout,里面动态各种view，如歌单view，唱片view
    private LinearLayout mContentlayout;

    //推荐歌单view
    private View mSongColletionView;

    //推荐歌单更多
    private TextView more;

    //新专辑上架view
    private View mNewAlbumView;

    private RecyclerView mSongCollectionRecycle, mNewAlbumRecycle;

    private GridLayoutManager gridLayoutManager;

    private IRecommendPresenter presenter;

    private ChangeViewPagerCallBack callBack;

    public void setChangeViewPagerCallBack(ChangeViewPagerCallBack callBack) {
        this.callBack = callBack;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recommend, container, false);

        cfvlayout = (LinearLayout) view.findViewById(R.id.cfvly_recommend_fragment);

        //获取屏幕的宽度
        WindowManager wm = getActivity().getWindowManager();
        Point outSize = new Point();
        wm.getDefaultDisplay().getSize(outSize);
        //设置轮播图的大小，宽度为屏幕的宽度，高度为宽度/2.5
        int width = outSize.x;
        int height = (int)(width / 2.5);

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(width, height);
        cfv = new CarouselFigureView(mContext);
        cfv.setLayoutParams(params);

        //把轮播图添加到轮播图的layout中
        cfvlayout.addView(cfv);

        //内容布局
        mContentlayout = (LinearLayout) view.findViewById(R.id.ly_recommend_fragment);


        //初始化推荐歌单模块
        mSongColletionView = mInflater.inflate(R.layout.layout_recommend_songcollection_view, container, false);
        //歌单更多按钮,点击跳转到歌单页
        more = (TextView) mSongColletionView.findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBack != null) {
                    callBack.changeViewPagerTo(1);
                }
            }
        });

        mSongCollectionRecycle = (RecyclerView) mSongColletionView.findViewById(R.id.recommend_playlist_recyclerview);
        //recycle排列方式，表格排列，一行3个
        gridLayoutManager = new GridLayoutManager(mContext, 3);
        mSongCollectionRecycle.setLayoutManager(gridLayoutManager);
        mSongCollectionRecycle.setHasFixedSize(true);
        //设置不可滚动
        mSongCollectionRecycle.setNestedScrollingEnabled(false);


        //初始化新专辑上架模块
        mNewAlbumView = mInflater.inflate(R.layout.layout_recommend_newalbum_view, container, false);
        mNewAlbumRecycle = (RecyclerView) mNewAlbumView.findViewById(R.id.recommend_newalbums_recyclerview);
        //recycle排列方式，表格排列，一行3个
        gridLayoutManager = new GridLayoutManager(mContext, 3);
        mNewAlbumRecycle.setLayoutManager(gridLayoutManager);
        mNewAlbumRecycle.setHasFixedSize(true);
        //设置不可滚动
        mNewAlbumRecycle.setNestedScrollingEnabled(false);



        //往内容布局添加推荐歌单模块
        mContentlayout.addView(mSongColletionView);
        //往内容布局添加新专辑上架模块
        mContentlayout.addView(mNewAlbumView);


        presenter = new RecommendPresenter(this);
        presenter.onCreateView();

        return view;
    }


    @Override
    public final void onDestroyView() {
        super.onDestroyView();
        cfv.onDestroy();
    }


    @Override
    public void setRecommendSongCollectionAdapter(RecommendSongCollectionAdapter adapter) {
        adapter.setOnItemClickListener(new RecommendSongCollectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, int position) {
                RecommendSongCollectionAdapter.ViewHolder holder = ((RecommendSongCollectionAdapter.ViewHolder)viewHolder);
                SongCollectionActivity.startActivity(MyApplication.getContext(), false,
                        holder.listid, holder.pic, holder.listenum, holder.title, holder.tag);
            }
        });
        mSongCollectionRecycle.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setRecommendNewAlbumAdapter(RecommendNewAlbumAdapter albumAdapter) {
        albumAdapter.setOnItemClickListener(new RecommendNewAlbumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, int position) {
                RecommendNewAlbumAdapter.ViewHolder holder = ((RecommendNewAlbumAdapter.ViewHolder)viewHolder);
                NewAlbumActivity.startActivity(MyApplication.getContext(),
                        holder.albumid, holder.pic, holder.title, holder.author, holder.publishtime);
            }
        });
        mNewAlbumRecycle.setAdapter(albumAdapter);
        albumAdapter.notifyDataSetChanged();
    }
}
