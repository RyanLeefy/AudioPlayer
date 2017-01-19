package com.example.administrator.audioplayer.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.activity.MainActivity;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.lang.ref.WeakReference;


/**
 * 后台播放服务
 */

public class MediaService extends Service {

    private static final String TAG = "MediaService";

    private MultiPlayer mPlayer = new MultiPlayer(this);

    private final IBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
    }

    public MultiPlayer getPlayer() {
        return mPlayer;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Notification.Builder builder = new Notification.Builder(this);

        //Notification notification = new Notification(R.drawable.ic_launcher,
        //        "有通知到来", System.currentTimeMillis());

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = builder.setTicker("有通知到来")
                .setContentIntent(pendingIntent)
                //.setSmallIcon()
                .build();

        //开启前台服务
        startForeground(1, notification);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent){
        return true;
    }


    /**
     * 播放器
     */
    private class MultiPlayer implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {


        private WeakReference<MediaService> mService;

        private MediaPlayer mCurrentMediaPlayer = new MediaPlayer();

        private MediaPlayer mNextMediaPlayer;

        private boolean mIsInitialized = false;

        private String mNextMediaPath;


        boolean mIsTrackPrepared = false;
        boolean mIsTrackNet = false;
        boolean mIsNextTrackPrepared = false;
        boolean mIsNextInitialized = false;


        public MultiPlayer(MediaService service) {
            this.mService = new WeakReference<MediaService>(service);
            //设置唤醒锁，保证系统睡眠时CPU仍然运行
            mCurrentMediaPlayer.setWakeMode(mService.get(), PowerManager.PARTIAL_WAKE_LOCK);

        }

        public void setDataSource(final String path) {
            //设置现在Player的数据源
            mIsInitialized = setDataSourceImpl(mCurrentMediaPlayer, path);
            if (mIsInitialized) {
                setNextDataSource(null);
            }
        }

        public void setNextDataSource(final String path) {
            mNextMediaPath = null;
            mIsNextInitialized = false;
            try {
                mCurrentMediaPlayer.setNextMediaPlayer(null);
            } catch (IllegalArgumentException e) {
                Log.i(TAG, "Next media player is current one, continuing");
            } catch (IllegalStateException e) {
                Log.e(TAG, "Media player not initialized!");
                return;
            }
            if (mNextMediaPlayer != null) {
                mNextMediaPlayer.release();
                mNextMediaPlayer = null;
            }
            if (path == null) {
                return;
            }
            mNextMediaPlayer = new MediaPlayer();
            mNextMediaPlayer.setWakeMode(mService.get(), PowerManager.PARTIAL_WAKE_LOCK);

            //设置下一个音频会话id为当前音乐播放器会话id
            mNextMediaPlayer.setAudioSessionId(mCurrentMediaPlayer.getAudioSessionId());

            if (setNextDataSourceImpl(mNextMediaPlayer, path)) {
                mNextMediaPath = path;
                mCurrentMediaPlayer.setNextMediaPlayer(mNextMediaPlayer);
                // mHandler.post(setNextMediaPlayerIfPrepared);

            } else {
                if (mNextMediaPlayer != null) {
                    mNextMediaPlayer.release();
                    mNextMediaPlayer = null;
                }
            }
        }

        private boolean setDataSourceImpl(final MediaPlayer player, final String path) {
            mIsTrackNet = false;
            mIsTrackPrepared = false;
            try {
                //调用reset进入Idle状态
                player.reset();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                //若是本地数据源，则同步
                if (path.startsWith("content://")) {
                    player.setOnPreparedListener(mPreparedListener);
                    //调用setDataSource进入Initialized状态
                    player.setDataSource(mService.get(), Uri.parse(path));
                    //调用prepare进入parepared状态
                    player.prepare();


                } else {
                    //若是网络数据源，则异步
                    player.setDataSource(path);
                    player.setOnPreparedListener(mPreparedListener);

                    player.prepareAsync();
                    mIsTrackNet = true;
                }

            } catch (IOException todo) {

                return false;
            }

            player.setOnErrorListener(this);
            player.setOnCompletionListener(this);
            //player.setOnBufferingUpdateListener(bufferingUpdateListener);
            return true;
        }

        private boolean setNextDataSourceImpl(MediaPlayer player, String path) {
            mIsNextInitialized = false;
            mIsNextTrackPrepared = false;

            try {
                player.reset();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                if (path.startsWith("content://")) {
                    player.setOnPreparedListener(mNextPreparedListener);
                    player.setDataSource(mService.get(), Uri.parse(path));
                    mIsNextInitialized = true;
                    player.prepare();

                } else {
                    //若是网络数据源，则异步
                    player.setDataSource(path);
                    player.setOnPreparedListener(mNextPreparedListener);
                    player.prepareAsync();

                }
            } catch (IOException todo) {
                return false;
            }

            player.setOnCompletionListener(this);
            player.setOnErrorListener(this);
            return true;
        }


        MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mIsTrackPrepared = true;
            }
        };

        MediaPlayer.OnPreparedListener mNextPreparedListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mIsNextTrackPrepared = true;
            }
        };


        @Override
        public void onCompletion(MediaPlayer mp) {
            /*
            Logger.d(TAG, "completion");
            //若是当前播放器结束且有下一播放器，则切换到下一播放器
            if (mp == mCurrentMediaPlayer && mNextMediaPlayer != null) {
                mCurrentMediaPlayer.release();
                mCurrentMediaPlayer = mNextMediaPlayer;
                mNextMediaPath = null;
                mNextMediaPlayer = null;
                mHandler.sendEmptyMessage(TRACK_WENT_TO_NEXT);
            } else {
                mService.get().mWakeLock.acquire(30000);
                mHandler.sendEmptyMessage(TRACK_ENDED);
                mHandler.sendEmptyMessage(RELEASE_WAKELOCK);
            }*/
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            /*
            Logger.e(TAG, "Music Server Error what: " + what + " extra: " + extra);
            switch (what) {
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    final MediaService service = mService.get();
                    final TrackErrorInfo errorInfo = new TrackErrorInfo(service.getAudioId(),
                            service.getTrackName());

                    mIsInitialized = false;
                    mCurrentMediaPlayer.release();
                    mCurrentMediaPlayer = new MediaPlayer();
                    mCurrentMediaPlayer.setWakeMode(service, PowerManager.PARTIAL_WAKE_LOCK);
                    Message msg = mHandler.obtainMessage(SERVER_DIED, errorInfo);
                    mHandler.sendMessageDelayed(msg, 2000);
                    return true;
                default:
                    break;
            }
            return false;
        }*/
            return false;
        }
    }


}
