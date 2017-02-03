package com.example.administrator.audioplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.bean.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/1/26.
 */

public class LocalMusicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final static int FIRST_ITEM = 0;
    final static int ITEM = 1;
    private Context mContext;
    private LayoutInflater mInflater;
    private List mList;

    public LocalMusicAdapter(Context context, List list) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mList = list;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FIRST_ITEM) {
            return new PlayAllItemViewHolder(mInflater.inflate(R.layout.list_item_playallitem, parent, false));
        }
        else {
            return new MusicItemViewHolder(mInflater.inflate(R.layout.list_item_music, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            //播放全部item
            case 0:
                ((PlayAllItemViewHolder) holder).textView.setText("(共" + mList.size() + "首)");


                //select跳转到选择activity
                /*
                ((PlayAllItemViewHolder) holder).select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, SelectActivity.class);
                        intent.putParcelableArrayListExtra("ids", mList);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getActivity().startActivity(intent);
                    }
                });*/
                break;
           //歌曲item
            case 1:
                ((MusicItemViewHolder) holder).mainTitle.setText(((MusicInfo) mList.get(position - 1)).getMusicName());
                ((MusicItemViewHolder) holder).title.setText(((MusicInfo) mList.get(position - 1)).getArtist());
                break;
            default:

                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        else if (mList.isEmpty()) {
            return 0;
        } else return  mList.size() + 1;
    }

    //判断布局类型
    @Override
    public int getItemViewType(int position) {
        return position == FIRST_ITEM ? FIRST_ITEM : ITEM;

    }



    //播放全部Item的Holder
    static class PlayAllItemViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView select;

        PlayAllItemViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.play_all_number);
            this.select = (ImageView) view.findViewById(R.id.select);
        }
    }


    //MusicItem的Holder
    static class MusicItemViewHolder extends RecyclerView.ViewHolder{

        ImageView playState,moreOverflow;
        TextView mainTitle, title;

        public MusicItemViewHolder(View view) {
            super(view);
            this.mainTitle = (TextView) view.findViewById(R.id.viewpager_list_toptext);
            this.title = (TextView) view.findViewById(R.id.viewpager_list_bottom_text);
            this.playState = (ImageView) view.findViewById(R.id.play_state);
            this.moreOverflow = (ImageView) view.findViewById(R.id.viewpager_list_button);

        }
    }


}
