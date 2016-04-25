package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class SeekProjectParticularsActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {


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
    private  View matingFacilyView;
    @ViewInject(R.id.checkBox_check)
    private CheckBox checkBoxCheck;
    private String seekProjectId;
    private SVProgressHUD mSVProgressHUD;//loding
    @ViewInject(R.id.competitive_button)
    private Button competitiveButton; //竞标按钮
    private SeekProjectBean seekProjectBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_project_particulars);
        ViewUtils.inject(this);
        init();



    }

    private void init() {
        initViewPager();
        initView();
        initData();



    }

    private void initData() {
        seekProjectId= getIntent().getStringExtra("seekProjectId");
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        if (!TextUtils.isEmpty(seekProjectId)){
            SQLhelper sqLhelper=new SQLhelper(SeekProjectParticularsActivity.this);
            SQLiteDatabase db= sqLhelper.getWritableDatabase();
            Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
            String uid=null;  //用户id

            while (cursor.moveToNext()) {
                uid=cursor.getString(0);

            }
            requestParams.addBodyParameter("projectId",seekProjectId);
            requestParams.addBodyParameter("id",uid);
            mSVProgressHUD.showWithStatus("加载中...");
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getProjecctParticularsData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("项目详情",responseInfo.result);
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        AppBean<SeekProjectBean> appBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<SeekProjectBean>>(){});
                        if (appBean.getResult().equals("success")){
                            seekProjectBean=appBean.getData();
                            if (seekProjectBean!=null){
                                initProjectView(seekProjectBean);
                                initMatingFacliyView(seekProjectBean);
                                mSVProgressHUD.dismiss();
                            }


                        }else {
                            mSVProgressHUD.dismiss();
                        }

                    }else {
                        mSVProgressHUD.dismiss();
                    }

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.e("项目详情",s);
                    mSVProgressHUD.dismiss();
                }
            });
        }else {
            MyAppliction.showToast("数据加载失败");
        }



    }
      //配置设施
    private void initMatingFacliyView(SeekProjectBean seekProjectBean) {
             TextView fuwuNumberText= (TextView) matingFacilyView.findViewById(R.id.fuwu_number_text);
             TextView exturdNumberText= (TextView) matingFacilyView.findViewById(R.id.exturd_number_text);
             TextView jshouNUmberText= (TextView) matingFacilyView.findViewById(R.id.jshou_number_text);
             TextView peiJNumberText= (TextView) matingFacilyView.findViewById(R.id.peij_number_text);
              fuwuNumberText.setText(seekProjectBean.getServiceProviderCount()+"");
              exturdNumberText.setText(seekProjectBean.getDriverCount()+"");
              fuwuNumberText.setText(seekProjectBean.getSecondHandCount()+"");
              fuwuNumberText.setText(seekProjectBean.getDeviceCount()+"");

    }
    //项目概况数据展示

    private void initProjectView(final SeekProjectBean seekProjectBean) {

        TextView tailtText= (TextView) projectParticularsView.findViewById(R.id.tailt_text);
        TextView phoneContentText= (TextView) projectParticularsView.findViewById(R.id.phone_content_text);
        TextView dateContentText= (TextView) projectParticularsView.findViewById(R.id.date_content_text);
        TextView addressContentText= (TextView) projectParticularsView.findViewById(R.id.address_content_text);
        TextView companyContentText= (TextView) projectParticularsView.findViewById(R.id.company_content_text);
        TextView workDateContentText= (TextView) projectParticularsView.findViewById(R.id.work_date_content_text);
        TextView gonchengContentText= (TextView) projectParticularsView.findViewById(R.id.goncheng_content_text);
        TextView diameterContentText= (TextView) projectParticularsView.findViewById(R.id.diameter_content_text);
        TextView pileContentText= (TextView) projectParticularsView.findViewById(R.id.pile_content_text);
        TextView leixingContentText= (TextView) projectParticularsView.findViewById(R.id.leixing_content_text);
        TextView projectIntroduceContent= (TextView) projectParticularsView.findViewById(R.id.project_introduce_content);
        TextView extruderContenText= (TextView) projectParticularsView.findViewById(R.id.extruder_conten_text);
        TextView addressContentTextView= (TextView) projectParticularsView.findViewById(R.id.address_content_text_view);

         if (!TextUtils.isEmpty(seekProjectBean.getTitle())){

             tailtText.setText(seekProjectBean.getTitle());
         }
        if (!TextUtils.isEmpty(seekProjectBean.getName())){

            phoneContentText.setText(seekProjectBean.getName());
        }
        if (!TextUtils.isEmpty(seekProjectBean.getCreateDateStr())){

            dateContentText.setText(seekProjectBean.getCreateDateStr());
        }
        if (!TextUtils.isEmpty(seekProjectBean.getProvince())&&!TextUtils.isEmpty(seekProjectBean.getCity())){

            addressContentText.setText(seekProjectBean.getProvince()+seekProjectBean.getCity());
        }
        if (!TextUtils.isEmpty(seekProjectBean.getProjectCompany())){

            companyContentText.setText(seekProjectBean.getProjectCompany());
        }
        if (!TextUtils.isEmpty(seekProjectBean.getTimeLimit())){

            workDateContentText.setText(seekProjectBean.getTimeLimit()+"个月");
        }
        if (!TextUtils.isEmpty(seekProjectBean.getWorkAmount())){

            gonchengContentText.setText(seekProjectBean.getWorkAmount()+"米");
        }
        if (!TextUtils.isEmpty(seekProjectBean.getDiameter())){

            diameterContentText.setText(seekProjectBean.getDiameter()+"米");
        }
        if (!TextUtils.isEmpty(seekProjectBean.getPileDepth())){

            pileContentText.setText(seekProjectBean.getPileDepth()+"米");
        }
        if (!TextUtils.isEmpty(seekProjectBean.getProjectRequirementTypeStr())){

            leixingContentText.setText(seekProjectBean.getProjectRequirementTypeStr());
        }
        if (!TextUtils.isEmpty(seekProjectBean.getProfile())){

            projectIntroduceContent.setText(seekProjectBean.getProfile());
        }
        if (!TextUtils.isEmpty(seekProjectBean.getDeviceRequirement())){

            extruderContenText.setText(seekProjectBean.getDeviceRequirement());
        }
        if (!TextUtils.isEmpty(seekProjectBean.getGeologicReport())){

            addressContentTextView.setText(seekProjectBean.getGeologicReport());
        }

         if (seekProjectBean.getIsCollection()==1){
             checkBoxCheck.setChecked(true);
         }

          if (seekProjectBean.getCanReply().equals("success")){
              competitiveButton.setText("我要竞标");
          }else if (seekProjectBean.getCanReply().equals("联系业主")){
              /*competitiveButton.setTextColor(getResources().getColor(R.color.tailt_dark));
              competitiveButton.setBackgroundResource(R.drawable.gray_button_corners);*/
              competitiveButton.setText(seekProjectBean.getCanReply());
          }else {
              competitiveButton.setTextColor(getResources().getColor(R.color.tailt_dark));
              competitiveButton.setBackgroundResource(R.drawable.gray_button_corners);
              competitiveButton.setText(seekProjectBean.getCanReply());
          }





    }

    private void initView() {
        shardText.setOnClickListener(this);
        titleNemeTv.setText("项目详情");
        retrunText.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);
        competitiveButton.setOnClickListener(this);
        checkBoxCheck.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        SQLhelper sqLhelper=new SQLhelper(SeekProjectParticularsActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        String states=null;  //用户星级

        while (cursor.moveToNext()) {
            uid=cursor.getString(0);
            states=cursor.getString(3);

        }
        switch (v.getId()){

            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;
            case R.id.shard_tv:
                showShare();

                break;
            case R.id.competitive_button:
                if (!TextUtils.isEmpty(uid)){
                    if (seekProjectBean.getIsCallOwnerFlag()==0){
                        if (seekProjectBean.getCanReply().equals("success")){
                            Intent intent=new Intent(SeekProjectParticularsActivity.this,CompetitiveDescribeActivity.class);
                            intent.putExtra("ProjectId",seekProjectId);
                            startActivity(intent);

                        }else {
                            MyAppliction.showToast(seekProjectBean.getCanReply());
                        }
                    }else {
                        if (!TextUtils.isEmpty(seekProjectBean.getPhone())){
                            //意图：打电话
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_DIAL);
                            //url:统一资源定位符
                            //uri:统一资源标示符（更广）
                            intent.setData(Uri.parse("tel:" + seekProjectBean.getPhone()));
                            //开启系统拨号器
                            startActivity(intent);
                        }else {
                            MyAppliction.showToast("该业主没有联系方式");
                        }

                    }

                }else {
                    Intent loginIntent=new Intent(SeekProjectParticularsActivity.this,LoginActivity.class);
                    startActivity(loginIntent);
                }



                break;




        }




    }

    private void isNoCheckedRequest(String uid,String sesstionId) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();

        if (!TextUtils.isEmpty(uid)){
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
            requestParams.addBodyParameter("Id",uid);
            requestParams.addBodyParameter("collectId",seekProjectId);
            requestParams.addBodyParameter("collectType","1");
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAttentionNoData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        AppDataBean appDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
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
        }







    }
    private void isCheckedRequest(String uid,String sesstionId) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();

        if (!TextUtils.isEmpty(uid)){
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
            requestParams.addBodyParameter("Id",uid);
            requestParams.addBodyParameter("collectId",seekProjectId);
            requestParams.addBodyParameter("collectType","1");
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAttentionData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        AppDataBean appDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
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


    private void initViewPager() {
       projectParticularsView=getLayoutInflater().inflate(R.layout.project_particulars_layout, null);
       matingFacilyView=getLayoutInflater().inflate(R.layout.mating_facility_layout, null);

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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SQLhelper sqLhelper=new SQLhelper(SeekProjectParticularsActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id

        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        if (!TextUtils.isEmpty(uid)){
            if (isChecked){
                if (seekProjectBean.getIsCollection()!=1){
                    isCheckedRequest(uid,sesstionId);
                }
            }else {
                isNoCheckedRequest(uid,sesstionId);
            }
        }else {
            Intent intent=new Intent(SeekProjectParticularsActivity.this,LoginActivity.class);
            startActivity(intent);

        }
    }
}
