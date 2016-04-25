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
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.uilts.CommentOwnerActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.CommitCashDepositActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 */
public class MyCompetitveTenderListAdapter extends AppBaseAdapter<ProjectlistDataBean>  {
      private ViewHold viewHold;
      private int positions;
    // 用来记录按钮状态的Map
    public static Map<Integer, Boolean> isChecked;

    public MyCompetitveTenderListAdapter(List<ProjectlistDataBean> data, Context context) {
        super(data, context);
    }
    private void initButton() {
        // 初使化操作，默认都是false
        isChecked = new HashMap<Integer, Boolean>();
        for (int i = 0; i < data.size(); i++){
            isChecked.put(i, false);
        }

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
        initButton();
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
                viewHold.commentImage.setVisibility(View.GONE);
                viewHold.commentButton.setVisibility(View.GONE);
                viewHold.zhongBiaoImage.setBackgroundResource(R.mipmap.bid_state_one);
            }else if (data.get(position).getStatus()==2){
                viewHold.commentImage.setVisibility(View.GONE);
                viewHold.commentButton.setVisibility(View.GONE);
                viewHold.zhongBiaoImage.setBackgroundResource(R.mipmap.bid_state_two);
            }else if (data.get(position).getStatus()==3){
                viewHold.commentImage.setVisibility(View.VISIBLE);
                viewHold.commentButton.setVisibility(View.VISIBLE);
                viewHold.zhongBiaoImage.setBackgroundResource(R.mipmap.bid_state_three);
            }

            if (data.get(position).getPayMarginStatus()==1){
                viewHold.cashDepositImage.setBackgroundResource(R.mipmap.project_success);
                viewHold.cashDepositButton.setTextColor(context.getResources().getColor(R.color.content_color));
                viewHold.cashDepositButton.setText("已缴纳保证金");
            }else if (data.get(position).getPayMarginStatus()==0){
                viewHold.cashDepositImage.setBackgroundResource(R.mipmap.project_bond);
                viewHold.cashDepositButton.setText("缴纳保证金");
                viewHold.cashDepositButton.setTextColor(context.getResources().getColor(R.color.red));
            }
            if (data.get(position).getIsEvaluete()==0){
                viewHold.commentImage.setBackgroundResource(R.mipmap.eval_icon);
                viewHold.commentButton.setText("立即评价");

            }else if (data.get(position).getIsEvaluete()==1){
                viewHold.commentImage.setBackgroundResource(R.mipmap.eval_success_icon);
                viewHold.commentButton.setText("已评价");
                viewHold.commentButton.setTextColor(context.getResources().getColor(R.color.content_color));
            }




        }
        viewHold.commentButton.setOnClickListener(new commentClick(position));
        viewHold.cashDepositButton.setOnClickListener(new cashDepositClick(position));



    }


    //此为listview条目中的cashDepositClick按钮点击事件的写法

    class cashDepositClick implements View.OnClickListener {

        private int position;

        public cashDepositClick(int pos){  // 在构造时将position传给它这样就知道点击的是哪个条目的按钮
            this.position = pos;
        }
        @Override
        public void onClick(View v) {
            int vid=v.getId();
            if (vid == viewHold.cashDepositButton.getId()){
                if (isChecked.get(position) == false){
                    isChecked.put(position, true);   // 根据点击的情况来将其位置和相应的状态存入
                    if (data.get(position).getPayMarginStatus()==0){
                        Intent cashDepositIntent=new Intent(context, CommitCashDepositActivity.class);
                        context.startActivity(cashDepositIntent);
                    }else {
                        MyAppliction.showToast("您已缴纳保证金");
                    }

                    //Log.e("steta________", position + "");
                } else if (isChecked.get(position) == true){
                    isChecked.put(position, false);  // 根据点击的情况来将其位置和相应的状态存入

                }
                notifyDataSetChanged();
            }
        }

    }
    //此为listview条目中的commentClick按钮点击事件的写法

    class commentClick implements View.OnClickListener {

        private int position;

        public commentClick(int pos){  // 在构造时将position传给它这样就知道点击的是哪个条目的按钮
            this.position = pos;
        }
        @Override
        public void onClick(View v) {
            int vid=v.getId();
            if (vid == viewHold.commentButton.getId()){
                if (isChecked.get(position) == false){
                    isChecked.put(position, true);   // 根据点击的情况来将其位置和相应的状态存入
                    if (data.get(position).getIsEvaluete()==0){

                        Intent commentIntent=new Intent(context, CommentOwnerActivity.class);
                        commentIntent.putExtra("ProjectTitle",data.get(position).getProjectTitle());
                        commentIntent.putExtra("ProjectCompany",data.get(position).getProjectCompany());
                        commentIntent.putExtra("projectId",data.get(position).getProjectId());
                        context.startActivity(commentIntent);
                    }else if (data.get(position).getIsEvaluete()==1){
                        MyAppliction.showToast("您已评论该业主");
                    }

                    //Log.e("steta________", position + "");
                } else if (isChecked.get(position) == true){
                    isChecked.put(position, false);  // 根据点击的情况来将其位置和相应的状态存入

                }
                notifyDataSetChanged();
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
        @ViewInject(R.id.zhong_biao_image)
        private ImageView zhongBiaoImage;
        @ViewInject(R.id.comment_button)
        private TextView commentButton;
        @ViewInject(R.id.cash_deposit_button)
        private TextView cashDepositButton;
        @ViewInject(R.id.cash_deposit_image)
        private ImageView cashDepositImage;
        @ViewInject(R.id.comment_image)
        private ImageView commentImage;


        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
