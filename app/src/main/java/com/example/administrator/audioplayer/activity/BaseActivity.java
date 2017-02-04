package com.example.administrator.audioplayer.activity;

import android.support.v7.app.AppCompatActivity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * activity基类，连接与解绑服务, 管理CompositeSubscription，Subscriber的生命周期
 */

public class BaseActivity extends AppCompatActivity {
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
