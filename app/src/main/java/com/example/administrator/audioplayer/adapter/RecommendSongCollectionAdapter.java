package com.example.administrator.audioplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.bean.RecommendSongCollectionItem;
import com.example.administrator.audioplayer.jsonbean.RecommendSongCollection;
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

public class RecommendSongCollectionAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List mList;

    //可变String，可以在其中间加图片
    private SpannableString spanString;


    //歌单图片的大小
    private static final int WIDTH = 160;
    private static final int HEIGHT = 160;

    public RecommendSongCollectionAdapter(Context context, List list) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mList = list;

        //初始化收听图片
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.index_icn_earphone);
        ImageSpan imageSpan = new ImageSpan(mContext, bitmap, ImageSpan.ALIGN_BASELINE);
        spanString = new SpannableString("icon");
        spanString.setSpan(imageSpan, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.list_item_recommend_songcollection, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecommendSongCollection.ContentBean.ListBean songCollection = (RecommendSongCollection.ContentBean.ListBean)mList.get(position);

        //创建一个ImageRequest,设置图片地址，裁剪图片，以防出现内存不足
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(songCollection.getPic()))
                .setResizeOptions(new ResizeOptions(WIDTH, HEIGHT))
                .build();

        //构建DraweeController
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(((ViewHolder) holder).art.getController())
                .setImageRequest(request)
                .build();

        //设置图片
        ((ViewHolder)holder).art.setController(controller);


        ((ViewHolder) holder).name.setText(songCollection.getTitle());
        ((ViewHolder) holder).count.setText(spanString);
        int count = Integer.parseInt(songCollection.getListenum());
        if (count > 10000) {
            count = count / 10000;
            ((ViewHolder) holder).count.append(" " + count + "万");
        } else {
            ((ViewHolder) holder).count.append(" " + songCollection.getListenum());
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView art;
        private TextView name, count;

        public ViewHolder(View itemView) {
            super(itemView);
            art = (SimpleDraweeView) itemView.findViewById(R.id.playlist_art);
            name = (TextView) itemView.findViewById(R.id.playlist_name);
            count = (TextView) itemView.findViewById(R.id.playlist_listen_count);
        }
    }


}
