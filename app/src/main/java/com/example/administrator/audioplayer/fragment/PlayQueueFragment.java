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
import android.widget.Toast;

import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.service.MusicPlayer;
import com.example.administrator.audioplayer.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/2/7.
 */

public class PlayQueueFragment extends DialogFragment {

    private TextView mCollect, mTitle, mClear;
    private RecyclerView mRecyclerView;

    private List<MusicInfo> playlist;

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
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));


        playlist = MusicPlayer.getQueue();

        MusicAdapter adapter = new MusicAdapter(getActivity(), playlist);
        //最后的imageview设为删除而不是更多
        adapter.setMore(false);
        //设置歌曲名，歌手名水平显示
        adapter.setHorizontal(true);
        mRecyclerView.setAdapter(adapter);


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

    //@Override
    public void setAdapter(MusicAdapter adapter) {
        //获取回来的adapter先设置监听事件，然后再设置给recycleView

        adapter.setOnMusicItemClickListener(new MusicAdapter.OnMusicItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //获取点击的MusicInfo实体类((LocalMusicAdapter)rv.getAdapter()).getItem(position)
                //调用presenter的方法，播放该实体代表的歌曲

                //presenter.pefromMusicClick(position);

                //Toast.makeText(getActivity(), ((MusicAdapter)rv.getAdapter()).getItem(position).getMusicName(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onMoreClick(View view, int position) {
                //Toast.makeText(getActivity(), ((MusicAdapter)rv.getAdapter()).getItem(position).getMusicName() + "more", Toast.LENGTH_SHORT).show();
                //删除
            }
        });

        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
