package com.example.administrator.audioplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.administrator.audioplayer.fragment.RoundFragment;
import com.example.administrator.audioplayer.service.MusicPlayer;

/**
 * Created by on 2017/2/8.
 */

public class RoundFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private int mChildCount = 0;

    public RoundFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        //把最左边的多添加的item初始化为最后一个唱片的图片
        if ( position == 0) {
            return RoundFragment.newInstance(MusicPlayer.getAlbumPathAll()[MusicPlayer.getQueueSize() - 1]);
        }
        //把最右边的多添加爱的item初始化为第一个唱片的图片
        if (position == MusicPlayer.getQueue().length + 1 ) {
            return RoundFragment.newInstance(MusicPlayer.getAlbumPathAll()[0]);
        }

        //return RoundFragment.newInstance(MusicPlayer.getQueue()[position - 1]);
        return RoundFragment.newInstance(MusicPlayer.getAlbumPathAll()[position - 1]);

    }

    @Override
    public int getCount() {
        //左右各加一个
        return MusicPlayer.getQueue().length + 2;
    }


    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
