package com.example.administrator.audioplayer.modelImp;

import com.example.administrator.audioplayer.Imodel.INetSongListModel;
import com.example.administrator.audioplayer.http.HttpMethods;
import com.example.administrator.audioplayer.jsonbean.SongCollection;

import rx.Observable;

/**
 * Created by on 2017/2/24 0024.
 */

public class NetSongListModel implements INetSongListModel {
    @Override
    public Observable<SongCollection> getSongCollection(int pageNo, int sizeNo) {
        return HttpMethods.getInstance().geDan(pageNo, sizeNo);
    }
}
