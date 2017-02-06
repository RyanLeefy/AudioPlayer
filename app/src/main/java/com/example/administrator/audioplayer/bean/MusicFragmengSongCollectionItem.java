package com.example.administrator.audioplayer.bean;



/**
 * Created by on 2017/1/23.
 */

public class MusicFragmengSongCollectionItem {

    private long id;   //暂无用，以后用于区分
    private String cover_uri;      //封面图片Uri,fresco设置图片要用URI
    private String name;    //歌单名字
    private int songCount;  //歌的数量

    //private String albumArt;
    //private String author;

    public MusicFragmengSongCollectionItem(int cover, String name, int songCount) {
        this.cover_uri = "res:/" + cover;
        this.name = name;
        this.songCount = songCount;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCover_uri() {
        return cover_uri;
    }

    public void setCover_uri(int cover) {
        this.cover_uri = "res:/" + cover;
    }

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }


}
