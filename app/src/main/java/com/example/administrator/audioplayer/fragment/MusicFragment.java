package com.example.administrator.audioplayer.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.audioplayer.Ipresenter.IMusicPresenter;
import com.example.administrator.audioplayer.Iview.IMusicView;
import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.activity.CreatedCollectionActivity;
import com.example.administrator.audioplayer.activity.DownActivity;
import com.example.administrator.audioplayer.activity.LocalMusicActivity;
import com.example.administrator.audioplayer.activity.NewAlbumActivity;
import com.example.administrator.audioplayer.activity.RecentActivity;
import com.example.administrator.audioplayer.activity.SongCollectionActivity;
import com.example.administrator.audioplayer.adapter.PopUpWindowMenuAdapter;
import com.example.administrator.audioplayer.adapter.SongListAdapter;
import com.example.administrator.audioplayer.bean.CollectionInfo;
import com.example.administrator.audioplayer.bean.LeftMenuItem;
import com.example.administrator.audioplayer.bean.MusicFragmentExpandItem;
import com.example.administrator.audioplayer.presenterImp.MusicPresenter;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.example.administrator.audioplayer.widget.DividerItemDecoration;

import java.util.Arrays;
import java.util.List;

/**
 * 主界面第二个fragment界面，本地音乐信息
 * Created on 2017/1/22.
 */

public class MusicFragment extends BaseFragment implements IMusicView {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Context mContext;
    private LayoutInflater mInflater;
    private PopupWindow popupWindow;

    //收藏歌单栏展开否
    public boolean collectExpanded = true;

    private IMusicPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_music, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_musicfragment);


        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //模拟请求，两秒后取消
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));


        presenter = new MusicPresenter(this);
        presenter.onCreateView();

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.onCreateView();
    }


    @Override
    public void setAdapter(final SongListAdapter adapter,
                           final List allItems,
                           final List<CollectionInfo> create_songCollectionItems,
                           final List<CollectionInfo> collect_songCollectionItems,
                           final List<MusicFragmentExpandItem> expandItemList) {

        //给headeritem添加点击事件
        adapter.setOnHeaderItemClickListener(new SongListAdapter.OnHeaderItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        //跳转到LocalMusicActivity
                        LocalMusicActivity.startActivity(mContext);
                        break;
                    case 1:
                        //跳转到RecentActivity
                        RecentActivity.startActivity(mContext);
                        break;
                    case 2:
                        //跳转到DownActivity
                        DownActivity.startActivity(mContext);
                        break;
                    //case 3:
                    //跳转到MyArtistActivity
                    //break;
                }
            }
        });
        //给expanditem添加点击事件
        adapter.setOnExpandItemClickListener(new SongListAdapter.OnExpandItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {

                SongListAdapter.ItemViewTag itemViewTag = (SongListAdapter.ItemViewTag) holder;
                ObjectAnimator anim = ObjectAnimator.ofFloat(itemViewTag.arrow, "rotation", 90, 0);
                anim.setDuration(100);
                anim.setRepeatCount(0);
                anim.setInterpolator(new LinearInterpolator());

                if (itemViewTag.getItemViewType() == 2) {            //判断类型，创建的歌单
                    //已展开则合上
                    if (itemViewTag.createdExpanded) {
                        anim.start();
                        //TODO 刷新列表
                        allItems.removeAll(create_songCollectionItems);
                        //adapter.removeCollecteSongList(collect_songCollectionItems);
                        itemViewTag.createdExpanded = false;
                    } else {
                        //未展开则展开
                        //TODO 刷新列表
                        anim.reverse();
                        //这里要分情况，分收藏的歌单是否打开
                        //若打开了，则要先去掉收藏的歌单，最后再添加上  否则不用处理收藏的歌单
                        //这里根据本地的collectExpanded变量来进行判断，因为无法获取到下一个expanditem里面的collectExpanded状态
                        if (collectExpanded) {
                            allItems.removeAll(collect_songCollectionItems);
                            allItems.remove(expandItemList.get(1));
                            allItems.addAll(create_songCollectionItems);
                            allItems.add(expandItemList.get(1));
                            allItems.addAll(collect_songCollectionItems);
                        } else {
                            allItems.remove(expandItemList.get(1));
                            allItems.addAll(create_songCollectionItems);
                            allItems.add(expandItemList.get(1));
                        }
                        itemViewTag.createdExpanded = true;
                    }
                } else if (itemViewTag.getItemViewType() == 3) {     //判断类型，收藏的歌单
                    //已展开则合上
                    //此处把collectExpanded状态存至本地变量中
                    collectExpanded = itemViewTag.collectExpanded;
                    if (collectExpanded) {
                        anim.start();
                        //TODO 刷新列表
                        allItems.removeAll(collect_songCollectionItems);
                        itemViewTag.collectExpanded = false;
                        //同步本地状态
                        collectExpanded = false;
                    } else {
                        //未展开则展开
                        anim.reverse();
                        //TODO 刷新列表
                        allItems.addAll(collect_songCollectionItems);
                        itemViewTag.collectExpanded = true;
                        //同步本地状态
                        collectExpanded = true;
                    }
                }
                adapter.updateAdapter(allItems);   //更新数据
                adapter.notifyDataSetChanged();    //刷新界面
            }

            @Override
            public void onMoreClick(View view, int position) {
                LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.popupwindow_menu_songlistmore, null);

                TextView popuptitle = (TextView) layout.findViewById(R.id.tv_title_popupwindow);
                ListView popuplistview = (ListView) layout.findViewById(R.id.ls_songlistmore_popupwindow);

                //添加弹窗菜单数据源
                MusicFragmentExpandItem musicFragmentExpandItem = (MusicFragmentExpandItem) allItems.get(position);
                popuptitle.setText(musicFragmentExpandItem.getTitle());
                final PopUpWindowMenuAdapter adapter = new PopUpWindowMenuAdapter(mContext,
                        Arrays.asList(new LeftMenuItem(R.drawable.popupwindow_menu_createlist, "创建新歌单")));
                popuplistview.setAdapter(adapter);
                //添加弹窗菜单点击事件
                popuplistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //点击创建新歌单
                        if (position == 0) {
                            if (popupWindow != null) {
                                popupWindow.dismiss();
                            }
                            //创建新歌单的弹框视图
                            View newCollection_dialogView = mInflater.inflate(R.layout.layout_create_new_collection, null, false);
                            final EditText editText = (EditText) newCollection_dialogView.findViewById(R.id.et_collection_name);
                            //弹出新建歌单窗口
                            new AlertDialog.Builder(mContext).setTitle("创建新歌单")
                                    .setView(newCollection_dialogView)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (editText.getText().toString().length() == 0) {
                                                Toast.makeText(mContext, "歌单名不能为空", Toast.LENGTH_SHORT).show();
                                            } else {
                                                //创建歌单，插入数据库
                                                if (presenter.createNewCollection(editText.getText().toString())) {
                                                    Toast.makeText(mContext, "创建成功", Toast.LENGTH_SHORT).show();
                                                    //刷新页面
                                                    presenter.onCreateView();
                                                } else {
                                                    Toast.makeText(mContext, "已有同名歌单，创建失败", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                        }
                    }
                });
                //初始化并弹出popupWindow
                popupWindow = CommonUtils.ShowPopUpWindow(getActivity(), popupWindow, view, layout);
            }
        });
        //给歌单添加点击事件
        adapter.setOnSongCollectionItemClickListener(new SongListAdapter.OnSongCollectionItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //跳转到歌单里面
                CollectionInfo songCollectionItem = (CollectionInfo) allItems.get(position);
                switch (songCollectionItem.getType()) {
                    //我喜欢的音乐
                    case 0:
                        CreatedCollectionActivity.startActivity(
                                MyApplication.getContext(),
                                songCollectionItem.getId(),
                                songCollectionItem.getAlbumArt(),
                                songCollectionItem.getCollectionName());
                        break;
                    //其他自建歌单
                    case 1:
                        CreatedCollectionActivity.startActivity(
                                getActivity(),
                                songCollectionItem.getId(),
                                songCollectionItem.getAlbumArt(),
                                songCollectionItem.getCollectionName());
                        break;
                    //收藏的网络歌单
                    case 2:
                        SongCollectionActivity.startActivity(
                                getActivity(),
                                false,
                                String.valueOf(songCollectionItem.getListId()),
                                songCollectionItem.getAlbumArt(),
                                songCollectionItem.getListenCount(),
                                songCollectionItem.getCollectionName(),
                                songCollectionItem.getCollectionTag());
                        break;
                    //收藏的网络专辑
                    case 3:
                        NewAlbumActivity.startActivity(
                                getActivity(),
                                String.valueOf(songCollectionItem.getListId()),
                                songCollectionItem.getAlbumArt(),
                                songCollectionItem.getCollectionName(),
                                songCollectionItem.getAuthor(),
                                songCollectionItem.getPublishTime());
                        break;
                }
            }

            @Override
            public void onMoreClick(View view, final int position) {
                LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.popupwindow_menu_songlistmore, null);

                TextView popuptitle = (TextView) layout.findViewById(R.id.tv_title_popupwindow);
                ListView popuplistview = (ListView) layout.findViewById(R.id.ls_songlistmore_popupwindow);

                //添加弹窗菜单数据源
                final CollectionInfo songCollectionItems = (CollectionInfo) allItems.get(position);
                popuptitle.setText("歌单:  " + songCollectionItems.getCollectionName());

                //如果是我喜欢的音乐，则没有删除,如果是其他自创建的则有删除也有修改，若是网络的则只有删除
                PopUpWindowMenuAdapter popupwindowadapter = null;
                if (songCollectionItems.getType() == 0) {
                    popupwindowadapter = new PopUpWindowMenuAdapter(mContext,
                            Arrays.asList(new LeftMenuItem(R.drawable.popupwindow_menu_manage, "编辑歌单信息")));
                } else if(songCollectionItems.getType() == 1) {
                    popupwindowadapter = new PopUpWindowMenuAdapter(mContext,
                            Arrays.asList(new LeftMenuItem(R.drawable.popupwindow_menu_delete, "删除"),
                                    new LeftMenuItem(R.drawable.popupwindow_menu_manage, "编辑歌单信息")));
                } else {
                    popupwindowadapter = new PopUpWindowMenuAdapter(mContext,
                            Arrays.asList(new LeftMenuItem(R.drawable.popupwindow_menu_delete, "删除")));
                }

                popuplistview.setAdapter(popupwindowadapter);
                //添加弹窗菜单点击事件
                popuplistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View childview, int childposition, long id) {
                        //弹出编辑歌单
                        //如果是我喜欢的音乐，则没有删除点击事件,只有修改歌单信息
                        if (songCollectionItems.getType() == 0) {

                            if (childposition == 0) {
                                if (popupWindow != null) {
                                    popupWindow.dismiss();
                                }
                                //创建修改歌单弹框视图
                                View updateCollection_alertView = mInflater.inflate(R.layout.layout_update_collection_info, null, false);
                                final EditText editText2 = (EditText) updateCollection_alertView.findViewById(R.id.et_collection_name);
                                //设置输入框初始文字为原始歌单名，并把光标移到最后
                                editText2.setText(songCollectionItems.getCollectionName());
                                editText2.setSelection(songCollectionItems.getCollectionName().length());
                                //弹出修改歌单窗口
                                new AlertDialog.Builder(mContext).setTitle("修改歌单信息")
                                        .setView(updateCollection_alertView)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (editText2.getText().toString().length() == 0) {
                                                    Toast.makeText(mContext, "歌单名不能为空", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    //修改歌单，更新数据库
                                                    if (presenter.updateCollection(songCollectionItems.getId(), editText2.getText().toString())) {
                                                        Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                                                        //刷新页面
                                                        presenter.onCreateView();
                                                    } else {
                                                        Toast.makeText(mContext, "已有同名歌单，修改失败", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }

                        } else {
                            //如果是其他自建歌单，则有删除点击事件,也有修改歌单信息
                            if (childposition == 0) {
                                if (popupWindow != null) {
                                    popupWindow.dismiss();
                                }
                                //弹框是否删除
                                new AlertDialog.Builder(mContext).setTitle("确定删除 " + songCollectionItems.getCollectionName() + " 吗？")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                presenter.deleteCollection(songCollectionItems.getId());
                                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                                                //刷新页面
                                                presenter.onCreateView();
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();


                            } else if (childposition == 1) {
                                if (popupWindow != null) {
                                    popupWindow.dismiss();
                                }
                                //创建修改歌单弹框视图
                                View updateCollection_alertView = mInflater.inflate(R.layout.layout_update_collection_info, null, false);
                                final EditText editText2 = (EditText) updateCollection_alertView.findViewById(R.id.et_collection_name);
                                //设置输入框初始文字为原始歌单名，并把光标移到最后
                                editText2.setText(songCollectionItems.getCollectionName());
                                editText2.setSelection(songCollectionItems.getCollectionName().length());
                                //弹出修改歌单窗口
                                new AlertDialog.Builder(mContext).setTitle("修改歌单信息")
                                        .setView(updateCollection_alertView)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (editText2.getText().toString().length() == 0) {
                                                    Toast.makeText(mContext, "歌单名不能为空", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    //修改歌单，更新数据库
                                                    if (presenter.updateCollection(songCollectionItems.getId(), editText2.getText().toString())) {
                                                        Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                                                        //刷新页面
                                                        presenter.onCreateView();
                                                    } else {
                                                        Toast.makeText(mContext, "已有同名歌单，修改失败", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }
                        }

                    }

                });


                //初始化并弹出popupWindow
                popupWindow = CommonUtils.ShowPopUpWindow(getActivity(), popupWindow, view, layout);
            }
        });

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
