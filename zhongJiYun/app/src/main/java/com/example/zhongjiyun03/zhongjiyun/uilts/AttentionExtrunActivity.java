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
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.AttentionSecondHandListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.AppDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.AttentionSecondHandDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.SecondHandBean;
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

public class AttentionExtrunActivity extends AppCompatActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2<ListView> {


    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边


    @ViewInject(R.id.attention_extrun_lsitview)
    private PullToRefreshListView attentionExtrunLsitview;
    private int pageIndex=1;
    private List<SecondHandBean> secondHandBeens; //列表数据
    private boolean isPullDownRefresh=true; //判断是下拉，还是上拉的标记
    private AttentionSecondHandListAdapter homeSecondHandListAdapter;
    @ViewInject(R.id.not_data_layout)
    private LinearLayout notDataLayout;
    private SVProgressHUD mSVProgressHUD;//loding
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.red_light);//通知栏所需颜色
        }
        setContentView(R.layout.activity_attention_extrun);
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
        initView();
        initListView();
        intiPullToRefresh();


    }

    private void initView() {
        secondHandBeens=new ArrayList<>();
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("关注的钻机");
        retrunText.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);
        networkRemindLayout.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        attentionExtrunLsitview.setRefreshing();
        JPushInterface.onResume(this);
    }

    private void intiListData(int pageIndex) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        SQLhelper sqLhelper=new SQLhelper(AttentionExtrunActivity.this);
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
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAttentionExtrunListData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    //Log.e("关注钻机",responseInfo.result);
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        AppBean<AttentionSecondHandDataBean> appListDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<AttentionSecondHandDataBean>>(){});
                        if ((appListDataBean.getResult()).equals("success")){
                            AttentionSecondHandDataBean attentionSecondHandDataBean=  appListDataBean.getData();

                            if (attentionSecondHandDataBean!=null){
                                List<SecondHandBean> secondHandBeen=  attentionSecondHandDataBean.getPagerData();
                               if (secondHandBeen!=null){
                                   if (isPullDownRefresh){
                                       secondHandBeens.clear();
                                   }
                                   secondHandBeens.addAll(secondHandBeen);
                               }
                                attentionExtrunLsitview.onRefreshComplete();
                                homeSecondHandListAdapter.notifyDataSetChanged();
                            }
                            notDataLayout.setVisibility(View.GONE);
                        }else if ((appListDataBean.getResult()).equals("nomore")){
                            attentionExtrunLsitview.onRefreshComplete();
                            MyAppliction.showToast("已到最底了");
                            homeSecondHandListAdapter.notifyDataSetChanged();
                        }else if ((appListDataBean.getResult()).equals("empty")){
                            if (isPullDownRefresh){
                                secondHandBeens.clear();
                            }
                            notDataLayout.setVisibility(View.VISIBLE);
                            attentionExtrunLsitview.onRefreshComplete();
                            homeSecondHandListAdapter.notifyDataSetChanged();
                            notDataImage.setBackgroundResource(R.mipmap.no_rig_icon);
                            notDataText.setText("您还没有关注的二手钻机哦");
                        }else if (appListDataBean.getResult().equals("unlogin")){
                            showExitGameAlertUnLonding("本次登录已过期");
                        }



                    }
                    networkRemindLayout.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.e("关注钻机",s);
                    networkRemindLayout.setVisibility(View.VISIBLE);
                    attentionExtrunLsitview.onRefreshComplete();
                    if (secondHandBeens.size()==0){
                        notDataLayout.setVisibility(View.VISIBLE);
                        notDataImage.setBackgroundResource(R.mipmap.no_wifi_icon);
                        notDataText.setText("没有网络哦");
                    }
                }
            });
        }else {
            MyAppliction.showToast("加载数据失败");
        }




    }

    public void intiPullToRefresh(){
        attentionExtrunLsitview.setMode(PullToRefreshBase.Mode.BOTH);
        attentionExtrunLsitview.setOnRefreshListener(this);
        ILoadingLayout endLabels  = attentionExtrunLsitview
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚上拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 上来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = attentionExtrunLsitview
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        attentionExtrunLsitview.setRefreshing();

    }
    private void initListView() {

        homeSecondHandListAdapter = new AttentionSecondHandListAdapter(secondHandBeens, this);
        attentionExtrunLsitview.setAdapter(homeSecondHandListAdapter);
        homeSecondHandListAdapter.notifyDataSetChanged();
        ListView listView=attentionExtrunLsitview.getRefreshableView();
        attentionExtrunLsitview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(AttentionExtrunActivity.this,ExturderParticularsActivity.class);
                intent.putExtra("secondHandData",secondHandBeens.get(position-1).getId());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                showExitGameAlert("确定要取消关注？",position-1);

                return true;
            }
        });


    }
    //对话框
    private void showExitGameAlert(String text, final int position) {
        final AlertDialog dlg = new AlertDialog.Builder(AttentionExtrunActivity.this).create();
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
                isNoCheckedRequest(position);
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
    private void isNoCheckedRequest(int position) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        SQLhelper sqLhelper=new SQLhelper(AttentionExtrunActivity.this);
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
            //Log.e("collectId",position+"");
            requestParams.addBodyParameter("collectId",secondHandBeens.get(position).getId());
            requestParams.addBodyParameter("collectType","5");
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAttentionNoData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        AppDataBean appDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
                        if (appDataBean.getResult().equals("success")){
                            mSVProgressHUD.showSuccessWithStatus("取消成功");
                            attentionExtrunLsitview.setRefreshing();
                            homeSecondHandListAdapter.notifyDataSetChanged();
                        }else {
                            mSVProgressHUD.showErrorWithStatus("取消失败");
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


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

        pageIndex=1;
        isPullDownRefresh=true;
        intiListData(pageIndex);


    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndex++;
        isPullDownRefresh=false;
        intiListData(pageIndex);
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
                break;



        }

    }


    //对话框
    private void showExitGameAlertUnLonding(String text) {
        final AlertDialog dlg = new AlertDialog.Builder(AttentionExtrunActivity.this).create();
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
                Intent intent=new Intent(AttentionExtrunActivity.this,LoginActivity.class);
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
