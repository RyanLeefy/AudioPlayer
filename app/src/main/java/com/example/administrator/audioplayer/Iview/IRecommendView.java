package com.example.administrator.audioplayer.Iview;

import com.example.administrator.audioplayer.adapter.RecommendNewAlbumAdapter;
import com.example.administrator.audioplayer.adapter.RecommendSongCollectionAdapter;

/**
 * Created by on 2017/2/17 0017.
 */

public interface IRecommendView {
    void setRecommendSongCollectionAdapter(RecommendSongCollectionAdapter adapter);
    void setRecommendNewAlbumAdapter(RecommendNewAlbumAdapter albumAdapter);
}
