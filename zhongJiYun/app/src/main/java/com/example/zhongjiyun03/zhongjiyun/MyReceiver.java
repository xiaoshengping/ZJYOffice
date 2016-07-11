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

import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
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

            //打开自定义的Activity
            Intent i = new Intent(context, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

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
