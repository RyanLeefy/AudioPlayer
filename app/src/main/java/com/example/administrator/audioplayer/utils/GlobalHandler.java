package com.example.administrator.audioplayer.utils;

import android.content.Context;
import android.os.Handler;

import com.example.administrator.audioplayer.MyApplication;

import java.lang.ref.WeakReference;

/**
 * Created by on 2017/2/6.
 * 仅用来post Runnable对象
 */

public class GlobalHandler extends Handler {
    private WeakReference<Context> reference;
    private static GlobalHandler instance = null;

    public GlobalHandler() {
        reference = new WeakReference<Context>(MyApplication.getContext());
    }
    public static GlobalHandler getInstance() {
        if(instance == null) {
            synchronized (GlobalHandler.class) {
                if(instance == null) {
                    instance = new GlobalHandler();
                }
            }
        }
        return instance;
    }
}
