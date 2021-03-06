package com.example.administrator.audioplayer.presenterImp;

import android.content.Intent;
import android.net.Uri;

import com.example.administrator.audioplayer.Imodel.ILocalSearchModel;
import com.example.administrator.audioplayer.Ipresenter.ILocalSearchPresenter;
import com.example.administrator.audioplayer.Iview.ILocalSearchView;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.fragment.LocalSearchFragment;
import com.example.administrator.audioplayer.modelImp.LocalSearchModel;
import com.example.administrator.audioplayer.service.MusicPlayer;
import com.orhanobut.logger.Logger;

import java.io.File;
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

    private List<MusicInfo> mList;
    private MusicAdapter adapter;

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
                mList = musicInfos;
                adapter = new MusicAdapter(((LocalSearchFragment) view).getActivity(), musicInfos);
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

    /**
     * 删除歌曲，删除成功返回true  步骤：先删除本地的音乐，再刷新音乐库，再删除列表，更新数据，刷新adapter
     * @param position
     * @return
     */
    @Override
    public boolean performMusicDelete(int position) {
        MusicInfo info = mList.get(position);
        if(info.getData() != null) {
            File file = new File(info.getData());
            if(file.isFile() && file.exists()) {
                //删除文件
                file.delete();
                Uri contentUri = Uri.fromFile(new File(info.getData()));
                //发送广播，扫描音乐刷新音乐库
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
                ((LocalSearchFragment)view).getActivity().sendBroadcast(mediaScanIntent);
                //在列表中给删除
                mList.remove(position);
                //更新adapter的列表
                adapter.updateAdapter(mList);
                //刷新数据
                adapter.notifyDataSetChanged();
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

}
