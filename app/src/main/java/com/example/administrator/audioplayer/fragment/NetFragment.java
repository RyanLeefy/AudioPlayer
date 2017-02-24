package com.example.administrator.audioplayer.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.NetFragmentPagerAdapter;

/**
 * 主界面第一个主fragment界面,里面有viewpager管理三个子fragment
 * Created on 2017/1/22.
 */

public class NetFragment extends Fragment implements ChangeViewPagerCallBack {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_net, container, false);

        viewPager = (ViewPager) v.findViewById(R.id.vp_netfragment);



        tabLayout = (TabLayout) v.findViewById(R.id.tablayout_netfragment);
        tabLayout.setTabTextColors(getResources().getColor(R.color.text_color),getResources().getColor(R.color.colorPrimary));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));

        setViewPager();

        return v;
    }



    private void setViewPager() {
        NetFragmentPagerAdapter netFragmentPagerAdapter = new NetFragmentPagerAdapter(getChildFragmentManager());

        RecommendFragment recommendFragment = new RecommendFragment();
        recommendFragment.setChangeViewPagerCallBack(this);

        NetSongListFragment netSongListFragment = new NetSongListFragment();
        netSongListFragment.setChangeViewPagerCallBack(this);

        RankingFragment rankingFragment = new RankingFragment();
        rankingFragment.setChangeViewPagerCallBack(this);

        netFragmentPagerAdapter.addFragment(recommendFragment, "新曲");
        netFragmentPagerAdapter.addFragment(netSongListFragment, "歌单");
        netFragmentPagerAdapter.addFragment(rankingFragment, "排行榜");

        viewPager.setAdapter(netFragmentPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void changeViewPagerTo(int position) {
        viewPager.setCurrentItem(position);
    }


}
