package com.example.administrator.audioplayer.fragment;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.R;

/**
 * Created by on 2017/2/7.
 */

public class PlayQueueFragment extends DialogFragment {

    private TextView mCollect, mTitle, mClear;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置样式
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_FRAME, R.style.BottomDialogFragment);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //设置从底部弹出
        WindowManager.LayoutParams params = getDialog().getWindow()
                .getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        getDialog().getWindow().setAttributes(params);


        View view = inflater.inflate(R.layout.fragment_playqueue, container);

        mCollect = (TextView) view.findViewById(R.id.playlist_addto);
        mTitle = (TextView) view.findViewById(R.id.play_list_number);
        mClear = (TextView) view.findViewById(R.id.playlist_clear_all);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.play_list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
        mRecyclerView.setHasFixedSize(true);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        //设置fragment高度 、宽度
        int dialogHeight = (int) (MyApplication.getContext().getResources().getDisplayMetrics().heightPixels * 0.6);
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, dialogHeight);
        getDialog().setCanceledOnTouchOutside(true);

    }
}
