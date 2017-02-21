package com.example.administrator.audioplayer.presenterImp;

import com.example.administrator.audioplayer.Imodel.ISongCollectionModel;
import com.example.administrator.audioplayer.Ipresenter.ISongCollectionPresenter;
import com.example.administrator.audioplayer.Iview.ISongCollectionView;
import com.example.administrator.audioplayer.activity.SongCollectionActivity;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.fragment.LocalMusicFragment;
import com.example.administrator.audioplayer.http.HttpMethods;
import com.example.administrator.audioplayer.jsonbean.SongBaseInfo;
import com.example.administrator.audioplayer.jsonbean.SongCollectionInfo;
import com.example.administrator.audioplayer.modelImp.SongCollectionModel;
import com.example.administrator.audioplayer.utils.RequestThreadPool;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by on 2017/2/20 0020.
 */

public class SongCollectionPresenter implements ISongCollectionPresenter {

    private ISongCollectionView view;
    private ISongCollectionModel model;

    public SongCollectionPresenter(ISongCollectionView view) {
        this.view = view;
        model = new SongCollectionModel();
    }

    @Override
    public void onCreate(final String listid) {
        //用Rxjava进行异步处理
        //第一步创建Subscriber
        //第二步创建Observable
        //第三步订阅，并添加到父类的CompositeSubscription中，进行管理

        //获取热门歌单数据并显示


        //Observable observable3 = model.getSongCollectionInfo(listid);

        Subscriber<List<MusicInfo>> subscriber = new Subscriber<List<MusicInfo>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Logger.e("Error", e);
            }

            @Override
            public void onNext(List<MusicInfo> adapterList) {
                MusicAdapter adapter = new MusicAdapter(((SongCollectionActivity) view), adapterList, true);
                //设置第一项有头部偏移量
                adapter.setHasTopPadding(true);
                //设置有序号
                adapter.setHasTrackNumber(true);

                view.setAdapter(adapter);
            }

        };


        Observable observable = Observable.create(new Observable.OnSubscribe<List>() {

            @Override
            public void call(final Subscriber<? super List> subscriber) {

                Subscriber<SongCollectionInfo> subscriber2 = new Subscriber<SongCollectionInfo>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("Error", e);
                        //因为有一些歌单有版权问题，所以没有content，所以用retrofit解析的时候会报错
                        //所以在这里直接传一个空的列表设置adapter，显示空视图，即没有版权的视图
                        ((SongCollectionActivity)view).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MusicAdapter adapter = new MusicAdapter(((SongCollectionActivity) view), null, true);
                                view.setAdapter(adapter);
                            }
                        });
                    }

                    @Override
                    public void onNext(SongCollectionInfo songCollectionInfo) {
                        //设置adapter，刷新界面
                        Logger.d(songCollectionInfo.getContent().size());
                        //网络获取回来的歌单里面歌曲信息
                        final List<SongCollectionInfo.ContentBean> mList = songCollectionInfo.getContent();
                        //网络获取回来的每首歌的详细信息
                        HashMap<Integer, SongBaseInfo.ResultBean.ItemsBean> mDetailList = new HashMap<>();

                        List<MusicInfo> adapterList = new ArrayList<>();

                        for(int i = 0; i < mList.size();i++) {
                            RequestThreadPool.post(new getSongBaseInfo(mList.get(i).getSong_id(), i, mDetailList));
                        }

                        int tryCount = 0;
                        while (mDetailList.size() != mList.size() && tryCount < 1000) {
                            tryCount++;
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        if (mDetailList.size() == mList.size()) {
                            for (int i = 0; i < mList.size(); i++) {
                                try {
                                    MusicInfo musicInfo = new MusicInfo();
                                    musicInfo.setAudioId(Integer.parseInt(mList.get(i).getSong_id()));
                                    musicInfo.setMusicName(mList.get(i).getTitle());
                                    musicInfo.setArtist(mDetailList.get(i).getArtist_name());
                                    musicInfo.setArtistId(Integer.parseInt(mDetailList.get(i).getArtist_id()));
                                    musicInfo.setAlbumName(mList.get(i).getAlbum_title());
                                    musicInfo.setAlbumId(Integer.parseInt(mList.get(i).getAlbum_id()));
                                    musicInfo.setLrc(mDetailList.get(i).getLrclink());
                                    musicInfo.setAlbumData(mDetailList.get(i).getPic_radio());
                                    musicInfo.setIslocal(false);
                                    adapterList.add(musicInfo);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        subscriber.onNext(adapterList);
                        subscriber.onCompleted();
                        //MusicAdapter adapter = new MusicAdapter(((SongCollectionActivity) view), adapterList, true);
                        //view.setAdapter(adapter);
                    }
                };


                Observable observable2 =  model.getSongCollectionInfo(listid);

                ((SongCollectionActivity)view).addSubscription(
                        observable2.subscribeOn(Schedulers.io())
                                .unsubscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe(subscriber2));

            }
        });


        ((SongCollectionActivity)view).addSubscription(
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));






    }


    class getSongBaseInfo implements Runnable {

        private String song_id;
        private int position;
        private HashMap<Integer,SongBaseInfo.ResultBean.ItemsBean> map;

        public getSongBaseInfo(String song_id, int position, HashMap<Integer,SongBaseInfo.ResultBean.ItemsBean> map) {
            this.song_id = song_id;
            this.position = position;
            this.map = map;
        }

        @Override
        public void run() {
            Subscriber<SongBaseInfo> subscriber = new Subscriber<SongBaseInfo>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Logger.e("Error", e);
                }

                @Override
                public void onNext(SongBaseInfo songBaseInfo) {
                    map.put(position, songBaseInfo.getResult().getItems().get(0));
                }
            };

            Observable observable = HttpMethods.getInstance().songBaseInfo(song_id);


            ((SongCollectionActivity) view).addSubscription(
                    observable.subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(subscriber));


        }
    }

}
