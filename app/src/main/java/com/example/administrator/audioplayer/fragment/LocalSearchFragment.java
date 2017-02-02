package com.example.administrator.audioplayer.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.widget.RecycleViewWithEmptyView;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Method;


public class LocalSearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private InputMethodManager mImm;
    private Callback callback;
    private LayoutInflater mInflater;
    private View view;

    public LocalSearchFragment() {
        // Required empty public constructor
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
        mInflater = inflater;
        view =  inflater.inflate(R.layout.fragment_local_search, container, false);


        setToolbar();



        mSearchView = (SearchView) view.findViewById(R.id.sv_local_search_fragment);
        mSearchView.setQueryHint("搜索本地歌曲");
        mSearchView.setOnQueryTextListener(this);

        mSearchView.setIconified(false);
        mSearchView.setIconifiedByDefault(false);




        RecycleViewWithEmptyView rv = (RecycleViewWithEmptyView) view.findViewById(R.id.rv_local_search_fragment);
        View emptyview = inflater.inflate(R.layout.nomusic_emptyview_recycleview, container, false);
        rv.setEmptyView(emptyview);

        return view;
    }


    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search_menu, menu);




        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search_menu));

        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("搜索本地歌曲");

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setIconified(false);

        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.search_menu), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return false;
            }
        });

        //menu.findItem(R.id.search_menu).expandActionView();

        super.onCreateOptionsMenu(menu,inflater);
    }*/



    public void setToolbar() {
        //((AppCompatActivity)getActivity()).setSupportActionBar(null);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tb_local_search_fragment);
        toolbar.setTitle("11111111111111111");
        //toolbar.inflateMenu(R.menu.search_menu);

        //toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.showLocalMusicFragment();
            }
        });
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        //callback.initToolbar(toolbar);

        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        /*
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case android.R.id.home:
                        Logger.d("search home");
                        getActivity().finish();
                }
                return true;
            }
        });*/

    }

/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
*/

/*
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
    }*/



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
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(getActivity(), "submit", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Toast.makeText(getActivity(), "textchange", Toast.LENGTH_SHORT).show();
        return false;
    }


    public void hideInputManager() {
        if (mSearchView != null) {
            if (mImm != null) {
                mImm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
            }
            mSearchView.clearFocus();


        }
    }

    public interface Callback {
        // TODO: Update argument type and name
        void showLocalMusicFragment();
        void initToolbar(Toolbar toolbar);
    }
}
