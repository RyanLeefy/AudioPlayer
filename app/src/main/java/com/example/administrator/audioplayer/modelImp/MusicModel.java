package com.example.administrator.audioplayer.modelImp;

import com.example.administrator.audioplayer.Imodel.IMusicModel;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.bean.CollectionInfo;
import com.example.administrator.audioplayer.db.SongCollectionDB;

import java.util.List;

/**
 * Created by on 2017/3/21 0021.
 */

public class MusicModel implements IMusicModel {


    @Override
    public List getCreateSongCollection() {
        return SongCollectionDB.getInstance(MyApplication.getContext()).getCreateSongCollection();
    }

    @Override
    public List getNetSongCollection() {
        return SongCollectionDB.getInstance(MyApplication.getContext()).getNetSongCollection();
    }

    @Override
    public void addCreateSongCollection(CollectionInfo collectionInfo) {

    }

    @Override
    public void deleteCreateSongCollection(int id) {

    }

    @Override
    public void updateCreateSongCollection(int id) {

    }

    @Override
    public void deleteNetSongCollection(int id) {

    }

    @Override
    public void updateNetSongCollection(int id) {

    }
}
