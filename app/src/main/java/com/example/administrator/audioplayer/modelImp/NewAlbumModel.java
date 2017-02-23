package com.example.administrator.audioplayer.modelImp;

import com.example.administrator.audioplayer.Imodel.INewAlbumModel;
import com.example.administrator.audioplayer.http.HttpMethods;
import com.example.administrator.audioplayer.jsonbean.Album;

import rx.Observable;

/**
 * Created by on 2017/2/23 0023.
 */

public class NewAlbumModel implements INewAlbumModel {
    @Override
    public Observable<Album> getNewAlbumInfo(String albumid) {
        return HttpMethods.getInstance().albumInfo(albumid);
    }
}
