package com.example.administrator.audioplayer.Iview;

import com.example.administrator.audioplayer.adapter.RankingAdapter;

/**
 * Created by on 2017/2/25 0025.
 */

public interface IRankingView {
    void setAdapter(RankingAdapter adapter);
    void showTryAgain();
}
