package com.example.zhongjiyun03.zhongjiyun;

import android.annotation.TargetApi;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.VersontDataBean;
import com.example.zhongjiyun03.zhongjiyun.fragment.FragmentTabAdapter;
import com.example.zhongjiyun03.zhongjiyun.fragment.HomeFragment;
import com.example.zhongjiyun03.zhongjiyun.fragment.MineFragment;
import com.example.zhongjiyun03.zhongjiyun.fragment.SeekMachinistFragment;
import com.example.zhongjiyun03.zhongjiyun.fragment.SeekProjectFragment;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.service.UpdateService;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;
import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

public class HomeActivity extends AppCompatActivity {
    private ArrayList<Fragment> fragments=new ArrayList<Fragment>();
    @ViewInject(R.id.home_rg)
    private RadioGroup homeRG;
    private long mExitTime;
    public Boolean isInitTage = true;
    @ViewInject(R.id.progress_bar)
    private ProgressBar progressBar;
    @ViewInject(R.id.loding_layout)
    private LinearLayout loodingLayout;
    @ViewInject(R.id.progress_bar_text)
    private TextView progressBarText;
    private static final int MSG_PROGRESS_UPDATE = 0x110;
    private TextView tailteTv;
    private boolean isCommitData=true;//是否提交用户信息
    public static final String appName = "中基云机主端";



    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //改变状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.red_light);//通知栏所需颜色
        }
        setContentView(R.layout.activity_home);
        ViewUtils.inject(this);


        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFirstRun)
        {
            //Log.d("debug", "第一次运行");
            Intent intent = new Intent().setClass(HomeActivity.this,MainActivity.class);
            startActivityForResult(intent,0);
            testAddContacts();  //添加联系人
            editor.putBoolean("isFirstRun", false);
            editor.commit();


        } else{
            //Log.d("debug", "不是第一次运行");
            Intent intent=new Intent(HomeActivity.this,WelcomeActivity.class);
            startActivity(intent);

            if (isCommitData) {
                initRegistration();//提交用户信息
            }

        }
        getVersontData();//版本更新
        init();
        //HomeFragment.setStart(0);
        //startPage();

        /*PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
        String device_token = Umeng·Registrar.getRegistrationId(this);
        //Log.e("shdhdhdh",device_token);
        //开启推送并设置注册的回调处理
        mPushAgent.enable(new IUmengRegisterCallback() {

            @Override
            public void onRegistered(String registrationId) {
                //onRegistered方法的参数registrationId即是device_token
                Log.d("device_token", registrationId);
            }
        });
        PushAgent.getInstance(this).onAppStart();*/
        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    //提交用户信息
    private void initRegistration() {
        HttpUtils httpUtils=new HttpUtils();
        TelephonyManager telephonyManager =( TelephonyManager )getSystemService( Context.TELEPHONY_SERVICE );
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("deviceId",telephonyManager.getDeviceId());
        requestParams.addBodyParameter("deviceOS",android.os.Build.VERSION.RELEASE);
        requestParams.addBodyParameter("deviceType",android.os.Build.MODEL);
        requestParams.addBodyParameter("versionType","0");
        requestParams.addBodyParameter("softUserType","0");
        requestParams.addBodyParameter("jiGuangID",JPushInterface.getRegistrationID(HomeActivity.this));
        try {
            requestParams.addBodyParameter("softVersion",getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpUtils.configSoTimeout(1200000);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRegistrationData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("极光提交用户信息",responseInfo.result);
                isCommitData=false;
            }

            @Override
            public void onFailure(HttpException e, String s) {
                //Log.e("极光提交用户信息onFailure",s);
                isCommitData=true;
            }
        });



    }




    private void getVersontData() {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        try {
            requestParams.addBodyParameter("versionNo",getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestParams.addBodyParameter("versionType","0");

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getVersonData(),requestParams, new RequestCallBack<String>() {


            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
             Log.e("获取本版信息",responseInfo.result);
               if (!TextUtils.isEmpty(responseInfo.result)){
                   AppBean<VersontDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<VersontDataBean>>(){});
                   if (appBean!=null){
                       if (appBean.getResult().equals("success")){
                         VersontDataBean versontDataBean=  appBean.getData();
                           if (versontDataBean!=null){
                               if (versontDataBean.getUpdateLevel()==1){ //不强制更新
                                   try {
                                      if (!versontDataBean.getNo().equals(getVersionName())){
                                          noShowExitGameAlert("版本更新",versontDataBean.getContent(),versontDataBean.getDownloadUrl());
                                      }

                                   } catch (Exception e) {
                                       e.printStackTrace();
                                   }
                                   //noShowExitGameAlert("版本更新",versontDataBean.getContent(),versontDataBean.getDownloadUrl());


                                   //showExitGameAlert("版本更新","更新的本版号为V"+versontDataBean.getNo(),versontDataBean.getDownloadUrl());
                               }else if (versontDataBean.getUpdateLevel()==2){  //强制更新
                                   /*try {
                                       if (!versontDataBean.getNo().equals(getVersionName())){
                                           showExitGameAlert("版本更新","更新的本版号为V"+versontDataBean.getNo(),versontDataBean.getDownloadUrl());

                                       }

                                   } catch (Exception e) {
                                       e.printStackTrace();
                                   }*/

                                   //showExitGameAlert("版本更新","更新的本版号为V"+versontDataBean.getNo(),versontDataBean.getDownloadUrl());
                                   Intent intent = new Intent(HomeActivity.this,UpdateService.class);
                                   intent.putExtra("Key_App_Name",appName);
                                   intent.putExtra("Key_Down_Url",versontDataBean.getDownloadUrl());
                                   startService(intent);
                               }
                           }
                       }
                   }
               }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("获取本版信息",s);
                init();
            }
        });



    }

    private void LodingApkData(String url) {
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.configSoTimeout(1200000);
        String filePath= Environment.getExternalStorageDirectory()+"/zhongJiYun/";
        String fileName="ZhongJiYun.apk";
        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath+fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        httpUtils.download(url, file.getPath(), new RequestCallBack<File>() {
            @Override
            public void onStart() {
                super.onStart();
                loodingLayout.setVisibility(View.VISIBLE);
                mHandler.sendEmptyMessage(MSG_PROGRESS_UPDATE);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                //progressBar.setMax((int)total);
                //progressBar.setProgress((int)current);
                //progressBarText.setText((int)current+"/"+(int)total);


            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                Log.e("下载",responseInfo.result.toString());
                loodingLayout.setVisibility(View.GONE);

                openFile(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("下载",s);
                loodingLayout.setVisibility(View.GONE);
            }
        });


    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int progress = progressBar.getProgress();
            progressBar.setProgress(++progress);
            if (progress >= 100) {
                mHandler.removeMessages(MSG_PROGRESS_UPDATE);
            }
            if (progress<=100){
                progressBarText.setText(progress+"/100");
            }
            if (progress==100){
                MyAppliction.showToast("下载成功,正在准备安装");

            }
            mHandler.sendEmptyMessageDelayed(MSG_PROGRESS_UPDATE, 100);
        }
    };


    private void openFile(File file) {
        // TODO Auto-generated method stub
        Log.e("OpenFile", file.getName());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        startActivity(intent);

    }

    //不强制下载对话框

    private void noShowExitGameAlert(String text, String textTv, final String url) {

        final AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.no_show_verstion_layout);
        TextView tv_message = (TextView) window.findViewById(R.id.tv_dialog_message);
        tv_message.setText(textTv);
        TextView cancelVerstionButton = (TextView) window.findViewById(R.id.cancel_verstion_button);
        TextView confirmVerstionButton = (TextView) window.findViewById(R.id.confirm_verstion_button);
        cancelVerstionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        confirmVerstionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,UpdateService.class);
                intent.putExtra("Key_App_Name",appName);
                intent.putExtra("Key_Down_Url",url);
                startService(intent);

                //LodingApkData(url);
                alertDialog.cancel();
            }
        });


    }
      //强制下载对话框
    private void showExitGameAlert(String text, String textTv, final String url) {
        /*final AlertDialog dlg = new AlertDialog.Builder(HomeActivity.this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.cell_alert_layout);
        TextView tailte = (TextView) window.findViewById(R.id.tailte_tv);
        tailteTv = (TextView) window.findViewById(R.id.tv);
        tailteTv.setText(text);
        tailteTv.setTextColor(getResources().getColor(R.color.tailt_dark));
        tailte.setText(textTv);
        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        ok.setText("确定");
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                LodingApkData(url);
                dlg.cancel();
            }
        });*/

        /*// 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText("取消");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });*/


       /* AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.no_show_verstion_layout);
        *//*TextView tv_title = (TextView) window.findViewById(R.id.tv_dialog_title);
        tv_title.setText("详细信息");*//*
        TextView tv_message = (TextView) window.findViewById(R.id.tv_dialog_message);
        tv_message.setText("sdhdhdhdhh");*/

    }



    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }

    /**
     * 添加联系人
     * 在同一个事务中完成联系人各项数据的添加
     * 使用ArrayList<ContentProviderOperation>，把每步操作放在它的对象中执行
     * */
    public void testAddContacts(){
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = getContentResolver();
        // 第一个参数：内容提供者的主机名
        // 第二个参数：要执行的操作
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();

        // 操作1.添加Google账号，这里值为null，表示不添加
        ContentProviderOperation operation = ContentProviderOperation.newInsert(uri)
                .withValue("account_name", null)// account_name:Google账号
                .build();

        // 操作2.添加data表中name字段
        uri = Uri.parse("content://com.android.contacts/data");
        ContentProviderOperation operation2 = ContentProviderOperation.newInsert(uri)
                // 第二个参数int previousResult:表示上一个操作的位于operations的第0个索引，
                // 所以能够将上一个操作返回的raw_contact_id作为该方法的参数
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/name")
                .withValue("data2", "中基云免费电话")
                .build();

        // 操作3.添加data表中phone字段
        uri = Uri.parse("content://com.android.contacts/data");
        ContentProviderOperation operation3 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                .withValue("data2", "2")
                .withValue("data1", "02038361432")
                .build();
        uri = Uri.parse("content://com.android.contacts/data");
        ContentProviderOperation operation4 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                .withValue("data2", "2")
                .withValue("data1", "02038258459")
                .build();
        uri = Uri.parse("content://com.android.contacts/data");
        ContentProviderOperation operation5 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                .withValue("data2", "2")
                .withValue("data1", "4002029797")
                .build();
        uri = Uri.parse("content://com.android.contacts/data");
        ContentProviderOperation operation6 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                .withValue("data2", "2")
                .withValue("data1", "18515333333")
                .build();
        operations.add(operation);
        operations.add(operation2);
        operations.add(operation3);
        operations.add(operation4);
        operations.add(operation5);
        operations.add(operation6);


        try {
            resolver.applyBatch("com.android.contacts", operations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init() {
        addFragment();
        FragmentTabAdapter fragmentTabAdapter=new FragmentTabAdapter(HomeActivity.this,fragments,R.id.home_layout,homeRG);
        //getVersontData();   //获取本版

    }




    private void addFragment() {
        fragments.add(new HomeFragment());
        fragments.add(new SeekProjectFragment());
        fragments.add(new SeekMachinistFragment());
        fragments.add(new MineFragment());
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                HomeActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }






}
