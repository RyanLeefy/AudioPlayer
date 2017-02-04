package com.example.administrator.audioplayer.fragment;

import android.support.v4.app.Fragment;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 在基类里面创建一个CompositeSubscription用来管理Subscriber的生命周期，以防内存泄漏
 * Created on 2017/1/24.
 */
public class BaseFragment extends Fragment {
    //在基类里面创建一个CompositeSubscription用来管理Subscriber的生命周期，以防内存泄漏
    protected CompositeSubscription mSubscriptions = new CompositeSubscription();

    public void addSubscription(Subscription subscription) {
        if (mSubscriptions != null) {
            mSubscriptions.add(subscription);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscriptions != null) {
            mSubscriptions.clear();
        }
    }



}
