package com.example.administrator.audioplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/1/23.
 */

public class NetFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragment_list = new ArrayList<>();
    private List<String> fragment_titlelist = new ArrayList<>();

    public NetFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public NetFragmentPagerAdapter(FragmentManager fm, List<Fragment> list, List<String> title) {
        super(fm);
        this.fragment_list = list;
        this.fragment_titlelist = title;
    }

    public void addFragment(Fragment fragment, String title) {
        fragment_list.add(fragment);
        fragment_titlelist.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragment_list.get(position);
    }

    @Override
    public int getCount() {
        return fragment_list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragment_titlelist.get(position);
    }

}
