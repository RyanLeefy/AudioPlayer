package com.example.administrator.audioplayer.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.fragment.BottomPlayBarFragment;
import com.example.administrator.audioplayer.service.MediaService;
import com.example.administrator.audioplayer.service.MusicPlayer;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * activity基类，连接与解绑服务, 管理CompositeSubscription，Subscriber的生命周期
 * 默认显示底部PlayBar栏，对应的activity布局中要有bottom_container底部栏
 * 若不需要显示的调用重写showQuickControl(boolean)方法并让方法为空即可
 *
 * TODO 不需要显示的activity布局，到时候新加一个标志位，如果标注位为false就不加载fragment的视图，以免报错
 * TODO 要在播放列表为空的时候不加载底部PlayBar栏，不然进去PlayingActivity没有东西显示
 *
 * 在baseActivity中注册receiver，统一监听各种播放状态的改变事件，然后对接口进行操作
 * 所有的其他需要具体实现的页面实现该接口
 */

public class BaseActivity extends AppCompatActivity {

    public static final String TAG = "BaseActivity";
    private MusicPlayer.ServiceToken mToken;
    private BottomPlayBarFragment fragment;   //底部播放条

    private PlaybackStatus mPlaybackStatus; //receiver 接受播放状态变化等

    private ArrayList<MusicStateListener> mMusicListener = new ArrayList<>();  //用来存储



    //在基类里面创建一个CompositeSubscription用来管理Subscriber的生命周期，以防内存泄漏
    protected CompositeSubscription mSubscriptions = new CompositeSubscription();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定服务，初始化MusicPlayer
        mToken = MusicPlayer.bindToService(this);



        //注册接收的动作
        IntentFilter f = new IntentFilter();
        f.addAction(MediaService.PLAYSTATE_CHANGED);
        f.addAction(MediaService.META_CHANGED);
        f.addAction(MediaService.QUEUE_CHANGED);
        //f.addAction(IConstants.MUSIC_COUNT_CHANGED);
        f.addAction(MediaService.TRACK_PREPARED);
        f.addAction(MediaService.BUFFER_UP);
        //f.addAction(IConstants.EMPTY_LIST);

        f.addAction(MediaService.LRC_UPDATED);
        //f.addAction(IConstants.PLAYLIST_COUNT_CHANGED);
        f.addAction(MediaService.MUSIC_LODING);


        mPlaybackStatus = new PlaybackStatus(this);

        //默认显示底部播放栏
        showQuickControl(true);

        //注册receiver
        registerReceiver(mPlaybackStatus, new IntentFilter(f));
    }




    /**
     * @param show 显示或关闭底部播放控制栏
     */
    protected void showQuickControl(boolean show) {
        Logger.d(MusicPlayer.getQueue().length);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (show) {
            if (fragment == null) {
                fragment = BottomPlayBarFragment.newInstance();
                ft.add(R.id.bottom_container, fragment).commitAllowingStateLoss();
            } else {
                ft.show(fragment).commitAllowingStateLoss();
            }
        } else {
            if (fragment != null)
                ft.hide(fragment).commitAllowingStateLoss();
        }
    }


    /**
     * 歌曲切换，在需要的activity中具体实现
     */
    public void updateTrack() {}


    /**
     * 刷新歌词，在需要的activity中具体实现
     */
    public void updateLrc() {}



    /**
     * 更新歌曲状态信息，在需要的fragment中具体实现
     */
    public void updateTrackInfo() {
        for (final MusicStateListener listener : mMusicListener) {
            if (listener != null) {
                listener.reloadAdapter();
                listener.updateTrackInfo();
            }
        }
    }

    /**
     * fragment界面刷新，在需要的fragment中具体实现
     */
    public void refreshUI() {
        for (final MusicStateListener listener : mMusicListener) {
            if (listener != null) {
                listener.reloadAdapter();
            }
        }

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

        //解除接受广播
        unregisterReceiver(mPlaybackStatus);

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


    /**
     * 注册receiver接受各种播放状态的改变
     */
    private final static class PlaybackStatus extends BroadcastReceiver {

        private final WeakReference<BaseActivity> mReference;


        public PlaybackStatus(final BaseActivity baseActivity) {
            mReference = new WeakReference<>(baseActivity);
        }


        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            Log.e("Receiver", "context:" + context);
            Log.e("Receiver", "Intent:" + intent);
            Log.e("Receiver", "Action:" + action);
            BaseActivity baseActivity = mReference.get();
            if (baseActivity != null) {
                if (action.equals(MediaService.META_CHANGED)) {
                    baseActivity.updateTrack();
                    baseActivity.updateTrackInfo();
                } else if (action.equals(MediaService.PLAYSTATE_CHANGED)) {

                } else if (action.equals(MediaService.TRACK_PREPARED)) {
                    //bottomPlayBarFragment.updateTime();
                } else if (action.equals(MediaService.BUFFER_UP)) {
                    //bottomPlayBarFragment.updateBuffer(intent.getIntExtra("progress", 0));
                } else if (action.equals(MediaService.MUSIC_LODING)) {
                    //bottomPlayBarFragment.loading(intent.getBooleanExtra("isloading", false));
                } else if (action.equals(MediaService.REFRESH)) {

                //} else if (action.equals(IConstants.MUSIC_COUNT_CHANGED)) {
                    //bottomPlayBarFragment.refreshUI();
                //} else if (action.equals(IConstants.PLAYLIST_COUNT_CHANGED)) {
                    //bottomPlayBarFragment.refreshUI();
                } else if (action.equals(MediaService.QUEUE_CHANGED)) {
                    //bottomPlayBarFragment.updateQueue();
                } else if (action.equals(MediaService.TRACK_ERROR)) {
                    //final String errorMsg = context.getString(R.string.exit,
                    //        intent.getStringExtra(MediaService.TrackErrorExtra.TRACK_NAME));
                    //Toast.makeText(baseActivity, errorMsg, Toast.LENGTH_SHORT).show();
                } else if (action.equals(MediaService.LRC_UPDATED)) {
                    //baseActivity.updateLrc();
                }

            }
        }
    }


    public void setMusicStateListenerListener(final MusicStateListener status) {
        if (status == this) {
            throw new UnsupportedOperationException("Override the method, don't add a listener");
        }

        if (status != null) {
            mMusicListener.add(status);
        }
    }

    public void removeMusicStateListenerListener(final MusicStateListener status) {
        if (status != null) {
            mMusicListener.remove(status);
        }
    }


    /**
     * fragment中要实现的回调接口，用于在播放状态改变时候做出相应操作
     */
    public interface MusicStateListener {

        /**
         * 更新歌曲状态信息
         */
        void updateTrackInfo();


        void reloadAdapter();
    }


}
