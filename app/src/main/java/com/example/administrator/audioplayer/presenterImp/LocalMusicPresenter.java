package com.example.administrator.audioplayer.presenterImp;

import com.example.administrator.audioplayer.Imodel.ILocalMusicModel;
import com.example.administrator.audioplayer.Ipresenter.ILocalMusicPresenter;
import com.example.administrator.audioplayer.Iview.ILocalMusicView;
import com.example.administrator.audioplayer.adapter.LocalMusicAdapter;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.fragment.LocalMusicFragment;
import com.example.administrator.audioplayer.modelImp.LocalMusicModel;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by on 2017/2/3.
 */

public class LocalMusicPresenter implements ILocalMusicPresenter {

    private ILocalMusicView view;   //localmusicfragment传进来
    private ILocalMusicModel model;


    public LocalMusicPresenter(ILocalMusicView view) {
        this.view = view;
        model = new LocalMusicModel();
    }

    @Override
    public void onCreateView() {
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
                Logger.e(e,"error");
            }

            @Override
            public void onNext(List list) {
                //设置adapter，刷新界面
                Logger.d(list.size());
                LocalMusicAdapter adapter = new LocalMusicAdapter(((LocalMusicFragment)view).getActivity(), list, true);
                view.setAdapter(adapter);
            }
        };


        Observable observable = Observable.create(new Observable.OnSubscribe<List>() {
            @Override
            public void call(Subscriber<? super List> subscriber) {
                subscriber.onNext(model.getLocalMusic());
                subscriber.onCompleted();
            }
        });

        ((LocalMusicFragment)view).addSubscription(
                observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }


}
