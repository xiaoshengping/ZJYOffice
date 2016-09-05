package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
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

public class RewardBuyAddActivity extends AppCompatActivity implements View.OnClickListener {


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

    @ViewInject(R.id.name_edit)
    private EditText nameEdit; //购买人姓名
    @ViewInject(R.id.phone_content_text)
    private EditText phoneContentText;  //手机号
    @ViewInject(R.id.buy_number_edit)
    private EditText buyNumberEdit;  //购买数量

    List<FacillyDataBean> facillyDataBeens;//设备厂商数据
    private int facillyOptions01;
    private int facillyOptions02;
    private String manufacture; //厂商
    private String model;// 型号
    private  ArrayList<String> facilly1Items;
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
    private RelativeLayout workTimeLayout; //工作小时范围
    @ViewInject(R.id.work_time_text)
    private TextView workTimeText;  //工作小时范围text
    private String workTimeTextString;  //工作小时范围String
    @ViewInject(R.id.work_data_layout)
    private RelativeLayout workDataLayout;  //工作年限范围
    @ViewInject(R.id.work_data_text)
    private TextView workDataText; //工作年限范围text
    private String workDataTextString; //工作年限范围String
    @ViewInject(R.id.office_edit)
    private EditText officeEdit;  //求购细节
    private SVProgressHUD mSVProgressHUD;//loding


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_reward_buy_add);
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
    }

    private void initView() {

        mSVProgressHUD = new SVProgressHUD(this);
        comtintJobText.setOnClickListener(this);
        retrunText.setOnClickListener(this);
        facilityLayout.setOnClickListener(this);
        workAddressLayout.setOnClickListener(this);
        workTimeLayout.setOnClickListener(this);
        workDataLayout.setOnClickListener(this);
        titleNemeTv.setText("要买钻机");
        facillyDataBeens=new ArrayList<>();
        comtintJobText.setText("提交");

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
            case R.id.work_data_layout:
                intiPvTime(3);
                break;

        }
    }

    /**
     * 初始化数据
     */
    private void initCommitData() {

        if (!TextUtils.isEmpty(nameEdit.getText().toString())){
            if (nameEdit.getText().toString().length()>1){
            if (!TextUtils.isEmpty(phoneContentText.getText().toString())){
                if (phoneContentText.getText().toString().length()==11){
                if (!TextUtils.isEmpty(buyNumberEdit.getText().toString())){
                    if (!TextUtils.isEmpty(facillyTextString)){
                        if (!TextUtils.isEmpty(workAddressTextString)){
                            if (!TextUtils.isEmpty(workTimeTextString)){
                                if (!TextUtils.isEmpty(workDataTextString)){
                                    if (!TextUtils.isEmpty(officeEdit.getText().toString())){

                                        commitData();

                                    }else {
                                        MyAppliction.showToast("请输入求购细节");
                                    }

                                }else {
                                    MyAppliction.showToast("请选择年限范围");
                                }
                            }else {
                                MyAppliction.showToast("请选择工作小时范围");
                            }

                        }else {
                            MyAppliction.showToast("请选择所在地");
                        }
                    }else {
                        MyAppliction.showToast("请选择设备型号");
                    }
                }else {

                    MyAppliction.showToast("请输入购买数量");
                }
                }else {
                    MyAppliction.showToast("请输入长度为11位的手机号");
                }
            }else {
              MyAppliction.showToast("请输入手机号");
            }
            }else {

                MyAppliction.showToast("购买人姓名至少要2个字");
            }
        }else {

            MyAppliction.showToast("请输入购买人姓名");
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
        requestParams.addBodyParameter("buyCount",buyNumberEdit.getText().toString());
        requestParams.addBodyParameter("manufacture",manufacture);
        requestParams.addBodyParameter("noOfManufacture",model);
        requestParams.addBodyParameter("province",province);
        requestParams.addBodyParameter("city",city);
        requestParams.addBodyParameter("hourOfWork",workTimeTextString);
        requestParams.addBodyParameter("dateOfManufacture",workDataTextString);
        requestParams.addBodyParameter("remark",officeEdit.getText().toString());
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        mSVProgressHUD.showWithStatus("正在提交中...");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getBoundyBuyData(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                   //Log.e("买钻戒",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppDataBean appDataBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
                    if (appDataBean!=null){
                        if (appDataBean.getResult().equals("success")){
                            Intent intent=new Intent(RewardBuyAddActivity.this,RawardBuyListActivity.class);
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
        String[] compensationData={"2000以内","2000(含)-5000","5000(含)-8000","8000及以上"};
        final ArrayList<String> compensationList=new ArrayList();
        //工作年限数据
        String[] workTimeData={"1年以下","1(含)-2年","2(含)-3年","3年及以上"};
        final ArrayList<String> workTimeList=new ArrayList();
        vMasker= findViewById(R.id.vMasker);
        //选项选择器
        pvOptions = new OptionsPickerView(RewardBuyAddActivity.this);

        if (tage==1){
            pvOptions.setTitle("选择所在地");
            pvOptions.setPicker(options1Items, options2Items,true);
            //设置默认选中的三级项目
            pvOptions.setSelectOptions(addressOptions01, addressOptions02);
        }else if (tage==2){
            pvOptions.setTitle("选择工作小时");
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
            pvOptions.setTitle("选择设备型号");
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
                        workAddressTextString=addressSelectString;
                        province=options1Items.get(options1);
                        city=options2Items.get(options1).get(option2);
                        addressOptions01=options1;
                        addressOptions02=option2;
                    }


                }else if (tage==2){
                    if (!TextUtils.isEmpty(compensationList.get(options1))){
                        workTimeText.setText(compensationList.get(options1)+" (小时)");
                        workTimeTextString=compensationList.get(options1);
                        timeOptions=options1;
                    }

                }else if (tage==3){
                    if (!TextUtils.isEmpty(workTimeList.get(options1))){
                        workDataText.setText(workTimeList.get(options1));
                        workDataTextString=workTimeList.get(options1);
                        typeOptions=options1;
                    }
                }else {
                    String addressSelectString = facilly1Items.get(options1)
                            + facilly2Items.get(options1).get(option2);
                    if (!TextUtils.isEmpty(addressSelectString)){

                        facillyTextString=addressSelectString;
                        manufacture=facilly1Items.get(options1);
                        model=facilly2Items.get(options1).get(option2);
                        facillyOptions01=options1;
                        facillyOptions02=option2;

                        facillyText.setText(facillyTextString);

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
        requestParams.addBodyParameter("DeviceJsonType","4");
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
// KeyEvent.KEYCODE_BACK代表返回操作.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (pvOptions!=null){
                if (pvOptions.isShowing()){
                    pvOptions.dismiss();
                }else {
                    // 处理返回操作.
                    finish();
                }
            }else {
                finish();
            }


        }
        return true;
    }
}
