package com.example.administrator.audioplayer.modelImp;

import com.example.administrator.audioplayer.Imodel.IPreSearchModel;
import com.example.administrator.audioplayer.http.HttpMethods;
import com.example.administrator.audioplayer.jsonbean.HotWord;

import rx.Observable;

/**
 * Created by on 2017/2/27 0027.
 */

public class PreSearchModel implements IPreSearchModel {
    @Override
    public Observable<HotWord> getHotWord() {
        return HttpMethods.getInstance().hotword();
    }
}
