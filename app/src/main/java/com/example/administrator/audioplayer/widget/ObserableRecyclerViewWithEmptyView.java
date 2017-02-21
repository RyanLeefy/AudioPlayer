package com.example.administrator.audioplayer.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;

/**
 * Created by on 2017/2/22 0022.
 */

public class ObserableRecyclerViewWithEmptyView extends ObservableRecyclerView {
    private View emptyView;

    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    public ObserableRecyclerViewWithEmptyView(Context context) {
        super(context, null);
    }

    public ObserableRecyclerViewWithEmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public ObserableRecyclerViewWithEmptyView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }


    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }

    private void checkIfEmpty() {
        if(getAdapter() == null) {
            emptyView.setVisibility(GONE);
        }
        if (emptyView != null && getAdapter() != null) {
            final boolean emptyViewVisible =
                    getAdapter().getItemCount() == 0;
            //Logger.d("check" + getAdapter().getItemCount());
            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
    }

}
