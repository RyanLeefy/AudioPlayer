package com.example.administrator.audioplayer.presenterImp;

import com.example.administrator.audioplayer.Imodel.IPreSearchModel;
import com.example.administrator.audioplayer.Ipresenter.IPreSearchPresenter;
import com.example.administrator.audioplayer.Iview.IPreSearchView;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.adapter.SearchHistoryAdapter;
import com.example.administrator.audioplayer.db.SearchHistory;
import com.example.administrator.audioplayer.fragment.PreSearchFragment;
import com.example.administrator.audioplayer.jsonbean.HotWord;
import com.example.administrator.audioplayer.modelImp.PreSearchModel;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by on 2017/2/27 0027.
 */

public class PreSearchPresenter implements IPreSearchPresenter {

    private IPreSearchView view;
    private IPreSearchModel model;

    //热门歌词的list
    private List<String> mList = new ArrayList<>();

    //搜索历史的list
    private List<String> mHistoryList = new ArrayList<>();

    public PreSearchPresenter(IPreSearchView view) {
        this.view = view;
        model = new PreSearchModel();
    }


    @Override
    public void onCreateView() {
        //如果上不了网，则直接显示网络无法连接，点击重连
        if(!CommonUtils.isConnectInternet(MyApplication.getContext())) {
            view.showTryAgain();
            return;
        }


        //获取热门词数据并显示
        Subscriber<HotWord> subscriber = new Subscriber<HotWord>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e.toString());
            }

            @Override
            public void onNext(HotWord hotWord) {
                //设置adapter，刷新界面
                Logger.d(hotWord.getResult().size());

                //获取前10个热词并装在list中返回
                for(int i = 0; i < 10; i++) {
                    mList.add(hotWord.getResult().get(i).getWord());
                }
                view.setHotWord(mList);
            }
        };


        Observable observable = model.getHotWord();


        ((PreSearchFragment) view).addSubscription(
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));





        //获取历史搜索并显示
        ((PreSearchFragment) view).addSubscription(
        Observable.create(new Observable.OnSubscribe<List>() {
            @Override
            public void call(Subscriber<? super List> subscriber) {
                mHistoryList = SearchHistory.getInstance(MyApplication.getContext()).getRecentSearches();
                subscriber.onNext(mHistoryList);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                    }

                    @Override
                    public void onNext(List list) {
                        SearchHistoryAdapter adapter = new SearchHistoryAdapter(((PreSearchFragment)view).getActivity(), list);
                        view.setSearchHistory(adapter);
                    }
                })
        );



    }
}
