package com.example.administrator.audioplayer.http;


import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.jsonbean.Album;
import com.example.administrator.audioplayer.jsonbean.Artist;
import com.example.administrator.audioplayer.jsonbean.BillBoard;
import com.example.administrator.audioplayer.jsonbean.CarouselFigure;
import com.example.administrator.audioplayer.jsonbean.HotWord;
import com.example.administrator.audioplayer.jsonbean.Lru;
import com.example.administrator.audioplayer.jsonbean.RecommendNewAlbum;
import com.example.administrator.audioplayer.jsonbean.RecommendSongCollection;
import com.example.administrator.audioplayer.jsonbean.SearchMeageResult;
import com.example.administrator.audioplayer.jsonbean.SongBaseInfo;
import com.example.administrator.audioplayer.jsonbean.SongCollection;
import com.example.administrator.audioplayer.jsonbean.SongCollectionInfo;
import com.example.administrator.audioplayer.jsonbean.SongExtraInfo;


import com.example.administrator.audioplayer.utils.CommonUtils;
import com.example.administrator.audioplayer.utils.PrintLog;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;



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


    private CarouselFigureService carouselFigureService;
    private AlbumService albumService;
    private SongService songService;
    private ArtistService artistService;
    private SongCollectionService songCollectionService;
    private BillBoardService billBoardService;
    private SearchService searchService;

    private static HttpMethods sInstance = new HttpMethods();

    public HttpMethods() {
        OkHttpClient.Builder httpclientBuilder = new OkHttpClient.Builder();
        httpclientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);



        //重写Interceptor拦截器来实现缓存
        Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                CacheControl.Builder cacheBuilder = new CacheControl.Builder();
                cacheBuilder.maxAge(0, TimeUnit.SECONDS);
                cacheBuilder.maxStale(1, TimeUnit.DAYS);
                CacheControl cacheControl = cacheBuilder.build();

                //获取请求
                Request request = chain.request();

                //如果没有网络
                if(!CommonUtils.isConnectInternet(MyApplication.getContext())){
                    //请求中设置缓存策略
                    request = request.newBuilder()
                             .cacheControl(cacheControl)
                             .build();
                }

                //获取响应
                Response originalResponse = chain.proceed(request);

                //如果有网络，则响应中缓存数据
                if(CommonUtils.isConnectInternet(MyApplication.getContext())) {
                    int maxAge = 0;// read from cache
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control","public ,max-age="+ maxAge)
                            .build();
                } else{
                    //如果没有网络，则响应中读取缓存数据
                    int maxStale =60*60*24;// 一天
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control","public, only-if-cached, max-stale="+ maxStale)
                            .build();
                }
            }
        };

        //设置缓存地址，设置拦截器实现缓存
        File sdcache = MyApplication.getContext().getCacheDir();
        Cache cache = new Cache(sdcache.getAbsoluteFile(), 1024 * 1024 * 30); //30Mb
        httpclientBuilder.addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .cache(cache);


        retrofit = new Retrofit.Builder()
                .client(httpclientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();


        carouselFigureService = retrofit.create(CarouselFigureService.class);
        albumService = retrofit.create(AlbumService.class);
        songService = retrofit.create(SongService.class);
        artistService = retrofit.create(ArtistService.class);
        songCollectionService = retrofit.create(SongCollectionService.class);
        billBoardService = retrofit.create(BillBoardService.class);
        searchService = retrofit.create(SearchService.class);

    }

    public static HttpMethods getInstance() {
        return sInstance;
    }


    /**
     * 获取轮播图
     * @param num
     * @return
     */
    public Observable<CarouselFigure> focusPic(int num) {
        String method = "baidu.ting.plaza.getFocusPic";

        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.plaza.getFocusPic"
                + "&num=" + num;
        printUrlLog(url);

        return carouselFigureService.focusPic(FROM, VERSION, FORMAT, method, num);
    }


    /**
     * 获取对应唱片内容
     * @param album_id
     */
    public Observable<Album> albumInfo(String album_id) {
        String method = "baidu.ting.album.getAlbumInfo";

        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.album.getAlbumInfo"
                + "&album_id=" + album_id;
        printUrlLog(url);

        return albumService.albumInfo(FROM, VERSION, FORMAT, method, album_id);
    }


    /**
     * 获取新上架专辑(唱片)
     * @param offset
     * @param limit
     * @return
     */
    public Observable<RecommendNewAlbum> recommendAlbum(int offset, int limit) {
        String method = "baidu.ting.plaza.getRecommendAlbum";

        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.plaza.getRecommendAlbum"
                + "&offset=" + offset + "&limit=" + limit;
        printUrlLog(url);

        return albumService.recommendAlbum(FROM, VERSION, FORMAT, method, offset, limit);
    }


    /**
     * 获取歌曲基本信息
     * @param song_id
     * @return
     */
    public Observable<SongBaseInfo> songBaseInfo(String song_id) {
        String method = "baidu.ting.song.baseInfos";

        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.song.baseInfos"
                + "&song_id=" + song_id;
        printUrlLog(url);

        return songService.songBaseInfo(FROM, VERSION, FORMAT, method, song_id);
    }


    /**
     * 获取歌曲详细信息和下载地址（异步，异步不可以）
     * @param songid
     * @return
     */
    public Observable<SongExtraInfo> songInfo(String songid) {
        String method = "baidu.ting.song.getInfos";
        String ts = Long.toString(System.currentTimeMillis());
        String e = AESTools.encrpty("songid=" + songid + "&ts=" + ts);

        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.song.getInfos"
                + "&songid=" + songid + "&ts=" + ts  + "&e=" + e;
        printUrlLog(url);

        return songService.songInfo(FROM, VERSION, FORMAT, method, songid, ts, e);
    }

    /**
     * 获取歌曲详细信息和下载地址（同步）
     * @param songid
     * @return
     */
    public String songInfoSyn(String songid) {
        String method = "baidu.ting.song.getInfos";
        String ts = Long.toString(System.currentTimeMillis());
        String e = AESTools.encrpty("songid=" + songid + "&ts=" + ts);

        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.song.getInfos"
                + "&songid=" + songid + "&ts=" + ts  + "&e=" + e;
        printUrlLog(url);

        return url;
    }


    /**
     * 获取歌手信息
     * @param artistid
     * @return
     */
    public Observable<Artist> artistInfo(String artistid) {
        String method = "baidu.ting.artist.getinfo";
        String tinguid = "";

        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.artist.getinfo"
                + "&tinguid=" + tinguid + "&artistid=" + artistid;
        printUrlLog(url);


        return artistService.artistInfo(FROM, VERSION, FORMAT, method, tinguid, artistid);
    }



    /**
     * 获取歌单
     * @param pageNo 页码，从1开始
     * @param pageSize 每页多少个
     * @return
     */
    public Observable<SongCollection> geDan(int pageNo, int pageSize) {
        String method = "baidu.ting.diy.gedan";

        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.diy.gedan"
                + "&page_no=" + pageNo + "&page_size=" + pageSize;
        printUrlLog(url);


        return songCollectionService.geDan(FROM, VERSION, FORMAT, method, pageNo, pageSize);
    }


    /**
     * 获取歌单内信息
     * @param listid  歌单的id
     * @return
     */
    public Observable<SongCollectionInfo> geDanInfo(String listid) {
        String method = "baidu.ting.diy.gedanInfo";

        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.diy.gedanInfo"
                + "&listid=" + listid;
        printUrlLog(url);

        return songCollectionService.geDanInfo(FROM, VERSION, FORMAT, method, listid);
    }


    /**
     * 获取热门歌单
     * @param num
     * @return
     */
    public Observable<RecommendSongCollection> hotGeDan(int num) {
        String method = "baidu.ting.diy.getHotGeDanAndOfficial";

        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.diy.getHotGeDanAndOfficial"
                + "&num=" + num;
        printUrlLog(url);

        return songCollectionService.hotGeDan(FROM, VERSION, FORMAT, method, num);

    }




    /**
     * 获取音乐榜单
     * @param type  音乐榜单类型  新歌榜 1; 原创音乐榜 200; 热歌榜 2; 欧美金曲榜 21; King榜 100; 华语金曲榜 25; 经典老哥榜 22;
     * @param offset 榜单内部偏移量，即从第几个开始
     * @param size 获取歌曲数量
     * @return
     */
    public Observable<BillBoard> billSongList(int type, int offset, int size) {
        String method = "baidu.ting.billboard.billList";
        String field = encode("song_id,title,author,album_title,pic_big,pic_small,havehigh,all_rate,charge,has_mv_mobile,learn,song_source,korean_bb_song");

        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.billboard.billList"
                + "&type=" + type + "&offset=" + offset + "&size=" + size + "&field=" + field;
        printUrlLog(url);

        return billBoardService.billSongList(FROM, VERSION, FORMAT, method, type, offset, size, field);
    }



    /**
     * 搜索热词
     */
    public Observable<HotWord> hotword() {
        String method = "baidu.ting.search.hot";

        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.search.hot";
        printUrlLog(url);

        return searchService.hotWord(FROM, VERSION, FORMAT, method);
    }


    /**
     * 搜索对应歌词（异步，异步有问题）
     * @param songname
     * @param artist
     */
    public Observable<Lru> searchLrcPic(String songname, String artist) {
        String method = "baidu.ting.search.lrcpic";
        String type = "2";
        String ts = Long.toString(System.currentTimeMillis());
        String query = encode(songname) + "$$" + encode(artist);
        String e = AESTools.encrpty("query=" + songname + "$$" + artist + "&ts=" + ts);

        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.search.lrcpic&query=" + query
                + "&ts=" + ts + "&type=" + type + "&e=" + e;
        printUrlLog(url);


        return searchService.searchLrcPic(FROM, VERSION, FORMAT, method, query, ts, type, e);
    }


    /**
     * 搜索对应歌词（同步）
     * @param songname
     * @param artist
     */
    public String searchLrcPicSyn(String songname, String artist) {
        String method = "baidu.ting.search.lrcpic";
        String type = "2";
        String ts = Long.toString(System.currentTimeMillis());
        String query = encode(songname) + "$$" + encode(artist);
        String e = AESTools.encrpty("query=" + songname + "$$" + artist + "&ts=" + ts);

        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.search.lrcpic&query=" + query
                + "&ts=" + ts + "&type=" + type + "&e=" + e;
        printUrlLog(url);

        return url;
    }


    /**
     * 综合搜索，会返回歌手，唱片和歌曲的搜索结果
     * @param query
     * @param page_no
     * @param page_size
     */

    public Observable<SearchMeageResult> searchMerge(String query, String page_no, String page_size) {
        String method = "baidu.ting.search.merge";
        String type = "-1";
        String data_source = "0";

        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.search.merge&query=" + query
                + "&page_no=" + page_no + "&page_size=" + page_size +  "&type=" + type + "&data_source=" + data_source;
        printUrlLog(url);

        return searchService.searchMerge(FROM, VERSION, FORMAT, method, query, page_no, page_size, type, data_source);
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
