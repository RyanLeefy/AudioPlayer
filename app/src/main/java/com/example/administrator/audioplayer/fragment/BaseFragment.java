package com.example.administrator.audioplayer.fragment;

import android.support.v4.app.Fragment;

import com.example.administrator.audioplayer.activity.BaseActivity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 在基类里面创建一个CompositeSubscription用来管理Subscriber的生命周期，以防内存泄漏
 * Created on 2017/1/24.
 *
 */


public class BaseFragment extends Fragment implements BaseActivity.MusicStateListener {
    //在基类里面创建一个CompositeSubscription用来管理Subscriber的生命周期，以防内存泄漏
    protected CompositeSubscription mSubscriptions = new CompositeSubscription();

    public void addSubscription(Subscription subscription) {
        if (mSubscriptions != null) {
            mSubscriptions.add(subscription);
        }
    }


    /**
     * 当fragment显示时候，把其作为回调函数加到BaseActivity中，以便进行各种回调操作
     */
    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) getActivity()).setMusicStateListenerListener(this);
        //reloadAdapter();
    }


    /**
     * 当fragment消失时候，去除回调
     */
    @Override
    public void onStop() {
        super.onStop();
        ((BaseActivity) getActivity()).removeMusicStateListenerListener(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscriptions != null) {
            mSubscriptions.clear();
        }
    }


    /**
     * BaseActivity中监听各种播放状态然后执行的函数，默认为空，子类需要自己实现
     */
    @Override
    public void updateTrackInfo() {}

    /**
     * BaseActivity中监听各种播放状态然后执行的函数，默认为空，子类需要自己实现
     */
    @Override
    public void reloadAdapter() {}
}
