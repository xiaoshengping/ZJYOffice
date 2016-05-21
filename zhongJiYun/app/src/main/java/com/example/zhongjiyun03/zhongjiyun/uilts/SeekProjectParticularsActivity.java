package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.MyAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.AppDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.seekProject.SeekProjectBean;
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
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

public class SeekProjectParticularsActivity extends AppCompatActivity implements View.OnClickListener {


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
    private View projectParticularsView;
    private View matingFacilyView;
    @ViewInject(R.id.checkBox_check)
    private ImageView checkBoxCheck;
    private String seekProjectId;
    private SVProgressHUD mSVProgressHUD;//loding
    @ViewInject(R.id.competitive_button)
    private Button competitiveButton; //竞标按钮
    private SeekProjectBean seekProjectBean;
    private TimeCount time;      //获取计时线程
    private AlertDialog dlg;
    private boolean isChecked = true;//是否关注
   /* @ViewInject(R.id.image_view)
    private ImageView imageViewFcto;*/
    @ViewInject(R.id.message_data_layout)
    private LinearLayout messageDataLayout;
    @ViewInject(R.id.no_data_rlayout)
    private RelativeLayout noDataRlayout;


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
        setContentView(R.layout.activity_seek_project_particulars);
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
        initViewPager();
        initView();
        initData();


    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        initData();
    }

    private void initData() {
        seekProjectId = getIntent().getStringExtra("seekProjectId");
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        if (!TextUtils.isEmpty(seekProjectId)) {
            SQLhelper sqLhelper = new SQLhelper(SeekProjectParticularsActivity.this);
            SQLiteDatabase db = sqLhelper.getWritableDatabase();
            Cursor cursor = db.query(SQLhelper.tableName, null, null, null, null, null, null);
            String uid = null;  //用户id
            while (cursor.moveToNext()) {
                uid = cursor.getString(0);
            }
            if (!TextUtils.isEmpty(uid)) {
                //步骤1：创建一个SharedPreferences接口对象
                SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
                //步骤2：获取文件中的值
                String sesstionId = read.getString("code", "");
                requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
                requestParams.addBodyParameter("id", uid);
            }
            requestParams.addBodyParameter("projectId", seekProjectId);
            mSVProgressHUD.showWithStatus("加载中...");
            messageDataLayout.setVisibility(View.GONE);
            competitiveButton.setVisibility(View.GONE);
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getProjecctParticularsData(), requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("项目详情", responseInfo.result);
                    if (!TextUtils.isEmpty(responseInfo.result)) {
                        AppBean<SeekProjectBean> appBean = JSONObject.parseObject(responseInfo.result, new TypeReference<AppBean<SeekProjectBean>>() {
                        });
                        if (appBean.getResult().equals("success")) {
                            seekProjectBean = appBean.getData();
                            if (seekProjectBean != null) {
                                initProjectView(seekProjectBean);
                                initMatingFacliyView(seekProjectBean);
                                //imageViewFcto.setVisibility(View.GONE);
                                mSVProgressHUD.dismiss();
                            }


                        } else {
                            //imageViewFcto.setVisibility(View.GONE);
                            mSVProgressHUD.dismiss();
                        }

                    } else {
                        mSVProgressHUD.dismiss();
                    }
                    messageDataLayout.setVisibility(View.VISIBLE);
                    noDataRlayout.setVisibility(View.GONE);
                    competitiveButton.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.e("项目详情", s);
                    mSVProgressHUD.dismiss();
                    noDataRlayout.setVisibility(View.VISIBLE);
                    competitiveButton.setVisibility(View.GONE);
                }
            });
        } else {
            MyAppliction.showToast("数据加载失败");
        }


    }

    //配置设施
    private void initMatingFacliyView(final SeekProjectBean seekProjectBean) {
        TextView fuwuNumberText = (TextView) matingFacilyView.findViewById(R.id.fuwu_number_text);
        TextView exturdNumberText = (TextView) matingFacilyView.findViewById(R.id.exturd_number_text);
        TextView jshouNUmberText = (TextView) matingFacilyView.findViewById(R.id.jshou_number_text);
        TextView peiJNumberText = (TextView) matingFacilyView.findViewById(R.id.peij_number_text);
        fuwuNumberText.setText(seekProjectBean.getServiceProviderCount() + "");
        exturdNumberText.setText(seekProjectBean.getSecondHandCount() + "");
        jshouNUmberText.setText(seekProjectBean.getDriverCount() + "");
        peiJNumberText.setText(seekProjectBean.getDeviceCount() + "");
        RelativeLayout serviceLayout = (RelativeLayout) matingFacilyView.findViewById(R.id.service_layout);
        RelativeLayout secondHandLayout = (RelativeLayout) matingFacilyView.findViewById(R.id.second_hand_layout);
        RelativeLayout exturdLayout = (RelativeLayout) matingFacilyView.findViewById(R.id.exturd_layout);

        serviceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekProjectBean.getServiceProviderCount() == 0) {
                    MyAppliction.showToast("您附近没有服务商");
                } else {
                    Intent intent = new Intent(SeekProjectParticularsActivity.this, ServiceProviderActivity.class);
                    intent.putExtra("data", seekProjectBean.getProvince());
                    intent.putExtra("tage", "matingFacily");
                    startActivity(intent);
                }

            }
        });
        secondHandLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekProjectBean.getSecondHandCount() == 0) {
                    MyAppliction.showToast("您附近没有二手钻机");
                } else {
                    Intent intent = new Intent(SeekProjectParticularsActivity.this, SecondHandActivity.class);
                    intent.putExtra("data", seekProjectBean.getProvince());
                    intent.putExtra("tage", "matingFacily");
                    startActivity(intent);
                }

            }
        });
        exturdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekProjectBean.getDriverCount() == 0) {
                    MyAppliction.showToast("您附近没有机手");
                } else {
                    Intent intent = new Intent(SeekProjectParticularsActivity.this, SeekMachinistActivity.class);
                    intent.putExtra("data", seekProjectBean.getProvince());
                    intent.putExtra("tage", "matingFacily");
                    startActivity(intent);
                }

            }
        });


    }
    //项目概况数据展示

    private void initProjectView(final SeekProjectBean seekProjectBean) {

        TextView tailtText = (TextView) projectParticularsView.findViewById(R.id.tailt_text);
        TextView phoneContentText = (TextView) projectParticularsView.findViewById(R.id.phone_content_text);
        TextView dateContentText = (TextView) projectParticularsView.findViewById(R.id.date_content_text);
        TextView addressContentText = (TextView) projectParticularsView.findViewById(R.id.address_content_text);
        TextView companyContentText = (TextView) projectParticularsView.findViewById(R.id.company_content_text);
        TextView workDateContentText = (TextView) projectParticularsView.findViewById(R.id.work_date_content_text);
        TextView gonchengContentText = (TextView) projectParticularsView.findViewById(R.id.goncheng_content_text);
        TextView diameterContentText = (TextView) projectParticularsView.findViewById(R.id.diameter_content_text);
        TextView pileContentText = (TextView) projectParticularsView.findViewById(R.id.pile_content_text);
        TextView leixingContentText = (TextView) projectParticularsView.findViewById(R.id.leixing_content_text);
        TextView projectIntroduceContent = (TextView) projectParticularsView.findViewById(R.id.project_introduce_content);
        TextView extruderContenText = (TextView) projectParticularsView.findViewById(R.id.extruder_conten_text);
        TextView addressContentTextView = (TextView) projectParticularsView.findViewById(R.id.address_content_text_view);

        TextView ratingOne = (TextView) projectParticularsView.findViewById(R.id.rating_one);

        TextView ratingTwo = (TextView) projectParticularsView.findViewById(R.id.rating_two);

        TextView ratingThree = (TextView) projectParticularsView.findViewById(R.id.rating_three);

        TextView ratingFour = (TextView) projectParticularsView.findViewById(R.id.rating_four);

        TextView ratingFive = (TextView) projectParticularsView.findViewById(R.id.rating_five);


        if (!TextUtils.isEmpty(seekProjectBean.getTitle())) {

            tailtText.setText(seekProjectBean.getTitle());
        }
        if (!TextUtils.isEmpty(seekProjectBean.getName())) {

            phoneContentText.setText(seekProjectBean.getName());
        }
        if (!TextUtils.isEmpty(seekProjectBean.getCreateDateStr())) {

            dateContentText.setText(seekProjectBean.getCreateDateStr());
        }
        if (!TextUtils.isEmpty(seekProjectBean.getProvince()) && !TextUtils.isEmpty(seekProjectBean.getCity())) {

            addressContentText.setText(seekProjectBean.getProvince() + seekProjectBean.getCity());
        }
        if (!TextUtils.isEmpty(seekProjectBean.getProjectCompany())) {

            companyContentText.setText(seekProjectBean.getProjectCompany());
        }
        if (!TextUtils.isEmpty(seekProjectBean.getTimeLimit())) {

            workDateContentText.setText(seekProjectBean.getTimeLimit() + "个月");
        }
        if (!TextUtils.isEmpty(seekProjectBean.getWorkAmount())) {

            gonchengContentText.setText(seekProjectBean.getWorkAmount() );
        }
        if (!TextUtils.isEmpty(seekProjectBean.getDiameter())) {

            diameterContentText.setText(seekProjectBean.getDiameter() );
        }
        if (!TextUtils.isEmpty(seekProjectBean.getPileDepth())) {

            pileContentText.setText(seekProjectBean.getPileDepth() );
        }
        if (!TextUtils.isEmpty(seekProjectBean.getProjectRequirementTypeStr())) {

            leixingContentText.setText(seekProjectBean.getProjectRequirementTypeStr());
        }
        if (!TextUtils.isEmpty(seekProjectBean.getProfile())) {

            projectIntroduceContent.setText(seekProjectBean.getProfile());
        }
        if (!TextUtils.isEmpty(seekProjectBean.getDeviceRequirement())) {

            extruderContenText.setText(seekProjectBean.getDeviceRequirement());
        }
        if (!TextUtils.isEmpty(seekProjectBean.getGeologicReport())) {

            addressContentTextView.setText(seekProjectBean.getGeologicReport());
        }else {
            addressContentTextView.setText("暂无");
        }

        if (seekProjectBean.getIsCollection() == 1) {
            checkBoxCheck.setBackgroundResource(R.mipmap.collect_icon_cur);
        } else {
            checkBoxCheck.setBackgroundResource(R.mipmap.collect_icon);
        }

        if (seekProjectBean.getCanReply().equals("success")) {
            competitiveButton.setText("我要竞标");
            competitiveButton.setTextColor(getResources().getColor(R.color.white));
            competitiveButton.setBackgroundResource(R.drawable.loing_button_corners);
        } else if (seekProjectBean.getCanReply().equals("联系业主")) {
            competitiveButton.setTextColor(getResources().getColor(R.color.white));
            competitiveButton.setBackgroundResource(R.drawable.loing_button_corners);
            competitiveButton.setText(seekProjectBean.getCanReply());
        } else {

            competitiveButton.setTextColor(getResources().getColor(R.color.tailt_dark));
            competitiveButton.setBackgroundResource(R.drawable.gray_button_corners);
            competitiveButton.setText(seekProjectBean.getCanReply());
        }

        String StarRate = seekProjectBean.getBossStarLevel();
        if (StarRate.equals("1")) {
            ratingOne.setBackgroundResource(R.mipmap.eval_icon);

        } else if (StarRate.equals("2")) {

            ratingOne.setBackgroundResource(R.mipmap.eval_icon);
            ratingTwo.setBackgroundResource(R.mipmap.eval_icon);


        } else if (StarRate.equals("3")) {

            ratingOne.setBackgroundResource(R.mipmap.eval_icon);
            ratingTwo.setBackgroundResource(R.mipmap.eval_icon);
            ratingThree.setBackgroundResource(R.mipmap.eval_icon);


        } else if (StarRate.equals("4")) {

            ratingOne.setBackgroundResource(R.mipmap.eval_icon);
            ratingTwo.setBackgroundResource(R.mipmap.eval_icon);
            ratingThree.setBackgroundResource(R.mipmap.eval_icon);
            ratingFour.setBackgroundResource(R.mipmap.eval_icon);


        } else if (StarRate.equals("5")) {
            ratingOne.setBackgroundResource(R.mipmap.eval_icon);
            ratingTwo.setBackgroundResource(R.mipmap.eval_icon);
            ratingThree.setBackgroundResource(R.mipmap.eval_icon);
            ratingFour.setBackgroundResource(R.mipmap.eval_icon);
            ratingFive.setBackgroundResource(R.mipmap.eval_icon);

        }


    }

    private void initView() {
        noDataRlayout.setOnClickListener(this);
        shardText.setOnClickListener(this);
        titleNemeTv.setText("项目详情");
        retrunText.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);
        competitiveButton.setOnClickListener(this);
        checkBoxCheck.setOnClickListener(this);
        time = new TimeCount(3000, 1000);//构造CountDownTimer对象
        dlg = new AlertDialog.Builder(SeekProjectParticularsActivity.this).create();

    }

    @Override
    public void onClick(View v) {
        SQLhelper sqLhelper = new SQLhelper(SeekProjectParticularsActivity.this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        Cursor cursor = db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid = null;  //用户id
        String states = null;  //用户星级

        while (cursor.moveToNext()) {
            uid = cursor.getString(0);
            states = cursor.getString(3);

        }
        switch (v.getId()) {

            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.shard_tv:
                showShare();

                break;
            case R.id.competitive_button:
                if (!TextUtils.isEmpty(uid)) {
                    if (seekProjectBean.getIsCallOwnerFlag() == 0) {
                        if (seekProjectBean.getCanReply().equals("success")) {
                            Intent intent = new Intent(SeekProjectParticularsActivity.this, CompetitiveDescribeActivity.class);
                            intent.putExtra("ProjectId", seekProjectId);
                            startActivity(intent);

                        } else {
                            MyAppliction.showToast(seekProjectBean.getCanReply());
                        }
                    } else {
                        if (!TextUtils.isEmpty(seekProjectId)) {
                            if (!TextUtils.isEmpty(uid)) {
                                //意图：打电话
                                CellOwnerData(uid, seekProjectId);
                            } else {
                                Intent intent = new Intent(SeekProjectParticularsActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }

                        } else {
                            MyAppliction.showToast("该业主没有联系方式");
                        }

                    }

                } else {
                    Intent loginIntent = new Intent(SeekProjectParticularsActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }
                break;
            case R.id.checkBox_check:

                //步骤1：创建一个SharedPreferences接口对象
                SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
                //步骤2：获取文件中的值
                String sesstionId = read.getString("code", "");
                if (!TextUtils.isEmpty(uid)) {
                    if (isChecked) {
                        if (seekProjectBean.getIsCollection() != 1) {
                            isCheckedRequest(uid, sesstionId);
                        }
                        checkBoxCheck.setBackgroundResource(R.mipmap.collect_icon_cur);
                        isChecked = false;
                    } else {
                        isNoCheckedRequest(uid, sesstionId);
                        checkBoxCheck.setBackgroundResource(R.mipmap.collect_icon);
                        isChecked = true;
                    }
                } else {
                    Intent intent = new Intent(SeekProjectParticularsActivity.this, LoginActivity.class);
                    startActivity(intent);

                }
                break;
            case R.id.no_data_rlayout:
                initData();
                break;


        }


    }

    //拨打电话
    private void CellOwnerData(String uid, String seekProjectId) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code", "");
        if (!TextUtils.isEmpty(seekProjectId)) {
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
            requestParams.addBodyParameter("id", uid);
            requestParams.addBodyParameter("projectId", seekProjectId);
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getCallOwerData(), requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("拨打业主电话", responseInfo.result);
                    if (!TextUtils.isEmpty(responseInfo.result)) {
                        AppDataBean appDataBean = JSONObject.parseObject(responseInfo.result, new TypeReference<AppDataBean>() {
                        });
                        if (appDataBean != null) {
                            if (appDataBean.getResult().equals("success")) {
                                time.start();
                                showExitGameAlert("正在为您拨打电话中", "尊敬的用户，中基云平台正在为您拨打机主电话，请耐心等待10秒钟");
                            } else {
                                MyAppliction.showToast("拨打电话失败");
                            }
                        }
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.e("拨打业主电话", s);
                }
            });

        } else {
            Intent intent = new Intent(SeekProjectParticularsActivity.this, LoginActivity.class);
            startActivity(intent);
        }


    }

    //电话框
    private void showExitGameAlert(String text, String textTv) {

        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.cell_alert_layout);
        TextView tailte = (TextView) window.findViewById(R.id.tailte_tv);
        TextView tailteTv = (TextView) window.findViewById(R.id.tv);
        tailteTv.setText(text);
        tailte.setText(textTv);
        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        ok.setText("关闭");
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                dlg.cancel();
            }
        });

        /*// 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText("取消");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });*/
    }



    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            dlg.cancel();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示

        }

    }

    private void isNoCheckedRequest(String uid, String sesstionId) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();

        if (!TextUtils.isEmpty(uid)) {
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
            requestParams.addBodyParameter("Id", uid);
            requestParams.addBodyParameter("collectId", seekProjectId);
            requestParams.addBodyParameter("collectType", "1");
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAttentionNoData(), requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (!TextUtils.isEmpty(responseInfo.result)) {
                        AppDataBean appDataBean = JSONObject.parseObject(responseInfo.result, new TypeReference<AppDataBean>() {
                        });
                        if (appDataBean.getResult().equals("success")) {
                            mSVProgressHUD.showSuccessWithStatus("您已取消关注！");
                        } else {
                            mSVProgressHUD.showErrorWithStatus("噢噢,取消关注失败");
                        }
                    }

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.e("取消关注项目", s);
                }
            });
        }


    }

    private void isCheckedRequest(String uid, String sesstionId) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();

        if (!TextUtils.isEmpty(uid)) {
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
            requestParams.addBodyParameter("Id", uid);
            requestParams.addBodyParameter("collectId", seekProjectId);
            requestParams.addBodyParameter("collectType", "1");
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAttentionData(), requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (!TextUtils.isEmpty(responseInfo.result)) {
                        AppDataBean appDataBean = JSONObject.parseObject(responseInfo.result, new TypeReference<AppDataBean>() {
                        });
                        if (appDataBean.getResult().equals("success")) {
                            mSVProgressHUD.showSuccessWithStatus("关注成功！");
                        } else {
                            mSVProgressHUD.showErrorWithStatus("噢噢,关注失败");
                        }
                    }

                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });
        }


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


    private void initViewPager() {
        projectParticularsView = getLayoutInflater().inflate(R.layout.project_particulars_layout, null);
        matingFacilyView = getLayoutInflater().inflate(R.layout.mating_facility_layout, null);

        lists.add(projectParticularsView);
        lists.add(matingFacilyView);

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
                            animation = new TranslateAnimation(0, offSet * 2
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
    public boolean dispatchTouchEvent(MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                yDown = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                yMove = event.getRawY();
                //滑动的距离
                int distanceX = (int) (xMove - xDown);
                int distanceY = (int) (yMove - yDown);
                //获取顺时速度
                int ySpeed = getScrollVelocity();
                //关闭Activity需满足以下条件：
                //1.x轴滑动的距离>XDISTANCE_MIN
                //2.y轴滑动的距离在YDISTANCE_MIN范围内
                //3.y轴上（即上下滑动的速度）<XSPEED_MIN，如果大于，则认为用户意图是在上下滑动而非左滑结束Activity
                if (distanceX > XDISTANCE_MIN && (distanceY < YDISTANCE_MIN && distanceY > -YDISTANCE_MIN) && ySpeed < YSPEED_MIN) {
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

    //分享
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                if ("Wechat".equals(platform.getName())) {
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);

                }
                if ("WechatMoments".equals(platform.getName())) {
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                }
            }
        });
        if (seekProjectBean!=null) {
            // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
            //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
            oks.setTitle(seekProjectBean.getTitle() + "_" + seekProjectBean.getTimeLimit() + "月_" + seekProjectBean.getProvince() + seekProjectBean.getCity() + "_中基云项目信息中心");
            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
            oks.setTitleUrl(AppUtilsUrl.BaseUrl+"App/Index.html#/tab/my/owner-bid-project-details?id=" + seekProjectId);
            // text是分享文本，所有平台都需要这个字段
            oks.setText(seekProjectBean.getTitle() + "_" + seekProjectBean.getTimeLimit() + "月_" + seekProjectBean.getProvince() + seekProjectBean.getCity() + "_中基云项目信息中心");
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
            oks.setImageUrl("http://www.zhongjiyun.cn/app/img/logo1.png");
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl(AppUtilsUrl.BaseUrl+"App/Index.html#/tab/my/owner-bid-project-details?id=" + seekProjectId);
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setComment(seekProjectBean.getTitle() + "_" + seekProjectBean.getTimeLimit() + "月_" + seekProjectBean.getProvince() + seekProjectBean.getCity() + "_中基云项目信息中心");
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(getString(R.string.app_name));
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl(AppUtilsUrl.BaseUrl+"App/Index.html#/tab/my/owner-bid-project-details?id=" + seekProjectId);
            // 启动分享GUI
            oks.show(this);
        }else {
            MyAppliction.showToast("网络异常，请稍后重试");
        }
    }


}
