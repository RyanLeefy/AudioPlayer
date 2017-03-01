package com.example.administrator.audioplayer.fragment;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.audioplayer.Ipresenter.INetSongListPresenter;
import com.example.administrator.audioplayer.Iview.INetSongListView;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.activity.SongCollectionActivity;
import com.example.administrator.audioplayer.adapter.NetSongListAdapter;
import com.example.administrator.audioplayer.presenterImp.NetSongListPresenter;

import java.util.List;

/**
 * Netfragment下的歌单fragment
 *
 */
public class NetSongListFragment extends BaseFragment implements INetSongListView {

    private Context mContext;

    private LayoutInflater mInflater;

    //最外层容器，用来转载懒加载的内容
    private FrameLayout lazyload_container;
    //是否已经加载过
    private Boolean isInit = false;
    //加载中动画视图
    private View loadingView;
    //加载动画视图中的动态ImageView
    private ImageView anim_image;
    //网络失败重试TextView
    private TextView try_again;

    private RecyclerView recyclerView;

    private NetSongListAdapter adapter;
    //recyclerView的排列方式
    private GridLayoutManager gridLayoutManager;

    //recyclerView可见的最后一项，用来监听加载更多数据
    private int lastVisibleItem;

    private INetSongListPresenter presenter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //读取懒加载框架，内容容器
        View view = inflater.inflate(R.layout.fragment_lazyload_container, container, false);
        lazyload_container = (FrameLayout) view.findViewById(R.id.lazyload_container);
        return view;
    }


    /**
     * 重写该方法，实现fragment的延时加载，只有当显示的时候才开始数据的加载
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isInit) {
            //视图放入懒加载容器当中
            View view = mInflater.inflate(R.layout.fragment_net_song_list, lazyload_container, true);
            loadingView = view.findViewById(R.id.layout_loading_view);

            //加载画面的帧动画
            anim_image = (ImageView) view.findViewById(R.id.anim_image);
            anim_image.setBackgroundResource(R.drawable.loading_animation);
            AnimationDrawable anim = (AnimationDrawable) anim_image.getBackground();
            anim.start();

            //设置重试文本的格式，把重试两个字设为点击事件，点击开始重新读取列表
            SpannableString spanString = new SpannableString("请连接网络后点击重试");
            spanString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    loadingView.setVisibility(View.VISIBLE);
                    try_again.setVisibility(View.INVISIBLE);
                    presenter.onCreateView();
                }
            }, 8, 10, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            try_again = (TextView) view.findViewById(R.id.tv_try_again);
            try_again.setText(spanString);
            try_again.setMovementMethod(LinkMovementMethod.getInstance());

            recyclerView = (RecyclerView) view.findViewById(R.id.rv_net_song_list_fragment);

            gridLayoutManager = new GridLayoutManager(mContext, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    //滑动停下来的时候，而且可见的最后一项是最后一项，即FooterItem，则加载下一页数据
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                        //当前页数
                        int pageNow = adapter.getItemCount()/12;
                        //读取下一页
                        presenter.showSongCollection(pageNow + 1);
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    //滑动的时候记录最后一项的位置
                    lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                }
            });

            presenter = new NetSongListPresenter(this);
            presenter.onCreateView();
            //加载成功
            isInit = true;
        }
    }

    /**
     * 当视图销毁的时候把加载标志设置为false，下次进入的时候重新加载
     */
    @Override
    public final void onDestroyView() {
        super.onDestroyView();
        isInit = false;
    }



    @Override
    public void setAdapter(final NetSongListAdapter adapter) {
        adapter.setOnItemClickListener(new NetSongListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, int position) {
                NetSongListAdapter.CommonItemViewHolder holder = ((NetSongListAdapter.CommonItemViewHolder)viewHolder);
                SongCollectionActivity.startActivity(MyApplication.getContext(), false,
                        holder.listid, holder.pic, holder.listenum, holder.title, holder.tag);
            }
        });
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        loadingView.setVisibility(View.GONE);
    }

    /**
     * 在原有的list上刷新后面的内容
     * @param list
     */
    @Override
    public void updateAdapter(List list) {
        adapter.updateAdapter(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showTryAgain() {
        loadingView.setVisibility(View.GONE);
        try_again.setVisibility(View.VISIBLE);
    }

}
