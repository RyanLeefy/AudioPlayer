package com.example.administrator.audioplayer.presenterImp;

import com.example.administrator.audioplayer.Imodel.IBillBoardModel;
import com.example.administrator.audioplayer.Ipresenter.IBillBoardPresenter;
import com.example.administrator.audioplayer.Iview.IBillBoardView;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.activity.BillBoardActivity;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.jsonbean.BillBoard;
import com.example.administrator.audioplayer.modelImp.BillBoardModel;
import com.example.administrator.audioplayer.service.MusicPlayer;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by on 2017/2/26 0026.
 */

public class BillBoardPresenter implements IBillBoardPresenter{

    private IBillBoardView view;
    private IBillBoardModel model;

    private List<MusicInfo> mList = new ArrayList<>();

    public BillBoardPresenter(IBillBoardView view) {
        this.view = view;
        model = new BillBoardModel();
    }



    @Override
    public void onCreate(int type) {
        //用Rxjava进行异步处理
        //第一步创建Subscriber
        //第二步创建Observable
        //第三不用doOnNext对返回的数据进行处理
        //第三步订阅，并添加到父类的CompositeSubscription中，进行管理

        //如果上不了网，则直接显示网络无法连接，点击重连
        if(!CommonUtils.isConnectInternet(MyApplication.getContext())) {
            view.showTryAgain();
            return;
        }


        Subscriber<BillBoard> subscriber = new Subscriber<BillBoard>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e.toString());
            }

            @Override
            public void onNext(BillBoard billBoard) {
                Logger.d(billBoard.getSong_list().size());
                for(int i = 0; i < billBoard.getSong_list().size(); i++) {
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setAudioId(Integer.parseInt(billBoard.getSong_list().get(i).getSong_id()));
                    musicInfo.setMusicName(billBoard.getSong_list().get(i).getTitle());
                    musicInfo.setArtist(billBoard.getSong_list().get(i).getArtist_name());
                    musicInfo.setArtistId(Integer.parseInt(billBoard.getSong_list().get(i).getArtist_id()));
                    musicInfo.setAlbumName(billBoard.getSong_list().get(i).getAlbum_title());
                    musicInfo.setAlbumId(Integer.parseInt(billBoard.getSong_list().get(i).getAlbum_id()));
                    musicInfo.setLrc(billBoard.getSong_list().get(i).getLrclink());
                    musicInfo.setAlbumData(billBoard.getSong_list().get(i).getPic_big());
                    musicInfo.setIslocal(false);
                    mList.add(musicInfo);
                }
                //设置adapter
                MusicAdapter adapter = new MusicAdapter(((BillBoardActivity) view), mList, true);
                //设置第一项有头部偏移量
                adapter.setHasTopPadding(true);
                //设置有序号
                adapter.setHasTrackNumber(true);
                view.setAdapter(adapter);

            }
        };

        //偏移0，获取前3个
        Observable observable = model.getBillBoard(type, 0, 100);


        ((BillBoardActivity) view).addSubscription(
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));
    }

    @Override
    public void peformMusicClick(int position) {
        //position应该大于等于1
        if(position == 0) {
            //点击播放全部按钮
            MusicPlayer.playAll(mList, 0, false);
        } else {

            //position - 1 对应歌单中的位置
            MusicPlayer.playAll(mList, position - 1, false);
        }
    }
}
