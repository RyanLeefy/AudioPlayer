package com.example.administrator.audioplayer.modelImp;

import com.example.administrator.audioplayer.Imodel.ISongCollectionModel;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.db.SongCollectionDB;
import com.example.administrator.audioplayer.http.HttpMethods;
import com.example.administrator.audioplayer.jsonbean.SongCollectionInfo;

import rx.Observable;

/**
 * Created by on 2017/2/20 0020.
 */

public class SongCollectionModel implements ISongCollectionModel {
    @Override
    public Observable<SongCollectionInfo> getSongCollectionInfo(String listid) {
        return HttpMethods.getInstance().geDanInfo(listid);
    }

    /**
     * 成功收藏返回true，取消收藏返回false
     * @param title
     * @param SongCount
     * @param pic
     * @param songListId
     * @param tag
     * @param songCollectionCount
     * @return
     */
    @Override
    public boolean collect(String title, int SongCount, String pic, String songListId, String tag, String songCollectionCount) {
        //判断是否已收藏，未收藏的返回-1，已收藏的返回其id
        int id = SongCollectionDB.getInstance(MyApplication.getContext()).isCollected(songListId);
        if(id != -1){
            //已经收藏,取消收藏，从数据库删除
            SongCollectionDB.getInstance(MyApplication.getContext()).deleteSongCollection(id);
            return false;
        } else {
            //没有收藏，添加到数据库，类型为2（网络歌单）
            SongCollectionDB.getInstance(MyApplication.getContext()).addNetSongCollection(2, Long.parseLong(songListId), title, SongCount, pic, tag , songCollectionCount);
            return true;
        }
    }
}
