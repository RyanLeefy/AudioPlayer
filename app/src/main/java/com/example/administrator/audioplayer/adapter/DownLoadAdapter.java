package com.example.administrator.audioplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.bean.DownLoadInfo;
import com.example.administrator.audioplayer.db.DownLoadDB;
import com.example.administrator.audioplayer.utils.PrintLog;

import java.util.List;

/**
 * Created by on 2017/3/1 0001.
 */

public class DownLoadAdapter extends RecyclerView.Adapter {

    private static final String TAG = "DownLoadAdapter";

    private Context mContext;

    private LayoutInflater mInflater;

    private List mList;

    private List currentTaskList;

    private OnItemClickListener onItemClickListener;

    private OnHeaderItemClickListener onHeaderItemClickListener;

    final static int FIRST_ITEM = 0;
    final static int ITEM = 1;

    private long completed = 0;
    private long totalsize = -1;

    //记录当前下载的位置
    private int downPosition = -1;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnHeaderItemClickListener(OnHeaderItemClickListener listener) {
        this.onHeaderItemClickListener = listener;
    }

    public DownLoadAdapter(Context context, List list,  List<String> currentTaskList) {
        this.mContext = context;
        this.mList = list;
        this.currentTaskList = currentTaskList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void notifyItem(long completed, long total) {
        // L.D(d,TAG," comleted = " + completed + "  total = " + total);
        this.completed = completed;
        if (total != -1)
            this.totalsize = total;
        notifyItemChanged(downPosition);

    }

    public void updateAdapter(List<String> currentTaskList) {
        this.mList = DownLoadDB.getInstance(MyApplication.getContext()).getDowningDownLoadInfoList();
        this.currentTaskList = currentTaskList;
        //重置状态
        completed = 0;
        totalsize = -1;
        notifyDataSetChanged();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == FIRST_ITEM) {
            return new HeaderViewHolder(mInflater.inflate(R.layout.list_item_download_header, parent, false));
        } else {
            return new CommonViewHolder(mInflater.inflate(R.layout.list_item_download, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(getItemViewType(position) == FIRST_ITEM) {
            //HeadItem
            if(currentTaskList.size() > 0) {
                ((HeaderViewHolder) holder).tv_download_state.setText("全部暂停");
                ((HeaderViewHolder) holder).img_download_state.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_pause_download));
            } else {
                ((HeaderViewHolder) holder).tv_download_state.setText("全部开始");
                ((HeaderViewHolder) holder).img_download_state.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_download));
            }

            //添加点击事件
            if(onHeaderItemClickListener != null) {
                ((HeaderViewHolder) holder).ll_download_state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onHeaderItemClickListener.onStartOrStopAllDownLoadClick(holder, position);
                    }
                });

                ((HeaderViewHolder) holder).ll_download_deleteall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onHeaderItemClickListener.onClearAllTaskClick(holder, position);
                    }
                });
            }


        } else {
            //是不是当前要下载的
            boolean isCurrent = false;
            //是不是准备要下载的
            boolean isPreparing = false;
            //设置标题
            final DownLoadInfo info = (DownLoadInfo) mList.get(position - 1);
            ((CommonViewHolder) holder).down_name.setText(info.getFileName());
            ((CommonViewHolder) holder).info = info;

            if (currentTaskList.size() > 0) {
                PrintLog.d(TAG, "currentlist size = " + currentTaskList.size());
                //如果下载任务中第一个的id等于下载信息的id，则该下载信息是当前要下载的
                isCurrent = currentTaskList.get(0).equals(info.getDownloadId());
                ((CommonViewHolder) holder).isCurrent = isCurrent;
                if (isCurrent) {
                    downPosition = position;
                }
                //如果下载任务中包括下载信息的id，则这些下载信息是要准备下载的
                if (currentTaskList.contains(info.getDownloadId())) {
                    isPreparing = true;
                }
                ((CommonViewHolder) holder).isPreparing = isPreparing;
            }

            //如果是当前要下载的
            if (isCurrent) {
                completed = completed > info.getCompletedSize() ? completed : info.getCompletedSize();
                totalsize = totalsize > info.getTotalSize() ? totalsize : info.getTotalSize();
                if (completed == 0 || totalsize == -1) {
                    //如果正在开始下载，还没下载过，则隐藏下载进度条，显示计算文件大小
                    ((CommonViewHolder) holder).down_count.setText("正在计算文件大小");
                    ((CommonViewHolder) holder).pb.setVisibility(View.GONE);
                } else {
                    //如果已经下载过，则显示进度
                    ((CommonViewHolder) holder).down_count.setText((float) (Math.round((float) completed / (1024 * 1024) * 10)) / 10 + "M/" +
                            (float) (Math.round((float) totalsize / (1024 * 1024) * 10)) / 10 + "M");
                    if (((CommonViewHolder) holder).pb.getVisibility() != View.VISIBLE)
                        ((CommonViewHolder) holder).pb.setVisibility(View.VISIBLE);
                    if (totalsize > 0)
                        ((CommonViewHolder) holder).pb.setProgress((int) (100 * completed / totalsize));
                }
            } else if (isPreparing) {
                //如果是准备要下载的，隐藏进度条，显示歌手名
                ((CommonViewHolder) holder).pb.setVisibility(View.GONE);
                ((CommonViewHolder) holder).down_count.setText(info.getArtist());
            } else {
                //如果是不是当前要下载的，也不是准备要下载的，则是已经暂停的，隐藏进度条，显示已暂停
                ((CommonViewHolder) holder).pb.setVisibility(View.GONE);
                ((CommonViewHolder) holder).down_count.setText("已经暂停下载，点击恢复");
            }


            //添加点击事件
            if(onItemClickListener != null) {
                ((CommonViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(holder, position);
                    }
                });

                ((CommonViewHolder) holder).delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onDeleteClick(holder, position);
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        //显示的是数据库读取出来的还没下载完成的下载信息，其中有包括正在下载的或者准备下载的或者暂停了的
        //具体怎么显示根据传入的下载任务DownLoadTask中来确定，
        //第一项是正在下载的
        //其他项是准备下载的
        //没有的是暂停了的
        if (mList == null) {
            return 0;
        }
        else if (mList.isEmpty()) {
            return 0;
        } else {
            return mList.size() + 1;
        }
    }

    //判断布局类型
    @Override
    public int getItemViewType(int position) {
        return position == FIRST_ITEM ? FIRST_ITEM : ITEM;
    }




    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_download_state, ll_download_deleteall;
        public TextView tv_download_state;
        public ImageView img_download_state;


        public HeaderViewHolder(View itemView) {
            super(itemView);

            ll_download_state = (LinearLayout) itemView.findViewById(R.id.ll_download_state);
            img_download_state = (ImageView) itemView.findViewById(R.id.img_download_state);
            tv_download_state = (TextView) itemView.findViewById(R.id.tv_download_state);
            ll_download_deleteall = (LinearLayout) itemView.findViewById(R.id.ll_download_deleteall);
        }
    }



    public static class CommonViewHolder extends RecyclerView.ViewHolder {

        //需要保存的数据
        public Boolean isCurrent = false;
        public Boolean isPreparing = false;
        public DownLoadInfo info;

        TextView down_name, down_count;
        ProgressBar pb;
        ImageView delete;

        public CommonViewHolder(View itemView) {
            super(itemView);

            down_name = (TextView) itemView.findViewById(R.id.tv_down_name);
            down_count = (TextView) itemView.findViewById(R.id.tv_down_count);
            pb = (ProgressBar) itemView.findViewById(R.id.pb_down_progressbar);
            pb.setMax(100);
            delete = (ImageView) itemView.findViewById(R.id.img_delete);

        }
    }



    public interface OnHeaderItemClickListener {
        void onStartOrStopAllDownLoadClick(RecyclerView.ViewHolder holder, int position);
        void onClearAllTaskClick(RecyclerView.ViewHolder holder, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder holder, int position);
        void onDeleteClick(RecyclerView.ViewHolder holder, int position);
    }

}
