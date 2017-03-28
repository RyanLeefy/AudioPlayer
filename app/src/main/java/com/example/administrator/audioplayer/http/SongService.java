package com.example.administrator.audioplayer.http;

import com.example.administrator.audioplayer.jsonbean.SongBaseInfo;
import com.example.administrator.audioplayer.jsonbean.SongExtraInfo;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by on 2017/2/15.
 */

public interface SongService {

    /**
     * 获取歌曲基本信息接口
     * @param from
     * @param version
     * @param format
     * @param method
     * @param song_id
     * @return
     */
    @Headers("User-Agent:Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
    @GET("v1/restserver/ting")
    Observable<SongBaseInfo> songBaseInfo(@Query("from") String from, @Query("version") String version, @Query("format") String format,
                                          @Query("method") String method, @Query("song_id") String song_id);


    /**
     * 获取歌曲详细信息和下载地址接口
     * @param from
     * @param version
     * @param format
     * @param method
     * @param songid
     * @param ts
     * @param e
     * @return
     */
    @Headers("User-Agent:Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
    @GET("v1/restserver/ting")
    Observable<SongExtraInfo> songInfo(@Query("from") String from, @Query("version") String version, @Query("format") String format,
                                       @Query("method") String method, @Query("songid") String songid, @Query("ts") String ts,
                                       @Query("e") String e);

}
