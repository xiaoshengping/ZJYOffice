package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.AppDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
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

import cn.jpush.android.api.JPushInterface;

public class ModificationPhoneActivity extends AppCompatActivity  implements View.OnClickListener {

    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边

    @ViewInject(R.id.phone_text)
    private TextView phoneText;
    @ViewInject(R.id.modify_phone_button)
    private Button modifyPhoneButton;
    @ViewInject(R.id.edit_new_phone)
    private EditText phoneNewEdit;
    @ViewInject(R.id.edit_formmer_phone)
    private EditText editFormmerPhone;
    @ViewInject(R.id.edit_code)
    private EditText editCode;
    private SVProgressHUD mSVProgressHUD;//loding
    @ViewInject(R.id.code_button)
    private Button codeButton;
    private TimeCount time;
    private String phone=null;  //用户手机号
    @ViewInject(R.id.root_layout)
    private ScrollView rootLayout;


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
        setContentView(R.layout.activity_modification_phone);
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
        intiView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLhelper sqLhelper=new SQLhelper(ModificationPhoneActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            phone=cursor.getString(1);
        }
        if (!TextUtils.isEmpty(phone)){
            StringBuffer stringBuffer=new StringBuffer(phone);
            stringBuffer.replace(3,7,"****");
            phoneText.setText(stringBuffer.toString());
            //value.replace("3","*");

        }
        JPushInterface.onResume(this);
    }

    private void intiView() {
        controlKeyboardLayout(rootLayout,modifyPhoneButton);
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("修改手号码");
        retrunText.setOnClickListener(this);
        modifyPhoneButton.setOnClickListener(this);
        codeButton.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象

    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.retrun_text_view:
                    finish();
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                    break;
                case R.id.modify_phone_button:
                    saveData();

                    break;
                case R.id.code_button:
                    intiVcodeData();
                    break;



            }
    }
    private void intiVcodeData() {

        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("PhoneNumber",phoneNewEdit.getText().toString());
        requestParams.addBodyParameter("SmsType","2");

        if (!TextUtils.isEmpty(phoneNewEdit.getText().toString())) {
            if (phoneNewEdit.getText().toString().length()==11){
              if (isMobileNO(phoneNewEdit.getText().toString())){


              if (!(phoneNewEdit.getText().toString()).equals(phone)){



            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getCodeData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        Log.e("登录验证码",responseInfo.result);
                        AppDataBean appDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
                        if ((appDataBean.getResult()).equals("success")){
                            MyAppliction.showToast("验证码已发送成功");
                            time.start();
                            codeButton.setTextColor(getResources().getColor(R.color.tailt_dark));
                            codeButton.setBackgroundResource(R.drawable.gray_button_corners);
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
                  MyAppliction.showToast("请输入新的手机号码");
              }
              }else {
                  MyAppliction.showToast("请输入正确的手机号码");
              }
            }else {
                MyAppliction.showToast("请输入长度为11位的手机号码");
            }
        }else {
            MyAppliction.showToast("请输入您的手机号码");


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
    private void saveData() {

        SQLhelper sqLhelper=new SQLhelper(this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id

        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        if (!phone.equals(editFormmerPhone.getText().toString())){


        if (!TextUtils.isEmpty(phoneNewEdit.getText().toString())){
            if ((phoneNewEdit.getText().toString()).length()==11){
                if (!TextUtils.isEmpty(editCode.getText().toString())){
                    HttpUtils httpUtils=new HttpUtils();
                    RequestParams requestParams=new RequestParams();
                    if (!TextUtils.isEmpty(uid)){
                        requestParams.addBodyParameter("id",uid);
                    }
                    //步骤1：创建一个SharedPreferences接口对象
                    SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
                    //步骤2：获取文件中的值
                    String sesstionId = read.getString("code","");
                    requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
                    requestParams.addBodyParameter("PhoneNumber",phoneNewEdit.getText().toString());
                    requestParams.addBodyParameter("SmsCode",editCode.getText().toString());
                    requestParams.addBodyParameter("oldPhoneNumber",editFormmerPhone.getText().toString());
                    mSVProgressHUD.showWithStatus("提交中...");
                    final String finalUid = uid;
                    httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getModifyPhoneData(),requestParams, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            Log.e("修改手机号",responseInfo.result);
                            if (!TextUtils.isEmpty(responseInfo.result)){
                                AppDataBean appDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
                                if (appDataBean.getResult().equals("success")){
                                    update(finalUid,phoneNewEdit.getText().toString());
                                    mSVProgressHUD.dismiss();
                                    mSVProgressHUD.showSuccessWithStatus("恭喜，修改成功！");
                                    finish();
                                }else {
                                    mSVProgressHUD.dismiss();
                                    mSVProgressHUD.showErrorWithStatus(appDataBean.getMsg());
                                }
                            }else {
                                mSVProgressHUD.dismiss();
                                mSVProgressHUD.showErrorWithStatus("噢噢,修改手机号码失败");
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
            MyAppliction.showToast("请输入您的新手机号码");

        }
        }else {
            MyAppliction.showToast("您输入的新手机号和当前手机号不能一样");
        }

    }

    /**
     * 更新头像
     */
    public void update(String uid,String phoneNumber){
        SQLhelper sqLhelper= new SQLhelper(ModificationPhoneActivity.this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLhelper.PHONENUMBER, phoneNumber);
        db.update(SQLhelper.tableName, contentValues,
                "uid=?", new String[]{uid});
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

}
