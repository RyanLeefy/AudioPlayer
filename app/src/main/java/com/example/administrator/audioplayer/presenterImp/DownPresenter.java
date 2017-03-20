package com.example.administrator.audioplayer.presenterImp;

import com.example.administrator.audioplayer.Ipresenter.IDownPresenter;
import com.example.administrator.audioplayer.Iview.IDownView;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.activity.DownActivity;
import com.example.administrator.audioplayer.adapter.DownLoadAdapter;
import com.example.administrator.audioplayer.bean.DownLoadInfo;
import com.example.administrator.audioplayer.db.DownLoadDB;
import com.example.administrator.audioplayer.download.DownloadService;

import java.util.ArrayList;

/**
 * Created by on 2017/3/1 0001.
 */

public class DownPresenter implements IDownPresenter {

    private IDownView view;

    private ArrayList<DownLoadInfo> mList = new ArrayList<>();

    public DownPresenter(IDownView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {

        mList = DownLoadDB.getInstance(MyApplication.getContext()).getDowningDownLoadInfoList();

        DownLoadAdapter adapter = new DownLoadAdapter((DownActivity)view, mList, DownloadService.getPrepareTasks());

        view.setAdapter(adapter);
    }


}
