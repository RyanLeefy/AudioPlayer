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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * 创建数据库
 */
public class MusicDBOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASENAME = "audio_player.db";
    private static final int VERSION = 1;
    private static MusicDBOpenHelper sInstance = null;

    private final Context mContext;

    public MusicDBOpenHelper(final Context context) {
        //初始化创建数据库
        super(context, DATABASENAME, null, VERSION);

        mContext = context;
    }

    //单例模式获取数据库
    public static synchronized MusicDBOpenHelper getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new MusicDBOpenHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //MusicPlaybackState.getInstance(mContext).onCreate(db);
        RecentMusicDB.getInstance(mContext).onCreate(db);
        SearchHistory.getInstance(mContext).onCreate(db);
        SongCollectionDB.getInstance(mContext).onCreate(db);
        SongCollectionSongDB.getInstance(mContext).onCreate(db);
        //PlaylistsManager.getInstance(mContext).onCreate(db);
        DownLoadDB.getInstance(mContext).onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //MusicPlaybackState.getInstance(mContext).onUpgrade(db, oldVersion, newVersion);
        RecentMusicDB.getInstance(mContext).onUpgrade(db, oldVersion, newVersion);
        SearchHistory.getInstance(mContext).onUpgrade(db, oldVersion, newVersion);
        SongCollectionDB.getInstance(mContext).onUpgrade(db, oldVersion, newVersion);
        SongCollectionSongDB.getInstance(mContext).onUpgrade(db, oldVersion, newVersion);
        //PlaylistsManager.getInstance(mContext).onUpgrade(db, oldVersion, newVersion);
        DownLoadDB.getInstance(mContext).onUpgrade(db, oldVersion, newVersion);
    }


}
