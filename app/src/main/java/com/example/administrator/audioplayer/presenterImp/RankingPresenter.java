package com.example.administrator.audioplayer.presenterImp;

import com.example.administrator.audioplayer.Imodel.IRankingModel;
import com.example.administrator.audioplayer.Ipresenter.IRankingPresenter;
import com.example.administrator.audioplayer.Iview.IRankingView;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.adapter.RankingAdapter;
import com.example.administrator.audioplayer.bean.BillBoardItem;
import com.example.administrator.audioplayer.fragment.RankingFragment;
import com.example.administrator.audioplayer.jsonbean.BillBoard;
import com.example.administrator.audioplayer.modelImp.RankingModel;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by on 2017/2/25 0025.
 */

public class RankingPresenter implements IRankingPresenter {

    private IRankingView view;
    private IRankingModel model;

    private List<BillBoardItem> mList = new ArrayList<>();

    //请求完一个榜单就+1，判断是否全部请求玩
    private int times = 0;

    //热歌榜
    public static int BILLBOARD_HOT_MUSIC = 2;
    //新歌榜
    public static int BILLBOARD_NEW_MUSIC = 1;
    //原创榜
    public static int BILLBOARD_ORIGINAL = 200;


    public RankingPresenter(IRankingView view) {
        this.view = view;
        model = new RankingModel();
    }


    @Override
    public void onCreateView() {
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

        //获取音乐榜单数据并显示,热歌榜，新歌榜，原创榜

        Observable.create(new Observable.OnSubscribe<List>() {
            @Override
            public void call(Subscriber<? super List> subscriber) {
                getBillBoard(BILLBOARD_HOT_MUSIC);
                //等待第一个请求结束
                int tryCount = 0;
                while(times != 1 && tryCount < 1000) {
                    tryCount++;
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                getBillBoard(BILLBOARD_NEW_MUSIC);
                //等待第二个请求结束
                tryCount = 0;
                while(times != 2 && tryCount < 1000) {
                    tryCount++;
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                getBillBoard(BILLBOARD_ORIGINAL);
                //等待第三个请求结束
                tryCount = 0;
                while(times != 3 && tryCount < 1000) {
                    tryCount++;
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                subscriber.onNext(mList);
                subscriber.onCompleted();
            }})
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                    }

                    @Override
                    public void onNext(List list) {
                        //设置adapter，刷新界面
                        RankingAdapter adapter = new RankingAdapter(((RankingFragment) view).getActivity(), mList);
                        view.setAdapter(adapter);
                    }
                });


    }



    public void getBillBoard(int type) {
        Subscriber<BillBoard> subscriber = new Subscriber<BillBoard>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e.toString());
            }

            @Override
            public void onNext(BillBoard billBoard) {
                Logger.d(billBoard.getSong_list().size());
                for(int i = 0; i < billBoard.getSong_list().size(); i++) {
                    BillBoardItem item = new BillBoardItem();

                    item.setType(Integer.parseInt(billBoard.getBillboard().getBillboard_type()));
                    item.setName(billBoard.getBillboard().getName());
                    if(billBoard.getBillboard().getUpdate_date() != null && billBoard.getBillboard().getUpdate_date().length() != 0) {
                        //有更新时间获取更新时间
                        item.setUpdate_time(billBoard.getBillboard().getUpdate_date());
                    } else {
                        //没有更新时间获取当前时间
                        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                        String date = formatter.format(curDate);
                        item.setUpdate_time(date);
                    }
                    item.setTitle(billBoard.getSong_list().get(i).getTitle());
                    item.setAuthor(billBoard.getSong_list().get(i).getAuthor());
                    mList.add(item);
                }
                times++;
            }
        };

        //偏移0，获取前3个
        Observable observable = model.getBillBoard(type, 0, 3);


        ((RankingFragment) view).addSubscription(
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));
    }

}
