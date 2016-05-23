package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.annotation.TargetApi;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import com.example.zhongjiyun03.zhongjiyun.adapter.HomeServicetListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.ServiceDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.ServiceProviderBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
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
*   服务商
* */

public class ServiceProviderActivity extends AppCompatActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2<ListView> {


    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边


    @ViewInject(R.id.service_provider_listview)
    private PullToRefreshListView serviceProviderListview; //列表
    private SVProgressHUD mSVProgressHUD;//loding


    private int pageIndext = 1;
    private List<ServiceProviderBean> serviceProviderBean;
    private HomeServicetListAdapter homeServiceListAdapter;
    private LocationManager lm;
    private static final String TAG = "GpsActivity";
    private String Longitude;
    private String Latitude;
    private boolean isPullDownRefresh=true; //判断是下拉，还是上拉的标记
    @ViewInject(R.id.not_data_layout)
    private LinearLayout notDataLayout;
    @ViewInject(R.id.network_remind_layout)
    private LinearLayout networkRemindLayout;
    @ViewInject(R.id.not_data_image)
    private ImageView notDataImage; //没有网络和没有数据显示
    @ViewInject(R.id.not_data_text)
    private TextView notDataText;

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
        setContentView(R.layout.activity_service_provider);
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
    //初始化
    private void inti() {

        initView();
        intiPullToRefresh();
        //initLocation();
    }

    private void initView() {
        networkRemindLayout.setOnClickListener(this);
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("配套服务");
        retrunText.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);
        serviceProviderBean = new ArrayList<>();
        intiListView();

    }

    private void initData(int pageIndex) {
        HttpUtils httpUtils=new HttpUtils();
        final RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("PageIndex",pageIndex+"");
        requestParams.addBodyParameter("PageSize","10");
        if (!TextUtils.isEmpty(MyAppliction.getLongitude())){
            requestParams.addBodyParameter("Longitude",MyAppliction.getLongitude());
        }
        if (!TextUtils.isEmpty(MyAppliction.getLatitude())){
            requestParams.addBodyParameter("Latitude",MyAppliction.getLatitude());
        }

        if (getIntent().getStringExtra("tage").equals("matingFacily")){
            String province=getIntent().getStringExtra("data");
            if (!TextUtils.isEmpty(province)){
                requestParams.addBodyParameter("province",province);
            }

        }
        //mSVProgressHUD.showWithStatus("加载中...");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getServiceListData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("服务商数据",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<ServiceDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<ServiceDataBean>>(){});
                     if ((appBean.getResult()).equals("success")){

                         ServiceDataBean serviceProvider=appBean.getData();
                         List<ServiceProviderBean>  serviceProviderBeans=  serviceProvider.getPagerData();
                         if (serviceProviderBean!=null){
                             if (isPullDownRefresh){
                                 serviceProviderBean.clear();
                             }
                             serviceProviderBean.addAll(serviceProviderBeans);
                         }
                         serviceProviderListview.onRefreshComplete();
                         notDataLayout.setVisibility(View.GONE);
                        // mSVProgressHUD.dismiss();
                     }else if ((appBean.getResult()).equals("nomore")){
                         MyAppliction.showToast("已到最底了");
                         serviceProviderListview.onRefreshComplete();
                         //notDataLayout.setVisibility(View.GONE);
                     }else if ((appBean.getResult()).equals("empty")){
                         //MyAppliction.showToast("没有更多数据");
                         serviceProviderListview.onRefreshComplete();
                         notDataLayout.setVisibility(View.VISIBLE);
                         notDataImage.setBackgroundResource(R.mipmap.no_services_icon);
                         notDataText.setText("还没有找到配套服务哦");

                     }
                    homeServiceListAdapter.notifyDataSetChanged();

                }
                networkRemindLayout.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                serviceProviderListview.onRefreshComplete();
                networkRemindLayout.setVisibility(View.VISIBLE);
                mSVProgressHUD.dismiss();
                //Log.e("服务商数据",s);
                if (serviceProviderBean.size()==0){
                    notDataLayout.setVisibility(View.VISIBLE);
                    notDataImage.setBackgroundResource(R.mipmap.no_wifi_icon);
                    notDataText.setText("没有网络哦");
                }
            }
        });


    }
    public void intiPullToRefresh(){
        serviceProviderListview.setMode(PullToRefreshBase.Mode.BOTH);
        serviceProviderListview.setOnRefreshListener(this);
        ILoadingLayout endLabels  = serviceProviderListview
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = serviceProviderListview
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        serviceProviderListview.setRefreshing();

    }

    private void intiListView() {

        homeServiceListAdapter=new HomeServicetListAdapter(serviceProviderBean,this);
        serviceProviderListview.setAdapter(homeServiceListAdapter);
        serviceProviderListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(ServiceProviderActivity.this,ServiceParticularsActivity.class);
                intent.putExtra("ServiceProviderId",serviceProviderBean.get(position-1).getId());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });

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
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);

        }
        return true;
    }



}
