package com.example.administrator.audioplayer.http;



import android.content.Context;
import android.util.Log;

import com.example.administrator.audioplayer.utils.PrintLog;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by on 2017/2/22 0022.
 */

public class HttpUtils {


    public static final OkHttpClient.Builder builder = new OkHttpClient.Builder();

    /**
     * 直接获取url返回的body数据
     * @param action1
     * @return
     */
    public static String getResposeString(String action1) {
        try {
            builder.connectTimeout(1000, TimeUnit.MINUTES);
            builder.readTimeout(1000, TimeUnit.MINUTES);
            Request request = new Request.Builder()
                    .url(action1)
                    .build();
            Response response = builder.build().newCall(request).execute();
            if (response.isSuccessful()) {
                String c = response.body().string();
                Logger.json(c);
                return c;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 同步的网络请求，
     * @param action1
     * @param context
     * @param forceCache
     * @return
     */
    public static JsonObject getResposeJsonObject(String action1, Context context, boolean forceCache) {
        try {
            Log.e("action-cache", action1);
            File sdcache = context.getCacheDir();
            Cache cache = new Cache(sdcache.getAbsoluteFile(), 1024 * 1024 * 30); //30Mb
            builder.cache(cache);

            builder.connectTimeout(1000, TimeUnit.MINUTES);
            builder.readTimeout(1000, TimeUnit.MINUTES);
            Request.Builder requestBuilder = new Request.Builder()
                    .url(action1);
            if (forceCache) {
                requestBuilder.cacheControl(CacheControl.FORCE_CACHE);
            }
            Request request = requestBuilder.build();
            Response response = builder.build().newCall(request).execute();
            if (response.isSuccessful()) {
                String c = response.body().string();
                Log.e("cache", c);
                JsonParser parser = new JsonParser();
                JsonElement el = parser.parse(c);
                return el.getAsJsonObject();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
