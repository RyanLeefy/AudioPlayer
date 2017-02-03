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
import android.support.v7.widget.LinearLayoutManager;
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
import com.example.administrator.audioplayer.adapter.LocalMusicAdapter;
import com.example.administrator.audioplayer.widget.RecycleViewWithEmptyView;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


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
        //View emptyview = inflater.inflate(R.layout.nomusic_emptyview_recycleview, container, false);
        View emptyview = view.findViewById(R.id.id_empty_view);
        rv.setEmptyView(emptyview);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setHasFixedSize(true);
        rv.setHasFixedSize(true);
        //rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        List list = new ArrayList<>();
        //list.add("1");
        //list.add("2");
        LocalMusicAdapter adapter = new LocalMusicAdapter(getActivity(), list);
        rv.setAdapter(adapter);
        rv.setEmptyView(emptyview);

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
        hideInputManager();
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
    }
}
