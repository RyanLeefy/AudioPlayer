package com.example.administrator.audioplayer.presenterImp;

import com.example.administrator.audioplayer.Imodel.ICreatedCollectionModel;
import com.example.administrator.audioplayer.Ipresenter.ICreatedCollectionPresenter;
import com.example.administrator.audioplayer.Iview.ICreatedCollectionView;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.activity.CreatedCollectionActivity;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.db.SongCollectionSongDB;
import com.example.administrator.audioplayer.modelImp.CreatedCollectionModel;
import com.example.administrator.audioplayer.service.MusicPlayer;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by on 2017/3/22 0022.
 */

public class CreatedCollectionPresenter implements ICreatedCollectionPresenter {
    private ICreatedCollectionView view;
    private ICreatedCollectionModel model;

    private MusicAdapter adapter;

    List<MusicInfo> adapterList = new ArrayList<>();


    public CreatedCollectionPresenter(ICreatedCollectionView view) {
        this.view = view;
        model = new CreatedCollectionModel();
    }

    @Override
    public void onCreate(final int id) {

        //用Rxjava进行异步处理
        //第一步创建Subscriber
        //第二步创建Observable
        //第三步订阅，并添加到父类的CompositeSubscription中，进行管理

        Subscriber<List<MusicInfo>> subscriber = new Subscriber<List<MusicInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.d("Error");
            }

            @Override
            public void onNext(List list) {
                //设置adapter，刷新界面
                adapterList = list;
                adapter =  new MusicAdapter(((CreatedCollectionActivity) view), adapterList, true);
                //设置第一项有头部偏移量
                adapter.setHasTopPadding(true);
                //设置有序号
                adapter.setHasTrackNumber(true);
                view.setAdapter(adapter);
            }
        };


        Observable observable = Observable.create(new Observable.OnSubscribe<List>() {
            @Override
            public void call(Subscriber<? super List> subscriber) {
                //调用model获取数据
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(model.getCollectionSong(id));
                subscriber.onCompleted();
            }
        });


        ((CreatedCollectionActivity)view).addSubscription(
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));


        /*
        //获取热门歌单数据并显示
        adapterList = SongCollectionSongDB.getInstance(MyApplication.getContext()).getSongListById(id);
        //设置adapter
        adapter = new MusicAdapter(((CreatedCollectionActivity) view), adapterList, true);
        //设置第一项有头部偏移量
        adapter.setHasTopPadding(true);
        //设置有序号
        adapter.setHasTrackNumber(true);
        view.setAdapter(adapter);*/
    }


    @Override
    public void peformMusicClick(int position) {
        //position应该大于等于1
        if(position == 0) {
            //点击播放全部按钮
            MusicPlayer.playAll(adapterList, 0, false);
        } else {

            //position - 1 对应歌单中的位置
            MusicPlayer.playAll(adapterList, position - 1, false);
        }
    }

    @Override
    public void peformCancelCollection(int id, MusicInfo musicInfo) {
        model.cancelCollect(id, musicInfo);
    }
}
