package com.example.administrator.audioplayer.modelImp;

import com.example.administrator.audioplayer.Imodel.ISongCollectionModel;
import com.example.administrator.audioplayer.http.HttpMethods;
import com.example.administrator.audioplayer.jsonbean.SongCollectionInfo;

import rx.Observable;

/**
 * Created by on 2017/2/20 0020.
 */

public class SongCollectionModel implements ISongCollectionModel {
    @Override
    public Observable<SongCollectionInfo> getSongCollectionInfo(String listid) {
        return HttpMethods.getInstance().geDanInfo(listid);
    }
}
