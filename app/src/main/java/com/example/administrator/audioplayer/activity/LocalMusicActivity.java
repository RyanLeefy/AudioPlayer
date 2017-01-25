package com.example.administrator.audioplayer.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.fragment.LocalMusicFragment;
import com.example.administrator.audioplayer.fragment.LocalSearchFragment;

/**
 * 本地音乐界面
 */
public class LocalMusicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);

        //两个Fragment  LocalMusicFragment， LocalSearchFragment
        LocalMusicFragment localMusicFragment = new LocalMusicFragment();
        LocalSearchFragment localSearchFragment = new LocalSearchFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(localMusicFragment, "localmusic");
        transaction.add(localSearchFragment, "localsearch");
        transaction.commit();



    }
}
