package com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.bean.ImageBean;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.utils.DensityUtils;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.utils.ScreenUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yinwei on 2015-11-16.
 */
public class ImageGridViewAdapter extends BaseAdapter {

    private ArrayList<ImageBean> marrayList;

    private DisplayImageOptions options;

    private Context context;

    private int mwidth;

    public ImageGridViewAdapter(Context context,DisplayImageOptions options) {

        this.context=context;

        this.options=options;

        mwidth= ScreenUtils.getScreenWidth(context);

    }


    /***
     * 设置数据源
     * @param marrayList
     */
    public void setArrayList(ArrayList<ImageBean> marrayList) {

        this.marrayList = marrayList;
        notifyDataSetChanged();


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return marrayList.get(position);
    }

    @Override
    public int getCount() {

        return marrayList==null?0:marrayList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.adaper_image_gridview_item,null);
            holder.imageView=(ImageView)convertView.findViewById(R.id.image);

            holder.imageView.getLayoutParams().height= (mwidth- DensityUtils.dp2px(context, 40))/3;
            holder.imageView.setLayoutParams(holder.imageView.getLayoutParams());
            convertView.setTag(holder);

        }else{

            holder=(ViewHolder)convertView.getTag();

        }

        ImageLoader.getInstance().displayImage("file://" + marrayList.get(position).getImagePath(), holder.imageView, options);

        return convertView;
    }

    private class ViewHolder{

        private ImageView imageView;

    }
}
