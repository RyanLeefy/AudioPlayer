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

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * activity基类，连接与解绑服务, 管理CompositeSubscription，Subscriber的生命周期
 * 默认显示底部PlayBar栏，对应的activity布局中要有bottom_container底部栏
 * 若不需要显示的调用showQuickControl(false)隐藏这个栏
 *
 * TODO 不需要显示的activity布局，到时候新加一个标志位，如果标注位为false就不加载fragment的视图，以免报错
 */

public class BaseActivity extends AppCompatActivity {

    public static final String TAG = "BaseActivity";
    private MusicPlayer.ServiceToken mToken;
    private BottomPlayBarFragment fragment;   //底部播放条

    private PlaybackStatus mPlaybackStatus; //receiver 接受播放状态变化等



    //在基类里面创建一个CompositeSubscription用来管理Subscriber的生命周期，以防内存泄漏
    protected CompositeSubscription mSubscriptions = new CompositeSubscription();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定服务，初始化MusicPlayer
        mToken = MusicPlayer.bindToService(this);




        IntentFilter f = new IntentFilter();
        f.addAction(MediaService.PLAYSTATE_CHANGED);
        f.addAction(MediaService.META_CHANGED);
        f.addAction(MediaService.QUEUE_CHANGED);
        //f.addAction(IConstants.MUSIC_COUNT_CHANGED);
        f.addAction(MediaService.TRACK_PREPARED);
        f.addAction(MediaService.BUFFER_UP);
        //f.addAction(IConstants.EMPTY_LIST);
        f.addAction(MediaService.MUSIC_CHANGED);
        f.addAction(MediaService.LRC_UPDATED);
        //f.addAction(IConstants.PLAYLIST_COUNT_CHANGED);
        f.addAction(MediaService.MUSIC_LODING);

        showQuickControl(true);

        if(fragment != null) {
            mPlaybackStatus = new PlaybackStatus(fragment);
        }

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


    private final static class PlaybackStatus extends BroadcastReceiver {

        private final WeakReference<BottomPlayBarFragment> mReference;


        public PlaybackStatus(final BottomPlayBarFragment fragment) {
            mReference = new WeakReference<>(fragment);
        }


        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            BottomPlayBarFragment bottomPlayBarFragment = mReference.get();
            if (bottomPlayBarFragment != null) {
                if (action.equals(MediaService.META_CHANGED)) {
                    bottomPlayBarFragment.updateTrackInfo();

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
                } else if (action.equals(MediaService.MUSIC_CHANGED)) {
                    //bottomPlayBarFragment.updateTrack();
                } else if (action.equals(MediaService.LRC_UPDATED)) {
                    //baseActivity.updateLrc();
                }

            }
        }
    }


    public interface MusicStateListener {

        /**
         * 更新歌曲状态信息
         */
        void updateTrackInfo();

        void updateTime();

        void changeTheme();

        void reloadAdapter();
    }


}
