package com.example.administrator.audioplayer.utils;

import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.http.HttpMethods;
import com.example.administrator.audioplayer.http.HttpUtils;
import com.example.administrator.audioplayer.jsonbean.SongExtraInfo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by on 2017/3/18 0018.
 */

public class GetDownloadLink implements Runnable {

    //adapter的list
    private List list;
    //位置
    private int i;
    //保存结果的数据结构
    private String[] names;
    private String[] artists;
    private ArrayList<String> urls;

    //类型，0为全部，1位单首歌曲
    private int type;

    public GetDownloadLink(List list, int i, String[] names, String[] artists, ArrayList<String> urls, int type) {
        this.list = list;
        this.i = i;
        this.names = names;
        this.artists = artists;
        this.urls = urls;
        this.type = type;
    }


    @Override
    public void run() {
        final int audioid = ((MusicInfo) list.get(i)).getAudioId();
        String url;


        //Observable<SongExtraInfo> observable = HttpMethods.getInstance().songInfo(String.valueOf(audioid));
        //observable.

                //获取热门歌单数据并显示
        /*
                Subscriber<SongExtraInfo> subscriber = new Subscriber<SongExtraInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e.toString());
            }

            @Override
            public void onNext(SongExtraInfo songExtraInfo) {
                PrintLog.e("SongExtraInfo", String.valueOf(songExtraInfo == null));
                PrintLog.e("SongExtraInfo", String.valueOf(songExtraInfo.getError_code()));
                int downloadBit = PreferencesUtils.getInstance(MyApplication.getContext()).getDownMusicBit();

                PrintLog.e("DownloadLink", "downloadBit:" + downloadBit);
                //从后往前循环获取，获取与下载配置最接近的数据
                int len = songExtraInfo.getSongurl().getUrl().size();
                SongExtraInfo.SongurlBean.UrlBean urlBean = null;
                for (int i = len - 1; i > -1; i--) {
                    int bit = songExtraInfo.getSongurl().getUrl().get(i).getFile_bitrate();
                    PrintLog.e("bit:" + bit);
                    if (bit == downloadBit) {
                        urlBean = songExtraInfo.getSongurl().getUrl().get(i);
                        break;
                    } else if (bit < downloadBit && bit >= 64) {
                        urlBean = songExtraInfo.getSongurl().getUrl().get(i);
                        break;
                    }
                }

                //url = ;
                PreferencesUtils.getInstance(MyApplication.getContext()).setPlayLink(audioid, urlBean.getShow_link());

                urls.add(urlBean.getShow_link());
                if (type == 1) {
                    //下载单首歌曲
                    names[0] = ((MusicInfo) list.get(i)).getMusicName();
                    artists[0] = ((MusicInfo) list.get(i)).getArtist();
                } else {
                    //下载全部歌曲
                    names[i] = ((MusicInfo) list.get(i)).getMusicName();
                    artists[i] = ((MusicInfo) list.get(i)).getArtist();
                }
            }
        };


        Observable observable = HttpMethods.getInstance().songInfo(String.valueOf(audioid));


                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber);
*/

        JsonObject object = HttpUtils.getResposeJsonObject(HttpMethods.getInstance().songInfoSyn(String.valueOf(audioid)), MyApplication.getContext(), false);

        PrintLog.e("error:code", String.valueOf(object == null));
        PrintLog.e("error:code", String.valueOf(object.get("error_code")));

        JsonArray jsonArray = object
                .get("songurl")
                .getAsJsonObject().get("url").getAsJsonArray();
        Gson gson = new Gson();

        //获取下载配置
        int downloadBit = PreferencesUtils.getInstance(MyApplication.getContext()).getDownMusicBit();

        PrintLog.e("DownloadLink", "downloadBit:" + downloadBit);
        //从后往前循环获取，获取与下载配置最接近的数据
        int len = jsonArray.size();
        SongExtraInfo.SongurlBean.UrlBean urlBean = null;
        for (int i = len - 1; i > -1; i--) {
            int bit = Integer.parseInt(jsonArray.get(i).getAsJsonObject().get("file_bitrate").toString());
            PrintLog.e("bit:" + bit);
            if (bit == downloadBit) {
                urlBean = gson.fromJson(jsonArray.get(i), SongExtraInfo.SongurlBean.UrlBean.class);
                break;
            } else if (bit < downloadBit && bit >= 64) {
                urlBean = gson.fromJson(jsonArray.get(i), SongExtraInfo.SongurlBean.UrlBean.class);
                break;
            }
        }

        //获取后保存本地
        url = urlBean.getShow_link();

        PreferencesUtils.getInstance(MyApplication.getContext()).setPlayLink(audioid, urlBean.getShow_link());

        urls.add(url);
        if (type == 1) {
            //下载单首歌曲
            names[0] = ((MusicInfo) list.get(i)).getMusicName();
            artists[0] = ((MusicInfo) list.get(i)).getArtist();
        } else {
            //下载全部歌曲
            names[i] = ((MusicInfo) list.get(i)).getMusicName();
            artists[i] = ((MusicInfo) list.get(i)).getArtist();
        }

    }
}



