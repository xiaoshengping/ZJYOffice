package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.ResumeListDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 */
public class ResumeListAdapter extends AppBaseAdapter<ResumeListDataBean> {
      private ViewHold viewHold;


    public ResumeListAdapter(List<ResumeListDataBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
          if (convertView==null){
              convertView= LayoutInflater.from(context).inflate(R.layout.resume_list_adapter,null);
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

            if (!TextUtils.isEmpty(data.get(position).getCreateDateStr())){
                viewHold.timeText.setText(data.get(position).getCreateDateStr()+"投递");
            }
            if (!TextUtils.isEmpty(data.get(position).getWorkingAge())){
                viewHold.dataText.setText(data.get(position).getWorkingAge()+"年");
            }
            if (!TextUtils.isEmpty(data.get(position).getDriverHeader())){
                MyAppliction.imageLoader.displayImage(data.get(position).getDriverHeader(),viewHold.imageView,MyAppliction.RoundedOptionsOne);
            }
            if (!TextUtils.isEmpty(data.get(position).getDeviceNames())){
                viewHold.drillingTypeText.setText("熟练机型："+data.get(position).getDeviceNames());

            }
            if (!TextUtils.isEmpty(data.get(position).getWage())){
                viewHold.payTextView.setText("￥"+data.get(position).getWage());

            }







        }



    }


    private class ViewHold {

        @ViewInject(R.id.tailt_text_view)
        private TextView tailtTextView;  //名字
        @ViewInject(R.id.time_text_view)
        private TextView timeText;    //投递时间
        @ViewInject(R.id.pay_text_view)
        private TextView payTextView;   //希望薪资
        @ViewInject(R.id.data_text)
        private TextView dataText;  //工作时间
        @ViewInject(R.id.image_view)
        private ImageView imageView;  //头像
        @ViewInject(R.id.drilling_type_text)
        private TextView drillingTypeText; //熟练钻机

        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
