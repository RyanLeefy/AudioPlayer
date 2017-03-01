package com.example.administrator.audioplayer.Imodel;

import com.example.administrator.audioplayer.jsonbean.HotWord;

import rx.Observable;

/**
 * Created by on 2017/2/27 0027.
 */

public interface IPreSearchModel {
    Observable<HotWord> getHotWord();
}
