package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.ModifatyHeadImage;
import com.example.zhongjiyun03.zhongjiyun.bean.main.PersonageInformationBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.activity.ClippingPageActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.activity.SelectImagesFromLocalActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.constants.ConstantSet;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.utils.SDCardUtils;
import com.example.zhongjiyun03.zhongjiyun.uilts.showPicture.ImagePagerActivity;
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
import java.util.Calendar;
import java.util.Locale;

public class PersonageInformationActivity extends AppCompatActivity implements View.OnClickListener {



    @ViewInject(R.id.register_tv)
    private TextView registerTv;  //头部右边
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边
    @ViewInject(R.id.title_name_tv)
    private TextView tailtText;

    @ViewInject(R.id.image_view)
    private ImageView imageView;   //头像设置
    @ViewInject(R.id.name_edit)
    private TextView nameEdit;  //名字
    @ViewInject(R.id.phonet_edit)
    private TextView phoneEdit;  //手机号码
    @ViewInject(R.id.id_card_edit)
    private TextView idCardEdit;    //身份证号码
    @ViewInject(R.id.area_text)
    private TextView areaText;     //承接区域
    @ViewInject(R.id.type_text)
    private TextView typeText;     //类型
    @ViewInject(R.id.idcard_front_iamge)
    private ImageView idcardFrontIamge;  //身份证正面
    @ViewInject(R.id.idcard_verso_iamge)
    private ImageView idcardVersoIamge;  //身份证反面
    @ViewInject(R.id.personage_image)
    private ImageView personageImage;      //个人照片
    @ViewInject(R.id.image_rlayout)
    private RelativeLayout imageRlayout;   //头像布局
    private static final String IMAGE_FILE_LOCATION = ConstantSet.LOCALFILE;//temp file
    private String imagePath;   //头像路径
    private File file ;   //拍照文件
    private Uri imageUri ;   //拍照uri
    private SVProgressHUD mSVProgressHUD;//loding
    @ViewInject(R.id.layout)
    private LinearLayout layout;
    private String uids;  //用户id
    private PersonageInformationBean personageInformation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perdonal_daata);
        ViewUtils.inject(this);
        inti();

    }

    private void inti() {
        initView();
        initInformationData();

    }

    @Override
    protected void onResume() {
        super.onResume();



    }

    private void initInformationData() {
        SQLhelper sqLhelper=new SQLhelper(PersonageInformationActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        if (!TextUtils.isEmpty(uid)){
            HttpUtils httpUtils=new HttpUtils();
            RequestParams requestParams=new RequestParams();
            requestParams.addBodyParameter("ID",uid);
            //步骤1：创建一个SharedPreferences接口对象
            SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
            //步骤2：获取文件中的值
            String sesstionId = read.getString("code","");
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
            //Log.e("uid",uids+"");
            mSVProgressHUD.showWithStatus("正在加载中...");
            layout.setVisibility(View.GONE);
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getUserInformationData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        AppBean<PersonageInformationBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<PersonageInformationBean>>(){});
                        if (appBean.getResult().equals("success")){
                            layout.setVisibility(View.VISIBLE);
                            personageInformation=  appBean.getData();
                            if (personageInformation!=null){
                                if (!TextUtils.isEmpty(personageInformation.getHeadthumb())){
                                    MyAppliction.imageLoader.displayImage(personageInformation.getHeadthumb(),imageView,MyAppliction.options);
                                }
                                if (!TextUtils.isEmpty(personageInformation.getName())){
                                    nameEdit.setText(personageInformation.getName());
                                }
                                if (!TextUtils.isEmpty(personageInformation.getPhoneNumber())){

                                    phoneEdit.setText(personageInformation.getPhoneNumber());
                                }
                                if (!TextUtils.isEmpty(personageInformation.getIdCard())){
                                    idCardEdit.setText(personageInformation.getIdCard());
                                }
                                if (!TextUtils.isEmpty(personageInformation.getCity())&&!TextUtils.isEmpty(personageInformation.getProvince())){
                                    areaText.setText(personageInformation.getProvince()+personageInformation.getCity());

                                }
                                if (personageInformation.getBossType()==1){

                                    typeText.setText("个人");

                                }else if (personageInformation.getBossType()==2){
                                    typeText.setText("企业");

                                }
                                if (!TextUtils.isEmpty(personageInformation.getIdCardImage1())){
                                    MyAppliction.imageLoader.displayImage(personageInformation.getIdCardImage1(),idcardFrontIamge,MyAppliction.options);
                                }
                                if (!TextUtils.isEmpty(personageInformation.getIdCardImage2())){
                                    MyAppliction.imageLoader.displayImage(personageInformation.getIdCardImage2(),idcardVersoIamge,MyAppliction.options);
                                }
                                if (!TextUtils.isEmpty(personageInformation.getPhoto())){
                                    MyAppliction.imageLoader.displayImage(personageInformation.getPhoto(),personageImage,MyAppliction.options);
                                }
                                mSVProgressHUD.dismiss();

                            }else {
                                mSVProgressHUD.dismiss();
                            }

                        }else {
                            MyAppliction.showToast(appBean.getMsg());
                            mSVProgressHUD.dismiss();

                        }




                    }



                }

                @Override
                public void onFailure(HttpException e, String s) {
                    mSVProgressHUD.dismiss();
                    MyAppliction.showToast("网络异常，请稍后重试！");
                }
            });


        }else {
            MyAppliction.showToast("加载失败...");
        }




    }

    private void initView() {
        retrunText.setOnClickListener(this);
        registerTv.setText("完成");
        registerTv.setOnClickListener(this);
        tailtText.setText("个人资料");
        areaText.setOnClickListener(this);
        imageRlayout.setOnClickListener(this);
        file = new File(IMAGE_FILE_LOCATION);
        if(!file.exists()){

            SDCardUtils.makeRootDirectory(IMAGE_FILE_LOCATION);
        }
        idcardFrontIamge.setOnClickListener(this);
        idcardVersoIamge.setOnClickListener(this);
        personageImage.setOnClickListener(this);
        file=new File(IMAGE_FILE_LOCATION+ConstantSet.USERTEMPPIC);
        imageUri = Uri.fromFile(file);//The Uri t
        mSVProgressHUD = new SVProgressHUD(this);



    }





    @Override
    public void onClick(View v) {
        SQLhelper sqLhelper=new SQLhelper(PersonageInformationActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        switch (v.getId()){
            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;
            case R.id.image_rlayout:
                showDialog(ConstantSet.TAKEPICTURE,ConstantSet.SELECTPICTURE,imageUri);

                break;
            case R.id.register_tv:
                if (!TextUtils.isEmpty(uid)){
                    if (!TextUtils.isEmpty(imagePath)){
                        updateImage(uid,"16",imagePath);
                        Log.e("imagePath",imagePath);
                    }else {
                        MyAppliction.showToast("请选择照片");
                    }
                }else {
                    MyAppliction.showToast("上传失败");
                }

                break;
            case R.id.idcard_front_iamge:
                imageBrower(0,personageInformation);
                break;
            case R.id.idcard_verso_iamge:
                imageBrower(1,personageInformation);
                break;
            case R.id.personage_image:
                imageBrower(2,personageInformation);
                break;





        }


    }

    private void imageBrower(int position,PersonageInformationBean urls) {
        Intent intent = new Intent(PersonageInformationActivity.this, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putExtra("tage","personageInformation");
        startActivity(intent);
    }

    private void updateImage(final String id, String userType, String imagePath) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requwstParams=new RequestParams();
        requwstParams.addBodyParameter("Id",id);
        requwstParams.addBodyParameter("ImageType",userType);
        requwstParams.addBodyParameter("UserType","boss");
        requwstParams.addBodyParameter("SourceType","5");
        requwstParams.addBodyParameter("File", new File(imagePath));
        mSVProgressHUD.showWithStatus("正在提交中...");
        httpUtils.send(HttpRequest.HttpMethod.POST,AppUtilsUrl.getPhoneData() ,requwstParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (!TextUtils.isEmpty(responseInfo.result)){
                    Log.e("修改头像",responseInfo.result);
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        AppBean<ModifatyHeadImage> appBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<ModifatyHeadImage>>(){});
                        if (appBean.getResult().equals("success")){
                            ModifatyHeadImage modifatyHeadImage=appBean.getData();
                            if (!TextUtils.isEmpty(modifatyHeadImage.getId())&&!TextUtils.isEmpty(modifatyHeadImage.getURL())){
                                update(modifatyHeadImage.getId(),modifatyHeadImage.getURL());
                            }
                            mSVProgressHUD.dismiss();
                            mSVProgressHUD.showSuccessWithStatus("修改资料成功");
                            finish();
                        }else {
                            mSVProgressHUD.showErrorWithStatus(appBean.getMsg(), SVProgressHUD.SVProgressHUDMaskType.GradientCancel);


                        }




                    }

                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                mSVProgressHUD.dismiss();
            }
        });





    }
    /**
     * 更新头像
     */
    public void update(String uid,String userIcon){
        SQLhelper sqLhelper= new SQLhelper(PersonageInformationActivity.this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLhelper.HEADTHUMB, userIcon);
        db.update(SQLhelper.tableName, contentValues,
                "uid=?", new String[]{uid});
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {

            return;

        }
        switch (requestCode) {

            case ConstantSet.TAKEPICTURE:
                Intent tcutIntent = new Intent(PersonageInformationActivity.this, ClippingPageActivity.class);
                tcutIntent.putExtra("type", "takePicture");
                startActivityForResult(tcutIntent, ConstantSet.CROPPICTURE);
                break;

            case ConstantSet.SELECTPICTURE:
                Intent scutIntent = new Intent(PersonageInformationActivity.this, ClippingPageActivity.class);
                scutIntent.putExtra("type", "selectPicture");
                scutIntent.putExtra("path", data.getStringExtra("path"));
                startActivityForResult(scutIntent, ConstantSet.CROPPICTURE);
                break;


            case ConstantSet.CROPPICTURE:
                byte[] bis = data.getByteArrayExtra("result");
                Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }

                String frontName = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File frontFile = getFile(bis, "/sdcard/zhongJiYunImage/", frontName);
                if (!TextUtils.isEmpty(frontFile.getPath())) {
                    imagePath = frontFile.getPath();
                }

                break;
        }
    }

    //拍照和相册弹出框
    private void showDialog(final int captureIndext, final int pickIndext, final Uri uri) {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        final Dialog dialog = new Dialog(PersonageInformationActivity.this, R.style.transparentFrameWindowStyle);
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
                Intent sintent = new Intent(PersonageInformationActivity.this, SelectImagesFromLocalActivity.class);
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
