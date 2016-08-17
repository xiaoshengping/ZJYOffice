package com.example.zhongjiyun03.zhongjiyun.popwindon;

/**
 * Created by ZHONGJIYUN03 on 2016/6/15.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.select.FacillyDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityBean;

import java.util.List;

/**
 * 二级目录的根目录的数据适配器
 * @author Administrator
 *
 */
public class RootListViewAdapter<T> extends BaseAdapter {


    private Context context;

    private LayoutInflater inflater;

    //private String[] items;
    private List<T> items;

    private int selectedPosition = -1;

    private int tage; //标识

    public int getTage() {
        return tage;
    }

    public void setTage(int tage) {
        this.tage = tage;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }


    public RootListViewAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }



    public void setItems(List<T> items) {
        this.items = items;
    }



    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
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
        if(convertView == null){

            holder = new ViewHolder();
            convertView = (View)inflater.inflate(R.layout.root_listview_item, parent , false);
            holder.item_text =(TextView) convertView.findViewById(R.id.item_name_text);
            holder.item_layout = (RelativeLayout)convertView.findViewById(R.id.root_item);
            holder.item_tage_image= (ImageView) convertView.findViewById(R.id.item_tage_image);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        /**
         * 该项被选中时改变背景色
         */
        if(selectedPosition == position){
//          Drawable item_bg = new ColorDrawable(R.color.sub_list_color);
            holder.item_layout.setBackgroundColor(Color.WHITE);
            holder.item_tage_image.setBackgroundResource(R.mipmap.filter_icon);
            holder.item_text.setTextColor(context.getResources().getColor(R.color.red_light));
        }else{
//          Drawable item_bg = new ColorDrawable(R.color.sub_list_color);
            holder.item_layout.setBackgroundColor(Color.TRANSPARENT);
            holder.item_tage_image.setBackgroundResource(R.mipmap.filter_cur_icon);
            holder.item_text.setTextColor(context.getResources().getColor(R.color.color_222222));
        }
       // Log.e("sdjhdjdj",tage+"");
        if (tage==1){
            holder.item_text.setText(((FacillyDataBean)items.get(position)).getText());
        }else if (tage==2){
            holder.item_text.setText((String)items.get(position));
        }else if (tage==3){
            holder.item_text.setText(((ProvinceCityBean)items.get(position)).getName());
        }else if (tage==4){
            holder.item_text.setText((String)items.get(position));
        }else if (tage==5){
            holder.item_text.setText(((ProvinceCityBean)items.get(position)).getName());
        }

        return convertView;
    }
    class ViewHolder{
        TextView item_text;
        ImageView item_tage_image;
        RelativeLayout item_layout;
    }

}
