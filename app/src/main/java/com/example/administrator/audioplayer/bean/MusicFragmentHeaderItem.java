package com.example.administrator.audioplayer.bean;

/**
 * Created by lipuyusx on 2017/1/23.
 */

public class MusicFragmentHeaderItem {

    private int icon;  //iconID
    private String title;   //标题
    private int count;    //数量

    public MusicFragmentHeaderItem(int icon, String title, int count) {
        this.icon = icon;
        this.title = title;
        this.count = count;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
