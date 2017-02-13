package com.example.administrator.audioplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.audioplayer.Ipresenter.IRecentPresenter;
import com.example.administrator.audioplayer.Iview.IRecentView;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.presenterImp.RencentPresenter;
import com.example.administrator.audioplayer.widget.DividerItemDecoration;
import com.example.administrator.audioplayer.widget.RecycleViewWithEmptyView;


/**
 * 最近播放界面
 */
public class RecentActivity extends BaseActivity implements IRecentView {

    private ActionBar ab;
    private Toolbar toolbar;
    private RecycleViewWithEmptyView rv;
    private IRecentPresenter presenter;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RecentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar();


        rv = (RecycleViewWithEmptyView) findViewById(R.id.rv_recent_activity);

        View emptyview = findViewById(R.id.id_empty_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));


        rv.setEmptyView(emptyview);
        rv.setAdapter(new MusicAdapter(this, null));


        presenter = new RencentPresenter(this);
        presenter.onCreateView();
    }


    /**
     * 初始化toolbar
     */
    private void setToolbar() {
        toolbar.setTitle("最近播放");
        setSupportActionBar(toolbar);

        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return true;
    }


    @Override
    public void setAdapter(MusicAdapter adapter) {
        //获取回来的adapter先设置监听事件，然后再设置给recycleView
        adapter.setOnPlayAllItemClickListener(new MusicAdapter.OnPlayAllItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(RecentActivity.this, "播放全部", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMoreClick(View view, int position) {
                Toast.makeText(RecentActivity.this, "更多", Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnMusicItemClickListener(new MusicAdapter.OnMusicItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //获取点击的MusicInfo实体类((LocalMusicAdapter)rv.getAdapter()).getItem(position)
                //调用presenter的方法，播放该实体代表的歌曲

                presenter.pefromMusicClick(position);

                Toast.makeText(RecentActivity.this, ((MusicAdapter)rv.getAdapter()).getItem(position).getMusicName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMoreClick(View view, int position) {
                Toast.makeText(RecentActivity.this, ((MusicAdapter)rv.getAdapter()).getItem(position).getMusicName() + "more", Toast.LENGTH_SHORT).show();
            }
        });

        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
