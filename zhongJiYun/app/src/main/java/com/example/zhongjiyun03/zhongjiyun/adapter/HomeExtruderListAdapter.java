package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.home.MyExtruderBean;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.uilts.CommitCashDepositActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.RentOutExtruderActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.SellExtruderActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 */
public class HomeExtruderListAdapter extends AppBaseAdapter<MyExtruderBean> implements View.OnClickListener {
      private ViewHold viewHold;
      private int positions;


    public HomeExtruderListAdapter(List<MyExtruderBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
          if (convertView==null){
              convertView= LayoutInflater.from(context).inflate(R.layout.myextruder_list_layout,null);
              viewHold=new ViewHold(convertView);
              convertView.setTag(viewHold);

          }else {

           viewHold= (ViewHold) convertView.getTag();

          }
        positions=position;
        inti(position);
        return convertView;
    }

    private void inti(int position) {
        if (data!=null){
            if (!TextUtils.isEmpty(data.get(position).getDeviceNo())){
                viewHold.numberTextView.setText(data.get(position).getDeviceNo());

            }
            if (!TextUtils.isEmpty(data.get(position).getDateOfManufacture())){
                viewHold.neirongTextView.setText(data.get(position).getDateOfManufacture());

            }
            if (!TextUtils.isEmpty(data.get(position).getThumbnail())){
                MyAppliction.imageLoader.displayImage(data.get(position).getThumbnail(),viewHold.imageView,MyAppliction.options);

            }
            viewHold.tailtTextView.setText(data.get(position).getManufacture()+data.get(position).getNoOfManufacture());
            if (data.get(position).getAuditStutas()==1){
                viewHold.auditStateImage.setBackgroundResource(R.mipmap.attestation_icon);

            }else if (data.get(position).getAuditStutas()==0){
                viewHold.auditStateImage.setBackgroundResource(R.mipmap.examine_icon);
            }
            if (!TextUtils.isEmpty(data.get(position).getSecondHandId())){
                  if (data.get(position).getSecondHandState()==1){
                      viewHold.imageChuzTage.setBackgroundResource(R.mipmap.leave_state);
                      if (data.get(position).getSecondHandType()==0){

                          viewHold.sellTextView.setText("撤销出售");
                          Drawable drawable= context.getResources().getDrawable(R.drawable.retract_icon);
                          drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
                          viewHold.sellTextView.setCompoundDrawables(drawable, null , null , null );
                          Drawable drawableOne= context.getResources().getDrawable(R.mipmap.update_icon);
                          drawableOne.setBounds( 0 ,  0 , drawableOne.getMinimumWidth(), drawableOne.getMinimumHeight());
                          viewHold.rentOutTextView.setCompoundDrawables(drawableOne, null , null , null );
                          viewHold.rentOutTextView.setText("更新出售信息");
                      }else if (data.get(position).getSecondHandType()==1){
                          viewHold.rentOutTextView.setText("撤销出租");
                          Drawable drawable= context.getResources().getDrawable(R.drawable.retract_icon);
                          drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
                          viewHold.sellTextView.setCompoundDrawables(drawable, null , null , null );
                          Drawable drawableOne= context.getResources().getDrawable(R.mipmap.update_icon);
                          drawableOne.setBounds( 0 ,  0 , drawableOne.getMinimumWidth(), drawableOne.getMinimumHeight());
                          viewHold.rentOutTextView.setCompoundDrawables(drawableOne, null , null , null );
                          viewHold.rentOutTextView.setText("更新出租信息");
                      }

                  }else if (data.get(position).getSecondHandState()==0){
                      viewHold.imageChuzTage.setBackgroundResource(R.mipmap.audit_ing_icon);
                      if (data.get(position).getSecondHandType()==0){
                          viewHold.sellTextView.setText("撤销出售");
                          Drawable drawable= context.getResources().getDrawable(R.drawable.retract_icon);
                          drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
                          viewHold.sellTextView.setCompoundDrawables(drawable, null , null , null );
                          Drawable drawableOne= context.getResources().getDrawable(R.drawable.used_rig_bond);
                          drawableOne.setBounds( 0 ,  0 , drawableOne.getMinimumWidth(), drawableOne.getMinimumHeight());
                          viewHold.rentOutTextView.setCompoundDrawables(drawableOne, null , null , null );
                          viewHold.rentOutTextView.setText("缴纳保证金");
                          viewHold.rentOutTextView.setTextColor(context.getResources().getColor(R.color.yellow));
                      }else if (data.get(position).getSecondHandType()==1){
                          viewHold.sellTextView.setText("撤销出租");
                          Drawable drawable= context.getResources().getDrawable(R.drawable.retract_icon);
                          drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
                          viewHold.sellTextView.setCompoundDrawables(drawable, null , null , null );
                          Drawable drawableOne= context.getResources().getDrawable(R.drawable.used_rig_bond);
                          drawableOne.setBounds( 0 ,  0 , drawableOne.getMinimumWidth(), drawableOne.getMinimumHeight());
                          viewHold.rentOutTextView.setCompoundDrawables(drawableOne, null , null , null );
                          viewHold.rentOutTextView.setText("缴纳保证金");
                          viewHold.rentOutTextView.setTextColor(context.getResources().getColor(R.color.yellow));


                      }
                  }


            }else {
                viewHold.sellTextView.setText("我要出售");
                viewHold.rentOutTextView.setText("我要出租");

            }
        }
        viewHold.rentOutTextView.setOnClickListener(this);
        viewHold.sellTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.rent_out_textview:
                 if (!TextUtils.isEmpty(data.get(positions).getSecondHandId())){
                     if (data.get(positions).getSecondHandState()==1){
                         Intent cashIntent=new Intent(context, CommitCashDepositActivity.class);
                         context.startActivity(cashIntent);

                     }else if (data.get(positions).getSecondHandState()==0){

                     }
                 }else {
                     Intent rentOutIntent=new Intent(context, RentOutExtruderActivity.class);
                     rentOutIntent.putExtra("data",data.get(positions));
                     context.startActivity(rentOutIntent);
                 }

                break;
            case  R.id.sell_text_view:
                if (!TextUtils.isEmpty(data.get(positions).getSecondHandId())){

                    if (data.get(positions).getSecondHandState()==0){
                        MyAppliction.showToast("已撤销出售");
                        viewHold.sellTextView.setText("我要出售");
                        Drawable drawable= context.getResources().getDrawable(R.mipmap.rig_sell);
                        drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        viewHold.sellTextView.setCompoundDrawables(drawable, null , null , null );
                    }else if (data.get(positions).getSecondHandState()==1){
                        MyAppliction.showToast("已撤销出租");
                        viewHold.sellTextView.setText("我要出售");
                        Drawable drawable= context.getResources().getDrawable(R.mipmap.rig_sell);
                        drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        viewHold.sellTextView.setCompoundDrawables(drawable, null , null , null );
                    }

                }else {
                    Intent sellIntent=new Intent(context, SellExtruderActivity.class);
                    sellIntent.putExtra("data",data.get(positions));
                    context.startActivity(sellIntent);
                }

                break;


        }
    }




    private class ViewHold {

        @ViewInject(R.id.tailt_text_view)
        private TextView tailtTextView;
        @ViewInject(R.id.rent_out_textview) //出租
        private TextView rentOutTextView;
        @ViewInject(R.id.sell_text_view)  //出售
        private TextView sellTextView;
        @ViewInject(R.id.number_text_view)
        private TextView numberTextView;
        @ViewInject(R.id.neirong_text_view)
        private TextView neirongTextView;
        @ViewInject(R.id.image_view)
        private ImageView imageView;
        @ViewInject(R.id.image_state)
        private ImageView auditStateImage;
        @ViewInject(R.id.image_chuz_tage)
        private ImageView imageChuzTage;

        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
