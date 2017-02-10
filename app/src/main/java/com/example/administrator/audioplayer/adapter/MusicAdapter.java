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
import com.example.administrator.audioplayer.service.MusicPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/1/26.
 * 歌曲的adapter，默认没有PlayItem，需要的话初始化时候传入true
 * 默认歌曲名和歌手名上下排列，如需打横排列设置setHorizontal（true）
 * 默认末尾是更多，若是删除，需要setMore(false)
 */

public class MusicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    //有无第一个播放全部项
    private boolean HasPlayItem = false;

    //默认歌曲名和歌手名上下排列
    private boolean isHorizontal = false;

    //末尾是更多还是删除
    private boolean isMore = true;
    final static int FIRST_ITEM = 0;
    final static int ITEM = 1;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<MusicInfo> mList;


    private OnPlayAllItemClickListener onPlayAllItemClickListener;
    private OnMusicItemClickListener onMusicItemClickListener;


    public MusicAdapter(Context context, List list) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mList = list;
    }

    public MusicAdapter(Context context, List list, boolean hasPlayItem) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mList = list;
        HasPlayItem = hasPlayItem;
    }

    public void setOnPlayAllItemClickListener(OnPlayAllItemClickListener onPlayAllItemClickListener) {
        this.onPlayAllItemClickListener = onPlayAllItemClickListener;
    }

    public void setOnMusicItemClickListener(OnMusicItemClickListener onMusicItemClickListener) {
        this.onMusicItemClickListener = onMusicItemClickListener;
    }

    public void updateAdapter(List list) {
        this.mList = list;
    }

    public void setHorizontal(boolean horizontal) {
        this.isHorizontal = horizontal;
    }

    public void setMore(boolean isMore) {
        this.isMore = isMore;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //先判断有无playitem
        if(!HasPlayItem) {
            if(isHorizontal){
                //播放列表的item
                return new MusicItemViewHolder(mInflater.inflate(R.layout.list_item_playlist, parent, false), isHorizontal);
            } else {
                //正常列表的item
                return new MusicItemViewHolder(mInflater.inflate(R.layout.list_item_music, parent, false), isHorizontal);
            }
        } else {
            if (viewType == FIRST_ITEM) {
                return new PlayAllItemViewHolder(mInflater.inflate(R.layout.list_item_playallitem, parent, false));
            }
            else {
                return new MusicItemViewHolder(mInflater.inflate(R.layout.list_item_music, parent, false), isHorizontal);
            }
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            //播放全部item
            case 0:
                ((PlayAllItemViewHolder) holder).textView.setText("(共" + mList.size() + "首)");
                if(onPlayAllItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onPlayAllItemClickListener.onItemClick(holder.itemView, position);
                        }
                    });
                    ((PlayAllItemViewHolder) holder).select.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onPlayAllItemClickListener.onMoreClick(holder.itemView, position);
                        }
                    });
                }

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
                //判断有没有播放全部的item
                int realPosition;
                if(HasPlayItem) {
                    realPosition = position - 1;
                } else {
                    realPosition = position;
                }

                ((MusicItemViewHolder) holder).musicName.setText(((MusicInfo) mList.get(realPosition)).getMusicName());
                if(isHorizontal){
                    //水平显示的话 歌曲名和歌手名要加个"-"
                    ((MusicItemViewHolder) holder).artistName.setText(" - " +((MusicInfo) mList.get(realPosition)).getArtist());
                } else {
                    ((MusicItemViewHolder) holder).artistName.setText(((MusicInfo) mList.get(realPosition)).getArtist());
                }


                //判断该条目音乐是否是当前音乐，是的话显示小喇叭
                if (MusicPlayer.getCurrentAudioId() == mList.get(realPosition).getAudioId()) {
                    ((MusicItemViewHolder) holder).playState.setVisibility(View.VISIBLE);
                    ((MusicItemViewHolder) holder).playState.setImageResource(R.drawable.song_play_icon);
                } else {
                    ((MusicItemViewHolder) holder).playState.setVisibility(View.GONE);
                }


                if(isMore) {
                    //末尾是更多图片
                    ((MusicItemViewHolder) holder).moreOverflow.setImageResource(R.drawable.list_icn_more);
                } else {
                    //末尾是删除图片
                    ((MusicItemViewHolder) holder).moreOverflow.setImageResource(R.drawable.list_icn_delete);
                }

                //设置点击事件
                if(onMusicItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onMusicItemClickListener.onItemClick(holder.itemView, position);
                        }
                    });
                    ((MusicItemViewHolder) holder).moreOverflow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onMusicItemClickListener.onMoreClick(holder.itemView, position);
                        }
                    });
                }
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
        } else if(HasPlayItem) {
            return  mList.size() + 1;
        } else {
            return mList.size();
        }
    }

    //判断布局类型
    @Override
    public int getItemViewType(int position) {
        if(!HasPlayItem) {
            return ITEM;
        } else {
            return position == FIRST_ITEM ? FIRST_ITEM : ITEM;
        }
    }

    public MusicInfo getItem(int position) {
        int realPosition;
        if(HasPlayItem) {
            realPosition = position - 1;
        } else {
            realPosition = position;
        }
        return mList.get(realPosition);
    }


    //回调接口
    public interface OnPlayAllItemClickListener
    {
        void onItemClick(View view, int position);
        void onMoreClick(View view, int position);
    }
    public interface OnMusicItemClickListener
    {
        void onItemClick(View view, int position);
        void onMoreClick(View view, int position);
    }



    //播放全部Item的Holder
    public static class PlayAllItemViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView select;

        PlayAllItemViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.play_all_number);
            this.select = (ImageView) view.findViewById(R.id.select);
        }
    }


    //MusicItem的Holder
    public static class MusicItemViewHolder extends RecyclerView.ViewHolder{

        ImageView playState,moreOverflow;
        public TextView musicName, artistName;

        public MusicItemViewHolder(View view, Boolean Horizontal) {
            super(view);
            this.musicName = (TextView) view.findViewById(R.id.tv_musicname);

            if(Horizontal) {
                this.artistName = (TextView) view.findViewById(R.id.tv_right_text);
            } else {
                this.artistName = (TextView) view.findViewById(R.id.tv_bottom_text);
            }
            this.playState = (ImageView) view.findViewById(R.id.play_state);
            this.moreOverflow = (ImageView) view.findViewById(R.id.img_moreOverflow);

        }
    }


}
