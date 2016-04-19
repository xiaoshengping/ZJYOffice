package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.MessageDataBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 */
public class MessageListAdapter extends AppBaseAdapter<MessageDataBean> {
      private ViewHold viewHold;


    public MessageListAdapter(List<MessageDataBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
          if (convertView==null){
              convertView= LayoutInflater.from(context).inflate(R.layout.message_lsit_adapter,null);
              viewHold=new ViewHold(convertView);
              convertView.setTag(viewHold);

          }else {

           viewHold= (ViewHold) convertView.getTag();

          }
        inti(position);
        return convertView;
    }

    private void inti(int position) {

        viewHold.tailtTextView.setText(data.get(position).getTitle());
        viewHold.dataText.setText(data.get(position).getBeginDate());
        /*viewHold.contentText.setText();*/


    }


    private class ViewHold {

        @ViewInject(R.id.tailt_text)
        private TextView tailtTextView;
        @ViewInject(R.id.data_text)
        private TextView dataText;


        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
