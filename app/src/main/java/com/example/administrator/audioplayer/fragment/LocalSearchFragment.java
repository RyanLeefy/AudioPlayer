package com.example.administrator.audioplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.administrator.audioplayer.Ipresenter.ILocalSearchPresenter;
import com.example.administrator.audioplayer.Iview.ILocalSearchView;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.MusicAdapter;
import com.example.administrator.audioplayer.presenterImp.LocalSearchPresenter;
import com.example.administrator.audioplayer.widget.DividerItemDecoration;
import com.example.administrator.audioplayer.widget.RecycleViewWithEmptyView;


public class LocalSearchFragment extends BaseFragment implements ILocalSearchView, SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    //private InputMethodManager mImm;
    private Callback callback;
    private View view;
    private RecycleViewWithEmptyView rv;
    private ILocalSearchPresenter presenter;

    public LocalSearchFragment() {
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
        view =  inflater.inflate(R.layout.fragment_local_search, container, false);

        setToolbar();

        //mImm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        mSearchView = (SearchView) view.findViewById(R.id.sv_local_search_fragment);
        mSearchView.setQueryHint("搜索本地歌曲");
        mSearchView.setOnQueryTextListener(this);

        mSearchView.setIconified(false);
        mSearchView.setIconifiedByDefault(false);


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
    public void setAdapter(MusicAdapter adapter) {
        //获取回来的adapter先设置监听事件，然后再设置给recycleView
        adapter.setOnMusicItemClickListener(new MusicAdapter.OnMusicItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onMoreClick(View view, int position) {

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
