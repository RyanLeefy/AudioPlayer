package com.example.administrator.audioplayer.Imodel;

import com.example.administrator.audioplayer.bean.CollectionInfo;

import java.util.List;

/**
 * Created by on 2017/2/14 0014.
 */

public interface IMusicModel {
    //获取创建的歌单
    List getCreateSongCollection();
    //获取收藏的歌单
    List getNetSongCollection();

    //添加创建的歌单
    void addCreateSongCollection(CollectionInfo collectionInfo);
    //删除创建的歌单
    void deleteCreateSongCollection(int id);
    //修改创建的歌单
    void updateCreateSongCollection(int id);


    //删除网络歌单或专辑
    void deleteNetSongCollection(int id);
    //修改创建的歌单
    void updateNetSongCollection(int id);

}
