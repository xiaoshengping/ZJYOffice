package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.SekkMachinisDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.view.CircleImageView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

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


    @ViewInject(R.id.work_experience_no)
    private TextView workExperienceNo;
    private SekkMachinisDataBean seekMachinisDataBean;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_machinist_particulas);
        ViewUtils.inject(this);
        inti();

    }

    private void inti() {
        initView();
        initSeekData();//数据

    }

    private void initSeekData() {

        seekMachinisDataBean= (SekkMachinisDataBean) getIntent().getSerializableExtra("seekData");
        //Log.e("城市",seekMachinisDataBean.getCity());
        if (seekMachinisDataBean!=null){
            MyAppliction.imageLoader.displayImage(seekMachinisDataBean.getDriverHeader(),circleImageView,MyAppliction.options);
            nameText.setText(seekMachinisDataBean.getDriverName());
            workExperienceText.setText(seekMachinisDataBean.getWorkingAge()+"年工作经验");
            unpterDataText.setText("更新时间"+seekMachinisDataBean.getLastUpdateTimeSubStr());
            addressText.setText("期望地:"+seekMachinisDataBean.getProvince()+seekMachinisDataBean.getCity());
            payText.setText("期望月薪:"+seekMachinisDataBean.getWage()+"元");

            if (seekMachinisDataBean.getWorkInfoItemDtos()!=null&&seekMachinisDataBean.getWorkInfoItemDtos().size()!=0){
                workExperienceNo.setVisibility(View.GONE);
                if (seekMachinisDataBean.getWorkInfoItemDtos().size()>0){
                    messageRlauout.setVisibility(View.VISIBLE);
                    workTimeText.setText("工作时间:"+seekMachinisDataBean.getWorkInfoItemDtos().get(0).getBeginYear()+"年"+
                            seekMachinisDataBean.getWorkInfoItemDtos().get(0).getBeginYear()+"月-"+
                            seekMachinisDataBean.getWorkInfoItemDtos().get(0).getEndYear()+"年"+
                            seekMachinisDataBean.getWorkInfoItemDtos().get(0).getEndMonth()+"月");
                    petyText.setText("钻机机型:"+seekMachinisDataBean.getWorkInfoItemDtos().get(0).getManufacture()+
                            seekMachinisDataBean.getWorkInfoItemDtos().get(0).getNoOfManufacture());
                    miaoShiText.setText("工作描述:"+seekMachinisDataBean.getWorkInfoItemDtos().get(0).getDescribing());
                }else {
                    messageRlauout.setVisibility(View.GONE);


                }
                if (seekMachinisDataBean.getWorkInfoItemDtos().size()>1){
                    workExperienceLayoutOne.setVisibility(View.VISIBLE);
                    imageLini.setVisibility(View.VISIBLE);
                    workTimeTextOne.setText("工作时间:"+seekMachinisDataBean.getWorkInfoItemDtos().get(1).getBeginYear()+"年"+
                            seekMachinisDataBean.getWorkInfoItemDtos().get(1).getBeginYear()+"月-"+
                            seekMachinisDataBean.getWorkInfoItemDtos().get(1).getEndYear()+"年"+
                            seekMachinisDataBean.getWorkInfoItemDtos().get(1).getEndMonth()+"月");
                    petyTextOne.setText("钻机机型:"+seekMachinisDataBean.getWorkInfoItemDtos().get(1).getManufacture()+
                            seekMachinisDataBean.getWorkInfoItemDtos().get(1).getNoOfManufacture());
                    miaoshiTextOne.setText("工作描述:"+seekMachinisDataBean.getWorkInfoItemDtos().get(1).getDescribing());
                }else {
                    workExperienceLayoutOne.setVisibility(View.GONE);
                    imageLini.setVisibility(View.GONE);
                }
                if (seekMachinisDataBean.getWorkInfoItemDtos().size()>2){
                    workExperienceLayoutTwo.setVisibility(View.VISIBLE);
                    imageLiniOne.setVisibility(View.VISIBLE);
                    workTimeTextTwo.setText("工作时间:"+seekMachinisDataBean.getWorkInfoItemDtos().get(1).getBeginYear()+"年"+
                            seekMachinisDataBean.getWorkInfoItemDtos().get(1).getBeginYear()+"月-"+
                            seekMachinisDataBean.getWorkInfoItemDtos().get(1).getEndYear()+"年"+
                            seekMachinisDataBean.getWorkInfoItemDtos().get(1).getEndMonth()+"月");
                    petyTextTwo.setText("钻机机型:"+seekMachinisDataBean.getWorkInfoItemDtos().get(1).getManufacture()+
                            seekMachinisDataBean.getWorkInfoItemDtos().get(1).getNoOfManufacture());
                    miaoshiTextTwo.setText("工作描述:"+seekMachinisDataBean.getWorkInfoItemDtos().get(1).getDescribing());
                }else {
                    workExperienceLayoutTwo.setVisibility(View.GONE);
                    imageLiniOne.setVisibility(View.GONE);
                }

            }else {
                workExperienceNo.setVisibility(View.VISIBLE);
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

        }



    }

    // 构建Runnable对象，在runnable中更新界面
    Runnable   runnableUi=new  Runnable(){
        @Override
        public void run() {
            //更新界面

            Bitmap bitmap=fastblur(SeekMachinistParticulasActivity.this,bitmaps,30);
            if (bitmap!=null){
                bsrImage.setImageBitmap(bitmap);
            }
        }

    };
    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("机主主页");
        retrunText.setOnClickListener(this);
        handler=new Handler();
        phoneTell.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;
            case R.id.phone_tell:
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
