package com.example.administrator.audioplayer.Imodel;


import com.example.administrator.audioplayer.jsonbean.RecommendNewAlbum;
import com.example.administrator.audioplayer.jsonbean.RecommendSongCollection;

import rx.Observable;

/**
 * Created by on 2017/2/17 0017.
 */

public interface IRecommendModel {
    Observable<RecommendSongCollection> getRecommendSongCollectionList();
    Observable<RecommendNewAlbum> getRecommendNewAlbumList();
}
