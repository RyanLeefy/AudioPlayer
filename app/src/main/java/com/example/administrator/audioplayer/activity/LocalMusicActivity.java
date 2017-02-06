package com.example.administrator.audioplayer.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.administrator.audioplayer.Iview.ILocalMusicView;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.LocalMusicAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);

        //两个Fragment  LocalMusicFragment， LocalSearchFragment
        localMusicFragment = new LocalMusicFragment();
        localSearchFragment = new LocalSearchFragment();
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.localmusic_fragment_container, localMusicFragment);
        transaction.commit();


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
