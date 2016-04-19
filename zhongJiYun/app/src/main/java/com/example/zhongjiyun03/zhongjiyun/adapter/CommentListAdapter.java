package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.main.CommentPagerDataBean;
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
public class CommentListAdapter extends AppBaseAdapter<CommentPagerDataBean> {
      private ViewHold viewHold;


    public CommentListAdapter(List<CommentPagerDataBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
          if (convertView==null){
              convertView= LayoutInflater.from(context).inflate(R.layout.comment_list_adapter_layout,null);
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
            MyAppliction.imageLoader.displayImage(data.get(position).getHeadthumb(),viewHold.imageView,MyAppliction.RoundedOptionsOne);
            viewHold.nameTextView.setText(data.get(position).getName());
            viewHold.timeText.setText(data.get(position).getDateTime());
            viewHold.commentContentText.setText(data.get(position).getContent());
        }




    }


    private class ViewHold {

        @ViewInject(R.id.image_view)
        private ImageView imageView;
        @ViewInject(R.id.name_text)
        private TextView nameTextView;
        @ViewInject(R.id.time_text)
        private TextView timeText;
        @ViewInject(R.id.comment_content_text)
        private TextView commentContentText;


        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
