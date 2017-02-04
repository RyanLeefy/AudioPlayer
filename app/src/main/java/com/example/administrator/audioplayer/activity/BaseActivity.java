package com.example.administrator.audioplayer.activity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.audioplayer.service.MusicPlayer;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * activity基类，连接与解绑服务, 管理CompositeSubscription，Subscriber的生命周期
 */

public class BaseActivity extends AppCompatActivity {

    private MusicPlayer.ServiceToken mToken;


    //在基类里面创建一个CompositeSubscription用来管理Subscriber的生命周期，以防内存泄漏
    protected CompositeSubscription mSubscriptions = new CompositeSubscription();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定服务，初始化MusicPlayer
        mToken = MusicPlayer.bindToService(this);
        /*
        mPlaybackStatus = new PlaybackStatus(this);

        IntentFilter f = new IntentFilter();
        f.addAction(MediaService.PLAYSTATE_CHANGED);
        f.addAction(MediaService.META_CHANGED);
        f.addAction(MediaService.QUEUE_CHANGED);
        f.addAction(IConstants.MUSIC_COUNT_CHANGED);
        f.addAction(MediaService.TRACK_PREPARED);
        f.addAction(MediaService.BUFFER_UP);
        f.addAction(IConstants.EMPTY_LIST);
        f.addAction(MediaService.MUSIC_CHANGED);
        f.addAction(MediaService.LRC_UPDATED);
        f.addAction(IConstants.PLAYLIST_COUNT_CHANGED);
        f.addAction(MediaService.MUSIC_LODING);
        registerReceiver(mPlaybackStatus, new IntentFilter(f));
        showQuickControl(true);*/
    }


    public void addSubscription(Subscription subscription) {
        if (mSubscriptions != null) {
            mSubscriptions.add(subscription);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除绑定
        unbindService();
        //取消所有Subscriber
        if (mSubscriptions != null) {
            mSubscriptions.clear();
        }
    }

    public void unbindService() {
        if (mToken != null) {
            MusicPlayer.unbindFromService(mToken);
            mToken = null;
        }
    }
}
