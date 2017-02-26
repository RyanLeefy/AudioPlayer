package com.example.administrator.audioplayer.bean;

/**
 * Created by on 2017/2/25 0025.
 */

/**
 * 音乐榜单的缩略图信息
 */
public class BillBoardItem {

    private int type;
    private String name;
    private String update_time;
    private String title;
    private String author;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
