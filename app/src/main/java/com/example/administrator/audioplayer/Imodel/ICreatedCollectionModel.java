package com.example.administrator.audioplayer.Imodel;

import com.example.administrator.audioplayer.bean.MusicInfo;

import java.util.List;

/**
 * Created by on 2017/3/22 0022.
 */

public interface ICreatedCollectionModel {
    List<MusicInfo> getCollectionSong(int id);
    void cancelCollect(int id, MusicInfo musicInfo);
}
