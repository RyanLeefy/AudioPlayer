package com.example.administrator.audioplayer.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.jsonbean.RecommendNewAlbum;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

/**
 * Created by on 2017/2/16 0016.
 */

public class RecommendNewAlbumAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List mList;

    private OnItemClickListener listener;


    //歌单图片的大小
    private static final int WIDTH = 160;
    private static final int HEIGHT = 160;


    public RecommendNewAlbumAdapter(Context context, List list) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mList = list;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.list_item_recommend_newalbum, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        RecommendNewAlbum.PlazeAlbumListBean.RMBean.AlbumListBean.ListBean newAlbum = (RecommendNewAlbum.PlazeAlbumListBean.RMBean.AlbumListBean.ListBean)mList.get(position);


        //先保存对应的歌单数据
        ((ViewHolder)holder).albumid = newAlbum.getAlbum_id();
        ((ViewHolder)holder).pic = newAlbum.getPic_big();
        ((ViewHolder)holder).title = newAlbum.getTitle();
        ((ViewHolder)holder).author = newAlbum.getAuthor();
        ((ViewHolder)holder).artist_id = newAlbum.getArtist_id();
        ((ViewHolder)holder).publishtime = newAlbum.getPublishtime();


        //创建一个ImageRequest,设置图片地址，裁剪图片，以防出现内存不足
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(newAlbum.getPic_big()))
                .setResizeOptions(new ResizeOptions(WIDTH, HEIGHT))
                .build();

        //构建DraweeController
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(((ViewHolder) holder).art.getController())
                .setImageRequest(request)
                .build();

        //设置图片
        ((ViewHolder) holder).art.setController(controller);

        ((ViewHolder) holder).albumName.setText(newAlbum.getTitle());
        ((ViewHolder) holder).artsit.setText(newAlbum.getAuthor());

        //设置点击事件
        if(listener != null) {
            ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(holder, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //用来保存对应的歌单数据
        public String albumid;
        public String pic;
        public String title;
        public String author;
        public String artist_id;
        public String publishtime;

        private SimpleDraweeView art;
        private TextView albumName, artsit;

        public ViewHolder(View view){
            super(view);
            art = (SimpleDraweeView) itemView.findViewById(R.id.album_art);
            albumName = (TextView) itemView.findViewById(R.id.album_name);
            artsit = (TextView) itemView.findViewById(R.id.artist_name);
        }
    }


    //回调接口
    public interface OnItemClickListener
    {
        void onItemClick(RecyclerView.ViewHolder viewHolder, int position);
    }

}
