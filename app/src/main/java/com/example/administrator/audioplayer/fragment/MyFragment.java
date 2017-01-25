package com.example.administrator.audioplayer.fragment;

import android.support.v4.app.Fragment;

/**
 * fragment中无法重现onBackPressed()对PopupWindow进行处理，所以提供一个接口，供activity中调用处理
 * Created on 2017/1/24.
 */
public abstract class MyFragment extends Fragment {
    public abstract boolean onBackPressed();
}
