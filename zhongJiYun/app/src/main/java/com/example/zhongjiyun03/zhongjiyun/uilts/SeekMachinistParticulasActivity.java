package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.SeekMachinistParticulasBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.view.CircleImageView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import cn.jpush.android.api.JPushInterface;

public class SeekMachinistParticulasActivity extends AppCompatActivity implements View.OnClickListener {


    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边


    /*
   * 向右滑动返回前一个页面
   *
   * */
    //手指上下滑动时的最小速度
    private static final int YSPEED_MIN = 1000;

    //手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 50;

    //手指向上滑或下滑时的最小距离
    private static final int YDISTANCE_MIN = 100;

    //记录手指按下时的横坐标。
    private float xDown;

    //记录手指按下时的纵坐标。
    private float yDown;

    //记录手指移动时的横坐标。
    private float xMove;

    //记录手指移动时的纵坐标。
    private float yMove;

    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;


    @ViewInject(R.id.usericon_background_iv)
    private CircleImageView circleImageView;
    @ViewInject(R.id.name_text)
    private TextView nameText;
    @ViewInject(R.id.work_experience_text)
    private TextView workExperienceText;
    @ViewInject(R.id.upter_date_text)
    private TextView unpterDataText;
     @ViewInject(R.id.phone_tell)
     private TextView phoneTell;
    @ViewInject(R.id.address_text)
    private TextView addressText;
    @ViewInject(R.id.pay_text)
    private TextView payText;
    private Handler handler;
    private Bitmap bitmaps;
    @ViewInject(R.id.bar_image)
    private ImageView bsrImage;
    @ViewInject(R.id.work_time_text)
    private TextView workTimeText;
    @ViewInject(R.id.pety_text)
    private TextView petyText;
    @ViewInject(R.id.miaoshi_text)
    private TextView miaoShiText;
    @ViewInject(R.id.work_time_text_one)
    private TextView workTimeTextOne;
    @ViewInject(R.id.pety_text_one)
    private TextView petyTextOne;
    @ViewInject(R.id.miaoshi_text_one)
    private TextView miaoshiTextOne;
    @ViewInject(R.id.work_experience_rlayout)
    private RelativeLayout workExperienceRlayout;
    @ViewInject(R.id.work_experience_layout_one)
    private RelativeLayout workExperienceLayoutOne;
    @ViewInject(R.id.message_rlauout)
    private RelativeLayout messageRlauout;
    @ViewInject(R.id.image_lini)
    private ImageView imageLini;
    @ViewInject(R.id.work_time_text_two)
    private TextView workTimeTextTwo;
    @ViewInject(R.id.pety_text_two)
    private TextView petyTextTwo;
    @ViewInject(R.id.miaoshi_text_two)
    private TextView miaoshiTextTwo;
    @ViewInject(R.id.work_experience_layout_two)
    private RelativeLayout workExperienceLayoutTwo;
    @ViewInject(R.id.image_lini_one)
    private ImageView imageLiniOne;
    private SeekMachinistParticulasBean seekMachinisDataBean;
    @ViewInject(R.id.age_text)
    private TextView ageText;  //年龄

    private SVProgressHUD mSVProgressHUD;//loding
    @ViewInject(R.id.message_layout)
    private ScrollView messageLayout;
    @ViewInject(R.id.not_data_image)
    private ImageView notDataImage;  //没有数据提示图片
    @ViewInject(R.id.refresh_button)
    private Button refreshButton;  //刷新按钮
    @ViewInject(R.id.not_data_layout)
    private LinearLayout notDataLayout; //没有数据布局

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
        setContentView(R.layout.activity_seek_machinist_particulas);
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
        if (!TextUtils.isEmpty(getIntent().getStringExtra("seekData").toString())){
            initSeekData();//数据
        }else {
            notDataLayout.setVisibility(View.VISIBLE);
            notDataImage.setBackgroundResource(R.mipmap.no_wifi_icon);
        }


    }

    private void initSeekData() {
         //mSVProgressHUD.showWithStatus("正在加载中...");
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("driverId",getIntent().getStringExtra("seekData").toString());
        messageLayout.setVisibility(View.GONE);
        mSVProgressHUD.showWithStatus("正在加载中...");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getSeekMachinisListParticualsData(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("机手详情",responseInfo.result);
                 if (!TextUtils.isEmpty(responseInfo.result)){
                     AppBean<SeekMachinistParticulasBean> appBean = JSONObject.parseObject(responseInfo.result, new TypeReference<AppBean<SeekMachinistParticulasBean>>() {
                     });
                     if (appBean!=null){
                         if (appBean.getResult().equals("success")){
                             seekMachinisDataBean=  appBean.getData();
                             if (seekMachinisDataBean!=null){
                                 MyAppliction.imageLoader.displayImage(seekMachinisDataBean.getDriverHeader(),circleImageView,MyAppliction.options);
                                 nameText.setText(seekMachinisDataBean.getDriverName());
                                 workExperienceText.setText(seekMachinisDataBean.getWorkingAge()+"年工作经验");
                                 unpterDataText.setText("更新时间"+seekMachinisDataBean.getLastUpdateTimeStr());
                                 addressText.setText("期望地:"+seekMachinisDataBean.getAddress());
                                 payText.setText("期望月薪:"+seekMachinisDataBean.getWage()+"元");
                                 if (!TextUtils.isEmpty(seekMachinisDataBean.getAge())){
                                     ageText.setText(seekMachinisDataBean.getAge()+"岁");
                                 }
                                 if (seekMachinisDataBean.getWorkExperiences()!=null&&seekMachinisDataBean.getWorkExperiences().size()!=0){
                                     if (seekMachinisDataBean.getWorkExperiences().size()==1){
                                         workExperienceRlayout.setVisibility(View.VISIBLE);

                                         if(!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getBeginYear())&&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getBeginMonth())
                                                 &&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getEndYear())&&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getEndMonth())){
                                             workTimeText.setText("工作时间:"+seekMachinisDataBean.getWorkExperiences().get(0).getBeginYear()+"年"+
                                                     seekMachinisDataBean.getWorkExperiences().get(0).getBeginMonth()+"月-"+
                                                     seekMachinisDataBean.getWorkExperiences().get(0).getEndYear()+"年"+
                                                     seekMachinisDataBean.getWorkExperiences().get(0).getEndMonth()+"月");
                                         }else {
                                             workTimeText.setText("工作时间:暂无");
                                         }

                                         if (!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getManufacture())&&
                                                 !TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getNoOfManufacture())){
                                             petyText.setText("钻机机型:"+seekMachinisDataBean.getWorkExperiences().get(0).getManufacture()+
                                                     seekMachinisDataBean.getWorkExperiences().get(0).getNoOfManufacture());
                                         }else {
                                             petyText.setText("钻机机型:暂无");

                                         }
                                         if (!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getDescribing())) {
                                             miaoShiText.setVisibility(View.VISIBLE);
                                             miaoShiText.setText("工作描述:" + seekMachinisDataBean.getWorkExperiences().get(0).getDescribing());
                                         }else {
                                             miaoShiText.setText("工作描述:暂无");
                                         }
                                     }else if (seekMachinisDataBean.getWorkExperiences().size()==2){

                                         workExperienceRlayout.setVisibility(View.VISIBLE);
                                         if(!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getBeginYear())&&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getBeginMonth())
                                                 &&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getEndYear())&&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getEndMonth())){
                                             workTimeText.setText("工作时间:"+seekMachinisDataBean.getWorkExperiences().get(0).getBeginYear()+"年"+
                                                     seekMachinisDataBean.getWorkExperiences().get(0).getBeginMonth()+"月-"+
                                                     seekMachinisDataBean.getWorkExperiences().get(0).getEndYear()+"年"+
                                                     seekMachinisDataBean.getWorkExperiences().get(0).getEndMonth()+"月");
                                         }else {
                                             workTimeText.setText("工作时间:暂无");
                                         }

                                         if (!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getManufacture())&&
                                                 !TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getNoOfManufacture())){
                                             petyText.setText("钻机机型:"+seekMachinisDataBean.getWorkExperiences().get(0).getManufacture()+
                                                     seekMachinisDataBean.getWorkExperiences().get(0).getNoOfManufacture());
                                         }else {
                                             petyText.setText("钻机机型:暂无");
                                         }
                                         if (!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getDescribing())) {
                                             miaoShiText.setVisibility(View.VISIBLE);
                                             miaoShiText.setText("工作描述:" + seekMachinisDataBean.getWorkExperiences().get(0).getDescribing());
                                         }else {
                                             miaoShiText.setText("工作描述:暂无");
                                         }
                                         workExperienceLayoutOne.setVisibility(View.VISIBLE);
                                         imageLini.setVisibility(View.VISIBLE);
                                         if(!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getBeginYear())&&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getBeginMonth())
                                                 &&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getEndYear())&&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getEndMonth())){
                                             workTimeTextOne.setText("工作时间:"+seekMachinisDataBean.getWorkExperiences().get(1).getBeginYear()+"年"+
                                                     seekMachinisDataBean.getWorkExperiences().get(1).getBeginMonth()+"月-"+
                                                     seekMachinisDataBean.getWorkExperiences().get(1).getEndYear()+"年"+
                                                     seekMachinisDataBean.getWorkExperiences().get(1).getEndMonth()+"月");

                                         }else {
                                             workTimeTextOne.setText("工作时间:暂无");

                                         }

                                         if (!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getManufacture())&&
                                                 !TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getNoOfManufacture())){
                                             petyTextOne.setText("钻机机型:"+seekMachinisDataBean.getWorkExperiences().get(1).getManufacture()+
                                                     seekMachinisDataBean.getWorkExperiences().get(1).getNoOfManufacture());
                                         }else {
                                             petyTextOne.setText("钻机机型:暂无");
                                         }
                                         if (!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getDescribing())) {
                                             miaoshiTextOne.setVisibility(View.VISIBLE);
                                             miaoshiTextOne.setText("工作描述:" + seekMachinisDataBean.getWorkExperiences().get(1).getDescribing());
                                         }else {
                                             miaoshiTextOne.setText("工作描述:暂无");
                                         }
                                     }else if (seekMachinisDataBean.getWorkExperiences().size()==3){
                                         workExperienceRlayout.setVisibility(View.VISIBLE);
                                         if(!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getBeginYear())&&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getBeginMonth())
                                                 &&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getEndYear())&&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getEndMonth())){
                                             workTimeText.setText("工作时间:"+seekMachinisDataBean.getWorkExperiences().get(0).getBeginYear()+"年"+
                                                     seekMachinisDataBean.getWorkExperiences().get(0).getBeginMonth()+"月-"+
                                                     seekMachinisDataBean.getWorkExperiences().get(0).getEndYear()+"年"+
                                                     seekMachinisDataBean.getWorkExperiences().get(0).getEndMonth()+"月");
                                         }else {
                                             workTimeText.setText("工作时间:暂无");
                                         }

                                         if (!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getManufacture())&&
                                                 !TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getNoOfManufacture())){
                                             petyText.setText("钻机机型:"+seekMachinisDataBean.getWorkExperiences().get(0).getManufacture()+
                                                     seekMachinisDataBean.getWorkExperiences().get(0).getNoOfManufacture());
                                         }else {
                                             petyText.setText("钻机机型:暂无");
                                         }
                                         if (!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getDescribing())) {
                                             miaoShiText.setVisibility(View.VISIBLE);
                                             miaoShiText.setText("工作描述:" + seekMachinisDataBean.getWorkExperiences().get(0).getDescribing());
                                         }else {
                                             miaoShiText.setText("工作描述:暂无");
                                         }
                                         workExperienceLayoutOne.setVisibility(View.VISIBLE);
                                         imageLini.setVisibility(View.VISIBLE);
                                         if(!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getBeginYear())&&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getBeginMonth())
                                                 &&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getEndYear())&&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getEndMonth())){
                                             workTimeTextOne.setText("工作时间:"+seekMachinisDataBean.getWorkExperiences().get(1).getBeginYear()+"年"+
                                                     seekMachinisDataBean.getWorkExperiences().get(1).getBeginMonth()+"月-"+
                                                     seekMachinisDataBean.getWorkExperiences().get(1).getEndYear()+"年"+
                                                     seekMachinisDataBean.getWorkExperiences().get(1).getEndMonth()+"月");
                                         }else {
                                             workTimeTextOne.setText("工作时间:暂无");
                                         }

                                         if (!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getManufacture())&&
                                                 !TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getNoOfManufacture())){
                                             petyTextOne.setText("钻机机型:"+seekMachinisDataBean.getWorkExperiences().get(1).getManufacture()+
                                                     seekMachinisDataBean.getWorkExperiences().get(1).getNoOfManufacture());
                                         }else {
                                             petyTextOne.setText("钻机机型:暂无");
                                         }
                                         if (!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getDescribing())) {
                                             miaoshiTextOne.setVisibility(View.VISIBLE);
                                             miaoshiTextOne.setText("工作描述:" + seekMachinisDataBean.getWorkExperiences().get(1).getDescribing());
                                         }else {
                                             miaoshiTextOne.setText("工作描述:暂无");
                                         }
                                         workExperienceLayoutTwo.setVisibility(View.VISIBLE);
                                         imageLiniOne.setVisibility(View.VISIBLE);
                                         if(!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(2).getBeginYear())&&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(2).getBeginMonth())
                                                 &&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(2).getEndYear())&&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(2).getEndMonth())){
                                             workTimeTextTwo.setText("工作时间:"+seekMachinisDataBean.getWorkExperiences().get(2).getBeginYear()+"年"+
                                                     seekMachinisDataBean.getWorkExperiences().get(2).getBeginMonth()+"月-"+
                                                     seekMachinisDataBean.getWorkExperiences().get(2).getEndYear()+"年"+
                                                     seekMachinisDataBean.getWorkExperiences().get(2).getEndMonth()+"月");
                                         }else {
                                             workTimeTextTwo.setText("工作时间:暂无");
                                         }

                                         if (!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(2).getManufacture())&&
                                                 !TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(2).getNoOfManufacture())){
                                             petyTextTwo.setText("钻机机型:"+seekMachinisDataBean.getWorkExperiences().get(2).getManufacture()+
                                                     seekMachinisDataBean.getWorkExperiences().get(2).getNoOfManufacture());
                                         }else {
                                             petyTextTwo.setText("钻机机型:暂无");
                                         }
                                         if (!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(2).getDescribing())){
                                             miaoshiTextTwo.setVisibility(View.VISIBLE);
                                             miaoshiTextTwo.setText("工作描述:"+seekMachinisDataBean.getWorkExperiences().get(2).getDescribing());
                                         }else {
                                             miaoshiTextTwo.setText("工作描述:暂无");
                                         }

                                     }else {
                                         workExperienceRlayout.setVisibility(View.VISIBLE);
                                         if(!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getBeginYear())&&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getBeginMonth())
                                                 &&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getEndYear())&&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getEndMonth())){
                                             workTimeText.setText("工作时间:"+seekMachinisDataBean.getWorkExperiences().get(0).getBeginYear()+"年"+
                                                     seekMachinisDataBean.getWorkExperiences().get(0).getBeginMonth()+"月-"+
                                                     seekMachinisDataBean.getWorkExperiences().get(0).getEndYear()+"年"+
                                                     seekMachinisDataBean.getWorkExperiences().get(0).getEndMonth()+"月");
                                         }else {
                                             workTimeText.setText("工作时间:暂无");
                                         }

                                         if (!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getManufacture())&&
                                                 !TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getNoOfManufacture())){
                                             petyText.setText("钻机机型:"+seekMachinisDataBean.getWorkExperiences().get(0).getManufacture()+
                                                     seekMachinisDataBean.getWorkExperiences().get(0).getNoOfManufacture());
                                         }else {
                                             petyText.setText("钻机机型:暂无");
                                         }
                                         if (!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(0).getDescribing())) {
                                             miaoShiText.setVisibility(View.VISIBLE);
                                             miaoShiText.setText("工作描述:" + seekMachinisDataBean.getWorkExperiences().get(0).getDescribing());
                                         }else {
                                             miaoShiText.setText("工作描述:暂无");
                                         }
                                         workExperienceLayoutOne.setVisibility(View.VISIBLE);
                                         imageLini.setVisibility(View.VISIBLE);
                                         if(!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getBeginYear())&&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getBeginMonth())
                                                 &&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getEndYear())&&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getEndMonth())){
                                             workTimeTextOne.setText("工作时间:"+seekMachinisDataBean.getWorkExperiences().get(1).getBeginYear()+"年"+
                                                     seekMachinisDataBean.getWorkExperiences().get(1).getBeginMonth()+"月-"+
                                                     seekMachinisDataBean.getWorkExperiences().get(1).getEndYear()+"年"+
                                                     seekMachinisDataBean.getWorkExperiences().get(1).getEndMonth()+"月");
                                         }else {
                                             workTimeTextOne.setText("工作时间:暂无");
                                         }

                                         if (!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getManufacture())&&
                                                 !TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getNoOfManufacture())){
                                             petyTextOne.setText("钻机机型:"+seekMachinisDataBean.getWorkExperiences().get(1).getManufacture()+
                                                     seekMachinisDataBean.getWorkExperiences().get(1).getNoOfManufacture());
                                         }else {
                                             petyTextOne.setText("钻机机型:暂无");
                                         }
                                         if (!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(1).getDescribing())) {
                                             miaoshiTextOne.setVisibility(View.VISIBLE);
                                             miaoshiTextOne.setText("工作描述:" + seekMachinisDataBean.getWorkExperiences().get(1).getDescribing());
                                         }else {
                                             miaoshiTextOne.setText("工作描述:暂无");
                                         }
                                         workExperienceLayoutTwo.setVisibility(View.VISIBLE);
                                         imageLiniOne.setVisibility(View.VISIBLE);
                                         if(!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(2).getBeginYear())&&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(2).getBeginMonth())
                                                 &&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(2).getEndYear())&&!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(2).getEndMonth())){
                                             workTimeTextTwo.setText("工作时间:"+seekMachinisDataBean.getWorkExperiences().get(2).getBeginYear()+"年"+
                                                     seekMachinisDataBean.getWorkExperiences().get(2).getBeginMonth()+"月-"+
                                                     seekMachinisDataBean.getWorkExperiences().get(2).getEndYear()+"年"+
                                                     seekMachinisDataBean.getWorkExperiences().get(2).getEndMonth()+"月");
                                         }else {
                                             workTimeTextTwo.setText("工作时间:暂无");
                                         }

                                         if (!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(2).getManufacture())&&
                                                 !TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(2).getNoOfManufacture())){
                                             petyTextTwo.setText("钻机机型:"+seekMachinisDataBean.getWorkExperiences().get(2).getManufacture()+
                                                     seekMachinisDataBean.getWorkExperiences().get(2).getNoOfManufacture());
                                         }else {
                                             petyTextTwo.setText("钻机机型:暂无");
                                         }
                                         if (!TextUtils.isEmpty(seekMachinisDataBean.getWorkExperiences().get(2).getDescribing())){
                                             miaoshiTextTwo.setVisibility(View.VISIBLE);
                                             miaoshiTextTwo.setText("工作描述:"+seekMachinisDataBean.getWorkExperiences().get(2).getDescribing());
                                         }else {
                                             miaoshiTextTwo.setText("工作描述:暂无");
                                         }

                                     }

                                 }else {
                                     workExperienceRlayout.setVisibility(View.GONE);
                                     workExperienceLayoutOne.setVisibility(View.GONE);
                                     workExperienceLayoutTwo.setVisibility(View.GONE);

                                 }


                                 BitmapUtils bitmapUtils = new BitmapUtils(SeekMachinistParticulasActivity.this);

                                 bitmapUtils.display(bsrImage,seekMachinisDataBean.getDriverHeader(), new DefaultBitmapLoadCallBack<ImageView>() {
                                     @Override
                                     public void onLoadCompleted(ImageView container, String uri, final Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
                                         super.onLoadCompleted(container, uri, bitmap, config, from);

                                         new Thread(){
                                             public void run(){
                                                 bitmaps=bitmap;
                                                 handler.post(runnableUi);
                                             }
                                         }.start();



                                     }
                                 });
                                 messageLayout.setVisibility(View.VISIBLE);
                                 mSVProgressHUD.dismiss();
                             }


                         }else {
                             notDataLayout.setVisibility(View.VISIBLE);
                             notDataImage.setBackgroundResource(R.mipmap.no_wifi_icon);
                             mSVProgressHUD.dismiss();
                         }


                     }





                 }



            }

            @Override
            public void onFailure(HttpException e, String s) {
                mSVProgressHUD.dismiss();
                notDataLayout.setVisibility(View.VISIBLE);
                notDataImage.setBackgroundResource(R.mipmap.no_wifi_icon);
            }
        });







    }

    // 构建Runnable对象，在runnable中更新界面
    Runnable   runnableUi=new  Runnable(){
        @Override
        public void run() {
            //更新界面

            Bitmap bitmap=fastblur(SeekMachinistParticulasActivity.this,bitmaps,30);
            if (bitmap!=null){
                bsrImage.setImageBitmap(bitmap);
                //mSVProgressHUD.dismiss();
            }
        }

    };
    private void initView() {
        mSVProgressHUD = new SVProgressHUD(this);
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("机手资料");
        retrunText.setOnClickListener(this);
        handler=new Handler();
        phoneTell.setOnClickListener(this);
        refreshButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        SQLhelper sqLhelper=new SQLhelper(SeekMachinistParticulasActivity.this);
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
            case R.id.phone_tell:
                if (!TextUtils.isEmpty(uid)){
                if (!TextUtils.isEmpty(seekMachinisDataBean.getDriverPhoneNumber())){
                    //意图：打电话
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    //url:统一资源定位符
                    //uri:统一资源标示符（更广）
                    intent.setData(Uri.parse("tel:" + seekMachinisDataBean.getDriverPhoneNumber()));
                    //开启系统拨号器
                    startActivity(intent);
                }else {
                    MyAppliction.showToast("该机手没有联系方式");
                }
                }else {
                    Intent intent=new Intent(SeekMachinistParticulasActivity.this,LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;
            case R.id.refresh_button:
                initSeekData();
                break;




        }




    }



    public Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int temp = 256 * divsum;
        int dv[] = new int[temp];
        for (i = 0; i < temp; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                yDown = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                yMove= event.getRawY();
                //滑动的距离
                int distanceX = (int) (xMove - xDown);
                int distanceY= (int) (yMove - yDown);
                //获取顺时速度
                int ySpeed = getScrollVelocity();
                //关闭Activity需满足以下条件：
                //1.x轴滑动的距离>XDISTANCE_MIN
                //2.y轴滑动的距离在YDISTANCE_MIN范围内
                //3.y轴上（即上下滑动的速度）<XSPEED_MIN，如果大于，则认为用户意图是在上下滑动而非左滑结束Activity
                if(distanceX > XDISTANCE_MIN &&(distanceY<YDISTANCE_MIN&&distanceY>-YDISTANCE_MIN)&& ySpeed < YSPEED_MIN) {
                    finish();
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 创建VelocityTracker对象，并将触摸界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     *
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(4000);
        int velocity = (int) mVelocityTracker.getYVelocity();
        return Math.abs(velocity);
    }
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }



}
