package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.ReleaseJobListBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 */
public class ReleaseJobListAdapter extends AppBaseAdapter<ReleaseJobListBean> {
      private ViewHold viewHold;


    public ReleaseJobListAdapter(List<ReleaseJobListBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
          if (convertView==null){
              convertView= LayoutInflater.from(context).inflate(R.layout.release_job_list_adapter,null);
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
            if (!TextUtils.isEmpty(data.get(position).getManufacture())&&!TextUtils.isEmpty(data.get(position).getNoOfManufacture())){
                viewHold.tailtText.setText(data.get(position).getManufacture()+data.get(position).getNoOfManufacture());
            }else {
                viewHold.tailtText.setText("暂无");
            }
            if (!TextUtils.isEmpty(data.get(position).getStatusStr())){
                String status=data.get(position).getStatusStr();
                if (status.equals("1")){

                    viewHold.tageText.setText("审核通过");
                    viewHold.tageText.setBackgroundResource(R.drawable.text_view_14b8e1_corners);
                    viewHold.tageText.setTextColor(context.getResources().getColor(R.color.color_14b8e1));
                }else if (status.equals("2")){
                    viewHold.tageText.setText("审核失败");
                    viewHold.tageText.setBackgroundResource(R.drawable.text_view_corners);
                    viewHold.tageText.setTextColor(context.getResources().getColor(R.color.red_light));
                }else if (status.equals("0")){
                    viewHold.tageText.setText("审核中");
                    viewHold.tageText.setBackgroundResource(R.drawable.text_view_555555_corners);
                    viewHold.tageText.setTextColor(context.getResources().getColor(R.color.color_555555));
                }
            }
            if (!TextUtils.isEmpty(data.get(position).getPayLevel())){
                viewHold.salaryText.setText("￥"+data.get(position).getPayLevel());
            }else {
                viewHold.salaryText.setText("暂无");
            }
            if (!TextUtils.isEmpty(data.get(position).getWorkingAge())){
                viewHold.limitAgeText.setText(data.get(position).getWorkingAge());
            }else {
                viewHold.limitAgeText.setText("暂无");
            }
            if (!TextUtils.isEmpty(data.get(position).getCount())){
                if (Integer.valueOf(data.get(position).getCount())>0){
                    viewHold.deliverNumberText.setText(data.get(position).getCount());
                    viewHold.deliverNumberText.setTextColor(context.getResources().getColor(R.color.red_light));
                }else {
                    viewHold.deliverNumberText.setText("0");
                    viewHold.deliverNumberText.setTextColor(context.getResources().getColor(R.color.color_888888));
                }

            }
            if (!TextUtils.isEmpty(data.get(position).getProvince())&&!TextUtils.isEmpty(data.get(position).getCity())){
                viewHold.addressText.setText(data.get(position).getProvince()+data.get(position).getCity());
            }
            if (!TextUtils.isEmpty(data.get(position).getCreateDateStr())){
                viewHold.dateText.setText(data.get(position).getCreateDateStr());
            }






        }



    }


    private class ViewHold {

        @ViewInject(R.id.tailt_text)
        private TextView tailtText;//标题
        @ViewInject(R.id.tage_text)
        private  TextView tageText; //审核标记
        @ViewInject(R.id.salary_text)
        private TextView salaryText;  //薪水
        @ViewInject(R.id.limit_age_text)
        private TextView limitAgeText; //年限
        @ViewInject(R.id.deliver_number_text)
        private TextView deliverNumberText;  //投递数量
        @ViewInject(R.id.deliver_text)
        private TextView deliverText; //投递文字
        @ViewInject(R.id.address_text)
        private TextView addressText;  //所在地
        @ViewInject(R.id.date_text)
        private TextView dateText;  //时间

        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
