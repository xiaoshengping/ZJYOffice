package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

public class CommentOwnerActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边


    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.company_text)
    private TextView companyText;
    @ViewInject(R.id.rating_bar)
    private RatingBar ratingBar;
    @ViewInject(R.id.comit_button)
    private Button comitButton;
    @ViewInject(R.id.comment_text)
    private EditText commentText;
    private SVProgressHUD mSVProgressHUD;//loding
    private int ratings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_owner);
        ViewUtils.inject(this);
        init();


    }

    private void init() {
        initView();

    }

    private void initView() {

        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("评价");
        retrunText.setOnClickListener(this);
        comitButton.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);
        tailtText.setText(getIntent().getStringExtra("ProjectTitle"));
        companyText.setText(getIntent().getStringExtra("ProjectCompany"));

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                ratings=Math.round(rating);


            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;
            case R.id.comit_button:
                cimitData();
                break;




        }




    }

    private void cimitData() {
        SQLhelper sqLhelper=new SQLhelper(CommentOwnerActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id

        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        //
        if (!TextUtils.isEmpty(getIntent().getStringExtra("projectId"))){
            if (ratings!=0.0){
            if (!TextUtils.isEmpty(commentText.getText().toString())){
            HttpUtils httpUtils=new HttpUtils();
            RequestParams requestParams=new RequestParams();
            requestParams.addBodyParameter("id",uid);
            requestParams.addBodyParameter("projectId",getIntent().getStringExtra("projectId"));
            //步骤1：创建一个SharedPreferences接口对象
            SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
            //步骤2：获取文件中的值
            String sesstionId = read.getString("code","");
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
            requestParams.addBodyParameter("commentary",commentText.getText().toString());
            requestParams.addBodyParameter("attitudeScore",ratings+"");
            mSVProgressHUD.showWithStatus("正在提交...");
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getCommentOwnerData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("评价业主",responseInfo.result);
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        AppDataBean appDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
                        if (appDataBean.getResult().equals("success")){
                            mSVProgressHUD.dismiss();
                            MyAppliction.showToast("评论成功");
                            finish();
                        }else {
                            mSVProgressHUD.dismiss();
                            MyAppliction.showToast("评论失败");
                        }

                    }else {
                        mSVProgressHUD.dismiss();
                        MyAppliction.showToast("评论失败");
                    }

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    mSVProgressHUD.dismiss();
                    MyAppliction.showToast("网络异常,请稍后重试");
                }
            });

            }else {
                MyAppliction.showToast("请填写一下评论内容");

            }
            }else {
                MyAppliction.showToast("请选择服务态度");
            }
        }else {

            MyAppliction.showToast("提交数据失败");
        }








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
