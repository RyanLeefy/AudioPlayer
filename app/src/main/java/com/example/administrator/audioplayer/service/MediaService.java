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
 * 提供播放服务供activity绑定调用，MediaService提供各操作的接口
 * MediaService对播放器各操作的实现通过调用MultiPlayer来进行
 * MultiPlayer是一个包括两个MediaPlayer的类，mCurrentMediaPlayer和mNextMediaPlayer
 * MultiPlayer的各种操作实际是通过MediaPlayer来实现
 */

public class MediaService extends Service {


    private static final String TAG = "MediaService";
    public static final String PLAYSTATE_CHANGED = "com.wm.remusic.playstatechanged";
    public static final String POSITION_CHANGED = "com.wm.remusic.positionchanged";
    public static final String META_CHANGED = "com.wm.remusic.metachanged";
    public static final String PLAYLIST_ITEM_MOVED = "com.wm.remusic.mmoved";
    public static final String QUEUE_CHANGED = "com.wm.remusic.queuechanged";
    public static final String PLAYLIST_CHANGED = "com.wm.remusic.playlistchanged";
    public static final String REPEATMODE_CHANGED = "com.wm.remusic.repeatmodechanged";
    public static final String SHUFFLEMODE_CHANGED = "com.wm.remusic.shufflemodechanged";
    public static final String TRACK_ERROR = "com.wm.remusic.trackerror";
    public static final String TIMBER_PACKAGE_NAME = "com.wm.remusic";
    public static final String MUSIC_PACKAGE_NAME = "com.android.music";
    public static final String SERVICECMD = "com.wm.remusic.musicservicecommand";
    public static final String TOGGLEPAUSE_ACTION = "com.wm.remusic.togglepause";
    public static final String PAUSE_ACTION = "com.wm.remusic.pause";
    public static final String STOP_ACTION = "com.wm.remusic.stop";
    public static final String PREVIOUS_ACTION = "com.wm.remusic.previous";
    public static final String PREVIOUS_FORCE_ACTION = "com.wm.remusic.previous.force";
    public static final String NEXT_ACTION = "com.wm.remusic.next";
    public static final String MUSIC_CHANGED = "com.wm.remusi.change_music";
    public static final String REPEAT_ACTION = "com.wm.remusic.repeat";
    public static final String SHUFFLE_ACTION = "com.wm.remusic.shuffle";
    public static final String FROM_MEDIA_BUTTON = "frommediabutton";
    public static final String REFRESH = "com.wm.remusic.refresh";
    public static final String LRC_UPDATED = "com.wm.remusic.updatelrc";
    public static final String UPDATE_LOCKSCREEN = "com.wm.remusic.updatelockscreen";
    public static final String CMDNAME = "command";
    public static final String CMDTOGGLEPAUSE = "togglepause";
    public static final String CMDSTOP = "stop";
    public static final String CMDPAUSE = "pause";
    public static final String CMDPLAY = "play";
    public static final String CMDPREVIOUS = "previous";
    public static final String CMDNEXT = "next";
    public static final String CMDNOTIF = "buttonId";
    public static final String TRACK_PREPARED = "com.wm.remusic.prepared";
    public static final String TRY_GET_TRACKINFO = "com.wm.remusic.gettrackinfo";
    public static final String BUFFER_UP = "com.wm.remusic.bufferup";
    public static final String LOCK_SCREEN = "com.wm.remusic.lock";
    public static final String SEND_PROGRESS = "com.wm.remusic.progress";
    public static final String MUSIC_LODING = "com.wm.remusic.loading";
    private static final String SHUTDOWN = "com.wm.remusic.shutdown";
    public static final String SETQUEUE = "com.wm.remusic.setqueue";
    public static final int NEXT = 2;
    public static final int LAST = 3;
    public static final int SHUFFLE_NONE = 0;
    public static final int SHUFFLE_NORMAL = 1;
    public static final int SHUFFLE_AUTO = 2;
    public static final int REPEAT_NONE = 2;
    public static final int REPEAT_CURRENT = 1;
    public static final int REPEAT_ALL = 2;
    public static final int MAX_HISTORY_SIZE = 1000;

    private static final boolean D = true;
    private static final int LRC_DOWNLOADED = -10;
    private static final int IDCOLIDX = 0;
    private static final int TRACK_ENDED = 1;
    private static final int TRACK_WENT_TO_NEXT = 2;
    private static final int RELEASE_WAKELOCK = 3;
    private static final int SERVER_DIED = 4;
    private static final int FOCUSCHANGE = 5;
    private static final int FADEDOWN = 6;
    private static final int FADEUP = 7;
    private static final int IDLE_DELAY = 5 * 60 * 1000;
    private static final long REWIND_INSTEAD_PREVIOUS_THRESHOLD = 3000;


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


        private boolean mIsTrackPrepared = false;
        private boolean mIsTrackNet = false;
        private boolean mIsNextTrackPrepared = false;
        private boolean mIsNextInitialized = false;


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
            player.setOnBufferingUpdateListener(bufferingUpdateListener);
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
                if (isFirstLoad) {
                    long seekpos = mService.get().mLastSeekPos;
                    Log.e(TAG, "seekpos = " + seekpos);
                    seek(seekpos >= 0 ? seekpos : 0);
                    isFirstLoad = false;
                }
                // mService.get().notifyChange(TRACK_PREPARED);
                mService.get().notifyChange(META_CHANGED);
                mp.setOnCompletionListener(MultiPlayer.this);
                mIsTrackPrepared = true;
            }
        };

        MediaPlayer.OnPreparedListener mNextPreparedListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mIsNextTrackPrepared = true;
            }
        };

        MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if (sencondaryPosition != 100)
                    mService.get().sendUpdateBuffer(percent);
                sencondaryPosition = percent;
            }
        };

        public boolean isInitialized() {
            return mIsInitialized;
        }

        public boolean isTrackPrepared() {
            return mIsTrackPrepared;
        }

        public void start() {
            if (D) Log.d(TAG, "mIsTrackNet, " + mIsTrackNet);
            if (!mIsTrackNet) {
                mService.get().sendUpdateBuffer(100);
                sencondaryPosition = 100;
                mCurrentMediaPlayer.start();
            } else {
                sencondaryPosition = 0;
                mService.get().loading(true);
                handler.postDelayed(startMediaPlayerIfPrepared, 50);
            }
            mService.get().notifyChange(MUSIC_CHANGED);
        }



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
