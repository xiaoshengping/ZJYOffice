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
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityChildsBean;

import java.util.List;

/**
 * 二级目录的子目录的数据适配器
 * @author Administrator
 *
 */
public class SubListViewAdapter<T> extends BaseAdapter {

    //private String[][] sub_items;
    private List<T> sub_items;
    private Context context;
    private int root_position;
    private LayoutInflater inflater;
    private int tage;




    public SubListViewAdapter(Context context, List<T> sub_items,
                              int position,int tage) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.sub_items = sub_items;
        this.root_position = position;
        this.tage=tage;
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
       // Log.e("sdfff",tage+"");
        if (tage==1){
            holder.item_text.setText(((FacillyChildsBean)sub_items.get(position)).getText());
        }else if (tage==3){
            holder.item_text.setText(((ProvinceCityChildsBean)sub_items.get(position)).getName());
        }else if (tage==4){
            holder.item_text.setText(((String)sub_items.get(position)));
        }

        return convertView;
    }
    class ViewHolder{
        TextView item_text;
    }

}
