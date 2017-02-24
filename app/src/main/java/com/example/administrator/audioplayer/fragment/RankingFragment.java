package com.example.administrator.audioplayer.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.audioplayer.R;

/**
 * Netfragment下的排行榜fragment
 *
 */
public class RankingFragment extends Fragment {


    private ChangeViewPagerCallBack callBack;

    public void setChangeViewPagerCallBack(ChangeViewPagerCallBack callBack) {
        this.callBack = callBack;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ranking, container, false);
    }

}
