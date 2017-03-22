package com.example.administrator.audioplayer.modelImp;

import com.example.administrator.audioplayer.Imodel.ICreatedCollectionModel;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.db.SongCollectionSongDB;

import java.util.List;

/**
 * Created by on 2017/3/22 0022.
 */

public class CreatedCollectionModel implements ICreatedCollectionModel {
    @Override
    public List<MusicInfo> getCollectionSong(int id) {
        return SongCollectionSongDB.getInstance(MyApplication.getContext()).getSongListById(id);
    }

    @Override
    public void cancelCollect(int id, MusicInfo musicInfo) {
        SongCollectionSongDB.getInstance(MyApplication.getContext()).deleteSongid(id, musicInfo);
    }
}
