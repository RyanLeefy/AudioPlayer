package com.example.administrator.audioplayer.bean;



/**
 * Created by lipuyusx on 2017/1/23.
 */

public class MusicFragmengSongCollectionItem {

    private long id;
    private String name;    //歌单名字
    private int songCount;  //歌的数量

    //private String albumArt;
    //private String author;


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

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }


}
