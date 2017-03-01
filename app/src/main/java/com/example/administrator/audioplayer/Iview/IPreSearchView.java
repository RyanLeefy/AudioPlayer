package com.example.administrator.audioplayer.Iview;

import com.example.administrator.audioplayer.adapter.SearchHistoryAdapter;

import java.util.List;

/**
 * Created by on 2017/2/27 0027.
 */

public interface IPreSearchView {
    void setHotWord(List<String> list);
    void setSearchHistory(SearchHistoryAdapter adapter);
    void showTryAgain();
}
