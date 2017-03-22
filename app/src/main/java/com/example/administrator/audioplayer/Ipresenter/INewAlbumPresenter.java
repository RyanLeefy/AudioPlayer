package com.example.administrator.audioplayer.Ipresenter;

/**
 * Created by on 2017/2/23 0023.
 */

public interface INewAlbumPresenter {
    void onCreate(String albumid);
    void performDownLoadAllClick();
    void performDownLoadMusicClick(int position);
    void peformMusicClick(int position);
    boolean peformCollect(String albumid, String name,
                          int count, String albumart, String author,
                          String publish_time);
}
