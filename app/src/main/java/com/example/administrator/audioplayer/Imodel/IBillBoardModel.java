package com.example.administrator.audioplayer.Imodel;

import com.example.administrator.audioplayer.jsonbean.BillBoard;

import rx.Observable;

/**
 * Created by on 2017/2/26 0026.
 */

public interface IBillBoardModel {
    Observable<BillBoard> getBillBoard(int type, int offset, int size);
}
