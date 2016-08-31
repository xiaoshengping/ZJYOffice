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
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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
    private List<CommentPagerDataBean> mineCommentDataBeens;
    private List<CommentPagerDataBean> commentMineDataBeens;
    private int commentMinePageIndex=1;  //评论我的commentMinePageIndex
    private int mineCommentPageIndex=1;  //我的评论mineCommentPageIndex
    private PullToRefreshListView mineCommentListView;
    private PullToRefreshListView commentMineListView;
    private boolean isPullDownRefresh=true; //判断是下拉，还是上拉的标记
    private boolean isPullDownRefresh1=true; //判断是下拉，还是上拉的标记
    private  CommentListAdapter commentListAdapter;
    private  CommentListAdapter mineCommentListAdapter;

    private LinearLayout notDataLayout;
    private ImageView notDataImage; //没有网络和没有数据显示
    private TextView notDataText;   //没有网络文字提醒

    private LinearLayout commentNotDataLayout;
    private ImageView commentNotDataImage; //没有网络和没有数据显示
    private TextView commentNotDataText;   //没有网络文字提醒
    @ViewInject(R.id.network_remind_layout)
    private LinearLayout networkRemindLayout; //网络提示


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
        setContentView(R.layout.activity_comment);
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
        initView();
        initViewPager();
        initCommentMine();//评论我的
        initMineComment();//我的评论



    }

    private void initMineCommentData(int PageIndex) {
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
                requestParams.addBodyParameter("PageIndex",PageIndex+"");
                requestParams.addBodyParameter("PageSize","10");
                httpUtils.send(HttpRequest.HttpMethod.POST,AppUtilsUrl.getCommentMineData() ,requestParams, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        if (!TextUtils.isEmpty(responseInfo.result)){
                            //Log.e("我的评论",responseInfo.result);
                            if (!TextUtils.isEmpty(responseInfo.result)){
                                AppBean<CommentDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<CommentDataBean>>(){});
                                if (appBean.getResult().equals("success")){
                                    CommentDataBean commentDataBean= appBean.getData();
                                    if (commentDataBean!=null){
                                        List<CommentPagerDataBean> commentPagerDataBean=commentDataBean.getPagerData();
                                        if (commentPagerDataBean!=null){
                                            if (isPullDownRefresh){
                                                mineCommentDataBeens.clear();
                                            }
                                            mineCommentDataBeens.addAll(commentPagerDataBean);
                                        }

                                    }
                                    commentListAdapter.notifyDataSetChanged();
                                    mineCommentListView.onRefreshComplete();
                                    notDataLayout.setVisibility(View.GONE);
                                }else if (appBean.getResult().equals("empty")){
                                    //MyAppliction.showToast("没有更多评论");
                                    notDataLayout.setVisibility(View.VISIBLE);
                                    mineCommentListView.onRefreshComplete();
                                    notDataImage.setBackgroundResource(R.mipmap.no_eval_icon);
                                    notDataText.setText("还没有业主评价您哦");

                                }else if ((appBean.getResult()).equals("nomore")){
                                    MyAppliction.showToast("已到最底了");
                                    //notDataLayout.setVisibility(View.GONE);
                                    mineCommentListView.onRefreshComplete();
                                }


                            }
                            mineCommentListView.onRefreshComplete();
                        }else {
                            MyAppliction.showToast("加载数据失败");
                            mineCommentListView.onRefreshComplete();
                        }


                        networkRemindLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Log.e("我的评论",s);
                        networkRemindLayout.setVisibility(View.VISIBLE);
                        mineCommentListView.onRefreshComplete();
                        if (mineCommentDataBeens.size()==0){
                            notDataLayout.setVisibility(View.VISIBLE);
                            notDataImage.setBackgroundResource(R.mipmap.no_wifi_icon);
                            notDataText.setText("没有网络哦");
                        }
                    }
                });

            }
        }else {
            MyAppliction.showToast("加载数据失败");

        }




    }

    private void initCommentMineData(int PageIndex) {
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
                requestParams.addBodyParameter("PageIndex",PageIndex+"");
                requestParams.addBodyParameter("PageSize","10");
                httpUtils.send(HttpRequest.HttpMethod.POST,AppUtilsUrl.getMineCommentData() ,requestParams, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        if (!TextUtils.isEmpty(responseInfo.result)){
                            //Log.e("我的评论",responseInfo.result);
                            if (!TextUtils.isEmpty(responseInfo.result)){
                                AppBean<CommentDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<CommentDataBean>>(){});
                                if (appBean.getResult().equals("success")){
                                    CommentDataBean commentDataBean= appBean.getData();
                                    if (commentDataBean!=null){
                                        List<CommentPagerDataBean> commentPagerDataBean=commentDataBean.getPagerData();
                                        if (commentPagerDataBean!=null){
                                            if (isPullDownRefresh1){
                                                commentMineDataBeens.clear();
                                            }
                                            commentMineDataBeens.addAll(commentPagerDataBean);
                                        }

                                    }
                                    commentNotDataLayout.setVisibility(View.GONE);
                                }else if (appBean.getResult().equals("empty")){
                                    //MyAppliction.showToast("没有更多评论");
                                    commentNotDataLayout.setVisibility(View.VISIBLE);
                                    commentMineListView.onRefreshComplete();
                                    commentNotDataImage.setBackgroundResource(R.mipmap.no_eval_icon);
                                    commentNotDataText.setText("您还没有评价哦");

                                }else if ((appBean.getResult()).equals("nomore")){
                                    MyAppliction.showToast("已到最底了");
                                    //commentNotDataLayout.setVisibility(View.GONE);
                                    commentMineListView.onRefreshComplete();
                                }


                            }
                            mineCommentListAdapter.notifyDataSetChanged();
                            commentMineListView.onRefreshComplete();

                        }else {
                            commentMineListView.onRefreshComplete();
                            MyAppliction.showToast("加载数据失败");
                        }

                        networkRemindLayout.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Log.e("我的评论",s);
                        networkRemindLayout.setVisibility(View.VISIBLE);
                        commentMineListView.onRefreshComplete();
                        if (commentMineDataBeens.size()==0){
                            commentNotDataLayout.setVisibility(View.VISIBLE);
                            commentNotDataImage.setBackgroundResource(R.mipmap.no_wifi_icon);
                            commentNotDataText.setText("没有网络哦");
                        }
                    }
                });

            }
        }else {
            MyAppliction.showToast("加载数据失败");

        }



    }

    //我的评论
    private void initMineComment() {

       mineCommentListView= (PullToRefreshListView) mineCommentView.findViewById(R.id.mine_comment_listview);
       commentListAdapter=new CommentListAdapter(mineCommentDataBeens,CommentActivity.this);
        mineCommentListView.setAdapter(commentListAdapter);

        mineCommentListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                mineCommentPageIndex=1;
                isPullDownRefresh=true;
                initMineCommentData(mineCommentPageIndex); //我的评论数据
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mineCommentPageIndex++;
                isPullDownRefresh=false;
                initMineCommentData(mineCommentPageIndex); //我的评论数据
            }
        });
        mineCommentListView.setMode(PullToRefreshBase.Mode.BOTH);

        ILoadingLayout endLabels  = mineCommentListView
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚上拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 上来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = mineCommentListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        mineCommentListView.setRefreshing();

    }
    //评论我的
    private void initCommentMine() {
        commentMineListView= (PullToRefreshListView) commentMineView.findViewById(R.id.comment_mine_listview);

        mineCommentListAdapter=new CommentListAdapter(commentMineDataBeens,CommentActivity.this);
        commentMineListView.setAdapter(mineCommentListAdapter);
        commentMineListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                commentMinePageIndex=1;
                isPullDownRefresh1=true;
                initCommentMineData(commentMinePageIndex); //评论我的数据
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                commentMinePageIndex++;
                isPullDownRefresh1=false;
                initCommentMineData(commentMinePageIndex); //评论我的数据
            }
        });
        commentMineListView.setMode(PullToRefreshBase.Mode.BOTH);

        ILoadingLayout endLabels  = commentMineListView
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚上拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 上来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = commentMineListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        commentMineListView.setRefreshing();
    }

    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("评价");
        retrunText.setOnClickListener(this);
        mineCommentDataBeens=new ArrayList<>();
        commentMineDataBeens=new ArrayList<>();
        networkRemindLayout.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.network_remind_layout:
                //跳转到设置界面
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
        }
    }

    private void initViewPager() {
        mineCommentView=getLayoutInflater().inflate(R.layout.mine_comment_layout, null);
        commentMineView=getLayoutInflater().inflate(R.layout.comment_mine_layout, null);
        lists.add(mineCommentView);
        lists.add(commentMineView);
        notDataLayout= (LinearLayout) mineCommentView.findViewById(R.id.not_data_layout);
        notDataImage= (ImageView) mineCommentView.findViewById(R.id.not_data_image);
        notDataText= (TextView) mineCommentView.findViewById(R.id.not_data_text);
        commentNotDataLayout= (LinearLayout) commentMineView.findViewById(R.id.comment_not_data_layout);
        commentNotDataImage= (ImageView) commentMineView.findViewById(R.id.comment_not_data_image);
        commentNotDataText= (TextView) commentMineView.findViewById(R.id.comment_not_data_text);
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
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);

        }
        return true;
    }

}
