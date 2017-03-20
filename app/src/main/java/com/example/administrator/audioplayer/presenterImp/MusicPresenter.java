package com.example.administrator.audioplayer.presenterImp;

import com.example.administrator.audioplayer.Ipresenter.IMusicPresenter;
import com.example.administrator.audioplayer.Iview.IMusicView;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.SongListAdapter;
import com.example.administrator.audioplayer.bean.MusicFragmengSongCollectionItem;
import com.example.administrator.audioplayer.bean.MusicFragmentExpandItem;
import com.example.administrator.audioplayer.bean.MusicFragmentHeaderItem;
import com.example.administrator.audioplayer.db.RecentMusicDB;
import com.example.administrator.audioplayer.fragment.MusicFragment;
import com.example.administrator.audioplayer.modelImp.LocalMusicModel;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by on 2017/2/14 0014.
 */

public class MusicPresenter implements IMusicPresenter {

    private IMusicView view;


    private List allItems;


    public MusicPresenter(IMusicView view) {
        this.view = view;
    }


    @Override
    public void onCreateView() {
        //用Rxjava进行异步处理
        //第一步创建Subscriber
        //第二步创建Observable
        //第三步订阅，并添加到父类的CompositeSubscription中，进行管理
        Subscriber<Map<String, Integer>>  subscriber = new Subscriber<Map<String, Integer>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e,"error");
            }

            @Override
            public void onNext(Map<String, Integer> map) {
                //主线程中调用
                //从map中获取各item数量，然后创建对象，放入list中，用list创建adapter，调用view的setAdapter
                //设置adapter，刷新界面
                //Logger.d(list.size());
                //mList = list;

                int num_local_music = map.get("num_local_music") == null ? 0 : map.get("num_local_music");
                int num_recent_music = map.get("num_recent_music") == null ? 0 : map.get("num_recent_music");

                List<MusicFragmentHeaderItem> headerItemList = new ArrayList<>(
                        Arrays.asList(new MusicFragmentHeaderItem(R.drawable.music_icn_local, "本地播放", num_local_music),
                                new MusicFragmentHeaderItem(R.drawable.music_icn_recent, "最近播放", num_recent_music),
                                new MusicFragmentHeaderItem(R.drawable.music_icn_download, "下载管理", 0)));
                                //new MusicFragmentHeaderItem(R.drawable.music_icn_artist, "我的歌手", 0)));


                final List<MusicFragmengSongCollectionItem> create_songCollectionItems = new ArrayList<>();
                create_songCollectionItems.add(new MusicFragmengSongCollectionItem(R.drawable.cover_faveriate_songcollection, "我喜欢的音乐", 0));
                create_songCollectionItems.add(new MusicFragmengSongCollectionItem(R.drawable.cover_faveriate_songcollection, "粤语歌单", 0));

                final List<MusicFragmengSongCollectionItem> collect_songCollectionItems = new ArrayList<>();
                collect_songCollectionItems.add(new MusicFragmengSongCollectionItem(R.drawable.cover_faveriate_songcollection, "我收藏的音乐", 0));
                collect_songCollectionItems.add(new MusicFragmengSongCollectionItem(R.drawable.cover_faveriate_songcollection, "英文歌", 0));

                final List<MusicFragmentExpandItem> expandItemList = new ArrayList<>(
                        Arrays.asList(new MusicFragmentExpandItem(MusicFragmentExpandItem.TYPE_CREATE, "创建的歌单", create_songCollectionItems.size()),
                                new MusicFragmentExpandItem(MusicFragmentExpandItem.TYPE_COLLECT, "收藏的歌单", collect_songCollectionItems.size())));

                allItems = new ArrayList();
                allItems.addAll(headerItemList);
                allItems.add(expandItemList.get(0));
                allItems.addAll(create_songCollectionItems);
                allItems.add(expandItemList.get(1));
                allItems.addAll(collect_songCollectionItems);

                SongListAdapter adapter = new SongListAdapter(((MusicFragment)view).getActivity(), allItems);
                view.setAdapter(adapter, allItems, create_songCollectionItems, collect_songCollectionItems, expandItemList);
            }
        };


        Observable observable = Observable.create(new Observable.OnSubscribe<Map>() {
            @Override
            public void call(Subscriber<? super Map> subscriber) {
                //子线程中调用
                //这里调用model层获取load各个item的数量，然后放到map里面，传入到onNext()里
                Map<String, Integer> map = new HashMap<String, Integer>();

                //本地音乐数量
                map.put("num_local_music", new LocalMusicModel().getLocalMusic().size());
                //最近播放数量
                map.put("num_recent_music", RecentMusicDB.getInstance(MyApplication.getContext()).getRecentCursor().getCount());
                //下载管理数量
                //xxx
                //我的歌手数量
                //xxxx

                subscriber.onNext(map);
                subscriber.onCompleted();
            }
        });

        ((MusicFragment)view).addSubscription(
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));
    }
}
