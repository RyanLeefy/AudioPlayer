package com.example.administrator.audioplayer;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created on 2017/1/24.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
