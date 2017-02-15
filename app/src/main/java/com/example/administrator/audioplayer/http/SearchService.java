package com.example.administrator.audioplayer.http;



import com.example.administrator.audioplayer.jsonbean.HotWord;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by on 2017/2/15.
 */

public interface SearchService {

    /**
     *
     * @param from
     * @param version
     * @param format
     * @param method
     * @return
     */
    @GET("v1/restserver/ting")
    Observable<HotWord> hotWord(@Query("from") String from, @Query("version") String version, @Query("format") String format, @Query("method") String method);



    @GET("v1/restserver/ting")
    Observable<String> searchLrcPic(@Query("from") String from, @Query("version") String version, @Query("format") String format, @Query("method") String method,
                                    @Query("query") String query, @Query("ts") String ts, @Query("type") String type, @Query("e") String e);


    @GET("v1/restserver/ting")
    Observable<String> searchMerge(@Query("from") String from, @Query("version") String version, @Query("format") String format, @Query("method") String method,
                                   @Query("query") String query, @Query("page_no") String page_no, @Query("page_size") String page_size,
                                   @Query("type") String type, @Query("data_source") String data_source);



}
