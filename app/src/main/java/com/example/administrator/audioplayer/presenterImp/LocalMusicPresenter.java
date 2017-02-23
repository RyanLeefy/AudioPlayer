package com.example.administrator.audioplayer.presenterImp;

import com.example.administrator.audioplayer.Imodel.ILocalMusicModel;
import com.example.administrator.audioplayer.Ipresenter.ILocalMusicPresenter;
import com.example.administrator.audioplayer.Iview.ILocalMusicView;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.fragment.LocalMusicFragment;
import com.example.administrator.audioplayer.modelImp.LocalMusicModel;
import com.example.administrator.audioplayer.service.MusicPlayer;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
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

    private List<MusicInfo> mList;


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
                mList = list;
                MusicAdapter adapter = new MusicAdapter(((LocalMusicFragment)view).getActivity(), list, true);
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

    @Override
    public void peformMusicClick(int position) {
        //position应该大于等于1
        if(position == 0) {
            //点击播放全部按钮
        } else {
            /*
            long[] list = new long[mList.size()];
            HashMap<Long, MusicInfo> infos = new HashMap();
            for (int i = 0; i < mList.size(); i++) {
                MusicInfo info = mList.get(i);
                list[i] = info.getAudioId();
                info.setIslocal(true);
                //info.albumData = MusicUtils.getAlbumArtUri(info.albumId) + "";
                infos.put(list[i], mList.get(i));
            }*/

            //position - 1 对应歌单中的位置
            MusicPlayer.playAll(mList, position - 1, false);
        }
    }


}
