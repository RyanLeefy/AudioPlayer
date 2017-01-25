package com.example.administrator.audioplayer.bean;

/**
 * Created on 2017/1/22.
 */

public class LeftMenuItem {
    private int mIcon;
    private String mName;

    public LeftMenuItem(int icon, String name) {
        this.mIcon = icon;
        this.mName = name;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmIcon() {
        return mIcon;
    }

    public void setmIcon(int mIcon) {
        this.mIcon = mIcon;
    }
}
