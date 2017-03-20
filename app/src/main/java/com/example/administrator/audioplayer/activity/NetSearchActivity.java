package com.example.administrator.audioplayer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.db.SearchHistory;
import com.example.administrator.audioplayer.fragment.AfterSearchFragment;
import com.example.administrator.audioplayer.fragment.PreSearchFragment;
import com.example.administrator.audioplayer.utils.ActivityManager;
import com.example.administrator.audioplayer.utils.RequestThreadPool;

/**
 * 网络搜索activity，里面有两个fragment 没搜索前显示PreSearchFragment，搜索后显示AfterSearchFragment
 */
public class NetSearchActivity extends BaseActivity implements  SearchView.OnQueryTextListener, SearchCallBack  {


    private SearchView mSearchView;

    private InputMethodManager mimm;

    private FragmentTransaction transaction;

    private FragmentManager fragmentManager;

    private PreSearchFragment preSearchFragment;

    private AfterSearchFragment afterSearchFragment;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, NetSearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //把Activity放入ActivityManager中进行管理，方便一键退出所有
        ActivityManager.getInstance().pushOneActivity(this);

        setContentView(R.layout.activity_net_search);
        //初始化底部播放栏，由父类BaseActivity在onStart()中显示
        bottom_container_framelayout = (FrameLayout) findViewById(R.id.bottom_container);

        setToolbar();

        mSearchView = (SearchView)findViewById(R.id.sv_net_search_fragment);
        //默认展开
        mSearchView.setIconified(false);
        //默认搜索图标再外面
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setQueryHint("搜索歌曲、歌手、专辑");
        mSearchView.setOnQueryTextListener(this);

        mSearchView.clearFocus();

        mimm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        //两个Fragment  PreSearchFragment， AfterSearchFragment
        preSearchFragment = new PreSearchFragment();
        preSearchFragment.setSearchCallBack(this);

        //afterSearchFragment = new AfterSearchFragment();
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.netsearch_fragment_container, preSearchFragment);
        transaction.commit();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //如果直接退出的话，Toolbar会有一写偏移问题，但是按返回键不会有，所以调用返回键来退出
        if(item.getItemId() == android.R.id.home) {
            if(mimm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0)) {
                //软键盘已弹出，调用两次返回键
                super.onBackPressed();
                this.finish();
            } else {
                //软键盘未弹出，调用一次返回键
                super.onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_net_search_fragment);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }

    //type为1 显示搜索前页面， type为2 显示搜索后页面。
    public void ShowFragment(int type) {
        if (type == 1) {
            if(preSearchFragment != null) {
                fragmentManager.beginTransaction().replace(R.id.netsearch_fragment_container, preSearchFragment).commit();
            }

        } else if (type == 2) {
            if(afterSearchFragment != null) {
                fragmentManager.beginTransaction().replace(R.id.netsearch_fragment_container, afterSearchFragment).commit();
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        if(query == null || query.length() == 0) {
           //没有东西的时候啥都不做
        } else {
            //去到搜索后页面
            afterSearchFragment = AfterSearchFragment.newInstance(query);
            ShowFragment(2);
            mimm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
            //把搜索内容加入到搜索历史数据库中
            RequestThreadPool.post(new Runnable() {
                @Override
                public void run() {
                    SearchHistory.getInstance(NetSearchActivity.this).addSearchString(query);
                }
            });

        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    @Override
    public void Search(String query) {
        //查询，并在搜索框显示要查询的字段
        mSearchView.setQuery(query, true);
    }


}
