package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.AppDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.AppListDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.FacillyChildsBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.FacillyDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityChildsBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.SelectData;
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

import java.util.ArrayList;
import java.util.List;

public class ReleaseJobActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {



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
    private String compensationString;

    @ViewInject(R.id.work_time_layout)
    private RelativeLayout workTimeLayout;  //工作年限
    @ViewInject(R.id.work_time_edit)
    private TextView workTimeText;  //工作年限显示
    private String workTimeString;

    List<FacillyDataBean> facillyDataBeens;//设备厂商数据
    private int facillyOptions01;
    private int facillyOptions02;
    @ViewInject(R.id.drilling_type_layout)
    private RelativeLayout drillingTypeLayout; //钻机型号、
    @ViewInject(R.id.drilling_type_edit)
    private TextView drillingTypeText;  //钻机型号显示
    private String manufacture; //厂商
    private String model;// 型号
    private  ArrayList<String> facilly1Items;
    private  ArrayList<ArrayList<String>> facilly2Items;
    @ViewInject(R.id.office_edit)
    private EditText officeEdit;  //任职要求
    @ViewInject(R.id.efficiency_rabutton)
    private CheckBox efficiencyCheck; //包吃住
    @ViewInject(R.id.manner_checkBox)
    private CheckBox manner_checkBox; //加班费
    @ViewInject(R.id.service_checkBox)
    private CheckBox service_checkBox; //节假日休息
    @ViewInject(R.id.rest_rabutton)
    private CheckBox restRabutton;  //其他
    private String efficinecyText;  //包吃住选中text
    private String mannerText;  //加班费选中text
    private String serviceText;  //节假日休息选中text
    private String restText;  //其他选中text
    @ViewInject(R.id.scrollview)
    private ScrollView scrollview;
    private SVProgressHUD mSVProgressHUD;//loding








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

    @Override
    protected void onResume() {
        super.onResume();
        initFacilly();
        //选项选择器
        pvOptions = new OptionsPickerView(ReleaseJobActivity.this);
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

        efficiencyCheck.setOnCheckedChangeListener(this);
        manner_checkBox.setOnCheckedChangeListener(this);
        service_checkBox.setOnCheckedChangeListener(this);
        restRabutton.setOnCheckedChangeListener(this);
        mSVProgressHUD = new SVProgressHUD(this);
        officeEdit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        scrollview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                officeEdit.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               switch (buttonView.getId()){
                   case R.id.efficiency_rabutton:
                       if (isChecked){
                          efficinecyText="包吃住";
                       }else {
                           efficinecyText=null;
                       }

                       break;
                   case R.id.manner_checkBox:
                       if (isChecked){
                          mannerText="加班费";
                       }else {
                           mannerText="";
                       }


                       break;
                   case R.id.service_checkBox:
                       if (isChecked){
                           serviceText="节假日休息";
                       }else {
                           serviceText="";
                       }

                       break;
                   case R.id.rest_rabutton:
                       if (isChecked){
                           restText="其他";
                       }else {
                           restText="";
                       }

                       break;


               }
    }

    private void initFacilly() {
        facilly1Items = new ArrayList<String>();
        facilly2Items = new ArrayList<ArrayList<String>>();
        //设备厂商
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("DeviceJsonType","2");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getFacillyData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("设备厂商",responseInfo.result);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_tv:
                 initCommitData();
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
     * 初始化提交数据
     */
    private void initCommitData() {
        if (!TextUtils.isEmpty(province)&&!TextUtils.isEmpty(city)){
            if (!TextUtils.isEmpty(manufacture)&&!TextUtils.isEmpty(model)){
              if (!TextUtils.isEmpty(compensationString)){
                  if (!TextUtils.isEmpty(workTimeString)){
                      if (!TextUtils.isEmpty(officeEdit.getText().toString())){
                          commitData();

                      }else {
                         MyAppliction.showToast("请填写任职要求");
                      }
                  }else {
                      MyAppliction.showToast("请选择工作年限");
                  }

              }else {
                  MyAppliction.showToast("请选择薪资");
              }

            }else {
                MyAppliction.showToast("请选择钻机型号");
            }


        }else {
            MyAppliction.showToast("请选择工作区域");
        }




    }

    /**
     *
     */

    private void commitData() {
        HttpUtils httpUtils=new HttpUtils();
        final RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("province",province);
        requestParams.addBodyParameter("city",city);
        requestParams.addBodyParameter("manufacture",manufacture);
        requestParams.addBodyParameter("noOfManufacture",model);
        requestParams.addBodyParameter("payLevel",compensationString);
        requestParams.addBodyParameter("workingAge",workTimeString);
        requestParams.addBodyParameter("requirements",officeEdit.getText().toString());
        if (!TextUtils.isEmpty(efficinecyText)&&!TextUtils.isEmpty(mannerText)&&!TextUtils.isEmpty(serviceText)&&!TextUtils.isEmpty(restText)){
            requestParams.addBodyParameter("benefits",efficinecyText+","+mannerText+","+serviceText+","+restText);
        }else if (!TextUtils.isEmpty(efficinecyText)&&!TextUtils.isEmpty(mannerText)&&!TextUtils.isEmpty(serviceText)){
            requestParams.addBodyParameter("benefits",efficinecyText+","+mannerText+","+serviceText);
        }else if (!TextUtils.isEmpty(efficinecyText)&&!TextUtils.isEmpty(mannerText)){
            requestParams.addBodyParameter("benefits",efficinecyText+","+mannerText);
        }else if (!TextUtils.isEmpty(efficinecyText)){
            requestParams.addBodyParameter("benefits",efficinecyText);
        }else if (!TextUtils.isEmpty(mannerText)){
            requestParams.addBodyParameter("benefits",mannerText);
        }else if (!TextUtils.isEmpty(serviceText)){
            requestParams.addBodyParameter("benefits",serviceText);
        }else if (!TextUtils.isEmpty(restText)){
            requestParams.addBodyParameter("benefits",restText);
        }
         //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        mSVProgressHUD.showWithStatus("正在提交中...");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getReleaseJobData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                   if (!TextUtils.isEmpty(responseInfo.result)){
                      AppDataBean appBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
                       if (appBean.getResult().equals("success")){
                           finish();
                           MyAppliction.showToast("发布成功");
                           Intent intent =new Intent(ReleaseJobActivity.this,ReleaseJobListActivity.class);
                           startActivity(intent);
                       }else {
                           MyAppliction.showToast(appBean.getMsg());
                       }


                   }
                mSVProgressHUD.dismiss();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("发布招聘onFailure",s);
                MyAppliction.showToast("网络异常，请稍后重试");
                mSVProgressHUD.dismiss();
            }
        });



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


        //薪资数据
        String[] compensationData={"4001-6000","6001-8000","8001-10000","10001-12000","12001-15000","15000以上"};
        final ArrayList<String> compensationList=new ArrayList();
        //工作年限数据
        String[] workTimeData={"不限","1年以下","1-3年","3-5年","5-10年","10年以上"};
        final ArrayList<String> workTimeList=new ArrayList();
        vMasker= findViewById(R.id.vMasker);


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
        }else {
            pvOptions.setTitle("选择钻机型号");
            pvOptions.setPicker(facilly1Items, facilly2Items,true);
            //设置默认选中的三级项目
            pvOptions.setSelectOptions(facillyOptions01, facillyOptions02);
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
                        compensationString=compensationList.get(options1);
                        timeOptions=options1;
                    }

                }else if (tage==3){
                    if (!TextUtils.isEmpty(workTimeList.get(options1))){
                        workTimeText.setText(workTimeList.get(options1));
                        workTimeString=workTimeList.get(options1);
                        typeOptions=options1;
                    }
                }else {
                    String addressSelectString = facilly1Items.get(options1)
                            + facilly2Items.get(options1).get(option2);
                    if (!TextUtils.isEmpty(addressSelectString)){
                        drillingTypeText.setText(addressSelectString);
                        //addressTextString=addressSelectString;
                        manufacture=facilly1Items.get(options1);
                        model=facilly2Items.get(options1).get(option2);
                        facillyOptions01=options1;
                        facillyOptions02=option2;
                    }



                }
                vMasker.setVisibility(View.GONE);
            }
        });

        pvOptions.show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
// KeyEvent.KEYCODE_BACK代表返回操作.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (pvOptions.isShowing()){
                pvOptions.dismiss();
            }else {
                // 处理返回操作.
                finish();
            }

        }
        return true;
    }


}
