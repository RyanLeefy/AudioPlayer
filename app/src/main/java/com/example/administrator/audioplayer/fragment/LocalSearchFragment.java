package com.example.administrator.audioplayer.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.audioplayer.Ipresenter.ILocalSearchPresenter;
import com.example.administrator.audioplayer.Iview.ILocalSearchView;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.adapter.PopUpWindowMenuAdapter;
import com.example.administrator.audioplayer.bean.LeftMenuItem;
import com.example.administrator.audioplayer.bean.MusicInfo;
import com.example.administrator.audioplayer.presenterImp.LocalSearchPresenter;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.example.administrator.audioplayer.widget.DividerItemDecoration;
import com.example.administrator.audioplayer.widget.RecycleViewWithEmptyView;

import java.util.Arrays;


public class LocalSearchFragment extends BaseFragment implements ILocalSearchView, SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    //private InputMethodManager mImm;
    private Callback callback;
    private View view;
    private RecycleViewWithEmptyView rv;
    private ILocalSearchPresenter presenter;

    private LayoutInflater mInflater;
    private PopupWindow popupWindow;


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
        view =  inflater.inflate(R.layout.fragment_local_search, container, false);

        setToolbar();

        //mImm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        mSearchView = (SearchView) view.findViewById(R.id.sv_local_search_fragment);
        //默认展开
        mSearchView.setIconified(false);
        //默认搜索图标再外面
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setQueryHint("搜索本地歌曲");
        mSearchView.setOnQueryTextListener(this);


        rv = (RecycleViewWithEmptyView) view.findViewById(R.id.rv_local_search_fragment);

        View emptyview = view.findViewById(R.id.id_empty_view);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        rv.setEmptyView(emptyview);
        rv.setAdapter(null);

        presenter = new LocalSearchPresenter(this);

        return view;
    }


    public void setToolbar() {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tb_local_search_fragment);
        toolbar.setTitle("");

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                callback.showLocalMusicFragment();
        }
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        onQueryTextChange(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Toast.makeText(getActivity(), "textchange", Toast.LENGTH_SHORT).show();
        if(newText == null || newText.length() == 0) {
            rv.setAdapter(null);
        } else {
            presenter.performSearch(newText);
        }
        return true;
    }

    /*
    public void hideInputManager() {
        if (mSearchView != null) {
            if (mImm != null) {
                mImm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
            }
            mSearchView.clearFocus();


        }
    }*/

    @Override
    public void setAdapter(final MusicAdapter adapter) {
        //获取回来的adapter先设置监听事件，然后再设置给recycleView
        adapter.setOnMusicItemClickListener(new MusicAdapter.OnMusicItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
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
                final MusicInfo musicInfo = (MusicInfo) adapter.getList().get(position);
                popuptitle.setText("歌曲信息:");

                String albumname = musicInfo.getAlbumName();
                if(albumname == null || albumname.length() == 0) {
                    albumname = "暂无信息";
                }

                PopUpWindowMenuAdapter adapter = new PopUpWindowMenuAdapter(getActivity(),
                        Arrays.asList( new LeftMenuItem(R.drawable.icon_music_name, "歌曲 —— " + musicInfo.getMusicName()),
                                new LeftMenuItem(R.drawable.icon_music_artist, "歌手 —— " + musicInfo.getArtist()),
                                new LeftMenuItem(R.drawable.icon_music_album, "专辑 —— " + albumname),
                                new LeftMenuItem(R.drawable.icon_music_info, "时长 —— " + CommonUtils.makeTimeString(musicInfo.getDuration())
                                        + "                   " + "大小 —— " + (float) (Math.round((float) musicInfo.getSize() / (1024 * 1024) * 10)) / 10 + "M"),
                                new LeftMenuItem(R.drawable.icon_music_location, "位置 —— " + musicInfo.getData()),
                                new LeftMenuItem(R.drawable.icon_music_delete, "删除歌曲" )
                        ));
                popuplistview.setAdapter(adapter);
                //添加弹窗菜单点击事件

                popuplistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View childview, int childposition, long id) {

                        //点击第五个按钮，删除按钮，弹框确认是否删除
                        if(childposition == 5) {
                            new AlertDialog.Builder(getActivity()).setTitle("确定删除该歌曲吗？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //删除歌曲
                                            if(presenter.performMusicDelete(position)) {
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
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    public interface Callback {
        // TODO: Update argument type and name
        void showLocalMusicFragment();
    }
}
