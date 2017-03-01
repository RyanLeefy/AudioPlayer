package com.example.administrator.audioplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.bean.BillBoardItem;

import java.util.List;

/**
 * Created by on 2017/2/25 0025.
 */

public class RankingAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private LayoutInflater mInflater;

    //用来装所有的歌曲，所以一共有mList/3 + 1项
    private List mList;

    //HeaderItem
    public static final int FIRST_ITEM = 0;
    //CommonItem
    public static final int ITEM = 1;

    private OnItemClickListener listener;

    //热歌榜，新歌榜，原创榜
    int[] pic = {R.drawable.ranklist_hotmusic, R.drawable.ranklist_newmusic, R.drawable.ranklist_origin};


    public RankingAdapter(Context context, List list) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mList = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

        @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == FIRST_ITEM) {
            return new HeaderItemViewHolder(mInflater.inflate(R.layout.list_item_header_ranking, parent, false));
        } else {
            return new CommonItemViewHolder(mInflater.inflate(R.layout.list_item_ranking, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(getItemViewType(position) == ITEM) {
            //一个item有一个图片，3个歌曲
            BillBoardItem billBoardItem1 = (BillBoardItem)mList.get((position - 1) * 3);
            BillBoardItem billBoardItem2 = (BillBoardItem)mList.get((position - 1) * 3 + 1);
            BillBoardItem billBoardItem3 = (BillBoardItem)mList.get((position - 1) * 3 + 2);

            //先保存对应的歌单数据
            if ((position - 1) == 0) {
                ((CommonItemViewHolder) holder).type = billBoardItem1.getType();
                ((CommonItemViewHolder) holder).name = billBoardItem1.getName();
                ((CommonItemViewHolder) holder).update_time = billBoardItem1.getUpdate_time();
            } else if ((position - 1) == 1) {
                ((CommonItemViewHolder) holder).type = billBoardItem2.getType();
                ((CommonItemViewHolder) holder).name = billBoardItem2.getName();
                ((CommonItemViewHolder) holder).update_time = billBoardItem2.getUpdate_time();
            } else if ((position - 1) == 2)  {
                ((CommonItemViewHolder) holder).type = billBoardItem3.getType();
                ((CommonItemViewHolder) holder).name = billBoardItem3.getName();
                ((CommonItemViewHolder) holder).update_time = billBoardItem3.getUpdate_time();
            }

            ((CommonItemViewHolder) holder).item_image.setImageResource(pic[position - 1]);
            ((CommonItemViewHolder) holder).rank_first_txt.setText("1." + billBoardItem1.getTitle() + " - " + billBoardItem1.getAuthor());
            ((CommonItemViewHolder) holder).rank_second_txt.setText("2." + billBoardItem2.getTitle() + " - " + billBoardItem2.getAuthor());
            ((CommonItemViewHolder) holder).rank_third_txt.setText("3." + billBoardItem3.getTitle() + " - " + billBoardItem3.getAuthor());



            //设置点击事件
            if (listener != null) {
                ((CommonItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(holder, position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        //包括HeaderItem
        return mList.size()/3 + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            //第一项头部
            return FIRST_ITEM;
        } else {
            return ITEM;
        }
    }


    public static class HeaderItemViewHolder extends RecyclerView.ViewHolder{

        public HeaderItemViewHolder(View itemView) {
            super(itemView);
        }
    }


    //歌单Item
    public static class CommonItemViewHolder extends RecyclerView.ViewHolder{
        //用来保存对应的数据
        public int type;
        public String name;
        public String update_time;

        public ImageView item_image;
        private TextView rank_first_txt, rank_second_txt, rank_third_txt;

        public CommonItemViewHolder(View itemView) {
            super(itemView);
            item_image = (ImageView) itemView.findViewById(R.id.item_image);
            rank_first_txt = (TextView) itemView.findViewById(R.id.rank_first_txt);
            rank_second_txt = (TextView) itemView.findViewById(R.id.rank_second_txt);
            rank_third_txt = (TextView) itemView.findViewById(R.id.rank_third_txt);
        }
    }


    //回调接口
    public interface OnItemClickListener
    {
        void onItemClick(RecyclerView.ViewHolder viewHolder, int position);
    }


}
