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
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.AppPhotoBean;
import com.example.zhongjiyun03.zhongjiyun.bean.PhotoDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.AppListDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.ExtruderDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.FacillyChildsBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.FacillyDataBean;
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

public class AddExtruderActivity extends AppCompatActivity implements View.OnClickListener {

        @ViewInject(R.id.title_name_tv)
        private TextView tailtName;     //biaoti
        @ViewInject(R.id.register_tv)
        private TextView registerText;   //右边
        @ViewInject(R.id.retrun_text_view)
        private TextView retrunText;       //左边



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
    private String dateOfManufacture;  //出厂年份
    private String dateMonthOfManufacture;  //出厂月份

        @ViewInject(R.id.save_button)
        private Button saveButton;  //保存数据按钮
        @ViewInject(R.id.serial_number_edit)
        private EditText serialNumberEdit;  //出厂编号
        @ViewInject(R.id.work_time_edit)
        private EditText workTimeEdit;       //工作时长
       @ViewInject(R.id.rood_layout)
       private ScrollView roodLayout;
       @ViewInject(R.id.leave_factory_layout)
       private LinearLayout leaveFactoryLayout; //出厂牌
       @ViewInject(R.id.leave_factory_image)
       private ImageView leaveFactoryImage;
       @ViewInject(R.id.panorama_layout)
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
        private String leavePath;
       private String panoramaPath;
       private String invoicePath;
       private String contractPath;
       private String qualifiedPath;
       private List<String> phoneListPath=new ArrayList<>();//图片路径集合
       private List<String> ImageTypeList =new ArrayList<>();


    private static final String IMAGE_FILE_LOCATION = ConstantSet.LOCALFILE;//temp file
      private SVProgressHUD mSVProgressHUD;//loding
      private File file;
      private Uri imageUri;



    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        //选项选择器
        pvOptions = new OptionsPickerView(AddExtruderActivity.this);
        initFacilly();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.red_light);//通知栏所需颜色
        }
        setContentView(R.layout.activity_add_extruder);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ViewUtils.inject(this);
        init();
        controlKeyboardLayout(roodLayout,saveButton);

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
    private void init() {
         initView();
    }

    private void initView() {
        tailtName.setText("添加钻机");
        registerText.setVisibility(View.GONE);
        retrunText.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        leaveFactoryLayout.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);
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

        facilityLayout.setOnClickListener(this);
        workAddressLayout.setOnClickListener(this);
        workTimeLayout.setOnClickListener(this);
        facillyDataBeens=new ArrayList<>();
        /**
         * 限制只能输入字母和数字，默认弹出英文输入法
         */
        serialNumberEdit.setKeyListener(new DigitsKeyListener() {
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
        SQLhelper sqLhelper=new SQLhelper(AddExtruderActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        switch (v.getId()){
            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.save_button:
                if (!TextUtils.isEmpty(uid)){
                 saveExtruderData(uid);
                }else {
                    Intent intent=new Intent(AddExtruderActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.leave_factory_layout:
                showDialog(ConstantSet.TAKEPICTURE,ConstantSet.SELECTPICTURE,imageUri);

                break;
            case R.id.panorama_layout:
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

    private void saveExtruderData(final String uid) {
            if (!TextUtils.isEmpty(serialNumberEdit.getText().toString())){
                if (!TextUtils.isEmpty(workTimeEdit.getText().toString())){
                    if (!TextUtils.isEmpty(facillyTextString)){
                        if (!TextUtils.isEmpty(workTimeTextString)){
                            if (!TextUtils.isEmpty(workAddressTextString)){
                                if (!TextUtils.isEmpty(MyAppliction.getLeaveFactoryId())){

                                    if (!TextUtils.isEmpty(MyAppliction.getPanoramaId())){

                                        if (!TextUtils.isEmpty(MyAppliction.getInvoiceId())||!TextUtils.isEmpty(MyAppliction.getContractId())){


                                    HttpUtils httpUtils=new HttpUtils();
                                    RequestParams requestParams=new RequestParams();
                                    //步骤1：创建一个SharedPreferences接口对象
                                    SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
                                    //步骤2：获取文件中的值
                                    String sesstionId = read.getString("code","");
                                    requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
                                    requestParams.addBodyParameter("Id",uid);
                                    requestParams.addBodyParameter("DeviceNo",serialNumberEdit.getText().toString());
                                    requestParams.addBodyParameter("HourOfWork",workTimeEdit.getText().toString());
                                    requestParams.addBodyParameter("DateOfManufacture",dateOfManufacture);
                                    requestParams.addBodyParameter("DateMonthOfManufacture",dateMonthOfManufacture);
                                    requestParams.addBodyParameter("Manufacture",manufacture);
                                    requestParams.addBodyParameter("NoOfManufacture",model);
                                    requestParams.addBodyParameter("Province",province);
                                    requestParams.addBodyParameter("City",city);
                                    requestParams.addBodyParameter("DeviceNoPhoto",MyAppliction.getLeaveFactoryId());
                                    requestParams.addBodyParameter("DevicePhoto",MyAppliction.getPanoramaId());
                                     if (!TextUtils.isEmpty(MyAppliction.getInvoiceId())){
                                         requestParams.addBodyParameter("DeviceInvoicePhoto",MyAppliction.getInvoiceId());
                                     }
                                     if (!TextUtils.isEmpty(MyAppliction.getContractId())){
                                         requestParams.addBodyParameter("DeviceContractPhoto",MyAppliction.getContractId());
                                     }
                                     if (!TextUtils.isEmpty(MyAppliction.getQualifiedId())){
                                        requestParams.addBodyParameter("DeviceCertificatePhoto",MyAppliction.getQualifiedId());
                                     }

                                    mSVProgressHUD.showWithStatus("正在提交中...");
                                    httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAddMyExtruderData(),requestParams, new RequestCallBack<String>() {
                                        @Override
                                        public void onSuccess(ResponseInfo<String> responseInfo) {
                                           // Log.e("增加钻机",responseInfo.result);
                                            if (!TextUtils.isEmpty(responseInfo.result)){
                                                AppBean<ExtruderDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<ExtruderDataBean>>(){});
                                                if (appBean.getResult().equals("success")){
                                                    /*ExtruderDataBean extruderDataBean=appBean.getData();
                                                    if (extruderDataBean!=null&&!TextUtils.isEmpty(extruderDataBean.getId())){
                                                        if (phoneListPath!=null&&phoneListPath.size()!=0) {

                                                            for (int i = 0; i <phoneListPath.size() ; i++) {
                                                                intiPhontData7(uid, ImageTypeList.get(i), phoneListPath.get(i), extruderDataBean.getId(),i);

                                                            }


                                                        }
                                                    }*/
                                                    MyAppliction.showToast("添加钻机成功");
                                                    mSVProgressHUD.dismiss();
                                                    finish();
                                                }else {
                                                    MyAppliction.showToast(appBean.getMsg());
                                                    mSVProgressHUD.dismiss();
                                                }
                                            }else {

                                                mSVProgressHUD.dismiss();
                                            }

                                        }

                                        @Override
                                        public void onFailure(HttpException e, String s) {
                                            mSVProgressHUD.dismiss();
                                            Log.e("tjiazji",s);
                                        }
                                    });



                                        }else {

                                            MyAppliction.showToast("请上传设备发票图或者设备合同图其中一个");
                                        }


                                    }else {

                                            MyAppliction.showToast("请上传设备全景图");
                                        }


                                }else {

                                   MyAppliction.showToast("请上传设备出厂牌图片");
                                }
                            }else {

                                MyAppliction.showToast("请输入所在地");
                            }


                        }else {

                            MyAppliction.showToast("请输入出厂时间");
                        }


                    }else {

                        MyAppliction.showToast("请输入设备型号");
                    }



                }else {

                    MyAppliction.showToast("请输入钻机工作时长");
                }



            }else {

                MyAppliction.showToast("请输入出厂编号");
            }





    }





    //拍照和相册弹出框
    private void showDialog(final int captureIndext, final int pickIndext, final Uri uri) {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        final Dialog dialog = new Dialog(AddExtruderActivity.this, R.style.transparentFrameWindowStyle);
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
                Intent sintent = new Intent(AddExtruderActivity.this, SelectImagesFromLocalActivity.class);
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

                Intent tcutIntent = new Intent(AddExtruderActivity.this, ClippingPageActivity.class);
                tcutIntent.putExtra("type", "takePicture");
                startActivityForResult(tcutIntent, ConstantSet.CROPPICTURE);
                break;

            case ConstantSet.SELECTPICTURE:
                Intent scutIntent = new Intent(AddExtruderActivity.this, ClippingPageActivity.class);
                scutIntent.putExtra("type", "selectPicture");
                scutIntent.putExtra("path", data.getStringExtra("path"));
                startActivityForResult(scutIntent, ConstantSet.CROPPICTURE);
                break;


            case ConstantSet.CROPPICTURE:
                byte[] bis = data.getByteArrayExtra("result");
                Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
                if (bitmap != null) {
                    String frontName = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                    File frontFile = getFile(bis, Environment.getExternalStorageDirectory() + "/zhongJiYun", frontName);
                    if (!TextUtils.isEmpty(frontFile.getPath())) {
                        upterImageData(1, bitmap, BitmapUtils.bitmapToString(frontFile.getPath()));
                    }
                } else {
                    MyAppliction.showToast("上传照片失败");
                }

                break;
            case ConstantSet.TAKEPICTURE0:
                Intent tcutIntentOne = new Intent(AddExtruderActivity.this, ClippingPageActivity.class);
                tcutIntentOne.putExtra("type", "takePicture");
                startActivityForResult(tcutIntentOne, ConstantSet.CROPPICTURE0);


                break;

            case ConstantSet.SELECTPICTURE0:
                Intent scutIntentOne = new Intent(AddExtruderActivity.this, ClippingPageActivity.class);
                scutIntentOne.putExtra("type", "selectPicture");
                scutIntentOne.putExtra("path", data.getStringExtra("path"));
                startActivityForResult(scutIntentOne, ConstantSet.CROPPICTURE0);


                break;


            case ConstantSet.CROPPICTURE0:
                byte[] biss = data.getByteArrayExtra("result");
                Bitmap bitmaps = BitmapFactory.decodeByteArray(biss, 0, biss.length);
                if (bitmaps != null) {
                    String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                    File file2 = getFile(biss, Environment.getExternalStorageDirectory() + "/zhongJiYun", name);
                    if (!TextUtils.isEmpty(file2.getPath())) {
                        upterImageData(2, bitmaps, BitmapUtils.bitmapToString(file2.getPath()));
                    }
                } else {
                    MyAppliction.showToast("上传照片失败");
                }
                break;
            case ConstantSet.TAKEPICTURE1:
                Intent persongeTakIntent = new Intent(AddExtruderActivity.this, ClippingPageActivity.class);
                persongeTakIntent.putExtra("type", "takePicture");
                startActivityForResult(persongeTakIntent, ConstantSet.CROPPICTURE1);


                break;

            case ConstantSet.SELECTPICTURE1:
                Intent scutPersongeIntent = new Intent(AddExtruderActivity.this, ClippingPageActivity.class);
                scutPersongeIntent.putExtra("type", "selectPicture");
                scutPersongeIntent.putExtra("path", data.getStringExtra("path"));
                startActivityForResult(scutPersongeIntent, ConstantSet.CROPPICTURE1);


                break;


            case ConstantSet.CROPPICTURE1:
                byte[] bisPersonge = data.getByteArrayExtra("result");
                Bitmap bitmapPersonge = BitmapFactory.decodeByteArray(bisPersonge, 0, bisPersonge.length);
                if (bitmapPersonge != null) {
                    String namePersonge = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                    File filePersonge = getFile(bisPersonge, Environment.getExternalStorageDirectory() + "/zhongJiYun", namePersonge);
                    if (!TextUtils.isEmpty(filePersonge.getPath())) {
                        upterImageData(3, bitmapPersonge, BitmapUtils.bitmapToString(filePersonge.getPath()));
                    }
                } else {
                    MyAppliction.showToast("上传照片失败");
                }
                break;
            case ConstantSet.TAKEPICTURE2:
                Intent companyFrontTakIntent = new Intent(AddExtruderActivity.this, ClippingPageActivity.class);
                companyFrontTakIntent.putExtra("type", "takePicture");
                startActivityForResult(companyFrontTakIntent, ConstantSet.CROPPICTURE2);


                break;

            case ConstantSet.SELECTPICTURE2:
                Intent companyFrontScutIntent = new Intent(AddExtruderActivity.this, ClippingPageActivity.class);
                companyFrontScutIntent.putExtra("type", "selectPicture");
                companyFrontScutIntent.putExtra("path", data.getStringExtra("path"));
                startActivityForResult(companyFrontScutIntent, ConstantSet.CROPPICTURE2);


                break;


            case ConstantSet.CROPPICTURE2:
                byte[] forntBis = data.getByteArrayExtra("result");
                Bitmap frontBitmap = BitmapFactory.decodeByteArray(forntBis, 0, forntBis.length);
                if (frontBitmap != null) {
                String nameFront = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File fileFront = getFile(forntBis, Environment.getExternalStorageDirectory() + "/zhongJiYun", nameFront);
                if (!TextUtils.isEmpty(fileFront.getPath())) {
                    upterImageData(4, frontBitmap, BitmapUtils.bitmapToString(fileFront.getPath()));
                }
                }else {
                    MyAppliction.showToast("上传照片失败");
                }

                break;
            case ConstantSet.TAKEPICTURE3:
                Intent companyVesonTakIntent = new Intent(AddExtruderActivity.this, ClippingPageActivity.class);
                companyVesonTakIntent.putExtra("type", "takePicture");
                startActivityForResult(companyVesonTakIntent, ConstantSet.CROPPICTURE3);


                break;

            case ConstantSet.SELECTPICTURE3:
                Intent companyVesonScutIntent = new Intent(AddExtruderActivity.this, ClippingPageActivity.class);
                companyVesonScutIntent.putExtra("type", "selectPicture");
                companyVesonScutIntent.putExtra("path", data.getStringExtra("path"));
                startActivityForResult(companyVesonScutIntent, ConstantSet.CROPPICTURE3);


                break;


            case ConstantSet.CROPPICTURE3:
                byte[] vesonBis = data.getByteArrayExtra("result");
                Bitmap vesonBitmap = BitmapFactory.decodeByteArray(vesonBis, 0, vesonBis.length);
                if (vesonBitmap != null) {
                String nameVeson = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File fileVeson = getFile(vesonBis,Environment.getExternalStorageDirectory()+"/zhongJiYun", nameVeson);
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


        if (tage==1){
            pvOptions.setTitle("选择所在地");
            pvOptions.setPicker(options1Items, options2Items,true);
            //设置默认选中的三级项目
            pvOptions.setSelectOptions(addressOptions01, addressOptions02);
        }else if (tage==2){
            pvOptions.setTitle("选择出厂时间");
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
            pvOptions.setTitle("选择设备型号");
            if (facilly1Items.size()!=0&&facilly2Items.size()!=0){
                pvOptions.setPicker(facilly1Items, facilly2Items,true);
                //设置默认选中的三级项目
                pvOptions.setSelectOptions(facillyOptions01, facillyOptions02);
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

    /**
     * @param root 最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    private void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    root.scrollTo(0, srollHeight);
                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
// KeyEvent.KEYCODE_BACK代表返回操作.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 处理返回操作.
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);

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
