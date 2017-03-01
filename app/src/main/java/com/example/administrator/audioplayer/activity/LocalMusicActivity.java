package com.example.administrator.audioplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.fragment.LocalMusicFragment;
import com.example.administrator.audioplayer.fragment.LocalSearchFragment;

/**
 * 本地音乐界面
 */
public class LocalMusicActivity extends BaseActivity
        implements LocalMusicFragment.Callback, LocalSearchFragment.Callback {


    private FragmentTransaction transaction;

    private FragmentManager fragmentManager;

    private LocalMusicFragment localMusicFragment;

    private LocalSearchFragment localSearchFragment;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LocalMusicActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);

        //初始化底部播放栏，由父类BaseActivity在onStart()中显示
        bottom_container_framelayout = (FrameLayout) findViewById(R.id.bottom_container);

        //两个Fragment  LocalMusicFragment， LocalSearchFragment

        localSearchFragment = new LocalSearchFragment();
        fragmentManager = getSupportFragmentManager();
        //如果还没有初始化fragment，就初始化fragment并放入container中
        if(localMusicFragment == null) {
            transaction = fragmentManager.beginTransaction();
            localMusicFragment = new LocalMusicFragment();
            transaction.add(R.id.localmusic_fragment_container, localMusicFragment);
            transaction.commit();
        }

    }


    //type为1 显示本地音乐界面， type为2 显示本地音乐搜索页面。
    public void ShowFragment(int type) {
        if (type == 1) {
            if(localMusicFragment != null) {
                fragmentManager.beginTransaction().replace(R.id.localmusic_fragment_container, localMusicFragment).commit();
            }

        } else if (type == 2) {
            if(localSearchFragment != null) {
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.localmusic_fragment_container, localSearchFragment).commit();
            }
        }
    }

    @Override
    public void showLocalSearchFragment() {
        ShowFragment(2);
    }


    @Override
    public void showLocalMusicFragment() {
        ShowFragment(1);
    }



}
