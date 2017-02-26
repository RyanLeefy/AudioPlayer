package com.example.administrator.audioplayer.fragment;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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

import com.example.administrator.audioplayer.Ipresenter.IRankingPresenter;
import com.example.administrator.audioplayer.Iview.IRankingView;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.activity.BillBoardActivity;
import com.example.administrator.audioplayer.adapter.RankingAdapter;
import com.example.administrator.audioplayer.presenterImp.RankingPresenter;
import com.example.administrator.audioplayer.widget.DividerItemDecoration;

/**
 * Netfragment下的排行榜fragment
 *
 */
public class RankingFragment extends BaseFragment implements IRankingView {

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

    private RankingAdapter adapter;

    private IRankingPresenter presenter;

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
            View view = mInflater.inflate(R.layout.fragment_ranking, lazyload_container, true);
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

            recyclerView = (RecyclerView) view.findViewById(R.id.rv_ranking_fragment);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

            presenter = new RankingPresenter(this);
            presenter.onCreateView();
            //加载成功
            isInit = true;
        }
    }

    @Override
    public void setAdapter(RankingAdapter adapter) {
        adapter.setOnItemClickListener(new RankingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, int position) {
                RankingAdapter.CommonItemViewHolder holder = ((RankingAdapter.CommonItemViewHolder)viewHolder);
                BillBoardActivity.startActivity(MyApplication.getContext(),
                        holder.type, holder.name, holder.update_time);
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
