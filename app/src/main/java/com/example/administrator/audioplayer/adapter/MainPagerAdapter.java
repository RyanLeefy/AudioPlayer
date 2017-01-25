package com.example.administrator.audioplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/1/22.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragment_list = new ArrayList<>();

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MainPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.fragment_list = list;
    }

    public void addFragment(Fragment fragment) {
        fragment_list.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragment_list.get(position);
    }

    @Override
    public int getCount() {
        return fragment_list.size();
    }
}
