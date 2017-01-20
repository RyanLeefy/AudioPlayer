package com.example.administrator.audioplayer.activity;

import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.widget.SplashScreen;


/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SplashScreen mSplashScreen = new SplashScreen(this);
        mSplashScreen.show(R.drawable.art_login_bg, SplashScreen.FADE_OUT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSplashScreen.dismiss();
            }
        }, 0);

        setToolbar();

    }


    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.actionbar_menu);

        //ab.setDisplayHomeAsUpEnabled(true);
        //ab.setHomeAsUpIndicator(R.drawable.actionbar_menu);


    }


}
