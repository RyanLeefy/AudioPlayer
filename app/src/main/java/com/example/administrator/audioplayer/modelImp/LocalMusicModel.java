package com.example.administrator.audioplayer.modelImp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.administrator.audioplayer.Imodel.ILocalMusicModel;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.bean.MusicInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by on 2017/2/3.
 */

public class LocalMusicModel implements ILocalMusicModel{

    public static final int FILTER_SIZE = 1 * 1024 * 1024;// 1MB
    public static final int FILTER_DURATION = 1 * 60 * 1000;// 1分钟


    //DATA是音频的绝对路径
    private static String[] proj_music = new String[]{
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.SIZE};


    //通过contentprovider获取sd卡上的音频,返回查询到的音乐List
    @Override
    public List getLocalMusic() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = MyApplication.getContext().getContentResolver();

        // 查询语句：检索出时长大于1分钟，文件大小大于1MB的媒体文件
        StringBuilder select = new StringBuilder("title != ''");
        select.append(" and " + MediaStore.Audio.Media.SIZE + " > " + FILTER_SIZE);
        select.append(" and " + MediaStore.Audio.Media.DURATION + " > " + FILTER_DURATION);

        Cursor cursor = cr.query(uri, proj_music, select.toString(), null, null);

        ArrayList<MusicInfo> musicList = new ArrayList<>();
        while (cursor.moveToNext()) {

            MusicInfo music = new MusicInfo();
            music.setSongId(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            music.setAlbumId(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            music.setAlbumName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));
            music.setAlbumData(getAlbumArtUri(music.getAlbumId()) + "");
            music.setDuration(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            music.setMusicName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            music.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            music.setArtistId(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)));
            String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            music.setData(filePath);
            music.setFolder(filePath.substring(0, filePath.lastIndexOf(File.separator)));
            music.setSize(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
            music.setIslocal(true);
            //music.setSort(Pinyin.toPinyin(music.musicName.charAt(0)).substring(0, 1).toUpperCase()); //排序
            musicList.add(music);
        }
        cursor.close();
        return musicList;
    }


    public static Uri getAlbumArtUri(long albumId) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
    }





}
