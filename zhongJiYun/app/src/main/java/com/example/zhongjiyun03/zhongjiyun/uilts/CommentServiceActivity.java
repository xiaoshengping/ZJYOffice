package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.AppDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

public class CommentServiceActivity extends AppCompatActivity implements View.OnClickListener {



    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边
    private SVProgressHUD mSVProgressHUD;//loding

    @ViewInject(R.id.efficiency_rabutton)
    private CheckBox efficiencyChebutton; //维修技术
    @ViewInject(R.id.manner_checkBox)
    private CheckBox mannerCheckBox;  //服务态度
    @ViewInject(R.id.service_checkBox)
    private CheckBox serviceCheckBox;  //维修技术
    @ViewInject(R.id.rest_rabutton)
    private CheckBox restChebutton;  //零配件质量
    @ViewInject(R.id.price_check_button)
    private CheckBox priceCheckButton;  //价格
    @ViewInject(R.id.enter_rating_bar)
    private RatingBar enterRatingBar;
    private String efficiencyString;  //维修技术选中
    private String mannerString;//服务态度选中
    private String serviceString;//维修技术选中
    private String restString;//零配件质量选中
    private String priceString;//价格选中


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_service);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {
        addExtruderTv.setText("提交");
        addExtruderTv.setOnClickListener(this);
        titleNemeTv.setText("我要点评");
        retrunText.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);
        enterRatingBar.setRating(5);
        enterRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating<3.0){
                    efficiencyChebutton.setText("维修技术差");
                    mannerCheckBox.setText("服务态度差");
                    serviceCheckBox.setText("维修速度慢");
                    restChebutton.setText("零配件质量差");
                    priceCheckButton.setText("价格偏贵");
                }else {
                    efficiencyChebutton.setText("维修技术好");
                    mannerCheckBox.setText("服务态度佳");
                    serviceCheckBox.setText("维修速度快");
                    restChebutton.setText("零配件质量可靠");
                    priceCheckButton.setText("价格合理");
                }
            }
        });
        efficiencyChebutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    efficiencyString= (String) efficiencyChebutton.getText();

                }else {
                    efficiencyString="";
                }
            }
        });
        mannerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mannerString= (String) mannerCheckBox.getText();

                }else {
                    mannerString="";
                }
            }
        });
        serviceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    serviceString= (String) serviceCheckBox.getText();

                }else {
                    serviceString="";
                }
            }
        });
        priceCheckButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    priceString= (String) priceCheckButton.getText();

                }else {
                    priceString="";
                }
            }
        });
        restChebutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    restString= (String) restChebutton.getText();
                }else {
                    restString="";
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.register_tv:

                 initData();
                break;


        }

    }

    private void initData() {
        if (enterRatingBar.getRating()>0){
            if (efficiencyChebutton.isChecked()||mannerCheckBox.isChecked()||serviceCheckBox.isChecked()
                    ||restChebutton.isChecked()||priceCheckButton.isChecked()){
                StringBuffer stringBuffer=new StringBuffer();
                if (!TextUtils.isEmpty(efficiencyString)){
                    if (!TextUtils.isEmpty(serviceString)||!TextUtils.isEmpty(restString)||!TextUtils.isEmpty(mannerString)||!TextUtils.isEmpty(priceString)){
                        stringBuffer.append(efficiencyString+",");
                    }else {
                        stringBuffer.append(efficiencyString);
                    }

                }
                if (!TextUtils.isEmpty(mannerString)){
                    if (!TextUtils.isEmpty(serviceString)||!TextUtils.isEmpty(restString)||!TextUtils.isEmpty(priceString)){
                        stringBuffer.append(mannerString+",");
                    }else {
                        stringBuffer.append(mannerString);
                    }

                }
                if (!TextUtils.isEmpty(serviceString)){
                    if (!TextUtils.isEmpty(restString)||!TextUtils.isEmpty(priceString)){
                        stringBuffer.append(serviceString+",");
                    }else {
                        stringBuffer.append(serviceString);
                    }

                }
                if (!TextUtils.isEmpty(restString)){
                    if (!TextUtils.isEmpty(priceString)){
                        stringBuffer.append(restString+",");
                    }else {
                        stringBuffer.append(restString);
                    }

                }
                if (!TextUtils.isEmpty(priceString)){
                    stringBuffer.append(priceString);
                }
                if (!TextUtils.isEmpty(stringBuffer.toString())){
                    initCommitData(stringBuffer.toString());
                }else {
                    MyAppliction.showToast("请选择评价内容");
                }



            }else {
                MyAppliction.showToast("请选择评价内容");
            }

        }else {
            MyAppliction.showToast("请选择星级");
        }

    }

    /**
     * 提交评价数据
     */
    private void initCommitData(String text) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("serviceProviderId",getIntent().getStringExtra("serviceId"));
        requestParams.addBodyParameter("comprehensiveScore",subZeroAndDot(String.valueOf(enterRatingBar.getRating())));
        requestParams.addBodyParameter("commentary",text);
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        mSVProgressHUD.showWithStatus("正在提交中...");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getCommentServiceData(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("评价服务商",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppDataBean appDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
                    if (appDataBean.getResult().equals("success")){
                        finish();
                        mSVProgressHUD.dismiss();
                        MyAppliction.showToast("评价成功");
                    }else {
                        MyAppliction.showToast(appDataBean.getMsg());
                        mSVProgressHUD.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("评价服务商onFailure",s);
                mSVProgressHUD.dismiss();
            }
        });



    }

    /**
     * 转换小数点
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
}
