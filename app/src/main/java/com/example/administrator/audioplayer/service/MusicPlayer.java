package com.example.administrator.audioplayer.service;

/**
 * Created by on 2017/1/19.
 */

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.audioplayer.bean.MusicInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * 管理客户端与后台服务的连接，保存Service实例在其中
 * 给客户端提供各种接口，提供绑定服务，解绑服务，还有各种Service的各种操作，可以包含特有的逻辑
 * 将所有关于音乐的操作集中到此类中，此类相当于工具类，不过需要初始化，即绑定操作
 */

public class MusicPlayer {

    //MediaService实例，在onServiceConnected中赋值
    private static MediaService mService = null;

    //绑定映射，用于解绑
    private static WeakHashMap<Context, ServiceBinder> mConnectionMap = new WeakHashMap<Context, ServiceBinder>();

    private static final long[] sEmptyList = new long[0];


    //初始化，绑定服务,多次调用不会重复绑定
    public static ServiceToken bindToService(final Context context) {

        Activity realActivity = ((Activity) context).getParent();
        if (realActivity == null) {
            realActivity = (Activity) context;
        }
        ContextWrapper contextWrapper = new ContextWrapper(realActivity);
        contextWrapper.startService(new Intent(contextWrapper, MediaService.class));
        ServiceBinder binder = new ServiceBinder(contextWrapper.getApplicationContext());
        if (contextWrapper.bindService(
                new Intent().setClass(contextWrapper, MediaService.class), binder, 0)) {
            mConnectionMap.put(contextWrapper, binder);
            return new ServiceToken(contextWrapper);
        }
        return null;
    }

    //解除绑定
    public static void unbindFromService(final ServiceToken token) {
        if (token == null) {
            return;
        }
        final ContextWrapper mContextWrapper = token.mWrappedContext;
        final ServiceBinder mBinder = mConnectionMap.remove(mContextWrapper);
        if (mBinder == null) {
            return;
        }
        mContextWrapper.unbindService(mBinder);
        if (mConnectionMap.isEmpty()) {
            mService = null;
        }
    }



    public static final boolean isPlaying() {
        if (mService != null) {
            return mService.isPlaying();
        }
        return false;
    }


    public static void playOrPause() {
        try {
            if (mService != null) {
                if (mService.isPlaying()) {
                    mService.pause();
                } else {
                    mService.play();
                }
            }
        } catch (final Exception ignored) {
        }
    }


    public static synchronized void playAll(final HashMap<Long, MusicInfo> infos, final long[] list, int position, final boolean forceShuffle) {
        if (list == null || list.length == 0 || mService == null) {
            return;
        }
        try {
            if (forceShuffle) {
                mService.setShuffleMode(MediaService.SHUFFLE_NORMAL);
            }
            final long currentId = mService.getAudioId();
            long playId = list[position];
            Log.e("currentId", currentId + "");
            final int currentQueuePosition = getQueuePosition();
            if (position != -1) {
                final long[] playlist = getQueue();
                if (Arrays.equals(list, playlist)) {
                    if (currentQueuePosition == position && currentId == list[position]) {
                        mService.play();
                        return;
                    } else {
                        mService.setQueuePosition(position);
                        return;
                    }

                }
            }
            if (position < 0) {
                position = 0;
            }
            mService.open(infos, list, forceShuffle ? -1 : position);
            mService.play();
            Log.e("time", System.currentTimeMillis() + "");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /*
    public static void playNext(Context context, final HashMap<Long, MusicInfo> map, final long[] list) {
        if (mService == null) {
            return;
        }
        try {
            int current = -1;
            long[] result = list;

            for (int i = 0; i < list.length; i++) {
                if (MusicPlayer.getCurrentAudioId() == list[i]) {
                    current = i;
                } else {
                    MusicPlayer.removeTrack(list[i]);
                }
            }

//            if( current != -1){
//                ArrayList lists = new ArrayList();
//                for(int i = 0; i<list.length;i++){
//                    if(i != current){
//                        lists.add(list[i]);
//                    }
//                }
//                result = new long[list.length - 1];
//                for(int i = 0;i<lists.size();i++){
//                     result[i] = (long) lists.get(i);
//                }
//            }

            mService.enqueue(list, map, MediaService.NEXT);

            //Toast.makeText(context, R.string.next_play, Toast.LENGTH_SHORT).show();
        } catch (final RemoteException ignored) {
        }
    }*/

    public static final long position() {
        if (mService != null) {
            return mService.position();
        }
        return 0;
    }

    public static final long duration() {
        if (mService != null) {
            return mService.duration();
        }
        return 0;
    }

    public static String getPath() {
        if (mService == null) {
            return null;
        }
        try {
            return mService.getPath();

        } catch (Exception e) {

        }
        return null;
    }

    public static void stop() {
        try {
            mService.stop();
        } catch (Exception e) {

        }
    }


    public static void next() {
        try {
            if (mService != null) {
                mService.next();
            }
        } catch (final RemoteException ignored) {
        }
    }

    /*
    public static void previous(final Context context, final boolean force) {
        final Intent previous = new Intent(context, MediaService.class);
        if (force) {
            previous.setAction(MediaService.PREVIOUS_FORCE_ACTION);
        } else {
            previous.setAction(MediaService.PREVIOUS_ACTION);
        }
        context.startService(previous);
    }*/

    public static void previous() {
       if(mService != null) {
           mService.prev();
       }
    }

    /**
     * 获取歌曲名字
     * @return
     */
    public static final String getTrackName() {
        if (mService != null) {
            return mService.getTrackName();
        }
        return null;
    }

    /**
     * 获取歌手名字
     * @return
     */
    public static final String getArtistName() {
        if (mService != null) {
            return mService.getArtistName();
        }
        return null;
    }

    /**
     * 获取封面地址
     * @return
     */

    public static final String getAlbumPath() {
        if (mService != null) {
            return mService.getAlbumPath();
        }
        return null;
    }

    public static final String[] getAlbumPathAll() {
        if (mService != null) {
            return mService.getAlbumPathAll();
        }
        return null;
    }


    public static final int getSongCountForAlbumInt(final Context context, final long id) {
        int songCount = 0;
        if (id == -1) {
            return songCount;
        }

        Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, id);
        Cursor cursor = context.getContentResolver().query(uri,
                new String[]{MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                if (!cursor.isNull(0)) {
                    songCount = cursor.getInt(0);
                }
            }
            cursor.close();
            cursor = null;
        }

        return songCount;
    }

    public static final String getReleaseDateForAlbum(final Context context, final long id) {
        if (id == -1) {
            return null;
        }
        Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, id);
        Cursor cursor = context.getContentResolver().query(uri, new String[]{
                MediaStore.Audio.AlbumColumns.FIRST_YEAR
        }, null, null, null);
        String releaseDate = null;
        if (cursor != null) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                releaseDate = cursor.getString(0);
            }
            cursor.close();
            cursor = null;
        }
        return releaseDate;
    }

    public static void seek(final long position) {
        if (mService != null) {
            mService.seek(position);
        }
    }

    public static final int getQueuePosition() {
        if (mService != null) {
            return mService.getQueuePosition();
        }
        return 0;
    }

    public static final long[] getQueue() {
        if (mService != null) {
            return mService.getQueue();
        } else {
        }

        return sEmptyList;
    }

    public static final int getQueueSize() {
        if (mService != null) {
            return mService.getQueueSize();
        }
        return 0;
    }

    public static void setQueuePosition(final int position) {
        if (mService != null) {
            mService.setQueuePosition(position);
        }
    }

    /**
     * 获取随机的模式，没有随机或正常随机
     * @return
     */
    public static final int getShuffleMode() {
        if (mService != null) {
            return mService.getShuffleMode();
        }
        return 0;
    }

    /**
     * 获取重复的模式，列表重复或单曲重复
     * @return
     */
    public static final int getRepeatMode() {
        if (mService != null) {
            return mService.getRepeatMode();
        }
        return 0;
    }

    /**
     * 改变模式
     */
    public static void changeMode() {
        if (mService != null) {
            //如果当前是随机播放
            if (mService.getShuffleMode() == MediaService.SHUFFLE_NORMAL) {
                //取消随机播放
                mService.setShuffleMode(MediaService.SHUFFLE_NONE);
                //设为单曲循环
                mService.setRepeatMode(MediaService.REPEAT_CURRENT);
                return;
            } else {
                switch (mService.getRepeatMode()) {
                    //如果当前是单曲播放，切换为列表循环
                    case MediaService.REPEAT_CURRENT:
                        mService.setRepeatMode(MediaService.REPEAT_ALL);
                        break;
                    //如果当前为列表循环，切换为随机播放
                    case MediaService.REPEAT_ALL:
                        mService.setShuffleMode(MediaService.SHUFFLE_NORMAL);
                        break;
                }
            }
        }
    }


    /**
     * 获取当前音频的id
     * @return
     */
    public static final long getCurrentAudioId() {
        if (mService != null) {
            return mService.getAudioId();
        }
        return -1;
    }




    //
    public static class ServiceBinder implements ServiceConnection {
        private Context mContext;


        public ServiceBinder(final Context context) {
            mContext = context;
        }

        @Override
        public void onServiceConnected(final ComponentName className, final IBinder service) {
            mService = ((MediaService.MyBinder)service).getService();
            //initPlaybackServiceWithSettings(mContext);
        }

        @Override
        public void onServiceDisconnected(final ComponentName className) {
            mService = null;
        }
    }


    public static class ServiceToken {
        public ContextWrapper mWrappedContext;

        public ServiceToken(final ContextWrapper context) {
            mWrappedContext = context;
        }
    }
}
