package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddExtruderActivity extends AppCompatActivity implements View.OnClickListener {

        @ViewInject(R.id.title_name_tv)
        private TextView tailtName;     //biaoti
        @ViewInject(R.id.register_tv)
        private TextView registerText;   //右边
        @ViewInject(R.id.retrun_text_view)
        private TextView retrunText;       //左边

        @ViewInject(R.id.save_button)
        private Button saveButton;  //保存数据按钮
        @ViewInject(R.id.serial_number_edit)
        private EditText serialNumberEdit;  //出厂编号
        @ViewInject(R.id.work_time_edit)
        private EditText workTimeEdit;       //工作时长
       @ViewInject(R.id.model_edit)
       private TextView modelEdit;          //设备型号
       @ViewInject(R.id.leave_factory_time_edit)
       private TextView leaveFactoryTimeEdit;  //出厂时间
       @ViewInject(R.id.address_text)
       private TextView addressText;         //所在地
       private OptionsPickerView pvOptions;
       private OptionsPickerView pvOptionsTime;
       private OptionsPickerView pvOptionsType;
       @ViewInject(R.id.company_vMasker)
       private View vMasker;   //弹出选择地点view
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
       private List<String> phoneListPath=new ArrayList<>();
        private String DateOfManufacture; //年份
        private String DateMonthOfManufacture; //月份
       private String Manufacture;  //设备厂商
       private String NoOfManufacture; //设备型号
       private String Province;  //省份
       private String City; //城市




    private static final String IMAGE_FILE_LOCATION = ConstantSet.LOCALFILE;//temp file
      private SVProgressHUD mSVProgressHUD;//loding
      private File file;
      private Uri imageUri;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_extruder);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ViewUtils.inject(this);
        init();
        controlKeyboardLayout(roodLayout,saveButton);

    }

    private void init() {
         initView();
        intiPvAddress();
        facillyData();
        timeData();

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
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
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







        }
    }

    private void saveExtruderData(final String uid) {
            if (!TextUtils.isEmpty(serialNumberEdit.getText().toString())){
                if (!TextUtils.isEmpty(workTimeEdit.getText().toString())){
                    if (!TextUtils.isEmpty(modelEdit.getText().toString())){

                        if (!TextUtils.isEmpty(leaveFactoryTimeEdit.getText().toString())){

                            if (!TextUtils.isEmpty(addressText.getText().toString())){
                                if (!TextUtils.isEmpty(leavePath)){
                                    phoneListPath.add(leavePath);
                                    if (!TextUtils.isEmpty(panoramaPath)){
                                        phoneListPath.add(panoramaPath);
                                        if (!TextUtils.isEmpty(invoicePath)){
                                            phoneListPath.add(invoicePath);
                                            if (!TextUtils.isEmpty(contractPath)){
                                                phoneListPath.add(contractPath);
                                                if (!TextUtils.isEmpty(qualifiedPath)){
                                                    phoneListPath.add(qualifiedPath);
                                                }
                                    HttpUtils httpUtils=new HttpUtils();
                                    RequestParams requestParams=new RequestParams();
                                    //步骤1：创建一个SharedPreferences接口对象
                                    SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
                                    //步骤2：获取文件中的值
                                    String sesstionId = read.getString("code","");
                                    requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
                                    Log.e("数字SesstionId",sesstionId);
                                    requestParams.addBodyParameter("Id",uid);
                                    requestParams.addBodyParameter("DeviceNo",serialNumberEdit.getText().toString());
                                    requestParams.addBodyParameter("HourOfWork",workTimeEdit.getText().toString());
                                    requestParams.addBodyParameter("DateOfManufacture",DateOfManufacture);
                                    requestParams.addBodyParameter("DateMonthOfManufacture",DateMonthOfManufacture);
                                    requestParams.addBodyParameter("Manufacture",Manufacture);
                                    requestParams.addBodyParameter("NoOfManufacture",NoOfManufacture);
                                    requestParams.addBodyParameter("Province",Province);
                                    requestParams.addBodyParameter("City",City);
                                    requestParams.addBodyParameter("DeviceNoPhoto","photo.jpg");
                                    requestParams.addBodyParameter("DevicePhoto","photo.jpg");
                                    requestParams.addBodyParameter("DeviceInvoicePhoto","photo.jpg");
                                    requestParams.addBodyParameter("DeviceContractPhoto","photo.jpg");
                                    requestParams.addBodyParameter("DeviceCertificatePhoto","photo.jpg");
                                    if (phoneListPath.size()==5){
                                        mSVProgressHUD.showWithStatus("上传照片中(5)...");
                                    }else if (phoneListPath.size()==4){
                                        mSVProgressHUD.showWithStatus("上传照片中(4)...");
                                    }
                                    httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAddMyExtruderData(),requestParams, new RequestCallBack<String>() {
                                        @Override
                                        public void onSuccess(ResponseInfo<String> responseInfo) {
                                            Log.e("增加钻机",responseInfo.result);
                                            if (!TextUtils.isEmpty(responseInfo.result)){
                                                AppBean<ExtruderDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<ExtruderDataBean>>(){});
                                                if (appBean.getResult().equals("success")){
                                                    ExtruderDataBean extruderDataBean=appBean.getData();
                                                    if (extruderDataBean!=null&&!TextUtils.isEmpty(extruderDataBean.getId())){
                                                        if (phoneListPath!=null&&phoneListPath.size()!=0) {
                                                            intiPhontData0(uid, "6", phoneListPath.get(0), extruderDataBean.getId());

                                                        }
                                                    }
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

                                                MyAppliction.showToast("请选择设备合同图");
                                            }
                                        }else {

                                            MyAppliction.showToast("请选择设备发票图");
                                        }


                                    }else {

                                            MyAppliction.showToast("请选择设备全景图");
                                        }


                                        }else {

                                            MyAppliction.showToast("请选择设备出厂牌图片");
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



    private void intiPhontData0(final String id, String userType, String imagePath, final String OwnId) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requwstParams=new RequestParams();
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requwstParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        Log.e("imagePath",imagePath);
        requwstParams.addBodyParameter("Id",id);
        requwstParams.addBodyParameter("ImageType",userType);
        requwstParams.addBodyParameter("UserType","boss");
        requwstParams.addBodyParameter("SourceType","3");
        requwstParams.addBodyParameter("File", new File(imagePath));
        requwstParams.addBodyParameter("OwnId",OwnId);

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getPhoneData(),requwstParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("照片请求",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<AppDataBean> appBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<AppDataBean>>(){});

                    if (appBean.getResult().equals("success")){
                        if (phoneListPath.size()==5){
                            mSVProgressHUD.showWithStatus("上传照片中(4)...");
                        }else if (phoneListPath.size()==4){
                            mSVProgressHUD.showWithStatus("上传照片中(3)...");
                        }
                            intiPhontData1(id,"7",phoneListPath.get(1),OwnId);


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
    private void intiPhontData1(final String id, String userType, String imagePath, final String OwnId) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requwstParams=new RequestParams();
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requwstParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        requwstParams.addBodyParameter("Id",id);
        requwstParams.addBodyParameter("ImageType",userType);
        requwstParams.addBodyParameter("UserType","boss");
        requwstParams.addBodyParameter("SourceType","3");
        requwstParams.addBodyParameter("File", new File(imagePath));
        requwstParams.addBodyParameter("OwnId",OwnId);

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getPhoneData(),requwstParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("照片请求",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<AppDataBean> appBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<AppDataBean>>(){});

                    if (appBean.getResult().equals("success")){
                        if (phoneListPath.size()==5){
                            mSVProgressHUD.showWithStatus("上传照片中(3)...");
                        }else if (phoneListPath.size()==4){
                            mSVProgressHUD.showWithStatus("上传照片中(2)...");
                        }
                            intiPhontData2(id,"8",phoneListPath.get(2),OwnId);




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
    private void intiPhontData2(final String id, String userType, String imagePath, final String OwnId) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requwstParams=new RequestParams();
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requwstParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        requwstParams.addBodyParameter("Id",id);
        requwstParams.addBodyParameter("ImageType",userType);
        requwstParams.addBodyParameter("UserType","boss");
        requwstParams.addBodyParameter("SourceType","3");
        requwstParams.addBodyParameter("File", new File(imagePath));
        requwstParams.addBodyParameter("OwnId",OwnId);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getPhoneData(),requwstParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("照片请求",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<AppDataBean> appBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<AppDataBean>>(){});

                    if (appBean.getResult().equals("success")){
                        if (phoneListPath.size()==5){
                            mSVProgressHUD.showWithStatus("上传照片中(2)...");
                        }else if (phoneListPath.size()==4){
                            mSVProgressHUD.showWithStatus("上传照片中(1)...");
                        }
                            intiPhontData3(id,"9",phoneListPath.get(3),OwnId);





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
    private void intiPhontData3(final String id, String userType, String imagePath, final String OwnId) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requwstParams=new RequestParams();
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requwstParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        requwstParams.addBodyParameter("Id",id);
        requwstParams.addBodyParameter("ImageType",userType);
        requwstParams.addBodyParameter("UserType","boss");
        requwstParams.addBodyParameter("SourceType","3");
        requwstParams.addBodyParameter("File", new File(imagePath));
        requwstParams.addBodyParameter("OwnId",OwnId);
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
                            mSVProgressHUD.showWithStatus("上传照片中(0)...");
                        }
                        if (phoneListPath!=null&&phoneListPath.size()==5){

                            intiPhontData4(id,"10",phoneListPath.get(4),OwnId);
                        }else {
                            MyAppliction.showToast("添加钻机成功");
                            mSVProgressHUD.dismiss();
                            finish();
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
    private void intiPhontData4(final String id, String userType, String imagePath, final String OwnId) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requwstParams=new RequestParams();
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requwstParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        requwstParams.addBodyParameter("Id",id);
        requwstParams.addBodyParameter("ImageType",userType);
        requwstParams.addBodyParameter("UserType","boss");
        requwstParams.addBodyParameter("SourceType","3");
        requwstParams.addBodyParameter("File", new File(imagePath));
        requwstParams.addBodyParameter("OwnId",OwnId);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getPhoneData(),requwstParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("照片请求",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<AppDataBean> appBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<AppDataBean>>(){});

                    if (appBean.getResult().equals("success")){

                            MyAppliction.showToast("添加钻机成功");
                            mSVProgressHUD.dismiss();
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
                    leaveFactoryImage.setImageBitmap(bitmap);
                }

                String frontName = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File frontFile = getFile(bis, "/sdcard/zhongJiYunImage/", frontName);
                if (!TextUtils.isEmpty(frontFile.getPath())) {
                    leavePath = frontFile.getPath();
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
                    panoramaImage.setImageBitmap(bitmaps);
                }
                String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File file2 = getFile(biss, "/sdcard/zhongJiYunImage/", name);
                if (!TextUtils.isEmpty(file2.getPath())) {
                    panoramaPath = file2.getPath();
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
                    invoiceImage.setImageBitmap(bitmapPersonge);
                }
                String namePersonge = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File filePersonge = getFile(bisPersonge, "/sdcard/zhongJiYunImage/", namePersonge);
                if (!TextUtils.isEmpty(filePersonge.getPath())) {
                    invoicePath = filePersonge.getPath();
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
                    contractImage.setImageBitmap(frontBitmap);
                }
                String nameFront = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File fileFront = getFile(forntBis, "/sdcard/zhongJiYunImage/", nameFront);
                if (!TextUtils.isEmpty(fileFront.getPath())) {
                    contractPath = fileFront.getPath();
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
    public  void timeData(){
        //时间
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("DeviceJsonType","3");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getFacillyData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("设备厂商",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppListDataBean<FacillyDataBean> appListDataBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppListDataBean<FacillyDataBean>>(){});
                    if (appListDataBean.getResult().equals("success")){
                        List<FacillyDataBean> facillyDataBeen=  appListDataBean.getData();
                        //facilluyFirstList.addAll(facillyDataBeen);

                        intiPvTime(facillyDataBeen);

                    }



                }



            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


    }
     public  void facillyData(){
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
                         List<FacillyDataBean> facillyDataBeen=  appListDataBean.getData();
                         //facilluyFirstList.addAll(facillyDataBeen);

                         intiPvType(facillyDataBeen);

                     }



                 }



             }

             @Override
             public void onFailure(HttpException e, String s) {

             }
         });


     }

    private void intiPvType(List<FacillyDataBean> facillyDataBeen) {
        final ArrayList<String> options1Items = new ArrayList<String>();
        final ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
        for (int i = 0; i <facillyDataBeen.size() ; i++) {
            options1Items.add(facillyDataBeen.get(i).getText());
            List<FacillyChildsBean> facillyChildBean= facillyDataBeen.get(i).getChilds();
            ArrayList<String> arrayList=new ArrayList<String>();
            for (int j = 0; j < facillyChildBean.size(); j++) {
                arrayList.add(facillyChildBean.get(j).getText());
            }
            options2Items.add(arrayList);
        }
        //选项选择器
        pvOptionsType = new OptionsPickerView(AddExtruderActivity.this);

        //二级联动
        pvOptionsType.setPicker(options1Items, options2Items,  true);
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
        pvOptionsType.setTitle("选择设备型号");
        pvOptionsType.setCyclic(false, false, true);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptionsType.setSelectOptions(1, 1, 1);
        pvOptionsType.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
               /* //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(option2)
                        + options3Items.get(options1).get(option2).get(options3);*/
                //返回的分别是两个级别的选中位置
                String tx = options1Items.get(options1)
                        + options2Items.get(options1).get(option2);
                modelEdit.setText(tx);
                Manufacture=options1Items.get(options1);
                NoOfManufacture=options2Items.get(options1).get(option2);
                vMasker.setVisibility(View.GONE);
            }
        });
        //点击弹出选项选择器
        modelEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pvOptionsType.show();
            }
        });
    }

    private void intiPvTime (List<FacillyDataBean> facillyDataBeen) {
        final ArrayList<String> options1Items = new ArrayList<String>();
        final ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
        for (int i = 0; i <facillyDataBeen.size() ; i++) {
            options1Items.add(facillyDataBeen.get(i).getText());
            List<FacillyChildsBean> facillyChildBean= facillyDataBeen.get(i).getChilds();
            ArrayList<String> arrayList=new ArrayList<String>();
            for (int j = 0; j < facillyChildBean.size(); j++) {
                arrayList.add(facillyChildBean.get(j).getText());
            }
            options2Items.add(arrayList);
        }


        //选项选择器
        pvOptionsTime = new OptionsPickerView(AddExtruderActivity.this);

        //二级联动
        pvOptionsTime.setPicker(options1Items, options2Items,  true);
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
        pvOptionsTime.setTitle("选择年份月份");
        pvOptionsTime.setCyclic(false, false, true);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptionsTime.setSelectOptions(1, 1, 1);
        pvOptionsTime.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
               /* //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(option2)
                        + options3Items.get(options1).get(option2).get(options3);*/
                //返回的分别是两个级别的选中位置
                String tx = options1Items.get(options1)+"年"
                        + options2Items.get(options1).get(option2)+"月";
                leaveFactoryTimeEdit.setText(tx);
                DateOfManufacture=options1Items.get(options1);
                DateMonthOfManufacture=options2Items.get(options1).get(option2);
                vMasker.setVisibility(View.GONE);
            }
        });
        //点击弹出选项选择器
        leaveFactoryTimeEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pvOptionsTime.show();
            }
        });
    }
    private void intiPvAddress() {
        final ArrayList<String> options1Items = new ArrayList<String>();
        final ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();


        ProvinceCityDataBean provinceCityDataBean=JSONObject.parseObject(SelectData.selectCityDatas+SelectData.selectCityDataOnes+SelectData.selectCityDataTwos,new TypeReference<ProvinceCityDataBean>(){});
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








        //选项选择器
        pvOptions = new OptionsPickerView(AddExtruderActivity.this);
       /* //选项1
        options1Items.add(new ProvinceBean(0,"广东"));
        options1Items.add(new ProvinceBean(1,"湖南"));
        options1Items.add(new ProvinceBean(3,"广西"));

        //选项2
        ArrayList<String> options2Items_01=new ArrayList<String>();
        options2Items_01.add("广州");
        options2Items_01.add("佛山");
        options2Items_01.add("东莞");
        options2Items_01.add("阳江");
        options2Items_01.add("珠海");
        ArrayList<String> options2Items_02=new ArrayList<String>();
        options2Items_02.add("长沙");
        options2Items_02.add("岳阳");
        ArrayList<String> options2Items_03=new ArrayList<String>();
        options2Items_03.add("桂林");
        options2Items.add(options2Items_01);
        options2Items.add(options2Items_02);
        options2Items.add(options2Items_03);*/


        //二级联动

        pvOptions.setPicker(options1Items, options2Items,  true);
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
                //返回的分别是三个级别的选中位置
               /* String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(option2)
                        + options3Items.get(options1).get(option2).get(options3);*/
                //返回的分别是两个级别的选中位置
                String tx = options1Items.get(options1)
                        + options2Items.get(options1).get(option2);
                Province=options1Items.get(options1);
                City=options2Items.get(options1).get(option2);
                addressText.setText(tx);
                vMasker.setVisibility(View.GONE);
            }
        });
        //点击弹出选项选择器
        addressText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pvOptions.show();
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
            overridePendingTransition(R.anim.anim_open, R.anim.anim_close);

        }
        return true;
    }
}
