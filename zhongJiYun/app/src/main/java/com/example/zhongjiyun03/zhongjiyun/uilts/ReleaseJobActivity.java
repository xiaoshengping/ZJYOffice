package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.pickerview.OptionsPickerView;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.home.AppListDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.FacillyChildsBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.FacillyDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityChildsBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.SelectData;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class ReleaseJobActivity extends AppCompatActivity implements View.OnClickListener {



    @ViewInject(R.id.register_tv)
    private TextView comtintJobText;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边


    private View vMasker;     //地址联动
    private OptionsPickerView pvOptions;  //地址联动
    private int addressOptions01; //地域选择标记一级
    private int addressOptions02; //地域选择标记二级
    private int timeOptions; //时间选择标记
    private int typeOptions; //时间选择标记

    @ViewInject(R.id.work_address_layout)
    private RelativeLayout workAddressLayout; //工作区域
    @ViewInject(R.id.work_address_edit)
    private TextView workAddressText;  //工作区域显示
    private String province;  //省份
    private String city;  //城市

    @ViewInject(R.id.compensation_layout)
    private RelativeLayout compensationLayout;  //薪资水平
    @ViewInject(R.id.compensation_edit)
    private TextView compensationText;   //薪资水平显示
    private String timeprojectString;

    @ViewInject(R.id.work_time_layout)
    private RelativeLayout workTimeLayout;  //工作年限
    @ViewInject(R.id.work_time_edit)
    private TextView workTimeText;  //工作年限显示
    private String typeTextString;

    List<FacillyDataBean> facillyDataBeens;//设备厂商数据
    private int facillyOptions01;
    private int facillyOptions02;
    @ViewInject(R.id.drilling_type_layout)
    private RelativeLayout drillingTypeLayout; //钻机型号、
    @ViewInject(R.id.drilling_type_edit)
    private TextView drillingTypeText;  //钻机型号显示
    private String manufacture; //厂商
    private String model;// 型号







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_release_job);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        initView();

    }

    private void initView() {
        comtintJobText.setText("提交");
        titleNemeTv.setText("发布需求");
        comtintJobText.setOnClickListener(this);
        retrunText.setOnClickListener(this);
        workAddressLayout.setOnClickListener(this);
        compensationLayout.setOnClickListener(this);
        workTimeLayout.setOnClickListener(this);
        drillingTypeLayout.setOnClickListener(this);
        facillyDataBeens=new ArrayList<>();



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_tv:

                break;
            case R.id.retrun_text_view:
                finish();
                break;
            case R.id.work_address_layout: //工作区域
                intiPvTime(1);
                break;
            case R.id.compensation_layout: //薪资水平
                intiPvTime(2);
                break;
            case R.id.work_time_layout:  //工作年限
                intiPvTime(3);
                break;
            case R.id.drilling_type_layout:  //钻机型号
                intiPvTime(4);
                break;


        }



    }





    /**
     * 联动
     * @param tage 类型
     */
    private void intiPvTime(final int tage) {
        //地址数据
        final ArrayList<String> options1Items = new ArrayList<String>();
        final ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();

        ProvinceCityDataBean provinceCityDataBean= JSONObject.parseObject(SelectData.selectCityDatas+SelectData.selectCityDataOnes+SelectData.selectCityDataTwos,new TypeReference<ProvinceCityDataBean>(){});
        if (provinceCityDataBean!=null){
            ArrayList<ProvinceCityBean>  options1Itemss= (ArrayList<ProvinceCityBean>) provinceCityDataBean.getProvinceCity();
            for (int i = 0; i <options1Itemss.size() ; i++) {
                options1Items.add(options1Itemss.get(i).getName());
            }
            for (int i = 0; i <options1Items.size() ; i++) {
                ArrayList<ProvinceCityChildsBean> provinceCity=  options1Itemss.get(i).getProvinceCityChilds();
                ArrayList<String> arrayList=new ArrayList<>();
                for (int J = 0; J <provinceCity.size() ; J++) {
                    arrayList.add(provinceCity.get(J).getName());
                }
                options2Items.add(arrayList);

            }
        }
        final ArrayList<String> facilly1Items = new ArrayList<String>();
        final ArrayList<ArrayList<String>> facilly2Items = new ArrayList<ArrayList<String>>();
        //设备厂商
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("DeviceJsonType","4");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getFacillyData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("设备厂商",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppListDataBean<FacillyDataBean> appListDataBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppListDataBean<FacillyDataBean>>(){});
                    if (appListDataBean.getResult().equals("success")){
                        ArrayList<FacillyDataBean> facillyDataBeen= (ArrayList<FacillyDataBean>) appListDataBean.getData();
                        //facilluyFirstList.addAll(facillyDataBeen);
                        if (facillyDataBeen.size()!=0){
                            for (int i = 0; i <facillyDataBeen.size() ; i++) {
                                facilly1Items.add(facillyDataBeen.get(i).getText());
                            }
                            for (int i = 0; i <facillyDataBeen.size() ; i++) {
                                ArrayList<FacillyChildsBean> facillyChildsData= (ArrayList<FacillyChildsBean>) facillyDataBeen.get(i).getChilds();
                                ArrayList<String> arrayList=new ArrayList<>();
                                for (int j = 0; j <facillyChildsData.size() ; j++) {
                                    arrayList.add(facillyChildsData.get(j).getText());
                                }
                                facilly2Items.add(arrayList);
                            }
                        }


                    }



                }



            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
        //工期数据
        String[] compensationData={"4001-6000","6001-8000","8001-10000","10001-12000","12001-15000","15000以上"};
        final ArrayList<String> compensationList=new ArrayList();
        //机主类型数据
        String[] workTimeData={"不限","1年以下","1-3年","3-5年","5-10年","10年以上"};
        final ArrayList<String> workTimeList=new ArrayList();
        vMasker= findViewById(R.id.vMasker);
        //选项选择器
        pvOptions = new OptionsPickerView(ReleaseJobActivity.this);

        if (tage==1){
            pvOptions.setTitle("选择城市");
            pvOptions.setPicker(options1Items, options2Items,true);
            //设置默认选中的三级项目
            pvOptions.setSelectOptions(addressOptions01, addressOptions02);
        }else if (tage==2){
            pvOptions.setTitle("选择薪资");
            for (int i = 0; i <compensationData.length ; i++) {
                compensationList.add(compensationData[i]);
            }
            pvOptions.setPicker(compensationList);
            pvOptions.setSelectOptions(timeOptions);
        }else if (tage==3){
            pvOptions.setTitle("选择工作年限");
            for (int i = 0; i < workTimeData.length; i++) {
                workTimeList.add(workTimeData[i]);
            }
            pvOptions.setPicker(workTimeList);
            pvOptions.setSelectOptions(typeOptions);
            Log.e("gqi",typeOptions+"");
        }else {
            pvOptions.setTitle("选择钻机型号");
            pvOptions.setPicker(facilly1Items, facilly2Items,true);
            //设置默认选中的三级项目
            pvOptions.setSelectOptions(facillyOptions01, facillyOptions01);
        }
        pvOptions.setCyclic(false, false, true);


        //监听确定选择按钮

        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                if (tage==1){
                    String addressSelectString = options1Items.get(options1)
                            + options2Items.get(options1).get(option2);
                    if (!TextUtils.isEmpty(addressSelectString)){
                        workAddressText.setText(addressSelectString);
                        //addressTextString=addressSelectString;
                        province=options1Items.get(options1);
                        city=options2Items.get(options1).get(option2);
                        addressOptions01=options1;
                        addressOptions02=option2;
                    }


                }else if (tage==2){
                    if (!TextUtils.isEmpty(compensationList.get(options1))){
                        compensationText.setText(compensationList.get(options1)+" 元");
                        timeprojectString=compensationList.get(options1);
                        timeOptions=options1;
                    }

                }else if (tage==3){
                    if (!TextUtils.isEmpty(workTimeList.get(options1))){
                        workTimeText.setText(workTimeList.get(options1));
                        typeTextString=workTimeList.get(options1);
                        typeOptions=options1;
                    }
                }else {
                    String addressSelectString = facilly1Items.get(options1)
                            + facilly2Items.get(options1).get(option2);
                    if (!TextUtils.isEmpty(addressSelectString)){
                        drillingTypeText.setText(addressSelectString);
                        //addressTextString=addressSelectString;
                        manufacture=options1Items.get(options1);
                        model=options2Items.get(options1).get(option2);
                        facillyOptions01=options1;
                        facillyOptions02=option2;
                    }



                }
                vMasker.setVisibility(View.GONE);
            }
        });

        pvOptions.show();

    }
}
