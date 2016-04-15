package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 */
public class MessageListAdapter extends AppBaseAdapter<String> {
      private ViewHold viewHold;


    public MessageListAdapter(List<String> data, Context context) {
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
        viewHold.tailtTextView.setText(data.get(position));
        /*viewHold.dataText.setText();
        viewHold.contentText.setText();*/


    }


    private class ViewHold {

        @ViewInject(R.id.tailt_text)
        private TextView tailtTextView;
        @ViewInject(R.id.data_text)
        private TextView dataText;
        @ViewInject(R.id.content_text)
        private TextView contentText;

        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
