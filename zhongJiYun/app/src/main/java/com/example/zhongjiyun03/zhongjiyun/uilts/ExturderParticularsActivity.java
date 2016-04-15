package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.ExturderpImagePagerAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.AppDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.DeviceImagesBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.SecondHandListProjectBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.example.zhongjiyun03.zhongjiyun.widget.AutoScrollViewPager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
/*
* 二手机详情
* */
public class ExturderParticularsActivity extends AppCompatActivity implements View.OnClickListener {



    @ViewInject(R.id.shard_tv)
    private TextView shardText;   //头部右边
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




    //广告轮播
    @ViewInject(R.id.home_banner_viewpager)
    private AutoScrollViewPager autoPager;
    @ViewInject(R.id.home_dot_ll)
    private LinearLayout dotLL;
    private ExturderpImagePagerAdapter pagerAdapter;
    private ArrayList<DeviceImagesBean> imageUrls = new ArrayList<>();
    private String url = "http://mobapi.meilishuo.com/2.0/activity/selected?imei=000000000000000&mac=08%3A00%3A27%3A51%3A2e%3Aaa&qudaoid=11601&access_token=d154111f2e870ea8e58198e0f8c59339";
    private String[] iamgeString = {"R.drawable.ic_launcher",};

    //拨打电话
    @ViewInject(R.id.phone_text_view)
    private TextView phoneTextView;
    @ViewInject(R.id.tailt_text_view)
    private TextView tailtTextView;  //设备型号
    @ViewInject(R.id.time_text_view)
    private TextView timeTextView;      //出厂时间
    @ViewInject(R.id.price_text_view)
    private TextView priceTextView;    //价格
    @ViewInject(R.id.box_name_text)
    private TextView boxNameText;     //机主名字
    @ViewInject(R.id.work_time_text)
    private TextView workTimeText;     //钻机工作时间
    @ViewInject(R.id.brand_text)
    private TextView brandText;          //钻机品牌
    @ViewInject(R.id.chassis_type_text)
    private TextView chassisTypeText;    //底盘型号
    @ViewInject(R.id.work_weight_text)
    private TextView workWeightTextl;    //整机工作重量
    @ViewInject(R.id.reverse_text)
    private TextView reverseText;    //最大扭转
    @ViewInject(R.id.drill_speed_text)
    private TextView drillSpeedText;  //钻孔速度
    @ViewInject(R.id.engine_type_text)
    private TextView engineTypeText;  //发动机型号
    @ViewInject(R.id.engine_power_text)
    private TextView enginePowerText;  //发动机额定功率
    @ViewInject(R.id.elevating_power_text)
    private TextView elevatingPowerText;  //提升力
    @ViewInject(R.id.dirll_diameter_text)
    private TextView dirllDiameteText;  //最大成孔直径
    @ViewInject(R.id.dirll_depth_text)
    private TextView dirllDepthText;    //最大成孔深度
    @ViewInject(R.id.txiang_image)
    private TextView txiangImage;     //头像image
    @ViewInject(R.id.address_text)
    private TextView addressText;     //定位地址
    private SecondHandListProjectBean secondHandBean; //数据
    @ViewInject(R.id.tailt_text)
    private TextView tailtText; //机主描述
    @ViewInject(R.id.shui_ming_text)
    private TextView shuiMingText;
    @ViewInject(R.id.image_view)
    private ImageView imageView;
    @ViewInject(R.id.advertisement_rlayout)
    private RelativeLayout advertisementRlayout;
    @ViewInject(R.id.checkBox_check)
    private CheckBox checkBoxCheck;
    private SVProgressHUD mSVProgressHUD;//loding









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exturder_particulars);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        initView();

        initPager();
        loadData();
        intiData();


    }

    private void intiData() {

       String secondHandBeanId= getIntent().getStringExtra("secondHandData");
         if (!TextUtils.isEmpty(secondHandBeanId)){
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("id",secondHandBeanId);
        mSVProgressHUD.showWithStatus("正在加载中...");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getSecondExtruderParticualsData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                if (!TextUtils.isEmpty(responseInfo.result)){
                    Log.e("二手钻机详情",responseInfo.result);
                    AppBean<SecondHandListProjectBean> appListDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<SecondHandListProjectBean>>(){});
                     if (appListDataBean.getResult().equals("success")){
                         secondHandBean=    appListDataBean.getData();
                        if (secondHandBean!=null){

                            tailtTextView.setText(secondHandBean.getDeviceDto().getManufacture()+secondHandBean.getDeviceDto().getNoOfManufacture());
                            timeTextView.setText(secondHandBean.getDeviceDto().getDateOfManufacture()+"年");
                            priceTextView.setText(secondHandBean.getPriceStr()+"万");
                            boxNameText.setText(secondHandBean.getDeviceDto().getBossName());
                            if (!TextUtils.isEmpty(secondHandBean.getDeviceDto().getHourOfWork()+"")){
                                workTimeText.setText(secondHandBean.getDeviceDto().getHourOfWork()+"");
                            }else {
                                workTimeText.setText("0");
                            }
                             if (!TextUtils.isEmpty(secondHandBean.getDeviceDto().getDeviceNo())){
                                 brandText.setText(secondHandBean.getDeviceDto().getDeviceNo());
                             }else {
                                 brandText.setText("0");
                             }
                             if (TextUtils.isEmpty(secondHandBean.getDeviceBaseDto().getChassisMode()+"")){
                                 chassisTypeText.setText(secondHandBean.getDeviceBaseDto().getChassisMode()+"");
                             }else {
                                 chassisTypeText.setText("0");
                             }

                           /*  workWeightTextl.setText(secondHandBean.getDeviceBaseDto().getWorkingWeight());
                           reverseText.setText(secondHandBean.getDeviceBaseDto().getMaxTorque());
                            drillSpeedText.setText(secondHandBean.getDeviceBaseDto().getDrillingSpeed());
                            engineTypeText.setText(secondHandBean.getDeviceBaseDto().getEngineType());
                            enginePowerText.setText(secondHandBean.getDeviceBaseDto().getEnginePowerRating());
                            elevatingPowerText.setText(secondHandBean.getDeviceBaseDto().getMainHoistingForce());
                            dirllDiameteText.setText(secondHandBean.getDeviceBaseDto().getMaxHoleDiameter());
                            dirllDepthText.setText(secondHandBean.getDeviceBaseDto().getMaxHoleDepth());*/
                            if (!TextUtils.isEmpty(secondHandBean.getProvince())&&!TextUtils.isEmpty(secondHandBean.getAddress())){
                             addressText.setText(secondHandBean.getProvince()+secondHandBean.getAddress());
                            }else if (!TextUtils.isEmpty(secondHandBean.getProvince())){
                                addressText.setText(secondHandBean.getProvince());
                            }
                            if (!TextUtils.isEmpty(secondHandBean.getDescribing())){
                                shuiMingText.setVisibility(View.VISIBLE);
                                shuiMingText.setText(secondHandBean.getDescribing());
                            }else {
                                shuiMingText.setVisibility(View.GONE);
                            }
                            tailtText.setText("机主"+secondHandBean.getDeviceDto().getBossName()+"自述");
                            MyAppliction.imageLoader.displayImage(AppUtilsUrl.BaseUrl+secondHandBean.getDeviceDto().getBossHeadthumb(),imageView,MyAppliction.RoundedOptionsOne);
                             if (secondHandBean.getDeviceBaseDto().getDeviceImages()!=null&&
                                     secondHandBean.getDeviceBaseDto().getDeviceImages().size()!=0){
                                 advertisementRlayout.setVisibility(View.VISIBLE);
                                 imageUrls.addAll(secondHandBean.getDeviceBaseDto().getDeviceImages());
                                 pagerAdapter.refreshData(true);
                             }else {
                                 advertisementRlayout.setVisibility(View.GONE);
                             }
                            mSVProgressHUD.dismiss();


                        }


                     }


                }



            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

         }else {

             MyAppliction.showToast("数据加载失败");

         }





    }

    private void initView() {
        phoneTextView.setOnClickListener(this);
        titleNemeTv.setText("钻机详情");
        shardText.setOnClickListener(this);
        retrunText.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);
        checkBoxCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isCheckedRequest();

                }else {

                    isNoCheckedRequest();
                }
            }


        });

    }
    private void isNoCheckedRequest() {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        SQLhelper sqLhelper=new SQLhelper(ExturderParticularsActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        if (!TextUtils.isEmpty(uid)){
            requestParams.addBodyParameter("Id",uid);
            requestParams.addBodyParameter("collectId",getIntent().getStringExtra("secondHandData"));
            requestParams.addBodyParameter("collectType","5");
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAttentionNoData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        AppDataBean appDataBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
                        if (appDataBean.getResult().equals("success")){
                            mSVProgressHUD.showSuccessWithStatus("您已取消关注！");
                        }else {
                            mSVProgressHUD.showErrorWithStatus("噢噢,取消关注失败");
                        }
                    }

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.e("取消关注项目",s);
                }
            });
        }else {
            Intent loginIntent=new Intent(ExturderParticularsActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            overridePendingTransition(R.anim.anim_open, R.anim.anim_close);


        }





    }
    private void isCheckedRequest() {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        SQLhelper sqLhelper=new SQLhelper(ExturderParticularsActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        if (!TextUtils.isEmpty(uid)){
            requestParams.addBodyParameter("Id",uid);
            requestParams.addBodyParameter("collectId",getIntent().getStringExtra("secondHandData"));
            requestParams.addBodyParameter("collectType","5");
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAttentionData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        AppDataBean appDataBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
                        if (appDataBean.getResult().equals("success")){
                            mSVProgressHUD.showSuccessWithStatus("关注成功！");
                        }else {
                            mSVProgressHUD.showErrorWithStatus("噢噢,关注失败");
                        }
                    }

                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });
        }else {
            Intent loginIntent=new Intent(ExturderParticularsActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            overridePendingTransition(R.anim.anim_open, R.anim.anim_close);


        }





    }

    private void initPager() {
        pagerAdapter = new ExturderpImagePagerAdapter(this, imageUrls, dotLL);
        autoPager.setAdapter(pagerAdapter);
        autoPager.setOnPageChangeListener(pagerAdapter);


    }


    /**
     * Gson
     */
    private void loadData() {
        /*HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAdvertisementData(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("dsjfjfj",responseInfo.result);

                if(!TextUtils.isEmpty(responseInfo.result)){
                    AppListDataBean<AdvertisementBean> appListDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppListDataBean<AdvertisementBean>>(){});
                    if (appListDataBean.getResult().equals("success")){
                        List<AdvertisementBean> advertisementBeen=appListDataBean.getData();
                        if (advertisementBeen!=null){


                        }

                    }


                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });*/


    }

    @Override
    public void onResume() {
        super.onResume();
        autoPager.startAutoScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        autoPager.stopAutoScroll();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone_text_view:
                  if (!TextUtils.isEmpty(secondHandBean.getDeviceBaseDto().getDeviceBasePhone())){
                      //意图：打电话
                      Intent intent = new Intent();
                      intent.setAction(Intent.ACTION_DIAL);
                      //url:统一资源定位符
                      //uri:统一资源标示符（更广）
                      intent.setData(Uri.parse("tel:" + secondHandBean.getDeviceBaseDto().getDeviceBasePhone()));
                      //开启系统拨号器
                      startActivity(intent);
                  }else {
                      MyAppliction.showToast("该机主没有联系方式");
                  }


                break;
            case R.id.shard_tv:
                showShare();

                break;
            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;


        }
    }



    //分享
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("分享");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
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

    /*
   * 向右滑动返回前一个页面
   *
   * */
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
        overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
    }


}
