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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created  on 2017/1/22.
 */

public class LeftMenuItemAdapter extends BaseAdapter {

    private List<LeftMenuItem> list = new ArrayList<>(    Arrays.asList(
            new LeftMenuItem(R.drawable.leftmenu_icn_identify, "听歌识曲"),
            new LeftMenuItem(R.drawable.leftmenu_icn_time, "定时关闭音乐"),
            new LeftMenuItem(R.drawable.leftmenu_icn_vip, "下载歌曲品质"),
            new LeftMenuItem(R.drawable.leftmenu_icn_exit, "退出")

    ));
    private LayoutInflater mInflater;

    public LeftMenuItemAdapter(Context context) {
        mInflater = LayoutInflater.from(context);

    }

    public LeftMenuItemAdapter(List<LeftMenuItem> list) {
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
        ItemViewTag itemViewTag;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_listitem_leftmenu, parent, false);
            ImageView icon = (ImageView) convertView.findViewById(R.id.img_leftmenu_icon);
            TextView textView = (TextView) convertView.findViewById(R.id.tv_leftmenu_name);
            itemViewTag = new ItemViewTag(icon, textView);
            convertView.setTag(itemViewTag);
        } else {
            itemViewTag = (ItemViewTag) convertView.getTag();
        }

        itemViewTag.icon.setImageResource(list.get(position).getmIcon());
        itemViewTag.name.setText(list.get(position).getmName());

        return convertView;
    }



     static class ItemViewTag {
        protected ImageView icon;
        protected TextView name;

        public ItemViewTag(ImageView icon, TextView name) {
            this.icon = icon;
            this.name = name;
        }
    }

}
