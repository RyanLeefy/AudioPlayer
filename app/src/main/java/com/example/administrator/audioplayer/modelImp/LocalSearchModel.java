package com.example.administrator.audioplayer.modelImp;

import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.example.administrator.audioplayer.Imodel.ILocalSearchModel;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.bean.MusicInfo;

import java.util.ArrayList;
import java.util.List;



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
                MusicInfo musicInfo = new MusicInfo();
                musicInfo.setSongId((int) cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                musicInfo.setAlbumId(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                musicInfo.setMusicName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                musicInfo.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                musicInfo.setAlbumName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                arrayList.add(musicInfo);
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;
    }

    public Cursor makeSongCursor(String selection, String[] paramArrayOfString) {
        String selectionStatement = "title != ''";
        // final String songSortOrder = PreferencesUtility.getInstance(context).getSongSortOrder();

        if (!TextUtils.isEmpty(selection)) {
            selectionStatement = selectionStatement + " AND " + selection;
        }
        Cursor cursor = MyApplication.getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id"}, selectionStatement, paramArrayOfString, null);

        return cursor;
    }

}
