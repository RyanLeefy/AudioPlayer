package com.example.administrator.audioplayer.Ipresenter;

/**
 * LocalMusicFragment
 * Created by on 2017/2/3.
 */

public interface ILocalMusicPresenter {
    void onCreateView();
    void peformMusicClick(int position);
    boolean performMusicDelete(int position);
}
