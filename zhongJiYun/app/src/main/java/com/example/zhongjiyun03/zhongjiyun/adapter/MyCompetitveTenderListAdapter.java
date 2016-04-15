package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.main.ProjectlistDataBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 */
public class MyCompetitveTenderListAdapter extends AppBaseAdapter<ProjectlistDataBean> {
      private ViewHold viewHold;


    public MyCompetitveTenderListAdapter(List<ProjectlistDataBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
          if (convertView==null){
              convertView= LayoutInflater.from(context).inflate(R.layout.mycompetitve_tender_list_layout,null);
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
            if (!TextUtils.isEmpty(data.get(position).getProjectTitle())){
                viewHold.tailtTextView.setText(data.get(position).getProjectTitle());
            }
            if (!TextUtils.isEmpty(data.get(position).getProjectCompany())){
                viewHold.companyText.setText("发布公司:"+data.get(position).getProjectCompany());
            }
            if (!TextUtils.isEmpty(data.get(position).getProvince())&&!TextUtils.isEmpty(data.get(position).getCity())){
                viewHold.addressText.setText("所在地:"+data.get(position).getProvince()+data.get(position).getCity());
            }else if (!TextUtils.isEmpty(data.get(position).getProvince())){
                viewHold.addressText.setText("所在地:"+data.get(position).getProvince());
            }
            if (!TextUtils.isEmpty(data.get(position).getCreateDateStr())){
                viewHold.companyText.setText(data.get(position).getCreateDateStr());
            }
        }



    }


    private class ViewHold {

        @ViewInject(R.id.tailt_text)
        private TextView tailtTextView;
        @ViewInject(R.id.company_text)
        private TextView companyText;
        @ViewInject(R.id.address_text)
        private TextView addressText;
        @ViewInject(R.id.data_text)
        private TextView dataText;


        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
