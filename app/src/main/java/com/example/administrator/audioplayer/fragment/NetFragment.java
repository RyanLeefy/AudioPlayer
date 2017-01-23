package com.example.administrator.audioplayer.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.MainPagerAdapter;
import com.example.administrator.audioplayer.adapter.NetFragmentPagerAdapter;

/**
 * 主界面第一个主fragment界面,里面有viewpager管理三个子fragment
 * Created on 2017/1/22.
 */

public class NetFragment extends Fragment {

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

        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        }).start();*/

        setViewPager();

        return v;
    }



    private void setViewPager() {
        NetFragmentPagerAdapter netFragmentPagerAdapter = new NetFragmentPagerAdapter(getChildFragmentManager());

        netFragmentPagerAdapter.addFragment(new RecommendFragment(), "新曲");
        netFragmentPagerAdapter.addFragment(new NetSongListFragment(), "歌单");
        netFragmentPagerAdapter.addFragment(new RankingFragment(), "排行榜");

        viewPager.setAdapter(netFragmentPagerAdapter);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
    }
}
