package com.example.administrator.audioplayer.fragment;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.audioplayer.Ipresenter.IAfterSearchPresenter;
import com.example.administrator.audioplayer.Iview.IAfterSearchView;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.NetFragmentPagerAdapter;
import com.example.administrator.audioplayer.presenterImp.AfterSearchPresenter;

import java.util.ArrayList;
import java.util.List;


/**
 * 网络搜索搜索后显示的fragment，里面是用一个viewpager管理的两个子fragment
 * 分别是单曲fragment和专辑fragment
 */
public class AfterSearchFragment extends BaseFragment implements IAfterSearchView, SearchMoreCallBack {

    private Context mContext;

    private String query;

    //加载中动画视图
    private View loadingView;
    //加载动画视图中的动态ImageView
    private ImageView anim_image;
    //网络失败重试TextView
    private TextView try_again;


    private ViewPager viewPager;
    private TabLayout tabLayout;

    private AfterSearchMusicFragment afterSearchMusicFragment;
    private AfterSearchAlbumFragment afterSearchAlbumFragment;

    private IAfterSearchPresenter presenter;


    public static AfterSearchFragment newInstance(String query) {
        AfterSearchFragment fragment = new AfterSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_after_search, container, false);

        if (getArguments() != null) {
            query = getArguments().getString("query");
        }


        loadingView = v.findViewById(R.id.layout_loading_view);

        //加载画面的帧动画
        anim_image = (ImageView) v.findViewById(R.id.anim_image);
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
                presenter.onCreateView(query);
            }
        }, 8, 10, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        try_again = (TextView) v.findViewById(R.id.tv_try_again);
        try_again.setText(spanString);
        try_again.setMovementMethod(LinkMovementMethod.getInstance());


        viewPager = (ViewPager) v.findViewById(R.id.vp_searchfragment);



        tabLayout = (TabLayout) v.findViewById(R.id.tablayout_searchfragment);
        tabLayout.setTabTextColors(getResources().getColor(R.color.text_color),getResources().getColor(R.color.colorPrimary));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));


        presenter = new AfterSearchPresenter(this);
        presenter.onCreateView(query);

        //完成后把加载中页面去掉
        loadingView.setVisibility(View.GONE);


        return v;
    }


    /**
     * 初次加载，设置viewPager
     * @param musicList
     * @param albumList
     */
    @Override
    public void setViewPager(ArrayList musicList, ArrayList albumList) {
        NetFragmentPagerAdapter netFragmentPagerAdapter = new NetFragmentPagerAdapter(getChildFragmentManager());

        afterSearchMusicFragment = AfterSearchMusicFragment.newInstance(musicList);
        afterSearchMusicFragment.setCallBack(this);

        afterSearchAlbumFragment = AfterSearchAlbumFragment.newInstance(albumList);
        afterSearchAlbumFragment.setCallBack(this);

        netFragmentPagerAdapter.addFragment(afterSearchMusicFragment, "单曲");
        netFragmentPagerAdapter.addFragment(afterSearchAlbumFragment, "专辑");

        viewPager.setAdapter(netFragmentPagerAdapter);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
    }


    /**
     * 加载更多，刷新musicList
     * @param musicList
     */
    @Override
    public void updateMusicList(List musicList) {
        if(afterSearchMusicFragment != null) {
            afterSearchMusicFragment.updateAdapter(musicList);
        }
    }

    /**
     * 加载更多，刷新albumList
     * @param albumList
     */
    @Override
    public void updateAlbumList(List albumList) {
        if(afterSearchAlbumFragment != null) {
            afterSearchAlbumFragment.updateAdapter(albumList);
        }
    }

    @Override
    public void showTryAgain() {
        loadingView.setVisibility(View.GONE);
        try_again.setVisibility(View.VISIBLE);
    }

    @Override
    public void searchMoreMusic(int page) {
        presenter.searchMoreMusic(query, page);
    }

    @Override
    public void searchMoreAlbum(int page) {
        presenter.searchMoreAlbum(query, page);
    }
}
