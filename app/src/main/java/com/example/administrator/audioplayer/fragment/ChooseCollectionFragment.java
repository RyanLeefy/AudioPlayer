package com.example.administrator.audioplayer.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.SongListAdapter;
import com.example.administrator.audioplayer.bean.CollectionInfo;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.db.SongCollectionDB;
import com.example.administrator.audioplayer.db.SongCollectionSongDB;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.example.administrator.audioplayer.utils.PreferencesUtils;

import java.util.List;

/**
 * Created by on 2017/3/21 0021.
 */

public class ChooseCollectionFragment extends DialogFragment {

    private Context mContext;

    private RecyclerView recyclerView;

    //数据库中歌单列表
    private List<CollectionInfo> list;

    //传进来的歌曲信息
    private MusicInfo musicInfo;


    public static ChooseCollectionFragment newInstance(MusicInfo info) {
        ChooseCollectionFragment fragment = new ChooseCollectionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("MusicInfo", info);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();

        //mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //设置无标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.fragment_choose_collection, container);

        if (getArguments() != null) {
            musicInfo = getArguments().getParcelable("MusicInfo");
        }


        recyclerView = (RecyclerView) view.findViewById(R.id.rv_choose_collection);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);



        list = SongCollectionDB.getInstance(MyApplication.getContext()).getCreateSongCollection();
        final SongListAdapter adapter = new SongListAdapter(mContext, list);
        //歌单没有more图标
        adapter.setCollectionHasMore(false);
        adapter.setOnSongCollectionItemClickListener(new SongListAdapter.OnSongCollectionItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //收藏到该歌单
                CollectionInfo collectionInfo = (CollectionInfo)adapter.getList().get(position);
                //先判断该歌曲是否已经收藏在该歌单中了
                if (SongCollectionSongDB.getInstance(MyApplication.getContext()).isCollected(collectionInfo.getId(), musicInfo)) {
                    //已经收藏在该歌单
                    Toast.makeText(mContext, "该歌曲已在该歌单中", Toast.LENGTH_SHORT).show();
                } else {
                    //未收藏，收藏到歌单，添加入数据库
                    SongCollectionSongDB.getInstance(MyApplication.getContext()).addSongToCollection(collectionInfo.getId(), musicInfo);
                    Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
                }

                dismiss();
            }

            @Override
            public void onMoreClick(View view, int position) {
                //无用
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }




    @Override
    public void onStart() {
        super.onStart();
        //设置fragment高度 、宽度
        //int dialogHeight = (int) (getActivity().getResources().getDisplayMetrics().heightPixels * 0.56);
        //int dialogWidth = (int) (getActivity().getResources().getDisplayMetrics().widthPixels * 0.63);
        //int dialogWidth = CommonUtils.dip2px(MyApplication.getContext(), 500);
        int dialogWidth = (int) (getActivity().getResources().getDisplayMetrics().widthPixels * 0.90);
        int dialogHeight = (int) (getActivity().getResources().getDisplayMetrics().heightPixels * 0.65);
        //int dialogHeight = CommonUtils.dip2px(MyApplication.getContext(), 500);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        getDialog().setCanceledOnTouchOutside(true);

    }
}
