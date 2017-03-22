package com.example.administrator.audioplayer.modelImp;

import com.example.administrator.audioplayer.Imodel.INewAlbumModel;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.db.SongCollectionDB;
import com.example.administrator.audioplayer.http.HttpMethods;
import com.example.administrator.audioplayer.jsonbean.Album;

import rx.Observable;

/**
 * Created by on 2017/2/23 0023.
 */

public class NewAlbumModel implements INewAlbumModel {
    @Override
    public Observable<Album> getNewAlbumInfo(String albumid) {
        return HttpMethods.getInstance().albumInfo(albumid);
    }

    @Override
    public boolean peformCollect(String albumid, String name, int count, String albumart, String author, String publish_time) {
        //判断是否已收藏，未收藏的返回-1，已收藏的返回其id
        int id = SongCollectionDB.getInstance(MyApplication.getContext()).isCollected(albumid);
        if(id != -1){
            //已经收藏,取消收藏，从数据库删除
            SongCollectionDB.getInstance(MyApplication.getContext()).deleteSongCollection(id);
            return false;
        } else {
            //没有收藏，添加到数据库，类型为3（网络新专辑）
            SongCollectionDB.getInstance(MyApplication.getContext()).addNewAlbum(3, Long.parseLong(albumid), name, count, albumart, author , publish_time);
            return true;
        }
    }
}
