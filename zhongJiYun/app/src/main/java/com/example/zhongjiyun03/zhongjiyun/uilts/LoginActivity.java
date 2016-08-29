package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.AppDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.AppUserDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.main.LoginDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.MyCookieStore;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.register_tv)
    private TextView registerTv;  //头部右边
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边
    @ViewInject(R.id.root_layout)
    private ScrollView rootLayout;


    @ViewInject(R.id.login_button)
    private Button loginButton;  //登录按钮
    @ViewInject(R.id.edit_phone)
    private EditText phoneEdit;  //手机号输入框
    @ViewInject(R.id.edit_code)
    private EditText codeEdit;   //验证码输入框
    private SVProgressHUD mSVProgressHUD;//loding
    private TimeCount time;      //获取验证码计时线程
    @ViewInject(R.id.code_button)
    private Button codeButton;  //获取验证码按钮
    public static String PHPSESSID = null;
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
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ViewUtils.inject(this);
        inti();


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
    private void inti() {
        initView();


    }

    private void initView() {
        controlKeyboardLayout(rootLayout,loginButton);
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        mSVProgressHUD = new SVProgressHUD(this);
        retrunText.setOnClickListener(this);
        registerTv.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        codeButton.setOnClickListener(this);
        //Log.e("极光id",JPushInterface.getRegistrationID(LoginActivity.this));

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.register_tv:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case R.id.login_button:
                loginData();  //用户登录
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
            case R.id.code_button:
                intiVcodeData();
                break;




        }
    }


    //用户登录

    private void loginData() {
        final HttpUtils httpUtils=new HttpUtils();
        String url=null;

        if (!TextUtils.isEmpty(phoneEdit.getText().toString())){

            if ((phoneEdit.getText().toString()).length()==11){
                if (!TextUtils.isEmpty(codeEdit.getText().toString())){
                    RequestParams requestParams=new RequestParams();
                    requestParams.addBodyParameter("PhoneNumber",phoneEdit.getText().toString());
                    requestParams.addBodyParameter("SmsCode",codeEdit.getText().toString());
                    requestParams.addBodyParameter("userType","boss");
                    requestParams.addBodyParameter("jiGuangID",JPushInterface.getRegistrationID(LoginActivity.this));
                   // mSVProgressHUD.showSuccessWithStatus("恭喜，提交成功！");
                   // mSVProgressHUD.showErrorWithStatus("不约，叔叔我们不约～", SVProgressHUD.SVProgressHUDMaskType.GradientCancel);
                    mSVProgressHUD.showWithStatus("登录中...");
                   //httpUtils.configCookieStore(MyCookieStore.cookieStore);
                    //步骤1：创建一个SharedPreferences接口对象
                    SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
                    //步骤2：获取文件中的值
                    String value = read.getString("code","");
                    if (!TextUtils.isEmpty(value)){
                        requestParams.setHeader("Cookie","ASP.NET_SessionId=" +  value );
                        //Log.e("jdfjfj",value);
                    }
                   httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getLoginData(),requestParams, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            Log.e("登录信息",responseInfo.result);
                           // MyAppliction.showToast("恭喜您,登录成功!");
                            if (!TextUtils.isEmpty(responseInfo.result)){
                                AppBean<AppUserDataBean<LoginDataBean>> appBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<AppUserDataBean<LoginDataBean>>>(){});
                                if (appBean.getResult().equals("success")){
                                    AppUserDataBean appUserDataBean=appBean.getData();
                                    LoginDataBean loginDataBean= (LoginDataBean) appUserDataBean.getUser();
                                    MyAppliction.setProjectRequestTage("login");
                                    // 取得sessionid.........................
                                    DefaultHttpClient dh = (DefaultHttpClient) httpUtils.getHttpClient();
                                    MyCookieStore.cookieStore = dh.getCookieStore();
                                    CookieStore cs = dh.getCookieStore();
                                    List<Cookie> cookies = cs.getCookies();
                                    String aspSesstion=null;
                                    for (int i = 0; i < cookies.size(); i++) {
                                        //这里是读取Cookie['PHPSESSID']的值存在静态变量中，保证每次都是同一个值
                                        if ("ASP.NET_SessionId".equals(cookies.get(i).getName())) {
                                            aspSesstion = cookies.get(i).getValue();
                                            Log.e("ASP.NET_SessionId", aspSesstion);
                                            break;
                                        }
                                    }
                                    if (!TextUtils.isEmpty(aspSesstion)){
                                        //步骤2-1：创建一个SharedPreferences.Editor接口对象，lock表示要写入的XML文件名，MODE_WORLD_WRITEABLE写操作
                                        SharedPreferences.Editor editor = getSharedPreferences("lock", MODE_WORLD_WRITEABLE).edit();
                                        //步骤2-2：将获取过来的值放入文件
                                        editor.putString("code", aspSesstion);
                                        //步骤3：提交
                                        editor.commit();
                                    }

                                    SQLhelper sqLhelper=new SQLhelper(LoginActivity.this);
                                    insertData(sqLhelper, loginDataBean.getId(), loginDataBean.getPhoneNumber(), loginDataBean.getName(), loginDataBean.getStarRate()+"",
                                            loginDataBean.getHeadthumb(), loginDataBean.getRole());

                                    mSVProgressHUD.dismiss();
                                    mSVProgressHUD.showSuccessWithStatus("恭喜，提交成功！");
                                    finish();
                                }else {
                                    mSVProgressHUD.showErrorWithStatus(appBean.getMsg(), SVProgressHUD.SVProgressHUDMaskType.GradientCancel);


                                }

                            }


                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            MyAppliction.showToast("网络异常,请稍后重试!");
                            mSVProgressHUD.dismiss();
                        }
                    });


                }else {

                    MyAppliction.showToast("请输入验证码");
                }



            }else {

                MyAppliction.showToast("请输入11位手机号码");

            }

        }else {
            MyAppliction.showToast("请输入您的手机号码");

        }
    }


    public void insertData(SQLhelper sqLhelper,String uid,String phoneNumber,
                           String name,String StarRate,String Headthumb,String Role){
        SQLiteDatabase db=sqLhelper.getWritableDatabase();
        // db.execSQL("insert into user(uid,userName,userIcon,state) values('战士',3,5,7)");
        ContentValues values=new ContentValues();
        values.put(SQLhelper.UID,uid);
        values.put(SQLhelper.PHONENUMBER,phoneNumber);
        values.put(SQLhelper.NAME, name);
        values.put(SQLhelper.STARRATE, StarRate);
        values.put(SQLhelper.HEADTHUMB, Headthumb);
        values.put(SQLhelper.ROLE, Role);


        db.insert(SQLhelper.tableName, SQLhelper.UID, values);
        db.close();
    }


    private void intiVcodeData() {
        String phone=phoneEdit.getText().toString();
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("PhoneNumber",phone);
        requestParams.addBodyParameter("SmsType","1");

        if (!TextUtils.isEmpty(phone)) {
            if (phone.length()==11){
            if (isMobileNO(phone)){

            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getCodeData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        Log.e("登录验证码",responseInfo.result);
                        AppDataBean appDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
                        if ((appDataBean.getResult()).equals("success")){
                            MyAppliction.showToast("验证码已发送成功");
                            codeButton.setTextColor(getResources().getColor(R.color.tailt_dark));
                            codeButton.setBackgroundResource(R.drawable.gray_button_corners);
                            time.start();
                        }else {
                            MyAppliction.showToast(appDataBean.getMsg());
                        }


                    }


                }

                @Override
                public void onFailure(HttpException e, String s) {
                    //MyAppliction.showToast(s);
                    MyAppliction.showToast("网络异常,请稍后重试");
                }
            });
            }else {
                MyAppliction.showToast("请输入正确的手机号码");
            }
        }else {
            MyAppliction.showToast("请输入长度为11位的手机号码");

        }
        }else {
            MyAppliction.showToast("您输入的手机号码不能为空");

        }

    }

    public static boolean isMobileNO(String mobiles) {
               String telRegex = "13\\d{9}|14[57]\\d{8}|15[012356789]\\d{8}|18[01256789]\\d{8}|17[0678]\\d{8}";
                if (TextUtils.isEmpty(mobiles)) return false;
                 else return mobiles.matches(telRegex);
          }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            codeButton.setText("重新发送");
            codeButton.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            codeButton.setClickable(false);
            codeButton.setText("请稍后"+millisUntilFinished / 1000 + "秒");
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
// KeyEvent.KEYCODE_BACK代表返回操作.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 处理返回操作.
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);

        }
        return true;
    }
   /**
     * @param root 最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    private void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    root.scrollTo(0, srollHeight);
                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }




}
