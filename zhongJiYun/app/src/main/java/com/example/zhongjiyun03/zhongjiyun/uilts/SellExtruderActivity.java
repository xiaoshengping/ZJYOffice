package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.AppDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.AppPhotoBean;
import com.example.zhongjiyun03.zhongjiyun.bean.PhotoDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.RentOutExtruderDeviceBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.MyExtruderBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.SecondHandListProjectBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityChildsBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.SelectData;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.SQLHelperUtils;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.activity.ClippingPageActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.activity.SelectImagesFromLocalActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.constants.ConstantSet;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.utils.SDCardUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;

public class SellExtruderActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv; //头部右边text
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;   //头部中间text
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;    //头部左边text

    @ViewInject(R.id.contract_checkbox)
    private CheckBox contractCheckbox;
    @ViewInject(R.id.invoice_checkBox)
    private CheckBox invoiceCheckBox;
    @ViewInject(R.id.park_address_edit)
    private TextView parkAddressEdit;
    @ViewInject(R.id.particluars_address_edit)
    private EditText particluarsAddressEdit;
    @ViewInject(R.id.price_edit)
    private EditText priceEdit;
    @ViewInject(R.id.jzhu_edit)
    private EditText jzhuMiaosEdit;
    @ViewInject(R.id.sell_save_button)
    private Button sellSaveButton;
    private OptionsPickerView pvOptions; //选项器
    private String Province;
    private String City;
    @ViewInject(R.id.company_vMasker)
    private View vMasker;   //弹出选择地点view
    private int contractTage;   //合同标识
    private int invoiceTage;     //发票标识


    private SVProgressHUD mSVProgressHUD;//loding
    private List<String> phoneListPath = new ArrayList<>();
    private File file;
    private Uri imageUri;
    private static final String IMAGE_FILE_LOCATION = ConstantSet.LOCALFILE;//temp file
    @ViewInject(R.id.leave_factory_textview)
    private LinearLayout leaveFactoryLayout; //全景1
    @ViewInject(R.id.leave_factory_image)
    private ImageView leaveFactoryImage;
    @ViewInject(R.id.panorama_text_view)
    private LinearLayout panoramaLayout;//全景2
    @ViewInject(R.id.panorama_image)
    private ImageView panoramaImage;
    @ViewInject(R.id.invoice_textview)
    private LinearLayout invoiceLayout;   //全景3
    @ViewInject(R.id.invoice_image)
    private ImageView invoiceImage;
    @ViewInject(R.id.contract_layout)
    private LinearLayout contractLayout;  //全景4
    @ViewInject(R.id.contract_image)
    private ImageView contractImage;
    @ViewInject(R.id.qualified_layout)
    private LinearLayout qualifiedLayout;  //全景5
    @ViewInject(R.id.qualified_image)
    private ImageView qualifiedImage;
    private String leavePath;   //全景照1路径
    private String panoramaPath;//全景照2路径
    private String invoicePath;//全景照3路径
    private String contractPath;//全景照4路径
    private String qualifiedPath;//全景照5路径
    private MyExtruderBean myExtruderBean; //钻机数据
    private SecondHandListProjectBean secondHandListProjectBean;//修改钻机数据]
    private String leaveImageID;//全景照1ID
    private String panoramaImageID;//全景照2ID
    private String invoiceImageID;//全景照3ID
    private String contractImageID;//全景照4ID
    private String qualifiedImageID;//全景照5ID
    private  String modifiSell;
    @ViewInject(R.id.sell_out_scrollView)
    private ScrollView sellOutScrollView;  //scrollview


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        intiPvAddress();
    }
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
        setContentView(R.layout.activity_sell_extruder);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
        initView();


    }

    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("钻机管理");
        retrunText.setOnClickListener(this);
        sellSaveButton.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);
        myExtruderBean = (MyExtruderBean) getIntent().getSerializableExtra("data");
        //获取修改数据
         modifiSell= getIntent().getStringExtra("tage");
        if (modifiSell.equals("modifiSell")) {
            intiData();
            sellSaveButton.setText("修改出售信息");
        }

        contractCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    invoiceTage = 1;

                } else {
                    invoiceTage = 0;
                }
            }
        });
        invoiceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    contractTage = 1;

                } else {
                    contractTage = 0;
                }
            }
        });

        file = new File(IMAGE_FILE_LOCATION);
        if (!file.exists()) {

            SDCardUtils.makeRootDirectory(IMAGE_FILE_LOCATION);
        }

        file = new File(IMAGE_FILE_LOCATION + ConstantSet.USERTEMPPIC);
        imageUri = Uri.fromFile(file);//The Uri t
        panoramaLayout.setOnClickListener(this);
        invoiceLayout.setOnClickListener(this);
        contractLayout.setOnClickListener(this);
        qualifiedLayout.setOnClickListener(this);
        leaveFactoryLayout.setOnClickListener(this);
        jzhuMiaosEdit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        sellOutScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                jzhuMiaosEdit.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.sell_save_button:
                if (getIntent().getStringExtra("tage").equals("modifiSell")){
                    modifiExtruderData();
                }else {
                    sellSaveData();
                }


                break;
            case R.id.leave_factory_textview:
                showDialog(ConstantSet.TAKEPICTURE, ConstantSet.SELECTPICTURE, imageUri);

                break;
            case R.id.panorama_text_view:
                showDialog(ConstantSet.TAKEPICTURE0, ConstantSet.SELECTPICTURE0, imageUri);
                break;
            case R.id.invoice_textview:
                showDialog(ConstantSet.TAKEPICTURE1, ConstantSet.SELECTPICTURE1, imageUri);
                break;
            case R.id.contract_layout:
                showDialog(ConstantSet.TAKEPICTURE2, ConstantSet.SELECTPICTURE2, imageUri);

                break;
            case R.id.qualified_layout:
                showDialog(ConstantSet.TAKEPICTURE3, ConstantSet.SELECTPICTURE3, imageUri);
                break;


        }


    }

    private void intiData() {
        String secondHandBeanId = myExtruderBean.getSecondHandId();
        if (!TextUtils.isEmpty(secondHandBeanId)) {
            HttpUtils httpUtils = new HttpUtils();
            RequestParams requestParams = new RequestParams();
            String uid = SQLHelperUtils.queryId(SellExtruderActivity.this);  //用户id
            if (!TextUtils.isEmpty(uid)) {
                requestParams.addBodyParameter("id", uid);
                //步骤1：创建一个SharedPreferences接口对象
                SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
                //步骤2：获取文件中的值
                String sesstionId = read.getString("code", "");
                requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
            }
            requestParams.addBodyParameter("deviceHistoryId", secondHandBeanId);
            mSVProgressHUD.showWithStatus("正在加载中...");
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getModifiExtruderData(), requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {

                    if (!TextUtils.isEmpty(responseInfo.result)) {
                       // Log.e("二手钻机详情", responseInfo.result);
                        AppBean<SecondHandListProjectBean> appListDataBean = JSONObject.parseObject(responseInfo.result, new TypeReference<AppBean<SecondHandListProjectBean>>() {
                        });
                        if (appListDataBean.getResult().equals("success")) {
                            secondHandListProjectBean = appListDataBean.getData();
                            if (secondHandListProjectBean != null) {
                                if (!TextUtils.isEmpty(secondHandListProjectBean.getProvince()) && !TextUtils.isEmpty(secondHandListProjectBean.getCity())) {
                                    parkAddressEdit.setText(secondHandListProjectBean.getProvince() + secondHandListProjectBean.getCity());
                                    Province=secondHandListProjectBean.getProvince();
                                    City=secondHandListProjectBean.getCity();
                                } else if (!TextUtils.isEmpty(secondHandListProjectBean.getProvince())) {
                                    parkAddressEdit.setText(secondHandListProjectBean.getProvince());
                                }
                                if (!TextUtils.isEmpty(secondHandListProjectBean.getAddress())) {
                                    particluarsAddressEdit.setText(secondHandListProjectBean.getAddress());
                                }
                                if (!TextUtils.isEmpty(secondHandListProjectBean.getPrice())) {
                                    priceEdit.setText(secondHandListProjectBean.getPrice());
                                }
                                if (!TextUtils.isEmpty(secondHandListProjectBean.getDescribing())) {
                                    jzhuMiaosEdit.setText(secondHandListProjectBean.getDescribing());
                                }
                                if (secondHandListProjectBean.getIsShowContract() == 1) {
                                    contractCheckbox.setChecked(true);
                                }
                                if (secondHandListProjectBean.getIsShowInvoice() == 1) {
                                    invoiceCheckBox.setChecked(true);
                                }
                                if (TextUtils.isEmpty(leavePath)) {
                                    if (!TextUtils.isEmpty(secondHandListProjectBean.getImage1())) {
                                        leavePath = secondHandListProjectBean.getImage1();
                                    }
                                }
                                if (TextUtils.isEmpty(panoramaPath)) {
                                    if (!TextUtils.isEmpty(secondHandListProjectBean.getImage2())) {
                                        panoramaPath = secondHandListProjectBean.getImage2();
                                    }
                                }
                                if (TextUtils.isEmpty(invoicePath)) {
                                    if (!TextUtils.isEmpty(secondHandListProjectBean.getImage3())) {
                                        invoicePath = secondHandListProjectBean.getImage3();
                                    }
                                }
                                if (TextUtils.isEmpty(contractPath)) {
                                    if (!TextUtils.isEmpty(secondHandListProjectBean.getImage4())) {
                                        contractPath = secondHandListProjectBean.getImage4();
                                    }
                                }

                                if (TextUtils.isEmpty(qualifiedPath)) {
                                    if (!TextUtils.isEmpty(secondHandListProjectBean.getImage5())) {
                                        qualifiedPath = secondHandListProjectBean.getImage5();
                                    }

                                }
                                if (!TextUtils.isEmpty(secondHandListProjectBean.getImage1())) {
                                    MyAppliction.imageLoader.displayImage(secondHandListProjectBean.getImage1(), leaveFactoryImage, MyAppliction.options);
                                }
                                if (!TextUtils.isEmpty(secondHandListProjectBean.getImage2())) {
                                    MyAppliction.imageLoader.displayImage(secondHandListProjectBean.getImage2(), panoramaImage, MyAppliction.options);
                                }
                                if (!TextUtils.isEmpty(secondHandListProjectBean.getImage3())) {
                                    MyAppliction.imageLoader.displayImage(secondHandListProjectBean.getImage3(), invoiceImage, MyAppliction.options);
                                }
                                if (!TextUtils.isEmpty(secondHandListProjectBean.getImage4())) {
                                    MyAppliction.imageLoader.displayImage(secondHandListProjectBean.getImage4(), contractImage, MyAppliction.options);
                                }

                                if (!TextUtils.isEmpty(secondHandListProjectBean.getImage5())) {
                                    MyAppliction.imageLoader.displayImage(secondHandListProjectBean.getImage5(), qualifiedImage, MyAppliction.options);
                                }


                            }


                        }

                        mSVProgressHUD.dismiss();
                    } else {
                        mSVProgressHUD.dismiss();

                    }


                }


                @Override
                public void onFailure(HttpException e, String s) {
                    mSVProgressHUD.dismiss();
                }
            });

        } else {

            MyAppliction.showToast("数据加载失败");

        }


    }


    private void modifiExtruderData() {

        if (!TextUtils.isEmpty(parkAddressEdit.getText().toString())){
            if (!TextUtils.isEmpty(particluarsAddressEdit.getText().toString())){
                if (!TextUtils.isEmpty(priceEdit.getText().toString())){
                        if (!TextUtils.isEmpty(jzhuMiaosEdit.getText().toString())){

                            String uid= SQLHelperUtils.queryId(SellExtruderActivity.this);  //用户id
                            HttpUtils httpUtils=new HttpUtils();
                            RequestParams requestParams=new RequestParams();
                            //步骤1：创建一个SharedPreferences接口对象
                            SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
                            //步骤2：获取文件中的值
                            String sesstionId = read.getString("code","");
                            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
                            requestParams.addBodyParameter("Id",uid);
                            requestParams.addBodyParameter("DeviceId",myExtruderBean.getId());
                            requestParams.addBodyParameter("SecondHandType","1");
                            if (!TextUtils.isEmpty(Province)){
                                requestParams.addBodyParameter("Province",Province);
                            }
                            if (!TextUtils.isEmpty(City)){
                                requestParams.addBodyParameter("City",City);
                            }
                            if (!TextUtils.isEmpty(myExtruderBean.getSecondHandId())){
                                requestParams.addBodyParameter("deviceHistoryId", myExtruderBean.getSecondHandId());
                            }
                            requestParams.addBodyParameter("Address",particluarsAddressEdit.getText().toString());
                            requestParams.addBodyParameter("Price",priceEdit.getText().toString());
                            requestParams.addBodyParameter("IsShowContract",contractTage+"");
                            requestParams.addBodyParameter("IsShowInvoice",invoiceTage+"");
                            requestParams.addBodyParameter("Describing",jzhuMiaosEdit.getText().toString());
                            if (!TextUtils.isEmpty(MyAppliction.getLeaveFactoryId())){
                                requestParams.addBodyParameter("image1Id",MyAppliction.getLeaveFactoryId());
                            }else {
                                requestParams.addBodyParameter("image1Id",secondHandListProjectBean.getImage1Id());
                            }
                            if (!TextUtils.isEmpty(MyAppliction.getPanoramaId())){
                                requestParams.addBodyParameter("image2Id",MyAppliction.getPanoramaId());
                            }else {

                                requestParams.addBodyParameter("image2Id",secondHandListProjectBean.getImage2Id());
                            }
                            if (!TextUtils.isEmpty(MyAppliction.getInvoiceId())){
                                requestParams.addBodyParameter("image3Id",MyAppliction.getInvoiceId());
                            }else {
                                requestParams.addBodyParameter("image3Id",secondHandListProjectBean.getImage3Id());
                            }
                            if (!TextUtils.isEmpty(MyAppliction.getContractId())){
                                requestParams.addBodyParameter("image4Id",MyAppliction.getContractId());
                            }else {
                                requestParams.addBodyParameter("image4Id",secondHandListProjectBean.getImage4Id());
                            }
                            if (!TextUtils.isEmpty(MyAppliction.getQualifiedId())){
                                requestParams.addBodyParameter("image5Id",MyAppliction.getQualifiedId());
                            }else {
                                requestParams.addBodyParameter("image5Id",secondHandListProjectBean.getImage5Id());
                            }
                            //Log.e("sdhhdhdhhdhdh","'"+leaveImageID+","+panoramaImageID+","+invoiceImageID+","+contractImageID+","+qualifiedImageID+"'".replace("null",""));
                            //requestParams.addBodyParameter("updateImgs", (leaveImageID+","+panoramaImageID+","+invoiceImageID+","+contractImageID+","+qualifiedImageID).replace("null",""));
                            mSVProgressHUD.showWithStatus("正在提交中...");
                            final String finalUid = uid;
                            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRentOrSellData(),requestParams, new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                    //Log.e("修改出售",responseInfo.result);
                                    if (!TextUtils.isEmpty(responseInfo.result)){
                                        AppBean<RentOutExtruderDeviceBean> appBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<RentOutExtruderDeviceBean>>(){});
                                        if (appBean.getResult().equals("success")){
                                            finish();
                                            MyAppliction.showToast("修改钻机信息成功");
                                            mSVProgressHUD.dismiss();
                                        }else {
                                            mSVProgressHUD.dismiss();
                                            MyAppliction.showToast(appBean.getMsg());
                                        }
                                    }


                                }

                                @Override
                                public void onFailure(HttpException e, String s) {
                                    Log.e("出租",s);
                                    mSVProgressHUD.dismiss();
                                    MyAppliction.showToast("网络异常，请稍后重试");
                                }
                            });




                        }else {
                            MyAppliction.showToast("请输入描述");

                        }
                }else {
                    MyAppliction.showToast("请输入价格");

                }



            }else {
                MyAppliction.showToast("请输入详细地址");

            }



        }else {
            MyAppliction.showToast("请选择停放地");

        }




    }


    private void sellSaveData() {
        if (!TextUtils.isEmpty(parkAddressEdit.getText().toString())) {
            if (!TextUtils.isEmpty(particluarsAddressEdit.getText().toString())) {
                if (!TextUtils.isEmpty(priceEdit.getText().toString())) {
                    if (!TextUtils.isEmpty(jzhuMiaosEdit.getText().toString())) {
                        if (!TextUtils.isEmpty(MyAppliction.getLeaveFactoryId())){
                            if (!TextUtils.isEmpty(MyAppliction.getPanoramaId())){
                                if (!TextUtils.isEmpty(MyAppliction.getInvoiceId())){
                                    SQLhelper sqLhelper = new SQLhelper(SellExtruderActivity.this);
                                    SQLiteDatabase db = sqLhelper.getWritableDatabase();
                                    Cursor cursor = db.query(SQLhelper.tableName, null, null, null, null, null, null);
                                    String uid = null;  //用户id
                                    while (cursor.moveToNext()) {
                                        uid = cursor.getString(0);
                                    }
                                    HttpUtils httpUtils = new HttpUtils();
                                    RequestParams requestParams = new RequestParams();
                                    //步骤1：创建一个SharedPreferences接口对象
                                    SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
                                    //步骤2：获取文件中的值
                                    String sesstionId = read.getString("code", "");
                                    requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
                                    requestParams.addBodyParameter("Id", uid);
                                    requestParams.addBodyParameter("DeviceId", ((MyExtruderBean) getIntent().getSerializableExtra("data")).getId());
                                    //requestParams.addBodyParameter("DeviceHistoryId","");
                                    requestParams.addBodyParameter("SecondHandType", "1");
                                    if (!TextUtils.isEmpty(Province)) {
                                        requestParams.addBodyParameter("Province", Province);
                                    }
                                    if (!TextUtils.isEmpty(City)) {
                                        requestParams.addBodyParameter("City", City);
                                    }
                                    if (getIntent().getStringExtra("tage").equals("modifiSell")) {
                                        if (!TextUtils.isEmpty(myExtruderBean.getSecondHandId())) {
                                            requestParams.addBodyParameter("deviceHistoryId", myExtruderBean.getSecondHandId());
                                        }

                                    }
                                    requestParams.addBodyParameter("Address", particluarsAddressEdit.getText().toString());
                                    requestParams.addBodyParameter("Price", priceEdit.getText().toString());
                                    requestParams.addBodyParameter("IsShowContract", contractTage + "");
                                    requestParams.addBodyParameter("IsShowInvoice", invoiceTage + "");
                                    requestParams.addBodyParameter("Describing", jzhuMiaosEdit.getText().toString());
                                    requestParams.addBodyParameter("image1Id",MyAppliction.getLeaveFactoryId());
                                    requestParams.addBodyParameter("image2Id",MyAppliction.getPanoramaId());
                                    requestParams.addBodyParameter("image3Id",MyAppliction.getInvoiceId());
                                    if (!TextUtils.isEmpty(MyAppliction.getContractId())){
                                         requestParams.addBodyParameter("image4Id",MyAppliction.getContractId());
                                     }
                                    if(!TextUtils.isEmpty(MyAppliction.getQualifiedId())){
                                      requestParams.addBodyParameter("image5Id",MyAppliction.getQualifiedId());
                                    }
                                    mSVProgressHUD.showWithStatus("正在提交中...");
                                    final String finalUid = uid;
                                    httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRentOrSellData(), requestParams, new RequestCallBack<String>() {
                                        @Override
                                        public void onSuccess(ResponseInfo<String> responseInfo) {
                                            //Log.e("出售", responseInfo.result);
                                            if (!TextUtils.isEmpty(responseInfo.result)) {
                                                AppBean<RentOutExtruderDeviceBean> appBean = JSONObject.parseObject(responseInfo.result, new TypeReference<AppBean<RentOutExtruderDeviceBean>>() {
                                                });
                                                if (appBean.getResult().equals("success")) {
                                                    //Log.e("出租id",appBean.getData().getOwnId());
                                                    MyAppliction.showToast("出租钻机成功");
                                                    showExitGameAlert("\u3000\u3000"+"敬的用户，您的钻机出租申请已提交成功，请等待后台审核，为了提高您审核通过的概率，现建议您去缴纳1000元的保证金，谢谢!","提交成功，等待后台审核");
                                                    mSVProgressHUD.dismiss();

                                                } else {
                                                    mSVProgressHUD.dismiss();
                                                    MyAppliction.showToast(appBean.getMsg());
                                                }
                                            } else {
                                                mSVProgressHUD.dismiss();
                                            }

                                        }

                                        @Override
                                        public void onFailure(HttpException e, String s) {
                                            Log.e("出售", s);
                                            mSVProgressHUD.dismiss();
                                            MyAppliction.showToast("网络异常，请稍后重试");
                                        }
                                    });




                                }else {

                                    MyAppliction.showToast("请上传全景图片3");
                                }
                            }else {

                                MyAppliction.showToast("请上传全景图片2");
                            }
                        }else {

                            MyAppliction.showToast("请上传全景图片1");
                        }
                    } else {
                        MyAppliction.showToast("请输入描述");

                    }


                } else {
                    MyAppliction.showToast("请输入价格");

                }


            } else {
                MyAppliction.showToast("请输入详细地址");

            }


        } else {
            MyAppliction.showToast("请选择停放地");

        }


    }

    private void intiPhontData7(String id, int imageType, String imagePath, String ownId, final int tages) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requwstParams = new RequestParams();
        requwstParams.addBodyParameter("Id", id);
        requwstParams.addBodyParameter("ImageType", imageType + "");
        requwstParams.addBodyParameter("UserType", "boss");
        requwstParams.addBodyParameter("SourceType", "4");
        requwstParams.addBodyParameter("File", new File(imagePath));
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code", "");
        requwstParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        requwstParams.addBodyParameter("OwnId", ownId);
        httpUtils.configSoTimeout(1200000);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getPhoneData(), requwstParams, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("照片请求", responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)) {
                    AppBean<AppDataBean> appBean = JSONObject.parseObject(responseInfo.result, new TypeReference<AppBean<AppDataBean>>() {
                    });
                    int tage = tages + 1;
                    if (appBean.getResult().equals("success")) {

                        if (getIntent().getStringExtra("tage").equals("modifiSell")) {
                            if (tage == phoneListPath.size()) {
                                MyAppliction.showToast("修改成功");
                                mSVProgressHUD.dismiss();
                                finish();
                            }
                        } else {
                            if (tage == phoneListPath.size()) {
                                MyAppliction.showToast("出售钻机成功");
                                showExitGameAlert("\u3000\u3000"+"敬的用户，您的钻机出售申请已提交成功，请等待后台审核，为了提高您审核通过的概率，现建议您去缴纳1000元的保证金，谢谢!","提交成功，等待后台审核");
                                mSVProgressHUD.dismiss();
                            }
                        }
                    } else {
                        MyAppliction.showToast("上传照片失败");
                        mSVProgressHUD.dismiss();
                    }


                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("照片请求s", s);
                MyAppliction.showToast("上传照片失败");


            }
        });


    }



    //对话框
    private void showExitGameAlert(String text,String tailtText) {
        final AlertDialog dlg = new AlertDialog.Builder(SellExtruderActivity.this).create();
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
        TextView tailtVt= (TextView) window.findViewById(R.id.tv);
        tailtVt.setText(tailtText);
        ok.setText("缴纳保证金");
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(SellExtruderActivity.this,CommitCashDepositActivity.class);
                intent.putExtra("zfuTage","sellExturd");
                startActivity(intent);
                finish();
                dlg.cancel();
            }
        });
        // 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText("关闭");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
                finish();
            }
        });
    }

    //拍照和相册弹出框
    private void showDialog(final int captureIndext, final int pickIndext, final Uri uri) {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        final Dialog dialog = new Dialog(SellExtruderActivity.this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Button pictureDialogButton = (Button) view.findViewById(R.id.picture_dialog_button);
        Button photographDialogButton = (Button) view.findViewById(R.id.photograph_dialog_button);
        Button cancelDialogButton = (Button) view.findViewById(R.id.cancel_dialog_button);
        pictureDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 指定调用相机拍照后照片的储存路径
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, captureIndext);//or TAKE_SMALL_PICTURE
                dialog.dismiss();
            }
        });
        photographDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sintent = new Intent(SellExtruderActivity.this, SelectImagesFromLocalActivity.class);
                startActivityForResult(sintent, pickIndext);
                dialog.dismiss();


            }
        });
        cancelDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {

            return;

        }
        switch (requestCode) {

            case ConstantSet.TAKEPICTURE:

                Intent tcutIntent = new Intent(SellExtruderActivity.this, ClippingPageActivity.class);
                tcutIntent.putExtra("type", "takePicture");
                startActivityForResult(tcutIntent, ConstantSet.CROPPICTURE);
                break;

            case ConstantSet.SELECTPICTURE:
                Intent scutIntent = new Intent(SellExtruderActivity.this, ClippingPageActivity.class);
                scutIntent.putExtra("type", "selectPicture");
                scutIntent.putExtra("path", data.getStringExtra("path"));
                startActivityForResult(scutIntent, ConstantSet.CROPPICTURE);
                break;


            case ConstantSet.CROPPICTURE:
                byte[] bis = data.getByteArrayExtra("result");
                Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
                if (bitmap != null) {
                   // leaveFactoryImage.setImageBitmap(bitmap);
                String frontName = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File frontFile = getFile(bis, Environment.getExternalStorageDirectory()+"/zhongJiYun", frontName);
                if (!TextUtils.isEmpty(frontFile.getPath())) {
                    /*leavePath = frontFile.getPath();
                    if (!TextUtils.isEmpty(leavePath)){
                        if (!modifiSell.equals("modifiSell")) {
                            phoneListPath.add(leavePath);
                        }
                    }*/
                    upterImageData(1, bitmap, BitmapUtils.bitmapToString(frontFile.getPath()));
                }
                }else {
                    MyAppliction.showToast("上传照片失败");
                }

                break;
            case ConstantSet.TAKEPICTURE0:
                Intent tcutIntentOne = new Intent(SellExtruderActivity.this, ClippingPageActivity.class);
                tcutIntentOne.putExtra("type", "takePicture");
                startActivityForResult(tcutIntentOne, ConstantSet.CROPPICTURE0);


                break;

            case ConstantSet.SELECTPICTURE0:
                Intent scutIntentOne = new Intent(SellExtruderActivity.this, ClippingPageActivity.class);
                scutIntentOne.putExtra("type", "selectPicture");
                scutIntentOne.putExtra("path", data.getStringExtra("path"));
                startActivityForResult(scutIntentOne, ConstantSet.CROPPICTURE0);


                break;


            case ConstantSet.CROPPICTURE0:
                byte[] biss = data.getByteArrayExtra("result");
                Bitmap bitmaps = BitmapFactory.decodeByteArray(biss, 0, biss.length);
                if (bitmaps != null) {
                String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File file2 = getFile(biss, Environment.getExternalStorageDirectory()+"/zhongJiYun", name);
                if (!TextUtils.isEmpty(file2.getPath())) {
                    upterImageData(2, bitmaps, BitmapUtils.bitmapToString(file2.getPath()));
                }
                }else {
                    MyAppliction.showToast("上传照片失败");
                }
                break;
            case ConstantSet.TAKEPICTURE1:
                Intent persongeTakIntent = new Intent(SellExtruderActivity.this, ClippingPageActivity.class);
                persongeTakIntent.putExtra("type", "takePicture");
                startActivityForResult(persongeTakIntent, ConstantSet.CROPPICTURE1);


                break;

            case ConstantSet.SELECTPICTURE1:
                Intent scutPersongeIntent = new Intent(SellExtruderActivity.this, ClippingPageActivity.class);
                scutPersongeIntent.putExtra("type", "selectPicture");
                scutPersongeIntent.putExtra("path", data.getStringExtra("path"));
                startActivityForResult(scutPersongeIntent, ConstantSet.CROPPICTURE1);


                break;


            case ConstantSet.CROPPICTURE1:
                byte[] bisPersonge = data.getByteArrayExtra("result");
                Bitmap bitmapPersonge = BitmapFactory.decodeByteArray(bisPersonge, 0, bisPersonge.length);
                if (bitmapPersonge != null) {

                String namePersonge = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File filePersonge = getFile(bisPersonge, Environment.getExternalStorageDirectory()+"/zhongJiYun", namePersonge);
                if (!TextUtils.isEmpty(filePersonge.getPath())) {
                    upterImageData(3, bitmapPersonge, BitmapUtils.bitmapToString(filePersonge.getPath()));
                }
                }else {
                    MyAppliction.showToast("上传照片失败");
                }
                break;
            case ConstantSet.TAKEPICTURE2:
                Intent companyFrontTakIntent = new Intent(SellExtruderActivity.this, ClippingPageActivity.class);
                companyFrontTakIntent.putExtra("type", "takePicture");
                startActivityForResult(companyFrontTakIntent, ConstantSet.CROPPICTURE2);


                break;

            case ConstantSet.SELECTPICTURE2:
                Intent companyFrontScutIntent = new Intent(SellExtruderActivity.this, ClippingPageActivity.class);
                companyFrontScutIntent.putExtra("type", "selectPicture");
                companyFrontScutIntent.putExtra("path", data.getStringExtra("path"));
                startActivityForResult(companyFrontScutIntent, ConstantSet.CROPPICTURE2);


                break;


            case ConstantSet.CROPPICTURE2:
                byte[] forntBis = data.getByteArrayExtra("result");
                Bitmap frontBitmap = BitmapFactory.decodeByteArray(forntBis, 0, forntBis.length);
                if (frontBitmap != null) {
                String nameFront = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File fileFront = getFile(forntBis, Environment.getExternalStorageDirectory()+"/zhongJiYun", nameFront);
                if (!TextUtils.isEmpty(fileFront.getPath())) {
                    upterImageData(4, frontBitmap, BitmapUtils.bitmapToString(fileFront.getPath()));
                }
                }else {
                    MyAppliction.showToast("上传照片失败");
                }
                break;
            case ConstantSet.TAKEPICTURE3:
                Intent companyVesonTakIntent = new Intent(SellExtruderActivity.this, ClippingPageActivity.class);
                companyVesonTakIntent.putExtra("type", "takePicture");
                startActivityForResult(companyVesonTakIntent, ConstantSet.CROPPICTURE3);


                break;

            case ConstantSet.SELECTPICTURE3:
                Intent companyVesonScutIntent = new Intent(SellExtruderActivity.this, ClippingPageActivity.class);
                companyVesonScutIntent.putExtra("type", "selectPicture");
                companyVesonScutIntent.putExtra("path", data.getStringExtra("path"));
                startActivityForResult(companyVesonScutIntent, ConstantSet.CROPPICTURE3);


                break;


            case ConstantSet.CROPPICTURE3:
                byte[] vesonBis = data.getByteArrayExtra("result");
                Bitmap vesonBitmap = BitmapFactory.decodeByteArray(vesonBis, 0, vesonBis.length);
                if (vesonBitmap != null) {
                String nameVeson = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File fileVeson = getFile(vesonBis, Environment.getExternalStorageDirectory()+"/zhongJiYun", nameVeson);
                if (!TextUtils.isEmpty(fileVeson.getPath())) {
                    upterImageData(5, vesonBitmap, BitmapUtils.bitmapToString(fileVeson.getPath()));
                }
                }else {
                    MyAppliction.showToast("上传照片失败");
                }
                break;


        }
    }


    /**
     * 上传照片
     */
    private void upterImageData(final int tage, final Bitmap bitmap, String photoString) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requeatParams=new RequestParams();
        requeatParams.addBodyParameter("fileByte",photoString);
        mSVProgressHUD.showWithStatus("正在上传中...", SVProgressHUD.SVProgressHUDMaskType.Black);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRegistImageData(),requeatParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("注册上传图片onSuccess",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    //mSVProgressHUD.dismiss();
                    AppPhotoBean appDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppPhotoBean>(){});
                    if (appDataBean!=null){
                        if (appDataBean.getResult().equals("success")){
                            PhotoDataBean photoDataBean=appDataBean.getData();
                            if (photoDataBean!=null){
                                if (tage==1){
                                    MyAppliction.setLeaveFactoryId(photoDataBean.getId());
                                    leaveFactoryImage.setImageBitmap(bitmap);

                                }else if (tage==2){
                                    MyAppliction.setPanoramaId(photoDataBean.getId());
                                    panoramaImage.setImageBitmap(bitmap);
                                }else if (tage==3){
                                    MyAppliction.setInvoiceId(photoDataBean.getId());
                                    invoiceImage.setImageBitmap(bitmap);

                                }else if (tage==4){
                                    MyAppliction.setContractId(photoDataBean.getId());
                                    contractImage.setImageBitmap(bitmap);
                                }else if (tage==5){
                                    MyAppliction.setQualifiedId(photoDataBean.getId());
                                    qualifiedImage.setImageBitmap(bitmap);
                                }

                            }
                            MyAppliction.showToast("上传照片成功");
                            mSVProgressHUD.dismiss();
                        }else {
                            MyAppliction.showToast(appDataBean.getMsg());
                            mSVProgressHUD.dismiss();

                        }

                    }else {
                        MyAppliction.showToast("上传照片失败");
                    }
                }else {
                    MyAppliction.showToast("上传照片失败");
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("注册上传图片onFailure",s);
                mSVProgressHUD.dismiss();
            }
        });





    }

    /**
     * 根据byte数组，生成文件
     */
    public static File getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return file;

    }


    private void intiPvAddress() {
        final ArrayList<String> options1Items = new ArrayList<String>();
        final ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();


        ProvinceCityDataBean provinceCityDataBean = JSONObject.parseObject(SelectData.selectCityDatas + SelectData.selectCityDataOnes + SelectData.selectCityDataTwos, new TypeReference<ProvinceCityDataBean>() {
        });
        if (provinceCityDataBean != null) {
            ArrayList<ProvinceCityBean> options1Itemss = (ArrayList<ProvinceCityBean>) provinceCityDataBean.getProvinceCity();
            for (int i = 0; i < options1Itemss.size(); i++) {
                options1Items.add(options1Itemss.get(i).getName());
            }
            for (int i = 0; i < options1Items.size(); i++) {
                ArrayList<ProvinceCityChildsBean> provinceCity = options1Itemss.get(i).getProvinceCityChilds();
                ArrayList<String> arrayList = new ArrayList<>();
                for (int J = 0; J < provinceCity.size(); J++) {
                    arrayList.add(provinceCity.get(J).getName());
                }
                options2Items.add(arrayList);

            }


        }

        pvOptions = new OptionsPickerView(SellExtruderActivity.this);
       /* //三级联动效果
        pvOptions.setPicker(options1Items, options2Items, options3Items, true);*/
        //二级联动
        pvOptions.setPicker(options1Items, options2Items, true);
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
        pvOptions.setTitle("选择城市");
        pvOptions.setCyclic(false, false, true);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(1, 1, 1);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
               /* //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(option2)
                        + options3Items.get(options1).get(option2).get(options3);*/
                //返回的分别是两个级别的选中位置
                String tx = options1Items.get(options1)
                        + options2Items.get(options1).get(option2);
                Province = options1Items.get(options1);
                City = options2Items.get(options1).get(option2);
                parkAddressEdit.setText(tx);
                vMasker.setVisibility(View.GONE);
            }
        });
        //点击弹出选项选择器
        parkAddressEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pvOptions.show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
// KeyEvent.KEYCODE_BACK代表返回操作.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 处理返回操作.
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
    protected void onDestroy() {
        super.onDestroy();
        MyAppliction.setLeaveFactoryId(null);
        MyAppliction.setPanoramaId(null);
        MyAppliction.setInvoiceId(null);
        MyAppliction.setContractId(null);
        MyAppliction.setQualifiedId(null);

    }

}
