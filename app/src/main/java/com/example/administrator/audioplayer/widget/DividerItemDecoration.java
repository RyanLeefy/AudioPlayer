package com.example.administrator.audioplayer.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.adapter.SongListAdapter;
import com.example.administrator.audioplayer.utils.CommonUtils;

/**
 * Created by on 2017/1/24.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    private Drawable mDivider;

    private int mOrientation;

    private Context mContext;

    public DividerItemDecoration(Context context, int orientation) {
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }


    public void drawVertical(Canvas c, RecyclerView parent) {
        //final int left = parent.getPaddingLeft();
        int left = 0;
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            //根据不同的视图画不同的线

            //MusicFragment里面线
            if(parent.getChildViewHolder(child) instanceof SongListAdapter.ItemViewTag) {
                SongListAdapter.ItemViewTag itemViewTag = (SongListAdapter.ItemViewTag)parent.getChildViewHolder(child);

                //最后一个头部headerItem不画分割线
                if (parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(child)) == 0 && i == 3) {
                    //left = parent.getPaddingLeft() + itemViewTag.icon.getWidth() +  itemViewTag.icon.getPaddingLeft();
                    continue;
                }
                //其他头部headerItem画分隔线
                if (parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(child)) == 0) {
                    left = parent.getPaddingLeft() + itemViewTag.icon.getWidth() +  itemViewTag.icon.getPaddingLeft();
                    //continue;
                }

                //如果是创建的最后一个歌单和收藏的最后一个歌单则不画下面的分割线
                if ((parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(child)) == 1
                        && i < childCount - 1
                        && parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(parent.getChildAt(i + 1))) == 3)
                        || (parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(child)) == 1 && (i == childCount - 1)) ) {
                    //left = parent.getPaddingLeft() + itemViewTag.cover.getWidth() +  itemViewTag.cover.getPaddingLeft();
                    continue;
                }
                //其他歌单画分割线
                if (parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(child)) == 1 ) {
                    left = parent.getPaddingLeft() + itemViewTag.cover.getWidth() +  CommonUtils.dip2px(MyApplication.getContext(), 20);
                    //continue;
                }
                //创建的歌单expanditem不画线
                if (parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(child)) == 2 ) {
                    //left = parent.getPaddingLeft();
                    continue;
                }
                //收藏的歌单expanditem不画线
                if (parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(child)) == 3 ) {
                    //left = parent.getPaddingLeft();
                    continue;
                }
            }
            //本地播放里面第一个播放全部的item
            if(parent.getChildViewHolder(child) instanceof MusicAdapter.PlayAllItemViewHolder) {
                left = parent.getPaddingLeft();
            }
            //本地播放里面其他歌曲item
            if(parent.getChildViewHolder(child) instanceof MusicAdapter.MusicItemViewHolder) {
                MusicAdapter.MusicItemViewHolder viewHolder = (MusicAdapter.MusicItemViewHolder)parent.getChildViewHolder(child);
                left = parent.getPaddingLeft() + CommonUtils.dip2px(MyApplication.getContext(), 13);
            }



            android.support.v7.widget.RecyclerView v = new android.support.v7.widget.RecyclerView(parent.getContext());
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }


    //获取偏移量，即画线的时候对item进行移位
    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            View child = parent.getChildAt(itemPosition);
            //SongListAdapter.ItemViewTag itemViewTag = (SongListAdapter.ItemViewTag)parent.getChildViewHolder(child);
            if (parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(child)) == 0 ) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else if (parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(child)) == 1 ) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else if (parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(child)) == 2 ) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }



}
