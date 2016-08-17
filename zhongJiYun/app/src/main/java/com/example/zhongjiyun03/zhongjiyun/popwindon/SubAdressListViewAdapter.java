package com.example.zhongjiyun03.zhongjiyun.popwindon;

/**
 * Created by ZHONGJIYUN03 on 2016/6/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.select.FacillyChildsBean;

import java.util.List;

/**
 * 二级目录的子目录的数据适配器
 * @author Administrator
 *
 */
public class SubAdressListViewAdapter extends BaseAdapter {

    //private String[][] sub_items;
    private List<FacillyChildsBean> sub_items;
    private Context context;
    private int root_position;
    private LayoutInflater inflater;

    public SubAdressListViewAdapter(Context context, List<FacillyChildsBean> sub_items,
                                    int position) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.sub_items = sub_items;
        this.root_position = position;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return sub_items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return sub_items.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = (View) inflater.inflate(R.layout.root_listview_item,
                    parent, false);
            holder.item_text = (TextView) convertView
                    .findViewById(R.id.item_name_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_text.setText(sub_items.get(position).getText());
        return convertView;
    }
    class ViewHolder{
        TextView item_text;
    }

}
