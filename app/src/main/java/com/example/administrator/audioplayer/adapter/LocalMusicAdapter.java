package com.example.administrator.audioplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.audioplayer.R;

/**
 * Created on 2017/1/26.
 */

public class LocalMusicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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



}
