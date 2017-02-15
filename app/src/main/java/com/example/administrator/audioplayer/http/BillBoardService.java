package com.example.administrator.audioplayer.http;

import com.example.administrator.audioplayer.jsonbean.BillBoard;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by on 2017/2/15.
 */

public interface BillBoardService {

    /**
     * 获取音乐榜单接口
     * @param from
     * @param version
     * @param format
     * @param method
     * @param type  类型
     * @param offset 偏移量
     * @param size  获取数量
     * @param fields
     * @return
     */
    @GET("v1/restserver/ting")
    Observable<BillBoard> billSongList(@Query("from") String from, @Query("version") String version, @Query("format") String format,
                                    @Query("method") String method, @Query("type") int type, @Query("offset") int offset,
                                       @Query("size") int size, @Query("fields") String fields);
}
