package com.example.administrator.audioplayer.modelImp;

import com.example.administrator.audioplayer.Imodel.IRecentModel;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.db.RecentMusicDB;

import java.util.List;

/**
 * Created by on 2017/2/13.
 */

public class RecentModel implements IRecentModel {
    @Override
    public List getRecentMusic() {
        return RecentMusicDB.getInstance(MyApplication.getContext()).getRecentList(null);
    }
}
