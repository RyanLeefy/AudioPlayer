package com.example.administrator.audioplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.bean.LeftMenuItem;

import java.util.List;

/**
 * Created  on 2017/1/24.
 */

public class PopUpWindowMenuAdapter extends BaseAdapter {

    private List<LeftMenuItem> list;
    private LayoutInflater mInflater;


    public PopUpWindowMenuAdapter(Context context, List<LeftMenuItem> list) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    public void addMenuItem(LeftMenuItem item) {
        list.add(item);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LeftMenuItemAdapter.ItemViewTag itemViewTag;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_menu_popupwindow, parent, false);
            ImageView icon = (ImageView) convertView.findViewById(R.id.img_popupitem);
            TextView textView = (TextView) convertView.findViewById(R.id.tv_title_popupitem);
            itemViewTag = new LeftMenuItemAdapter.ItemViewTag(icon, textView);
            convertView.setTag(itemViewTag);
        } else {
            itemViewTag = (LeftMenuItemAdapter.ItemViewTag) convertView.getTag();
        }

        itemViewTag.icon.setImageResource(list.get(position).getmIcon());
        itemViewTag.name.setText(list.get(position).getmName());

        return convertView;
    }

}
