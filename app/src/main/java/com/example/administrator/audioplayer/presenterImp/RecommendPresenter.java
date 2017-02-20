package com.example.administrator.audioplayer.presenterImp;

import com.example.administrator.audioplayer.Imodel.IRecommendModel;
import com.example.administrator.audioplayer.Ipresenter.IRecommendPresenter;
import com.example.administrator.audioplayer.Iview.IRecommendView;
import com.example.administrator.audioplayer.adapter.RecommendNewAlbumAdapter;
import com.example.administrator.audioplayer.adapter.RecommendSongCollectionAdapter;
import com.example.administrator.audioplayer.bean.RecommendNewAlbumItem;
import com.example.administrator.audioplayer.bean.RecommendSongCollectionItem;
import com.example.administrator.audioplayer.fragment.RecommendFragment;
import com.example.administrator.audioplayer.jsonbean.RecommendNewAlbum;
import com.example.administrator.audioplayer.jsonbean.RecommendSongCollection;
import com.example.administrator.audioplayer.modelImp.RecommendModel;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by on 2017/2/17 0017.
 */

public class RecommendPresenter implements IRecommendPresenter{

    private IRecommendView view;
    private IRecommendModel model;

    private List mSongCollectionList, mNewAlbumList;

    public RecommendPresenter(IRecommendView view) {
        this.view = view;
        model = new RecommendModel();
    }

    @Override
    public void onCreateView() {
        //用Rxjava进行异步处理
        //第一步创建Subscriber
        //第二步创建Observable
        //第三步订阅，并添加到父类的CompositeSubscription中，进行管理

        //获取热门歌单数据并显示
        Subscriber<RecommendSongCollection> subscriber = new Subscriber<RecommendSongCollection>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e("Error", e);
            }

            @Override
            public void onNext(RecommendSongCollection recommendSongCollection) {
                //设置adapter，刷新界面
                Logger.d(recommendSongCollection.getContent().getList().size());
                mSongCollectionList = recommendSongCollection.getContent().getList();
                RecommendSongCollectionAdapter adapter = new RecommendSongCollectionAdapter(((RecommendFragment) view).getActivity(), mSongCollectionList);
                view.setRecommendSongCollectionAdapter(adapter);
            }
        };

        Observable observable = model.getRecommendSongCollectionList();


        ((RecommendFragment) view).addSubscription(
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));



        //获取新专辑上架数据并显示
        Subscriber<RecommendNewAlbum> subscriber2 = new Subscriber<RecommendNewAlbum>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e("Error", e);
            }

            @Override
            public void onNext(RecommendNewAlbum recommendNewAlbum) {
                //设置adapter，刷新界面
                Logger.d(recommendNewAlbum.getPlaze_album_list().getRM().getAlbum_list().getList());
                mNewAlbumList = recommendNewAlbum.getPlaze_album_list().getRM().getAlbum_list().getList();
                RecommendNewAlbumAdapter adapter = new RecommendNewAlbumAdapter(((RecommendFragment) view).getActivity(), mNewAlbumList);
                view.setRecommendNewAlbumAdapter(adapter);
            }
        };


        Observable observable2 = model.getRecommendNewAlbumList();

        ((RecommendFragment) view).addSubscription(
                observable2.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber2));

    }
}
