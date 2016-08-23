package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class RewardSellAddActivity extends AppCompatActivity implements View.OnClickListener {

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
    private int timeOptions1; //时间选择标记

    List<FacillyDataBean> facillyDataBeens;//设备厂商数据
    private int facillyOptions01;
    private int facillyOptions02;
    private String manufacture; //厂商
    private String model;// 型号
    private ArrayList<String> facilly1Items;
    private  ArrayList<ArrayList<String>> facilly2Items;
    @ViewInject(R.id.facility_layout)
    private RelativeLayout facilityLayout; //设备型号
    @ViewInject(R.id.facilly_text)
    private TextView facillyText;  //设备型号text
    private String  facillyTextString;  //设备型号String
    @ViewInject(R.id.work_address_layout)
    private RelativeLayout workAddressLayout; //所在地
    @ViewInject(R.id.work_address)
    private TextView workAddressText;  //所在地text
    private String workAddressTextString;  //所在地String
    private String province;  //省份
    private String city;  //城市
    @ViewInject(R.id.work_time_layout)
    private RelativeLayout workTimeLayout; //出厂时间范围
    @ViewInject(R.id.work_time_text)
    private TextView workTimeText;  //出厂时间text
    private String workTimeTextString;  //出厂时间String

    @ViewInject(R.id.facillty_number_edit)
    private EditText facilltyNumberEdit;  //出厂牌编号
    @ViewInject(R.id.work_time_edit)
    private EditText workTimeEdit; //钻机工作时间
    private String dateOfManufacture;  //出厂年份
    private String dateMonthOfManufacture;  //出厂月份

    @ViewInject(R.id.price_edit)
    private EditText priceEdit; //价格
    @ViewInject(R.id.name_edit)
    private EditText nameEdit; //机主姓名
    @ViewInject(R.id.phone_content_text)
    private EditText phoneContentText;  //机主手机号
    @ViewInject(R.id.office_edit)
    private EditText office_edit;  //备注
    private SVProgressHUD mSVProgressHUD;//loding



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_reward_sell_add);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {
        initFacilly();
        mSVProgressHUD = new SVProgressHUD(this);
        comtintJobText.setOnClickListener(this);
        retrunText.setOnClickListener(this);
        facilityLayout.setOnClickListener(this);
        workAddressLayout.setOnClickListener(this);
        workTimeLayout.setOnClickListener(this);
        titleNemeTv.setText("要卖钻机");
        facillyDataBeens=new ArrayList<>();
        comtintJobText.setText("提交");

        /**
         * 限制只能输入字母和数字，默认弹出英文输入法
         */
        facilltyNumberEdit.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }
            @Override
            protected char[] getAcceptedChars() {
                char[] data = getStringData(R.string.login_only_can_input).toCharArray();
                return data;
            }
        });

    }
    public String getStringData(int id) {
        return getResources().getString(id);
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
            case R.id.facility_layout:
                intiPvTime(4);
                break;
            case R.id.work_address_layout:
                intiPvTime(1);
                break;
            case R.id.work_time_layout:
                intiPvTime(2);
                break;

        }
    }

    private void initCommitData() {
        if (!TextUtils.isEmpty(facillyTextString)){
            if (!TextUtils.isEmpty(workAddressTextString)){
                if (!TextUtils.isEmpty(workTimeTextString)){
                   if (!TextUtils.isEmpty(facilltyNumberEdit.getText().toString())){
                       if (!TextUtils.isEmpty(workTimeEdit.getText().toString())){
                           if (!TextUtils.isEmpty(priceEdit.getText().toString())){
                               if (!TextUtils.isEmpty(nameEdit.getText().toString())){
                                   if (nameEdit.getText().toString().length()>1){
                                   if (!TextUtils.isEmpty(phoneContentText.getText().toString())){
                                       if ((phoneContentText.getText().toString()).length()==11){
                                           commitData();
                                       }else {
                                           MyAppliction.showToast("请填写正确的手机号");
                                       }

                                   }else {
                                       MyAppliction.showToast("请填写机主手机号");
                                   }
                                   }else {
                                       MyAppliction.showToast("姓名至少要两个字");
                                   }
                               }else {
                                   MyAppliction.showToast("请填写机主姓名");
                               }

                           }else {
                               MyAppliction.showToast("请填写价格");
                           }

                       }else {
                           MyAppliction.showToast("请填写钻机工作时长");
                       }

                   }else {
                      MyAppliction.showToast("请填写出厂牌编号");
                   }


                }else {
                    MyAppliction.showToast("请选择出厂时间");
                }

            }else {
                MyAppliction.showToast("请选择所在地");
            }

        }else {
            MyAppliction.showToast("请选择设备型号");
        }



    }

    /**
     * 提交数据
     */

    private void commitData() {

        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("bossName",nameEdit.getText().toString());
        requestParams.addBodyParameter("bossPhone",phoneContentText.getText().toString());

        requestParams.addBodyParameter("manufacture",manufacture);
        requestParams.addBodyParameter("noOfManufacture",model);
        requestParams.addBodyParameter("province",province);
        requestParams.addBodyParameter("city",city);
        requestParams.addBodyParameter("hourOfWork",workTimeTextString);
        requestParams.addBodyParameter("dateOfManufacture",dateOfManufacture);
        requestParams.addBodyParameter("dateMonthOfManufacture",dateMonthOfManufacture);
        requestParams.addBodyParameter("deviceNo",facilltyNumberEdit.getText().toString());
        requestParams.addBodyParameter("hourOfWork",workTimeEdit.getText().toString());
        requestParams.addBodyParameter("price",priceEdit.getText().toString());
        if (!TextUtils.isEmpty(office_edit.getText().toString())){
            requestParams.addBodyParameter("remark",office_edit.getText().toString());
        }
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        mSVProgressHUD.showWithStatus("正在提交中...");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getBoundySellData(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("买钻戒",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppDataBean appDataBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
                    if (appDataBean!=null){
                        if (appDataBean.getResult().equals("success")){
                            Intent intent=new Intent(RewardSellAddActivity.this,RawardSellListActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            MyAppliction.showToast(appDataBean.getMsg());
                        }
                    }
                    mSVProgressHUD.dismiss();
                }else {
                    mSVProgressHUD.dismiss();
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
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


        //工作小时数据
        String[] compensationData={"2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013","2014","2015","2016"};
        String[] compensationData1={"01","02","03","04","05","06","07","08","09","10","11","12"};
        final ArrayList<String> compensationList1=new ArrayList();
        final ArrayList<ArrayList<String>> compensationList2=new ArrayList();

        vMasker= findViewById(R.id.vMasker);
        //选项选择器
        pvOptions = new OptionsPickerView(RewardSellAddActivity.this);

        if (tage==1){
            pvOptions.setTitle("选择所在地");
            pvOptions.setPicker(options1Items, options2Items,true);
            //设置默认选中的三级项目
            pvOptions.setSelectOptions(addressOptions01, addressOptions02);
        }else if (tage==2){
            pvOptions.setTitle("选择工作小时");
            for (int i = 0; i <compensationData.length ; i++) {
                compensationList1.add(compensationData[i]);

            }
            for (int i = 0; i <compensationData.length ; i++) {
                ArrayList<String> list=new ArrayList<>();
                for (int J = 0; J <compensationData1.length ; J++) {
                    list.add(compensationData1[J]);
                }
                compensationList2.add(list);
            }

            pvOptions.setPicker(compensationList1,compensationList2,true);
            pvOptions.setSelectOptions(timeOptions,timeOptions1);
        }else {
            pvOptions.setTitle("选择钻机型号");
            if (facilly1Items.size()!=0&&facilly2Items.size()!=0){
                pvOptions.setPicker(facilly1Items, facilly2Items,true);
                //设置默认选中的三级项目
                pvOptions.setSelectOptions(facillyOptions01, facillyOptions01);
            }

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
                        workAddressTextString=addressSelectString;
                        province=options1Items.get(options1);
                        city=options2Items.get(options1).get(option2);
                        addressOptions01=options1;
                        addressOptions02=option2;
                    }


                }else if (tage==2){
                    if (!TextUtils.isEmpty(compensationList1.get(options1))){
                        workTimeText.setText(compensationList1.get(options1)+"年"+compensationList2.get(options1).get(option2)+"月");
                        workTimeTextString=compensationList1.get(options1);
                        dateOfManufacture=compensationList1.get(options1);
                        dateMonthOfManufacture=compensationList2.get(options1).get(option2);
                        timeOptions=options1;
                        timeOptions1=option2;
                    }

                }else {
                    String addressSelectString = facilly1Items.get(options1)
                            + facilly2Items.get(options1).get(option2);
                    if (!TextUtils.isEmpty(addressSelectString)){
                        facillyText.setText(addressSelectString);
                        facillyTextString=addressSelectString;
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
    }

    /*
   * 软键盘隐藏和显示
   * */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


}
