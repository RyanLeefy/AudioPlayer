package com.example.administrator.audioplayer.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import android.widget.Toast;

import com.example.administrator.audioplayer.Ipresenter.ILocalMusicPresenter;
import com.example.administrator.audioplayer.Iview.ILocalMusicView;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.presenterImp.LocalMusicPresenter;
import com.example.administrator.audioplayer.widget.DividerItemDecoration;
import com.example.administrator.audioplayer.widget.RecycleViewWithEmptyView;

import java.lang.reflect.Method;


public class LocalMusicFragment extends BaseFragment implements ILocalMusicView {

    private Callback callback;
    private View view;
    private RecycleViewWithEmptyView rv;
    private ILocalMusicPresenter presenter;

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
    public void setAdapter(MusicAdapter adapter) {
       //获取回来的adapter先设置监听事件，然后再设置给recycleView
        adapter.setOnPlayAllItemClickListener(new MusicAdapter.OnPlayAllItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), "播放全部", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMoreClick(View view, int position) {
                Toast.makeText(getActivity(), "更多", Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnMusicItemClickListener(new MusicAdapter.OnMusicItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //获取点击的MusicInfo实体类((LocalMusicAdapter)rv.getAdapter()).getItem(position)
                //调用presenter的方法，播放该实体代表的歌曲

                presenter.pefromMusicClick(position);

                Toast.makeText(getActivity(), ((MusicAdapter)rv.getAdapter()).getItem(position).getMusicName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMoreClick(View view, int position) {
                Toast.makeText(getActivity(), ((MusicAdapter)rv.getAdapter()).getItem(position).getMusicName() + "more", Toast.LENGTH_SHORT).show();
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
