package com.example.administrator.audioplayer.Imodel;

import com.example.administrator.audioplayer.jsonbean.BillBoard;

import rx.Observable;

/**
 * Created by on 2017/2/25 0025.
 */

public interface IRankingModel {
    Observable<BillBoard> getBillBoard(int type, int offset, int size);
}
