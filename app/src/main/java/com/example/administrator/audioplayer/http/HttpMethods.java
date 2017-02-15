package com.example.administrator.audioplayer.http;

import android.util.Log;

import com.example.administrator.audioplayer.jsonbean.HotWord;
import com.example.administrator.audioplayer.jsonbean.Lru;
import com.example.administrator.audioplayer.jsonbean.SearchMeageResult;
import com.example.administrator.audioplayer.utils.PrintLog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;

/**
 * Created by on 2017/2/15.
 */

public class HttpMethods {

    private static final String TAG = "Httpethods";

    //public static final String BASE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&";
    public static final String BASE_URL = "http://tingapi.ting.baidu.com/";
    // PATH: v1/restserver/ting 在各个接口中加在头部 @GET("v1/restserver/ting")

    public static final String FROM = "android";

    public static final String VERSION = "5.6.5.6";

    public static final String FORMAT = "json";

    public static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    private SearchService searchService;

    private static HttpMethods sInstance = new HttpMethods();

    public HttpMethods() {
        OkHttpClient.Builder httpclientBuilder = new OkHttpClient.Builder();
        httpclientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        //缓存
        //File sdcache = context.getCacheDir();
        //Cache cache = new Cache(sdcache.getAbsoluteFile(), 1024 * 1024 * 30); //30Mb
        //httpclientBuilder.cache(cache);

        retrofit = new Retrofit.Builder()
                .client(httpclientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        searchService = retrofit.create(SearchService.class);

    }

    public static HttpMethods getInstance() {
        return sInstance;
    }


    /**
     * 搜索热词
     * @param subscriber
     */
    public void hotword(Subscriber<HotWord> subscriber) {
        String method = "baidu.ting.search.hot";
        searchService.hotWord(FROM, VERSION, FORMAT, method);

        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.search.hot";
        printUrlLog(url);
    }


    /**
     * 搜索对应歌词
     * @param subscriber
     * @param songname
     * @param artist
     */
    public void searchLrcPic(Subscriber<Lru> subscriber, String songname, String artist) {
        String method = "baidu.ting.search.lrcpic";
        String type = "2";
        String ts = Long.toString(System.currentTimeMillis());
        String query = encode(songname) + "$$" + encode(artist);
        String e = AESTools.encrpty("query=" + songname + "$$" + artist + "&ts=" + ts);
        searchService.searchLrcPic(FROM, VERSION, FORMAT, method, query, ts, type, e);


        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.search.lrcpic&query=" + query
                + "&ts=" + ts + "&type=" + type + "&e=" + e;
        printUrlLog(url);
    }


    /**
     * 综合搜索，会返回歌手，唱片和歌曲的搜索结果
     * @param subscriber
     * @param query
     * @param page_no
     * @param page_size
     */
    public void searchMerge(Subscriber<SearchMeageResult> subscriber, String query, String page_no, String page_size) {
        String method = "baidu.ting.search.merge";
        String type = "-1";
        String data_source = "0";
        searchService.searchMerge(FROM, VERSION, FORMAT, method, query, page_no, page_size, type, data_source);

        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.search.merge&query=" + query
                + "&page_no=" + page_no + "&page_size=" + page_size +  "&type=" + type + "&data_source=" + data_source;
        printUrlLog(url);
    }



    public static String encode(String str) {
        if (str == null) return "";

        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }


    //打印url log
    public void printUrlLog(String url) {
        PrintLog.e(TAG, "=======================HttpURL============================");
        PrintLog.e(TAG, "URL:" + url);
        PrintLog.e(TAG, "=======================HttpURL============================");
    }

}
