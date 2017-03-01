package com.example.administrator.audioplayer.Iview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/2/28 0028.
 */

public interface IAfterSearchView {
    void setViewPager(ArrayList musicList, ArrayList albumList);
    //新的内容
    void updateMusicList(List musicList);
    //新的内容
    void updateAlbumList(List albumList);
    void showTryAgain();
}
