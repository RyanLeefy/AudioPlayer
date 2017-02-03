package com.example.administrator.audioplayer.presenterImp;

import com.example.administrator.audioplayer.Imodel.ILocalMusicModel;
import com.example.administrator.audioplayer.Ipresenter.ILocalMusicPresenter;
import com.example.administrator.audioplayer.Iview.ILocalMusicView;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.adapter.LocalMusicAdapter;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.fragment.LocalMusicFragment;
import com.example.administrator.audioplayer.modelImp.LocalMusicModel;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Subscriber;

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
        //TODO 创建Subcriber，调用model的方法和view的方法进行业务逻辑和界面显示

        Logger.d("oncreateView");
        Subscriber<List<MusicInfo>> subscriber = new Subscriber<List<MusicInfo>>() {
            @Override
            public void onCompleted() {
                Logger.d("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e,"error");
                Logger.d("onError");
            }

            @Override
            public void onNext(List list) {
                //设置adapter，刷新界面
                Logger.d("onNext");
                Logger.d(list.size());
                LocalMusicAdapter adapter = new LocalMusicAdapter(((LocalMusicFragment)view).getActivity(), list);
                view.setAdapter(adapter);
            }
        };


        model.getLocalMusic(subscriber);
    }

    @Override
    public void onDestroy() {

    }
}
