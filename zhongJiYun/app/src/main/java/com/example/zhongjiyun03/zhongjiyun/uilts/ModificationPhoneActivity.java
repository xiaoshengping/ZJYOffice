package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    @ViewInject(R.id.edit_phone)
    private EditText phoneEdit;
    @ViewInject(R.id.edit_code)
    private EditText editCode;
    private SVProgressHUD mSVProgressHUD;//loding
    @ViewInject(R.id.code_button)
    private Button codeButton;
    private TimeCount time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_phone);
        ViewUtils.inject(this);
        inti();




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
        String phone=null;  //用户id

        while (cursor.moveToNext()) {

            phone=cursor.getString(1);

        }
        if (!TextUtils.isEmpty(phone)){
            phoneText.setText(phone);

        }
    }

    private void intiView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("修改机手号码");
        retrunText.setOnClickListener(this);
        modifyPhoneButton.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象

    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.retrun_text_view:
                    finish();
                    overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
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
        String phone=phoneEdit.getText().toString();
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("PhoneNumber",phone);
        requestParams.addBodyParameter("SmsType","1");
        if (!TextUtils.isEmpty(phone)&&phone.length()==11) {
            time.start();
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getCodeData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        Log.e("登录验证码",responseInfo.result);
                        AppDataBean appDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
                        if ((appDataBean.getResult()).equals("success")){
                            MyAppliction.showToast("验证码已发送成功");

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
            MyAppliction.showToast("您输入的手机号码有误");


        }

    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            codeButton.setText("重新验证");
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

        if (!TextUtils.isEmpty(phoneEdit.getText().toString())){
            if ((phoneEdit.getText().toString()).length()==11){
                if (!TextUtils.isEmpty(editCode.getText().toString())){
                    HttpUtils httpUtils=new HttpUtils();
                    RequestParams requestParams=new RequestParams();
                    if (!TextUtils.isEmpty(uid)){
                        requestParams.addBodyParameter("Id",uid);
                    }
                    requestParams.addBodyParameter("PhoneNumber",phoneEdit.getText().toString());
                    requestParams.addBodyParameter("SmsCode",editCode.getText().toString());
                    mSVProgressHUD.showWithStatus("提交中...");
                    final String finalUid = uid;
                    httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getModifyPhoneData(),requestParams, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            Log.e("修改手机号",responseInfo.result);
                            if (!TextUtils.isEmpty(responseInfo.result)){
                                AppDataBean appDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
                                if (appDataBean.getResult().equals("success")){
                                    update(finalUid,phoneEdit.getText().toString());
                                    mSVProgressHUD.dismiss();
                                    mSVProgressHUD.showSuccessWithStatus("恭喜，修改成功！");
                                    finish();
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
            MyAppliction.showToast("请输入您的手机号码");

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



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
// KeyEvent.KEYCODE_BACK代表返回操作.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 处理返回操作.
            finish();
            overridePendingTransition(R.anim.anim_open, R.anim.anim_close);

        }
        return true;
    }

}
