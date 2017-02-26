package com.example.administrator.audioplayer.modelImp;

import com.example.administrator.audioplayer.Imodel.IBillBoardModel;
import com.example.administrator.audioplayer.http.HttpMethods;
import com.example.administrator.audioplayer.jsonbean.BillBoard;

import rx.Observable;

/**
 * Created by on 2017/2/26 0026.
 */

public class BillBoardModel implements IBillBoardModel {
    @Override
    public Observable<BillBoard> getBillBoard(int type, int offset, int size) {
        return HttpMethods.getInstance().billSongList(type, offset, size);
    }
}
