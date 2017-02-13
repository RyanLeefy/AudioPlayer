package com.example.administrator.audioplayer.fragment;


import android.content.res.TypedArray;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.audioplayer.Ipresenter.IPlayQueuePresenter;
import com.example.administrator.audioplayer.Iview.IPlayQueueView;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.activity.BaseActivity;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.presenterImp.PlayQueuePresenter;
import com.example.administrator.audioplayer.service.MusicPlayer;
import com.example.administrator.audioplayer.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/2/7.
 */

public class PlayQueueFragment extends DialogFragment implements IPlayQueueView,  BaseActivity.MusicStateListener {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    private TextView mCollect, mTitle, mClear;
    private RecyclerView mRecyclerView;

    private List<MusicInfo> playlist;

    private IPlayQueuePresenter presenter;


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


        //playlist = MusicPlayer.getQueue();

        //MusicAdapter adapter = new MusicAdapter(getActivity(), playlist);
        //最后的imageview设为删除而不是更多
        //adapter.setMore(false);
        //设置歌曲名，歌手名水平显示
        //adapter.setHorizontal(true);
        //mRecyclerView.setAdapter(adapter);

        initListener();

        presenter = new PlayQueuePresenter(this);
        presenter.onCreateView();

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

    /**
     * 当fragment显示时候，把其作为回调函数加到BaseActivity中，以便进行各种回调操作
     */
    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) getActivity()).setMusicStateListenerListener(this);
    }


    /**
     * 当fragment消失时候，去除回调
     */
    @Override
    public void onStop() {
        super.onStop();
        ((BaseActivity) getActivity()).removeMusicStateListenerListener(this);
    }


    public void initListener(){
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.peformClearClick();
            }
        });
    }

    @Override
    public void updateSongNumber(int n) {
        mTitle.setText("播放列表（" + n + "）");
    }

    //@Override
    public void setAdapter(MusicAdapter adapter) {
        //获取回来的adapter先设置监听事件，然后再设置给recycleView

        //最后的imageview设为删除而不是更多
        adapter.setMore(false);
        //设置歌曲名，歌手名水平显示
        adapter.setHorizontal(true);

        adapter.setOnMusicItemClickListener(new MusicAdapter.OnMusicItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //调用presenter的方法，播放该歌曲
                presenter.peformMusicClick(position);
                Toast.makeText(getActivity(), ((MusicAdapter)mRecyclerView.getAdapter()).getItem(position).getMusicName(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onMoreClick(View view, int position) {
                //Toast.makeText(getActivity(), ((MusicAdapter)rv.getAdapter()).getItem(position).getMusicName() + "more", Toast.LENGTH_SHORT).show();
                //删除
                presenter.peformDeleteClick(position);
            }
        });

        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMetaChange() {
        //重新载入数据
        presenter.onCreateView();
    }

    @Override
    public void onPlayStateChange() {

    }

    @Override
    public void updateTrackInfo() {

    }

    @Override
    public void reloadAdapter() {

    }
}
