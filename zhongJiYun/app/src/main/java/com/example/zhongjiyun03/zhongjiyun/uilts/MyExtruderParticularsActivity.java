package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.home.MyExtruderBean;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.uilts.showPicture.ImagePagerActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MyExtruderParticularsActivity extends AppCompatActivity implements View.OnClickListener {


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


    @ViewInject(R.id.number_text)
    private  TextView numberText;   //出厂编号
    @ViewInject(R.id.duration_text)
    private TextView durationText;    //钻机工作时长
    @ViewInject(R.id.type_text)
     private TextView typeText  ;   //设备型号
    @ViewInject(R.id.time_text)
    private TextView timeText;       //出厂时间
    @ViewInject(R.id.address_text)
    private TextView addressText;    //所在地
    @ViewInject(R.id.brand_image)
    private ImageView brandImage;      //设备出厂牌
    @ViewInject(R.id.panorama_iamge)
    private ImageView panoramaIamge;   //设备全景
    @ViewInject(R.id.invoice_image)
    private ImageView invoiceImage;   //设备发票
    @ViewInject(R.id.contract_image)
    private ImageView contractImage;   ///合同照
    @ViewInject(R.id.qualified_image)
    private ImageView qualifiedImage;   //合格证
    @ViewInject(R.id.qualified_layout)
    private LinearLayout qualifiedLayout;
    @ViewInject(R.id.leave_factory_textview)
    private LinearLayout leaveFactoryLayout;
    @ViewInject(R.id.panorama_text_view)
    private LinearLayout panoramaLayout;
    @ViewInject(R.id.invoice_textview)
    private LinearLayout invoiceLayout;
    @ViewInject(R.id.contract_layout)
    private LinearLayout contractLayout;

    @ViewInject(R.id.layout)
    private LinearLayout layout;
    private SVProgressHUD mSVProgressHUD;//loding
    private MyExtruderBean myExtruderBean;











    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extruder_particulars);
        ViewUtils.inject(this);
        init();

    }

    private void init() {
        initView();
        initData();

    }

    private void initData() {
        layout.setVisibility(View.GONE);
        myExtruderBean= (MyExtruderBean) getIntent().getSerializableExtra("myExtruderData");
        mSVProgressHUD.showWithStatus("加载中...");
        if (myExtruderBean!=null){
            if (!TextUtils.isEmpty(myExtruderBean.getDeviceNo())){
                numberText.setText(myExtruderBean.getDeviceNo());
            }
            if (!TextUtils.isEmpty(myExtruderBean.getHourOfWork()+"")){
                durationText.setText(myExtruderBean.getHourOfWork()+"");
            }
            if (!TextUtils.isEmpty(myExtruderBean.getNoOfManufacture())){
                typeText.setText(myExtruderBean.getNoOfManufacture());
            }
            if (!TextUtils.isEmpty(myExtruderBean.getDateOfManufacture())&&!TextUtils.isEmpty(myExtruderBean.getDateMonthOfManufacture())){
                timeText.setText(myExtruderBean.getDateOfManufacture()+"年"+myExtruderBean.getDateMonthOfManufacture()+"月");
            }
            if (!TextUtils.isEmpty(myExtruderBean.getProvince())&&!TextUtils.isEmpty(myExtruderBean.getCity())){
                addressText.setText(myExtruderBean.getProvince()+myExtruderBean.getCity());
            }
            if (!TextUtils.isEmpty(myExtruderBean.getDeviceNoPhoto())){
                //Log.e("图片路径",myExtruderBean.getDeviceNoPhoto());
                MyAppliction.imageLoader.displayImage(myExtruderBean.getDeviceNoPhoto(),brandImage,MyAppliction.options);
            }
            if (!TextUtils.isEmpty(myExtruderBean.getDevicePhoto())){

                MyAppliction.imageLoader.displayImage(myExtruderBean.getDevicePhoto(),panoramaIamge,MyAppliction.options);
            }
            if (!TextUtils.isEmpty(myExtruderBean.getDeviceInvoicePhoto())){

                MyAppliction.imageLoader.displayImage(myExtruderBean.getDeviceInvoicePhoto(),invoiceImage,MyAppliction.options);
            }
            if (!TextUtils.isEmpty(myExtruderBean.getDeviceContractPhoto())){

                MyAppliction.imageLoader.displayImage(myExtruderBean.getDeviceContractPhoto(),contractImage,MyAppliction.options);
            }
            if (!TextUtils.isEmpty(myExtruderBean.getDeviceCertificatePhoto())){
                qualifiedLayout.setVisibility(View.VISIBLE);
                MyAppliction.imageLoader.displayImage(myExtruderBean.getDeviceCertificatePhoto(),qualifiedImage,MyAppliction.options);
            }else {
                qualifiedLayout.setVisibility(View.GONE);

            }
            layout.setVisibility(View.VISIBLE);
            mSVProgressHUD.dismiss();
        }else {
            mSVProgressHUD.dismiss();
            MyAppliction.showToast("加载数据失败,请稍后重试！");
            finish();
            overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
        }


    }

    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("钻机详情");
        retrunText.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);
        qualifiedLayout.setOnClickListener(this);
        leaveFactoryLayout.setOnClickListener(this);
        panoramaLayout.setOnClickListener(this);
        invoiceLayout.setOnClickListener(this);
        contractLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;
            case R.id.leave_factory_textview:
                imageBrower(0,myExtruderBean);
                break;
            case R.id.panorama_text_view:
                imageBrower(1,myExtruderBean);
                break;
            case R.id.invoice_textview:
                imageBrower(2,myExtruderBean);

                break;
            case R.id.contract_layout:
                imageBrower(3,myExtruderBean);
                break;
            case R.id.qualified_layout:
                imageBrower(4,myExtruderBean);
                break;





        }




    }

    private void imageBrower(int position,MyExtruderBean urls) {
        Intent intent = new Intent(MyExtruderParticularsActivity.this, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putExtra("tage","MyExtruderBean");
        startActivity(intent);
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
