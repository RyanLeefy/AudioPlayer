package com.example.administrator.audioplayer.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.bean.RecommendNewAlbumItem;
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


    public RecommendNewAlbumAdapter(Context context, List list) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mList = list;
    }

    //歌单图片的大小
    private static final int WIDTH = 160;
    private static final int HEIGHT = 160;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.list_item_recommend_newalbum, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecommendNewAlbum.PlazeAlbumListBean.RMBean.AlbumListBean.ListBean newAlbum = (RecommendNewAlbum.PlazeAlbumListBean.RMBean.AlbumListBean.ListBean)mList.get(position);

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
        ((ViewHolder)holder).art.setController(controller);

        ((ViewHolder) holder).albumName.setText(newAlbum.getTitle());
        ((ViewHolder) holder).artsit.setText(newAlbum.getAuthor());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView art;
        private TextView albumName, artsit;

        public ViewHolder(View view){
            super(view);
            art = (SimpleDraweeView) itemView.findViewById(R.id.album_art);
            albumName = (TextView) itemView.findViewById(R.id.album_name);
            artsit = (TextView) itemView.findViewById(R.id.artist_name);
        }
    }

}
