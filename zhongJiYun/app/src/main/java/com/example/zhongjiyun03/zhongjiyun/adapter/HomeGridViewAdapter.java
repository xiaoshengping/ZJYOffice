package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.zhongjiyun03.zhongjiyun.R;

/**
 * Created by ZHONGJIYUN03 on 2016/3/10.
 * 首页 HomeGridViewAdapter
 */
public class HomeGridViewAdapter extends BaseAdapter {

    private Context context;

    public HomeGridViewAdapter() {
        this.context=context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.home_gridview_adapter_layout,null);


        return convertView;
    }
}
