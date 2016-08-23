package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.RawardBuyListBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 */
public class RawardBuyListAdapter extends AppBaseAdapter<RawardBuyListBean> {
      private ViewHold viewHold;
      private int tage;


    public RawardBuyListAdapter(List<RawardBuyListBean> data, Context context,int tage) {
        super(data, context);
        this.tage=tage;
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
          if (convertView==null){
              convertView= LayoutInflater.from(context).inflate(R.layout.raward_buy_list_adapter,null);
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


            if (tage==1){
                if (!TextUtils.isEmpty(data.get(position).getHourOfWork())){
                    viewHold.salaryText.setText(data.get(position).getHourOfWork()+"小时");
                }else {
                    viewHold.salaryText.setText("暂无");
                }
                if (!TextUtils.isEmpty(data.get(position).getBossName())){
                    viewHold.tailtText.setText("购买人姓名："+data.get(position).getBossName());
                }else {
                    viewHold.tailtText.setText("暂无");
                }
                if (!TextUtils.isEmpty(data.get(position).getDateOfManufacture())){
                    viewHold.limitAgeText.setText(data.get(position).getDateOfManufacture());
                }else {
                    viewHold.limitAgeText.setText("暂无");
                }
                viewHold.limitAgeImage.setVisibility(View.GONE);
                viewHold.timeView.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(data.get(position).getAuditStutasStr())) {
                    String status = data.get(position).getAuditStutasStr();
                    if (status.equals("1")) {

                        viewHold.tageText.setText("审核通过");
                        viewHold.tageText.setTextColor(context.getResources().getColor(R.color.color_14b8e1));
                    } else if (status.equals("2")) {
                        viewHold.tageText.setText("未通过");
                        viewHold.tageText.setTextColor(context.getResources().getColor(R.color.red_light));
                    } else if (status.equals("0")) {
                        viewHold.tageText.setText("未审核");
                        viewHold.tageText.setTextColor(context.getResources().getColor(R.color.color_333333));
                    }
                }
            }else {
                if (!TextUtils.isEmpty(data.get(position).getPrice())){
                    viewHold.salaryText.setText("￥"+data.get(position).getPrice()+"万");
                    viewHold.salaryText.setTextColor(context.getResources().getColor(R.color.red_light));
                }else {
                    viewHold.salaryText.setText("暂无");
                }
                if (!TextUtils.isEmpty(data.get(position).getManufacture())&&!TextUtils.isEmpty(data.get(position).getNoOfManufacture())){
                    viewHold.tailtText.setText(data.get(position).getManufacture()+data.get(position).getNoOfManufacture());
                }else {
                    viewHold.tailtText.setText("暂无");
                }
                if (!TextUtils.isEmpty(data.get(position).getDateOfManufacture())&&!TextUtils.isEmpty(data.get(position).getDateMonthOfManufacture())){
                    viewHold.limitAgeText.setText(data.get(position).getDateOfManufacture()+"年"+data.get(position).getDateMonthOfManufacture()+"月");
                }else {
                    viewHold.limitAgeText.setText("暂无");
                }
                viewHold.limitAgeImage.setVisibility(View.VISIBLE);
                viewHold.timeView.setVisibility(View.VISIBLE);
                viewHold.timeView.setText(data.get(position).getHourOfWork()+"小时");
                if (!TextUtils.isEmpty(data.get(position).getStatusStr())){
                    String status=data.get(position).getStatusStr();
                    if (status.equals("1")){

                        viewHold.tageText.setText("审核通过");
                        viewHold.tageText.setTextColor(context.getResources().getColor(R.color.color_14b8e1));
                    }else if (status.equals("2")){
                        viewHold.tageText.setText("未通过");
                        viewHold.tageText.setTextColor(context.getResources().getColor(R.color.red_light));
                    }else if (status.equals("0")){
                        viewHold.tageText.setText("未审核");
                        viewHold.tageText.setTextColor(context.getResources().getColor(R.color.color_333333));
                    }else if (status.equals("3")){
                        viewHold.tageText.setText("已采纳");
                        viewHold.tageText.setTextColor(context.getResources().getColor(R.color.color_14b8e1));
                    }else if (status.equals("4")){
                        viewHold.tageText.setText("已拒绝");
                        viewHold.tageText.setTextColor(context.getResources().getColor(R.color.red_light));
                    }
                }


            }


            if (!TextUtils.isEmpty(data.get(position).getProvince())&&!TextUtils.isEmpty(data.get(position).getCity())){
                viewHold.addressText.setText("所在地："+data.get(position).getProvince()+data.get(position).getCity());
            }
            if (!TextUtils.isEmpty(data.get(position).getCreateDateStr())){
                viewHold.dateText.setText(data.get(position).getCreateDateStr()+"参与");
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
        @ViewInject(R.id.address_text)
        private TextView addressText;  //所在地
        @ViewInject(R.id.date_text)
        private TextView dateText;  //时间
        @ViewInject(R.id.time_text)
        private TextView timeView; //钻机工作小时
        @ViewInject(R.id.limit_age_image)
        private ImageView limitAgeImage;

        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
