package com.example.administrator.audioplayer.modelImp;

import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.example.administrator.audioplayer.Imodel.ILocalSearchModel;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.bean.MusicInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.administrator.audioplayer.modelImp.LocalMusicModel.FILTER_DURATION;
import static com.example.administrator.audioplayer.modelImp.LocalMusicModel.FILTER_SIZE;
import static com.example.administrator.audioplayer.modelImp.LocalMusicModel.getAlbumArtUri;


/**
 * Created by on 2017/2/4.
 */

public class LocalSearchModel implements ILocalSearchModel {

    @Override
    public List searchLocalMusic(String searchString) {
        return getSongsForCursor(makeSongCursor("title LIKE ?", new String[]{"%" + searchString + "%"}));
    }

    public ArrayList<MusicInfo> getSongsForCursor(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                MusicInfo music = new MusicInfo();
                music.setAudioId(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
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
                arrayList.add(music);
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;
    }

    public Cursor makeSongCursor(String selection, String[] paramArrayOfString) {
        StringBuilder selectionStatement = new StringBuilder("title != ''");
        selectionStatement.append(" and " + MediaStore.Audio.Media.SIZE + " > " + FILTER_SIZE);
        selectionStatement.append(" and " + MediaStore.Audio.Media.DURATION + " > " + FILTER_DURATION);
        // final String songSortOrder = PreferencesUtility.getInstance(context).getSongSortOrder();

        if (!TextUtils.isEmpty(selection)) {
            selectionStatement.append(" AND " + selection);
        }
        Cursor cursor = MyApplication.getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "title", "_data", "artist", "album", "duration", "_size", "artist_id", "album_id"}, selectionStatement.toString(), paramArrayOfString, null);

        return cursor;
    }

}
