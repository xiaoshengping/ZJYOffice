package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.AppDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.RentOutExtruderDeviceDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.MyExtruderBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityChildsBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.SelectData;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/*
* 出租
* */

public class RentOutExtruderActivity extends AppCompatActivity implements View.OnClickListener {


    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv; //头部右边text
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;   //头部中间text
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;    //头部左边text


    @ViewInject(R.id.rent_part_address)
    private TextView rentPartAddress;   //停放地
    @ViewInject(R.id.rent_address_particulars)
    private EditText rentAddressParticulars; //详细地址
    @ViewInject(R.id.rent_price_edit)
    private EditText rentPriceEdit;   //价格
    @ViewInject(R.id.rent_tenancy_term)
    private EditText rentTenancyTerm;   //租期
    @ViewInject(R.id.rent_describe)
    private EditText rentDescribe ;     //描述
    @ViewInject(R.id.rent_out_button)
    private Button rentOutButton;
    private OptionsPickerView pvOptions; //选项器
    private String Province; //省份
    private String City; //城市
    @ViewInject(R.id.company_vMasker)
    private View vMasker;   //弹出选择地点view
    private MyExtruderBean myExtruderBean;   //数据]
    @ViewInject(R.id.contract_check)
    private CheckBox contractCheck;  //合同
    @ViewInject(R.id.invoice_check)
    private CheckBox invoiceCheck;    //发票
    private int contractTage;   //合同标识
    private int invoiceTage;     //发票标识
    private SVProgressHUD mSVProgressHUD;//loding
    private List<String> phoneListPath=new ArrayList<>();
    private File file;
    private Uri imageUri;
    private static final String IMAGE_FILE_LOCATION = ConstantSet.LOCALFILE;//temp file
    @ViewInject(R.id.leave_factory_textview)
    private LinearLayout leaveFactoryLayout; //出厂牌
    @ViewInject(R.id.leave_factory_image)
    private ImageView leaveFactoryImage;
    @ViewInject(R.id.panorama_text_view)
    private LinearLayout panoramaLayout;//设备全景
    @ViewInject(R.id.panorama_image)
    private ImageView panoramaImage;
    @ViewInject(R.id.invoice_textview)
    private LinearLayout invoiceLayout;   //发票
    @ViewInject(R.id.invoice_image)
    private ImageView invoiceImage;
    @ViewInject(R.id.contract_layout)
    private LinearLayout contractLayout;  //合同
    @ViewInject(R.id.contract_image)
    private ImageView contractImage;
    @ViewInject(R.id.qualified_layout)
    private LinearLayout qualifiedLayout;  //合格证
    @ViewInject(R.id.qualified_image)
    private ImageView qualifiedImage;
    private String leavePath;   //全景照1路径
    private String panoramaPath;//全景照2路径
    private String invoicePath;//全景照3路径
    private String contractPath;//全景照4路径
    private String qualifiedPath;//全景照5路径




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_out_extruder);
       getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ViewUtils.inject(this);
        inti();




    }

    private void inti() {
        initView();
        intiPvAddress();

    }

    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("钻机管理");
        retrunText.setOnClickListener(this);
        rentOutButton.setOnClickListener(this);
        myExtruderBean= (MyExtruderBean) getIntent().getSerializableExtra("data");
        mSVProgressHUD = new SVProgressHUD(this);
        invoiceCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                  invoiceTage=1;

                }else {
                    invoiceTage=0;
                }
            }
        });
        contractCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    contractTage=1;

                }else {
                    contractTage=0;
                }
            }
        });

        file = new File(IMAGE_FILE_LOCATION);
        if(!file.exists()){

            SDCardUtils.makeRootDirectory(IMAGE_FILE_LOCATION);
        }

        file=new File(IMAGE_FILE_LOCATION+ConstantSet.USERTEMPPIC);
        imageUri = Uri.fromFile(file);//The Uri t
        panoramaLayout.setOnClickListener(this);
        invoiceLayout.setOnClickListener(this);
        contractLayout.setOnClickListener(this);
        qualifiedLayout.setOnClickListener(this);
        leaveFactoryLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;
            case R.id.rent_out_button:
                sellSaveData();

                break;
            case R.id.leave_factory_textview:
                showDialog(ConstantSet.TAKEPICTURE,ConstantSet.SELECTPICTURE,imageUri);

                break;
            case R.id.panorama_text_view:
                showDialog(ConstantSet.TAKEPICTURE0,ConstantSet.SELECTPICTURE0,imageUri);
                break;
            case R.id.invoice_textview:
                showDialog(ConstantSet.TAKEPICTURE1,ConstantSet.SELECTPICTURE1,imageUri);
                break;
            case R.id.contract_layout:
                showDialog(ConstantSet.TAKEPICTURE2,ConstantSet.SELECTPICTURE2,imageUri);

                break;
            case R.id.qualified_layout:
                showDialog(ConstantSet.TAKEPICTURE3,ConstantSet.SELECTPICTURE3,imageUri);
                break;



        }




    }
    private void sellSaveData() {
        if (!TextUtils.isEmpty(rentPartAddress.getText().toString())){
            if (!TextUtils.isEmpty(rentAddressParticulars.getText().toString())){
                if (!TextUtils.isEmpty(rentPriceEdit.getText().toString())){
                    if (!TextUtils.isEmpty(rentTenancyTerm.getText().toString())){
                        if (!TextUtils.isEmpty(rentDescribe.getText().toString())){
                            if (!TextUtils.isEmpty(leavePath)){
                                phoneListPath.add(leavePath);
                                if (!TextUtils.isEmpty(panoramaPath)){
                                    phoneListPath.add(panoramaPath);
                                    if (!TextUtils.isEmpty(invoicePath)){
                                        phoneListPath.add(invoicePath);
                                        if (!TextUtils.isEmpty(contractPath)) {
                                            phoneListPath.add(contractPath);
                                        }
                                            if (!TextUtils.isEmpty(qualifiedPath)){
                                                phoneListPath.add(qualifiedPath);
                                            }
                        SQLhelper sqLhelper=new SQLhelper(RentOutExtruderActivity.this);
                        SQLiteDatabase db= sqLhelper.getWritableDatabase();
                        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
                        String uid=null;  //用户id
                        while (cursor.moveToNext()) {
                        uid=cursor.getString(0);
                                        }
                        HttpUtils httpUtils=new HttpUtils();
                            RequestParams requestParams=new RequestParams();
                            requestParams.addBodyParameter("Id",uid);
                            requestParams.addBodyParameter("DeviceId",myExtruderBean.getId());
                            /*if (!TextUtils.isEmpty(myExtruderBean.getHistoryList().get(0).getId())){
                                requestParams.addBodyParameter("DeviceHistoryId",myExtruderBean.getHistoryList().get(0).getId());
                            }*/
                            requestParams.addBodyParameter("SecondHandType","1");
                            if (!TextUtils.isEmpty(Province)){
                                requestParams.addBodyParameter("Province",Province);
                            }
                            if (!TextUtils.isEmpty(City)){
                                requestParams.addBodyParameter("City",City);
                            }
                            requestParams.addBodyParameter("Address",rentAddressParticulars.getText().toString());
                            requestParams.addBodyParameter("Tenancy",rentTenancyTerm.getText().toString());
                            requestParams.addBodyParameter("Price",rentPriceEdit.getText().toString());
                            requestParams.addBodyParameter("IsShowContract",contractTage+"");

                            requestParams.addBodyParameter("IsShowInvoice",invoiceTage+"");
                            requestParams.addBodyParameter("Describing",rentDescribe.getText().toString());
                            requestParams.addBodyParameter("Image1ServerId","phont.png");
                            requestParams.addBodyParameter("Image2ServerId","phont.png");
                            requestParams.addBodyParameter("Image3ServerId","phont.png");
                            requestParams.addBodyParameter("Image4ServerId","phont.png");
                            requestParams.addBodyParameter("Image5ServerId","phont.png");
                            if (phoneListPath.size()==5){
                                 mSVProgressHUD.showWithStatus("上传照片中(5张)...");
                            } else if (phoneListPath.size()==4){
                               mSVProgressHUD.showWithStatus("上传照片中(4张)...");
                            }else if (phoneListPath.size()==3){
                              mSVProgressHUD.showWithStatus("上传照片中(3张)...");
                            }

                            final String finalUid = uid;
                            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRentOrSellData(),requestParams, new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                Log.e("出租",responseInfo.result);
                                if (!TextUtils.isEmpty(responseInfo.result)){
                                    AppBean<RentOutExtruderDeviceDataBean> appBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<RentOutExtruderDeviceDataBean>>(){});
                                    if (appBean.getResult().equals("success")){
                                        //Log.e("出租id",appBean.getData().getOwnId());
                                        if (appBean.getData()!=null){
                                            RentOutExtruderDeviceDataBean rentOutExtruderDeviceDataBean=appBean.getData();
                                            if (rentOutExtruderDeviceDataBean!=null){
                                                intiPhontData0(finalUid,"11",phoneListPath.get(0),rentOutExtruderDeviceDataBean.getDevice().getOwnId());
                                            }
                                        }


                                    }else {
                                      MyAppliction.showToast(appBean.getMsg());
                                    }
                                }


                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                Log.e("出租",s);
                                MyAppliction.showToast("网络异常，请稍后重试");
                            }
                        });


                                    }else {

                                        MyAppliction.showToast("请选择全景图片3");
                                    }


                                }else {

                                    MyAppliction.showToast("请选择全景图片2");
                                }


                            }else {

                                MyAppliction.showToast("请选择全景图片1");
                            }

                        }else {
                            MyAppliction.showToast("请输入描述");

                        }
                    }else {
                        MyAppliction.showToast("请输租期");

                    }



                }else {
                    MyAppliction.showToast("请输入价格");

                }



            }else {
                MyAppliction.showToast("请输入详细地址");

            }



        }else {
            MyAppliction.showToast("请输入停放地");

        }






    }

    private void intiPhontData0(final String id, String imageType, String imagePath, final String ownId) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requwstParams=new RequestParams();
        requwstParams.addBodyParameter("Id",id);
        requwstParams.addBodyParameter("ImageType",imageType);
        requwstParams.addBodyParameter("UserType","boss");
        requwstParams.addBodyParameter("SourceType","4");
        requwstParams.addBodyParameter("File", new File(imagePath));

            requwstParams.addBodyParameter("OwnId",ownId);


        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getPhoneData(),requwstParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("照片请求",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<AppDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<AppDataBean>>(){});

                    if (appBean.getResult().equals("success")){
                        if (phoneListPath.size()==5){
                            mSVProgressHUD.showWithStatus("上传照片中(4张)...");
                        } else if (phoneListPath.size()==4){
                            mSVProgressHUD.showWithStatus("上传照片中(3张)...");
                        }else if (phoneListPath.size()==3){
                            mSVProgressHUD.showWithStatus("上传照片中(2张)...");
                        }
                        intiPhontData1(id,"12",phoneListPath.get(1),ownId);


                    }else {
                        MyAppliction.showToast("上传照片失败");
                        mSVProgressHUD.dismiss();
                    }



                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("照片请求",s);
                mSVProgressHUD.dismiss();
            }
        });


    }
    private void intiPhontData1(final String id, String iamgeType, String imagePath, final String ownId) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requwstParams=new RequestParams();
        requwstParams.addBodyParameter("Id",id);
        requwstParams.addBodyParameter("ImageType",iamgeType);
        requwstParams.addBodyParameter("UserType","boss");
        requwstParams.addBodyParameter("SourceType","4");
        requwstParams.addBodyParameter("File", new File(imagePath));

            requwstParams.addBodyParameter("OwnId",ownId);


        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getPhoneData(),requwstParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("照片请求",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<AppDataBean> appBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<AppDataBean>>(){});

                    if (appBean.getResult().equals("success")){
                        if (phoneListPath.size()==5){
                            mSVProgressHUD.showWithStatus("上传照片中(3张)...");
                        } else if (phoneListPath.size()==4){
                            mSVProgressHUD.showWithStatus("上传照片中(2张)...");
                        }else if (phoneListPath.size()==3){
                            mSVProgressHUD.showWithStatus("上传照片中(1张)...");
                        }
                        intiPhontData2(id,"13",phoneListPath.get(2),ownId);




                    }else {
                        mSVProgressHUD.dismiss();
                        MyAppliction.showToast("上传照片失败");

                    }



                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("照片请求",s);
                mSVProgressHUD.dismiss();
            }
        });


    }
    private void intiPhontData2(final String id, String imageType, String imagePath, final String ownId) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requwstParams=new RequestParams();
        requwstParams.addBodyParameter("Id",id);
        requwstParams.addBodyParameter("ImageType",imageType);
        requwstParams.addBodyParameter("UserType","boss");
        requwstParams.addBodyParameter("SourceType","4");
        requwstParams.addBodyParameter("File", new File(imagePath));

            requwstParams.addBodyParameter("OwnId",ownId);

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getPhoneData(),requwstParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("照片请求",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<AppDataBean> appBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<AppDataBean>>(){});

                    if (appBean.getResult().equals("success")){
                        if (phoneListPath.size()==5){
                            mSVProgressHUD.showWithStatus("上传照片中(2张)...");
                        } else if (phoneListPath.size()==4){
                            mSVProgressHUD.showWithStatus("上传照片中(1张)...");
                        }else if (phoneListPath.size()==3){
                            mSVProgressHUD.dismiss();
                            mSVProgressHUD.showSuccessWithStatus("出租钻机成功");
                            finish();
                        }
                        if (panoramaPath.length()>3){
                            intiPhontData3(id,"14",phoneListPath.get(3),ownId);
                        }






                    }else {
                        MyAppliction.showToast(appBean.getMsg());
                        mSVProgressHUD.dismiss();
                    }



                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("照片请求",s);
                mSVProgressHUD.dismiss();
            }
        });


    }
    private void intiPhontData3(final String id, String imageType, String imagePath, final String ownId) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requwstParams=new RequestParams();
        requwstParams.addBodyParameter("Id",id);
        requwstParams.addBodyParameter("ImageType",imageType);
        requwstParams.addBodyParameter("UserType","boss");
        requwstParams.addBodyParameter("SourceType","3");
        requwstParams.addBodyParameter("File", new File(imagePath));

            requwstParams.addBodyParameter("OwnId",ownId);

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getPhoneData(),requwstParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("照片请求",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<AppDataBean> appBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<AppDataBean>>(){});

                    if (appBean.getResult().equals("success")){
                        if (phoneListPath.size()==5){
                            mSVProgressHUD.showWithStatus("上传照片中(1)...");
                        }else if (phoneListPath.size()==4){
                            mSVProgressHUD.dismiss();
                            mSVProgressHUD.showSuccessWithStatus("出租钻机成功");
                            finish();
                        }
                        if (phoneListPath!=null&&phoneListPath.size()==5){

                            intiPhontData4(id,"15",phoneListPath.get(4),ownId);
                        }



                    }else {
                        MyAppliction.showToast("上传照片失败");
                        mSVProgressHUD.dismiss();
                    }



                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("照片请求",s);
                mSVProgressHUD.dismiss();
            }
        });


    }
    private void intiPhontData4(final String id, String userType, String imagePath,String ownId) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requwstParams=new RequestParams();
        requwstParams.addBodyParameter("Id",id);
        requwstParams.addBodyParameter("ImageType",userType);
        requwstParams.addBodyParameter("UserType","boss");
        requwstParams.addBodyParameter("SourceType","3");
        requwstParams.addBodyParameter("File", new File(imagePath));

            requwstParams.addBodyParameter("OwnId",ownId);

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getPhoneData(),requwstParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("照片请求",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<AppDataBean> appBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<AppDataBean>>(){});

                    if (appBean.getResult().equals("success")){

                        mSVProgressHUD.dismiss();
                        mSVProgressHUD.showSuccessWithStatus("出租钻机成功");
                        finish();



                    }else {
                        MyAppliction.showToast("上传照片失败");
                        mSVProgressHUD.dismiss();
                    }



                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("照片请求",s);
                mSVProgressHUD.dismiss();
            }
        });


    }


    //拍照和相册弹出框
    private void showDialog(final int captureIndext, final int pickIndext, final Uri uri) {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        final Dialog dialog = new Dialog(RentOutExtruderActivity.this, R.style.transparentFrameWindowStyle);
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
        Button pictureDialogButton= (Button) view.findViewById(R.id.picture_dialog_button);
        Button photographDialogButton= (Button) view.findViewById(R.id.photograph_dialog_button);
        Button cancelDialogButton= (Button) view.findViewById(R.id.cancel_dialog_button);
        pictureDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 指定调用相机拍照后照片的储存路径
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent,captureIndext );//or TAKE_SMALL_PICTURE
                dialog.dismiss();
            }
        });
        photographDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sintent = new Intent(RentOutExtruderActivity.this, SelectImagesFromLocalActivity.class);
                startActivityForResult(sintent,pickIndext );
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

                Intent tcutIntent = new Intent(RentOutExtruderActivity.this, ClippingPageActivity.class);
                tcutIntent.putExtra("type", "takePicture");
                startActivityForResult(tcutIntent, ConstantSet.CROPPICTURE);
                break;

            case ConstantSet.SELECTPICTURE:
                Intent scutIntent = new Intent(RentOutExtruderActivity.this, ClippingPageActivity.class);
                scutIntent.putExtra("type", "selectPicture");
                scutIntent.putExtra("path", data.getStringExtra("path"));
                startActivityForResult(scutIntent, ConstantSet.CROPPICTURE);
                break;


            case ConstantSet.CROPPICTURE:
                byte[] bis = data.getByteArrayExtra("result");
                Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
                if (bitmap != null) {
                    leaveFactoryImage.setImageBitmap(bitmap);
                }

                String frontName = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File frontFile = getFile(bis, "/sdcard/zhongJiYunImage/", frontName);
                if (!TextUtils.isEmpty(frontFile.getPath())) {
                    leavePath = frontFile.getPath();
                }

                break;
            case ConstantSet.TAKEPICTURE0:
                Intent tcutIntentOne = new Intent(RentOutExtruderActivity.this, ClippingPageActivity.class);
                tcutIntentOne.putExtra("type", "takePicture");
                startActivityForResult(tcutIntentOne, ConstantSet.CROPPICTURE0);


                break;

            case ConstantSet.SELECTPICTURE0:
                Intent scutIntentOne = new Intent(RentOutExtruderActivity.this, ClippingPageActivity.class);
                scutIntentOne.putExtra("type", "selectPicture");
                scutIntentOne.putExtra("path", data.getStringExtra("path"));
                startActivityForResult(scutIntentOne, ConstantSet.CROPPICTURE0);


                break;


            case ConstantSet.CROPPICTURE0:
                byte[] biss = data.getByteArrayExtra("result");
                Bitmap bitmaps = BitmapFactory.decodeByteArray(biss, 0, biss.length);
                if (bitmaps != null) {
                    panoramaImage.setImageBitmap(bitmaps);
                }
                String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File file2 = getFile(biss, "/sdcard/zhongJiYunImage/", name);
                if (!TextUtils.isEmpty(file2.getPath())) {
                    panoramaPath = file2.getPath();
                }
                break;
            case ConstantSet.TAKEPICTURE1:
                Intent persongeTakIntent = new Intent(RentOutExtruderActivity.this, ClippingPageActivity.class);
                persongeTakIntent.putExtra("type", "takePicture");
                startActivityForResult(persongeTakIntent, ConstantSet.CROPPICTURE1);


                break;

            case ConstantSet.SELECTPICTURE1:
                Intent scutPersongeIntent = new Intent(RentOutExtruderActivity.this, ClippingPageActivity.class);
                scutPersongeIntent.putExtra("type", "selectPicture");
                scutPersongeIntent.putExtra("path", data.getStringExtra("path"));
                startActivityForResult(scutPersongeIntent, ConstantSet.CROPPICTURE1);


                break;


            case ConstantSet.CROPPICTURE1:
                byte[] bisPersonge = data.getByteArrayExtra("result");
                Bitmap bitmapPersonge = BitmapFactory.decodeByteArray(bisPersonge, 0, bisPersonge.length);
                if (bitmapPersonge != null) {
                    invoiceImage.setImageBitmap(bitmapPersonge);
                }
                String namePersonge = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File filePersonge = getFile(bisPersonge, "/sdcard/zhongJiYunImage/", namePersonge);
                if (!TextUtils.isEmpty(filePersonge.getPath())) {
                    invoicePath = filePersonge.getPath();
                }
                break;
            case ConstantSet.TAKEPICTURE2:
                Intent companyFrontTakIntent = new Intent(RentOutExtruderActivity.this, ClippingPageActivity.class);
                companyFrontTakIntent.putExtra("type", "takePicture");
                startActivityForResult(companyFrontTakIntent, ConstantSet.CROPPICTURE2);


                break;

            case ConstantSet.SELECTPICTURE2:
                Intent companyFrontScutIntent = new Intent(RentOutExtruderActivity.this, ClippingPageActivity.class);
                companyFrontScutIntent.putExtra("type", "selectPicture");
                companyFrontScutIntent.putExtra("path", data.getStringExtra("path"));
                startActivityForResult(companyFrontScutIntent, ConstantSet.CROPPICTURE2);


                break;


            case ConstantSet.CROPPICTURE2:
                byte[] forntBis = data.getByteArrayExtra("result");
                Bitmap frontBitmap = BitmapFactory.decodeByteArray(forntBis, 0, forntBis.length);
                if (frontBitmap != null) {
                    contractImage.setImageBitmap(frontBitmap);
                }
                String nameFront = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File fileFront = getFile(forntBis, "/sdcard/zhongJiYunImage/", nameFront);
                if (!TextUtils.isEmpty(fileFront.getPath())) {
                    contractPath = fileFront.getPath();
                }
                break;
            case ConstantSet.TAKEPICTURE3:
                Intent companyVesonTakIntent = new Intent(RentOutExtruderActivity.this, ClippingPageActivity.class);
                companyVesonTakIntent.putExtra("type", "takePicture");
                startActivityForResult(companyVesonTakIntent, ConstantSet.CROPPICTURE3);


                break;

            case ConstantSet.SELECTPICTURE3:
                Intent companyVesonScutIntent = new Intent(RentOutExtruderActivity.this, ClippingPageActivity.class);
                companyVesonScutIntent.putExtra("type", "selectPicture");
                companyVesonScutIntent.putExtra("path", data.getStringExtra("path"));
                startActivityForResult(companyVesonScutIntent, ConstantSet.CROPPICTURE3);


                break;


            case ConstantSet.CROPPICTURE3:
                byte[] vesonBis = data.getByteArrayExtra("result");
                Bitmap vesonBitmap = BitmapFactory.decodeByteArray(vesonBis, 0, vesonBis.length);
                if (vesonBitmap != null) {
                    qualifiedImage.setImageBitmap(vesonBitmap);
                }
                String nameVeson = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File fileVeson = getFile(vesonBis, "/sdcard/zhongJiYunImage/", nameVeson);
                if (!TextUtils.isEmpty(fileVeson.getPath())) {
                    qualifiedPath = fileVeson.getPath();
                }
                break;


        }
    }

    /**
     * 根据byte数组，生成文件
     */
    public static File  getFile(byte[] bfile, String filePath,String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath+fileName);
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


        ProvinceCityDataBean provinceCityDataBean=JSONObject.parseObject(SelectData.selectCityData+SelectData.selectCityDataOne+SelectData.selectCityDataTwo,new TypeReference<ProvinceCityDataBean>(){});
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

        pvOptions = new OptionsPickerView(RentOutExtruderActivity.this);
       /* //三级联动效果
        pvOptions.setPicker(options1Items, options2Items, options3Items, true);*/
        //二级联动
        pvOptions.setPicker(options1Items, options2Items,  true);
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
        pvOptions.setTitle("选择城市");
        pvOptions.setCyclic(false, true, true);
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
                Province=options1Items.get(options1);
                City=options2Items.get(options1).get(option2);
                rentPartAddress.setText(tx);
                vMasker.setVisibility(View.GONE);
            }
        });
        //点击弹出选项选择器
        rentPartAddress.setOnClickListener(new View.OnClickListener() {

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
            finish();
            overridePendingTransition(R.anim.anim_open, R.anim.anim_close);

        }
        return true;
    }

}
