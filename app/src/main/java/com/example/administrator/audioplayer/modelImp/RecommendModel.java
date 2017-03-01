package com.example.administrator.audioplayer.modelImp;

import com.example.administrator.audioplayer.Imodel.IRecommendModel;
import com.example.administrator.audioplayer.http.HttpMethods;
import com.example.administrator.audioplayer.jsonbean.RecommendNewAlbum;
import com.example.administrator.audioplayer.jsonbean.RecommendSongCollection;

import rx.Observable;

/**
 * Created by on 2017/2/17 0017.
 */

public class RecommendModel implements IRecommendModel {
    @Override
    public Observable<RecommendSongCollection> getRecommendSongCollectionList() {
        return HttpMethods.getInstance().hotGeDan(6);
    }

    @Override
    public Observable<RecommendNewAlbum> getRecommendNewAlbumList() {
        return HttpMethods.getInstance().recommendAlbum(0, 6);
    }
}
