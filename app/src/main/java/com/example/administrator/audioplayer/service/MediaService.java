package com.example.administrator.audioplayer.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.db.RecentMusicDB;
import com.example.administrator.audioplayer.http.HttpMethods;
import com.example.administrator.audioplayer.http.HttpUtils;
import com.example.administrator.audioplayer.jsonbean.SongExtraInfo;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.example.administrator.audioplayer.utils.PreferencesUtils;
import com.example.administrator.audioplayer.utils.PrintLog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.TreeSet;


/**
 * 后台播放服务
 * 提供播放服务供activity绑定调用，MediaService提供各操作的接口
 * MediaService对播放器各操作的实现通过调用MultiPlayer来进行
 * MultiPlayer是一个包括两个MediaPlayer的类，mCurrentMediaPlayer和mNextMediaPlayer
 * MultiPlayer的各种操作实际是通过MediaPlayer来实现
 */

@SuppressLint("NewApi")
public class MediaService extends Service {


    private static final String TAG = "MediaService";
    public static final String PLAYSTATE_CHANGED = "playstatechanged";
    public static final String POSITION_CHANGED = "positionchanged";
    public static final String META_CHANGED = "metachanged";
    public static final String PLAYLIST_ITEM_MOVED = "mmoved";
    public static final String QUEUE_CHANGED = "queuechanged";
    public static final String PLAYLIST_CHANGED = "playlistchanged";
    public static final String REPEATMODE_CHANGED = "repeatmodechanged";
    public static final String SHUFFLEMODE_CHANGED = "shufflemodechanged";
    public static final String TRACK_ERROR = "trackerror";
    public static final String TIMBER_PACKAGE_NAME = "com.example.administrator.audioplay";
    public static final String MUSIC_PACKAGE_NAME = "com.android.music";
    public static final String SERVICECMD = "musicservicecommand";
    public static final String TOGGLEPAUSE_ACTION = "togglepause";
    public static final String PAUSE_ACTION = "pause";
    public static final String STOP_ACTION = "stop";
    public static final String PREVIOUS_ACTION = "previous";
    public static final String NEXT_ACTION = "next";
    public static final String REPEAT_ACTION = "repeat";
    public static final String SHUFFLE_ACTION = "shuffle";
    public static final String FROM_MEDIA_BUTTON = "frommediabutton";
    public static final String REFRESH = "refresh";
    public static final String LRC_UPDATED = "updatelrc";
    public static final String UPDATE_LOCKSCREEN = "updatelockscreen";
  
    
    public static final String TRACK_PREPARED = "prepared";
    public static final String TRY_GET_TRACKINFO = "gettrackinfo";
    public static final String BUFFER_UP = "bufferup";
    public static final String LOCK_SCREEN = "lock";
    public static final String SEND_PROGRESS = "progress";
    public static final String MUSIC_LODING = "loading";
    private static final String SHUTDOWN = "shutdown";
    public static final String SETQUEUE = "setqueue";
    public static final int NEXT = 2;
    public static final int LAST = 3;
    public static final int SHUFFLE_NONE = 0;
    public static final int SHUFFLE_NORMAL = 1;
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


    private static final String[] PROJECTION = new String[]{
            "audio._id AS _id", MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST_ID
    };
    private static final String[] ALBUM_PROJECTION = new String[]{
            MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.LAST_YEAR
    };
    private static final Shuffler mShuffler = new Shuffler();
    private static final int NOTIFY_MODE_NONE = 0;
    private static final int NOTIFY_MODE_FOREGROUND = 1;
    private static final int NOTIFY_MODE_BACKGROUND = 2;
    private static final String[] PROJECTION_MATRIX = new String[]{
            "_id", MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST_ID
    };
    private static LinkedList<Integer> mHistory = new LinkedList<>();
    private String mFileToPlay;
    private PowerManager.WakeLock mWakeLock;
    private AlarmManager mAlarmManager;
    private PendingIntent mShutdownIntent;
    private boolean mShutdownScheduled;
    private NotificationManager mNotificationManager;
    private Cursor mCursor;
    private Cursor mAlbumCursor;
    private AudioManager mAudioManager;
    private SharedPreferences mPreferences;
    private boolean mServiceInUse = false;
    private boolean mIsSupposedToBePlaying = false;
    private long mLastPlayedTime;
    private int mNotifyMode = NOTIFY_MODE_NONE;
    private long mNotificationPostTime = 0;
    private boolean mQueueIsSaveable = true;
    private boolean mPausedByTransientLossOfFocus = false;


    //在Android5.0之后新增了MediaSession，可以在系统锁屏那显示歌曲唱片和控制歌曲状态
    private MediaSession mSession;

    private ComponentName mMediaButtonReceiverComponent;

    private int mCardId;

    private int mPlayPos = -1;

    private int mNextPlayPos = -1;

    private int mOpenFailedCounter = 0;

    private int mMediaMountedCount = 0;

    private int mShuffleMode = SHUFFLE_NONE;

    private int mRepeatMode = REPEAT_ALL;

    private int mServiceStartId = -1;

    //播放列表 用List来装MusicInfo实例
    private ArrayList<MusicInfo> mPlaylist = new ArrayList<MusicInfo>();


    private long[] mAutoShuffleList = null;

    private MusicPlayerHandler mPlayerHandler;

    private HandlerThread mHandlerThread;
    private BroadcastReceiver mUnmountReceiver = null;
    //private MusicPlaybackState mPlaybackStateStore;
    private boolean mShowAlbumArtOnLockscreen;
    //private SongPlayCount mSongPlayCount;
    //private RecentStore mRecentStore;
    private int mNotificationId = 1000;

    private ContentObserver mMediaStoreObserver;
    private static Handler mUrlHandler;
    private static Handler mLrcHandler;
    //private MediaPlayerProxy mProxy;
    public static final String LRC_PATH = "/audioplayer/lrc/";
    private long mLastSeekPos = 0;
    private RequestPlayUrl mRequestUrl;
    private RequestLrc mRequestLrc;
    private boolean mIsSending = false;
    private boolean mIsLocked;
    private Bitmap mNoBit;
    private Notification mNotification;


    private MultiPlayer mPlayer;

    private final IBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
    }

    public MultiPlayer getPlayer() {
        return mPlayer;
    }


    //创建一个关于歌词的线程，创建对应的handler
    private Thread mLrcThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            mLrcHandler = new Handler();
            Looper.loop();
        }
    });

    //创建一个关于获取Url的线程，创建对应的handler
    private Thread mGetUrlThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            mUrlHandler = new Handler();
            Looper.loop();
        }
    });


    //OnAudioFocusChangeListener，用与监听手机Audio的状态变化，例如当其他音频播放时，有电话响时等状态
    private final AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(final int focusChange) {
            mPlayerHandler.obtainMessage(FOCUSCHANGE, focusChange, 0).sendToTarget();
        }
    };

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, final Intent intent) {


            Log.d(TAG, "onreceive" + intent.toURI());
            handleCommandIntent(intent);

        }
    };





    @Override
    public void onCreate() {
        super.onCreate();

        //开启线程
        mGetUrlThread.start();
        mLrcThread.start();

        mHandlerThread = new HandlerThread("MusicPlayerHandler",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();

        mPlayerHandler = new MusicPlayerHandler(this, mHandlerThread.getLooper());

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        //判断系统版本，如果大于5.0则初始化MediaSession，在系统锁屏处显示封面和操作
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setUpMediaSession();
        }

        mPreferences = getSharedPreferences("Service", 0);
        //mCardId = getCardId();

        //registerExternalStorageListener();

        mPlayer = new MultiPlayer(this);
        mPlayer.setHandler(mPlayerHandler);

        // Initialize the intent filter and each action
        final IntentFilter filter = new IntentFilter();
        filter.addAction(SERVICECMD);
        filter.addAction(TOGGLEPAUSE_ACTION);
        filter.addAction(PAUSE_ACTION);
        filter.addAction(STOP_ACTION);
        filter.addAction(NEXT_ACTION);
        filter.addAction(PREVIOUS_ACTION);
        filter.addAction(REPEAT_ACTION);
        filter.addAction(SHUFFLE_ACTION);
        filter.addAction(TRY_GET_TRACKINFO);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(LOCK_SCREEN);
        filter.addAction(SEND_PROGRESS);
        filter.addAction(SETQUEUE);
        // Attach the broadcast listener
        registerReceiver(mIntentReceiver, filter);

        mMediaStoreObserver = new MediaStoreObserver(mPlayerHandler);
        getContentResolver().registerContentObserver(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI, true, mMediaStoreObserver);
        getContentResolver().registerContentObserver(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true, mMediaStoreObserver);

        // Initialize the wake lock
        final PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        mWakeLock.setReferenceCounted(false);


        final Intent shutdownIntent = new Intent(this, MediaService.class);
        shutdownIntent.setAction(SHUTDOWN);

        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mShutdownIntent = PendingIntent.getService(this, 0, shutdownIntent, 0);

        scheduleDelayedShutdown();

        //reloadQueueAfterPermissionCheck();
        notifyChange(QUEUE_CHANGED);
        notifyChange(META_CHANGED);
        Logger.d("Service create");



        /*
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
        startForeground(1, notification);*/


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
     * 初始化MediaSession，在系统锁屏处显示唱片和操作
     */
    private void setUpMediaSession() {
        mSession = new MediaSession(this, "audioplay");
        mSession.setCallback(new MediaSession.Callback() {
            @Override
            public void onPause() {
                pause();
                mPausedByTransientLossOfFocus = false;
            }

            @Override
            public void onPlay() {
                play();
            }

            @Override
            public void onSeekTo(long pos) {
                seek(pos);
            }

            @Override
            public void onSkipToNext() {
                gotoNext(true);
            }

            @Override
            public void onSkipToPrevious() {
                prev();
            }

            @Override
            public void onStop() {
                pause();
                mPausedByTransientLossOfFocus = false;
                seek(0);
                releaseServiceUiAndStop();
            }
        });
        //设置通过回调来处理控制命令
        mSession.setFlags(MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        if (D) Log.d(TAG, "Got new intent " + intent + ", startId = " + startId);
        mServiceStartId = startId;
        if (intent != null) {
            final String action = intent.getAction();

            if (SHUTDOWN.equals(action)) {
                mShutdownScheduled = false;
                releaseServiceUiAndStop();
                return START_NOT_STICKY;
            }
            handleCommandIntent(intent);
        }

        scheduleDelayedShutdown();

        if (intent != null && intent.getBooleanExtra(FROM_MEDIA_BUTTON, false)) {
            //MediaButtonIntentReceiver.completeWakefulIntent(intent);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (D) Log.d(TAG, "Destroying service");
        super.onDestroy();
        // Remove any sound effects
        final Intent audioEffectsIntent = new Intent(
                AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION);
        audioEffectsIntent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
        audioEffectsIntent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
        sendBroadcast(audioEffectsIntent);

        //cancelNotification();

        mAlarmManager.cancel(mShutdownIntent);

        mPlayerHandler.removeCallbacksAndMessages(null);

        if (CommonUtils.isJellyBeanMR2())
            mHandlerThread.quitSafely();
        else mHandlerThread.quit();

        mPlayer.release();
        mPlayer = null;

        mAudioManager.abandonAudioFocus(mAudioFocusListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mSession.release();

        getContentResolver().unregisterContentObserver(mMediaStoreObserver);

        closeCursor();

        unregisterReceiver(mIntentReceiver);
        if (mUnmountReceiver != null) {
            unregisterReceiver(mUnmountReceiver);
            mUnmountReceiver = null;
        }

        mWakeLock.release();
    }


    private void releaseServiceUiAndStop() {
        if (isPlaying()
                || mPausedByTransientLossOfFocus
                || mPlayerHandler.hasMessages(TRACK_ENDED)) {
            return;
        }

        if (D) Log.d(TAG, "Nothing is playing anymore, releasing notification");
        //cancelNotification();
        mAudioManager.abandonAudioFocus(mAudioFocusListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mSession.setActive(false);

        if (!mServiceInUse) {
            //saveQueue(true);
            stopSelf(mServiceStartId);
        }
    }

    private void handleCommandIntent(Intent intent) {
        final String action = intent.getAction();
        //final String command = SERVICECMD.equals(action) ? intent.getStringExtra(CMDNAME) : null;

        if (D) Log.d(TAG, "handleCommandIntent: action = " + action);

        if (NEXT_ACTION.equals(action)) {
            gotoNext(true);
        } else if (PREVIOUS_ACTION.equals(action)) {
            prev();
        } else if (TOGGLEPAUSE_ACTION.equals(action)) {
            if (isPlaying()) {
                pause();
                mPausedByTransientLossOfFocus = false;
            } else {
                play();
            }
        } else if (PAUSE_ACTION.equals(action)) {
            pause();
            mPausedByTransientLossOfFocus = false;
        } else if (STOP_ACTION.equals(action)) {
            pause();
            mPausedByTransientLossOfFocus = false;
            seek(0);
            releaseServiceUiAndStop();
        } else if (REPEAT_ACTION.equals(action)) {
            //cycleRepeat();
        } else if (SHUFFLE_ACTION.equals(action)) {
            //cycleShuffle();
        } else if (TRY_GET_TRACKINFO.equals(action)) {
            //getLrc(mPlaylist.get(mPlayPos).mId);
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {

            if (isPlaying() && !mIsLocked) {
                //Intent lockscreen = new Intent(this, LockActivity.class);
                //lockscreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(lockscreen);
            }
        } else if (LOCK_SCREEN.equals(action)) {
            mIsLocked = intent.getBooleanExtra("islock", true);
            Logger.d( TAG, "isloced = " + mIsLocked);
        } else if (SEND_PROGRESS.equals(action)) {
            if (isPlaying() && !mIsSending) {
                mPlayerHandler.post(sendDuration);
                mIsSending = true;
            } else if (!isPlaying()) {
                mPlayerHandler.removeCallbacks(sendDuration);
                mIsSending = false;
            }

        } else if (SETQUEUE.equals(action)) {
            Log.e("playab", "action");
            setQueuePosition(intent.getIntExtra("position", 0));
        }
    }

    private Runnable sendDuration = new Runnable() {
        @Override
        public void run() {
            notifyChange(SEND_PROGRESS);
            mPlayerHandler.postDelayed(sendDuration, 1000);
        }
    };

    /*
    private void updateNotification() {
        final int newNotifyMode;
        if (isPlaying()) {
            newNotifyMode = NOTIFY_MODE_FOREGROUND;
        } else if (recentlyPlayed()) {
            newNotifyMode = NOTIFY_MODE_BACKGROUND;
        } else {
            newNotifyMode = NOTIFY_MODE_NONE;
        }

        // int mNotificationId = hashCode();

        if (mNotifyMode != newNotifyMode) {
            if (mNotifyMode == NOTIFY_MODE_FOREGROUND) {
                if (CommonUtils.isLollipop())
                    stopForeground(newNotifyMode == NOTIFY_MODE_NONE);
                else
                    stopForeground(newNotifyMode == NOTIFY_MODE_NONE || newNotifyMode == NOTIFY_MODE_BACKGROUND);
            } else if (newNotifyMode == NOTIFY_MODE_NONE) {
                mNotificationManager.cancel(mNotificationId);
                mNotificationPostTime = 0;
            }
        }

        if (newNotifyMode == NOTIFY_MODE_FOREGROUND) {
            startForeground(mNotificationId, getNotification());

        } else if (newNotifyMode == NOTIFY_MODE_BACKGROUND) {
            mNotificationManager.notify(mNotificationId, getNotification());
        }

        mNotifyMode = newNotifyMode;
    }*/

    /*
    private void cancelNotification() {
        stopForeground(true);
        //mNotificationManager.cancel(hashCode());
        mNotificationManager.cancel(mNotificationId);
        mNotificationPostTime = 0;
        mNotifyMode = NOTIFY_MODE_NONE;
    }*/

    /*
    private int getCardId() {
        if (CommonUtils.isMarshmallow()) {
            if (Nammu.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                return getmCardId();
            } else return 0;
        } else {
            return getmCardId();
        }
    }

    private int getmCardId() {
        final ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(Uri.parse("content://media/external/fs_id"), null, null,
                null, null);
        int mCardId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            mCardId = cursor.getInt(0);
            cursor.close();
        }
        return mCardId;
    }*/


    /*
    public void closeExternalStorageFiles(final String storagePath) {
        stop(true);
        notifyChange(QUEUE_CHANGED);
        notifyChange(META_CHANGED);
    }

    public void registerExternalStorageListener() {
        if (mUnmountReceiver == null) {
            mUnmountReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(final Context context, final Intent intent) {
                    final String action = intent.getAction();
                    if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
                        saveQueue(true);
                        mQueueIsSaveable = false;
                        closeExternalStorageFiles(intent.getData().getPath());
                    } else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                        mMediaMountedCount++;
                        mCardId = getCardId();
                        reloadQueueAfterPermissionCheck();
                        mQueueIsSaveable = true;
                        notifyChange(QUEUE_CHANGED);
                        notifyChange(META_CHANGED);
                    }
                }
            };
            final IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_MEDIA_EJECT);
            filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
            filter.addDataScheme("file");
            registerReceiver(mUnmountReceiver, filter);
        }
    }*/

    private void scheduleDelayedShutdown() {
        if (D) Log.v(TAG, "Scheduling shutdown in " + IDLE_DELAY + " ms");
        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + IDLE_DELAY, mShutdownIntent);
        mShutdownScheduled = true;
    }

    private void cancelShutdown() {
        if (D) Log.d(TAG, "Cancelling delayed shutdown, scheduled = " + mShutdownScheduled);
        if (mShutdownScheduled) {
            mAlarmManager.cancel(mShutdownIntent);
            mShutdownScheduled = false;
        }
    }

    private void stop(final boolean goToIdle) {
        if (D) Log.d(TAG, "Stopping playback, goToIdle = " + goToIdle);
        if (mPlayer.isInitialized()) {
            mPlayer.stop();
        }
        mFileToPlay = null;
        closeCursor();
        if (goToIdle) {
            setIsSupposedToBePlaying(false, false);
        }
// else {
//            if (CommonUtils.isLollipop())
//                stopForeground(false);
//            else stopForeground(true);
//        }
    }

    private int removeTracksInternal(int first, int last) {
        synchronized (this) {
            if (last < first) {
                return 0;
            } else if (first < 0) {
                first = 0;
            } else if (last >= mPlaylist.size()) {
                last = mPlaylist.size() - 1;
            }

            boolean gotonext = false;
            if (first <= mPlayPos && mPlayPos <= last) {
                mPlayPos = first;
                gotonext = true;
            } else if (mPlayPos > last) {
                mPlayPos -= last - first + 1;
            }
            final int numToRemove = last - first + 1;

            if (first == 0 && last == mPlaylist.size() - 1) {
                mPlayPos = -1;
                mNextPlayPos = -1;
                mPlaylist.clear();
                mHistory.clear();
            } else {
                for (int i = 0; i < numToRemove; i++) {
                    mPlaylist.remove(first);
                }

                ListIterator<Integer> positionIterator = mHistory.listIterator();
                while (positionIterator.hasNext()) {
                    int pos = positionIterator.next();
                    if (pos >= first && pos <= last) {
                        positionIterator.remove();
                    } else if (pos > last) {
                        positionIterator.set(pos - numToRemove);
                    }
                }
            }
            if (gotonext) {
                if (mPlaylist.size() == 0) {
                    stop(true);
                    mPlayPos = -1;
                    closeCursor();
                } else {
                    if (mShuffleMode != SHUFFLE_NONE) {
                        mPlayPos = getNextPosition(true);
                    } else if (mPlayPos >= mPlaylist.size()) {
                        mPlayPos = 0;
                    }
                    final boolean wasPlaying = isPlaying();
                    stop(false);
                    openCurrentAndNext();
                    if (wasPlaying) {
                        play();
                    }
                }
                notifyChange(META_CHANGED);
            }
            return last - first + 1;
        }
    }

    private void addToPlayList(final List<MusicInfo> list, int position) {
        final int addlen = list.size();
        Logger.d("Addtoplaylist");
        if (position < 0) {
            mPlaylist.clear();
            position = 0;
        }

        mPlaylist.ensureCapacity(mPlaylist.size() + addlen);
        if (position > mPlaylist.size()) {
            position = mPlaylist.size();
        }

        mPlaylist.addAll(position, list);

        if (mPlaylist.size() == 0) {
            closeCursor();
            Logger.d("AddtoplaylistMETA_CHANGED");
            notifyChange(META_CHANGED);
        }
    }


    private void updateCursor() {

        MusicInfo info = mPlaylist.get(mPlayPos);
        if (info != null) {
            MatrixCursor cursor = new MatrixCursor(PROJECTION);
            cursor.addRow(new Object[]{info.getAudioId(), info.getArtist(), info.getAlbumName(), info.getMusicName()
                    , info.getData(), info.getAlbumData(), info.getAlbumId(), info.getArtistId()});
            cursor.moveToFirst();
            mCursor = cursor;
            cursor.close();
        }
    }

    private void updateCursor(final String selection, final String[] selectionArgs) {
        synchronized (this) {
            closeCursor();
            mCursor = openCursorAndGoToFirst(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    PROJECTION, selection, selectionArgs);
        }
    }

    private void updateCursor(final Uri uri) {
        synchronized (this) {
            closeCursor();
            mCursor = openCursorAndGoToFirst(uri, PROJECTION, null, null);
        }
    }


    private Cursor openCursorAndGoToFirst(Uri uri, String[] projection,
                                          String selection, String[] selectionArgs) {
        Cursor c = getContentResolver().query(uri, projection,
                selection, selectionArgs, null);
        if (c == null) {
            return null;
        }
        if (!c.moveToFirst()) {
            c.close();
            return null;
        }
        return c;
    }

    private synchronized void closeCursor() {
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
        if (mAlbumCursor != null) {
            mAlbumCursor.close();
            mAlbumCursor = null;
        }
    }


    /**
     * 播放网络歌曲的任务类
     */
    class RequestPlayUrl implements Runnable {
        private long id;
        private boolean play;
        private boolean stop;

        private String url;

        public RequestPlayUrl(long id, boolean play) {
            this.id = id;
            this.play = play;
        }

        public void stop() {
            stop = true;
        }

        @Override
        public void run() {
            //从PreferencesUtils获取该音乐的播放地址
            url = PreferencesUtils.getInstance(MediaService.this).getPlayLink(id);
            if(url != null) {
                PrintLog.e(TAG, "current url = " + url);
                if (!stop) {
                    mPlayer.setDataSource(url);
                }
                if (play && !stop) {
                    play();
                }
            } else {
            //如果没有则从网络获取，并把地址存入PreferencesUtils中
                Logger.d("id:" + id);

                try {
                    JsonArray jsonArray = HttpUtils.getResposeJsonObject(HttpMethods.getInstance().songInfoSyn(String.valueOf(id)), MediaService.this, false)
                            .get("songurl")
                            .getAsJsonObject().get("url").getAsJsonArray();
                    Gson gson = new Gson();
                    SongExtraInfo.SongurlBean.UrlBean urlBean = gson.fromJson(jsonArray.get(0), SongExtraInfo.SongurlBean.UrlBean.class);

                    Log.e(TAG, String.valueOf(urlBean.getShow_link() == null));
                    Log.e(TAG, String.valueOf(urlBean.getShow_link()));
                    url = urlBean.getShow_link();
                    PreferencesUtils.getInstance(MediaService.this).setPlayLink(id, url);

                    if (url != null) {
                        PrintLog.e(TAG, "current url = " + url);
                    } else {
                        gotoNext(true);
                    }
                    if (!stop) {
                        mPlayer.setDataSource(url);
                    }

                    if (play && !stop) {
                        play();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }

        }
    }


    /**
     * 获取歌词的任务类
     */
    class RequestLrc implements Runnable {

        private MusicInfo musicInfo;
        private boolean stop;
        String url;

        RequestLrc(MusicInfo info) {
            this.musicInfo = info;
        }

        public void stop() {
            stop = true;
        }

        @Override
        public void run() {
            PrintLog.e(TAG, "start to getlrc");
            //如果已经有歌词地址则获取
            if (musicInfo != null && musicInfo.getLrc() != null && musicInfo.getLrc().length() != 0) {
                url = musicInfo.getLrc();
                PrintLog.e(TAG, "lrcurl:" + url);
                if (!stop) {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + LRC_PATH + musicInfo.getAudioId());
                    String lrc = null;
                    try {
                        //获取歌词连接返回的歌词数据，并写入文件当中,文件在/audioplay/lrc/音频id;
                        lrc = HttpUtils.getResposeString(url);
                        if (lrc != null && !lrc.isEmpty()) {
                            if (!file.exists())
                                file.createNewFile();
                            writeToFile(file, lrc);
                            mPlayerHandler.sendEmptyMessage(LRC_DOWNLOADED);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //没有歌词地址则从网络获取歌词地址
                try {
                    JsonArray jsonArray = HttpUtils.getResposeJsonObject(HttpMethods.getInstance().searchLrcPicSyn(musicInfo.getMusicName(), musicInfo.getArtist()), MediaService.this, false)
                            .get("songinfo").getAsJsonArray();
                    int len = jsonArray.size();
                    url = null;
                    for (int i = 0; i < len; i++) {
                        url = jsonArray.get(i).getAsJsonObject().get("lrclink").getAsString();
                        if (url != null) {
                            PrintLog.e(TAG, "lrclink = " + url);
                            break;
                        }
                    }

                    if (!stop) {
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + LRC_PATH + musicInfo.getAudioId());
                        String lrc = null;
                        try {
                            lrc = HttpUtils.getResposeString(url);
                            if (lrc != null && !lrc.isEmpty()) {
                                if (!file.exists())
                                    file.createNewFile();
                                writeToFile(file, lrc);
                                mPlayerHandler.sendEmptyMessage(LRC_DOWNLOADED);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }

        }
    }


    /**
     * 获取当前音频的歌词
     */
    private void getLrc() {
        MusicInfo info =  mPlaylist.get(mPlayPos);

        if (info == null) {
            Logger.d(TAG, "get lrc err ,musicinfo is null");
        }
        String lrc = Environment.getExternalStorageDirectory().getAbsolutePath() + LRC_PATH;
        File file = new File(lrc);
        Logger.d("file LRC_PATH exists = " + file.exists());
        if (!file.exists()) {
            //不存在就建立此目录
            boolean r = file.mkdirs();
            Logger.d(TAG, "file created = " + r);

        }
        file = new File(lrc + info.getAudioId());
        Logger.d("file LRC_PATH/id exists = " + file.exists());
        PrintLog.e("file path:"+file.getAbsolutePath());
        if (!file.exists()) {
            if (mRequestLrc != null) {
                mRequestLrc.stop();
                mLrcHandler.removeCallbacks(mRequestLrc);
            }
            //把获取歌词任务传递给获取歌词的handler中进行处理
            mRequestLrc = new RequestLrc(info);
            mLrcHandler.postDelayed(mRequestLrc, 70);
        }
    }

    private synchronized void writeToFile(File file, String lrc) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(lrc.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openCurrentAndNextPlay(boolean play) {
        openCurrentAndMaybeNext(play, true);
    }

    private void openCurrentAndNext() {
        openCurrentAndMaybeNext(false, true);
    }


    private void openCurrentAndMaybeNext(final boolean play, final boolean openNext) {
        synchronized (this) {
            if (D) Log.d(TAG, "open current");
            closeCursor();
            stop(false);
            boolean shutdown = false;

            if (mPlaylist.size() == 0  && mPlayPos >= mPlaylist.size()) {
                //clearPlayInfos();
                return;
            }

            updateCursor();
            getLrc();

            //不是本地音乐
            if (!getCurrentTrack().islocal()) {
                if (mRequestUrl != null) {
                    mRequestUrl.stop();
                    mUrlHandler.removeCallbacks(mRequestUrl);
                }
                mRequestUrl = new RequestPlayUrl(getCurrentTrack().getAudioId(), play);
                mUrlHandler.postDelayed(mRequestUrl, 70);
            } else {
                //本地音乐
                while (true) {
                    if (mCursor != null
                            && openFile(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/"
                            + mCursor.getLong(IDCOLIDX))) {
                        break;
                    }

                    closeCursor();
                    if (mOpenFailedCounter++ < 10 && mPlaylist.size() > 1) {
                        final int pos = getNextPosition(false);
                        if (pos < 0) {
                            shutdown = true;
                            break;
                        }
                        mPlayPos = pos;
                        stop(false);
                        mPlayPos = pos;
                        updateCursor();
                    } else {
                        mOpenFailedCounter = 0;
                        Log.w(TAG, "Failed to open file for playback");
                        shutdown = true;
                        break;
                    }
                }
            }

            if (shutdown) {
                scheduleDelayedShutdown();
                if (mIsSupposedToBePlaying) {
                    mIsSupposedToBePlaying = false;
                    notifyChange(PLAYSTATE_CHANGED);
                    Logger.d("openCurrentAndMaybeNext");
                }
            } else if (openNext) {
                setNextTrack();
            }
        }
    }

    /*
    private void sendErrorMessage(final String trackName) {
        final Intent i = new Intent(TRACK_ERROR);
        i.putExtra(TrackErrorExtra.TRACK_NAME, trackName);
        sendBroadcast(i);
    }*/

    private int getNextPosition(final boolean force) {
        if (mPlaylist == null || mPlaylist.isEmpty()) {
            return -1;
        }
        if (!force && mRepeatMode == REPEAT_CURRENT) {
            if (mPlayPos < 0) {
                return 0;
            }
            return mPlayPos;
        } else if (mShuffleMode == SHUFFLE_NORMAL) {
            final int numTracks = mPlaylist.size();


            final int[] trackNumPlays = new int[numTracks];
            for (int i = 0; i < numTracks; i++) {
                trackNumPlays[i] = 0;
            }


            final int numHistory = mHistory.size();
            for (int i = 0; i < numHistory; i++) {
                final int idx = mHistory.get(i).intValue();
                if (idx >= 0 && idx < numTracks) {
                    trackNumPlays[idx]++;
                }
            }

            if (mPlayPos >= 0 && mPlayPos < numTracks) {
                trackNumPlays[mPlayPos]++;
            }

            int minNumPlays = Integer.MAX_VALUE;
            int numTracksWithMinNumPlays = 0;
            for (int i = 0; i < trackNumPlays.length; i++) {
                if (trackNumPlays[i] < minNumPlays) {
                    minNumPlays = trackNumPlays[i];
                    numTracksWithMinNumPlays = 1;
                } else if (trackNumPlays[i] == minNumPlays) {
                    numTracksWithMinNumPlays++;
                }
            }


            if (minNumPlays > 0 && numTracksWithMinNumPlays == numTracks
                    && mRepeatMode != REPEAT_ALL && !force) {
                return -1;
            }


            int skip = mShuffler.nextInt(numTracksWithMinNumPlays);
            for (int i = 0; i < trackNumPlays.length; i++) {
                if (trackNumPlays[i] == minNumPlays) {
                    if (skip == 0) {
                        return i;
                    } else {
                        skip--;
                    }
                }
            }

            if (D)
                Log.e(TAG, "Getting the next position resulted did not get a result when it should have");
            return -1;
        } else {
            if (mPlayPos >= mPlaylist.size() - 1) {
                if (mRepeatMode == REPEAT_NONE && !force) {
                    return -1;
                } else if (mRepeatMode == REPEAT_ALL || force) {
                    return 0;
                }
                return -1;
            } else {
                return mPlayPos + 1;
            }
        }
    }

    private void setNextTrack() {
        setNextTrack(getNextPosition(false));
    }

    private void setNextTrack(int position) {
        mNextPlayPos = position;
        if (D) Log.d(TAG, "setNextTrack: next play position = " + mNextPlayPos);
        if (mNextPlayPos >= 0 && mPlaylist != null && mNextPlayPos < mPlaylist.size()) {
            final int id = mPlaylist.get(mNextPlayPos).getAudioId();
                if (mPlaylist.get(mNextPlayPos).islocal()) {
                    //如果下一首歌曲是本地的话就设置数据源
                    mPlayer.setNextDataSource(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/" + id);
                } else {
                    //如果不是本地歌曲则不设置
                    mPlayer.setNextDataSource(null);
                }
        } else {
            mPlayer.setNextDataSource(null);
        }
    }

    private void sendUpdateBuffer(int progress) {
        Intent intent = new Intent(BUFFER_UP);
        intent.putExtra("progress", progress);
        sendBroadcast(intent);
    }

    private void notifyChange(final String what) {
        if (D) Log.d(TAG, "notifyChange: what = " + what);
        if (SEND_PROGRESS.equals(what)) {
            final Intent intent = new Intent(what);
            intent.putExtra("position", position());
            intent.putExtra("duration", duration());
            sendStickyBroadcast(intent);
            return;
        }

        // Update the lockscreen controls
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            updateMediaSession(what);

        if (what.equals(POSITION_CHANGED)) {
            return;
        }

        final Intent intent = new Intent(what);
        intent.putExtra("id", getAudioId());
        intent.putExtra("artist", getArtistName());
        intent.putExtra("album", getAlbumName());
        intent.putExtra("track", getTrackName());
        intent.putExtra("playing", isPlaying());
        intent.putExtra("albumuri", getAlbumPath());
        intent.putExtra("islocal", isTrackLocal());

        //sendStickyBroadcast(intent);
        sendBroadcast(intent);
        final Intent musicIntent = new Intent(intent);
        musicIntent.setAction(what.replace(TIMBER_PACKAGE_NAME, MUSIC_PACKAGE_NAME));
        //sendStickyBroadcast(musicIntent);
        sendBroadcast(musicIntent);
//        if (what.equals(TRACK_PREPARED)) {
//            return;
//        }

        //如果切换了歌曲，那么把当前歌曲加入到最近播放数据库中
        if (what.equals(META_CHANGED)) {
            if(getCurrentTrack() != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RecentMusicDB.getInstance(MyApplication.getContext()).insertRecentSong(getCurrentTrack());
                    }
                }).start();

            }
            //mSongPlayCount.bumpSongCount(getAudioId());

        } else if (what.equals(QUEUE_CHANGED)) {
            Intent intent1 = new Intent("emptyplaylist");
            intent.putExtra("showorhide", "show");
            sendBroadcast(intent1);
            //saveQueue(true);
            if (isPlaying()) {

                if (mNextPlayPos >= 0 && mNextPlayPos < mPlaylist.size()
                        && getShuffleMode() != SHUFFLE_NONE) {
                    setNextTrack(mNextPlayPos);
                } else {
                    setNextTrack();
                }
            }
        } else {
            //saveQueue(false);
        }

        if (what.equals(PLAYSTATE_CHANGED)) {
            //updateNotification();
        }

    }

    private void updateMediaSession(final String what) {
        int playState = mIsSupposedToBePlaying
                ? PlaybackState.STATE_PLAYING
                : PlaybackState.STATE_PAUSED;

        if (what.equals(PLAYSTATE_CHANGED) || what.equals(POSITION_CHANGED)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mSession.setPlaybackState(new PlaybackState.Builder()
                        .setState(playState, position(), 1.0f)
                        .setActions(PlaybackState.ACTION_PLAY | PlaybackState.ACTION_PAUSE | PlaybackState.ACTION_PLAY_PAUSE |
                                PlaybackState.ACTION_SKIP_TO_NEXT | PlaybackState.ACTION_SKIP_TO_PREVIOUS)
                        .build());
                Logger.d("updateMediaSession");
            }
        } else if (what.equals(META_CHANGED) || what.equals(QUEUE_CHANGED)) {
            //Bitmap albumArt = ImageLoader.getInstance().loadImageSync(CommonUtils.getAlbumArtUri(getAlbumId()).toString());
            Bitmap albumArt = null;
            if (albumArt != null) {

                Bitmap.Config config = albumArt.getConfig();
                if (config == null) {
                    config = Bitmap.Config.ARGB_8888;
                }
                albumArt = albumArt.copy(config, false);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mSession.setMetadata(new MediaMetadata.Builder()
                        .putString(MediaMetadata.METADATA_KEY_ARTIST, getArtistName())
                        .putString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST, getAlbumArtistName())
                        .putString(MediaMetadata.METADATA_KEY_ALBUM, getAlbumName())
                        .putString(MediaMetadata.METADATA_KEY_TITLE, getTrackName())
                        .putLong(MediaMetadata.METADATA_KEY_DURATION, duration())
                        .putLong(MediaMetadata.METADATA_KEY_TRACK_NUMBER, getQueuePosition() + 1)
                        .putLong(MediaMetadata.METADATA_KEY_NUM_TRACKS, getQueue().size())
                        .putString(MediaMetadata.METADATA_KEY_GENRE, getGenreName())
                        .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART,
                                mShowAlbumArtOnLockscreen ? albumArt : null)
                        .build());

                mSession.setPlaybackState(new PlaybackState.Builder()
                        .setState(playState, position(), 1.0f)
                        .setActions(PlaybackState.ACTION_PLAY | PlaybackState.ACTION_PAUSE | PlaybackState.ACTION_PLAY_PAUSE |
                                PlaybackState.ACTION_SKIP_TO_NEXT | PlaybackState.ACTION_SKIP_TO_PREVIOUS)
                        .build());
            }
        }
    }

    /*
    private Notification getNotification() {
        RemoteViews remoteViews;
        final int PAUSE_FLAG = 0x1;
        final int NEXT_FLAG = 0x2;
        final int STOP_FLAG = 0x3;
        final String albumName = getAlbumName();
        final String artistName = getArtistName();
        final boolean isPlaying = isPlaying();

        remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification);
        String text = TextUtils.isEmpty(albumName) ? artistName : artistName + " - " + albumName;
        remoteViews.setTextViewText(R.id.title, getTrackName());
        remoteViews.setTextViewText(R.id.text, text);

        //此处action不能是一样的 如果一样的 接受的flag参数只是第一个设置的值
        Intent pauseIntent = new Intent(TOGGLEPAUSE_ACTION);
        pauseIntent.putExtra("FLAG", PAUSE_FLAG);
        PendingIntent pausePIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, 0);
        remoteViews.setImageViewResource(R.id.iv_pause, isPlaying ? R.drawable.note_btn_pause : R.drawable.note_btn_play);
        remoteViews.setOnClickPendingIntent(R.id.iv_pause, pausePIntent);

        Intent nextIntent = new Intent(NEXT_ACTION);
        nextIntent.putExtra("FLAG", NEXT_FLAG);
        PendingIntent nextPIntent = PendingIntent.getBroadcast(this, 0, nextIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.iv_next, nextPIntent);

        Intent preIntent = new Intent(STOP_ACTION);
        preIntent.putExtra("FLAG", STOP_FLAG);
        PendingIntent prePIntent = PendingIntent.getBroadcast(this, 0, preIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.iv_stop, prePIntent);

//        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0,
//                new Intent(this.getApplicationContext(), PlayingActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        final Intent nowPlayingIntent = new Intent();
        //nowPlayingIntent.setAction("LAUNCH_NOW_PLAYING_ACTION");
        nowPlayingIntent.setComponent(new ComponentName("com.example.administrator.audioplay", "activity.PlayingActivity"));
        nowPlayingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent clickIntent = PendingIntent.getBroadcast(this, 0, nowPlayingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent click = PendingIntent.getActivity(this, 0, nowPlayingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final Bitmap bitmap = ImageUtils.getArtworkQuick(this, getAlbumId(), 160, 160);
        if (bitmap != null) {
            remoteViews.setImageViewBitmap(R.id.image, bitmap);
            // remoteViews.setImageViewUri(R.id.image, MusicUtils.getAlbumUri(this, getAudioId()));
            mNoBit = null;

        } else if (!isTrackLocal()) {
            if (mNoBit != null) {
                remoteViews.setImageViewBitmap(R.id.image, mNoBit);
                mNoBit = null;

            } else {
                Uri uri = null;
                if (getAlbumPath() != null) {
                    try {
                        uri = Uri.parse(getAlbumPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (getAlbumPath() == null || uri == null) {
                    mNoBit = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_disk_210);
                    updateNotification();
                } else {
                    ImageRequest imageRequest = ImageRequestBuilder
                            .newBuilderWithSource(uri)
                            .setProgressiveRenderingEnabled(true)
                            .build();
                    ImagePipeline imagePipeline = Fresco.getImagePipeline();
                    DataSource<CloseableReference<CloseableImage>>
                            dataSource = imagePipeline.fetchDecodedImage(imageRequest, MediaService.this);

                    dataSource.subscribe(new BaseBitmapDataSubscriber() {

                                             @Override
                                             public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                                 // You can use the bitmap in only limited ways
                                                 // No need to do any cleanup.
                                                 if (bitmap != null) {
                                                     mNoBit = bitmap;
                                                 }
                                                 updateNotification();
                                             }

                                             @Override
                                             public void onFailureImpl(DataSource dataSource) {
                                                 // No cleanup required here.
                                                 mNoBit = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_disk_210);
                                                 updateNotification();
                                             }
                                         },
                            CallerThreadExecutor.getInstance());
                }
            }

        } else {
            remoteViews.setImageViewResource(R.id.image, R.drawable.placeholder_disk_210);
        }


        if (mNotificationPostTime == 0) {
            mNotificationPostTime = System.currentTimeMillis();
        }
        if (mNotification == null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setContent(remoteViews)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentIntent(click)
                    .setWhen(mNotificationPostTime);
            if (CommonUtils.isJellyBeanMR1()) {
                builder.setShowWhen(false);
            }
            mNotification = builder.build();
        } else {
            mNotification.contentView = remoteViews;
        }

        return mNotification;
    }
    */

    private final PendingIntent retrievePlaybackAction(final String action) {
        final ComponentName serviceName = new ComponentName(this, MediaService.class);
        Intent intent = new Intent(action);
        intent.setComponent(serviceName);

        return PendingIntent.getService(this, 0, intent, 0);
    }

    /*
    private void saveQueue(final boolean full) {
        if (!mQueueIsSaveable) {
            return;
        }

        final SharedPreferences.Editor editor = mPreferences.edit();
        if (full) {
            mPlaybackStateStore.saveState(mPlaylist, mShuffleMode != SHUFFLE_NONE ? mHistory : null);
            if (mPlaylistInfo.size() > 0) {
                String temp = MainApplication.gsonInstance().toJson(mPlaylistInfo);
                try {
                    File file = new File(getCacheDir().getAbsolutePath() + "playlist");
                    RandomAccessFile ra = new RandomAccessFile(file, "rws");
                    ra.write(temp.getBytes());
                    ra.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            editor.putInt("cardid", mCardId);

        }
        editor.putInt("curpos", mPlayPos);
        if (mPlayer.isInitialized()) {
            editor.putLong("seekpos", mPlayer.position());
        }
        editor.putInt("repeatmode", mRepeatMode);
        editor.putInt("shufflemode", mShuffleMode);
        editor.apply();
    }*/
    /*
    private void reloadQueueAfterPermissionCheck() {
        if (CommonUtils.isMarshmallow()) {
            if (Nammu.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                reloadQueue();
            }
        } else {
            reloadQueue();
        }
    }*/

    private String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer();
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();
    }

    /*
    private void clearPlayInfos() {
        File file = new File(getCacheDir().getAbsolutePath() + "playlist");
        if (file.exists()) {
            file.delete();
        }
        MusicPlaybackState.getInstance(this).clearQueue();
    }*/

    /*
    private void reloadQueue() {
        int id = mCardId;
        if (mPreferences.contains("cardid")) {
            id = mPreferences.getInt("cardid", ~mCardId);
        }
        if (id == mCardId) {
            mPlaylist = mPlaybackStateStore.getQueue();
            try {
                FileInputStream in = new FileInputStream(new File(getCacheDir().getAbsolutePath() + "playlist"));
                String c = readTextFromSDcard(in);
                HashMap<Long, MusicInfo> play = MainApplication.gsonInstance().fromJson(c, new TypeToken<HashMap<Long, MusicInfo>>() {
                }.getType());
                if (play != null && play.size() > 0) {
                    mPlaylistInfo = play;
                    Logger.d( TAG, mPlaylistInfo.keySet().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ((mPlaylist.size() == mPlaylistInfo.size()) && mPlaylist.size() > 0) {
            final int pos = mPreferences.getInt("curpos", 0);
            if (pos < 0 || pos >= mPlaylist.size()) {
                mPlaylist.clear();
                return;
            }
            mPlayPos = pos;
            updateCursor(mPlaylist.get(mPlayPos).mId);
            if (mCursor == null) {
                SystemClock.sleep(3000);
                updateCursor(mPlaylist.get(mPlayPos).mId);
            }
            synchronized (this) {
                closeCursor();
                mOpenFailedCounter = 20;
                openCurrentAndNext();
            }

//            if (!mPlayer.isInitialized() && isTrackLocal()) {
//                mPlaylist.clear();
//                return;
//            }
            final long seekpos = mPreferences.getLong("seekpos", 0);
            mLastSeekPos = seekpos;
            seek(seekpos >= 0 && seekpos < duration() ? seekpos : 0);

            if (D) {
                Log.d(TAG, "restored queue, currently at position "
                        + position() + "/" + duration()
                        + " (requested " + seekpos + ")");
            }

            int repmode = mPreferences.getInt("repeatmode", REPEAT_ALL);
            if (repmode != REPEAT_ALL && repmode != REPEAT_CURRENT) {
                repmode = REPEAT_NONE;
            }
            mRepeatMode = repmode;

            int shufmode = mPreferences.getInt("shufflemode", SHUFFLE_NONE);
            if (shufmode != SHUFFLE_AUTO && shufmode != SHUFFLE_NORMAL) {
                shufmode = SHUFFLE_NONE;
            }
            if (shufmode != SHUFFLE_NONE) {
                mHistory = mPlaybackStateStore.getHistory(mPlaylist.size());
            }
            if (shufmode == SHUFFLE_AUTO) {
                if (!makeAutoShuffleList()) {
                    shufmode = SHUFFLE_NONE;
                }
            }
            mShuffleMode = shufmode;
        } else {
            clearPlayInfos();
        }
        notifyChange(MUSIC_CHANGED);
    }*/

    public boolean openFile(final String path) {
        if (D) Log.d(TAG, "openFile: path = " + path);
        synchronized (this) {
            if (path == null) {
                return false;
            }

            if (mCursor == null) {
                Uri uri = Uri.parse(path);
                boolean shouldAddToPlaylist = true;
                long id = -1;
                try {
                    id = Long.valueOf(uri.getLastPathSegment());
                } catch (NumberFormatException ex) {
                    // Ignore
                }

                if (id != -1 && path.startsWith(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString())) {
                    updateCursor(uri);

                } else if (id != -1 && path.startsWith(
                        MediaStore.Files.getContentUri("external").toString())) {
                    updateCursor();

                } else if (path.startsWith("content://downloads/")) {

                    String mpUri = getValueForDownloadedFile(this, uri, "mediaprovider_uri");
                    if (D) Log.i(TAG, "Downloaded file's MP uri : " + mpUri);
                    if (!TextUtils.isEmpty(mpUri)) {
                        if (openFile(mpUri)) {
                            notifyChange(META_CHANGED);
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        updateCursorForDownloadedFile(this, uri);
                        shouldAddToPlaylist = false;
                    }

                } else {
                    String where = MediaStore.Audio.Media.DATA + "=?";
                    String[] selectionArgs = new String[]{path};
                    updateCursor(where, selectionArgs);
                }
                try {
                    if (mCursor != null && shouldAddToPlaylist) {
                        mPlaylist.clear();
                        //mPlaylist.add(new MusicInfo(mCursor.getLong(IDCOLIDX), -1));
                        notifyChange(QUEUE_CHANGED);
                        mPlayPos = 0;
                        mHistory.clear();
                    }
                } catch (final UnsupportedOperationException ex) {
                    // Ignore
                }
            }

            mFileToPlay = path;
            mPlayer.setDataSource(mFileToPlay);
            if (mPlayer.isInitialized()) {
                mOpenFailedCounter = 0;
                return true;
            }

            String trackName = getTrackName();
            if (TextUtils.isEmpty(trackName)) {
                trackName = path;
            }
            //sendErrorMessage(trackName);

            stop(true);
            return false;
        }
    }

    private void updateCursorForDownloadedFile(Context context, Uri uri) {
        synchronized (this) {
            closeCursor();
            MatrixCursor cursor = new MatrixCursor(PROJECTION_MATRIX);
            String title = getValueForDownloadedFile(this, uri, "title");
            cursor.addRow(new Object[]{
                    null,
                    null,
                    null,
                    title,
                    null,
                    null,
                    null,
                    null
            });
            mCursor = cursor;
            mCursor.moveToFirst();
        }
    }

    private String getValueForDownloadedFile(Context context, Uri uri, String column) {

        Cursor cursor = null;
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public int getAudioSessionId() {
        synchronized (this) {
            return mPlayer.getAudioSessionId();
        }
    }

    public int getMediaMountedCount() {
        return mMediaMountedCount;
    }

    public int getShuffleMode() {
        return mShuffleMode;
    }

        public void setShuffleMode(final int shufflemode) {
        synchronized (this) {
            if (mShuffleMode == shufflemode && mPlaylist.size() > 0) {
                return;
            }

            mShuffleMode = shufflemode;
            setNextTrack();

            //saveQueue(false);
            notifyChange(SHUFFLEMODE_CHANGED);
        }
    }

    public int getRepeatMode() {
        return mRepeatMode;
    }

    public void setRepeatMode(final int repeatmode) {
        synchronized (this) {
            mRepeatMode = repeatmode;
            setNextTrack();
            //saveQueue(false);
            notifyChange(REPEATMODE_CHANGED);
        }
    }

    public int removeTrack(final long id) {
        int numremoved = 0;
        synchronized (this) {
            for (int i = 0; i < mPlaylist.size(); i++) {
                if (mPlaylist.get(i).getAudioId() == id) {
                    numremoved += removeTracksInternal(i, i);
                    i--;
                }
            }

        }

        if (numremoved > 0) {
            notifyChange(QUEUE_CHANGED);
        }
        return numremoved;
    }

    public boolean removeTrackAtPosition(final long id, final int position) {
        synchronized (this) {
            if (position >= 0 &&
                    position < mPlaylist.size() &&
                    mPlaylist.get(position).getAudioId() == id) {
                return removeTracks(position, position) > 0;
            }

        }
        return false;
    }

    public int removeTracks(final int first, final int last) {
        final int numremoved = removeTracksInternal(first, last);
        if (numremoved > 0) {
            notifyChange(QUEUE_CHANGED);
        }
        return numremoved;
    }

    public int getQueuePosition() {
        synchronized (this) {
            return mPlayPos;
        }
    }

    public void setQueuePosition(final int index) {
        synchronized (this) {
            stop(false);
            mPlayPos = index;
            openCurrentAndNext();
            play();
            notifyChange(META_CHANGED);

        }
    }

    public int getQueueHistorySize() {
        synchronized (this) {
            return mHistory.size();
        }
    }

    public int getQueueHistoryPosition(int position) {
        synchronized (this) {
            if (position >= 0 && position < mHistory.size()) {
                return mHistory.get(position);
            }
        }

        return -1;
    }

    public int[] getQueueHistoryList() {
        synchronized (this) {
            int[] history = new int[mHistory.size()];
            for (int i = 0; i < mHistory.size(); i++) {
                history[i] = mHistory.get(i);
            }

            return history;
        }
    }

    public String getPath() {
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA));
        }
    }

    public String getAlbumName() {
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            try {
                //如果是null的话会抛出异常
                return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM));
            } catch (Exception e) {
                return null;
            }
        }
    }


    public String getAlbumPath() {
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            try {
                return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.MIME_TYPE));
            } catch (Exception e) {
                return null;
            }

        }
    }


    public String[] getAlbumPathAll() {
        synchronized (this) {
            try {
                int len = mPlaylist.size();
                String[] albums = new String[len];
                for (int i = 0; i < len; i++) {
                    albums[i] = mPlaylist.get(i).getAlbumData();
                }
                return albums;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new String[]{};
        }
    }

    public String getTrackName() {
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE));
        }
    }

    public boolean isTrackLocal() {
        synchronized (this) {
            MusicInfo info = getCurrentTrack();
            if (info == null) {
                return true;
            }
            return info.islocal();
        }
    }



    public String getGenreName() {
        synchronized (this) {
            if (mCursor == null || mPlayPos < 0 || mPlayPos >= mPlaylist.size()) {
                return null;
            }
            String[] genreProjection = {MediaStore.Audio.Genres.NAME};
            Uri genreUri = MediaStore.Audio.Genres.getContentUriForAudioId("external",
                    (int) mPlaylist.get(mPlayPos).getAudioId());
            Cursor genreCursor = getContentResolver().query(genreUri, genreProjection,
                    null, null, null);
            if (genreCursor != null) {
                try {
                    if (genreCursor.moveToFirst()) {
                        return genreCursor.getString(
                                genreCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME));
                    }
                } finally {
                    genreCursor.close();
                }
            }
            return null;
        }
    }

    public String getArtistName() {
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST));
        }
    }

    public String getAlbumArtistName() {
        synchronized (this) {
            if (mAlbumCursor == null) {
                return null;
            }
            return mAlbumCursor.getString(mAlbumCursor.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.ARTIST));
        }
    }

    public long getAlbumId() {
        synchronized (this) {
            if (mCursor == null) {
                return -1;
            }
            return mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID));
        }
    }

    public long getArtistId() {
        synchronized (this) {
            if (mCursor == null) {
                return -1;
            }
            return mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID));
        }
    }

    public int getAudioId() {
        MusicInfo music = getCurrentTrack();
        if (music != null) {
            return music.getAudioId();
        }

        return -1;
    }

    public MusicInfo getCurrentTrack() {
        return getTrack(mPlayPos);
    }

    public synchronized MusicInfo getTrack(int index) {
        if (index >= 0 && index < mPlaylist.size()) {
            return mPlaylist.get(index);
        }

        return null;
    }
    /*
    public long getNextAudioId() {
        synchronized (this) {
            if (mNextPlayPos >= 0 && mNextPlayPos < mPlaylist.size() && mPlayer.isInitialized()) {
                return mPlaylist.get(mNextPlayPos).mId;
            }
        }
        return -1;
    }*/

    /*
    public long getPreviousAudioId() {
        synchronized (this) {
            if (mPlayer.isInitialized()) {
                int pos = getPreviousPlayPosition(false);
                if (pos >= 0 && pos < mPlaylist.size()) {
                    return mPlaylist.get(pos).mId;
                }
            }
        }
        return -1;
    }*/

    public long seek(long position) {
        if (mPlayer.isInitialized()) {
            if (position < 0) {
                position = 0;
            } else if (position > mPlayer.duration()) {
                position = mPlayer.duration();
            }
            long result = mPlayer.seek(position);
            notifyChange(POSITION_CHANGED);
            return result;
        }
        return -1;
    }

    /*
    public void seekRelative(long deltaInMs) {
        synchronized (this) {
            if (mPlayer.isInitialized()) {
                final long newPos = position() + deltaInMs;
                final long duration = duration();
                if (newPos < 0) {
                    prev(true);
                    // seek to the new duration + the leftover position
                    seek(duration() + newPos);
                } else if (newPos >= duration) {
                    gotoNext(true);
                    // seek to the leftover duration
                    seek(newPos - duration);
                } else {
                    seek(newPos);
                }
            }
        }
    }*/

    public long position() {
        if (mPlayer.isInitialized() && mPlayer.isTrackPrepared()) {
            return mPlayer.position();
        }
        return -1;
    }

    public int getSecondPosition() {
        if (mPlayer.isInitialized()) {
            return mPlayer.sencondaryPosition;
        }
        return -1;
    }


    public long duration() {
        if (mPlayer.isInitialized() && mPlayer.isTrackPrepared()) {
            return mPlayer.duration();
        }
        return -1;
    }



    public List<MusicInfo> getQueue() {
        synchronized (this) {
            /*
            final int len = mPlaylist.size();
            final long[] list = new long[len];
            for (int i = 0; i < len; i++) {
                list[i] = mPlaylist.get(i).getAudioId();
            }*/
            return mPlaylist;
        }
    }

    public long getQueueItemAtPosition(int position) {
        synchronized (this) {
            if (position >= 0 && position < mPlaylist.size()) {
                return mPlaylist.get(position).getAudioId();
            }
        }

        return -1;
    }

    public int getQueueSize() {
        synchronized (this) {
            return mPlaylist.size();
        }
    }

    public boolean isPlaying() {
        return mIsSupposedToBePlaying;
    }

    private void setIsSupposedToBePlaying(boolean value, boolean notify) {
        if (mIsSupposedToBePlaying != value) {
            mIsSupposedToBePlaying = value;


            if (!mIsSupposedToBePlaying) {
                scheduleDelayedShutdown();
                mLastPlayedTime = System.currentTimeMillis();
            }

            if (notify) {
                notifyChange(PLAYSTATE_CHANGED);
                Logger.d("setIsSupposedToBePlaying");
            }
        }
    }

    private boolean recentlyPlayed() {
        return isPlaying() || System.currentTimeMillis() - mLastPlayedTime < IDLE_DELAY;
    }


    /**
     * 创建播放列表
     * @param list
     * @param position
     */
    public void open(final List<MusicInfo> list, final int position) {
        synchronized (this) {

            final long oldId = getAudioId();
            final int listlength = list.size();
            boolean newlist = true;
            if (mPlaylist.size() == listlength) {
                newlist = false;
                for (int i = 0; i < listlength; i++) {
                    if (list.get(i) != mPlaylist.get(i)) {
                        newlist = true;
                        break;
                    }
                }
            }
            if (newlist) {
                addToPlayList(list, -1);
                notifyChange(QUEUE_CHANGED);
                Logger.d("QUEUE_CHANGED");
            }
            if (position >= 0) {
                mPlayPos = position;
            } else {
                mPlayPos = mShuffler.nextInt(mPlaylist.size());
            }


            mHistory.clear();
            openCurrentAndNextPlay(true);
            if (oldId != getAudioId()) {
                notifyChange(META_CHANGED);
                Logger.d("META_CHANGED");
            }
        }
    }

    public void stop() {
        stop(true);
    }

    public void play() {
        play(true);
    }

    /**
     * 开始播放
     * @param createNewNextTrack
     */
    public void play(boolean createNewNextTrack) {
        int status = mAudioManager.requestAudioFocus(mAudioFocusListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (D) Log.d(TAG, "Starting playback: audio focus request status = " + status);

        if (status != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return;
        }

        final Intent intent = new Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION);
        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
        sendBroadcast(intent);

        //mAudioManager.registerMediaButtonEventReceiver(new ComponentName(getPackageName(),
        //        MediaButtonIntentReceiver.class.getName()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mSession.setActive(true);
        if (createNewNextTrack) {
            setNextTrack();
        } else {
            setNextTrack(mNextPlayPos);
        }
        if (mPlayer.isTrackPrepared()) {
            final long duration = mPlayer.duration();
            if (mRepeatMode != REPEAT_CURRENT && duration > 2000
                    && mPlayer.position() >= duration - 2000) {
                //gotoNext(true);
            }
        }
        mPlayer.start();
        mPlayerHandler.removeMessages(FADEDOWN);
        mPlayerHandler.sendEmptyMessage(FADEUP);
        setIsSupposedToBePlaying(true, true);
        cancelShutdown();
        //updateNotification();
        notifyChange(META_CHANGED);
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (D) Log.d(TAG, "Pausing playback");
        synchronized (this) {
            mPlayerHandler.removeMessages(FADEUP);
            if (mIsSupposedToBePlaying) {
                final Intent intent = new Intent(
                        AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION);
                intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
                intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
                sendBroadcast(intent);

                mPlayer.pause();

                setIsSupposedToBePlaying(false, true);
                notifyChange(META_CHANGED);
            }
        }
    }



    public void next() throws RemoteException {
        gotoNext(true);
    }


    public void gotoNext(final boolean force) {
        if (D) Log.d(TAG, "Going to next track");
        synchronized (this) {
            if (mPlaylist.size() <= 0) {
                if (D) Log.d(TAG, "No play queue");
                scheduleDelayedShutdown();
                return;
            }

            int pos = mNextPlayPos;
            if (pos < 0) {
                pos = getNextPosition(force);

            }

            if (pos < 0) {
                setIsSupposedToBePlaying(false, true);
                return;
            }
            Logger.d("mNextPlayPos:" + pos);
            stop(false);
            setAndRecordPlayPos(pos);
            openCurrentAndNext();
            play();
            notifyChange(META_CHANGED);

        }
    }


    public void setAndRecordPlayPos(int nextPos) {
        synchronized (this) {

            if (mShuffleMode != SHUFFLE_NONE) {
                mHistory.add(mPlayPos);
                if (mHistory.size() > MAX_HISTORY_SIZE) {
                    mHistory.remove(0);
                }
            }

            mPlayPos = nextPos;
        }
    }


    public void prev() {
        synchronized (this) {


            boolean goPrevious = getRepeatMode() != REPEAT_CURRENT;

            if (goPrevious) {
                if (D) Log.d(TAG, "Going to previous track");
                int pos = getPreviousPlayPosition(true);

                if (pos < 0) {
                    return;
                }
                mNextPlayPos = mPlayPos;
                mPlayPos = pos;
                stop(false);
                openCurrent();
                play(false);
                notifyChange(META_CHANGED);

            } else {
                if (D) Log.d(TAG, "Going to beginning of track");
                seek(0);
                play(false);
            }
        }
    }

    public int getPreviousPlayPosition(boolean removeFromHistory) {
        synchronized (this) {
            if (mShuffleMode == SHUFFLE_NORMAL) {

                final int histsize = mHistory.size();
                if (histsize == 0) {
                    return -1;
                }
                final Integer pos = mHistory.get(histsize - 1);
                if (removeFromHistory) {
                    mHistory.remove(histsize - 1);
                }
                return pos.intValue();
            } else {
                if (mPlayPos > 0) {
                    return mPlayPos - 1;
                } else {
                    return mPlaylist.size() - 1;
                }
            }
        }
    }


    private void openCurrent() {
        openCurrentAndMaybeNext(false, false);
    }

    /*
    public void moveQueueItem(int index1, int index2) {
        synchronized (this) {
            if (index1 >= mPlaylist.size()) {
                index1 = mPlaylist.size() - 1;
            }
            if (index2 >= mPlaylist.size()) {
                index2 = mPlaylist.size() - 1;
            }

            if (index1 == index2) {
                return;
            }
            mPlaylistInfo.remove(mPlaylist.get(index1).mId);
            final MusicInfo track = mPlaylist.remove(index1);
            if (index1 < index2) {
                mPlaylist.add(index2, track);
                if (mPlayPos == index1) {
                    mPlayPos = index2;
                } else if (mPlayPos >= index1 && mPlayPos <= index2) {
                    mPlayPos--;
                }
            } else if (index2 < index1) {
                mPlaylist.add(index2, track);
                if (mPlayPos == index1) {
                    mPlayPos = index2;
                } else if (mPlayPos >= index2 && mPlayPos <= index1) {
                    mPlayPos++;
                }
            }
            notifyChange(QUEUE_CHANGED);
        }
    }

    public void enqueue(final long[] list, final HashMap<Long, MusicInfo> map, final int action) {
        synchronized (this) {
            mPlaylistInfo.putAll(map);
            if (action == NEXT && mPlayPos + 1 < mPlaylist.size()) {
                addToPlayList(list, mPlayPos + 1);
                mNextPlayPos = mPlayPos + 1;
                notifyChange(QUEUE_CHANGED);
            } else {
                addToPlayList(list, Integer.MAX_VALUE);
                notifyChange(QUEUE_CHANGED);
            }

            if (mPlayPos < 0) {
                mPlayPos = 0;
                openCurrentAndNext();
                play();
                notifyChange(META_CHANGED);
            }
        }
    }

    private void cycleRepeat() {
        if (mRepeatMode == REPEAT_NONE) {
            setRepeatMode(REPEAT_CURRENT);
            if (mShuffleMode != SHUFFLE_NONE) {
                setShuffleMode(SHUFFLE_NONE);
            }
        } else {
            setRepeatMode(REPEAT_NONE);
        }
    }

    private void cycleShuffle() {
        if (mShuffleMode == SHUFFLE_NONE) {
            setShuffleMode(SHUFFLE_NORMAL);
            if (mRepeatMode == REPEAT_CURRENT) {
                setRepeatMode(REPEAT_ALL);
            }
        } else if (mShuffleMode == SHUFFLE_NORMAL || mShuffleMode == SHUFFLE_AUTO) {
            setShuffleMode(SHUFFLE_NONE);
        }
    }

    public void refresh() {
        notifyChange(REFRESH);
    }

    public void playlistChanged() {
        notifyChange(PLAYLIST_CHANGED);
    }

    public void loading(boolean l) {
        Intent intent = new Intent(MUSIC_LODING);
        intent.putExtra("isloading", l);
        sendBroadcast(intent);
    }

    public void setLockscreenAlbumArt(boolean enabled) {
        mShowAlbumArtOnLockscreen = enabled;
        notifyChange(META_CHANGED);
    }

    public void timing(int time) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(PAUSE_ACTION),
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC, System.currentTimeMillis() + time, pendingIntent);

    }


    public interface TrackErrorExtra {

        String TRACK_NAME = "trackname";
    }
    */


    /**
     *
     * MultiPlayer类，包括两个MediaPlayer，一个现在MediaPlayer，一个下个MediaPlayer，提供对Media的实际操作
     *
     */
    private class MultiPlayer implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {


        private WeakReference<MediaService> mService;

        private MediaPlayer mCurrentMediaPlayer = new MediaPlayer();

        private MediaPlayer mNextMediaPlayer;

        private boolean mIsInitialized = false;

        private String mNextMediaPath;

        private boolean isFirstLoad = true;

        private int sencondaryPosition = 0;

        //MusicPlayerHandler，由外部MediaService设置进来，用来传递状态
        private Handler mHandler;

        //创建另外一个handler，用来传Running对象
        private Handler handler = new Handler();

        private boolean mIsTrackPrepared = false;
        private boolean mIsTrackNet = false;
        private boolean mIsNextTrackPrepared = false;
        private boolean mIsNextInitialized = false;


        /**
         * 初始化MultiPlayer
         * @param service
         */
        public MultiPlayer(MediaService service) {
            this.mService = new WeakReference<MediaService>(service);
            //设置唤醒锁，保证系统睡眠时CPU仍然运行
            mCurrentMediaPlayer.setWakeMode(mService.get(), PowerManager.PARTIAL_WAKE_LOCK);

        }

        /**
         * 设置现在Player的数据源
         * @param path
         */
        public void setDataSource(final String path) {
            mIsInitialized = setDataSourceImpl(mCurrentMediaPlayer, path);
            if (mIsInitialized) {
                setNextDataSource(null);
            }
        }

        /**
         * 设置下个Player的数据源
         * @param path
         */
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

        /**
         * 设置现在Player的数据源（具体实现）
         * @param player
         * @param path
         * @return
         */
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

        /**
         * 设置下个Player的数据源（具体实现）
         * @param player
         * @param path
         * @return
         */
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

        public void setHandler(final Handler handler) {
            mHandler = handler;
        }

        public boolean isInitialized() {
            return mIsInitialized;
        }

        public boolean isTrackPrepared() {
            return mIsTrackPrepared;
        }


        /**
         * 现在Player开始播放
         */
        public void start() {
            if (D) Log.d(TAG, "mIsTrackNet, " + mIsTrackNet);
            if (!mIsTrackNet) {
                mService.get().sendUpdateBuffer(100);
                sencondaryPosition = 100;
                mCurrentMediaPlayer.start();
            } else {
                sencondaryPosition = 0;
                //mService.get().loading(true);
                handler.postDelayed(startMediaPlayerIfPrepared, 50);
            }
            //mService.get().notifyChange(MUSIC_CHANGED);
        }


        /**
         * 现在Player暂行reset()操作，恢复空闲状态
         */
        public void stop() {
            handler.removeCallbacks(setNextMediaPlayerIfPrepared);
            handler.removeCallbacks(startMediaPlayerIfPrepared);
            mCurrentMediaPlayer.reset();
            mIsInitialized = false;
            mIsTrackPrepared = false;
        }


        /**
         * 释放现在Player，结束其生命周期
         */
        public void release() {
            mCurrentMediaPlayer.release();
        }


        /**
         * 暂停现在Player
         */
        public void pause() {
            handler.removeCallbacks(startMediaPlayerIfPrepared);
            mCurrentMediaPlayer.pause();
        }


        /**
         * 获取当前Player的音频时常
         * @return
         */
        public long duration() {
            if (mIsTrackPrepared) {
                return mCurrentMediaPlayer.getDuration();
            }
            return -1;
        }


        /**
         * 获取当前Player播放的位置
         * @return
         */
        public long position() {
            if (mIsTrackPrepared) {
                try {
                    return mCurrentMediaPlayer.getCurrentPosition();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return -1;
        }


        public long secondPosition() {
            if (mIsTrackPrepared) {
                return sencondaryPosition;
            }
            return -1;
        }


        /**
         * 跳转音频时间点
         * @param whereto
         * @return
         */
        public long seek(final long whereto) {
            mCurrentMediaPlayer.seekTo((int) whereto);
            return whereto;
        }


        /**
         * 设置音量
         * @param vol
         */
        public void setVolume(final float vol) {
            try {
                mCurrentMediaPlayer.setVolume(vol, vol);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 获取当前音频会话id
         * @return
         */
        public int getAudioSessionId() {
            return mCurrentMediaPlayer.getAudioSessionId();
        }

        /**
         * 设置当前音频会话id
         * @param sessionId
         */
        public void setAudioSessionId(final int sessionId) {
            mCurrentMediaPlayer.setAudioSessionId(sessionId);
        }



        @Override
        public void onCompletion(MediaPlayer mp) {

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
            }
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {

            PrintLog.e(TAG, "Music Server Error what: " + what + " extra: " + extra);
            /*
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
            return false;*/


            //出错了就重新创建
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

        }


        //Runnable对象
        Runnable setNextMediaPlayerIfPrepared = new Runnable() {
            int count = 0;

            @Override
            public void run() {
                if (mIsNextTrackPrepared && mIsInitialized) {

//                    mCurrentMediaPlayer.setNextMediaPlayer(mNextMediaPlayer);
                } else if (count < 60) {
                    handler.postDelayed(setNextMediaPlayerIfPrepared, 100);
                }
                count++;
            }
        };

        Runnable startMediaPlayerIfPrepared = new Runnable() {

            @Override
            public void run() {
                if (D) Log.d(TAG, "mIsTrackPrepared, " + mIsTrackPrepared);
                if (mIsTrackPrepared) {
                    mCurrentMediaPlayer.start();
                    final long duration = duration();
                    if (mService.get().mRepeatMode != REPEAT_CURRENT && duration > 2000
                            && position() >= duration - 2000) {
                        //mService.get().gotoNext(true);
                        Log.e("play to go", "");
                    }
                    //mService.get().loading(false);
                } else {
                    handler.postDelayed(startMediaPlayerIfPrepared, 700);
                }
            }
        };

    }


    /**
     *
     * MusicPlayerHandler类，接收MediaPlayer（播放器）各种状态变换并进行处理
     *
     */
    private static final class MusicPlayerHandler extends Handler {
        private final WeakReference<MediaService> mService;
        private float mCurrentVolume = 1.0f;


        public MusicPlayerHandler(final MediaService service, final Looper looper) {
            super(looper);
            mService = new WeakReference<MediaService>(service);
        }


        @Override
        public void handleMessage(final Message msg) {
            final MediaService service = mService.get();
            if (service == null) {
                return;
            }

            synchronized (service) {
                switch (msg.what) {
                    case FADEDOWN:
                        mCurrentVolume -= .05f;
                        if (mCurrentVolume > .2f) {
                            sendEmptyMessageDelayed(FADEDOWN, 10);
                        } else {
                            mCurrentVolume = .2f;
                        }
                        service.mPlayer.setVolume(mCurrentVolume);
                        break;
                    case FADEUP:
                        mCurrentVolume += .01f;
                        if (mCurrentVolume < 1.0f) {
                            sendEmptyMessageDelayed(FADEUP, 10);
                        } else {
                            mCurrentVolume = 1.0f;
                        }
                        service.mPlayer.setVolume(mCurrentVolume);
                        break;
                    case SERVER_DIED:
                        if (service.isPlaying()) {
                            final TrackErrorInfo info = (TrackErrorInfo) msg.obj;
                            //service.sendErrorMessage(info.mTrackName);


                            service.removeTrack(info.mId);
                        } else {
                            service.openCurrentAndNext();
                        }
                        break;
                    case TRACK_WENT_TO_NEXT:
                        service.setAndRecordPlayPos(service.mNextPlayPos);
                        service.setNextTrack();
                        if (service.mCursor != null) {
                            service.mCursor.close();
                            service.mCursor = null;
                        }

                        service.updateCursor();
                        service.notifyChange(META_CHANGED);

                        //service.updateNotification();
                        break;
                    case TRACK_ENDED:
                        if (service.mRepeatMode == REPEAT_CURRENT) {
                            service.seek(0);
                            service.play();
                        } else {
                            if (D) Log.d(TAG, "Going to  of track");
                            service.gotoNext(true);
                        }
                        break;
                    case RELEASE_WAKELOCK:
                        service.mWakeLock.release();
                        break;
                    case FOCUSCHANGE:
                        if (D) Log.d(TAG, "Received audio focus change event " + msg.arg1);
                        switch (msg.arg1) {
                            case AudioManager.AUDIOFOCUS_LOSS:
                            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                                if (service.isPlaying()) {
                                    service.mPausedByTransientLossOfFocus =
                                            msg.arg1 == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
                                }
                                service.pause();
                                break;
                            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                                removeMessages(FADEUP);
                                sendEmptyMessage(FADEDOWN);
                                break;
                            case AudioManager.AUDIOFOCUS_GAIN:
                                if (!service.isPlaying()
                                        && service.mPausedByTransientLossOfFocus) {
                                    service.mPausedByTransientLossOfFocus = false;
                                    mCurrentVolume = 0f;
                                    service.mPlayer.setVolume(mCurrentVolume);
                                    service.play();
                                } else {
                                    removeMessages(FADEDOWN);
                                    sendEmptyMessage(FADEUP);
                                }
                                break;
                            default:
                        }
                        break;
                    case LRC_DOWNLOADED:
                        PrintLog.e("receiver:LRC_DOWNLOADED");
                        service.notifyChange(LRC_UPDATED);
                    default:
                        break;
                }
            }
        }
    }

    private static final class Shuffler {

        private final LinkedList<Integer> mHistoryOfNumbers = new LinkedList<Integer>();

        private final TreeSet<Integer> mPreviousNumbers = new TreeSet<Integer>();

        private final Random mRandom = new Random();

        private int mPrevious;


        public Shuffler() {
            super();
        }


        public int nextInt(final int interval) {
            int next;
            do {
                next = mRandom.nextInt(interval);
            } while (next == mPrevious && interval > 1
                    && !mPreviousNumbers.contains(Integer.valueOf(next)));
            mPrevious = next;
            mHistoryOfNumbers.add(mPrevious);
            mPreviousNumbers.add(mPrevious);
            cleanUpHistory();
            return next;
        }


        private void cleanUpHistory() {
            if (!mHistoryOfNumbers.isEmpty() && mHistoryOfNumbers.size() >= MAX_HISTORY_SIZE) {
                for (int i = 0; i < Math.max(1, MAX_HISTORY_SIZE / 2); i++) {
                    mPreviousNumbers.remove(mHistoryOfNumbers.removeFirst());
                }
            }
        }
    }


    private static final class TrackErrorInfo {
        public long mId;
        public String mTrackName;

        public TrackErrorInfo(long id, String trackName) {
            mId = id;
            mTrackName = trackName;
        }
    }

    private class MediaStoreObserver extends ContentObserver implements Runnable {

        private static final long REFRESH_DELAY = 500;
        private Handler mHandler;

        public MediaStoreObserver(Handler handler) {
            super(handler);
            mHandler = handler;
        }

        @Override
        public void onChange(boolean selfChange) {


            mHandler.removeCallbacks(this);
            mHandler.postDelayed(this, REFRESH_DELAY);
        }

        @Override
        public void run() {

            Log.e("ELEVEN", "calling refresh!");
            //refresh();
        }
    }



}
