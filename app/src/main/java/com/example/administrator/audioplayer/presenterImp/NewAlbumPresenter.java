package com.example.administrator.audioplayer.presenterImp;

import com.example.administrator.audioplayer.Imodel.INewAlbumModel;
import com.example.administrator.audioplayer.Ipresenter.INewAlbumPresenter;
import com.example.administrator.audioplayer.Iview.INewAlbumView;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.activity.NewAlbumActivity;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.jsonbean.Album;
import com.example.administrator.audioplayer.modelImp.NewAlbumModel;
import com.example.administrator.audioplayer.service.MusicPlayer;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.example.administrator.audioplayer.utils.PrintLog;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by on 2017/2/23 0023.
 */

public class NewAlbumPresenter implements INewAlbumPresenter {


    private INewAlbumView view;
    private INewAlbumModel model;

    List<MusicInfo> adapterList = new ArrayList<>();

    public NewAlbumPresenter(INewAlbumView view) {
        this.view = view;
        model = new NewAlbumModel();
    }
    @Override
    public void onCreate(String albumid) {
        //用Rxjava进行异步处理
        //第一步创建Subscriber
        //第二步创建Observable
        //第三不用doOnNext对返回的数据进行处理
        //第三步订阅，并添加到父类的CompositeSubscription中，进行管理

        //如果上不了网，则直接显示网络无法连接，点击重连
        if(!CommonUtils.isConnectInternet(MyApplication.getContext())) {
            view.showTryAgain();
            return;
        }

        //获取专辑数据并显示
        ((NewAlbumActivity)view).addSubscription(
                model.getNewAlbumInfo(albumid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Album>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        PrintLog.e(e.toString());
                    }

                    @Override
                    public void onNext(Album album) {
                        for(int i = 0; i < album.getSonglist().size();i++) {
                            MusicInfo musicInfo = new MusicInfo();
                            musicInfo.setAudioId(Integer.parseInt(album.getSonglist().get(i).getSong_id()));
                            musicInfo.setMusicName(album.getSonglist().get(i).getTitle());
                            musicInfo.setArtist(album.getSonglist().get(i).getAuthor());
                            musicInfo.setArtistId(Integer.parseInt(album.getSonglist().get(i).getArtist_id()));
                            musicInfo.setAlbumName(album.getSonglist().get(i).getAlbum_title());
                            musicInfo.setAlbumId(Integer.parseInt(album.getSonglist().get(i).getAlbum_id()));
                            musicInfo.setLrc(album.getSonglist().get(i).getLrclink());
                            musicInfo.setAlbumData(album.getSonglist().get(i).getPic_big());
                            musicInfo.setIslocal(false);
                            adapterList.add(musicInfo);
                        }

                        //设置adapter
                        MusicAdapter adapter = new MusicAdapter(((NewAlbumActivity) view), adapterList, true);
                        //设置第一项有头部偏移量
                        adapter.setHasTopPadding(true);
                        //设置有序号
                        adapter.setHasTrackNumber(true);
                        view.setAdapter(adapter);
                    }})
        );
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
}
