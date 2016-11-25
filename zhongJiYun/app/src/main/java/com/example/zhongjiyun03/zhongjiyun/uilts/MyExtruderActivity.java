package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.HomeExtruderListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.AppDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.MyExtruderBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.MyExtruderDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.SQLHelperUtils;
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

/*
* 我的钻机
* */
public class MyExtruderActivity extends AppCompatActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2<ListView> {
       //列表
      @ViewInject(R.id.extruder_list_view)
      private PullToRefreshListView extruderListView;
      //添加钻机
      @ViewInject(R.id.register_tv)
      private TextView addExtruderTv;
      @ViewInject(R.id.title_name_tv)
      private TextView titleNemeTv;
      @ViewInject(R.id.retrun_text_view)
      private TextView retrunText;
      private int pageIndext=1;
      private List<MyExtruderBean> myExtruderBeens;
      private boolean isPullDownRefresh=true; //判断是下拉，还是上拉的标记
      private HomeExtruderListAdapter homeExtruderAdapter;
      @ViewInject(R.id.not_data_layout)
      private LinearLayout notDataLayout;
      @ViewInject(R.id.network_remind_layout)
      private LinearLayout networkRemindLayout;
      @ViewInject(R.id.not_data_image)
      private ImageView notDataImage; //没有网络和没有数据显示
      @ViewInject(R.id.not_data_text)
      private TextView notDataText;
      private SVProgressHUD mSVProgressHUD;//loding

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
        setContentView(R.layout.activity_my_extruder);
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
        mSVProgressHUD = new SVProgressHUD(this);
        myExtruderBeens=new ArrayList<>();
        addExtruderTv.setOnClickListener(this);
        Drawable img = getResources().getDrawable(R.mipmap.add_icon);
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        addExtruderTv.setCompoundDrawables(img, null, null, null);
        addExtruderTv.setCompoundDrawablePadding(10);
        addExtruderTv.setText("新增");
        titleNemeTv.setText("我的钻机");
        retrunText.setOnClickListener(this);
        networkRemindLayout.setOnClickListener(this);
        intiListView();
        intiPullToRefresh();

    }

    @Override
    protected void onResume() {
        super.onResume();
        pageIndext=1;
        extruderListView.setRefreshing();
        JPushInterface.onResume(this);

    }

    private void initData(int pageIndex) {

        HttpUtils httpUtils=new HttpUtils();
        final RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("PageIndex",pageIndex+"");
        requestParams.addBodyParameter("PageSize","10");
        requestParams.addBodyParameter("Id", SQLHelperUtils.queryId(MyExtruderActivity.this));
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        if (!TextUtils.isEmpty(sesstionId)){
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getMyExtruderListData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    //Log.e("我的钻机列表",responseInfo.result);
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        AppBean<MyExtruderDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<MyExtruderDataBean>>(){});
                        if ((appBean.getResult()).equals("success")){
                            MyExtruderDataBean myExtruderDataBean=appBean.getData();
                            List<MyExtruderBean> myExtruderBeen= myExtruderDataBean.getPagerData();
                            if (myExtruderBeen!=null){
                                if (isPullDownRefresh){
                                    myExtruderBeens.clear();
                                }
                                myExtruderBeens.addAll(myExtruderBeen);
                                homeExtruderAdapter.notifyDataSetChanged();
                                extruderListView.onRefreshComplete();

                            }else {
                                homeExtruderAdapter.notifyDataSetChanged();
                                extruderListView.onRefreshComplete();
                               // MyAppliction.showToast("您还没有添加钻机,请添加钻机");
                            }
                            notDataLayout.setVisibility(View.GONE);

                        }else if ((appBean.getResult()).equals("nomore")){
                            MyAppliction.showToast("已到最底了");
                            homeExtruderAdapter.notifyDataSetChanged();
                            extruderListView.onRefreshComplete();
                            //notDataLayout.setVisibility(View.GONE);
                        }else  if ((appBean.getResult()).equals("empty")){
                            //MyAppliction.showToast("没有更多数据");
                            if (isPullDownRefresh){
                                myExtruderBeens.clear();
                            }
                            homeExtruderAdapter.notifyDataSetChanged();
                            extruderListView.onRefreshComplete();
                            notDataLayout.setVisibility(View.VISIBLE);
                            notDataImage.setBackgroundResource(R.mipmap.no_rig_icon);
                            notDataText.setText("您还没有添加钻机哦");
                        }else if (appBean.getResult().equals("unlogin")){
                            //MyAppliction.showToast("登录已失效，请重新登陆");
                            showExitGameAlert("本次登录已过期");

                        }


                    }
                    networkRemindLayout.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    extruderListView.onRefreshComplete();
                    networkRemindLayout.setVisibility(View.VISIBLE);
                    Log.e("我的钻机列表",s);
                    if (myExtruderBeens.size()==0){
                        notDataLayout.setVisibility(View.VISIBLE);
                        notDataImage.setBackgroundResource(R.mipmap.no_wifi_icon);
                        notDataText.setText("没有网络哦");
                    }
                }
            });
        }else {
            MyAppliction.showToast("数据加载失败");
        }
        //mSVProgressHUD.showWithStatus("加载中...");



    }



    private void intiListView() {

        homeExtruderAdapter=new HomeExtruderListAdapter(myExtruderBeens,this,extruderListView);
        extruderListView.setAdapter(homeExtruderAdapter);
        ListView listView=extruderListView.getRefreshableView();
        extruderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MyExtruderActivity.this,MyExtruderParticularsActivity.class);
                intent.putExtra("myExtruderData",myExtruderBeens.get(position-1));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                showExitGameAlert("确定要删除钻机？",position-1);

                return true;
            }
        });


    }
    public void intiPullToRefresh(){
        extruderListView.setMode(PullToRefreshBase.Mode.BOTH);
        extruderListView.setOnRefreshListener(this);
        ILoadingLayout endLabels  = extruderListView
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚上拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 上来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = extruderListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        extruderListView.setRefreshing();

    }

    @Override
    public void onClick(View v) {
        SQLhelper sqLhelper=new SQLhelper(MyExtruderActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);
        }
        switch (v.getId()){
            case R.id.register_tv:
                if (!TextUtils.isEmpty(uid)){
                    Intent addExtruderIntent=new Intent(MyExtruderActivity.this,AddExtruderActivity.class);
                    startActivity(addExtruderIntent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }else {
                    Intent intent=new Intent(MyExtruderActivity.this,LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }

                break;
            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.network_remind_layout:
                //跳转到设置界面
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                break;



        }




    }



    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

        pageIndext=1;
        isPullDownRefresh=true;
        initData(pageIndext);


    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndext++;
        isPullDownRefresh=false;
        initData(pageIndext);


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
// KeyEvent.KEYCODE_BACK代表返回操作.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 处理返回操作.
            finish();
            //overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }
        return true;
    }
    //对话框
    private void showExitGameAlert(String text, final int position) {
        final AlertDialog dlg = new AlertDialog.Builder(MyExtruderActivity.this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.shrew_exit_dialog);
        TextView tailte = (TextView) window.findViewById(R.id.tailte_tv);
        tailte.setText(text);
        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        ok.setText("确定");
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isDeleteRequest(position);
                dlg.cancel();
            }
        });

        // 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText("取消");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }
    private void isDeleteRequest(int position) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        SQLhelper sqLhelper=new SQLhelper(MyExtruderActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid = null;
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        if (!TextUtils.isEmpty(uid)){
            //步骤1：创建一个SharedPreferences接口对象
            SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
            //步骤2：获取文件中的值
            String sesstionId = read.getString("code","");
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
            requestParams.addBodyParameter("Id",uid);
            requestParams.addBodyParameter("deviceId",myExtruderBeens.get(position).getId());

            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getDeleteExtruderDevice(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        //Log.e("hshshsh",responseInfo.result);
                        AppDataBean appDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
                        if (appDataBean.getResult().equals("success")){
                            mSVProgressHUD.showSuccessWithStatus("删除钻机成功");
                            extruderListView.setRefreshing();
                            homeExtruderAdapter.notifyDataSetChanged();
                        }else {
                            mSVProgressHUD.showErrorWithStatus(appDataBean.getMsg());
                        }
                    }

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.e("取消关注钻机",s);
                }
            });
        }

    }

    //对话框
    private void showExitGameAlert(String text) {
        final AlertDialog dlg = new AlertDialog.Builder(MyExtruderActivity.this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.shrew_exit_dialog);
        TextView tailte = (TextView) window.findViewById(R.id.tailte_tv);
        tailte.setText(text);
        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        ok.setText("去登录");
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(MyExtruderActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                SQLHelperUtils.deleteUid(MyExtruderActivity.this);
                dlg.cancel();
            }
        });

        // 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText("暂不去");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
                finish();
                SQLHelperUtils.deleteUid(MyExtruderActivity.this);
            }
        });
    }
}
