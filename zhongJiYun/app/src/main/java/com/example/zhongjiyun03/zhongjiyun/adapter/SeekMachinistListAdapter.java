package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.SekkMachinisDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 */
public class SeekMachinistListAdapter extends AppBaseAdapter<SekkMachinisDataBean> {
      private ViewHold viewHold;


    public SeekMachinistListAdapter(List<SekkMachinisDataBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
          if (convertView==null){
              convertView= LayoutInflater.from(context).inflate(R.layout.seel_machinist_list_adapter,null);
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
            if (!TextUtils.isEmpty(data.get(position).getDriverName())){
                viewHold.tailtTextView.setText(data.get(position).getDriverName());
            }
            if (!TextUtils.isEmpty(data.get(position).getDistance())){
                if (data.get(position).getDistance().equals("-1")){
                    viewHold.addressText.setVisibility(View.GONE);
                    viewHold.addressImage.setVisibility(View.GONE);
                }else {
                    viewHold.addressText.setText(data.get(position).getDistance()+"Km");
                    viewHold.addressText.setVisibility(View.VISIBLE);
                    viewHold.addressImage.setVisibility(View.VISIBLE);
                }

            }else {
                viewHold.addressText.setVisibility(View.GONE);
                viewHold.addressImage.setVisibility(View.GONE);

            }
            if (!TextUtils.isEmpty(data.get(position).getLastUpdateTime())){
                viewHold.timeText.setText(data.get(position).getLastUpdateTime()+"更新");
            }
            if (!TextUtils.isEmpty(data.get(position).getAddress())){
                viewHold.companyAddressText.setText("期望工作地:"+data.get(position).getAddress());
            }
            if (!TextUtils.isEmpty(data.get(position).getWage()+"")){
                viewHold.neirongText.setText(data.get(position).getWage()+"元");
            }
            if (!TextUtils.isEmpty(data.get(position).getWorkingAge())){
                viewHold.dataText.setText(data.get(position).getWorkingAge()+"年");
            }
            if (!TextUtils.isEmpty(data.get(position).getDriverHeader())){
                MyAppliction.imageLoader.displayImage(data.get(position).getDriverHeader(),viewHold.imageView,MyAppliction.RoundedOptionsOne);
            }
            if (!TextUtils.isEmpty(data.get(position).getDeviceNames())){
                viewHold.imageLiginEnd.setVisibility(View.VISIBLE);
                viewHold.imageLiginr.setVisibility(View.VISIBLE);
                viewHold.textView.setText(data.get(position).getDeviceNames());
                viewHold.textView.setVisibility(View.VISIBLE);
            }else {
                viewHold.imageLiginEnd.setVisibility(View.GONE);
                viewHold.imageLiginr.setVisibility(View.GONE);
                viewHold.textView.setVisibility(View.GONE);
            }




        }



    }


    private class ViewHold {

        @ViewInject(R.id.tailt_text_view)
        private TextView tailtTextView;
        @ViewInject(R.id.address_text_view)
        private  TextView addressText;
        @ViewInject(R.id.time_text_view)
        private TextView timeText;
        @ViewInject(R.id.company_text_view)
        private TextView companyAddressText;
        @ViewInject(R.id.neirong_text_view)
        private TextView neirongText;
        @ViewInject(R.id.data_text)
        private TextView dataText;
        @ViewInject(R.id.image_view)
        private ImageView imageView;
        @ViewInject(R.id.text_view)
        private TextView textView;
        @ViewInject(R.id.image_ligin_end)
        private ImageView imageLiginEnd;
        @ViewInject(R.id.image_liginr)
        private ImageView imageLiginr;
        @ViewInject(R.id.address_image)
        private ImageView addressImage;

        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
