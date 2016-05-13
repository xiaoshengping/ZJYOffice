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
import com.example.zhongjiyun03.zhongjiyun.bean.home.ServiceParticualrsBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.jpush.android.api.JPushInterface;

public class ServiceParticularsActivity extends AppCompatActivity implements View.OnClickListener {


    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边

    @ViewInject(R.id.name_text_view)
    private TextView nameTextView;  //名字
    @ViewInject(R.id.company_text_view)
    private TextView companyTextView;  //公司名字
    @ViewInject(R.id.phone_text_view)
    private TextView phoneTextView;   //手机号码
    @ViewInject(R.id.immobilization_text_view)
    private TextView immobilizationTextView;  //固定电话
    @ViewInject(R.id.address_text_view)
    private TextView addressTextView;  //地址
    @ViewInject(R.id.chance_zhu_text)
    private TextView chanceZhuText;   //主机
    @ViewInject(R.id.summary_text)
    private TextView summaryText;     //说明
    private SVProgressHUD mSVProgressHUD;//loding
    @ViewInject(R.id.image_view)
    private ImageView imageView;
    @ViewInject(R.id.mssage_data_layout)
    private ScrollView layout;
    @ViewInject(R.id.immob_image)
    private ImageView immobImage;
    @ViewInject(R.id.cell_service_button)
    private Button cellServiceButton;
    private ServiceParticualrsBean serviceProviderBean;




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
    private String uid=null;  //用户id

    @ViewInject(R.id.no_data_rlayout)
    private RelativeLayout noDataRlayout;
    @ViewInject(R.id.button_layout)
    private LinearLayout buttonLayout;

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
        setContentView(R.layout.activity_service_particulars);
        ViewUtils.inject(this);
        inti();

    }

    private void inti() {
        intiView();
        initParticularsData();//获取数据


    }

    private void initParticularsData() {
        HttpUtils htpUtils=new HttpUtils();
        RequestParams requestParms=new RequestParams();
        SQLhelper sqLhelper=new SQLhelper(ServiceParticularsActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("ServiceProviderId"))){
         if (!TextUtils.isEmpty(uid)){
             requestParms.addBodyParameter("id",uid);
         }else {
             requestParms.addBodyParameter("id","");
         }

        requestParms.addBodyParameter("ServiceProviderId",getIntent().getStringExtra("ServiceProviderId"));
            layout.setVisibility(View.GONE);
            mSVProgressHUD.showWithStatus("加载中...");
            buttonLayout.setVisibility(View.GONE);
        htpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getServiceParticularsData(),requestParms, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                if (!TextUtils.isEmpty(responseInfo.result)){
                    Log.e("服务商详情",responseInfo.result);
                    AppBean<ServiceParticualrsBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<ServiceParticualrsBean>>(){});
                    if ((appBean.getResult()).equals("success")){
                        layout.setVisibility(View.VISIBLE);
                        serviceProviderBean=  appBean.getData();
                        if (serviceProviderBean!=null){
                            MyAppliction.imageLoader.displayImage(serviceProviderBean.getThumbnail(),imageView,MyAppliction.RoundedOptionsOne);
                            nameTextView.setText(serviceProviderBean.getName());
                            companyTextView.setText(serviceProviderBean.getProviderName());
                            StringBuffer stringBuffer=new StringBuffer(serviceProviderBean.getPhoneNumber());
                            stringBuffer.replace(3,7,"****");
                            phoneTextView.setText(stringBuffer.toString());
                            if (!TextUtils.isEmpty(serviceProviderBean.getTel())){
                                immobImage.setVisibility(View.VISIBLE);
                                immobilizationTextView.setVisibility(View.VISIBLE);
                                immobilizationTextView.setText(serviceProviderBean.getTel());
                            }else {
                                immobilizationTextView.setVisibility(View.GONE);
                                immobImage.setVisibility(View.GONE);
                            }
                            addressTextView.setText(serviceProviderBean.getAddress());
                            chanceZhuText.setText(serviceProviderBean.getProviderTypeStr());
                            summaryText.setText(serviceProviderBean.getSummary());
                            mSVProgressHUD.dismiss();

                        }

                    }else {
                        mSVProgressHUD.dismiss();
                        mSVProgressHUD.showErrorWithStatus("获取数据失败,请稍后重试");
                        finish();

                    }
                }
                buttonLayout.setVisibility(View.VISIBLE);
                noDataRlayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                mSVProgressHUD.dismiss();
                //mSVProgressHUD.showErrorWithStatus("网络异常,请稍后重试");
                noDataRlayout.setVisibility(View.VISIBLE);
                buttonLayout.setVisibility(View.GONE);
            }
        });

        }else {

            MyAppliction.showToast("暂无数据...");


        }
    }

    private void intiView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("个人资料");
        retrunText.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);
        cellServiceButton.setOnClickListener(this);
        noDataRlayout.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;
            case R.id.cell_service_button:

                    if (!TextUtils.isEmpty(uid)) {
                        if (!TextUtils.isEmpty(serviceProviderBean.getPhoneNumber())){
                        //意图：打电话
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DIAL);
                        //url:统一资源定位符
                        //uri:统一资源标示符（更广）
                        intent.setData(Uri.parse("tel:" + serviceProviderBean.getPhoneNumber()));
                        //开启系统拨号器
                        startActivity(intent);
                        }else {
                            MyAppliction.showToast("该服务商没有联系方式");
                        }
                    }else {
                        Intent intent=new Intent(ServiceParticularsActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }

                break;
            case R.id.no_data_rlayout:
                initParticularsData();

                break;




        }




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
