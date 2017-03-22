package com.example.administrator.audioplayer.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.audioplayer.Ipresenter.ILocalMusicPresenter;
import com.example.administrator.audioplayer.Iview.ILocalMusicView;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.adapter.PopUpWindowMenuAdapter;
import com.example.administrator.audioplayer.bean.LeftMenuItem;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.presenterImp.LocalMusicPresenter;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.example.administrator.audioplayer.widget.DividerItemDecoration;
import com.example.administrator.audioplayer.widget.RecycleViewWithEmptyView;

import java.lang.reflect.Method;
import java.util.Arrays;


public class LocalMusicFragment extends BaseFragment implements ILocalMusicView {

    private Callback callback;
    private View view;
    private RecycleViewWithEmptyView rv;
    private ILocalMusicPresenter presenter;

    private LayoutInflater mInflater;
    private PopupWindow popupWindow;

    public LocalMusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            callback = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //菜单生效
        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = LayoutInflater.from(getActivity());
        view =  inflater.inflate(R.layout.fragment_local_music, container, false);

        setToolbar();

        rv = (RecycleViewWithEmptyView) view.findViewById(R.id.rv_local_music_fragment);

        View emptyview = view.findViewById(R.id.id_empty_view);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        //rv.setAdapter(null);
        rv.setEmptyView(emptyview);

        presenter = new LocalMusicPresenter(this);
        presenter.onCreateView();

        return view;
    }



    public void setToolbar() {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tb_local_music_fragment);
        toolbar.setTitle("本地播放");

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.actionbar_search:
                callback.showLocalSearchFragment();
                break;
            case android.R.id.home:
                getActivity().finish();
        }
        return true;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }



    //重写该方法，使MenuItem里面的图标可以显示出来
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (menu != null&& menu instanceof MenuBuilder) {
            //编sdk版本24的情况 可以直接使用 setOptionalIconsVisible
            if (Build.VERSION.SDK_INT > 23) {
                MenuBuilder builder = (MenuBuilder) menu;
                builder.setOptionalIconsVisible(true);
            } else {
                //sdk版本24的以下，需要通过反射去执行该方法
                try {
                    MenuBuilder builder = (MenuBuilder) menu;
                    Method m =  builder.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void setAdapter(final MusicAdapter adapter) {
       //获取回来的adapter先设置监听事件，然后再设置给recycleView
        adapter.setOnPlayAllItemClickListener(new MusicAdapter.OnPlayAllItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                presenter.peformMusicClick(0);
            }

            @Override
            public void onMoreClick(View view, int position) {
                //Toast.makeText(getActivity(), "更多", Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnMusicItemClickListener(new MusicAdapter.OnMusicItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //获取点击的MusicInfo实体类((LocalMusicAdapter)rv.getAdapter()).getItem(position)
                //调用presenter的方法，播放该实体代表的歌曲

                presenter.peformMusicClick(position);

                Toast.makeText(getActivity(), ((MusicAdapter)rv.getAdapter()).getItem(position).getMusicName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMoreClick(View view, final int position) {
                LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.popupwindow_menu_songlistmore, null);

                TextView popuptitle = (TextView) layout.findViewById(R.id.tv_title_popupwindow);
                ListView popuplistview = (ListView) layout.findViewById(R.id.ls_songlistmore_popupwindow);

                //添加弹窗菜单数据源
                //获取当前点击的歌曲数据
                final MusicInfo musicInfo = (MusicInfo) adapter.getList().get(position - 1);
                popuptitle.setText("歌曲信息:");

                String albumname = musicInfo.getAlbumName();
                //判断专辑名，如果是没有专辑名字的 在下载之后专辑会变成文件夹名字，所以这里多一步判断是否是文件夹名字
                if(albumname == null || albumname.length() == 0 || albumname.equals("audioplayer")) {
                    albumname = "暂无信息";
                }

                PopUpWindowMenuAdapter adapter = new PopUpWindowMenuAdapter(getActivity(),
                        Arrays.asList( new LeftMenuItem(R.drawable.icon_music_name, "歌曲 —— " + musicInfo.getMusicName()),
                                new LeftMenuItem(R.drawable.icon_music_artist, "歌手 —— " + musicInfo.getArtist()),
                                new LeftMenuItem(R.drawable.icon_music_album, "专辑 —— " + albumname),
                                new LeftMenuItem(R.drawable.icon_music_info, "时长 —— " + CommonUtils.makeTimeString(musicInfo.getDuration())
                                        + "                   " + "大小 —— " + (float) (Math.round((float) musicInfo.getSize() / (1024 * 1024) * 10)) / 10 + "M"),
                                new LeftMenuItem(R.drawable.icon_music_location, "位置 —— " + musicInfo.getData()),
                                new LeftMenuItem(R.drawable.icon_music_collect, "收藏到歌单"),
                                new LeftMenuItem(R.drawable.icon_music_delete, "删除歌曲" )
                        ));
                popuplistview.setAdapter(adapter);
                //添加弹窗菜单点击事件

                popuplistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View childview, int childposition, long id) {

                        //点击第6个按钮，收藏到歌单，弹出fragment选择添加到的歌单
                        if(childposition == 5) {
                            //TODO 收藏
                            if (popupWindow != null) {
                                popupWindow.dismiss();
                            }
                            ChooseCollectionFragment chooseCollectionFragment = ChooseCollectionFragment.newInstance(musicInfo);
                            chooseCollectionFragment.show(getFragmentManager(), "chooseCollection");
                        }

                        //点击第7个按钮，删除按钮，弹框确认是否删除
                        if(childposition == 6) {
                            new AlertDialog.Builder(getActivity()).setTitle("确定删除该歌曲吗？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //删除歌曲
                                            if(presenter.performMusicDelete(position - 1)) {
                                                Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                                            }
                                            if (popupWindow != null) {
                                                popupWindow.dismiss();
                                            }
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();

                        }
                    }
                });
                //初始化并弹出popupWindow
                popupWindow = CommonUtils.ShowPopUpWindow(getActivity(), popupWindow, view, layout);
            }
        });

        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.onCreateView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }


    /**
     * 重写该方法，在baseActivity中接受到个歌曲信息变化时候调用
     */
    @Override
    public void onMetaChange() {
        //重新载入数据
        presenter.onCreateView();
    }

    @Override
    public void onPlayStateChange() {
        presenter.onCreateView();
    }


    public interface Callback {
        // TODO: Update argument type and name
        void showLocalSearchFragment();
    }
}
