package com.example.administrator.audioplayer.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.bean.MusicFragmengSongCollectionItem;
import com.example.administrator.audioplayer.bean.MusicFragmentExpandItem;
import com.example.administrator.audioplayer.bean.MusicFragmentHeaderItem;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created  on 2017/1/23.
 */

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ItemViewTag> {

    private Context mContext;
    private LayoutInflater mInflater;

    //全部item的集合，包括header，扩展栏，还有所有歌单，因为全在一个RecycleView里面控制，所以用一个全部item的集合来控制不同的显示
    private List allItems = new ArrayList();


    //点击回调
    private OnHeaderItemClickListener onHeaderItemClickListener;
    private OnExpandItemClickListener onExpandItemClickListener;
    private OnSongCollectionItemClickListener onSongCollectionItemClickListener;


    public SongListAdapter(Context context, List allItems) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.allItems = allItems;
    }

    public void setOnHeaderItemClickListener(OnHeaderItemClickListener listener) {
        this.onHeaderItemClickListener = listener;
    }

    public void setOnExpandItemClickListener(OnExpandItemClickListener listener) {
        this.onExpandItemClickListener = listener;
    }

    public void setOnSongCollectionItemClickListener(OnSongCollectionItemClickListener listener) {
        this.onSongCollectionItemClickListener = listener;
    }

    public void updateAdapter(List allItems) {
       this.allItems = allItems;
    }


    @Override
    public ItemViewTag onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据不同item类型获取不同的布局
        switch (viewType) {
            case 0:
                return new ItemViewTag(mInflater.inflate(R.layout.list_item_header, parent, false));
            case 1:
                return new ItemViewTag(mInflater.inflate(R.layout.list_item_songcollection, parent, false));
            case 2:
                return new ItemViewTag(mInflater.inflate(R.layout.list_item_expand, parent, false));
            case 3:
                return new ItemViewTag(mInflater.inflate(R.layout.list_item_expand, parent, false));
        }
        return null;
    }


    @Override
    public void onBindViewHolder(final ItemViewTag holder, final int position) {
        //设置数据以及回调点击事件
        switch (getItemViewType(position)) {
            //header
            case 0:
                MusicFragmentHeaderItem headerItem = (MusicFragmentHeaderItem) allItems.get(position);
                holder.icon.setImageResource(headerItem.getIcon());
                holder.title.setText(headerItem.getTitle());
                holder.count.setText("(" + headerItem.getCount() + ")");
                //回调点击事件
                if(onHeaderItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onHeaderItemClickListener.onItemClick(holder.itemView, position);
                        }
                    });
                }
                break;
            //歌单
            case 1:
                MusicFragmengSongCollectionItem create_songCollectionItems = (MusicFragmengSongCollectionItem) allItems.get(position);
                holder.cover.setImageURI(Uri.parse(create_songCollectionItems.getCover_uri()));
                holder.name.setText(create_songCollectionItems.getName());
                holder.songcount.setText(create_songCollectionItems.getSongCount() + "首");
                holder.songcollectionmore.setImageResource(R.drawable.list_icn_more);

                //回调点击事件
                if(onSongCollectionItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onSongCollectionItemClickListener.onItemClick(holder.itemView, position);
                        }
                    });
                    holder.songcollectionmore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onSongCollectionItemClickListener.onMoreClick(holder.itemView, position);
                        }
                    });
                }
                break;
            default:
                //扩展栏 两个扩展栏视图都是一样的 不同的东西在外部添加点击事件的时候区分
                MusicFragmentExpandItem expandItem = (MusicFragmentExpandItem) allItems.get(position);
                holder.arrow.setImageResource(R.drawable.list_icn_arr_right);
                holder.type.setText(expandItem.getTitle());
                holder.songcollectioncount.setText("(" +expandItem.getSongCollectionCount() + ")");
                //回调点击事件
                if(onExpandItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onExpandItemClickListener.onItemClick(holder, position);
                        }
                    });
                    holder.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onExpandItemClickListener.onMoreClick(holder.itemView, position);
                        }
                    });
                }
                break;
        }
    }


    @Override
    public int getItemCount() {
        return allItems == null ? 0 : allItems.size();
    }



    //设置不同item返回的类型，根据类型来进行不同的绑定
    @Override
    public int getItemViewType(int position) {
        if (getItemCount() == 0) {
            return -1;
        }
        //如果是header，返回type为0
        if (allItems.get(position) instanceof MusicFragmentHeaderItem)
            return 0;
        //如果是歌单，返回type为1
        if (allItems.get(position) instanceof MusicFragmengSongCollectionItem) {
            return 1;
        }
        if (allItems.get(position) instanceof MusicFragmentExpandItem) {
            //如果是扩展栏--创建的歌单，返回type为2
            if (((MusicFragmentExpandItem) allItems.get(position)).getType() == MusicFragmentExpandItem.TYPE_CREATE)
                return 2;
            else {
                //如果是扩展栏--收藏的歌单，返回type为3
                return 3;
            }
        }
        return -1;
    }

    //回调接口
    public interface OnHeaderItemClickListener
    {
        void onItemClick(View view, int position);
    }
    public interface OnExpandItemClickListener
    {
        void onItemClick(RecyclerView.ViewHolder holder, int position);
        void onMoreClick(View view, int position);
    }
    public interface OnSongCollectionItemClickListener
    {
        void onItemClick(View view, int position);
        void onMoreClick(View view, int position);
    }




    public static class ItemViewTag extends RecyclerView.ViewHolder{
        public TextView title, count, type, songcollectioncount, name, songcount;
        public ImageView icon, arrow, more, songcollectionmore;
        public SimpleDraweeView cover;

        //创建歌单栏展开否
        public boolean createdExpanded = true;
        //收藏歌单栏展开否
        public boolean collectExpanded = true;

        public ItemViewTag(View itemView) {
            super(itemView);
            //HeaderItem
            icon = (ImageView) itemView.findViewById(R.id.img_headeritem);
            title = (TextView) itemView.findViewById(R.id.tv_title_headeritem);
            count = (TextView) itemView.findViewById(R.id.tv_count_headeritem);

            //ExpandItem
            arrow = (ImageView) itemView.findViewById(R.id.img_arrow_expanditem);
            type = (TextView) itemView.findViewById(R.id.tv_title_expanditem);
            songcollectioncount = (TextView) itemView.findViewById(R.id.tv_count_expanditem);
            more = (ImageView) itemView.findViewById(R.id.img_more_expanditem);

            //SongCollectionItem
            cover = (SimpleDraweeView) itemView.findViewById(R.id.sdw_cover_songcollectionitem);
            name = (TextView) itemView.findViewById(R.id.tv_title_songcollectionitem);
            songcount = (TextView) itemView.findViewById(R.id.tv_count_songcollectionitem);
            songcollectionmore = (ImageView) itemView.findViewById(R.id.img_more_songcollectionitem);

        }
    }


}
