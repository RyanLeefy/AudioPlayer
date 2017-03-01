package com.example.administrator.audioplayer.Ipresenter;

/**
 * Created by on 2017/2/28 0028.
 */

public interface IAfterSearchPresenter {
    void onCreateView(String query);
    void searchMoreMusic(String query, int page);
    void searchMoreAlbum(String query, int page);
}
