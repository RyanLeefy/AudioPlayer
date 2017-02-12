package com.example.administrator.audioplayer.Ipresenter;

/**
 * Created by on 2017/2/11 0011.
 */

public interface IPlayQueuePresenter {
    void onCreateView();
    void peformMusicClick(int position);
    void peformDeleteClick(int position);
}
