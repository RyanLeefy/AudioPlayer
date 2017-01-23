package com.example.administrator.audioplayer.adapter;

import android.content.Context;
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

    private List<MusicFragmentHeaderItem> headerItemsList;
    private List<MusicFragmentExpandItem> expandItemsList;
    //所有歌单
    private List<MusicFragmengSongCollectionItem> songCollectionItemsList = new ArrayList<>();
    //创建的歌单
    private List<MusicFragmengSongCollectionItem> create_songCollectionItemsList;
    //收藏的歌单
    private List<MusicFragmengSongCollectionItem> collect_songCollectionItemsList;

    //全部item的集合，包括header，扩展栏，还有所有歌单，因为全在一个RecycleView里面控制，所以用一个全部item的集合来控制不同的显示
    private List allItems = new ArrayList();


    /**
     *
     * @param context
     * @param headerItemsList
     * @param expandItemsList
     * @param create_songCollectionItemsList
     * @param collect_songCollectionItemsList
     */
    public SongListAdapter(Context context,
                           List<MusicFragmentHeaderItem> headerItemsList,
                           List<MusicFragmentExpandItem> expandItemsList,
                           List<MusicFragmengSongCollectionItem> create_songCollectionItemsList,
                           List<MusicFragmengSongCollectionItem> collect_songCollectionItemsList) {

        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.headerItemsList = headerItemsList;
        this.expandItemsList = expandItemsList;
        this.create_songCollectionItemsList = create_songCollectionItemsList;
        this.collect_songCollectionItemsList = collect_songCollectionItemsList;

        songCollectionItemsList.addAll(create_songCollectionItemsList);
        songCollectionItemsList.addAll(collect_songCollectionItemsList);

    }

    public void setHeaderItemsList(List<MusicFragmentHeaderItem> headerItemsList) {
        this.headerItemsList = headerItemsList;
    }

    public void setExpandItemsList(List<MusicFragmentExpandItem> expandItemsList) {
        this.expandItemsList = expandItemsList;
    }

    public void setSongCollectionItemsList(List<MusicFragmengSongCollectionItem> songCollectionItemsList) {
        this.songCollectionItemsList = songCollectionItemsList;
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
    public void onBindViewHolder(ItemViewTag holder, int position) {
        //设置数据
        switch (getItemViewType(position)) {
            //header
            case 0:
                //holder.icon.setImageResource();
                holder.title.setText("本地音乐");
                holder.count.setText("0");
                break;
            //歌单
            case 1:
               //holder.cover.setImageURI();
               holder.name.setText("我喜欢的音乐");
               holder.songcount.setText("0");
               holder.songcollectionmore.setImageResource(R.drawable.list_icn_more);
                break;
            //扩展栏-创建的歌单
            case 2:
                holder.arrow.setImageResource(R.drawable.list_icn_arr_right);
                holder.type.setText("创建的歌单");
                break;
            //扩展栏-收藏的歌单
            case 3:
                holder.arrow.setImageResource(R.drawable.list_icn_arr_right);
                holder.type.setText("收藏的歌单");
                break;

        }

    }

    @Override
    public int getItemCount() {
        return 0;
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
        //如果是扩展栏--创建的歌单，返回type为2
        if (allItems.get(position) instanceof MusicFragmentExpandItem) {
            if (((MusicFragmentExpandItem) allItems.get(position)).getType() == MusicFragmentExpandItem.TYPE_CREATE)
                return 2;
        }
        //如果是扩展栏--收藏的歌单，返回type为3
        return 3;
    }



    private void setHeaderItemListener(ItemViewTag holder, int position) {
        switch (position) {
            //
            case 0:
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            //
            case 1:
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            //
            case 2:
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            //
            case 3:
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
        }
    }

    private void setExpandItemListener(int position) {

    }

    private void setSongCollectionItemListener(int position){

    }









    static class ItemViewTag extends RecyclerView.ViewHolder{
        protected TextView title, count, type, name, songcount;
        protected ImageView icon, arrow, more, songcollectionmore;
        protected SimpleDraweeView cover;


        public ItemViewTag(View itemView) {
            super(itemView);
            //HeaderItem
            icon = (ImageView) itemView.findViewById(R.id.img_headeritem);
            title = (TextView) itemView.findViewById(R.id.tv_title_headeritem);
            count = (TextView) itemView.findViewById(R.id.tv_count_headeritem);

            //ExpandItem
            arrow = (ImageView) itemView.findViewById(R.id.img_arrow_expanditem);
            type = (TextView) itemView.findViewById(R.id.tv_title_expanditem);
            more = (ImageView) itemView.findViewById(R.id.img_more_expanditem);

            //SongCollectionItem
            cover = (SimpleDraweeView) itemView.findViewById(R.id.sdw_cover_songcollectionitem);
            name = (TextView) itemView.findViewById(R.id.tv_title_songcollectionitem);
            songcount = (TextView) itemView.findViewById(R.id.tv_count_songcollectionitem);
            songcollectionmore = (ImageView) itemView.findViewById(R.id.img_more_songcollectionitem);

        }
    }


}
