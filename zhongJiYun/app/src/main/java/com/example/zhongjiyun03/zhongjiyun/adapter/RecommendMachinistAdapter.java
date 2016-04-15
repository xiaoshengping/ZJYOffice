package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.home.SecondHandBean;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/4/11.
 */
public class RecommendMachinistAdapter extends AppBaseAdapter<SecondHandBean> {

     private  HolderView holderView;

    public RecommendMachinistAdapter(List<SecondHandBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.recommend_machinist_adapter,null);
            holderView=new HolderView(convertView);
            convertView.setTag(holderView);
        }else {

            holderView= (HolderView) convertView.getTag();
        }
        init(position);
        return convertView;
    }

    private void init(int position) {
        if (data!=null){
            if (!TextUtils.isEmpty(data.get(position).getDeviceDto().getDevicePhoto())){
                MyAppliction.imageLoader.displayImage(data.get(position).getDeviceDto().getDevicePhoto(),holderView.imageView,MyAppliction.options);
            }
            if (!TextUtils.isEmpty(data.get(position).getDeviceDto().getDateOfManufacture())&&!TextUtils.isEmpty(data.get(position).getDeviceDto().getDateMonthOfManufacture())){
                holderView.dateTextVIew.setText(data.get(position).getDeviceDto().getDateOfManufacture()+"年"+
                        data.get(position).getDeviceDto().getDateMonthOfManufacture()+"月");

            }else {
                holderView.dateTextVIew.setText(data.get(position).getDeviceDto().getDateOfManufacture()+"年");
            }
            holderView.priceTextView.setText(data.get(position).getPriceStr()+"万");

        }




    }


    private class HolderView{

        @ViewInject(R.id.image_view)
        private ImageView imageView ;
        @ViewInject(R.id.date_text_view)
        private TextView dateTextVIew;
        @ViewInject(R.id.price_text_view)
        private TextView priceTextView;

        public HolderView(View view) {
            ViewUtils.inject(this,view);


        }
    }
}
