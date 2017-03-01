package com.example.administrator.audioplayer.presenterImp;

import com.example.administrator.audioplayer.Imodel.IAfterSearchModel;
import com.example.administrator.audioplayer.Ipresenter.IAfterSearchPresenter;
import com.example.administrator.audioplayer.Iview.IAfterSearchView;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.fragment.AfterSearchFragment;
import com.example.administrator.audioplayer.jsonbean.SearchMeageResult;
import com.example.administrator.audioplayer.modelImp.AfterSearchModel;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by on 2017/2/28 0028.
 */

public class AfterSearchPresenter implements IAfterSearchPresenter {

    private IAfterSearchView view;
    private IAfterSearchModel model;

    //从网络获取回来的音乐队列数据
    private ArrayList<SearchMeageResult.ResultBean.SongInfoBean.SongListBean> mMusicList;
    //根据上述数据组装成的MusicInfo队列数据
    private ArrayList<MusicInfo> resultList = new ArrayList<>();

    //从网络获取回来的专辑队列数据
    private ArrayList mAlbumList = new ArrayList();


    public AfterSearchPresenter(IAfterSearchView view) {
        this.view = view;
        model = new AfterSearchModel();
    }

    @Override
    public void onCreateView(String query) {
        showSearchMeageResult(query, 1);
    }

    @Override
    public void searchMoreMusic(String query,int page) {
        //如果上不了网，则直接显示网络无法连接，点击重连
        if(!CommonUtils.isConnectInternet(MyApplication.getContext())) {
            view.showTryAgain();
            return;
        }

        //获取搜索结果并显示
        Subscriber<SearchMeageResult> subscriber = new Subscriber<SearchMeageResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e.toString());
                //没有一些数据的时候，直接用现有的数据来进行刷新，没有数据的直接传入空的
                view.updateMusicList(mMusicList);
            }

            @Override
            public void onNext(SearchMeageResult searchMeageResult) {
                //设置adapter，刷新界面

                if(searchMeageResult.getResult().getSong_info() != null) {
                    Logger.d(searchMeageResult.getResult().getSong_info().getSong_list().size());
                    mMusicList = (ArrayList) searchMeageResult.getResult().getSong_info().getSong_list();
                    //把加载更多的数据装入队列中，进行update
                    ArrayList<MusicInfo> moreMusicList = new ArrayList<>();
                    for (int i = 0; i < mMusicList.size(); i++) {
                        MusicInfo musicInfo = new MusicInfo();
                        musicInfo.setAudioId(Integer.valueOf(mMusicList.get(i).getSong_id()));
                        musicInfo.setMusicName(mMusicList.get(i).getTitle());
                        musicInfo.setArtist(mMusicList.get(i).getAuthor());
                        musicInfo.setArtistId(Integer.parseInt(mMusicList.get(i).getArtist_id()));
                        musicInfo.setAlbumName(mMusicList.get(i).getAlbum_title());
                        musicInfo.setAlbumId(Integer.parseInt(mMusicList.get(i).getAlbum_id()));
                        musicInfo.setLrc(mMusicList.get(i).getLrclink());
                        musicInfo.setAlbumData(mMusicList.get(i).getPic_small());
                        musicInfo.setIslocal(false);
                        moreMusicList.add(musicInfo);
                    }

                    view.updateMusicList(moreMusicList);
                }
            }
        };

        //每页显示10个
        Observable observable = model.getSearchMeageResult(query, String.valueOf(page), String.valueOf(10));


        ((AfterSearchFragment) view).addSubscription(
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));
    }

    @Override
    public void searchMoreAlbum(String query, int page) {
        //如果上不了网，则直接显示网络无法连接，点击重连
        if(!CommonUtils.isConnectInternet(MyApplication.getContext())) {
            view.showTryAgain();
            return;
        }

        //获取搜索结果并显示
        Subscriber<SearchMeageResult> subscriber = new Subscriber<SearchMeageResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e.toString());
                //没有一些数据的时候，直接用现有的数据来进行刷新，没有数据的直接传入空的
                view.updateAlbumList(mAlbumList);
            }

            @Override
            public void onNext(SearchMeageResult searchMeageResult) {
                //设置adapter，刷新界面
                if(searchMeageResult.getResult().getAlbum_info() != null) {
                    Logger.d(searchMeageResult.getResult().getAlbum_info().getAlbum_list().size());
                    mAlbumList = (ArrayList) searchMeageResult.getResult().getAlbum_info().getAlbum_list();
                    view.updateAlbumList(mAlbumList);
                }
            }
        };

        //每页显示10个
        Observable observable = model.getSearchMeageResult(query, String.valueOf(page), String.valueOf(10));


        ((AfterSearchFragment) view).addSubscription(
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));
    }


    /**
     * 网络获取歌单，传入页码
     * @param pageNo
     */
    public void showSearchMeageResult(String query, final int pageNo) {

        //如果上不了网，则直接显示网络无法连接，点击重连
        if(!CommonUtils.isConnectInternet(MyApplication.getContext())) {
            view.showTryAgain();
            return;
        }

        //获取热门歌单数据并显示
        Subscriber<SearchMeageResult> subscriber = new Subscriber<SearchMeageResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e.toString());
                //没有一些数据的时候，直接用现有的数据来进行刷新，没有数据的直接传入空的
                view.setViewPager(resultList, mAlbumList);
            }

            @Override
            public void onNext(SearchMeageResult searchMeageResult) {
                //设置adapter，刷新界面
                Logger.d(searchMeageResult.getError_code());

                //如果搜索到音乐信息
                if(searchMeageResult.getResult().getSong_info() != null) {
                    Logger.d(searchMeageResult.getResult().getSong_info().getSong_list().size());
                    mMusicList = (ArrayList) searchMeageResult.getResult().getSong_info().getSong_list();
                    for(int i = 0; i < mMusicList.size(); i++) {
                        MusicInfo musicInfo = new MusicInfo();
                        musicInfo.setAudioId(Integer.valueOf(mMusicList.get(i).getSong_id()));
                        musicInfo.setMusicName(mMusicList.get(i).getTitle());
                        musicInfo.setArtist(mMusicList.get(i).getAuthor());
                        musicInfo.setArtistId(Integer.parseInt(mMusicList.get(i).getArtist_id()));
                        musicInfo.setAlbumName(mMusicList.get(i).getAlbum_title());
                        musicInfo.setAlbumId(Integer.parseInt(mMusicList.get(i).getAlbum_id()));
                        musicInfo.setLrc(mMusicList.get(i).getLrclink());
                        musicInfo.setAlbumData(mMusicList.get(i).getPic_small());
                        musicInfo.setIslocal(false);
                        resultList.add(musicInfo);
                    }
                }

                //如果搜索到专辑信息
                if(searchMeageResult.getResult().getAlbum_info() != null) {
                    Logger.d(searchMeageResult.getResult().getAlbum_info().getAlbum_list().size());
                    mAlbumList = (ArrayList) searchMeageResult.getResult().getAlbum_info().getAlbum_list();
                }
                view.setViewPager(resultList, mAlbumList);
            }
        };

        //每页显示6个
        Observable observable = model.getSearchMeageResult(query, String.valueOf(pageNo), String.valueOf(10));


        ((AfterSearchFragment) view).addSubscription(
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));
    }
}
