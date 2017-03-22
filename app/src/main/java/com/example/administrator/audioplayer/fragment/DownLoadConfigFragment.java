package com.example.administrator.audioplayer.fragment;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.example.administrator.audioplayer.utils.PreferencesUtils;

/**
 * Created by on 2017/3/21 0021.
 */

public class DownLoadConfigFragment extends DialogFragment implements View.OnClickListener {

    private TextView low, normal, high;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //设置无标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.fragment_download_config, container);
        low = (TextView) view.findViewById(R.id.tv_download_config_low);
        normal = (TextView) view.findViewById(R.id.tv_download_config_normal);
        high = (TextView) view.findViewById(R.id.tv_download_config_high);

        low.setOnClickListener(this);
        normal.setOnClickListener(this);
        high.setOnClickListener(this);


        switch (PreferencesUtils.getInstance(MyApplication.getContext()).getDownMusicBit()) {
            case 128:
                low.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 256:
                normal.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 320:
                high.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_download_config_low:
                PreferencesUtils.getInstance(MyApplication.getContext()).setDownMusicBit(128);
                dismiss();
                break;
            case R.id.tv_download_config_normal:
                PreferencesUtils.getInstance(MyApplication.getContext()).setDownMusicBit(256);
                dismiss();
                break;
            case R.id.tv_download_config_high:
                PreferencesUtils.getInstance(MyApplication.getContext()).setDownMusicBit(320);
                dismiss();
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置样式
        //setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDatePickerDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置fragment高度 、宽度
        //int dialogHeight = (int) (getActivity().getResources().getDisplayMetrics().heightPixels * 0.56);
        //int dialogWidth = (int) (getActivity().getResources().getDisplayMetrics().widthPixels * 0.63);
        int dialogWidth = CommonUtils.dip2px(MyApplication.getContext(), 300);
        int dialogHeight = CommonUtils.dip2px(MyApplication.getContext(), 240);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        getDialog().setCanceledOnTouchOutside(true);

    }
}
