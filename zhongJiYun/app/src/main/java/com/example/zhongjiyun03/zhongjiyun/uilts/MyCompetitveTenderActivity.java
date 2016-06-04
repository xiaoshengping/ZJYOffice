package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.MyCompetitveTenderListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.main.ProjectlistBean;
import com.example.zhongjiyun03.zhongjiyun.bean.main.ProjectlistDataBean;
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

public class MyCompetitveTenderActivity extends AppCompatActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2<ListView> {

    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边

    @ViewInject(R.id.competitve_tender_lsitview)
    private PullToRefreshListView competitveTenderLsitview;
    private int pageIndex=1;
    List<ProjectlistDataBean> projectlistDataBeanLists;
    private boolean isPullDownRefresh=true; //判断是下拉，还是上拉的标记
    private MyCompetitveTenderListAdapter myCompetitveAdapter;
    @ViewInject(R.id.not_data_layout)
    private LinearLayout notDataLayout; //没有数据提示
    @ViewInject(R.id.network_remind_layout)
    private LinearLayout networkRemindLayout; //网络提示
    @ViewInject(R.id.not_data_image)
    private ImageView notDataImage; //没有网络和没有数据显示
    @ViewInject(R.id.not_data_text)
    private TextView notDataText;

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
        setContentView(R.layout.activity_my_competitve_tender);
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
        intiPullToRefresh();
        initListView();


    }

    @Override
    protected void onResume() {
        super.onResume();
        competitveTenderLsitview.setRefreshing();
        JPushInterface.onResume(this);
    }

    private void initListData(int pageIndex) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        SQLhelper sqLhelper=new SQLhelper(MyCompetitveTenderActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id

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
            requestParams.addBodyParameter("pageIndex",pageIndex+"");
            requestParams.addBodyParameter("pageSize","10");
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getCompetitvetListData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("我的竞标",responseInfo.result);
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        AppBean<ProjectlistBean> appBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<ProjectlistBean>>(){});
                        if (( appBean.getResult()).equals("success")){
                            ProjectlistBean projectlistBean=  appBean.getData();
                            if (projectlistBean!=null){
                                if (isPullDownRefresh){
                                    projectlistDataBeanLists.clear();
                                }
                                projectlistDataBeanLists.addAll(projectlistBean.getPagerData());
                                competitveTenderLsitview.onRefreshComplete();
                            }
                            notDataLayout.setVisibility(View.GONE);
                        }else if (( appBean.getResult()).equals("nomore")){
                            MyAppliction.showToast("已到最底了");
                            //notDataLayout.setVisibility(View.GONE);
                            competitveTenderLsitview.onRefreshComplete();
                        }else if ((appBean.getResult()).equals("empty")){
                            //secondHandBeen.clear();]
                            if (isPullDownRefresh){
                                projectlistDataBeanLists.clear();
                            }
                            notDataLayout.setVisibility(View.VISIBLE);
                            competitveTenderLsitview.onRefreshComplete();
                            //MyAppliction.showToast("没有更多数据");
                            notDataImage.setBackgroundResource(R.mipmap.no_project_icon);
                            notDataText.setText("您还没有竞标项目哦");
                        }else if (appBean.getResult().equals("unlogin")){
                            showExitGameAlertUnLonding("本次登录已过期");
                        }

                        myCompetitveAdapter.notifyDataSetChanged();
                    }else {
                        competitveTenderLsitview.onRefreshComplete();
                    }
                    networkRemindLayout.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.e("我的竞标",s);
                    networkRemindLayout.setVisibility(View.VISIBLE);
                    competitveTenderLsitview.onRefreshComplete();
                    if (projectlistDataBeanLists.size()==0){
                        notDataLayout.setVisibility(View.VISIBLE);
                        notDataImage.setBackgroundResource(R.mipmap.no_wifi_icon);
                        notDataText.setText("没有网络哦");
                    }
                }
            });
        }else {
            finish();
            MyAppliction.showToast("数据加载失败");
        }

    }

    private void initListView() {
        myCompetitveAdapter=new MyCompetitveTenderListAdapter(projectlistDataBeanLists,this);
        competitveTenderLsitview.setAdapter(myCompetitveAdapter);
        competitveTenderLsitview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MyCompetitveTenderActivity.this, SeekProjectParticularsActivity.class);
                intent.putExtra("seekProjectId",projectlistDataBeanLists.get(position-1).getProjectId());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });



    }

    public void intiPullToRefresh(){
        competitveTenderLsitview.setMode(PullToRefreshBase.Mode.BOTH);
        competitveTenderLsitview.setOnRefreshListener(this);
        ILoadingLayout endLabels  = competitveTenderLsitview
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = competitveTenderLsitview
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        competitveTenderLsitview.setRefreshing();

    }


    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("我的竞标");
        retrunText.setOnClickListener(this);
        projectlistDataBeanLists=new ArrayList<>();
        networkRemindLayout.setOnClickListener(this);


    }
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndex=1;
        isPullDownRefresh=true;
        initListData(pageIndex);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndex++;
        isPullDownRefresh=false;
        initListData(pageIndex);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

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
    //对话框
    private void showExitGameAlertUnLonding(String text) {
        final AlertDialog dlg = new AlertDialog.Builder(MyCompetitveTenderActivity.this).create();
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
                Intent intent=new Intent(MyCompetitveTenderActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                dlg.cancel();
            }
        });

        // 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText("暂不去");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
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
