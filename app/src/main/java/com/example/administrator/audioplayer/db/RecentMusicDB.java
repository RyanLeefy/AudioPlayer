/*
* Copyright (C) 2014 The CyanogenMod Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.administrator.audioplayer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.audioplayer.bean.MusicInfo;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecentMusicDB {

    private static final String TABLE_NAME = "recenthistory";
    
    private static final int MAX_ITEMS_IN_DB = 100;

    private static RecentMusicDB sInstance = null;

    private MusicDBOpenHelper mMusicDatabase = null;

    //Column包括MusicInfo各属性，外加一个播放时间，用于排序
    //String KEY_AUDIO_ID = "audioid";
    //String KEY_ALBUM_ID = "albumid";
    //String KEY_ALBUM_NAME = "albumname";
    //String KEY_ALBUM_DATA = "albumdata";
    //String KEY_DURATION = "duration";
    //String KEY_MUSIC_NAME = "musicname";
    //String KEY_ARTIST = "artist";
    //String KEY_ARTIST_ID = "artist_id";
    //String KEY_DATA = "data";
    //String KEY_FOLDER = "folder";
    //String KEY_SIZE = "size";
    //String KEY_FAVORITE = "favorite";
    //String KEY_LRC = "lrc";
    //String KEY_ISLOCAL = "islocal";
    //String KEY_SORT = "sort";
    public static final String TIMEPLAYED = "timeplayed";


    public RecentMusicDB(final Context context) {
        mMusicDatabase = MusicDBOpenHelper.getInstance(context);
    }

    public static synchronized RecentMusicDB getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new RecentMusicDB(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * 创建recenthistory表
     * @param db
     */
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + MusicInfo.KEY_AUDIO_ID + " INT,"
                + MusicInfo.KEY_ALBUM_ID + " INT,"
                + MusicInfo.KEY_ALBUM_NAME + " VARCHAR,"
                + MusicInfo.KEY_ALBUM_DATA + " VARCHAR,"
                + MusicInfo.KEY_DURATION + " INT,"
                + MusicInfo.KEY_MUSIC_NAME + " VARCHAR,"
                + MusicInfo.KEY_ARTIST + " VARCHAR,"
                + MusicInfo.KEY_ARTIST_ID + " LONG,"
                + MusicInfo.KEY_DATA + " VARCHAR,"
                + MusicInfo.KEY_FOLDER + " VARCHAR,"
                + MusicInfo.KEY_SIZE + " INT,"
                + MusicInfo.KEY_FAVORITE + " INT,"
                + MusicInfo.KEY_LRC + " VARCHAR,"
                + MusicInfo.KEY_ISLOCAL + " INT,"  //没有boolean，0表示false，1表示true
                + MusicInfo.KEY_SORT  + " VARCHAR,"
                + TIMEPLAYED + " LONG);");
    }

    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
    }


    /**
     * 插入数据
     * @param musicInfo
     */
    public synchronized void insertRecentSong(MusicInfo musicInfo) {
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.beginTransaction();

        Map<Integer, Integer> recentMap = null;
        try {

            //查询是否有与插入歌曲音频id一样的，有的话删除旧的
            recentMap = getRecentIdMap();
            //如果该map中存在该音频id对应的int值为1，则表示存在
            if (!recentMap.isEmpty() && recentMap.get(musicInfo.getAudioId()) != null) {
                //删除旧的
                removeItem(musicInfo.getAudioId());
            }


            //插入数据
            final ContentValues values = new ContentValues(16);
            values.put(MusicInfo.KEY_AUDIO_ID, musicInfo.getAudioId());
            values.put(MusicInfo.KEY_ALBUM_ID, musicInfo.getAlbumId());
            values.put(MusicInfo.KEY_ALBUM_NAME, musicInfo.getAlbumName());
            values.put(MusicInfo.KEY_ALBUM_DATA, musicInfo.getAlbumData());
            values.put(MusicInfo.KEY_DURATION, musicInfo.getDuration());
            values.put(MusicInfo.KEY_MUSIC_NAME, musicInfo.getMusicName());
            values.put(MusicInfo.KEY_ARTIST, musicInfo.getArtist());
            values.put(MusicInfo.KEY_ARTIST_ID, musicInfo.getArtistId());
            values.put(MusicInfo.KEY_DATA, musicInfo.getData());
            values.put(MusicInfo.KEY_FOLDER, musicInfo.getFolder());
            values.put(MusicInfo.KEY_SIZE, musicInfo.getSize());
            values.put(MusicInfo.KEY_FAVORITE, musicInfo.getFavorite());
            values.put(MusicInfo.KEY_LRC , musicInfo.getLrc());
            if(musicInfo.islocal()) {
                values.put(MusicInfo.KEY_ISLOCAL, 1);
            } else {
                values.put(MusicInfo.KEY_ISLOCAL, 0);
            }
            values.put(MusicInfo.KEY_SORT , musicInfo.getSort());
            values.put(TIMEPLAYED, System.currentTimeMillis());
            database.insert(TABLE_NAME, null, values);

            //若数量大于上限，则删除旧的数据
            Cursor oldest = null;
            try {
                oldest = database.query(TABLE_NAME,
                        new String[]{TIMEPLAYED}, null, null, null, null,
                        TIMEPLAYED + " ASC");

                if (oldest != null && oldest.getCount() > MAX_ITEMS_IN_DB) {
                    oldest.moveToPosition(oldest.getCount() - MAX_ITEMS_IN_DB);
                    long timeOfRecordToKeep = oldest.getLong(0);

                    Logger.d("begin delete");
                    database.delete(TABLE_NAME,
                            TIMEPLAYED + " < ?",
                            new String[]{String.valueOf(timeOfRecordToKeep)});
                    Logger.d("end delete");
                }
            } finally {
                if (oldest != null) {
                    oldest.close();
                    oldest = null;
                }
            }
        } finally {
            database.setTransactionSuccessful();
            database.endTransaction();
        }
    }


    /**
     * 删除一条数据，
     * @param songId  音频id
     */
    public synchronized void removeItem(long songId) {
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.delete(TABLE_NAME, MusicInfo.KEY_AUDIO_ID + " = ?", new String[]{
                String.valueOf(songId)
        });

    }

    /**
     * 删除所有数据
     */
    public void deleteAll() {
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.delete(TABLE_NAME, null, null);
    }


    /**
     * 获取前多少条数据,若传入null 则查询所有数据
     * @return 返回cursor，可以直接用于getCount得到数据的多少
     */
    public synchronized Cursor getRecentCursor() {
        final SQLiteDatabase database = mMusicDatabase.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{MusicInfo.KEY_AUDIO_ID,
                        MusicInfo.KEY_ALBUM_ID,
                        MusicInfo.KEY_ALBUM_NAME,
                        MusicInfo.KEY_ALBUM_DATA,
                        MusicInfo.KEY_DURATION,
                        MusicInfo.KEY_MUSIC_NAME,
                        MusicInfo.KEY_ARTIST,
                        MusicInfo.KEY_ARTIST_ID,
                        MusicInfo.KEY_DATA,
                        MusicInfo.KEY_FOLDER,
                        MusicInfo.KEY_SIZE,
                        MusicInfo.KEY_FAVORITE,
                        MusicInfo.KEY_LRC,
                        MusicInfo.KEY_ISLOCAL,
                        MusicInfo.KEY_SORT}, null, null, null, null, TIMEPLAYED + " DESC");
        return cursor;
    }
    
    
    /**
     * 获取前多少条数据,若传入null 则查询所有数据
     * @param limit
     * @return 返回组装好的list
     */
    public synchronized List<MusicInfo> getRecentList(final String limit) {
        final SQLiteDatabase database = mMusicDatabase.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{MusicInfo.KEY_AUDIO_ID,
                        MusicInfo.KEY_ALBUM_ID,
                        MusicInfo.KEY_ALBUM_NAME,
                        MusicInfo.KEY_ALBUM_DATA,
                        MusicInfo.KEY_DURATION,
                        MusicInfo.KEY_MUSIC_NAME,
                        MusicInfo.KEY_ARTIST,
                        MusicInfo.KEY_ARTIST_ID,
                        MusicInfo.KEY_DATA,
                        MusicInfo.KEY_FOLDER,
                        MusicInfo.KEY_SIZE,
                        MusicInfo.KEY_FAVORITE,
                        MusicInfo.KEY_LRC,
                        MusicInfo.KEY_ISLOCAL,
                        MusicInfo.KEY_SORT}, null, null, null, null, TIMEPLAYED + " DESC", limit);
        List<MusicInfo> list = new ArrayList<>();
        while(cursor != null && cursor.moveToNext()) {
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.setAudioId(cursor.getInt(cursor.getColumnIndex(MusicInfo.KEY_AUDIO_ID)));
            musicInfo.setAlbumId(cursor.getInt(cursor.getColumnIndex(MusicInfo.KEY_ALBUM_ID)));
            musicInfo.setAlbumName(cursor.getString(cursor.getColumnIndex(MusicInfo.KEY_ALBUM_NAME)));
            musicInfo.setAlbumData(cursor.getString(cursor.getColumnIndex(MusicInfo.KEY_ALBUM_DATA)));
            musicInfo.setDuration(cursor.getInt(cursor.getColumnIndex(MusicInfo.KEY_DURATION)));
            musicInfo.setMusicName(cursor.getString(cursor.getColumnIndex(MusicInfo.KEY_MUSIC_NAME)));
            musicInfo.setArtist(cursor.getString(cursor.getColumnIndex(MusicInfo.KEY_ARTIST)));
            musicInfo.setArtistId(cursor.getLong(cursor.getColumnIndex(MusicInfo.KEY_ARTIST_ID)));
            musicInfo.setData(cursor.getString(cursor.getColumnIndex(MusicInfo.KEY_DATA)));
            musicInfo.setFolder(cursor.getString(cursor.getColumnIndex(MusicInfo.KEY_FOLDER)));
            musicInfo.setSize(cursor.getInt(cursor.getColumnIndex(MusicInfo.KEY_SIZE)));
            musicInfo.setFavorite(cursor.getInt(cursor.getColumnIndex(MusicInfo.KEY_FAVORITE)));
            musicInfo.setLrc(cursor.getString(cursor.getColumnIndex(MusicInfo.KEY_LRC)));
            if(cursor.getInt(cursor.getColumnIndex(MusicInfo.KEY_ISLOCAL)) == 0) {
                musicInfo.setIslocal(false);
            } else {
                musicInfo.setIslocal(true);
            }
            musicInfo.setSort(cursor.getString(cursor.getColumnIndex(MusicInfo.KEY_SORT)));
            list.add(musicInfo);
        }

        if(cursor != null) {
            cursor.close();
        }
        return list;
    }


    /**
     * 返回最近播放音频id map, 把有的都设置为1
     * @return
     */
    public synchronized Map<Integer, Integer> getRecentIdMap() {
        Logger.d("begin get RecentIds");
        List<MusicInfo> musicInfos = getRecentList(null);
        Map<Integer, Integer> map = new HashMap<>();
        if (musicInfos == null || musicInfos.isEmpty()) {
            return map;
        }

        for(int i = 0; i<musicInfos.size(); i++) {
            map.put(musicInfos.get(i).getAudioId(), 1);
        }

        return map;
    }


}
