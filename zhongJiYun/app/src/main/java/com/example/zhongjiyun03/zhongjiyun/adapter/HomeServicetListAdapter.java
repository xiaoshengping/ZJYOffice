package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.home.ServiceProviderBean;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 *
 * 服务商
 *
 */
public class HomeServicetListAdapter extends AppBaseAdapter<ServiceProviderBean> {
      private ViewHold viewHold;


    public HomeServicetListAdapter(List<ServiceProviderBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
          if (convertView==null){
              convertView= LayoutInflater.from(context).inflate(R.layout.service_provider_list_layout,null);
              viewHold=new ViewHold(convertView);
              convertView.setTag(viewHold);

          }else {

           viewHold= (ViewHold) convertView.getTag();

          }
        inti(position);
        return convertView;
    }

    private void inti(int position) {
        if (data.size()!=0){
            MyAppliction.imageLoader.displayImage(data.get(position).getThumbnail(),viewHold.imageView,MyAppliction.RoundedOptionsOne);
            viewHold.describeTextView.setText(data.get(position).getSummary());
            viewHold.companyTextView.setText(data.get(position).getProviderName());
            if (!TextUtils.isEmpty(data.get(position).getDistance())){
                 if (data.get(position).getDistance().equals("-1")){
                     viewHold.addressTextView.setVisibility(View.GONE);
                }else {
                     viewHold.addressTextView.setVisibility(View.VISIBLE);
                     viewHold.addressTextView.setText(data.get(position).getDistance()+"Km");
                 }
            }else {
                viewHold.addressTextView.setVisibility(View.GONE);
            }

            viewHold.typeTextView.setText(data.get(position).getProviderTypeStr());
            if (!TextUtils.isEmpty(data.get(position).getProvince())&&!TextUtils.isEmpty(data.get(position).getCity())){
                viewHold.addressText.setText("所在地："+data.get(position).getProvince()+data.get(position).getCity());
            }else {
                if (!TextUtils.isEmpty(data.get(position).getProvince())){
                    viewHold.addressText.setText("所在地："+data.get(position).getProvince());
                }else {
                    viewHold.addressText.setText("所在地：");
                }
            }


        }




    }


    private class ViewHold {

        @ViewInject(R.id.image_view)
        private ImageView imageView;  //头像
        /*@ViewInject(R.id.tailt_text_view)
        private TextView tailtTextView;  */
        @ViewInject(R.id.address_text_view)
        private TextView addressTextView;  //距离
        @ViewInject(R.id.company_text_view)
        private TextView companyTextView;  //公司名称
        @ViewInject(R.id.type_text_view)
        private TextView typeTextView;   //类型
        @ViewInject(R.id.describe_text_view)
        private TextView describeTextView;  //备注
        @ViewInject(R.id.address_text)
        private TextView addressText;  // 所在地

        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
