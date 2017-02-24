package com.example.administrator.audioplayer.Imodel;

import com.example.administrator.audioplayer.jsonbean.SongCollection;

import rx.Observable;

/**
 * Created by on 2017/2/24 0024.
 */

public interface INetSongListModel {
    Observable<SongCollection> getSongCollection(int pageNo, int sizeNo);
}
