package com.example.administrator.audioplayer.presenterImp;

import com.example.administrator.audioplayer.Ipresenter.IPlayQueuePresenter;
import com.example.administrator.audioplayer.Iview.IPlayQueueView;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.fragment.LocalMusicFragment;
import com.example.administrator.audioplayer.fragment.PlayQueueFragment;
import com.example.administrator.audioplayer.service.MusicPlayer;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by on 2017/2/11 0011.
 */

public class PlayQueuePresenter implements IPlayQueuePresenter {

    private IPlayQueueView view;

    private List<MusicInfo> mList;

    private MusicAdapter adapter;

    public PlayQueuePresenter(IPlayQueueView view) {
        this.view = view;
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
                view.updateSongNumber(list.size());
                adapter = new MusicAdapter(((PlayQueueFragment)view).getActivity(), list);
                view.setAdapter(adapter);
            }
        };


        Observable observable = Observable.create(new Observable.OnSubscribe<List>() {
            @Override
            public void call(Subscriber<? super List> subscriber) {
                subscriber.onNext(MusicPlayer.getQueue());
                subscriber.onCompleted();
            }
        });


        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    @Override
    public void peformMusicClick(int position) {
        MusicPlayer.playAll(mList, position, false);
    }

    @Override
    public void peformDeleteClick(int position) {
        long audioid = mList.get(position).getAudioId();
        //adapter.notifyItemRemoved(position);
        MusicPlayer.removeTrack(audioid);
        mList = MusicPlayer.getQueue();
        adapter.updateAdapter(mList);
        //如果没有了就暂停
        if (mList == null) {
            MusicPlayer.stop();
        }
        //如果是目前正在播放的歌曲，则播放下一首
        if (MusicPlayer.isPlaying() && (MusicPlayer.getCurrentAudioId() == audioid)) {
            MusicPlayer.next();
        }
        adapter.notifyDataSetChanged();
        view.updateSongNumber(mList.size());
    }
}
