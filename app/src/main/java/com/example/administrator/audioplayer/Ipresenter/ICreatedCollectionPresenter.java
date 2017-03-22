package com.example.administrator.audioplayer.Ipresenter;

import com.example.administrator.audioplayer.bean.MusicInfo;

/**
 * Created by on 2017/3/22 0022.
 */

public interface ICreatedCollectionPresenter {
    void onCreate(int id);
    void peformMusicClick(int position);
    void peformCancelCollection(int id, MusicInfo musicInfo);
}
