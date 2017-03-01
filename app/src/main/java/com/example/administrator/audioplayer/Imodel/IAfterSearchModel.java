package com.example.administrator.audioplayer.Imodel;

import com.example.administrator.audioplayer.jsonbean.SearchMeageResult;

import rx.Observable;

/**
 * Created by on 2017/2/28 0028.
 */

public interface IAfterSearchModel {
    Observable<SearchMeageResult> getSearchMeageResult(String query, String page_no, String page_size);
}
