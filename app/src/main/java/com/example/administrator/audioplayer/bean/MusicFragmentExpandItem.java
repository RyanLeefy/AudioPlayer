package com.example.administrator.audioplayer.bean;

/**
 * Created by lipuyusx on 2017/1/23.
 */

public class MusicFragmentExpandItem {

    public static final int TYPE_CREATE = 1;   //创建的歌单
    public static final int TYPE_COLLECT = 2;  //收藏的歌单

    private int type;  //类型

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
