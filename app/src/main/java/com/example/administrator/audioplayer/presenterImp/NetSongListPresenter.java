package com.example.administrator.audioplayer.presenterImp;

import com.example.administrator.audioplayer.Imodel.INetSongListModel;
import com.example.administrator.audioplayer.Ipresenter.INetSongListPresenter;
import com.example.administrator.audioplayer.Iview.INetSongListView;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.adapter.NetSongListAdapter;
import com.example.administrator.audioplayer.fragment.NetSongListFragment;
import com.example.administrator.audioplayer.jsonbean.SongCollection;
import com.example.administrator.audioplayer.modelImp.NetSongListModel;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by on 2017/2/24 0024.
 */

public class NetSongListPresenter implements INetSongListPresenter {

    private INetSongListView view;
    private INetSongListModel model;

    private List<SongCollection.ContentBean> mList;

    public NetSongListPresenter(INetSongListView view) {
        this.view = view;
        model = new NetSongListModel();
    }

    @Override
    public void onCreateView() {
        //显示第一页
        showSongCollection(1);
    }


    /**
     * 网络获取歌单，传入页码
     * @param pageNo
     */
    public void showSongCollection(final int pageNo) {

        //如果上不了网，则直接显示网络无法连接，点击重连
        if(!CommonUtils.isConnectInternet(MyApplication.getContext())) {
            view.showTryAgain();
            return;
        }

        //获取热门歌单数据并显示
        Subscriber<SongCollection> subscriber = new Subscriber<SongCollection>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e.toString());
            }

            @Override
            public void onNext(SongCollection songCollection) {
                //设置adapter，刷新界面
                Logger.d(songCollection.getContent().size());
                mList = songCollection.getContent();
                if(pageNo == 1) {
                    //第一次请求，显示第一页
                    NetSongListAdapter adapter = new NetSongListAdapter(((NetSongListFragment) view).getActivity(), mList);
                    view.setAdapter(adapter);
                } else {
                    view.updateAdapter(mList);
                }
            }
        };

        //每页显示6个
        Observable observable = model.getSongCollection(pageNo, 12);


        ((NetSongListFragment) view).addSubscription(
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));
    }

}
