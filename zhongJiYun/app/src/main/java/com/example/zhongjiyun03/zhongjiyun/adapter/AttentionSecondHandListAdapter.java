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
 * Created by ZHONGJIYUN03 on 2016/3/12.
 *
 * 二手机列表
 */
public class AttentionSecondHandListAdapter extends AppBaseAdapter<SecondHandBean> {
      private ViewHold viewHold;


    public AttentionSecondHandListAdapter(List<SecondHandBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
          if (convertView==null){
              convertView= LayoutInflater.from(context).inflate(R.layout.attention_second_hand_list_layout,null);
              viewHold=new ViewHold(convertView);
              convertView.setTag(viewHold);

          }else {

           viewHold= (ViewHold) convertView.getTag();

          }
        inti(position);
        return convertView;
    }

    private void inti(int position) {
        if (data!=null){

                if (!TextUtils.isEmpty(data.get(position).getDevicePhoto())) {
                    MyAppliction.imageLoader.displayImage(data.get(position).getDevicePhoto(), viewHold.imageView, MyAppliction.options);
                }
                if (!TextUtils.isEmpty(data.get(position).getManufacture())
                        &&!TextUtils.isEmpty(data.get(position).getNoOfManufacture())){
                    viewHold.tailtTextView.setText(data.get(position).getManufacture()
                            +data.get(position).getNoOfManufacture());
                }
                if (!TextUtils.isEmpty(data.get(position).getDateOfManufacture())){
                    viewHold.dataTextView.setText(data.get(position).getDateOfManufacture()+"年");
                }
                if (!TextUtils.isEmpty(data.get(position).getHourOfWork())){
                    viewHold.timeTextView.setText(data.get(position).getHourOfWork()+"小时");
                }



            /*if (!TextUtils.isEmpty(data.get(position).getDistanceStr())){
                if (data.get(position).getDistanceStr().equals("-1")){
                    viewHold.distanceTextView.setVisibility(View.GONE);
                }else {
                    viewHold.distanceTextView.setVisibility(View.VISIBLE);
                    viewHold.distanceTextView.setText(data.get(position).getDistanceStr()+"Km");

                }

            }else {
                viewHold.distanceTextView.setVisibility(View.GONE);
            }*/
            if (!TextUtils.isEmpty(data.get(position).getCity())){
                viewHold.addressTextView.setText(data.get(position).getCity());
            }



           if (!TextUtils.isEmpty(data.get(position).getPriceStr())){
               if (data.get(position).getPriceStr().equals("面议")){
                   viewHold.priceTextView.setText(data.get(position).getPriceStr());
               }else {
                   viewHold.priceTextView.setText(data.get(position).getPriceStr()+"万");
               }
           }
            viewHold.updateTimeTextViewl.setText(data.get(position).getUpdateDateStr()+"更新");
            if (data.get(position).getSecondHandType()==0){
                viewHold.secondTageImage.setBackgroundResource(R.mipmap.lease_icon);
                viewHold.tenancyTermTextView.setText("租期:"+data.get(position).getTenancy()+"个月");
                viewHold.tenancyTermTextView.setVisibility(View.VISIBLE);
            }else if (data.get(position).getSecondHandType()==1){
                viewHold.secondTageImage.setBackgroundResource(R.mipmap.sell_icon);
                viewHold.tenancyTermTextView.setVisibility(View.GONE);
            }



        }












    }


    private class ViewHold {

        @ViewInject(R.id.tailt_text_view)
        private TextView tailtTextView;
        @ViewInject(R.id.distance_text_view)
        private TextView distanceTextView; //定位距离
        @ViewInject(R.id.address_text_view)
        private TextView addressTextView;
        @ViewInject(R.id.data_text_view)
        private TextView dataTextView;
        @ViewInject(R.id.time_text_view)
        private TextView timeTextView;
        @ViewInject(R.id.price_text_view)
        private TextView priceTextView;
        @ViewInject(R.id.tenancy_term_text_view)
        private TextView tenancyTermTextView;
        @ViewInject(R.id.update_time_text_view)
        private TextView updateTimeTextViewl;
        @ViewInject(R.id.image_view)
        private ImageView imageView;
        @ViewInject(R.id.second_tage_image)
        private ImageView secondTageImage;





        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
