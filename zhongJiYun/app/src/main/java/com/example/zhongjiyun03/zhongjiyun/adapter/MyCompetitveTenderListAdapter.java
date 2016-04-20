package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.main.ProjectlistDataBean;
import com.example.zhongjiyun03.zhongjiyun.uilts.CommentOwnerActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.CommitCashDepositActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 */
public class MyCompetitveTenderListAdapter extends AppBaseAdapter<ProjectlistDataBean> implements View.OnClickListener {
      private ViewHold viewHold;
      private int positions;


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
        positions=position;
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
                viewHold.dataText.setText(data.get(position).getCreateDateStr());
            }
            if (data.get(position).getStatus()==1){
                viewHold.commentButton.setVisibility(View.GONE);
                viewHold.zhongBiaoImage.setBackgroundResource(R.mipmap.bid_state_one);
            }else if (data.get(position).getStatus()==2){
                viewHold.commentButton.setVisibility(View.GONE);
                viewHold.zhongBiaoImage.setBackgroundResource(R.mipmap.bid_state_two);
            }else if (data.get(position).getStatus()==3){
                viewHold.commentButton.setVisibility(View.VISIBLE);
                viewHold.zhongBiaoImage.setBackgroundResource(R.mipmap.bid_state_three);
            }
        }
        viewHold.commentButton.setOnClickListener(this);
        viewHold.cashDepositButton.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.cash_deposit_button:
                Intent cashDepositIntent=new Intent(context, CommitCashDepositActivity.class);
                context.startActivity(cashDepositIntent);
               break;
            case R.id.comment_button:
                Intent commentIntent=new Intent(context, CommentOwnerActivity.class);
                commentIntent.putExtra("ProjectTitle",data.get(positions).getProjectTitle());
                commentIntent.putExtra("ProjectCompany",data.get(positions).getProjectCompany());
                commentIntent.putExtra("projectId",data.get(positions).getProjectId());
                context.startActivity(commentIntent);
                break;

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
        @ViewInject(R.id.zhong_biao_image)
        private ImageView zhongBiaoImage;
        @ViewInject(R.id.comment_button)
        private TextView commentButton;
        @ViewInject(R.id.cash_deposit_button)
        private TextView cashDepositButton;


        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
