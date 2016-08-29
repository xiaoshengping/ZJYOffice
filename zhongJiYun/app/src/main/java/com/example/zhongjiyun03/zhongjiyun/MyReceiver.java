package com.example.zhongjiyun03.zhongjiyun;

/**
 * Created by ZHONGJIYUN03 on 2016/7/11.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhongjiyun03.zhongjiyun.bean.JPush.ExtrasBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.uilts.ExturderParticularsActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.HomeMarketActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.HomeTribeActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.JpushWebViewActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.MyExtruderActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.MyRedPacketActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.RawardBuyListActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.RawardSellListActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.ReleaseJobListActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "接收Registration Id : " + regId);
            if (!TextUtils.isEmpty(regId)){
             initRegistration(context,regId);
            }


            //send the Registration Id to your server...
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            //MyAppliction.showToast(extras);
            if (!TextUtils.isEmpty(extras)){
                ExtrasBean extrasBean= JSONObject.parseObject(extras,new TypeReference<ExtrasBean>(){});
                if (extrasBean!=null){
                    if ((extrasBean.getType()).equals("App")){
                        if ((extrasBean.getBLLCode()).equals("App_BossJobsInfo")){ //我的招聘
                            //打开自定义的Activity
                            Intent i = new Intent(context, ReleaseJobListActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }else if ((extrasBean.getBLLCode()).equals("App_Home")){  //app首页

                            Intent i = new Intent(context, HomeActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }else if ((extrasBean.getBLLCode()).equals("App_SaleBuy")){  //悬赏求买
                            Intent i = new Intent(context, RawardBuyListActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);


                        }else if ((extrasBean.getBLLCode()).equals("App_SaleSell")){  //悬赏求卖
                            Intent i=new Intent(context, RawardSellListActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }else if ((extrasBean.getBLLCode()).equals("App_SecondDeviceDetail")){
                            Intent i=new Intent(context, ExturderParticularsActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("secondHandData",extrasBean.getApiParams());
                            context.startActivity(i);
                        }else if ((extrasBean.getBLLCode()).equals("App_DeviceAudit")){
                            Intent i=new Intent(context, MyExtruderActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }else if ((extrasBean.getBLLCode()).equals("App_SendGift")){
                            Intent i=new Intent(context, MyRedPacketActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }else  {
                            Intent i = new Intent(context, HomeActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }


                    }else if ((extrasBean.getType()).equals("WebApp")){
                        if ((extrasBean.getBLLCode()).equals("WebApp_ShoppingHome")){  //商城首页
                            Intent i = new Intent(context, HomeMarketActivity.class);
                            i.putExtra("tage","0");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }else if ((extrasBean.getBLLCode()).equals("WebApp_Product")){ //商城商品
                            Intent i = new Intent(context, HomeMarketActivity.class);
                            i.putExtra("tage","1");
                            i.putExtra("appUrl",extrasBean.getApiUrl());
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);

                        }else if ((extrasBean.getBLLCode()).equals("WebApp_TribePost")){ //部落帖子
                            Intent i = new Intent(context, HomeTribeActivity.class);
                            i.putExtra("tage","3");
                            i.putExtra("appUrl",extrasBean.getApiUrl());
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);

                        }else if ((extrasBean.getBLLCode()).equals("WebApp_Tribe")){ //部落首页
                            Intent i = new Intent(context, HomeTribeActivity.class);
                            i.putExtra("tage","2");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }else {
                            Intent i = new Intent(context, HomeActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }

                    }else if ((extrasBean.getType()).equals("Web")) {
                        Intent jpushIntent=new Intent(context, JpushWebViewActivity.class);
                        jpushIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (!TextUtils.isEmpty(extrasBean.getApiUrl())){
                            jpushIntent.putExtra("url",extrasBean.getApiUrl());
                        }
                        context.startActivity(jpushIntent);
                    }else {
                        Intent i = new Intent(context, HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }



                }


            }else {
                Intent i = new Intent(context, HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }

        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    //提交用户信息
    private void initRegistration(Context context,String jiGuangID) {
        Log.e("提交用户信息","提交用户信息");
        HttpUtils httpUtils=new HttpUtils();
        TelephonyManager telephonyManager =( TelephonyManager )context.getSystemService( Context.TELEPHONY_SERVICE );
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("deviceId",telephonyManager.getDeviceId());
        requestParams.addBodyParameter("deviceOS",android.os.Build.VERSION.RELEASE);
        requestParams.addBodyParameter("deviceType",android.os.Build.MODEL);
        requestParams.addBodyParameter("versionType","0");
        requestParams.addBodyParameter("softUserType","0");
        requestParams.addBodyParameter("jiGuangID",jiGuangID);
        Log.e("极光id", jiGuangID);
        try {
            requestParams.addBodyParameter("softVersion",getVersionName(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpUtils.configSoTimeout(1200000);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRegistrationData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });



    }

    private String getVersionName(Context context) throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("nkey:" + key + ", value:" + bundle.getInt(key));
            } else {
                sb.append("nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }


}
