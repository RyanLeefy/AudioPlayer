package com.example.administrator.audioplayer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.audioplayer.bean.DownLoadInfo;
import com.example.administrator.audioplayer.download.DownloadStatus;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

/**
 * 下载数据库，包括还没下载好的下载信息和已经下载好的下载信息
 */
public class DownLoadDB {

    private static final String TABLE_NAME = "downloadtask";

    private static DownLoadDB sInstance = null;

    private MusicDBOpenHelper mMusicDatabase = null;

    public DownLoadDB(final Context context) {
        mMusicDatabase = MusicDBOpenHelper.getInstance(context);
    }

    public static synchronized DownLoadDB getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new DownLoadDB(context.getApplicationContext());
        }

        return sInstance;
    }

    /**
     * 创建downloadtask表
     * @param db
     */
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + DownLoadInfo.KEY_DOWNLOAD_ID + " TEXT NOT NULL PRIMARY KEY,"
                + DownLoadInfo.KEY_TOTALSIZE + " INT NOT NULL,"
                + DownLoadInfo.KEY_COMPLETEDSIZE + " INT NOT NULL, "
                + DownLoadInfo.KEY_URL + " TEXT NOT NULL,"
                + DownLoadInfo.KEY_SAVEDIRPATH + " TEXT NOT NULL,"
                + DownLoadInfo.KEY_FILENAME + " TEXT NOT NULL,"
                + DownLoadInfo.KEY_ARTIST + " TEXT NOT NULL,"
                + DownLoadInfo.KEY_DOWNLOADSTATUS + " INT NOT NULL);");
    }

    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
    }


    /**
     * 插入下载任务
     * @param info
     */
    public synchronized void insert(DownLoadInfo info) {
        Logger.e("download id = " + info.getDownloadId());
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues(8);
            values.put(DownLoadInfo.KEY_DOWNLOAD_ID, info.getDownloadId());
            values.put(DownLoadInfo.KEY_TOTALSIZE, info.getTotalSize());
            values.put(DownLoadInfo.KEY_COMPLETEDSIZE, info.getCompletedSize());
            values.put(DownLoadInfo.KEY_URL, info.getUrl());
            values.put(DownLoadInfo.KEY_SAVEDIRPATH, info.getSaveDirPath());
            values.put(DownLoadInfo.KEY_FILENAME, info.getFileName());
            values.put(DownLoadInfo.KEY_ARTIST, info.getArtist());
            values.put(DownLoadInfo.KEY_DOWNLOADSTATUS, info.getDownloadStatus());

            database.insert(TABLE_NAME, null, values);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }


    /**
     * 更新下载任务
     * @param info
     */
    public synchronized void update(DownLoadInfo info) {

        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues(6);
            values.put(DownLoadInfo.KEY_TOTALSIZE, info.getTotalSize());
            values.put(DownLoadInfo.KEY_COMPLETEDSIZE, info.getCompletedSize());
            values.put(DownLoadInfo.KEY_URL, info.getUrl());
            values.put(DownLoadInfo.KEY_SAVEDIRPATH, info.getSaveDirPath());
            values.put(DownLoadInfo.KEY_FILENAME, info.getFileName());
            values.put(DownLoadInfo.KEY_ARTIST, info.getArtist());
            values.put(DownLoadInfo.KEY_DOWNLOADSTATUS, info.getDownloadStatus());
            database.update(TABLE_NAME, values, DownLoadInfo.KEY_DOWNLOAD_ID + " = ?",
                    new String[]{info.getDownloadId()});
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }


    /**
     * 删除下载任务
     * @param Id
     */
    public void deleteTask(String Id) {
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.delete(TABLE_NAME, DownLoadInfo.KEY_DOWNLOAD_ID + " = ?", new String[]
                {String.valueOf(Id)});
    }




    /**
     * 删除所有还没下载完的下载信息
     */
    public void deleteAllDowningTasks() {
        ArrayList<String> results = new ArrayList<>();
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = database.query(TABLE_NAME, null,
                    null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                results.ensureCapacity(cursor.getCount());

                do {
                    if (cursor.getInt(7) != DownloadStatus.DOWNLOAD_STATUS_COMPLETED)
                        results.add(cursor.getString(0));

                } while (cursor.moveToNext());
            }
            String[] t = new String[results.size()];
            for (int i = 0; i < results.size(); i++) {
                t[i] = results.get(i);
            }
            final StringBuilder selection = new StringBuilder();
            selection.append(DownLoadInfo.KEY_DOWNLOAD_ID + " IN (");
            for (int i = 0; i < t.length; i++) {
                selection.append(t[i]);
                if (i < t.length - 1) {
                    selection.append(",");
                }
            }
            selection.append(")");
            database.delete(TABLE_NAME, selection.toString(), null);
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }


    }




    /**
     * 获取根据下载id，获取下载信息
     * @param Id
     * @return
     */
    public synchronized DownLoadInfo getDownLoadInfo(String Id) {
        Cursor cursor = null;
        DownLoadInfo entity = null;
        try {
            cursor = mMusicDatabase.getReadableDatabase().query(TABLE_NAME, null,
                    DownLoadInfo.KEY_DOWNLOAD_ID + " = ?", new String[]{String.valueOf(Id)}, null, null, null);
            if (cursor == null) {
                return null;
            }

            if (cursor.moveToFirst()) {

                do {
                    entity = new DownLoadInfo(cursor.getString(0), cursor.getLong(1), cursor.getLong(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7));
                } while (cursor.moveToNext());
                return entity;
            } else return null;

        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }


    /**
     * 获取所有下载信息
     * @return
     */
    public synchronized ArrayList<DownLoadInfo> getDowningDownLoadInfoList() {
        ArrayList<DownLoadInfo> results = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = mMusicDatabase.getReadableDatabase().query(TABLE_NAME, null,
                    null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                results.ensureCapacity(cursor.getCount());

                while(cursor.moveToNext()) {
                    //还没下载完成的，加入到list当中
                    if (cursor.getInt(cursor.getColumnIndex(DownLoadInfo.KEY_DOWNLOADSTATUS)) != DownloadStatus.DOWNLOAD_STATUS_COMPLETED) {
                        DownLoadInfo info = new DownLoadInfo();

                        info.setDownloadId(cursor.getString(cursor.getColumnIndex(DownLoadInfo.KEY_DOWNLOAD_ID)));
                        info.setTotalSize(cursor.getLong(cursor.getColumnIndex(DownLoadInfo.KEY_TOTALSIZE)));
                        info.setCompletedSize(cursor.getLong(cursor.getColumnIndex(DownLoadInfo.KEY_COMPLETEDSIZE)));
                        info.setUrl(cursor.getString(cursor.getColumnIndex(DownLoadInfo.KEY_URL)));
                        info.setSaveDirPath(cursor.getString(cursor.getColumnIndex(DownLoadInfo.KEY_SAVEDIRPATH)));
                        info.setFileName(cursor.getString(cursor.getColumnIndex(DownLoadInfo.KEY_FILENAME)));
                        info.setArtist(cursor.getString(cursor.getColumnIndex(DownLoadInfo.KEY_ARTIST)));
                        info.setDownloadStatus(cursor.getInt(cursor.getColumnIndex(DownLoadInfo.KEY_DOWNLOADSTATUS)));

                        results.add(info);
                    }
                }
            }

            return results;
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }


    /**
     *  获取所有正在下载的歌曲信息的id
     */
    public synchronized String[] getDownLoadInfoListAllDowningIds() {
        ArrayList<String> results = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = mMusicDatabase.getReadableDatabase().query(TABLE_NAME, null,
                    null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                results.ensureCapacity(cursor.getCount());

                do {
                    if (cursor.getInt(7) != DownloadStatus.DOWNLOAD_STATUS_COMPLETED)
                        results.add(cursor.getString(0));

                } while (cursor.moveToNext());
            }
            String[] t = new String[results.size()];
            for (int i = 0; i < results.size(); i++) {
                t[i] = results.get(i);
            }
            return t;
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }


}
