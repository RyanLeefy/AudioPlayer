package com.example.administrator.audioplayer.utils;

/**
 * Created by on 2017/2/27 0027.
 */

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * 控件处理工具
 */
public class WidgetUtils {

    /**
     * 获取控件宽度
     * @param view
     * @return
     */
    public static int getWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredWidth());
    }

    /**
     * 获取控件高度
     * @param view
     * @return
     */
    public static int getHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredHeight());
    }

    /**
     * 设置横坐标位置
     * @param view
     * @param x
     */
    public static void setLayoutX(View view, int x) {
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x, margin.topMargin, x + margin.width, margin.bottomMargin);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }

    /**
     * 设置纵坐标位置
     * @param view
     * @param y
     */
    public static void setLayoutY(View view, int y) {
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(margin.leftMargin, y, margin.rightMargin, y + margin.height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }


    /**
     * 设置控件的位置，xy为绝对位置，
     * @param view
     * @param x
     * @param y
     */
    public static void setLayout(View view, int x, int y) { //ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(group.getLayoutParams());
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x, y, 0, 0);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);

    }
}
