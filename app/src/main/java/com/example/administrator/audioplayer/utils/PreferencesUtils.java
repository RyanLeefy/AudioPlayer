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

    public PreferencesUtils(final Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static final PreferencesUtils getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesUtils(context.getApplicationContext());
        }
        return sInstance;
    }


    public void setPlayLink(long id, String link) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(id + "", link);
        editor.apply();
    }

    public String getPlayLink(long id) {
        return mPreferences.getString(id + "", null);
    }
}
