package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.home.CoopanyListDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 */
public class HomeCooperationtListAdapter extends AppBaseAdapter<CoopanyListDataBean> {
      private ViewHold viewHold;


    public HomeCooperationtListAdapter(List<CoopanyListDataBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
          if (convertView==null){
              convertView= LayoutInflater.from(context).inflate(R.layout.cooperation_list_adapter,null);
              viewHold=new ViewHold(convertView);
              convertView.setTag(viewHold);

          }else {

           viewHold= (ViewHold) convertView.getTag();

          }
        inti(position);
        return convertView;
    }

    private void inti(int position) {
        if (data!=null&&data.size()!=0){
            MyAppliction.imageLoader.displayImage(AppUtilsUrl.BaseUrl+data.get(position).getLogo(),viewHold.imageView,MyAppliction.options);
            viewHold.tailtTextView.setText(data.get(position).getName());
            viewHold.abstractText.setText(data.get(position).getContent());

        }



    }


    private class ViewHold {

        @ViewInject(R.id.tailt_text_view)
        private TextView tailtTextView;
        @ViewInject(R.id.image_view)
        private ImageView imageView;
        @ViewInject(R.id.abstract_text)
        private TextView abstractText;

        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
