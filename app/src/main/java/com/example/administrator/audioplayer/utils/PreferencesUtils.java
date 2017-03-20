package com.example.administrator.audioplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by on 2017/2/22 0022.
 */

public class PreferencesUtils {

    private static PreferencesUtils sInstance;

    private static SharedPreferences mPreferences;

    private static final String DOWNMUSIC_BIT = "downmusic_bit";

    public PreferencesUtils(final Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static final PreferencesUtils getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesUtils(context.getApplicationContext());
        }
        return sInstance;
    }


    //设置歌曲的播放连接
    public void setPlayLink(long id, String link) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(id + "", link);
        editor.apply();
    }

    public String getPlayLink(long id) {
        return mPreferences.getString(id + "", null);
    }


    //设置下载的码率
    public void setDownMusicBit(int bit) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(DOWNMUSIC_BIT, bit);
        editor.apply();
    }

    //有128标准，256较高，320高品质
    //默认256，较高
    public int getDownMusicBit() {
        return mPreferences.getInt(DOWNMUSIC_BIT, 256);
    }
}
