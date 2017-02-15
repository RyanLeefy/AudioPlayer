package com.example.administrator.audioplayer.http;



import com.example.administrator.audioplayer.jsonbean.HotWord;
import com.example.administrator.audioplayer.jsonbean.Lru;
import com.example.administrator.audioplayer.jsonbean.SearchMeageResult;


import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by on 2017/2/15.
 */

public interface SearchService {


    /**
     * 获取搜索热词接口
    /**
     *
     * @param from
     * @param version
     * @param format
     * @param method
     * @return
     */
    @GET("v1/restserver/ting")
    Observable<HotWord> hotWord(@Query("from") String from, @Query("version") String version, @Query("format") String format,
                                @Query("method") String method);


    /**
     * 获取Lrc的接口
     * @param from
     * @param version
     * @param format
     * @param method
     * @param query
     * @param ts
     * @param type
     * @param e
     * @return
     */
    @GET("v1/restserver/ting")
    Observable<Lru> searchLrcPic(@Query("from") String from, @Query("version") String version, @Query("format") String format,
                                 @Query("method") String method, @Query("query") String query, @Query("ts") String ts,
                                 @Query("type") String type, @Query("e") String e);


    /**
     * 获取综合搜索的接口
     * @param from
     * @param version
     * @param format
     * @param method
     * @param query
     * @param page_no
     * @param page_size
     * @param type
     * @param data_source
     * @return
     */
    @GET("v1/restserver/ting")
    Observable<SearchMeageResult> searchMerge(@Query("from") String from, @Query("version") String version, @Query("format") String format,
                                              @Query("method") String method, @Query("query") String query, @Query("page_no") String page_no,
                                              @Query("page_size") String page_size, @Query("type") String type, @Query("data_source") String data_source);

}
