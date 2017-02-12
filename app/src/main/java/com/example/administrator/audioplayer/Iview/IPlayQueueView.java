package com.example.administrator.audioplayer.Iview;

import com.example.administrator.audioplayer.adapter.MusicAdapter;

/**
 * Created by on 2017/2/11 0011.
 */

public interface IPlayQueueView {
    void updateSongNumber(int n);
    void setAdapter(MusicAdapter adapter);
}
