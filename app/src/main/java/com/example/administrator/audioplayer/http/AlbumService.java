package com.example.administrator.audioplayer.http;

import com.example.administrator.audioplayer.jsonbean.Album;
import com.example.administrator.audioplayer.jsonbean.HotWord;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by on 2017/2/15.
 */

public interface AlbumService {

    /**
     * 获取唱片信息接口
     * @param from
     * @param version
     * @param format
     * @param method
     * @param album_id
     * @return
     */
    @GET("v1/restserver/ting")
    Observable<Album> albumInfo(@Query("from") String from, @Query("version") String version, @Query("format") String format,
                                @Query("method") String method, @Query("album_id") String album_id);

}
