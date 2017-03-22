package com.example.administrator.audioplayer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.utils.PrintLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/21 0021.
 * 创建的歌单里面的歌曲信息
 */

public class SongCollectionSongDB {
    //表名
    public static final String TABLE_NAME = "songcollectionsong";

    private static final String SONG_ID = "song_id";

    private static SongCollectionSongDB sInstance = null;

    private MusicDBOpenHelper mMusicDatabase = null;

    public SongCollectionSongDB(final Context context) {
        mMusicDatabase = MusicDBOpenHelper.getInstance(context);
    }

    public static synchronized SongCollectionSongDB getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new SongCollectionSongDB(context.getApplicationContext());
        }

        return sInstance;
    }

    /**
     * 创建downloadtask表
     * @param db
     */
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + SONG_ID + " INTEGER NOT NULL PRIMARY KEY autoincrement,"
                + SongCollectionDB.SONGCOLLECTION_ID + " INTEGER NOT NULL,"
                + MusicInfo.KEY_MUSIC_NAME + " TEXT ,"
                + MusicInfo.KEY_ALBUM_DATA + " TEXT ,"
                + MusicInfo.KEY_ALBUM_ID + " INTEGER, "
                + MusicInfo.KEY_ALBUM_NAME + " TEXT, "
                + MusicInfo.KEY_ARTIST + " TEXT ,"
                + MusicInfo.KEY_ARTIST_ID + " TEXT ,"
                + MusicInfo.KEY_AUDIO_ID + " TEXT ,"
                + MusicInfo.KEY_DATA + " INTEGER ,"
                + MusicInfo.KEY_DURATION + " INTEGER ,"
                + MusicInfo.KEY_FAVORITE + " INTEGER  ,"
                + MusicInfo.KEY_FOLDER + " INTEGER  ,"
                + MusicInfo.KEY_ISLOCAL + " INTEGER  ,"
                + MusicInfo.KEY_LRC + " INTEGER ,"
                + MusicInfo.KEY_SIZE + " INTEGER ,"
                + MusicInfo.KEY_SORT + " INTEGER  );");
    }

    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
    }



    /**
     * 判断歌曲是否已经收藏在歌单中了（判断歌曲名和歌手名）
     * @param songcollectionId  歌单id
     * @param musicInfo  歌曲信息
     * @return 已收藏返回true，未收藏返回false
     */
    public synchronized boolean isCollected(int songcollectionId, MusicInfo musicInfo) {
        SQLiteDatabase database = mMusicDatabase.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null,
                "songcollection_id = ? and musicname = ? and artist = ? ",
                new String[]{String.valueOf(songcollectionId), musicInfo.getMusicName(), musicInfo.getArtist()},
                null, null, null);


        if(cursor.getCount() == 0) {
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }


    /**
     * 收藏单首歌曲
     * @param songCollection_id
     * @param musicInfo
     */
    public synchronized void addSongToCollection(int songCollection_id, MusicInfo musicInfo) {
        SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues(16);
            //插入数据
            values.put(SongCollectionDB.SONGCOLLECTION_ID, songCollection_id);
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


            //插入歌单歌曲数据库
            database.insert(TABLE_NAME, null, values);
            //更新歌单数据库

            //先获取该歌单的歌曲数量
            Cursor cursor = database.query("songcollection",
                    new String[]{SongCollectionDB.SONG_COUNT}, "songcollection_id = ? ",
                    new String[]{String.valueOf(songCollection_id)}, null, null, null);
            if(cursor != null && cursor.moveToFirst()) {

                int count = cursor.getInt(cursor.getColumnIndex(SongCollectionDB.SONG_COUNT));
                PrintLog.e("countfromdb:" + count);
                //然后更新歌曲数量，为原来的值+1
                count++;
                PrintLog.e("countafter:" + count);
                ContentValues countvalues = new ContentValues(1);
                countvalues.put(SongCollectionDB.SONG_COUNT, count);
                database.update("songcollection",
                        countvalues,
                        SongCollectionDB.SONGCOLLECTION_ID + " = ? ",
                        new String[]{String.valueOf(songCollection_id)});

            }
            cursor.close();




            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }


    /**
     * 收藏多首歌曲，传入一个列表
     * @param songCollection_id
     * @param musicInfos
     */
    public synchronized void addSongsToCollection(int songCollection_id, List<MusicInfo> musicInfos) {
        SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.beginTransaction();
        try {
            for(int i = 0; i < musicInfos.size(); i++) {
                ContentValues values = new ContentValues(16);
                //插入数据
                values.put(SongCollectionDB.SONGCOLLECTION_ID, songCollection_id);
                values.put(MusicInfo.KEY_AUDIO_ID, musicInfos.get(i).getAudioId());
                values.put(MusicInfo.KEY_ALBUM_ID, musicInfos.get(i).getAlbumId());
                values.put(MusicInfo.KEY_ALBUM_NAME, musicInfos.get(i).getAlbumName());
                values.put(MusicInfo.KEY_ALBUM_DATA, musicInfos.get(i).getAlbumData());
                values.put(MusicInfo.KEY_DURATION, musicInfos.get(i).getDuration());
                values.put(MusicInfo.KEY_MUSIC_NAME, musicInfos.get(i).getMusicName());
                values.put(MusicInfo.KEY_ARTIST, musicInfos.get(i).getArtist());
                values.put(MusicInfo.KEY_ARTIST_ID, musicInfos.get(i).getArtistId());
                values.put(MusicInfo.KEY_DATA, musicInfos.get(i).getData());
                values.put(MusicInfo.KEY_FOLDER, musicInfos.get(i).getFolder());
                values.put(MusicInfo.KEY_SIZE, musicInfos.get(i).getSize());
                values.put(MusicInfo.KEY_FAVORITE, musicInfos.get(i).getFavorite());
                values.put(MusicInfo.KEY_LRC, musicInfos.get(i).getLrc());
                if (musicInfos.get(i).islocal()) {
                    values.put(MusicInfo.KEY_ISLOCAL, 1);
                } else {
                    values.put(MusicInfo.KEY_ISLOCAL, 0);
                }
                values.put(MusicInfo.KEY_SORT, musicInfos.get(i).getSort());


                database.insert(TABLE_NAME, null, values);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }




    /**
     * 删除单首歌曲（从歌单中移除），传入歌曲歌单id，歌曲信息,删除后把对应歌单的count减1
     * @param id 歌单在数据库中id
     * @param musicInfo
     */
    public synchronized void deleteSongid(int id, MusicInfo musicInfo) {
        SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.beginTransaction();
        try {
            //删除歌曲
            database.delete(TABLE_NAME,
                    SongCollectionDB.SONGCOLLECTION_ID + " = ? and "
                            + MusicInfo.KEY_MUSIC_NAME + " = ? and "
                            + MusicInfo.KEY_ARTIST + " = ?",
                    new String[]{String.valueOf(id), musicInfo.getMusicName(), musicInfo.getArtist()});



            //先获取该歌单的歌曲数量
            Cursor cursor = database.query("songcollection",
                    new String[]{SongCollectionDB.SONG_COUNT}, "songcollection_id = ? ",
                    new String[]{String.valueOf(id)}, null, null, null);
            if(cursor != null && cursor.moveToFirst()) {

                int count = cursor.getInt(cursor.getColumnIndex(SongCollectionDB.SONG_COUNT));
                PrintLog.e("countfromdb:" + count);
                //然后更新歌曲数量，为原来的值+1
                count--;
                PrintLog.e("countafter:" + count);
                ContentValues countvalues = new ContentValues(1);
                countvalues.put(SongCollectionDB.SONG_COUNT, count);
                database.update("songcollection",
                        countvalues,
                        SongCollectionDB.SONGCOLLECTION_ID + " = ? ",
                        new String[]{String.valueOf(id)});

            }
            cursor.close();

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }



    /**
     * 删除歌单，传入歌单id
     * @param songCollection_id
     */
    public synchronized void deleteSongsByCollection(int songCollection_id) {
        SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.beginTransaction();
        try {
            database.delete(TABLE_NAME, SongCollectionDB.SONGCOLLECTION_ID + " = ?", new String[]{String.valueOf(songCollection_id)});
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }





    /**
     * 获取歌单对应的歌曲
     * @param id
     */
    public synchronized List<MusicInfo> getSongListById(int id) {
        List<MusicInfo> resultList = new ArrayList<>();

        SQLiteDatabase database = mMusicDatabase.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null,
                "songcollection_id = ? ",
                new String[]{String.valueOf(id)},
                null, null, SONG_ID + " ASC");

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

            resultList.add(musicInfo);
        }

        return resultList;
    }






}
