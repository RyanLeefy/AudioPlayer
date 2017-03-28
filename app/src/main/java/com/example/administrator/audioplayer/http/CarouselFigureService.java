package com.example.administrator.audioplayer.http;

import com.example.administrator.audioplayer.jsonbean.CarouselFigure;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by on 2017/2/16 0016.
 */

public interface CarouselFigureService {


    /**
     * 获取轮播图接口
     * @param from
     * @param version
     * @param format
     * @param method
     * @param num
     * @return
     */
    @Headers("User-Agent:Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
    @GET("v1/restserver/ting")
    Observable<CarouselFigure> focusPic(@Query("from") String from, @Query("version") String version, @Query("format") String format,
                                          @Query("method") String method, @Query("num") int num);
}
