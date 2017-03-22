package com.example.administrator.audioplayer.Ipresenter;

/**
 * MusicFragment
 * Created by on 2017/2/14 0014.
 */

public interface IMusicPresenter {
    void onCreateView();
    boolean createNewCollection(String name);
    boolean updateCollection(int id, String newName);
    void deleteCollection(int id);
}
