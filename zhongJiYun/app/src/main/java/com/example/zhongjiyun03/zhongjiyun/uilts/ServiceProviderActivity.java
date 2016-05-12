package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider);
        ViewUtils.inject(this);
        inti();

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

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 判断GPS是否正常启动
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
            // 返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 0);
            return;
        }

        // 为获取地理位置信息时设置查询条件
        String bestProvider = lm.getBestProvider(getCriteria(), true);
        // 获取位置信息
        // 如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(bestProvider);
        updateView(location);
        // 监听状态
        lm.addGpsStatusListener(listener);
        // 绑定监听，有4个参数
        // 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
        // 参数2，位置信息更新周期，单位毫秒
        // 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
        // 参数4，监听
        // 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

        // 1秒更新一次，或最小位移变化超过1米更新一次；
        // 注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
    }


    // 位置监听
    private LocationListener locationListener = new LocationListener() {

        /**
         * 位置信息变化时触发
         */
        public void onLocationChanged(Location location) {
            updateView(location);
            Log.i(TAG, "时间：" + location.getTime());
            Log.i(TAG, "经度：" + location.getLongitude());
            Log.i(TAG, "纬度：" + location.getLatitude());
            Log.i(TAG, "海拔：" + location.getAltitude());
        }

        /**
         * GPS状态变化时触发
         */
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                // GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    Log.i(TAG, "当前GPS状态为可见状态");
                    break;
                // GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    Log.i(TAG, "当前GPS状态为服务区外状态");
                    break;
                // GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.i(TAG, "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        /**
         * GPS开启时触发
         */
        public void onProviderEnabled(String provider) {
            Location location = lm.getLastKnownLocation(provider);
            updateView(location);
        }

        /**
         * GPS禁用时触发
         */
        public void onProviderDisabled(String provider) {
            updateView(null);
        }

    };

    // 状态监听
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                // 第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Log.i(TAG, "第一次定位");
                    break;
                // 卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    Log.i(TAG, "卫星状态改变");
                    // 获取当前状态
                    GpsStatus gpsStatus = lm.getGpsStatus(null);
                    // 获取卫星颗数的默认最大值
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    // 创建一个迭代器保存所有卫星
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites()
                            .iterator();
                    int count = 0;
                    while (iters.hasNext() && count <= maxSatellites) {
                        GpsSatellite s = iters.next();
                        count++;
                    }
                    System.out.println("搜索到：" + count + "颗卫星");
                    break;
                // 定位启动
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.i(TAG, "定位启动");
                    break;
                // 定位结束
                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.e(TAG, "定位结束");
                    break;
            }
        };
    };

    /**
     * 实时更新文本内容
     *
     * @param location
     */
    private void updateView(Location location) {
        if (location != null) {
            Longitude=String.valueOf(location.getLongitude());
            Latitude=String.valueOf(location.getLatitude());
            //MyAppliction.showToast(String.valueOf(location.getLongitude())+"----"+String.valueOf(location.getLatitude()));

        } else {
            // 清空EditText对象
            //editText.getEditableText().clear();
            MyAppliction.showToast("获取地理位置失败");
        }
    }

    /**
     * 返回查询条件
     *
     * @return
     */
    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        // 设置是否需要方位信息
        criteria.setBearingRequired(false);
        // 设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    private void initData(int pageIndex) {
        HttpUtils httpUtils=new HttpUtils();
        final RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("PageIndex",pageIndex+"");
        requestParams.addBodyParameter("PageSize","10");
        requestParams.addBodyParameter("Longitude",Longitude);
        requestParams.addBodyParameter("Latitude",Latitude);
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
                Log.e("服务商数据",s);
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
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
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
            overridePendingTransition(R.anim.anim_open, R.anim.anim_close);

        }
        return true;
    }



}
