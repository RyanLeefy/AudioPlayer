package com.example.administrator.audioplayer.modelImp;

import com.example.administrator.audioplayer.Imodel.IRankingModel;
import com.example.administrator.audioplayer.http.HttpMethods;
import com.example.administrator.audioplayer.jsonbean.BillBoard;

import rx.Observable;

/**
 * Created by on 2017/2/25 0025.
 */

public class RankingModel implements IRankingModel {
    @Override
    public Observable<BillBoard> getBillBoard(int type, int offset, int size) {
        return HttpMethods.getInstance().billSongList(type, offset, size);
    }
}
