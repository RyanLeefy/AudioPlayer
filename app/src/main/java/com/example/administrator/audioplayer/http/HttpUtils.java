package com.example.administrator.audioplayer.http;


import android.content.Context;
import android.util.Log;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
            builder.connectTimeout(10000, TimeUnit.MINUTES);
            builder.readTimeout(10000, TimeUnit.MINUTES);
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

            builder.connectTimeout(10000, TimeUnit.MINUTES);
            builder.readTimeout(10000, TimeUnit.MINUTES);
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

    public static void testServer(String url, Callback callback) {
        builder.connectTimeout(10000, TimeUnit.MINUTES)
                .readTimeout(10000, TimeUnit.MINUTES);

        //RequestBody body = RequestBody.create();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = builder.build().newCall(request);
        //传入回调函数
        call.enqueue(callback);

    }



    public static void postFile(String url, String filepath, Callback callback) {
        builder.connectTimeout(10000, TimeUnit.MINUTES)
                .readTimeout(10000, TimeUnit.MINUTES);

        byte[] bytes = File2byte(filepath);
        StringBuilder sb = new StringBuilder();
        for(int j = 0; j <bytes.length; j++) {
            if(sb.length() == 0) {
                sb.append(bytes[j]);
            } else {
                sb.append("," + bytes[j]);
            }
        }
        JSONObject json = new JSONObject();
        try {
            json.put("fileString", sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json;"), json.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = builder.build().newCall(request);
        //传入回调函数
        call.enqueue(callback);

    }


    public static byte[] File2byte(String filePath)
    {
        byte[] buffer = null;
        try
        {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return buffer;

    }


    public static void post(final String filepath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.42.0.1:5000/recognize");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setConnectTimeout(10000);
                    con.setReadTimeout(10000);
                    con.setRequestMethod("POST");

                    String fileString = File2byte(filepath).toString();
                    DataOutputStream outputStream = new DataOutputStream(con.getOutputStream());
                    outputStream.writeUTF(fileString);
                    outputStream.flush();

                    ObjectInputStream inputStream = new ObjectInputStream(con.getInputStream());
                    String result = (String) inputStream.readObject();
                    Logger.e(result);
                    outputStream.close();
                    inputStream.close();
                    con.disconnect();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {

                }


            }
        }).start();

    }


}
