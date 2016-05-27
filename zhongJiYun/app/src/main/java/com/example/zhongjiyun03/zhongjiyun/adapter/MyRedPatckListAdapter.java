package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.main.RePackedListBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 */
public class MyRedPatckListAdapter extends AppBaseAdapter<RePackedListBean> {
      private ViewHold viewHold;


    public MyRedPatckListAdapter(List<RePackedListBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
          if (convertView==null){
              convertView= LayoutInflater.from(context).inflate(R.layout.myred_packed_list_adapter,null);
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
            viewHold.tailtTextView.setText(data.get(position).getTitle());
            viewHold.numberText.setText(data.get(position).getCloudMoney()+"");
            if (data.get(position).getIsGet()==1){
                viewHold.imageView.setVisibility(View.VISIBLE);
                viewHold.redPackImage.setBackgroundResource(R.mipmap.my_reward);
                viewHold.dataText.setText("领取日期："+data.get(position).getGetTime());

            }else {
                viewHold.numberText.setTextColor(context.getResources().getColor(R.color.red));
                viewHold.imageView.setVisibility(View.GONE);
                viewHold.redPackImage.setBackgroundResource(R.mipmap.my_reward_cur);
                viewHold.dataText.setText("有效期至："+data.get(position).getExpirationTime());
            }



        }



    }


    private class ViewHold {

        @ViewInject(R.id.tailt_text)
        private TextView tailtTextView;
        @ViewInject(R.id.data_text)
        private TextView dataText;
        @ViewInject(R.id.number_text)
        private TextView numberText;
        @ViewInject(R.id.lingqu_image)
        private ImageView imageView;
        @ViewInject(R.id.red_pack_image)
        private ImageView redPackImage;

        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
