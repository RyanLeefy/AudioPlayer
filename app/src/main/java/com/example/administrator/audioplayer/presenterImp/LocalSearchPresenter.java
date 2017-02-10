package com.example.administrator.audioplayer.presenterImp;

import com.example.administrator.audioplayer.Imodel.ILocalSearchModel;
import com.example.administrator.audioplayer.Ipresenter.ILocalSearchPresenter;
import com.example.administrator.audioplayer.Iview.ILocalSearchView;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.fragment.LocalSearchFragment;
import com.example.administrator.audioplayer.modelImp.LocalSearchModel;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by on 2017/2/4.
 */

public class LocalSearchPresenter implements ILocalSearchPresenter {

    private ILocalSearchView view;   //localsearchfragment传进来
    private ILocalSearchModel model;

    public LocalSearchPresenter(ILocalSearchView view) {
        this.view = view;
        model = new LocalSearchModel();
    }


    @Override
    public void performSearch(final String queryString) {
        //从本地音乐里面搜索
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
            public void onNext(List<MusicInfo> musicInfos) {
                MusicAdapter adapter = new MusicAdapter(((LocalSearchFragment) view).getActivity(), musicInfos);
                view.setAdapter(adapter);
            }
        };


        Observable observable = Observable.create(new Observable.OnSubscribe<List>() {
            @Override
            public void call(Subscriber<? super List> subscriber) {
                subscriber.onNext(model.searchLocalMusic(queryString));
                subscriber.onCompleted();
            }
        });

        ((LocalSearchFragment)view).addSubscription((
                observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)));

    }


}
