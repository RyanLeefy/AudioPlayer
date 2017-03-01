package com.example.administrator.audioplayer.modelImp;

import com.example.administrator.audioplayer.Imodel.IAfterSearchModel;
import com.example.administrator.audioplayer.http.HttpMethods;
import com.example.administrator.audioplayer.jsonbean.SearchMeageResult;

import rx.Observable;

/**
 * Created by on 2017/2/28 0028.
 */

public class AfterSearchModel implements IAfterSearchModel {
    @Override
    public Observable<SearchMeageResult> getSearchMeageResult(String query, String page_no, String page_size) {
        return HttpMethods.getInstance().searchMerge(query, page_no, page_size);
    }
}
