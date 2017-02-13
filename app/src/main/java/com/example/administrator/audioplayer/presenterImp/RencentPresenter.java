package com.example.administrator.audioplayer.presenterImp;

import android.util.Log;
import android.widget.Toast;

import com.example.administrator.audioplayer.Imodel.IRecentModel;
import com.example.administrator.audioplayer.Ipresenter.IRecentPresenter;
import com.example.administrator.audioplayer.Iview.IRecentView;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.activity.RecentActivity;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.fragment.LocalMusicFragment;
import com.example.administrator.audioplayer.modelImp.RecentModel;
import com.example.administrator.audioplayer.service.MusicPlayer;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by on 2017/2/3.
 */

public class RencentPresenter implements IRecentPresenter {

    private IRecentView view;
    private IRecentModel model;

    private List<MusicInfo> mList;


    public RencentPresenter(IRecentView view) {
        this.view = view;
        model = new RecentModel();
    }

    @Override
    public void onCreateView() {
        //用Rxjava进行异步处理
        //第一步创建Subscriber
        //第二步创建Observable
        //第三步订阅，并添加到父类的CompositeSubscription中，进行管理
        Logger.d("1111");
        Subscriber<List<MusicInfo>> subscriber = new Subscriber<List<MusicInfo>>() {
            @Override
            public void onCompleted() {
                Logger.d("onCompleted");
                Toast.makeText(MyApplication.getContext(),"onCompleted",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Logger.d("Error");
                Toast.makeText(MyApplication.getContext(),"error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(List list) {
                //设置adapter，刷新界面
                Logger.d("onNext");
                Toast.makeText(MyApplication.getContext(),"onNext",Toast.LENGTH_SHORT).show();
                Logger.d("111");
                Logger.d("111" + list.size());
                mList = list;
                MusicAdapter adapter = new MusicAdapter(((RecentActivity)view), list, true);
                view.setAdapter(adapter);
            }
        };


        Observable observable = Observable.create(new Observable.OnSubscribe<List>() {
            @Override
            public void call(Subscriber<? super List> subscriber) {
                //调用model获取数据
                subscriber.onNext(model.getRecentMusic());
                subscriber.onCompleted();
            }
        });

        /*
        ((RecentActivity)view).addSubscription(
                observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
                */
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    @Override
    public void pefromMusicClick(int position) {
        //position应该大于等于1
        if(position == 0) {
            //点击播放全部按钮
        } else {
            //position - 1 对应歌单中的位置
            MusicPlayer.playAll(mList, position - 1, false);
        }
    }


}
