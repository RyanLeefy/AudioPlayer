package com.example.administrator.audioplayer.fragment;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.audioplayer.Ipresenter.IPreSearchPresenter;
import com.example.administrator.audioplayer.Iview.IPreSearchView;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.activity.SearchCallBack;
import com.example.administrator.audioplayer.adapter.SearchHistoryAdapter;
import com.example.administrator.audioplayer.db.SearchHistory;
import com.example.administrator.audioplayer.presenterImp.PreSearchPresenter;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.example.administrator.audioplayer.utils.RequestThreadPool;
import com.example.administrator.audioplayer.utils.WidgetUtils;
import com.example.administrator.audioplayer.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;


/**
 * 网络搜索搜索前显示的fragment，包括搜索热词和搜索记录
 */
public class PreSearchFragment extends BaseFragment implements IPreSearchView {


    private Context mContext;

    private LayoutInflater mInflater;

    //加载中动画视图
    private View loadingView;
    //加载动画视图中的动态ImageView
    private ImageView anim_image;
    //网络失败重试TextView
    private TextView try_again;

    //内容框，一开始是invisiable，加载完之后设置为显示
    private RelativeLayout content_container;

    //历史记录
    private RecyclerView recyclerView;

    //用来装textView的list
    private List<TextView> textviewList = new ArrayList<>();

    //用来判断视图加载完毕没
    private int times = 0;

    private IPreSearchPresenter presenter;

    private SearchCallBack callBack;

    public void setSearchCallBack(SearchCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pre_search, container, false);

        content_container = (RelativeLayout) view.findViewById(R.id.rl_pre_search_content_container);

        loadingView = view.findViewById(R.id.layout_loading_view);

        //加载画面的帧动画
        anim_image = (ImageView) view.findViewById(R.id.anim_image);
        anim_image.setBackgroundResource(R.drawable.loading_animation);
        AnimationDrawable anim = (AnimationDrawable) anim_image.getBackground();
        anim.start();

        //设置重试文本的格式，把重试两个字设为点击事件，点击开始重新读取列表
        SpannableString spanString = new SpannableString("请连接网络后点击重试");
        spanString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                loadingView.setVisibility(View.VISIBLE);
                try_again.setVisibility(View.INVISIBLE);
                presenter.onCreateView();
            }
        }, 8, 10, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        try_again = (TextView) view.findViewById(R.id.tv_try_again);
        try_again.setText(spanString);
        try_again.setMovementMethod(LinkMovementMethod.getInstance());


        //10个热门词
        TextView text1 = (TextView) view.findViewById(R.id.tv_pre_search_fragment1);
        TextView text2 = (TextView) view.findViewById(R.id.tv_pre_search_fragment2);
        TextView text3 = (TextView) view.findViewById(R.id.tv_pre_search_fragment3);
        TextView text4 = (TextView) view.findViewById(R.id.tv_pre_search_fragment4);
        TextView text5 = (TextView) view.findViewById(R.id.tv_pre_search_fragment5);
        TextView text6 = (TextView) view.findViewById(R.id.tv_pre_search_fragment6);
        TextView text7 = (TextView) view.findViewById(R.id.tv_pre_search_fragment7);
        TextView text8 = (TextView) view.findViewById(R.id.tv_pre_search_fragment8);
        TextView text9 = (TextView) view.findViewById(R.id.tv_pre_search_fragment9);
        TextView text10 = (TextView) view.findViewById(R.id.tv_pre_search_fragment10);

        //把10个textview装入list中
        textviewList.add(text1);
        textviewList.add(text2);
        textviewList.add(text3);
        textviewList.add(text4);
        textviewList.add(text5);
        textviewList.add(text6);
        textviewList.add(text7);
        textviewList.add(text8);
        textviewList.add(text9);
        textviewList.add(text10);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_pre_search_fragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));


        presenter = new PreSearchPresenter(this);
        presenter.onCreateView();

        return view;

    }




    @Override
    public void setHotWord(List<String> list) {
        //返回的list装着对应的热词
        //textviewList装着textview
        //给每个textview设置对应的热词，然后计算他们的位置

        int w = mContext.getResources().getDisplayMetrics().widthPixels;
        //横坐标位置，-1表示每一行的开头
        int xdistance = -1;
        //纵坐标位置
        int ydistance = 0;
        //每个textview之间的间距
        int distance = CommonUtils.dip2px(mContext, 16);

        for (int i = 0; i < 10; i++) {
            final String query = list.get(i);
            textviewList.get(i).setText(query);
            //设置点击事件
            textviewList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.Search(query);
                }
            });

            //如果是一行的开头
            if (xdistance == -1) {
                //横坐标设为0
                xdistance = 0;
                //设置位置
                WidgetUtils.setLayout(textviewList.get(i), xdistance, ydistance);
                continue;
            }
            //不是一行的开头，则横坐标为前一项的横坐标 + 前一项的宽度 + 间隔
            xdistance += WidgetUtils.getWidth(textviewList.get(i - 1)) + distance;
            //如果计算出来的横坐标 + 当前项的宽度 + 间隔 > 屏幕宽度 的话 那就另起一行，即横坐标变回-1， 纵坐标加每行的高度
            if (xdistance + WidgetUtils.getWidth(textviewList.get(i)) + distance > w) {
                xdistance = -1;
                ydistance += 130;
                i--;
                continue;
            }
            //设置位置
            WidgetUtils.setLayout(textviewList.get(i), xdistance, ydistance);



        }


        times++;
        if(times == 2) {
            //完成后把加载中页面去掉
            loadingView.setVisibility(View.GONE);
            //显示内容
            content_container.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setSearchHistory(SearchHistoryAdapter adapter) {
        adapter.setOnItemClickListener(new SearchHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                callBack.Search(((SearchHistoryAdapter.ViewHolder)holder).searchString.getText().toString());
            }

            @Override
            public void onDeleteClick(final RecyclerView.ViewHolder holder, int position) {
                RequestThreadPool.post(new Runnable() {
                    @Override
                    public void run() {
                        //删除记录
                        SearchHistory.getInstance(mContext).deleteRecentSearches(((SearchHistoryAdapter.ViewHolder)holder).searchString.getText().toString());
                        //重新读取记录，刷新页面
                        presenter.onCreateView();
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        times++;
        if(times == 2) {
            //完成后把加载中页面去掉
            loadingView.setVisibility(View.GONE);
            //显示内容
            content_container.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void showTryAgain() {
        loadingView.setVisibility(View.GONE);
        try_again.setVisibility(View.VISIBLE);
    }
}
