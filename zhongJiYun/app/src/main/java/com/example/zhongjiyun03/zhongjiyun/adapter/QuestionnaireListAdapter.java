package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.QuestionnaireListBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 */
public class QuestionnaireListAdapter extends AppBaseAdapter<QuestionnaireListBean> {
      private ViewHold viewHold;


    public QuestionnaireListAdapter(List<QuestionnaireListBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
          if (convertView==null){
              convertView= LayoutInflater.from(context).inflate(R.layout.questionnaise_list_layout,null);
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
            if (!TextUtils.isEmpty(data.get(position).getTitle())){
                viewHold.tailtTextView.setText(data.get(position).getTitle());
            }
            if (!TextUtils.isEmpty(data.get(position).getCloudMoney())){
                viewHold.numberText.setText(data.get(position).getCloudMoney());
            }
            if (!TextUtils.isEmpty(data.get(position).getExpireDateTime())){
                viewHold.timeText.setText("过期时间："+data.get(position).getExpireDateTime());
            }


        }



    }


    private class ViewHold {

        @ViewInject(R.id.tailt_text)
        private TextView tailtTextView;  //标题
        @ViewInject(R.id.number_text)
        private TextView numberText;  //云币数
        @ViewInject(R.id.time_text)
        private TextView timeText;   //时间


        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
