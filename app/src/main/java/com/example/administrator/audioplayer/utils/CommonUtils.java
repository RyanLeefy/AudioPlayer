package com.example.administrator.audioplayer.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.administrator.audioplayer.R;

/**
 * Created by lipuyusx on 2017/2/6.
 */

public class CommonUtils {

    //检测各种版本

    //API23
    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
    //API21
    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
    //API18
    public static boolean isJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }
    //API17
    public static boolean isJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }
    //API16
    public static boolean isJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }



    //dp转px
    public static int dip2px(Context context,float dipVlue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float sDensity = metrics.density;
        return (int) (dipVlue * sDensity + 0.5F);//+0.5表示四舍五入
    }


    //把时间转换为00：00格式
    public static String makeTimeString(long milliSecs) {
        StringBuffer sb = new StringBuffer();
        long m = milliSecs / (60 * 1000);
        sb.append(m < 10 ? "0" + m : m);
        sb.append(":");
        long s = (milliSecs % (60 * 1000)) / 1000;
        sb.append(s < 10 ? "0" + s : s);
        return sb.toString();
    }


    /**
     * 检查是否有网络
     * @param Context
     * @return
     */
    public static boolean isConnectInternet(final Context Context) {
        final ConnectivityManager conManager = (ConnectivityManager) Context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }

        return false;
    }


    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }


    /**
     * 获取导航栏高度
     * @param context
     * @return
     */
    public static int getActionBarHeight(Context context) {
        int mActionBarHeight;
        TypedValue mTypedValue = new TypedValue();

        context.getTheme().resolveAttribute(R.attr.actionBarSize, mTypedValue, true);

        mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, context.getResources().getDisplayMetrics());

        return mActionBarHeight;
    }


    /**

     * @param activity 需要弹出的activity
     * @param popupWindow popupwindow实例
     * @param view 要出现的位置相关的view
     * @param layout popupwindow的样式
     */
    public static PopupWindow ShowPopUpWindow(final Activity activity, PopupWindow popupWindow, View view, LinearLayout layout) {
        //初始化并弹出popupWindow
        if (popupWindow != null && popupWindow.isShowing()) {
            return null;
        }

        popupWindow = new PopupWindow(layout,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.Popupwindow);//包括进入和退出两个动画

        //popupwindow获取焦点，点击其他地方消失
        popupWindow.setFocusable(true);
        //popupWindow.setOutsideTouchable(true);

        popupWindow.setBackgroundDrawable(new ColorDrawable());


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //popupwindow消失的时候恢复成原来的透明度
                backgroundAlpha(activity, 1f);
            }
        });

        backgroundAlpha(activity, 0.7f);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        //返回显示的popupwindow实例，以供dismiss
        return popupWindow;
    }


    /**
     * 设置添加屏幕的背景透明度
     * @param activity
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity activity, float bgAlpha)
    {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }


}
