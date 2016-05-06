package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
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

public class  CompetitiveDescribeActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边

    @ViewInject(R.id.competitive_button)
    private Button competitiveButton;
    @ViewInject(R.id.edit_text)
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitive_describe);
        ViewUtils.inject(this);
        inti();

    }

    private void inti() {
        initView();
    }



    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("竞标描述");
        retrunText.setOnClickListener(this);
        competitiveButton.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        SQLhelper sqLhelper=new SQLhelper(CompetitiveDescribeActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        switch (v.getId()){

            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;
            case R.id.competitive_button:
                if (!TextUtils.isEmpty(uid)&&!TextUtils.isEmpty(getIntent().getStringExtra("ProjectId"))){
                    if (!TextUtils.isEmpty(editText.getText().toString())){
                        commintData(uid,getIntent().getStringExtra("ProjectId"),editText.getText().toString());
                    }else {
                        MyAppliction.showToast("请输入竞标描述");
                    }

                }else {
                    MyAppliction.showToast("提交失败");
                }


                break;




        }




    }
    /*
    * 提交数据
    *
    * uid用户id
    * ProjectId项目id
    * text 竞标内容
    *
    * */
    private void commintData(String uid,String ProjectId,String text) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        requestParams.addBodyParameter("Id",uid);
        requestParams.addBodyParameter("ProjectId",ProjectId);
        requestParams.addBodyParameter("Description",text);

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getCompetitiveDescribeData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("项目竞标",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppDataBean appDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
                    if (appDataBean.getResult().equals("success")){

                        showExitGameAlert("\u3000\u3000"+"尊敬的用户,您已成功竞标,请您耐心等待业主选标,为提高您中标的概率,现建议您先缴纳1000元的保证金,谢谢!");
                    }else {
                        MyAppliction.showToast(appDataBean.getMsg());
                    }
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("项目竞标",s);
                MyAppliction.showToast("网络异常，请稍后重试");
            }
        });


    }

    //对话框
    private void showExitGameAlert(String text) {
        final AlertDialog dlg = new AlertDialog.Builder(CompetitiveDescribeActivity.this).create();
        dlg.show();
        dlg.setCanceledOnTouchOutside(false);
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.commit_cash_deposit_layout);
        TextView tailte = (TextView) window.findViewById(R.id.tailte_tv);
        tailte.setText(text);
        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        ok.setText("缴纳保证金");
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(CompetitiveDescribeActivity.this,CommitCashDepositActivity.class);
                intent.putExtra("zfuTage","project");
                startActivity(intent);
                dlg.cancel();
            }
        });

        // 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText("取消");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
                finish();
            }
        });
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
