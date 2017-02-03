package com.example.administrator.audioplayer.presenterImp;

import com.example.administrator.audioplayer.Imodel.ILocalMusicModel;
import com.example.administrator.audioplayer.Ipresenter.ILocalMusicPresenter;
import com.example.administrator.audioplayer.Iview.ILocalMusicView;
import com.example.administrator.audioplayer.modelImp.LocalMusicModel;

/**
 * Created by on 2017/2/3.
 */

public class LocalMusicPresenter implements ILocalMusicPresenter {

    private ILocalMusicView view;
    private ILocalMusicModel model;

    public LocalMusicPresenter(ILocalMusicView view) {
        this.view = view;
        model = new LocalMusicModel();
    }

    @Override
    public void onCreate() {
        //TODO 创建Subcriber，调用model的方法和view的方法进行业务逻辑和界面显示
    }

    @Override
    public void onDestroy() {

    }
}
