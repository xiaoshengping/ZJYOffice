package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.CommentListAdapter;
import com.example.zhongjiyun03.zhongjiyun.adapter.MyAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.main.CommentDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.main.CommentPagerDataBean;
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

public class CommentActivity extends AppCompatActivity implements View.OnClickListener{
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
    private  View mineCommentView; //我的评价
    private  View commentMineView; //评价我的
    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边
    private List<CommentPagerDataBean> mineCommentDataBeens=new ArrayList<>();
    private List<CommentPagerDataBean> commentMineDataBeens=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ViewUtils.inject(this);
        init();

    }

    private void init() {
        initView();
        initViewPager();
        initCommentMine();//评论我的
        initMineComment();//我的评论
        initMineCommentData(); //我的评论数据
        initCommentMineData(); //评论我的数据

    }

    private void initCommentMineData() {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        SQLhelper sqLhelper=new SQLhelper(CommentActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id

        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionID = read.getString("code","");
        if(!TextUtils.isEmpty(sesstionID)){


            if (!TextUtils.isEmpty(uid)){
                requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionID);
                requestParams.addBodyParameter("Id",uid);
                requestParams.addBodyParameter("PageIndex","1");
                requestParams.addBodyParameter("PageSize","10");
                httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getCommentMineData(),requestParams, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        if (!TextUtils.isEmpty(responseInfo.result)){
                            Log.e("我的评论",responseInfo.result);
                            if (!TextUtils.isEmpty(responseInfo.result)){
                                AppBean<CommentDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<CommentDataBean>>(){});
                                if (appBean.getResult().equals("success")){
                                    CommentDataBean commentDataBean= appBean.getData();
                                    if (commentDataBean!=null){
                                        List<CommentPagerDataBean> commentPagerDataBean=commentDataBean.getPagerData();
                                        if (commentPagerDataBean!=null){
                                            commentMineDataBeens.addAll(commentPagerDataBean);
                                        }

                                    }
                                }else if (appBean.getResult().equals("empty")){

                                    MyAppliction.showToast("还没有业主评论您");

                                }


                            }

                        }else {
                            MyAppliction.showToast("加载数据失败");
                        }

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Log.e("我的评论",s);
                    }
                });

            }
        }else {
            MyAppliction.showToast("加载数据失败");

        }


    }

    private void initMineCommentData() {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        SQLhelper sqLhelper=new SQLhelper(CommentActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id

        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionID = read.getString("code","");
        if(!TextUtils.isEmpty(sesstionID)){


        if (!TextUtils.isEmpty(uid)){
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionID);
            requestParams.addBodyParameter("Id",uid);
            requestParams.addBodyParameter("PageIndex","1");
            requestParams.addBodyParameter("PageSize","10");
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getMineCommentData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {

                    if (!TextUtils.isEmpty(responseInfo.result)){
                        Log.e("我的评论",responseInfo.result);
                        if (!TextUtils.isEmpty(responseInfo.result)){
                            AppBean<CommentDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<CommentDataBean>>(){});
                            if (appBean.getResult().equals("success")){
                                CommentDataBean commentDataBean= appBean.getData();
                                if (commentDataBean!=null){
                                    List<CommentPagerDataBean> commentPagerDataBean=commentDataBean.getPagerData();
                                    if (commentPagerDataBean!=null){
                                        mineCommentDataBeens.addAll(commentPagerDataBean);
                                    }

                                }
                            }else if (appBean.getResult().equals("empty")){
                                MyAppliction.showToast("你还没有评论业主");

                            }


                        }

                    }else {
                        MyAppliction.showToast("加载数据失败");
                    }

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.e("我的评论",s);
                }
            });

        }
        }else {
            MyAppliction.showToast("加载数据失败");

        }



    }

    //我的评论
    private void initMineComment() {

        ListView mineCommentListView= (ListView) mineCommentView.findViewById(R.id.mine_comment_listview);
        CommentListAdapter commentListAdapter=new CommentListAdapter(mineCommentDataBeens,CommentActivity.this);
        mineCommentListView.setAdapter(commentListAdapter);
        commentListAdapter.notifyDataSetChanged();



    }
    //评论我的
    private void initCommentMine() {

      ListView commentMineListView= (ListView) commentMineView.findViewById(R.id.comment_mine_listview);
        CommentListAdapter commentListAdapter=new CommentListAdapter(commentMineDataBeens,CommentActivity.this);
        commentMineListView.setAdapter(commentListAdapter);
        commentListAdapter.notifyDataSetChanged();



    }

    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("评价");
        retrunText.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;

        }
    }

    private void initViewPager() {
        mineCommentView=getLayoutInflater().inflate(R.layout.mine_comment_layout, null);
        commentMineView=getLayoutInflater().inflate(R.layout.comment_mine_layout, null);
        lists.add(mineCommentView);
        lists.add(commentMineView);

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
            // 处理返回操作.
            finish();
            overridePendingTransition(R.anim.anim_open, R.anim.anim_close);

        }
        return true;
    }

}
