package com.example.administrator.audioplayer.Iview;

import com.example.administrator.audioplayer.adapter.NetSongListAdapter;

import java.util.List;

/**
 * Created by on 2017/2/24 0024.
 */

public interface INetSongListView {
    void setAdapter(NetSongListAdapter adapter);
    void updateAdapter(List list);
    void showTryAgain();
}
