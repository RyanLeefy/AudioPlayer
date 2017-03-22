package com.example.administrator.audioplayer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.audioplayer.bean.CollectionInfo;
import com.example.administrator.audioplayer.bean.DownLoadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建和收藏的歌单
 */
public class SongCollectionDB {
    //表名
    public static final String TABLE_NAME = "songcollection";

    //数据库内歌单id，自增
    public static final String SONGCOLLECTION_ID = "songcollection_id";
    //歌单类型，0为固定歌单（我喜欢的歌单），1为自创建歌单，2为网络歌单，3为网络专辑
    public static final String SONGCOLLECTION_TYPE = "songcollection_type";
    //歌单原本id（收藏其他歌单或新歌单中有，即网络中）
    public static final String SONGCOLLECTION_LISTID = "songcollection_listid";
    //歌单名
    public static final String SONGCOLLECTION_NAME = "songcollection_name";
    //歌单内歌曲数量
    public static final String SONG_COUNT = "count";
    //歌单标签（收藏的是其他歌单中有）
    public static final String SONGCOLLECTION_TAG = "songcollection_tag";
    //收听人数（收藏的是其他歌单中有）
    public static final String SONGCOLLECTION_LISTEN_COUNT = "songcollection_listen_count";
    //歌单封面
    public static final String ALBUM_ART = "album_art";
    //歌手（收藏的是新专辑中有）
    public static final String AUTHOR = "author";
    //发布时间（收藏的是新专辑中有）
    public static final String PUBLISH_TIME = "publish_time";
    private static SongCollectionDB sInstance = null;

    private MusicDBOpenHelper mMusicDatabase = null;

    public SongCollectionDB(final Context context) {
        mMusicDatabase = MusicDBOpenHelper.getInstance(context);
    }

    public static final synchronized SongCollectionDB getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new SongCollectionDB(context.getApplicationContext());
        }

        return sInstance;
    }

    public void onCreate(final SQLiteDatabase db) {
        //创建表
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + SONGCOLLECTION_ID + " INTEGER NOT NULL PRIMARY KEY autoincrement, "
                + SONGCOLLECTION_TYPE + " INTEGER NOT NULL, "
                + SONGCOLLECTION_LISTID + " VARCHAR, "
                + SONGCOLLECTION_NAME + " VARCHAR NOT NULL, "
                + SONG_COUNT + " INTEGER NOT NULL, "
                + SONGCOLLECTION_TAG + " VARCHAR,"
                + SONGCOLLECTION_LISTEN_COUNT + " VARCHAR, "
                + ALBUM_ART + " VARCHAR, "
                + AUTHOR + " VARCHAR, "
                + PUBLISH_TIME + " VARCHAR "
                + ");"
        );

        //初始化我喜欢的音乐，插入表中,封面传入null，使用默认图片
        addCreateSongCollection(db, 0, "我喜欢的音乐", 0, null);
    }


    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    /**
     * 添加默认歌单，在数据库创建时调用，为了避免递归调用数据库，这里传入数据库实例
     * @param database
     * @param type
     * @param name
     * @param count
     * @param albumart
     */
    public synchronized void addCreateSongCollection(SQLiteDatabase database, int type, String name, int count,
                                                     String albumart) {
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues(4);
            values.put(SONGCOLLECTION_TYPE, type);
            values.put(SONGCOLLECTION_NAME, name);
            values.put(SONG_COUNT, count);
            values.put(ALBUM_ART, albumart);

            database.insert(TABLE_NAME, null, values);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }


    /**
     * 判断创建的歌单是否已经存在
     * @return false:不存在   true:存在
     */
    public synchronized boolean isExist(String name) {
        SQLiteDatabase database = mMusicDatabase.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null,
                "(songcollection_type = 0 or songcollection_type = 1) and songcollection_name = ?",
                new String[]{name}, null, null, null);

        if(cursor.getCount() == 0) {
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }

    /**
     * 判断收藏的歌单是否已经收藏,不存在返回-1，存在返回其id
     * @return  false:不存在   true:存在
     */
    public synchronized int isCollected(String list_id) {
        SQLiteDatabase database = mMusicDatabase.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, new String[]{SONGCOLLECTION_ID},
                "(songcollection_type = 2 or songcollection_type = 3) and songcollection_listid = ?",
                    new String[]{list_id}, null, null, null);


        if(cursor.getCount() == 0) {
            cursor.close();
            return -1;
        } else {
            cursor.moveToFirst();
            int result = cursor.getInt(cursor.getColumnIndex(SONGCOLLECTION_ID));
            cursor.close();
            return result;
        }
    }


    /**
     * 获取所有创建的歌单，返回一个列表
     *
     */
    public synchronized List<CollectionInfo> getCreateSongCollection() {
        ArrayList<CollectionInfo> results = new ArrayList<>();

        SQLiteDatabase database = mMusicDatabase.getReadableDatabase();
        //获取所有创建的歌单
        Cursor cursor = database.query(TABLE_NAME, null, "songcollection_type = 0 or songcollection_type = 1", null, null, null, SONGCOLLECTION_ID + " ASC");

        while(cursor != null && cursor.moveToNext()) {
            CollectionInfo collectionInfo = new CollectionInfo();
            collectionInfo.setId(cursor.getInt(cursor.getColumnIndex(SONGCOLLECTION_ID)));
            collectionInfo.setType(cursor.getInt(cursor.getColumnIndex(SONGCOLLECTION_TYPE)));
            collectionInfo.setCollectionName(cursor.getString(cursor.getColumnIndex(SONGCOLLECTION_NAME)));
            collectionInfo.setSongCount(cursor.getInt(cursor.getColumnIndex(SONG_COUNT)));
            collectionInfo.setAlbumArt(cursor.getString(cursor.getColumnIndex(ALBUM_ART)));

            results.add(collectionInfo);
        }

        if(cursor != null) {
            cursor.close();
        }

        return results;
    }


    /**
     * 获取所有收藏的歌单,返回一个列表
     * @return
     */
    public synchronized List<CollectionInfo> getNetSongCollection() {
        ArrayList<CollectionInfo> results = new ArrayList<>();

        SQLiteDatabase database = mMusicDatabase.getReadableDatabase();
        //获取所有创建的歌单
        Cursor cursor = database.query(TABLE_NAME, null, "songcollection_type = 2 or songcollection_type = 3", null, null, null, SONGCOLLECTION_ID + " ASC");

        while(cursor != null && cursor.moveToNext()) {
            CollectionInfo collectionInfo = new CollectionInfo();
            collectionInfo.setId(cursor.getInt(cursor.getColumnIndex(SONGCOLLECTION_ID)));
            collectionInfo.setType(cursor.getInt(cursor.getColumnIndex(SONGCOLLECTION_TYPE)));
            collectionInfo.setListId(Long.parseLong(cursor.getString(cursor.getColumnIndex(SONGCOLLECTION_LISTID))));
            collectionInfo.setCollectionName(cursor.getString(cursor.getColumnIndex(SONGCOLLECTION_NAME)));
            collectionInfo.setSongCount(cursor.getInt(cursor.getColumnIndex(SONG_COUNT)));
            collectionInfo.setCollectionTag(cursor.getString(cursor.getColumnIndex(SONGCOLLECTION_TAG)));
            collectionInfo.setListenCount(cursor.getString(cursor.getColumnIndex(SONGCOLLECTION_LISTEN_COUNT)));
            collectionInfo.setAlbumArt(cursor.getString(cursor.getColumnIndex(ALBUM_ART)));
            collectionInfo.setAuthor(cursor.getString(cursor.getColumnIndex(AUTHOR)));
            collectionInfo.setPublishTime(cursor.getString(cursor.getColumnIndex(PUBLISH_TIME)));
            results.add(collectionInfo);
        }

        if(cursor != null) {
            cursor.close();
        }

        return results;
    }






    /**
     * 添加自创建歌单
     * @param type
     * @param name
     * @param count
     * @param albumart
     */
    public synchronized void addCreateSongCollection(int type, String name, int count,
                                                     String albumart) {
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues(4);
            values.put(SONGCOLLECTION_TYPE, type);
            values.put(SONGCOLLECTION_NAME, name);
            values.put(SONG_COUNT, count);
            values.put(ALBUM_ART, albumart);

            database.insert(TABLE_NAME, null, values);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }



    /**
     * 添加收藏网络歌单
     * @param type
     * @param listid
     * @param name
     * @param count
     * @param albumart
     * @param tag
     */
    public synchronized void addNetSongCollection(int type, long listid, String name,
                                                  int count, String albumart, String tag, String songCollectionCount) {
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues(7);
            values.put(SONGCOLLECTION_TYPE, type);
            values.put(SONGCOLLECTION_LISTID, listid);
            values.put(SONGCOLLECTION_NAME, name);
            values.put(SONG_COUNT, count);
            values.put(ALBUM_ART, albumart);
            values.put(SONGCOLLECTION_TAG, tag);
            values.put(SONGCOLLECTION_LISTEN_COUNT, songCollectionCount);

            database.insert(TABLE_NAME, null, values);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }


    /**
     * 添加收藏网络新专辑
     * @param type
     * @param albumid
     * @param name
     * @param count
     * @param albumart
     * @param author
     * @param publish_time
     */
    public synchronized void addNewAlbum(int type, long albumid, String name,
                                         int count, String albumart, String author,
                                         String publish_time) {
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues(7);
            values.put(SONGCOLLECTION_TYPE, type);
            values.put(SONGCOLLECTION_LISTID, albumid);
            values.put(SONGCOLLECTION_NAME, name);
            values.put(SONG_COUNT, count);
            values.put(ALBUM_ART, albumart);
            values.put(AUTHOR, author);
            values.put(PUBLISH_TIME, publish_time);

            database.insert(TABLE_NAME, null, values);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }


    /**
     * 更改歌单信息
     * @param id
     * @param name 修改的名字
     */
    public synchronized void updateCreateSongCollection(int id, String name) {
        SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues(1);
            values.put(SONGCOLLECTION_NAME, name);
            database.update(TABLE_NAME, values, SONGCOLLECTION_ID + " = " + id, null);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }


    /**
     * 删除创建的歌单或收藏的歌单，先删除歌单，然后删除歌单歌曲数据库里面相关歌曲
     * @param id
     */
    public synchronized void deleteSongCollection(int id) {
        SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.beginTransaction();
        try {
            //删除歌单
            database.delete(TABLE_NAME, SONGCOLLECTION_ID + " = ?", new String[]{String.valueOf(id)});
            //删除歌曲
            database.delete("songcollectionsong", SONGCOLLECTION_ID + " = ?", new String[]{String.valueOf(id)});

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }



    /*
    public synchronized void updatePlaylist(long playlistid, int oldcount) {
        ArrayList<CollectionInfo> results = getPlaylist();
        int countt = 0;
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).id == playlistid) {
                countt = results.get(i).songCount;
            }
        }
        countt = countt + oldcount;
        update(playlistid, countt);

    }

    public synchronized void update(long playlistid, int count) {
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues(2);
            values.put(SONGCOLLECTION_ID, playlistid);
            //values.put(PlaylistInfoColumns.SONGCOLLECTION_NAME, name);
            values.put(SONG_COUNT, count);
            database.update(TABLE_NAME, values, SONGCOLLECTION_ID + " = " + playlistid, null);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public synchronized void update(long playlistid, int count, String album) {
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues(3);
            values.put(SONGCOLLECTION_ID, playlistid);
            values.put(SONG_COUNT, count);
            values.put(ALBUM_ART, album);
            database.update(TABLE_NAME, values, SONGCOLLECTION_ID + " = " + playlistid, null);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    //删除本地文件时更新播放列表歌曲数量信息
    public void updatePlaylistMusicCount(long[] PlaylistId) {

        SQLiteDatabase database = null;

        final StringBuilder selection = new StringBuilder();
        selection.append(SONGCOLLECTION_ID + " IN (");
        for (int i = 0; i < PlaylistId.length; i++) {
            selection.append(PlaylistId[i]);
            if (i < PlaylistId.length - 1) {
                selection.append(",");
            }
        }
        selection.append(")");

        Cursor cursor = null;
        try {
            cursor = mMusicDatabase.getReadableDatabase().query(TABLE_NAME, null,
                    selection.toString(), null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                database = mMusicDatabase.getWritableDatabase();
                database.beginTransaction();

                do {
                    int count = cursor.getInt(cursor.getColumnIndex(SONG_COUNT)) - 1;
                    long playlistid = cursor.getLong(cursor.getColumnIndex(SONGCOLLECTION_ID));
                    if (count == 0) {
                        database.delete(TABLE_NAME, SONGCOLLECTION_ID + " = ?", new String[]
                                {String.valueOf(playlistid)});
                    } else {
                        ContentValues values = new ContentValues(2);
                        values.put(SONGCOLLECTION_ID, playlistid);
                        values.put(SONG_COUNT, count);
                        database.update(TABLE_NAME, values, SONGCOLLECTION_ID + " = " + playlistid, null);
                    }
                    // update(playlistid,count);

                } while (cursor.moveToNext());

                database.setTransactionSuccessful();
            }

        } finally {
            database.endTransaction();
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

    }


    public void deletePlaylist(final long PlaylistId) {
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.delete(TABLE_NAME, SONGCOLLECTION_ID + " = ?", new String[]
                {String.valueOf(PlaylistId)});
    }

    public synchronized boolean hasPlaylist(final long PlaylistId) {

        Cursor cursor = null;
        try {
            cursor = mMusicDatabase.getReadableDatabase().query(TABLE_NAME, null,
                    SONGCOLLECTION_ID + " = ?", new String[]
                            {String.valueOf(PlaylistId)}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                return true;
            }

            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }

    public synchronized void deletePlaylist(final long[] PlaylistId) {

        final StringBuilder selection = new StringBuilder();
        selection.append(SONGCOLLECTION_ID + " IN (");
        for (int i = 0; i < PlaylistId.length; i++) {
            selection.append(PlaylistId[i]);
            if (i < PlaylistId.length - 1) {
                selection.append(",");
            }
        }
        selection.append(")");


        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.delete(TABLE_NAME, selection.toString(), null);
    }

    public void deleteAll() {
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.delete(TABLE_NAME, null, null);
    }


    public synchronized ArrayList<CollectionInfo> getPlaylist() {
        ArrayList<CollectionInfo> results = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = mMusicDatabase.getReadableDatabase().query(TABLE_NAME, null,
                    null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                results.ensureCapacity(cursor.getCount());

                do {
                    if (cursor.getString(4).equals("local"))
                        results.add(new CollectionInfo(cursor.getLong(0), cursor.getString(1), cursor.getInt(2),
                                cursor.getString(3), cursor.getString(4)));
                } while (cursor.moveToNext());
            }

            return results;
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }

    public synchronized ArrayList<CollectionInfo> getNetPlaylist() {
        ArrayList<CollectionInfo> results = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = mMusicDatabase.getReadableDatabase().query(TABLE_NAME, null,
                    null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                results.ensureCapacity(cursor.getCount());

                do {
                    if (!cursor.getString(4).equals("local"))
                        results.add(new CollectionInfo(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4)));
                } while (cursor.moveToNext());
            }

            return results;
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }
    */


}
