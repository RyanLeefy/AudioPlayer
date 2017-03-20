package com.example.administrator.audioplayer.Ipresenter;


/**
 * LocalSearchFragment
 * Created on 2017/2/4.
 */

public interface ILocalSearchPresenter {
    void performSearch(String queryString);
    void peformMusicClick(int position);
    boolean performMusicDelete(int position);
}
