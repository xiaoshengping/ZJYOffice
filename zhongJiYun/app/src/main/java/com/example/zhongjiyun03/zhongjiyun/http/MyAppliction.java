package com.example.zhongjiyun03.zhongjiyun.http;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.baidumap.LocationService;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by xiaoshengping on 2015/9/8.
 */
public class MyAppliction extends Application {
    public static ImageLoader imageLoader = ImageLoader.getInstance();
    public static DisplayImageOptions options;
    public static DisplayImageOptions RoundedOptions;
    public static DisplayImageOptions RoundedOptionsOne;
    private static MyAppliction app = null;
    private static String homeRequestTage;  //首页二手钻机请求标志
    private static String projectRequestTage;//项目刷新请求标志
    private static MyAppliction instance;
    //百度地图
    public LocationService locationService;
    public Vibrator mVibrator;
    private static String latitude;//纬度
    private static String longitude; //经度
    private static boolean isProjectMessage;//我的竞标消息提醒
    private static int   messageSize; //消息长度大小
    private static boolean isCheck; //是否接受推送消息
    private static String cacheData;
    private static boolean jiGuangIsCheck;
    private static String projectRefresh;//项目详情刷新
    private static String phone;  //用户手机号码
    private static String IdCardFrontId;  //身份证正面照片id
    private static String certificateId;  //个人照片id
    private static String companyFrontId;  //企业身份证正面照片id
    private static String companyPersongeId;  //企业个人照片id
    private static String companyTradingId;  //企业营业执照或者资格证书照片id
    private static String leaveFactoryId;  //添加钻机出厂牌id
    private static String panoramaId;  //添加钻机设备全景id
    private static String invoiceId;  //添加钻机设备发票id
    private static String contractId;  //添加钻机设备发票id
    private static String qualifiedId;  //添加钻机设备合格证id




    public static MyAppliction getInstance() {
        if (instance == null) {
            instance = new MyAppliction();
        }
        return instance;
    }



    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        CrashHandler.getInstance().init(getApplicationContext()); //异常处理
        JPushInterface.init(getApplicationContext());
        JPushInterface.setDebugMode(true);
        app=this;
        initImageLoader(getApplicationContext());
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_img)//加载等待时显示的图片
                .showImageForEmptyUri(R.mipmap.default_img)//加载数据为空时显示的图片
                .showImageOnFail(R.mipmap.default_img)//加载失败时显示的图片
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        RoundedOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.default_img)//加载等待 时显示的图片
                .showImageForEmptyUri(R.mipmap.default_img)//加载数据为空时显示的图片
                .showImageOnFail(R.mipmap.default_img)//加载失败时显示的图片
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .displayer(new RoundedBitmapDisplayer(200))
                .build();
        RoundedOptionsOne = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.default_img)//加载等待 时显示的图片
                .showImageForEmptyUri(R.mipmap.default_img)//加载数据为空时显示的图片
                .showImageOnFail(R.mipmap.default_img)//加载失败时显示的图片
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
       // SDKInitializer.initialize(getApplicationContext());

    }


    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        //.enableLogging() // Not necessary in common

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
        //imageLoader.init(ImageLoaderConfiguration.createDefault(context));

    }
    /**
     * 拿到上下文对象
     *
     *
     */
    public static MyAppliction getContextInstance(){
        return app;
    }
    /**
     * 土司
     */
    public static void showToast(String msg){
        Toast.makeText(app, msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean isJiGuangIsCheck() {
        return jiGuangIsCheck;
    }

    public static void setJiGuangIsCheck(boolean jiGuangIsCheck) {
        MyAppliction.jiGuangIsCheck = jiGuangIsCheck;
    }


    public static String getQualifiedId() {
        return qualifiedId;
    }

    public static void setQualifiedId(String qualifiedId) {
        MyAppliction.qualifiedId = qualifiedId;
    }

    public static String getContractId() {
        return contractId;
    }

    public static void setContractId(String contractId) {
        MyAppliction.contractId = contractId;
    }

    public static String getInvoiceId() {
        return invoiceId;
    }

    public static void setInvoiceId(String invoiceId) {
        MyAppliction.invoiceId = invoiceId;
    }

    public static String getPanoramaId() {
        return panoramaId;
    }

    public static void setPanoramaId(String panoramaId) {
        MyAppliction.panoramaId = panoramaId;
    }

    public static String getLeaveFactoryId() {
        return leaveFactoryId;
    }

    public static void setLeaveFactoryId(String leaveFactoryId) {
        MyAppliction.leaveFactoryId = leaveFactoryId;
    }

    public static String getCertificateId() {
        return certificateId;
    }


    public static String getCompanyTradingId() {
        return companyTradingId;
    }

    public static void setCompanyTradingId(String companyTradingId) {
        MyAppliction.companyTradingId = companyTradingId;
    }

    public static String getCompanyPersongeId() {
        return companyPersongeId;
    }

    public static void setCompanyPersongeId(String companyPersongeId) {
        MyAppliction.companyPersongeId = companyPersongeId;
    }

    public static String getCompanyFrontId() {
        return companyFrontId;
    }

    public static void setCompanyFrontId(String companyFrontId) {
        MyAppliction.companyFrontId = companyFrontId;
    }

    public static void setCertificateId(String certificateId) {
        MyAppliction.certificateId = certificateId;
    }

    public static String getIdCardFrontId() {
        return IdCardFrontId;
    }

    public static void setIdCardFrontId(String idCardFrontId) {
        IdCardFrontId = idCardFrontId;
    }

    public static String getCacheData() {
        return cacheData;
    }

    public static void setCacheData(String cacheData) {
        MyAppliction.cacheData = cacheData;
    }

    public static boolean isCheck() {
        return isCheck;
    }

    public static void setIsCheck(boolean isCheck) {
        MyAppliction.isCheck = isCheck;
    }

    public static int getMessageSize() {
        return messageSize;
    }

    public static void setMessageSize(int messageSize) {
        MyAppliction.messageSize = messageSize;
    }

    public static boolean isProjectMessage() {
        return isProjectMessage;
    }

    public static void setIsProjectMessage(boolean isProjectMessage) {
        MyAppliction.isProjectMessage = isProjectMessage;
    }

    public static String getLatitude() {
        return latitude;
    }

    public static void setLatitude(String latitude) {
        MyAppliction.latitude = latitude;
    }

    public static String getLongitude() {
        return longitude;
    }

    public static void setLongitude(String longitude) {
        MyAppliction.longitude = longitude;
    }

    public static String getHomeRequestTage() {
        return homeRequestTage;
    }

    public static void setHomeRequestTage(String homeRequestTage) {
        MyAppliction.homeRequestTage = homeRequestTage;
    }

    public static String getProjectRequestTage() {
        return projectRequestTage;
    }

    public static void setProjectRequestTage(String projectRequestTage) {
        MyAppliction.projectRequestTage = projectRequestTage;
    }

    public static String getProjectRefresh() {
        return projectRefresh;
    }

    public static void setProjectRefresh(String projectRefresh) {
        MyAppliction.projectRefresh = projectRefresh;
    }


    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        MyAppliction.phone = phone;
    }

    //对话框
    public static void showExitGameAlert(String text,Context app) {
        final AlertDialog dlg = new AlertDialog.Builder(app).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.tishi_exit_dialog);
        TextView tailte = (TextView) window.findViewById(R.id.tailte_tv);
        tailte.setText(text);
        // 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText("确定");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }



}
