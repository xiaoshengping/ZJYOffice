package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.home.NewsDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 */
public class HomeNewsListAdapter extends AppBaseAdapter<NewsDataBean> {
      private ViewHold viewHold;


    public HomeNewsListAdapter(List<NewsDataBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
          if (convertView==null){
              convertView= LayoutInflater.from(context).inflate(R.layout.news_list_layout,null);
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
            if (!TextUtils.isEmpty(data.get(position).getSubTitle())){
                viewHold.contentText.setText(data.get(position).getSubTitle());
            }
            if (!TextUtils.isEmpty(data.get(position).getFrontCover())){

                MyAppliction.imageLoader.displayImage(data.get(position).getFrontCover(),viewHold.newsIcon,MyAppliction.options);

            }


        }



    }


    private class ViewHold {

        @ViewInject(R.id.tailt_text)
        private TextView tailtTextView;
        @ViewInject(R.id.news_icon)
        private ImageView newsIcon;
        @ViewInject(R.id.content_text)
        private TextView contentText;

        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
