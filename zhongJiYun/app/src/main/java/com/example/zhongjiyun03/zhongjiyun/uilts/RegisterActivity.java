package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.MyAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.AppDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.AppPhotoBean;
import com.example.zhongjiyun03.zhongjiyun.bean.PhotoDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.RegisterBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityChildsBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.SelectData;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;

/*
*  注册
* */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


      @ViewInject(R.id.retrun_text_view)
      private TextView retrunTv;
      @ViewInject(R.id.title_name_tv)
      private TextView titlNameTv;
      @ViewInject(R.id.register_tv)
      private TextView registerTv;



    @ViewInject(R.id.viewPager)
    private ViewPager viewPager;
    @ViewInject(R.id.cursor)
    private ImageView imageView;
    private List<View> lists = new ArrayList<View>();
    private MyAdapter myAdapter;
    private Bitmap cursor;
    private int offSet;
    private int currentItem;
    private Matrix matrix = new Matrix();
    private int bmWidth;
    private Animation animation;
    @ViewInject(R.id.textView1)
    private TextView textView1;
    @ViewInject(R.id.textView2)
    private TextView textView2;
    private  View personageRisterView; //个人注册view
    private  View companyRisterView; //企业注册view
    private Button codeButton;       //获取验证码按钮
    private TimeCount time;      //获取验证码计时线程
    private EditText phoneEdit;   //手机号码输入框
    private Button submitButton; // 提交按钮

    private EditText codeEdit;  //验证码输入框
    private EditText nameEdit;     //名字输入框
    private EditText idCardEdit;    //身份证输入框
    private EditText recommentEdit;  //推荐人手机号输入框
    private EditText abstractEdit;    //简介输入框
    private TextView addressTextView;
    private View vMasker;
    private  OptionsPickerView pvOptions;
    private ImageView frontIdcardImage;
    private RelativeLayout frontUIdCardLayout; //身份证正面
    private ImageView certificateImage;
    private RelativeLayout certificateLayout;
    private String imageVersoPath;   //个人注册身份证反面路径
    private String frontImagePath; //个人注册身份证正面路径
    private String personageImagePath; //个人注册个人照片路径
    private List<String> imagePathList=new ArrayList<>();
    private int tage=3;
    private static final String IMAGE_FILE_LOCATION = ConstantSet.LOCALFILE;//temp file
    private File file ;   //拍照文件
    private Uri imageUri ;   //拍照uri
    private File fileVeson;
    private Uri vesonUri;    //文件uri
    private String province;  //个人注册省份
    private String city;      //个人注册城市
    private ScrollView personageScrollview;  //个人注册scrollView


    /*
      企业注册
    * */
    private TimeCompanyCount companyTime;
    private Button companyCodeButton;       //获取验证码按钮
    private EditText companyPhoneEdit;   //企业手机号码输入框
    private Button companyRegisterButton; // 提交按钮
    private EditText companyCodeEdit;  //验证码输入框
    private EditText companyNameEdit;     //名字输入框
    private EditText companyIdCardEdit;    //身份证输入框
    private EditText companyRecommentEdit;  //推荐人手机号输入框
    private EditText companyIntroduceEdit;    //简介输入框
    private TextView companyAddressTextView;   //地址
    private ImageView  companyFrontImage;//身份证正面
    private RelativeLayout companyFrontLayout;//身份证正面layout
    private ImageView  companyPersongeImage;//个人照片
    private RelativeLayout companyPersongeLayout;//个人layout
    private ImageView  companyTradingImage;//营业执照
    private RelativeLayout companyTradingLayout;//营业执照layout

    private View vMaskerCompany;
    private OptionsPickerView pvOptionsCompany;  //地区选择
    private SVProgressHUD mSVProgressHUD;//loding
    private String companyFrontPath;   //身份证正面照片路径
    private String companyVesonPath;    //身份证反面照片路径
    private String companyPersongePath;  //个人照片照片路径
    private String companyTradingPath;    //营业执照照片路径
    private String companyStatusPath;      //证书照片路径
    private List<String> companyListPath=new ArrayList<>();//企业注册照片路径集合
    private  String companyProvince;    //省份
    private String companyCity;         //城市
    @ViewInject(R.id.company_scrollView)
    private ScrollView companyScrollView;  //企业注册scrollView



    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
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
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ViewUtils.inject(this);
        init();

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
        intiView();
        intiPersonageView();
        intiCompanyView();



    }




    //企业注册初始化
    private void intiCompanyView() {
        RelativeLayout rootTlayout= (RelativeLayout) companyRisterView.findViewById(R.id.company_root_layout);
        companyScrollView= (ScrollView) companyRisterView.findViewById(R.id.company_scrollView);
        companyCodeButton= (Button) companyRisterView.findViewById(R.id.company_code_button);
        companyRegisterButton= (Button) companyRisterView.findViewById(R.id.cpmpany_register_button);
        companyPhoneEdit= (EditText) companyRisterView.findViewById(R.id.company_edit_phone);
        companyCodeEdit= (EditText) companyRisterView.findViewById(R.id.company_code_edit);
        companyNameEdit= (EditText) companyRisterView.findViewById(R.id.company_name_edit);
        companyIdCardEdit= (EditText) companyRisterView.findViewById(R.id.company_idcar_edit);
        companyRecommentEdit= (EditText) companyRisterView.findViewById(R.id.company_recomment_edit);
        companyIntroduceEdit= (EditText) companyRisterView.findViewById(R.id.company_introduce_edit);
        companyAddressTextView= (TextView) companyRisterView.findViewById(R.id.company_address_text);

        companyFrontImage= (ImageView) companyRisterView.findViewById(R.id.company_front_image);//身份证正面
        companyFrontLayout= (RelativeLayout) companyRisterView.findViewById(R.id.company_front_layout);//身份证正面layout
        companyPersongeImage= (ImageView) companyRisterView.findViewById(R.id.company_personge_image);//个人照片
        companyPersongeLayout= (RelativeLayout) companyRisterView.findViewById(R.id.company_personge_layout);//个人照片layout
        companyTradingImage= (ImageView) companyRisterView.findViewById(R.id.company_trading_image);//营业执照
        companyTradingLayout= (RelativeLayout) companyRisterView.findViewById(R.id.company_trading_layout);//营业执照layout
        controlKeyboardLayout(rootTlayout,submitButton);
        vMaskerCompany=companyRisterView.findViewById(R.id.company_vMasker);
        companyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intiVcodeData(2,companyPhoneEdit.getText().toString());
            }
        });
        intiPvCompanyTime();
        // 提交数据
        companyRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCompanyData();
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        });
        //身份证正面拍照
        companyFrontLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(ConstantSet.TAKEPICTURE2,ConstantSet.SELECTPICTURE2,imageUri);
            }
        });

        //个人照片拍照
        companyPersongeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(ConstantSet.TAKEPICTURE4,ConstantSet.SELECTPICTURE4,imageUri);
            }
        });
        //营业执照拍照
        companyTradingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(ConstantSet.TAKEPICTURE5,ConstantSet.SELECTPICTURE5,imageUri);
            }
        });

        companyIntroduceEdit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        companyScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                companyIntroduceEdit.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });









    }


    //个人注册数据初始化
    private void intiPersonageView() {

        RelativeLayout rootTlayout= (RelativeLayout) personageRisterView.findViewById(R.id.root_layout);
        personageScrollview= (ScrollView) personageRisterView.findViewById(R.id.personage_scrollview);
        codeButton= (Button) personageRisterView.findViewById(R.id.code_button);
        submitButton= (Button) personageRisterView.findViewById(R.id.submit_button);
        phoneEdit= (EditText) personageRisterView.findViewById(R.id.edit_phone);
        codeEdit= (EditText) personageRisterView.findViewById(R.id.code_edit);
        nameEdit= (EditText) personageRisterView.findViewById(R.id.name_edit);
        idCardEdit= (EditText) personageRisterView.findViewById(R.id.id_card_edit);
        recommentEdit= (EditText) personageRisterView.findViewById(R.id.recommend_edit);
        abstractEdit= (EditText) personageRisterView.findViewById(R.id.abstract_edit);
        addressTextView= (TextView) personageRisterView.findViewById(R.id.address_textview);
        vMasker= personageRisterView.findViewById(R.id.vMasker);
        frontIdcardImage= (ImageView) personageRisterView.findViewById(R.id.front_idcard_image);//身份证正面
        frontUIdCardLayout= (RelativeLayout) personageRisterView.findViewById(R.id.front_idcard_layout);//身份证正面layout
        certificateImage= (ImageView) personageRisterView.findViewById(R.id.certificate_image);//证书
        certificateLayout= (RelativeLayout) personageRisterView.findViewById(R.id.certificate_layout);//证书layout
        controlKeyboardLayout(rootTlayout,submitButton);
        //选项选择器
        pvOptions = new OptionsPickerView(RegisterActivity.this);

        codeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intiVcodeData(1,phoneEdit.getText().toString());
            }
        });
        // 提交数据
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPersonageData();
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


            }
        });
        intiPvTime();

        frontUIdCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(ConstantSet.TAKEPICTURE,ConstantSet.SELECTPICTURE,imageUri);

            }
        });
        certificateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(ConstantSet.TAKEPICTURE1,ConstantSet.SELECTPICTURE1,imageUri);
            }
        });

        abstractEdit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        personageScrollview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                abstractEdit.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });



    }
       //拍照和相册弹出框
    private void showDialog(final int captureIndext, final int pickIndext, final Uri uri) {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        final Dialog dialog = new Dialog(RegisterActivity.this, R.style.transparentFrameWindowStyle);
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
                Intent sintent = new Intent(RegisterActivity.this, SelectImagesFromLocalActivity.class);
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
    public static String savePicToSdcard(Bitmap bitmap, String path,
                                         String fileName) {
        String filePath = "";
        if (bitmap == null) {
            return filePath;
        } else {

            filePath=path+ fileName;
            File destFile = new File(filePath);
            OutputStream os = null;
            try {
                os = new FileOutputStream(destFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
            } catch (IOException e) {
                filePath = "";
            }
        }
        return filePath;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {

            return;

        }
        switch (requestCode) {

            case ConstantSet.TAKEPICTURE:

                Intent tcutIntent = new Intent(RegisterActivity.this, ClippingPageActivity.class);
                tcutIntent.putExtra("type", "takePicture");
                startActivityForResult(tcutIntent, ConstantSet.CROPPICTURE);
                break;

            case ConstantSet.SELECTPICTURE:
                Intent scutIntent = new Intent(RegisterActivity.this, ClippingPageActivity.class);
                scutIntent.putExtra("type", "selectPicture");
                scutIntent.putExtra("path",data.getStringExtra("path"));
                startActivityForResult(scutIntent, ConstantSet.CROPPICTURE);
                break;


            case ConstantSet.CROPPICTURE:
                byte[] bis = data.getByteArrayExtra("result");
                Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
                if (bitmap!=null){
                //data.getStringExtra("uri");
                String frontName = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                 File frontFile=  getFile(bis,Environment.getExternalStorageDirectory()+"/zhongJiYun",frontName);
                if (!TextUtils.isEmpty(frontFile.getPath())){
                    //frontImagePath=frontFile.getPath();
                    upterImageData(1,bitmap,BitmapUtils.bitmapToString(frontFile.getPath()));
                }
                }else {
                    MyAppliction.showToast("上传照片失败");
                }

                break;

            case ConstantSet.TAKEPICTURE1:
                Intent persongeTakIntent = new Intent(RegisterActivity.this, ClippingPageActivity.class);
                persongeTakIntent.putExtra("type", "takePicture");
                startActivityForResult(persongeTakIntent, ConstantSet.CROPPICTURE1);

                break;

            case ConstantSet.SELECTPICTURE1:
                Intent scutPersongeIntent = new Intent(RegisterActivity.this, ClippingPageActivity.class);
                scutPersongeIntent.putExtra("type", "selectPicture");
                scutPersongeIntent.putExtra("path",data.getStringExtra("path"));
                startActivityForResult(scutPersongeIntent, ConstantSet.CROPPICTURE1);


                break;


            case ConstantSet.CROPPICTURE1:
                byte[] bisPersonge = data.getByteArrayExtra("result");
                Bitmap bitmapPersonge = BitmapFactory.decodeByteArray(bisPersonge, 0, bisPersonge.length);
                if (bitmapPersonge!=null){
                String namePersonge = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File filePersonge=  getFile(bisPersonge,Environment.getExternalStorageDirectory()+"/zhongJiYun",namePersonge);
                if (!TextUtils.isEmpty(filePersonge.getPath())){
                    upterImageData(2,bitmapPersonge,BitmapUtils.bitmapToString(filePersonge.getPath()));
                }
                }else {
                    MyAppliction.showToast("上传照片失败");
                }

                break;
            case ConstantSet.TAKEPICTURE2:
                Intent companyFrontTakIntent = new Intent(RegisterActivity.this, ClippingPageActivity.class);
                companyFrontTakIntent.putExtra("type", "takePicture");
                startActivityForResult(companyFrontTakIntent, ConstantSet.CROPPICTURE2);



                break;

            case ConstantSet.SELECTPICTURE2:
                Intent companyFrontScutIntent = new Intent(RegisterActivity.this, ClippingPageActivity.class);
                companyFrontScutIntent.putExtra("type", "selectPicture");
                companyFrontScutIntent.putExtra("path",data.getStringExtra("path"));
                startActivityForResult(companyFrontScutIntent, ConstantSet.CROPPICTURE2);


                break;


            case ConstantSet.CROPPICTURE2:
                byte[] forntBis = data.getByteArrayExtra("result");
                Bitmap frontBitmap = BitmapFactory.decodeByteArray(forntBis, 0, forntBis.length);
                if (frontBitmap!=null){
                String nameFront = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File fileFront=  getFile(forntBis,Environment.getExternalStorageDirectory()+"/zhongJiYun",nameFront);
                if (!TextUtils.isEmpty( fileFront.getPath())){
                    upterImageData(3,frontBitmap,BitmapUtils.bitmapToString(fileFront.getPath()));
                }
                }else {
                    MyAppliction.showToast("上传照片失败");
                }

                break;


            case ConstantSet.TAKEPICTURE4:
                Intent companyPersongeTakIntent = new Intent(RegisterActivity.this, ClippingPageActivity.class);
                companyPersongeTakIntent.putExtra("type", "takePicture");
                startActivityForResult(companyPersongeTakIntent, ConstantSet.CROPPICTURE4);



                break;

            case ConstantSet.SELECTPICTURE4:
                Intent companyPersongeScutIntent = new Intent(RegisterActivity.this, ClippingPageActivity.class);
                companyPersongeScutIntent.putExtra("type", "selectPicture");
                companyPersongeScutIntent.putExtra("path",data.getStringExtra("path"));
                startActivityForResult(companyPersongeScutIntent, ConstantSet.CROPPICTURE4);


                break;


            case ConstantSet.CROPPICTURE4:
                byte[] comopanyPersongeBis = data.getByteArrayExtra("result");
                Bitmap companyPersongeBitmap = BitmapFactory.decodeByteArray(comopanyPersongeBis, 0, comopanyPersongeBis.length);
                if (companyPersongeBitmap!=null){
                String nameCompanyPersonge = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File fileCompanyPersonge=  getFile(comopanyPersongeBis,Environment.getExternalStorageDirectory()+"/zhongJiYun",nameCompanyPersonge);
                if (!TextUtils.isEmpty( fileCompanyPersonge.getPath())){
                    companyPersongePath= fileCompanyPersonge.getPath();
                    upterImageData(4,companyPersongeBitmap,BitmapUtils.bitmapToString(fileCompanyPersonge.getPath()));
                }
                }else {
                    MyAppliction.showToast("上传照片失败");
                }
                break;
            case ConstantSet.TAKEPICTURE5:
                Intent companyTradingTakIntent = new Intent(RegisterActivity.this, ClippingPageActivity.class);
                companyTradingTakIntent.putExtra("type", "takePicture");
                startActivityForResult(companyTradingTakIntent, ConstantSet.CROPPICTURE5);



                break;

            case ConstantSet.SELECTPICTURE5:
                Intent companyTradingScutIntent = new Intent(RegisterActivity.this, ClippingPageActivity.class);
                companyTradingScutIntent.putExtra("type", "selectPicture");
                companyTradingScutIntent.putExtra("path",data.getStringExtra("path"));
                startActivityForResult(companyTradingScutIntent, ConstantSet.CROPPICTURE5);


                break;


            case ConstantSet.CROPPICTURE5:
                byte[] tradingBis = data.getByteArrayExtra("result");
                Bitmap tradingBitmap = BitmapFactory.decodeByteArray(tradingBis, 0, tradingBis.length);
                if (tradingBitmap!=null) {
                    if (tradingBitmap != null) {

                        String tradingString = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                        File fileCompanyStringrading = getFile(tradingBis, Environment.getExternalStorageDirectory() + "/zhongJiYun", tradingString);
                        if (!TextUtils.isEmpty(fileCompanyStringrading.getPath())) {
                            upterImageData(5, tradingBitmap, BitmapUtils.bitmapToString(fileCompanyStringrading.getPath()));
                        }

                    }
                }else {

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
                                    MyAppliction.setIdCardFrontId(photoDataBean.getId());
                                    frontIdcardImage.setImageBitmap(bitmap);

                                }else if (tage==2){
                                    MyAppliction.setCertificateId(photoDataBean.getId());
                                    certificateImage.setImageBitmap(bitmap);
                                }else if (tage==3){
                                    MyAppliction.setCompanyFrontId(photoDataBean.getId());
                                    companyFrontImage.setImageBitmap(bitmap);

                                }else if (tage==4){
                                    MyAppliction.setCompanyPersongeId(photoDataBean.getId());
                                    companyPersongeImage.setImageBitmap(bitmap);
                                }else if (tage==5){
                                    MyAppliction.setCompanyTradingId(photoDataBean.getId());
                                    companyTradingImage.setImageBitmap(bitmap);
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


    //企业注册数据提交
    private void submitCompanyData() {
        if (!TextUtils.isEmpty(companyPhoneEdit.getText().toString())){
            if (companyPhoneEdit.getText().length()==11){
                if (!TextUtils.isEmpty(companyCodeEdit.getText().toString())){
                    if (!TextUtils.isEmpty(companyNameEdit.getText().toString())){

                        if (!TextUtils.isEmpty(companyIdCardEdit.getText().toString())){

                                if (!TextUtils.isEmpty(companyAddressTextView.getText().toString())){

                                    if (!TextUtils.isEmpty(companyIntroduceEdit.getText().toString())){

                                        if (!TextUtils.isEmpty(MyAppliction.getCompanyFrontId())){

                                                if (!TextUtils.isEmpty(MyAppliction.getCompanyPersongeId())){
                                                    if (!TextUtils.isEmpty(MyAppliction.getCompanyTradingId())){
                                                            HttpUtils httpUtils=new HttpUtils();
                                                            RequestParams requestParams=new RequestParams();
                                                            requestParams.addBodyParameter("phoneNumber",companyPhoneEdit.getText().toString());
                                                            requestParams.addBodyParameter("smsCode",companyCodeEdit.getText().toString());
                                                            requestParams.addBodyParameter("name",companyNameEdit.getText().toString());
                                                            requestParams.addBodyParameter("idCard",companyIdCardEdit.getText().toString());
                                                            if (!TextUtils.isEmpty(recommentEdit.getText().toString())){
                                                                requestParams.addBodyParameter("referralPhone",recommentEdit.getText().toString());
                                                            }
                                                            requestParams.addBodyParameter("province",companyProvince);
                                                            requestParams.addBodyParameter("city",companyCity);
                                                            requestParams.addBodyParameter("Summary",companyIntroduceEdit.getText().toString());
                                                            requestParams.addBodyParameter("BossType","2");
                                                            requestParams.addBodyParameter("IdCardImage1Id",MyAppliction.getCompanyFrontId());
                                                            requestParams.addBodyParameter("PhotoId",MyAppliction.getCompanyPersongeId());
                                                            requestParams.addBodyParameter("BusinessLicenceId",MyAppliction.getCompanyTradingId());
                                                            mSVProgressHUD.showWithStatus("正在提交中...");
                                                            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRegisterData(),requestParams, new RequestCallBack<String>() {
                                                                @Override
                                                                public void onSuccess(ResponseInfo<String> responseInfo) {

                                                                    //Log.e("企业注册",responseInfo.result);
                                                                    AppBean<RegisterBean> appDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<RegisterBean>>(){});
                                                                    if ((appDataBean.getResult()).equals("success")){
                                                                       /* RegisterBean registerBean=   appDataBean.getData();
                                                                       if (companyListPath!=null&&companyListPath.size()==5){
                                                                            if (!TextUtils.isEmpty(registerBean.getId())){

                                                                                int[] imageType={1,2,0,3,4};
                                                                                for (int i = 0; i <companyListPath.size() ; i++) {
                                                                                    intiPhontData7(registerBean.getId(),imageType[i],companyListPath.get(i),i,2);

                                                                                }
                                                                            }
                                                                        }*/

                                                                        MyAppliction.showToast("提交成功，请耐心等待审核");
                                                                        Intent intent=new Intent(RegisterActivity.this,RegisterFishActivity.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                        mSVProgressHUD.dismiss();


                                                                    }else {

                                                                        MyAppliction.showToast(appDataBean.getMsg());
                                                                        mSVProgressHUD.dismiss();
                                                                    }

                                                                }

                                                                @Override
                                                                public void onFailure(HttpException e, String s) {
                                                                    mSVProgressHUD.dismiss();
                                                                }
                                                            });





                                                        }else {

                                                            MyAppliction.showToast("请上传营业执照或者资格证书照片");
                                                        }

                                                    }else {

                                                    MyAppliction.showToast("请上传个人照片");
                                                }


                                        }else {
                                         MyAppliction.showToast("请上传身份证正面照片");

                                        }

                                    }else {

                                        MyAppliction.showToast("请填写一下自我简介");


                                    }

                                }else {
                                    MyAppliction.showToast("请选择所在地");


                                }



                        }else {

                            MyAppliction.showToast("请输入你的身份证号码");

                        }

                    }else {

                        MyAppliction.showToast("请输入您的姓名");

                    }

                }else {
                    MyAppliction.showToast("请输入验证码");

                }

            }else {

                MyAppliction.showToast("请输入长度为11位的手机号码");
            }


        }else {
            MyAppliction.showToast("请输入手机号码");

        }





    }


    private void intiPhontData7(String id, int imageType, final String imagePath,  final int tages, final int registerTage) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requwstParams=new RequestParams();
        requwstParams.addBodyParameter("Id",id);
        requwstParams.addBodyParameter("ImageType",imageType+"");
        requwstParams.addBodyParameter("UserType","boss");
        requwstParams.addBodyParameter("SourceType","0");
        requwstParams.addBodyParameter("File", new File(imagePath));
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requwstParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        httpUtils.configSoTimeout(1200000);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getPhoneData(),requwstParams, new RequestCallBack<String>() {

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("照片请求",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<AppDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<AppDataBean>>(){});
                    int tage=tages+1;
                    if (appBean.getResult().equals("success")){

                        if (registerTage==1){
                            if (tage==imagePathList.size()){

                                mSVProgressHUD.showSuccessWithStatus("恭喜,已提交数据成功");
                                Intent intent=new Intent(RegisterActivity.this,RegisterFishActivity.class);
                                startActivity(intent);
                                finish();
                                mSVProgressHUD.dismiss();
                            }
                        }else {
                            if (tage==companyListPath.size()){
                                mSVProgressHUD.showSuccessWithStatus("恭喜,已提交数据成功");
                                Intent intent=new Intent(RegisterActivity.this,RegisterFishActivity.class);
                                startActivity(intent);
                                finish();
                                mSVProgressHUD.dismiss();
                            }
                        }


                    }else {
                        MyAppliction.showToast("上传照片失败");
                        mSVProgressHUD.dismiss();
                    }



                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("照片请求s",s);
                    MyAppliction.showToast("网络异常,请稍后重试");
                    mSVProgressHUD.dismiss();
                    finish();



            }
        });


    }




    //个人注册数据提交
    private void submitPersonageData() {
          if (!TextUtils.isEmpty(phoneEdit.getText().toString())){

              if (phoneEdit.getText().length()==11){
                 if (!TextUtils.isEmpty(codeEdit.getText().toString())){
                     if (!TextUtils.isEmpty(nameEdit.getText().toString())){

                         if (!TextUtils.isEmpty(idCardEdit.getText().toString())){

                             if (idCardEdit.getText().toString().length()==18){
                                 if (!TextUtils.isEmpty(addressTextView.getText().toString())){

                                         if (!TextUtils.isEmpty(abstractEdit.getText().toString())){

                                         if (!TextUtils.isEmpty(MyAppliction.getIdCardFrontId())){

                                             if (!TextUtils.isEmpty(MyAppliction.getCertificateId())){
                                             HttpUtils httpUtils=new HttpUtils();
                                             RequestParams requestParams=new RequestParams();
                                             requestParams.addBodyParameter("phoneNumber",phoneEdit.getText().toString());
                                             requestParams.addBodyParameter("smsCode",codeEdit.getText().toString());
                                             requestParams.addBodyParameter("name",nameEdit.getText().toString());
                                             requestParams.addBodyParameter("idCard",idCardEdit.getText().toString());
                                             if (!TextUtils.isEmpty(recommentEdit.getText().toString())){
                                                 requestParams.addBodyParameter("referralPhone",recommentEdit.getText().toString());
                                             }
                                             requestParams.addBodyParameter("province",province);
                                             requestParams.addBodyParameter("city",city);
                                             requestParams.addBodyParameter("summary",abstractEdit.getText().toString());
                                             requestParams.addBodyParameter("bossType","1");
                                             requestParams.addBodyParameter("IdCardImage1Id",MyAppliction.getIdCardFrontId());
                                             requestParams.addBodyParameter("PhotoId",MyAppliction.getCertificateId());
                                             mSVProgressHUD.showWithStatus("正在提交中...");
                                             httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRegisterData(), requestParams,new RequestCallBack<String>() {
                                                 @Override
                                                 public void onSuccess(ResponseInfo<String> responseInfo) {
                                                     //Log.e("注册信息",responseInfo.result);
                                                     AppBean<RegisterBean> appDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<RegisterBean>>(){});
                                                     if ((appDataBean.getResult()).equals("success")){
                                                         /*RegisterBean registerBean=   appDataBean.getData();
                                                         if (imagePathList!=null&&imagePathList.size()!=0){
                                                             int[] imageType={1,2,0};
                                                             for (int i = 0; i <imagePathList.size() ; i++) {
                                                                 intiPhontData7(registerBean.getId(),imageType[i],imagePathList.get(i),i,1);
                                                             }
                                                         }else {
                                                             mSVProgressHUD.dismiss();
                                                         }*/
                                                         MyAppliction.showToast("提交成功，请耐心等待审核");
                                                         Intent intent=new Intent(RegisterActivity.this,RegisterFishActivity.class);
                                                         startActivity(intent);
                                                         finish();
                                                         mSVProgressHUD.dismiss();
                                                     }else {

                                                         MyAppliction.showToast(appDataBean.getMsg());
                                                         mSVProgressHUD.dismiss();
                                                     }


                                                 }

                                                 @Override
                                                 public void onFailure(HttpException e, String s) {
                                                     Log.e("注册信息",s);
                                                     MyAppliction.showToast("网络异常,请稍后重试");
                                                     mSVProgressHUD.dismiss();
                                                 }
                                             });


                                             }else {
                                                 MyAppliction.showToast("请上传本人照片");

                                             }
                                             }else {
                                                 MyAppliction.showToast("请上传身份证正面照片");

                                             }

                                         }else {
                                             MyAppliction.showToast("请填写一下自我简介");

                                         }
                                 }else {
                                     MyAppliction.showToast("请选择所在地");


                                 }

                             }else {

                                 MyAppliction.showToast("请输入长度为18位的身份证号码");

                             }

                         }else {

                             MyAppliction.showToast("请输入你的身份证号码");

                         }

                     }else {

                         MyAppliction.showToast("请输入您的姓名");

                     }

                 }else {
                     MyAppliction.showToast("请输入验证码");

                 }

              }else {

                  MyAppliction.showToast("请输入长度为11位的手机号码");
              }


          }else {
            MyAppliction.showToast("请输入手机号码");

          }





    }

    private void intiView() {
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        companyTime=new TimeCompanyCount(60000, 1000);//构造CountDownTimer对象企业注册
        titlNameTv.setText("注册");
        registerTv.setVisibility(View.GONE);
        retrunTv.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);
        file = new File(IMAGE_FILE_LOCATION);
        if(!file.exists()){

            SDCardUtils.makeRootDirectory(IMAGE_FILE_LOCATION);
        }

        file=new File(IMAGE_FILE_LOCATION+ConstantSet.USERTEMPPIC);
        imageUri = Uri.fromFile(file);//The Uri t

        fileVeson = new File(IMAGE_FILE_LOCATION);
        if(!fileVeson.exists()){

            SDCardUtils.makeRootDirectory(IMAGE_FILE_LOCATION);
        }

        fileVeson=new File(IMAGE_FILE_LOCATION+ConstantSet.USERTEMPPIC);
        vesonUri = Uri.fromFile(fileVeson);//The Uri t
        initViewPager();







    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;


        }




    }

    //个人注册地址联动
    private void intiPvTime() {
        final ArrayList<String> options1Items = new ArrayList<String>();
        final ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
        //final ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();

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
        pvOptions = new OptionsPickerView(RegisterActivity.this);
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
        pvOptions.setPicker(options1Items, options2Items,true);
        pvOptions.setTitle("选择城市");
        pvOptions.setCyclic(false, false, true);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(1, 1, 1);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1)
                        + options2Items.get(options1).get(option2);
                        /*+ options3Items.get(options1).get(option2).get(options3);*/
                province=options1Items.get(options1);
                city=options2Items.get(options1).get(option2);
                addressTextView.setText(tx);
                vMasker.setVisibility(View.GONE);
            }
        });
        //点击弹出选项选择器
        addressTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pvOptions.show();
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }
    //企业注册地址联动
    private void intiPvCompanyTime() {
        final ArrayList<String> options1Items = new ArrayList<String>();
        final ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
        //final ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();


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
        pvOptionsCompany = new OptionsPickerView(RegisterActivity.this);

        /*
        //选项1
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
        options2Items.add(options2Items_03);

        //选项3
        ArrayList<ArrayList<String>> options3Items_01 = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> options3Items_02 = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> options3Items_03 = new ArrayList<ArrayList<String>>();
        ArrayList<String> options3Items_01_01=new ArrayList<String>();
        options3Items_01_01.add("白云");
        options3Items_01_01.add("天河");
        options3Items_01_01.add("海珠");
        options3Items_01_01.add("越秀");
        options3Items_01.add(options3Items_01_01);
        ArrayList<String> options3Items_01_02=new ArrayList<String>();
        options3Items_01_02.add("南海");
        options3Items_01_02.add("高明");
        options3Items_01_02.add("顺德");
        options3Items_01_02.add("禅城");
        options3Items_01.add(options3Items_01_02);
        ArrayList<String> options3Items_01_03=new ArrayList<String>();
        options3Items_01_03.add("其他");
        options3Items_01_03.add("常平");
        options3Items_01_03.add("虎门");
        options3Items_01.add(options3Items_01_03);
        ArrayList<String> options3Items_01_04=new ArrayList<String>();
        options3Items_01_04.add("其他1");
        options3Items_01_04.add("其他2");
        options3Items_01_04.add("其他3");
        options3Items_01.add(options3Items_01_04);
        ArrayList<String> options3Items_01_05=new ArrayList<String>();
        options3Items_01_05.add("其他1");
        options3Items_01_05.add("其他2");
        options3Items_01_05.add("其他3");
        options3Items_01.add(options3Items_01_05);

        ArrayList<String> options3Items_02_01=new ArrayList<String>();
        options3Items_02_01.add("长沙长沙长沙长沙长沙长沙长沙长沙长沙1111111111");
        options3Items_02_01.add("长沙2");
        options3Items_02_01.add("长沙3");
        options3Items_02_01.add("长沙4");
        options3Items_02_01.add("长沙5");
        options3Items_02_01.add("长沙6");
        options3Items_02_01.add("长沙7");
        options3Items_02_01.add("长沙8");
        options3Items_02.add(options3Items_02_01);
        ArrayList<String> options3Items_02_02=new ArrayList<String>();
        options3Items_02_02.add("岳1");
        options3Items_02_02.add("岳2");
        options3Items_02_02.add("岳3");
        options3Items_02_02.add("岳4");
        options3Items_02_02.add("岳5");
        options3Items_02_02.add("岳6");
        options3Items_02_02.add("岳7");
        options3Items_02_02.add("岳8");
        options3Items_02_02.add("岳9");
        options3Items_02.add(options3Items_02_02);
        ArrayList<String> options3Items_03_01=new ArrayList<String>();
        options3Items_03_01.add("好山水");
        options3Items_03.add(options3Items_03_01);

        options3Items.add(options3Items_01);
        options3Items.add(options3Items_02);
        options3Items.add(options3Items_03);

        //三级联动效果
        pvOptionsCompany.setPicker(options1Items, options2Items, options3Items, true);*/
        //设置选择的三级单位
        pvOptionsCompany.setPicker(options1Items, options2Items,true);
//        pwOptions.setLabels("省", "市", "区");
        pvOptionsCompany.setTitle("选择城市");
        pvOptionsCompany.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptionsCompany.setSelectOptions(1, 1, 1);
        pvOptionsCompany.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1)
                        + options2Items.get(options1).get(option2);
             companyProvince=options1Items.get(options1);
             companyCity= options2Items.get(options1).get(option2);
                companyAddressTextView.setText(tx);
                vMaskerCompany.setVisibility(View.GONE);
            }
        });
        //点击弹出选项选择器
        companyAddressTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pvOptionsCompany.show();
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    private void intiVcodeData(final int tage, String phoneNumber) {

        if (!TextUtils.isEmpty(phoneNumber)) {
            if (phoneNumber.length()==11){
            HttpUtils httpUtils=new HttpUtils();
            RequestParams requestParams=new RequestParams();
            requestParams.addBodyParameter("PhoneNumber",phoneNumber);
            requestParams.addBodyParameter("SmsType","0");
           httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getCodeData(), requestParams,new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    AppDataBean appDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});

                    if ((appDataBean.getResult()).equals("success")){
                        MyAppliction.showToast("验证码已发送成功");
                        if (tage==1){
                            time.start();
                            codeButton.setTextColor(getResources().getColor(R.color.tailt_dark));
                            codeButton.setBackgroundResource(R.drawable.gray_button_corners);
                        }else {
                            companyCodeButton.setTextColor(getResources().getColor(R.color.tailt_dark));
                            companyCodeButton.setBackgroundResource(R.drawable.gray_button_corners);
                            companyTime.start();

                        }
                    }else {
                        MyAppliction.showToast(appDataBean.getMsg());
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.e("onFailure", s);
                    MyAppliction.showToast("网络异常,请稍后重试");
                }
            });

            }else {
                MyAppliction.showToast("请输入长度为11位的手机号码");


            }
        }else {
            MyAppliction.showToast("您输入的手机号码不能为空");


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

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔

        }

        @Override
        public void onFinish() {//计时完毕时触发
            codeButton.setText("重新发送");
            codeButton.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            codeButton.setClickable(false);
            codeButton.setText("请稍后"+millisUntilFinished / 1000 + "秒");
        }

    }
    //企业注册计时器
    class TimeCompanyCount extends CountDownTimer {
        public TimeCompanyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            companyCodeButton.setText("重新发送");
            companyCodeButton.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            companyCodeButton.setClickable(false);
            companyCodeButton.setText("请稍后"+millisUntilFinished / 1000 + "秒");
        }

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




    private void initViewPager() {
        personageRisterView=getLayoutInflater().inflate(R.layout.fragment_personage_risterg, null);
        companyRisterView=getLayoutInflater().inflate(R.layout.fragment_company_risterg, null);
        lists.add(personageRisterView);
        lists.add(companyRisterView);

        initeCursor();
        myAdapter = new MyAdapter(lists);
        viewPager.setAdapter(myAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) { // 当滑动式，顶部的imageView是通过animation缓慢的滑动
                // TODO Auto-generated method stub
                switch (arg0) {
                    case 0:
                        if (currentItem == 1) {
                            animation = new TranslateAnimation(
                                    offSet * 2 + bmWidth, 0, 0, 0);
                        } else if (currentItem == 2) {
                            animation = new TranslateAnimation(offSet * 4 + 2
                                    * bmWidth, 0, 0, 0);
                        }
                        break;
                    case 1:
                        if (currentItem == 0) {
                            animation = new TranslateAnimation(0,offSet * 2
                                    + bmWidth, 0, 0);
                        } else if (currentItem == 2) {
                            //TODO
                            animation = new TranslateAnimation(2 * offSet + 2
                                    * bmWidth, offSet * 2 + bmWidth, 0, 0);
                        }
                        break;



                }
                currentItem = arg0;

                animation.setDuration(500);
                animation.setFillAfter(true);
                imageView.startAnimation(animation);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        textView1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                viewPager.setCurrentItem(0);
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                viewPager.setCurrentItem(1);
            }
        });




    }

    private void initeCursor() {
        cursor = BitmapFactory
                .decodeResource(getResources(), R.drawable.cursor);
        bmWidth = cursor.getWidth();

        DisplayMetrics dm;
        dm = getResources().getDisplayMetrics();

        offSet = (dm.widthPixels - 2 * bmWidth) / 4;
        matrix.setTranslate(offSet, 0);
        imageView.setImageMatrix(matrix); // 需要iamgeView的scaleType为matrix
        currentItem = 0;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
// KeyEvent.KEYCODE_BACK代表返回操作.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitGameAlertUnLonding("确定要退出注册？");
            // 处理返回操作.
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);

        }
        return true;
    }

    //对话框
    private void showExitGameAlertUnLonding(String text) {
        final AlertDialog dlg = new AlertDialog.Builder(RegisterActivity.this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.shrew_exit_dialog);
        TextView tailte = (TextView) window.findViewById(R.id.tailte_tv);
        tailte.setText(text);
        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        ok.setText("确定");
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               finish();
                dlg.cancel();
            }
        });

        // 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText("取消");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyAppliction.setIdCardFrontId(null);
        MyAppliction.setCertificateId(null);
        MyAppliction.setCompanyFrontId(null);
        MyAppliction.setCompanyPersongeId(null);
        MyAppliction.setCompanyTradingId(null);
    }
}
