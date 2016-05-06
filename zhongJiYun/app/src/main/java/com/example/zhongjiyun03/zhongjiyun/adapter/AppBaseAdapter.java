package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @Package com.windy.prejectnuomi.adapters
 * @作 用: list adapter
 * @创 建 人: XianJiaReng
 * @日 期: 2015/3/11
 * @修 改 人:
 * @日 期:
 */
public abstract class AppBaseAdapter<T> extends BaseAdapter {
    public List<T> data;
    public Context context;
    //public LayoutInflater inflater;

    public AppBaseAdapter(List<T> data, Context context) {
        this.context = context;
        this.data = data;
        //inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data != null && !data.isEmpty() ? data.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    public abstract View createView(int position, View convertView,
                                    ViewGroup parent);

}
