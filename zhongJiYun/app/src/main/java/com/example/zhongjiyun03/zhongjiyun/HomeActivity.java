package com.example.zhongjiyun03.zhongjiyun;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.zhongjiyun03.zhongjiyun.fragment.MoreFragment;
import com.example.zhongjiyun03.zhongjiyun.fragment.SeekProjectFragment;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.Base64;
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

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import javax.crypto.Cipher;

import cn.jpush.android.api.JPushInterface;

public class HomeActivity extends AppCompatActivity {
    private ArrayList<Fragment> fragments=new ArrayList<Fragment>();
    @ViewInject(R.id.home_rg)
    private RadioGroup homeRG;
    private long mExitTime;
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
        JPushInterface.init(HomeActivity.this);
        JPushInterface.setDebugMode(true);
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
            MyAppliction.setProjectRefresh("1");


        }else{
            //Log.d("debug", "不是第一次运行");
            Intent intent=new Intent(HomeActivity.this,WelcomeActivity.class);
            startActivity(intent);
        }

        if (isNetworkAvailable(HomeActivity.this)) {
            getVersontData();//版本更新
            /*if (isCommitData) {
                if (!TextUtils.isEmpty(JPushInterface.getRegistrationID(HomeActivity.this))){
                    //Log.e("极光id",JPushInterface.getRegistrationID(HomeActivity.this));
                    initRegistration();//提交用户信息
                }


            }*/
            try {
                userLoginData();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }else {
            //Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
        }

        init();

    }
        //防止sesstion丢失登录
    private void userLoginData() throws Exception {

        if (!TextUtils.isEmpty(MyAppliction.getPhone())){
        String Password = "zjy888888";
        // 从文件中得到公钥
        String key="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDqi/nzVA6vTRoCgzH1zN9KsFz8ph3T4RHzfEPHnpsa2VF1FyhOg34HYiwors5bM87uFvyNAoFOHFt6JdtE8mICBI/PAxBFPy+wP6uUEjZz58MjJwGhTK3t4IP+gbq6sU0I10USFga6UswKWgMCDhfe91FWyXmhTccZcREMKiedIwIDAQAB";
        String phoneNumber = encryptByPublic(MyAppliction.getPhone(),key);
        String password = encryptByPublic(Password,key);
           // Log.e("phoneNumber",phoneNumber);
           // Log.e("password",password);
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("PhoneNumber",phoneNumber);
        requestParams.addBodyParameter("Password",password);
        requestParams.addBodyParameter("UserType","boss");
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String value = read.getString("code","");
        if (!TextUtils.isEmpty(value)){
           requestParams.setHeader("Cookie","ASP.NET_SessionId=" +  value );
        }
        requestParams.addBodyParameter("JiGuangId",JPushInterface.getRegistrationID(HomeActivity.this));
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getUserLoginData(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                   // Log.e("防止sesstion丢失登录",responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("防止sesstion丢失登录onFailure",s);
            }
        });
        }



    }



    /**
     * 使用公钥加密
     *
     * @param content 密文
     * //@param key 公钥
     * @return
     */
    public static String encryptByPublic(String content, String pub_key) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(pub_key);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);
            byte plaintext[] = content.getBytes("UTF-8");
            byte[] output = cipher.doFinal(plaintext);
            String s = new String(Base64.encode(output));
            return s;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 得到公钥
     *
     * //@param algorithm
     * @param bysKey
     * @return
     */
    private static PublicKey getPublicKeyFromX509(String bysKey) throws NoSuchAlgorithmException, Exception {
        byte[] decodedKey = Base64.decode(bysKey);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(x509);
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
    /**
     * 检查当前网络是否可用
     *
     * //@param context
     * @return
     */

    public boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    /*System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());*/
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
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
             //Log.e("获取本版信息",responseInfo.result);
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
                               }else if (versontDataBean.getUpdateLevel()==2){  //强制更新

                                   showGameAlert("版本更新",versontDataBean.getContent(),versontDataBean.getDownloadUrl());
                                   //showExitGameAlert("版本更新","更新的本版号为V"+versontDataBean.getNo(),versontDataBean.getDownloadUrl());
                                   /*Intent intent = new Intent(HomeActivity.this,UpdateService.class);
                                   intent.putExtra("Key_App_Name",appName);
                                   intent.putExtra("Key_Down_Url",versontDataBean.getDownloadUrl());
                                   startService(intent);*/
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

    /**
     * 功能：强制更新
     * @param text
     * @param url
     */
    private void showGameAlert(String text,String texttv, final String url) {
        final AlertDialog dlg = new AlertDialog.Builder(HomeActivity.this).create();
        dlg.setCancelable(false);
        dlg.show();

        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.shrew_exit_dialog);
        TextView tailte = (TextView) window.findViewById(R.id.tailte_tv);
        tailte.setText(texttv);
        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        ok.setText("马上更新");
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this,UpdateService.class);
                intent.putExtra("Key_App_Name",appName);
                intent.putExtra("Key_Down_Url",url);
                startService(intent);
                MyAppliction.showToast("已开始下载应用包，可以在通知栏查看下载进度");
                finish();
                dlg.cancel();
            }
        });

        // 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText("稍后更新");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                dlg.cancel();
            }
        });
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
                if (isWifi(HomeActivity.this)){
                    Intent intent = new Intent(HomeActivity.this,UpdateService.class);
                    intent.putExtra("Key_App_Name",appName);
                    intent.putExtra("Key_Down_Url",url);
                    startService(intent);
                }else {
                    showExitGameAlert("你现在使用的是运营商网络，要继续下载？",url);
                }


                //LodingApkData(url);
                alertDialog.cancel();
            }
        });


    }

    /**
     * make true current connect service is wifi
     * @param mContext
     * @return
     */
    private static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }


    //对话框
    private void showExitGameAlert(String text, final String url) {
        final AlertDialog dlg = new AlertDialog.Builder(HomeActivity.this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.shrew_exit_dialog);
        TextView tailte = (TextView) window.findViewById(R.id.tailte_tv);
        tailte.setText(text);
        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        ok.setText("继续下载");
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,UpdateService.class);
                intent.putExtra("Key_App_Name",appName);
                intent.putExtra("Key_Down_Url",url);
                startService(intent);


                dlg.cancel();
            }
        });

        // 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText("取消下载");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
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
        fragments.add(new MineFragment());
        fragments.add(new MoreFragment());
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
