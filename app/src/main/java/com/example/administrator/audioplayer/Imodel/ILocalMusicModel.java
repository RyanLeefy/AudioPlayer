package com.example.administrator.audioplayer.Imodel;

import com.example.administrator.audioplayer.bean.MusicInfo;

import java.util.List;

import rx.Subscriber;

/**
 * Created by on 2017/2/3.
 */

public interface ILocalMusicModel {
    void getLocalMusic(Subscriber<List<MusicInfo>> subscriber);
}
