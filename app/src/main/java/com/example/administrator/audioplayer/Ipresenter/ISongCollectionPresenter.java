package com.example.administrator.audioplayer.Ipresenter;

/**
 * Created by on 2017/2/20 0020.
 */

public interface ISongCollectionPresenter {
    void onCreate(String listid);
    void performDownLoadAllClick();
    void performDownLoadMusicClick(int position);
    void peformMusicClick(int position);
}
