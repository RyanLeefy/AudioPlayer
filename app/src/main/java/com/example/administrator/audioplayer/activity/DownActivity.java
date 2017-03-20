package com.example.administrator.audioplayer.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.administrator.audioplayer.Ipresenter.IDownPresenter;
import com.example.administrator.audioplayer.Iview.IDownView;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.DownLoadAdapter;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.download.DownloadService;
import com.example.administrator.audioplayer.presenterImp.DownPresenter;
import com.example.administrator.audioplayer.utils.PrintLog;
import com.example.administrator.audioplayer.widget.DividerItemDecoration;
import com.example.administrator.audioplayer.widget.RecycleViewWithEmptyView;

/**
 * 下载管理界面
 */
public class DownActivity extends BaseActivity implements IDownView {

    private ActionBar ab;
    private Toolbar toolbar;
    private RecycleViewWithEmptyView rv;

    private DownLoadAdapter adapter;

    private IDownPresenter presenter;

    private DownStatusBroadcastReceiver receiver;



    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DownActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down);

        //初始化底部播放栏，由父类BaseActivity在onStart()中显示
        bottom_container_framelayout = (FrameLayout) findViewById(R.id.bottom_container);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar();


        rv = (RecycleViewWithEmptyView) findViewById(R.id.rv_down_activity);

        View emptyview = findViewById(R.id.id_empty_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));


        rv.setEmptyView(emptyview);

        presenter = new DownPresenter(this);
        presenter.onCreate();
        //rv.setAdapter(new MusicAdapter(this, null));
    }


    /**
     * 初始化toolbar
     */
    private void setToolbar() {
        toolbar.setTitle("下载管理");
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

    /**
     * 注册监听器，监听下载的状态
     */
    @Override
    public void onStart() {
        super.onStart();
        receiver = new DownStatusBroadcastReceiver();
        IntentFilter f = new IntentFilter();
        f.addAction(DownloadService.TASK_STARTDOWN);
        f.addAction(DownloadService.UPDATE_DOWNSTAUS);
        f.addAction(DownloadService.TASKS_CHANGED);
        registerReceiver(receiver, new IntentFilter(f));
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    @Override
    public void setAdapter(DownLoadAdapter adapter) {
        adapter.setOnHeaderItemClickListener(new DownLoadAdapter.OnHeaderItemClickListener() {
            @Override
            public void onStartOrStopAllDownLoadClick(RecyclerView.ViewHolder holder, int position) {
                DownLoadAdapter.HeaderViewHolder viewHolder = (DownLoadAdapter.HeaderViewHolder) holder;
                if(DownloadService.getPrepareTasks().size() > 0) {
                    //如果目前下载任务大于0，则暂停
                    Intent intent = new Intent(DownloadService.PAUSE_ALLTASK);
                    intent.setPackage(DownloadService.PACKAGE);
                    DownActivity.this.startService(intent);
                    //文字和图片变成开始
                    viewHolder.tv_download_state.setText("全部开始");
                    viewHolder.img_download_state.setImageDrawable(getResources().getDrawable(R.drawable.icon_download));
                } else {
                    //否则开始
                    Intent intent = new Intent(DownloadService.START_ALL_DOWNTASK);
                    intent.setPackage(DownloadService.PACKAGE);
                    DownActivity.this.startService(intent);
                    //文字和图片变成开始
                    viewHolder.tv_download_state.setText("全部暂停");
                    viewHolder.img_download_state.setImageDrawable(getResources().getDrawable(R.drawable.icon_pause_download));
                }
            }

            @Override
            public void onClearAllTaskClick(RecyclerView.ViewHolder holder, int position) {
                    //如果目前下载任务大于0，则暂停
                    new AlertDialog.Builder(DownActivity.this).setTitle("要清楚所有下载任务吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(DownloadService.CANCLE_ALL_DOWNTASK);
                                    intent.setPackage(DownloadService.PACKAGE);
                                    DownActivity.this.startService(intent);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
            }
        });

        adapter.setOnItemClickListener(new DownLoadAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                DownLoadAdapter.CommonViewHolder viewHolder = (DownLoadAdapter.CommonViewHolder)holder;
                if (viewHolder.isPreparing) {
                    //如果是在当前的下载队列中的，即准备要进行下载的，点击的话会暂停任务
                    PrintLog.d(TAG, "isprepaing");
                    Intent intent = new Intent(DownloadService.PAUSE_TASK);
                    intent.setPackage(DownloadService.PACKAGE);
                    intent.putExtra("downloadid", viewHolder.info.getDownloadId());
                    DownActivity.this.startService(intent);
                } else {
                    //如果是不在当前下载队列中的，即已经暂停了的，点击会恢复任务
                    PrintLog.d(TAG, "not isprepaing");
                    Intent intent = new Intent(DownloadService.RESUME_START_DOWNTASK);
                    intent.setPackage(DownloadService.PACKAGE);
                    intent.putExtra("downloadid", viewHolder.info.getDownloadId());
                    DownActivity.this.startService(intent);
                }
            }

            @Override
            public void onDeleteClick(RecyclerView.ViewHolder holder, int position) {
                final DownLoadAdapter.CommonViewHolder viewHolder = (DownLoadAdapter.CommonViewHolder)holder;
                //弹窗询问是否要删除任务
                new AlertDialog.Builder(DownActivity.this).setTitle("要删除该下载任务吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除该任务
                                Intent intent = new Intent(DownloadService.CANCLE_DOWNTASK);
                                intent.putExtra("downloadid", viewHolder.info.getDownloadId());
                                intent.setPackage(DownloadService.PACKAGE);
                                DownActivity.this.startService(intent);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        this.adapter = adapter;
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    /**
     * 广播接受器，监听下载的状态，刷新adapter
     */
    private class DownStatusBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case DownloadService.UPDATE_DOWNSTAUS:
                    adapter.notifyItem(intent.getLongExtra("completesize", 0), intent.getLongExtra("totalsize", -1));
                    break;
                case DownloadService.TASK_STARTDOWN:
                    adapter.notifyItem(intent.getLongExtra("completesize", 0), intent.getLongExtra("totalsize", -1));
                    break;
                case DownloadService.TASKS_CHANGED:
                    //重新加载数据，显示视图
                    adapter.updateAdapter(DownloadService.getPrepareTasks());
                    break;

            }
        }
    }

}
