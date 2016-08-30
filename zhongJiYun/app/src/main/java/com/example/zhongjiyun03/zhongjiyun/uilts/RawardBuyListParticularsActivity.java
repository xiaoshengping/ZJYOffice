package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.RawardBuyParticulasBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RawardBuyListParticularsActivity extends AppCompatActivity implements View.OnClickListener {


    @ViewInject(R.id.register_tv)
    private TextView comtintJobText;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边


    @ViewInject(R.id.name_text)
    private TextView nameText;  //购买人姓名
    @ViewInject(R.id.phone_text)
    private TextView phoneText;  //手机号
    @ViewInject(R.id.address_text)
    private TextView addressText; //所在地
    @ViewInject(R.id.facillty_text)
    private TextView facilltyText;  //品牌
    @ViewInject(R.id.facility_type_text)
    private TextView facilityTypeText; //型号
    @ViewInject(R.id.work_time_text)
    private TextView workTimeText;  //工作小时范围
    @ViewInject(R.id.work_age_text)
    private TextView workAgeText;  //年限范围
    @ViewInject(R.id.buy_number_text)
    private TextView buyNumberText;   //购买数量
    @ViewInject(R.id.detail_content_text)
    private TextView detailContentText;  //求购细节
    @ViewInject(R.id.detail_text)
    private TextView detailText;
    @ViewInject(R.id.raward_layout)
    private RelativeLayout rawardLayout;
    private SVProgressHUD mSVProgressHUD;//loding

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raward_buy_list_particulars);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {
        comtintJobText.setVisibility(View.GONE);
        mSVProgressHUD = new SVProgressHUD(this);
        retrunText.setOnClickListener(this);
        if (getIntent().getStringExtra("tage").equals("buy")){
            titleNemeTv.setText("悬赏求买详情");
            detailText.setText("求购细节");
            initBuyData();
        }else {
            initSellData();
            titleNemeTv.setText("悬赏求卖详情");
            detailText.setText("备注");
        }


    }

    /**
     * 获取求卖详情
     */
    private void initSellData() {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("sellId",getIntent().getStringExtra("sellId").toString());
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        mSVProgressHUD.showWithStatus("加载中...");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRawardSellListParticualsData(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("jjjsjf",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<RawardBuyParticulasBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<RawardBuyParticulasBean>>(){});
                    if (appBean!=null){
                        if (appBean.getResult().equals("success")){

                            RawardBuyParticulasBean rawardBuyParticulasBean=   appBean.getData();
                            if (rawardBuyParticulasBean!=null){
                                nameText.setText(rawardBuyParticulasBean.getManufacture()+rawardBuyParticulasBean.getNoOfManufacture());
                                phoneText.setText("所在地："+rawardBuyParticulasBean.getProvince()+rawardBuyParticulasBean.getCity());
                                addressText.setText("出厂时间："+rawardBuyParticulasBean.getDateOfManufacture()+"年"+rawardBuyParticulasBean.getDateMonthOfManufacture()+"月");
                                facilltyText.setText("出厂编号："+rawardBuyParticulasBean.getDeviceNo());
                                facilityTypeText.setText("价格："+rawardBuyParticulasBean.getPrice());
                                workTimeText.setText("机主姓名："+rawardBuyParticulasBean.getBossName());
                                workAgeText.setText("机主手机："+rawardBuyParticulasBean.getBossPhone());
                                buyNumberText.setText("钻机工作时长："+rawardBuyParticulasBean.getHourOfWork());
                                detailContentText.setText(rawardBuyParticulasBean.getRemark());
                                rawardLayout.setVisibility(View.VISIBLE);
                            }
                        }else {
                            rawardLayout.setVisibility(View.GONE);
                        }


                    }
                    mSVProgressHUD.dismiss();
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                mSVProgressHUD.dismiss();
            }
        });




    }

    /**
     * 获取求买详情数据
     */
    private void initBuyData() {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("buyId",getIntent().getStringExtra("buyId").toString());
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        mSVProgressHUD.showWithStatus("加载中...");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRawardBuyListParticualsData(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("jjjsjf",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<RawardBuyParticulasBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<RawardBuyParticulasBean>>(){});
                    if (appBean!=null){
                        if (appBean.getResult().equals("success")){
                         RawardBuyParticulasBean rawardBuyParticulasBean=   appBean.getData();
                            if (rawardBuyParticulasBean!=null){
                                nameText.setText("购买人姓名："+rawardBuyParticulasBean.getBossName());
                                phoneText.setText("手机号："+rawardBuyParticulasBean.getBossPhone());
                                addressText.setText("所在地："+rawardBuyParticulasBean.getProvince()+rawardBuyParticulasBean.getCity());
                                facilltyText.setText("品牌："+rawardBuyParticulasBean.getManufacture());
                                facilityTypeText.setText("型号："+rawardBuyParticulasBean.getNoOfManufacture());
                                workTimeText.setText("工作小时范围："+rawardBuyParticulasBean.getHourOfWork());
                                workAgeText.setText("年限范围："+rawardBuyParticulasBean.getDateOfManufacture());
                                buyNumberText.setText("购买数量："+rawardBuyParticulasBean.getBuyCount());
                                detailContentText.setText(rawardBuyParticulasBean.getRemark());
                                rawardLayout.setVisibility(View.VISIBLE);
                            }else {
                                rawardLayout.setVisibility(View.GONE);
                            }
                        }


                    }
                    mSVProgressHUD.dismiss();
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                mSVProgressHUD.dismiss();
            }
        });



    }

    @Override
    public void onClick(View v) {

    }
}
