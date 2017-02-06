package com.example.administrator.audioplayer.activity;

import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.LeftMenuItemAdapter;
import com.example.administrator.audioplayer.adapter.MainPagerAdapter;
import com.example.administrator.audioplayer.fragment.MusicFragment;
import com.example.administrator.audioplayer.fragment.NetFragment;
import com.example.administrator.audioplayer.widget.SplashScreen;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;


/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Handler mHandler = new Handler();

    private ImageView img_menu,img_net, img_music, img_search;
    private ViewPager viewPager;

    private DrawerLayout drawer;
    private ListView left_menu;

    //用来保存两个导航图片，进行切换
    private ArrayList<ImageView> tabs = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final SplashScreen mSplashScreen = new SplashScreen(this);
        mSplashScreen.show(R.drawable.art_login_bg, SplashScreen.FADE_OUT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_menu = (ImageView) findViewById(R.id.bar_menu);
        img_net = (ImageView) findViewById(R.id.bar_net);
        img_music = (ImageView) findViewById(R.id.bar_music);
        img_search = (ImageView) findViewById(R.id.bar_search);

        viewPager = (ViewPager) findViewById(R.id.main_viewpager);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        left_menu = (ListView) findViewById(R.id.left_menu);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSplashScreen.dismiss();
            }
        }, 0);

        setToolbar();  //初始化toolbar
        setViewPager();  //初始化viewpager
        setListener();  //初始化监听事件
        setUpDrawer();  //初始化drawer
    }


    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //toolbar.setNavigationIcon(R.drawable.actionbar_menu);
        //ActionBar ab = getSupportActionBar();

        //ab.setDisplayHomeAsUpEnabled(true);
        //ab.setHomeAsUpIndicator(R.drawable.actionbar_menu);


    }


    private void setViewPager() {
        tabs.add(img_net);
        tabs.add(img_music);

        NetFragment netFragment = new NetFragment();
        MusicFragment musicFragment = new MusicFragment();

        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mainPagerAdapter.addFragment(netFragment);
        mainPagerAdapter.addFragment(musicFragment);

        viewPager.setAdapter(mainPagerAdapter);
        viewPager.setCurrentItem(0);
        img_net.setSelected(true);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchTabs(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void setListener() {
        img_menu.setOnClickListener(this);
        img_net.setOnClickListener(this);
        img_music.setOnClickListener(this);
        img_search.setOnClickListener(this);
    }

    private void setUpDrawer() {


        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.leftmenu_ban_bg);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout layout = new LinearLayout(this);
        layout.setFitsSystemWindows(true);
        layout.addView(imageView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);


        left_menu.addHeaderView(layout);
        left_menu.setAdapter(new LeftMenuItemAdapter(this));
        left_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        //听歌识曲
                        drawer.closeDrawers();
                        break;
                    case 2:
                        //定时关闭
                        drawer.closeDrawers();
                        break;
                    case 3:
                        //下载音质
                        drawer.closeDrawers();
                        break;
                    case 4:
                        //退出
                        drawer.closeDrawers();
                        break;
                }
            }
        });


    }


    //改变图标的颜色
    private void switchTabs(int position) {
        for(int i = 0; i < tabs.size(); i++) {
            if(i == position) {
                tabs.get(i).setSelected(true);
            } else {
                tabs.get(i).setSelected(false);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bar_menu:
                drawer.openDrawer(GravityCompat.START);
            case R.id.bar_net:
                viewPager.setCurrentItem(0);
                break;
            case R.id.bar_music:
                viewPager.setCurrentItem(1);
                break;
            case R.id.bar_search:
                //跳转到搜索activity
                break;
        }
    }


}
