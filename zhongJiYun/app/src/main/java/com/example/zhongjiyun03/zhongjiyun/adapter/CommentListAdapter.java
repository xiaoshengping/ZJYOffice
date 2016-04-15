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
 *
 * 服务商
 *
 */
public class CommentListAdapter extends AppBaseAdapter<String> {
      private ViewHold viewHold;


    public CommentListAdapter(List<String> data, Context context) {
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
           // MyAppliction.imageLoader.displayImage(data.get(position).getThumbnail(),viewHold.imageView,MyAppliction.RoundedOptionsOne);
            viewHold.nameTextView.setText(data.get(position));
          /*  viewHold.companyTextView.setText(data.get(position).getProviderName());
            viewHold.addressTextView.setText(data.get(position).getDistance()+"m");
            viewHold.neirongTextView.setText(data.get(position).getProviderTypeStr());*/

        }




    }


    private class ViewHold {

      /*  @ViewInject(R.id.image_view)
        private ImageView imageView;*/
        @ViewInject(R.id.name_text)
        private TextView nameTextView;
        /*@ViewInject(R.id.address_text_view)
        private TextView addressTextView;
        @ViewInject(R.id.company_text_view)
        private TextView companyTextView;
        @ViewInject(R.id.neirong_text_view)
        private TextView neirongTextView;*/

        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
