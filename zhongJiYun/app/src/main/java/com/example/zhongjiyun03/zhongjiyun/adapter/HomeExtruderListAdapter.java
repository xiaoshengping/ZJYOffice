package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.AppDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.MyExtruderBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.example.zhongjiyun03.zhongjiyun.uilts.CommitCashDepositActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.RentOutExtruderActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.SellExtruderActivity;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 */
public class HomeExtruderListAdapter extends AppBaseAdapter<MyExtruderBean> {
      private ViewHold viewHold;
    // 用来记录按钮状态的Map
    public static Map<Integer, Boolean> isChecked;
    private SVProgressHUD mSVProgressHUD;//loding
    private PullToRefreshListView extruderListView;
    public HomeExtruderListAdapter(List<MyExtruderBean> data, Context context,PullToRefreshListView extruderListView) {
        super(data, context);
        this.extruderListView=extruderListView;
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
              convertView= LayoutInflater.from(context).inflate(R.layout.myextruder_list_layout,null);
              viewHold=new ViewHold(convertView);
              convertView.setTag(viewHold);

          }else {

           viewHold= (ViewHold) convertView.getTag();

          }
        initButton();
        inti(position);

        return convertView;
    }

    private void inti(int position) {
        mSVProgressHUD = new SVProgressHUD(context);
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
                      if (data.get(position).getSecondHandState()==1){
                          viewHold.imageChuzTage.setVisibility(View.VISIBLE);
                          viewHold.imageChuzTage.setBackgroundResource(R.mipmap.leave_state);
                      }else {
                          viewHold.imageChuzTage.setBackgroundResource(0);
                      }

                      if (data.get(position).getSecondHandType()==0){
                          viewHold.sellTextView.setText("撤回出租");
                          Drawable drawable= context.getResources().getDrawable(R.drawable.retract_icon);
                          drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
                          viewHold.sellTextView.setCompoundDrawables(drawable, null , null , null );
                          Drawable drawableOne= context.getResources().getDrawable(R.mipmap.update_icon);
                          drawableOne.setBounds( 0 ,  0 , drawableOne.getMinimumWidth(), drawableOne.getMinimumHeight());
                          viewHold.rentOutTextView.setCompoundDrawables(drawableOne, null , null , null );
                          viewHold.rentOutTextView.setText("更新出租信息");
                      }else if (data.get(position).getSecondHandType()==1){
                          viewHold.rentOutTextView.setText("撤回出售");
                          Drawable drawable= context.getResources().getDrawable(R.drawable.retract_icon);
                          drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
                          viewHold.sellTextView.setCompoundDrawables(drawable, null , null , null );
                          Drawable drawableOne= context.getResources().getDrawable(R.mipmap.update_icon);
                          drawableOne.setBounds( 0 ,  0 , drawableOne.getMinimumWidth(), drawableOne.getMinimumHeight());
                          viewHold.rentOutTextView.setCompoundDrawables(drawableOne, null , null , null );
                          viewHold.rentOutTextView.setText("更新出售信息");
                      }

                  }else if (data.get(position).getSecondHandState()==0){
                      if (data.get(position).getSecondHandState()==0){
                          viewHold.imageChuzTage.setVisibility(View.VISIBLE);
                          viewHold.imageChuzTage.setBackgroundResource(R.mipmap.audit_ing_icon);
                      }else {
                          viewHold.imageChuzTage.setBackgroundResource(0);
                      }

                      if (data.get(position).getSecondHandType()==0){
                          viewHold.sellTextView.setText("撤回出租");
                          Drawable drawable= context.getResources().getDrawable(R.drawable.retract_icon);
                          drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
                          viewHold.sellTextView.setCompoundDrawables(drawable, null , null , null );
                          Drawable drawableOne= context.getResources().getDrawable(R.drawable.used_rig_bond);
                          drawableOne.setBounds( 0 ,  0 , drawableOne.getMinimumWidth(), drawableOne.getMinimumHeight());
                          viewHold.rentOutTextView.setCompoundDrawables(drawableOne, null , null , null );
                          viewHold.rentOutTextView.setText("缴纳保证金");
                          viewHold.rentOutTextView.setTextColor(context.getResources().getColor(R.color.yellow));
                      }else if (data.get(position).getSecondHandType()==1){
                          viewHold.sellTextView.setText("撤回出售");
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
                viewHold.sellTextView.setTextColor(context.getResources().getColor(R.color.red));
                viewHold.sellTextView.setText("我要出售");
                Drawable drawable= context.getResources().getDrawable(R.mipmap.rig_sell);
                drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
                viewHold.sellTextView.setCompoundDrawables(drawable, null , null , null );
                Drawable drawableOne= context.getResources().getDrawable(R.mipmap.rig_lease);
                drawableOne.setBounds( 0 ,  0 , drawableOne.getMinimumWidth(), drawableOne.getMinimumHeight());
                viewHold.rentOutTextView.setCompoundDrawables(drawableOne, null , null , null );
                viewHold.rentOutTextView.setText("我要出租");
                viewHold.rentOutTextView.setTextColor(context.getResources().getColor(R.color.blue));



            }
        }
        viewHold.rentOutTextView.setOnClickListener(new rentOutClick(position));
        viewHold.sellTextView.setOnClickListener(new sellClick(position));
    }

    //此为listview条目中的rentOutClick按钮点击事件的写法

    class rentOutClick implements View.OnClickListener {

        private int position;

        public rentOutClick(int pos){  // 在构造时将position传给它这样就知道点击的是哪个条目的按钮
            this.position = pos;
        }
        @Override
        public void onClick(View v) {
            int vid=v.getId();
            if (vid == viewHold.rentOutTextView.getId()){
                if (isChecked.get(position) == false){
                    isChecked.put(position, true);   // 根据点击的情况来将其位置和相应的状态存入
                    if (!TextUtils.isEmpty(data.get(position).getSecondHandId())){
                        if (data.get(position).getSecondHandState()==1){


                        }else if (data.get(position).getSecondHandState()==0){
                            Intent cashIntent=new Intent(context, CommitCashDepositActivity.class);
                            context.startActivity(cashIntent);
                        }
                    }else {
                        Intent rentOutIntent=new Intent(context, RentOutExtruderActivity.class);
                        rentOutIntent.putExtra("data",data.get(position));
                        context.startActivity(rentOutIntent);
                    }
                    //Log.e("steta________", position + "");
                } else if (isChecked.get(position) == true){
                    isChecked.put(position, false);  // 根据点击的情况来将其位置和相应的状态存入

                }
                notifyDataSetChanged();
            }
        }

    }
    //此为listview条目中的sellClick按钮点击事件的写法

    class sellClick implements View.OnClickListener {

        private int position;

        public sellClick(int pos){  // 在构造时将position传给它这样就知道点击的是哪个条目的按钮
            this.position = pos;
        }
        @Override
        public void onClick(View v) {
            int vid=v.getId();
            if (vid == viewHold.sellTextView.getId()){
                if (isChecked.get(position) == false){
                    isChecked.put(position, true);   // 根据点击的情况来将其位置和相应的状态存入
                    Log.e("secondHandId",data.get(position).getSecondHandId()+"---"+position);
                    if (!TextUtils.isEmpty(data.get(position).getSecondHandId())){
                        if (data.get(position).getSecondHandType()==0){
                            MyAppliction.showToast("已撤回出租");
                            recallRentOutData(position);

                        }else if (data.get(position).getSecondHandType()==1){
                            MyAppliction.showToast("已撤回出售");
                            recallRentOutData(position );

                        }

                    }else {
                        Intent sellIntent=new Intent(context, SellExtruderActivity.class);
                        sellIntent.putExtra("data",data.get(position));
                        context.startActivity(sellIntent);
                    }
                    //Log.e("steta________", position + "");
                } else if (isChecked.get(position) == true){
                    isChecked.put(position, false);  // 根据点击的情况来将其位置和相应的状态存入

                }
                notifyDataSetChanged();
            }
        }

    }

    private void recallRentOutData(int position) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        SQLhelper sqLhelper=new SQLhelper(context);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        if (!TextUtils.isEmpty(uid)){
            //步骤1：创建一个SharedPreferences接口对象
            SharedPreferences read = context.getSharedPreferences("lock", context.MODE_WORLD_READABLE);
            //步骤2：获取文件中的值
            String sesstionId = read.getString("code","");
            requestParams.addBodyParameter("id",uid);
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
            requestParams.addBodyParameter("deviceId",data.get(position).getId());
            requestParams.addBodyParameter("deviceHistoryId",data.get(position).getSecondHandId());

            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRecallRentOrSellData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (!TextUtils.isEmpty(responseInfo.result)){
                       Log.e("撤回出售",responseInfo.result);
                        AppDataBean appDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
                        if (appDataBean.getResult().equals("success")){
                            mSVProgressHUD.showSuccessWithStatus("撤回成功");
                            extruderListView.setRefreshing();
                        }else if (appDataBean.getResult().equals("fail")){
                            mSVProgressHUD.showErrorWithStatus("撤回失败");

                        }

                    }


                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.e("撤回出售",s);
                }
            });
        }else {
            MyAppliction.showToast("撤回失败");
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
