package com.example.administrator.audioplayer;

import android.app.Application;
import android.content.Context;

import com.example.administrator.audioplayer.utils.PrintLog;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created on 2017/1/24.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Fresco.initialize(this);
        //打开Log
        PrintLog.enable(true);
}

    public static Context getContext(){
        return context;
    }


}
