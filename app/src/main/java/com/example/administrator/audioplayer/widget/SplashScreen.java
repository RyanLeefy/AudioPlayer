package com.example.administrator.audioplayer.widget;


import android.app.Activity;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.administrator.audioplayer.R;

/**
 * 引导页图片，停留若干秒然后自动消失
 */
public class SplashScreen {

    public static final int SLIDE_LEFT = 1;
    public static final int SLIDE_UP = 2;
    public static final int FADE_OUT = 3;

    private Dialog mSplashDialog;

    private Activity mActivity;

    public SplashScreen(Activity activity) {
        this.mActivity = activity;
    }

    public void show(final int imageResource, final int animation) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //获取屏幕数据
                DisplayMetrics metrics = new DisplayMetrics();
                mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

                //设置dialog的layout
                LinearLayout layout = new LinearLayout(mActivity);
                layout.setMinimumHeight(metrics.heightPixels);
                layout.setMinimumWidth(metrics.widthPixels);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setBackgroundResource(imageResource);

                //创建dialog
                mSplashDialog = new Dialog(mActivity, android.R.style.Theme_NoTitleBar_Fullscreen);
                //获取dialog的window，设置动画
                Window window = mSplashDialog.getWindow();
                switch (animation) {
                    case SLIDE_LEFT:
                        window.setWindowAnimations(R.style.dialog_anim_slide_left);
                        break;
                    case SLIDE_UP:
                        window.setWindowAnimations(R.style.dialog_anim_slide_up);
                        break;
                    case FADE_OUT:
                        window.setWindowAnimations(R.style.dialog_anim_fade_out);
                        break;
                }

                mSplashDialog.setContentView(layout);
                mSplashDialog.setCancelable(false);
                //弹出dialog
                mSplashDialog.show();

            }
        };

        mActivity.runOnUiThread(runnable);
    }

    public void dismiss() {
        if (mSplashDialog != null && mSplashDialog.isShowing()) {
            mSplashDialog.dismiss();
            mSplashDialog = null;
        }
    }


}
