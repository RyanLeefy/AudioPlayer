package com.example.administrator.audioplayer.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

/**
 * Created on 2017/1/25.
 */

public class ToolbarWithSearch extends Toolbar{
    public ToolbarWithSearch(Context context) {
        this(context, null);
    }

    public ToolbarWithSearch(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolbarWithSearch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);



    }
}
