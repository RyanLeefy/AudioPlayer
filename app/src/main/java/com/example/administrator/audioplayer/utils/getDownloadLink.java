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
        int audioid = ((MusicInfo) list.get(i)).getAudioId();
        String url;
        JsonArray jsonArray = HttpUtils.getResposeJsonObject(HttpMethods.getInstance().songInfoSyn(String.valueOf(audioid)), MyApplication.getContext(), false)
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



