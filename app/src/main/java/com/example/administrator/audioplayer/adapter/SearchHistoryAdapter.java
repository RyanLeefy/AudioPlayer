package com.example.administrator.audioplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.audioplayer.R;

import java.util.List;

/**
 * Created by on 2017/2/27 0027.
 */

public class SearchHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;

    private List mList;

    private OnItemClickListener listener;

    public SearchHistoryAdapter(Context context, List list) {
        this.mList = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.list_item_search_history, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.searchString.setText((String)mList.get(position));


        //设置点击事件
        if(listener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(holder, position);
                }
            });

            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteClick(holder, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public interface OnItemClickListener
    {
        void onItemClick(RecyclerView.ViewHolder holder, int position);
        void onDeleteClick(RecyclerView.ViewHolder holder, int position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView searchString;
        private ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.img_recent);
            searchString = (TextView) itemView.findViewById(R.id.tv_search_history);
            delete = (ImageView) itemView.findViewById(R.id.img_delete);

        }
    }

}
