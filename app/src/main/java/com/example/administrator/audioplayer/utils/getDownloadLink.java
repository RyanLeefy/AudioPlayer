package com.example.administrator.audioplayer.utils;

import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.http.HttpMethods;
import com.example.administrator.audioplayer.http.HttpUtils;
import com.example.administrator.audioplayer.jsonbean.SongExtraInfo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

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


    public GetDownloadLink(List list, int i, String[] names, String[] artists, ArrayList<String> urls) {
        this.list = list;
        this.i = i;
        this.names = names;
        this.artists = artists;
        this.urls = urls;
    }


    @Override
    public void run() {
        //从PreferencesUtils获取该音乐的地址
        int audioid = ((MusicInfo)list.get(i)).getAudioId();
        String url = PreferencesUtils.getInstance(MyApplication.getContext()).getPlayLink(audioid);
        //如果先前没有保存地址，则从网络上获取
        if(url == null) {
            JsonArray jsonArray = HttpUtils.getResposeJsonObject(HttpMethods.getInstance().songInfoSyn(String.valueOf(audioid)), MyApplication.getContext(), false)
                    .get("songurl")
                    .getAsJsonObject().get("url").getAsJsonArray();
            Gson gson = new Gson();
            SongExtraInfo.SongurlBean.UrlBean urlBean = gson.fromJson(jsonArray.get(0), SongExtraInfo.SongurlBean.UrlBean.class);

            //获取后保存本地
            url = urlBean.getShow_link();

            PreferencesUtils.getInstance(MyApplication.getContext()).setPlayLink(audioid, urlBean.getShow_link());
        }
        urls.add(url);
        PrintLog.e("getShowLink:" + url);
        names[0] = ((MusicInfo)list.get(i)).getMusicName();
        artists[0] = ((MusicInfo)list.get(i)).getArtist();
    }
}



