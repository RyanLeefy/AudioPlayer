package com.example.administrator.audioplayer.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.jsonbean.SearchMeageResult;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

/**
 * Created by on 2017/2/28 0028.
 */

public class SearchAlbumAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List mList;

    //HeaderItem
    public static final int FIRST_ITEM = 0;
    //CommonItem
    public static final int ITEM = 1;
    //FooterItem
    public static final int FOOTER_ITEM = 2;


    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public List getList() {
        return mList;
    }


    public SearchAlbumAdapter(Context context, List list) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mList = list;
    }

    public void updateAdapterWithMoreList(List list) {
        mList.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM) {
            return new CommonItemViewHolder(mInflater.inflate(R.layout.list_item_search_album, parent, false));
        } else {
            return new FooterItemViewHolder(mInflater.inflate(R.layout.list_item_footer_songcolletion, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(getItemViewType(position) == ITEM) {
            SearchMeageResult.ResultBean.AlbumInfoBean.AlbumListBean albumListBean = (SearchMeageResult.ResultBean.AlbumInfoBean.AlbumListBean) mList.get(position);


            //先保存对应的歌单数据
            ((CommonItemViewHolder)holder).albumid = albumListBean.getAlbum_id();
            ((CommonItemViewHolder)holder).pic = albumListBean.getPic_small();
            ((CommonItemViewHolder)holder).title = albumListBean.getTitle().replaceAll("<em>", "").replaceAll("</em>", "");
            ((CommonItemViewHolder)holder).author = albumListBean.getAuthor().replaceAll("<em>", "").replaceAll("</em>", "");
            ((CommonItemViewHolder)holder).artist_id = albumListBean.getArtist_id();
            ((CommonItemViewHolder)holder).publishtime = albumListBean.getPublishtime();


            //创建一个ImageRequest,设置图片地址，裁剪图片，以防出现内存不足
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(albumListBean.getPic_small()))
                    .build();

            //构建DraweeController
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(((CommonItemViewHolder) holder).art.getController())
                    .setImageRequest(request)
                    .build();

            //设置图片
            ((CommonItemViewHolder) holder).art.setController(controller);

            ((CommonItemViewHolder) holder).albumname.setText(albumListBean.getTitle().replaceAll("<em>", "").replaceAll("</em>", ""));
            ((CommonItemViewHolder) holder).albumauthor.setText(albumListBean.getAuthor().replaceAll("<em>", "").replaceAll("</em>", ""));


            if(listener != null) {
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
        if (mList == null) {
            return 0;
        } else if (mList.isEmpty()) {
            return 0;
        } else {
            //有footer的话，判断mList的大小，如果不是10的倍数，则表示已经读完了，则不用显示footer，否则显示footer
            if(mList.size() % 10 == 0) {
                return mList.size() + 1;
            } else {
                return mList.size();
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        //有footer的话，判断mList的大小，如果不是10的倍数，则表示已经读完了，则不用显示footer，否则显示footer
        if(mList.size() % 10 == 0) {
            if (position == mList.size()) {
                return FOOTER_ITEM;
            } else {
                return ITEM;
            }
        } else {
            return ITEM;
        }
    }



    //回调接口
    public interface OnItemClickListener
    {
        void onItemClick(RecyclerView.ViewHolder viewHolder, int position);
    }



    public static class CommonItemViewHolder extends RecyclerView.ViewHolder {

        //用来保存对应的歌单数据
        public String albumid;
        public String pic;
        public String title;
        public String author;
        public String artist_id;
        public String publishtime;

        public SimpleDraweeView art;
        TextView albumname;
        TextView albumauthor;

        public CommonItemViewHolder(View itemView) {
            super(itemView);
            art = (SimpleDraweeView) itemView.findViewById(R.id.sdv_search_album);
            albumname = (TextView) itemView.findViewById(R.id.tv_title_search_album);
            albumauthor = (TextView) itemView.findViewById(R.id.tv_author_search_album);
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
}
