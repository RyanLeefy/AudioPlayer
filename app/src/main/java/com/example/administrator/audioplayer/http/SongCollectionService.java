package com.example.administrator.audioplayer.http;

import com.example.administrator.audioplayer.jsonbean.RecommendSongCollection;
import com.example.administrator.audioplayer.jsonbean.SongCollection;
import com.example.administrator.audioplayer.jsonbean.SongCollectionInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by on 2017/2/15.
 */

public interface SongCollectionService {

    /**
     * 获取歌单
     * @param from
     * @param version
     * @param format
     * @param method
     * @param page_size
     * @param page_no
     * @return
     */
    @GET("v1/restserver/ting")
    Observable<SongCollection> geDan(@Query("from") String from, @Query("version") String version, @Query("format") String format,
                                     @Query("method") String method, @Query("page_size") int page_size, @Query("page_no") int page_no);


    /**
     * 获取歌单内歌曲
     * @param from
     * @param version
     * @param format
     * @param method
     * @param listid
     * @return
     */
    @GET("v1/restserver/ting")
    Observable<SongCollectionInfo> geDanInfo(@Query("from") String from, @Query("version") String version, @Query("format") String format,
                                             @Query("method") String method, @Query("listid") String listid);


    /**
     * 获取热门歌单接口
     * @param from
     * @param version
     * @param format
     * @param method
     * @param num
     * @return
     */
    @GET("v1/restserver/ting")
    Observable<RecommendSongCollection> hotGeDan(@Query("from") String from, @Query("version") String version, @Query("format") String format,
                                                 @Query("method") String method, @Query("num") int num);



}
