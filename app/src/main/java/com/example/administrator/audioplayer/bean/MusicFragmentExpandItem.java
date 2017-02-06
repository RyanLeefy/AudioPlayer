package com.example.administrator.audioplayer.bean;

/**
 * Created by on 2017/1/23.
 */

public class MusicFragmentExpandItem {

    public static final int TYPE_CREATE = 1;   //创建的歌单
    public static final int TYPE_COLLECT = 2;  //收藏的歌单

    private int type;  //类型
    private String title;
    private int songCollectionCount; //歌单数量

    public MusicFragmentExpandItem(int type, String title,int songCollectionCount) {
        this.type = type;
        this.title = title;
        this.songCollectionCount = songCollectionCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSongCollectionCount() {
        return songCollectionCount;
    }

    public void setSongCollectionCount(int songCollectionCount) {
        this.songCollectionCount = songCollectionCount;
    }

}
