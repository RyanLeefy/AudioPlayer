package com.example.administrator.audioplayer.Imodel;

import com.example.administrator.audioplayer.jsonbean.SongCollectionInfo;

import rx.Observable;

/**
 * Created by on 2017/2/20 0020.
 */

public interface ISongCollectionModel {
    Observable<SongCollectionInfo> getSongCollectionInfo(String listid);
    boolean collect(String title, int SongCount, String pic, String songListId, String tag, String songCollectionCount);
}
