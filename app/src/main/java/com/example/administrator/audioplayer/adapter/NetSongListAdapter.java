package com.example.administrator.audioplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.jsonbean.SongCollection;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

/**
 * Created by on 2017/2/16 0016.
 */

public class NetSongListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List mList;

    //可变String，可以在其中间加图片
    private SpannableString spanString;

    private OnItemClickListener listener;

    //HeaderItem
    public static final int FIRST_ITEM = 0;
    //CommonItem
    public static final int ITEM = 1;
    //FooterItem
    public static final int FOOTER_ITEM = 2;

    //歌单图片的大小
    private static final int WIDTH = 160;
    private static final int HEIGHT = 160;

    public NetSongListAdapter(Context context, List list) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mList = list;

        //初始化收听图片
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.index_icn_earphone);
        ImageSpan imageSpan = new ImageSpan(mContext, bitmap, ImageSpan.ALIGN_BASELINE);
        spanString = new SpannableString("icon");
        spanString.setSpan(imageSpan, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    /**
     * 把新的内容附加在原有的list后面
     * @param list
     */
    public void updateAdapter(List list) {
        this.mList.addAll(list);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == FIRST_ITEM) {
            return new HeaderItemViewHolder(mInflater.inflate(R.layout.list_item_header_songcolletion, parent, false));
        } else if(viewType == ITEM) {
            return new CommonItemViewHolder(mInflater.inflate(R.layout.list_item_netsonglist_songcollection, parent, false));
        } else {
            return new FooterItemViewHolder(mInflater.inflate(R.layout.list_item_footer_songcolletion, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(getItemViewType(position) == ITEM) {
            //position要-1 ，因为第一项是headerItem
            SongCollection.ContentBean songCollection = (SongCollection.ContentBean) mList.get(position - 1);

            //先保存对应的歌单数据
            ((CommonItemViewHolder) holder).listid = songCollection.getListid();
            ((CommonItemViewHolder) holder).pic = songCollection.getPic_300();
            ((CommonItemViewHolder) holder).listenum = songCollection.getListenum();
            ((CommonItemViewHolder) holder).title = songCollection.getTitle();
            ((CommonItemViewHolder) holder).tag = songCollection.getTag();

            //创建一个ImageRequest,设置图片地址，裁剪图片，以防出现内存不足
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(songCollection.getPic_300()))
                    //.setResizeOptions(new ResizeOptions(WIDTH, HEIGHT))
                    .build();

            //构建DraweeController
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(((CommonItemViewHolder) holder).art.getController())
                    .setImageRequest(request)
                    .build();

            //设置图片
            ((CommonItemViewHolder) holder).art.setController(controller);


            ((CommonItemViewHolder) holder).name.setText(songCollection.getTitle());
            ((CommonItemViewHolder) holder).count.setText(spanString);
            int count = Integer.parseInt(songCollection.getListenum());
            if (count > 10000) {
                count = count / 10000;
                ((CommonItemViewHolder) holder).count.append(" " + count + "万");
            } else {
                ((CommonItemViewHolder) holder).count.append(" " + songCollection.getListenum());
            }

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
        //还有headerItem 和 footerItem
        return mList.size() + 2;
    }


    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            //第一项头部
           return FIRST_ITEM;
        } else if(position == mList.size() + 1) {
            //最后一项尾部
            return FOOTER_ITEM;
        } else {
            return ITEM;
        }
    }


    /**
     * 如果布局是GridLayoutManager，则第一行设置只显示一列，用来显示HeaderItem
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(getItemViewType(position) == FIRST_ITEM) {
                        //头部的话占一行
                        return gridManager.getSpanCount();
                    } else if(getItemViewType(position) == FOOTER_ITEM) {
                        //尾部的话占一行
                        return gridManager.getSpanCount();
                    } else {
                        //正常item占一行的一格
                        return 1;
                    }

                }
            });
        }
    }




     static class HeaderItemViewHolder extends RecyclerView.ViewHolder{

         public HeaderItemViewHolder(View itemView) {
            super(itemView);
        }
    }


    //歌单Item
    public static class CommonItemViewHolder extends RecyclerView.ViewHolder{
        //用来保存对应的歌单数据
        public String listid;
        public String pic;
        public String listenum;
        public String title;
        public String tag;

        private SimpleDraweeView art;
        private TextView name, count;

        public CommonItemViewHolder(View itemView) {
            super(itemView);
            art = (SimpleDraweeView) itemView.findViewById(R.id.playlist_art);
            name = (TextView) itemView.findViewById(R.id.playlist_name);
            count = (TextView) itemView.findViewById(R.id.playlist_listen_count);
        }
    }



    public static class FooterItemViewHolder extends RecyclerView.ViewHolder{

        private ImageView anim_image;

        public FooterItemViewHolder(View itemView) {
            super(itemView);
            //加载画面的帧动画
            anim_image = (ImageView) itemView.findViewById(R.id.anim_image);
            anim_image.setBackgroundResource(R.drawable.loading_animation);
            AnimationDrawable anim = (AnimationDrawable) anim_image.getBackground();
            anim.start();
        }
    }


    //回调接口
    public interface OnItemClickListener
    {
        void onItemClick(RecyclerView.ViewHolder viewHolder, int position);
    }

}
