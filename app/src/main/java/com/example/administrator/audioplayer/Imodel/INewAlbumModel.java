package com.example.administrator.audioplayer.Imodel;



import com.example.administrator.audioplayer.jsonbean.Album;

import rx.Observable;

/**
 * Created by on 2017/2/23 0023.
 */

public interface INewAlbumModel {
    Observable<Album> getNewAlbumInfo(String albumid);
}
