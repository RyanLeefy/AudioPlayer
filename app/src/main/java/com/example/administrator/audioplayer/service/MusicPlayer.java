package com.example.administrator.audioplayer.service;

/**
 * Created by on 2017/1/19.
 */

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.WeakHashMap;

/**
 * 管理客户端与后台服务的连接，保存Service实例在其中
 * 给客户端提供各种接口，提供绑定服务，解绑服务，还有各种Service的各种操作，可以包含特有的逻辑
 * 将所有关于音乐的操作集中到此类中，此类相当于工具类，不过需要初始化，即绑定操作
 */

public class MusicPlayer {

    //MediaService实例，在onServiceConnected中赋值
    private static MediaService mService = null;

    //绑定映射，用于解绑
    private static WeakHashMap<Context, ServiceBinder> mConnectionMap = new WeakHashMap<Context, ServiceBinder>();



    //初始化，绑定服务,多次调用不会重复绑定
    public static ServiceToken bindToService(final Context context) {

        Activity realActivity = ((Activity) context).getParent();
        if (realActivity == null) {
            realActivity = (Activity) context;
        }
        ContextWrapper contextWrapper = new ContextWrapper(realActivity);
        contextWrapper.startService(new Intent(contextWrapper, MediaService.class));
        ServiceBinder binder = new ServiceBinder(contextWrapper.getApplicationContext());
        if (contextWrapper.bindService(
                new Intent().setClass(contextWrapper, MediaService.class), binder, 0)) {
            mConnectionMap.put(contextWrapper, binder);
            return new ServiceToken(contextWrapper);
        }
        return null;
    }

    //解除绑定
    public static void unbindFromService(final ServiceToken token) {
        if (token == null) {
            return;
        }
        final ContextWrapper mContextWrapper = token.mWrappedContext;
        final ServiceBinder mBinder = mConnectionMap.remove(mContextWrapper);
        if (mBinder == null) {
            return;
        }
        mContextWrapper.unbindService(mBinder);
        if (mConnectionMap.isEmpty()) {
            mService = null;
        }
    }


    //
    public static class ServiceBinder implements ServiceConnection {
        private Context mContext;


        public ServiceBinder(final Context context) {
            mContext = context;
        }

        @Override
        public void onServiceConnected(final ComponentName className, final IBinder service) {
            mService = ((MediaService.MyBinder)service).getService();
            //initPlaybackServiceWithSettings(mContext);
        }

        @Override
        public void onServiceDisconnected(final ComponentName className) {
            mService = null;
        }
    }


    public static class ServiceToken {
        public ContextWrapper mWrappedContext;

        public ServiceToken(final ContextWrapper context) {
            mWrappedContext = context;
        }
    }
}
