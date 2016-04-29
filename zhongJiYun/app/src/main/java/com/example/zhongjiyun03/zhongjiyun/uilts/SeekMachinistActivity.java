package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.SeekMachinistListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.SeekMachinisBean;
import com.example.zhongjiyun03.zhongjiyun.bean.SekkMachinisDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.AppListDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.FacillyChildsBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.FacillyDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityChildsBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.SelectData;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.popwin.FacillyFirstClassAdapter;
import com.example.zhongjiyun03.zhongjiyun.popwin.FacillySecondClassAdapter;
import com.example.zhongjiyun03.zhongjiyun.popwin.FirstClassAdapter;
import com.example.zhongjiyun03.zhongjiyun.popwin.PopupWindowHelper;
import com.example.zhongjiyun03.zhongjiyun.popwin.ScreenUtils;
import com.example.zhongjiyun03.zhongjiyun.popwin.SecondClassAdapter;
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

public class SeekMachinistActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener {
    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边

    //排序
    @ViewInject(R.id.time_text_view)
    private TextView sortButton; //排序选项
    PopupWindow popupWindowTime;
    private List<String> list;
    int cur_pos = -1;// 当前显示的一行
    int popTag = 1;

    //项目进展
    @ViewInject(R.id.evolve_text_view)
    private TextView evolveButton;
    int cur_evolve_pos = -1;// 当前显示的一行

    //地址选择
    private TextView mainTab1TV;
    /**
     * 左侧一级分类的数据
     */
    private List<ProvinceCityBean> firstList;
    /**
     * 右侧二级分类的数据
     */
    private List<ProvinceCityChildsBean> secondList;

    /**
     * 使用PopupWindow显示一级分类和二级分类
     */
    private PopupWindow popupWindow;

    /**
     * 左侧和右侧两个ListView
     */
    private ListView leftLV, rightLV;
    //弹出PopupWindow时背景变暗
    private View darkView;

    //弹出PopupWindow时，背景变暗的动画
    private Animation animIn, animOut;


    //设备产商
    private PopupWindow popupWindowFacilly;
    /**
     * 左侧和右侧两个ListView
     */
    private ListView facillyLeftLV, facillyRightLV;
    @ViewInject(R.id.facilly_text_view)
    private TextView facillyText;
    /**左侧一级分类的数据*/
    // private List<FacillyDataBean> facilluyFirstList=new ArrayList<>();
    /**
     * 右侧二级分类的数据
     */
    private List<FacillyChildsBean> facillySecondList;
    private String type; //型号
    private String city; //城市
    private String year;
    private String order;


    @ViewInject(R.id.project_list_view)
    private ListView projectListView;
    private SeekMachinistListAdapter homeServiceListAdapter;


    @ViewInject(R.id.seek_machinist_listview)
    private PullToRefreshListView seekMachinistListview;  //列表
    private int pageIndex = 1;
    private List<SekkMachinisDataBean> sekkMachinisDataBeens;
    private LocationManager lm;
    private static final String TAG = "GpsActivity";
    private String Longitude;   //经度
    private String Latitude;    //纬度
    private boolean isPullDownRefresh = true; //判断是下拉，还是上拉的标记


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_machinist);
        ViewUtils.inject(this);
        PopupWindowHelper.init(this);
        init();

    }

    private void init() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("寻找机手");
        retrunText.setOnClickListener(this);
        sekkMachinisDataBeens = new ArrayList<>();
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSortPopupWindow(sortButton);
                popTag = 1;

            }
        });
        evolveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showEvolvePopupWindow(evolveButton);
                popTag = 2;
            }
        });

        findView();
        initData();
        initPopup();

        OnClickListenerImpl l = new OnClickListenerImpl();
        mainTab1TV.setOnClickListener(l);
        initListView();
        intiPullToRefresh();

        facillyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabFacillynClick();
            }
        });
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

    private void initListData(int pageIndex, String type, String city, String year, String order) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("Id", "019f64b5-b05e-4996-9d9c-572cfb8fa3bd");
        requestParams.addBodyParameter("pageIndex", pageIndex + "");
        requestParams.addBodyParameter("pageSize", "10");
        if (!TextUtils.isEmpty(type)) {
            requestParams.addBodyParameter("type", type);
        }
        if (!TextUtils.isEmpty(city)) {
            if (city.equals("全部")) {

            } else {
                requestParams.addBodyParameter("city", city);
            }

        }
        if (!TextUtils.isEmpty(year)) {
            requestParams.addBodyParameter("year", year);
        }
        if (!TextUtils.isEmpty(order)) {
            requestParams.addBodyParameter("order", order);
        }
        if (!TextUtils.isEmpty(Latitude)) {
            requestParams.addBodyParameter("latitude", Latitude);
        }
        if (!TextUtils.isEmpty(Longitude)) {
            requestParams.addBodyParameter("longitude", Longitude);
        }

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getMachinisData(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Log.e("找机手", responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)) {

                    AppBean<SeekMachinisBean> appListDataBean = JSONObject.parseObject(responseInfo.result, new TypeReference<AppBean<SeekMachinisBean>>() {
                    });
                    if ((appListDataBean.getResult()).equals("success")) {
                        SeekMachinisBean seekMachinisBean = appListDataBean.getData();
                        List<SekkMachinisDataBean> sekkMachinisDataBeen = seekMachinisBean.getPagerData();
                        if (isPullDownRefresh) {
                            sekkMachinisDataBeens.clear();
                        }
                        sekkMachinisDataBeens.addAll(sekkMachinisDataBeen);

                        homeServiceListAdapter.notifyDataSetChanged();
                        seekMachinistListview.onRefreshComplete();

                    } else if ((appListDataBean.getResult()).equals("nomore")) {
                        homeServiceListAdapter.notifyDataSetChanged();
                        seekMachinistListview.onRefreshComplete();
                        MyAppliction.showToast("已到最底了");
                    } else if ((appListDataBean.getResult()).equals("empty")) {
                        homeServiceListAdapter.notifyDataSetChanged();
                        seekMachinistListview.onRefreshComplete();
                        MyAppliction.showToast("没有更多数据");
                    }


                } else {
                    homeServiceListAdapter.notifyDataSetChanged();
                    seekMachinistListview.onRefreshComplete();
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("找机手", s);
                seekMachinistListview.onRefreshComplete();
            }
        });


    }

    private void initListView() {
        homeServiceListAdapter = new SeekMachinistListAdapter(sekkMachinisDataBeens, SeekMachinistActivity.this);
        seekMachinistListview.setAdapter(homeServiceListAdapter);
        seekMachinistListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SeekMachinistActivity.this, SeekMachinistParticulasActivity.class);
                intent.putExtra("seekData", sekkMachinisDataBeens.get(position - 1));
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                startActivity(intent);
            }
        });


    }


    public void intiPullToRefresh() {
        seekMachinistListview.setMode(PullToRefreshBase.Mode.BOTH);
        seekMachinistListview.setOnRefreshListener(this);
        ILoadingLayout endLabels = seekMachinistListview
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels = seekMachinistListview
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        seekMachinistListview.setRefreshing();

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndex = 1;
        isPullDownRefresh = true;
        initListData(pageIndex, type, city, year, order);  //列表数据


    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndex++;
        isPullDownRefresh = false;
        initListData(pageIndex, type, city, year, order);  //列表数据

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
        }

        ;
    };

    /**
     * 实时更新文本内容
     *
     * @param location
     */
    private void updateView(Location location) {
        if (location != null) {
            Longitude = String.valueOf(location.getLongitude());
            Latitude = String.valueOf(location.getLatitude());
            //MyAppliction.showToast(String.valueOf(location.getLongitude())+"----"+String.valueOf(location.getLatitude()));

        } else {
            // 清空EditText对象
            //editText.getEditableText().clear();
            MyAppliction.showToast("定位失败");
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


    private void findView() {
        mainTab1TV = (TextView) findViewById(R.id.main_tab1);
        darkView =findViewById(R.id.main_darkview);

        animIn = AnimationUtils.loadAnimation(SeekMachinistActivity.this, R.anim.fade_in_anim);
        animOut = AnimationUtils.loadAnimation(SeekMachinistActivity.this, R.anim.fade_out_anim);
    }

    private void initData() {
       /* firstList = new ArrayList<FirstClassItem>();
        //1
        ArrayList<SecondClassItem> secondList1 = new ArrayList<SecondClassItem>();
        secondList1.add(new SecondClassItem(101, "自助餐"));
        secondList1.add(new SecondClassItem(102, "西餐"));
        secondList1.add(new SecondClassItem(103, "川菜"));
        firstList.add(new FirstClassItem(1, "美食", secondList1));
        //2
        ArrayList<SecondClassItem> secondList2 = new ArrayList<SecondClassItem>();
        secondList2.add(new SecondClassItem(201, "天津"));
        secondList2.add(new SecondClassItem(202, "北京"));
        secondList2.add(new SecondClassItem(203, "秦皇岛"));
        secondList2.add(new SecondClassItem(204, "沈阳"));
        secondList2.add(new SecondClassItem(205, "大连"));
        secondList2.add(new SecondClassItem(206, "哈尔滨"));
        secondList2.add(new SecondClassItem(207, "锦州"));
        secondList2.add(new SecondClassItem(208, "上海"));
        secondList2.add(new SecondClassItem(209, "杭州"));
        secondList2.add(new SecondClassItem(210, "南京"));
        secondList2.add(new SecondClassItem(211, "嘉兴"));
        secondList2.add(new SecondClassItem(212, "苏州"));
        firstList.add(new FirstClassItem(2, "旅游", secondList2));
        //3
        ArrayList<SecondClassItem> secondList3 = new ArrayList<SecondClassItem>();
        secondList3.add(new SecondClassItem(301, "南开区"));
        secondList3.add(new SecondClassItem(302, "和平区"));
        secondList3.add(new SecondClassItem(303, "河西区"));
        secondList3.add(new SecondClassItem(304, "河东区"));
        secondList3.add(new SecondClassItem(305, "滨海新区"));
        firstList.add(new FirstClassItem(3, "电影", secondList3));
        //4
        firstList.add(new FirstClassItem(4, "手机", new ArrayList<SecondClassItem>()));
        //5
        firstList.add(new FirstClassItem(5, "娱乐", null));

        //copy
        firstList.addAll(firstList);*/
        //城市数据
        ProvinceCityDataBean provinceCityDataBean = JSONObject.parseObject(SelectData.selectCityData + SelectData.selectCityDataOne + SelectData.selectCityDataTwo, new TypeReference<ProvinceCityDataBean>() {
        });
        if (provinceCityDataBean != null) {
            firstList = provinceCityDataBean.getProvinceCity();


        }

        //设备厂商
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("DeviceJsonType", "2");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getFacillyData(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("设备厂商",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)) {
                    AppListDataBean<FacillyDataBean> appListDataBean = JSONObject.parseObject(responseInfo.result, new TypeReference<AppListDataBean<FacillyDataBean>>() {
                    });
                    if (appListDataBean.getResult().equals("success")) {
                        List<FacillyDataBean> facillyDataBeen = appListDataBean.getData();
                        //facilluyFirstList.addAll(facillyDataBeen);
                        initPopupFacilly(facillyDataBeen);

                    }


                }


            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


    }


    private void initPopupFacilly(final List<FacillyDataBean> facilluyFirstList) {
        popupWindowFacilly = new PopupWindow(SeekMachinistActivity.this);
        View view = LayoutInflater.from(SeekMachinistActivity.this).inflate(R.layout.facilly_popup_layout, null);
        facillyLeftLV = (ListView) view.findViewById(R.id.facilly_pop_listview_left);
        facillyRightLV = (ListView) view.findViewById(R.id.facilly_pop_listview_right);

        popupWindowFacilly.setContentView(view);
        popupWindowFacilly.setBackgroundDrawable(new PaintDrawable());
        popupWindowFacilly.setFocusable(true);

        popupWindowFacilly.setHeight(ScreenUtils.getScreenH(SeekMachinistActivity.this) * 2 / 3);
        popupWindowFacilly.setWidth(ScreenUtils.getScreenW(SeekMachinistActivity.this));

        popupWindowFacilly.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkView.startAnimation(animOut);
                darkView.setVisibility(View.GONE);

                facillyLeftLV.setSelection(0);
                facillyRightLV.setSelection(0);
            }
        });


        //为了方便扩展，这里的两个ListView均使用BaseAdapter.如果分类名称只显示一个字符串，建议改为ArrayAdapter.
        //加载一级分类
        final FacillyFirstClassAdapter firstAdapter = new FacillyFirstClassAdapter(SeekMachinistActivity.this, facilluyFirstList);
        facillyLeftLV.setAdapter(firstAdapter);

        //加载左侧第一行对应右侧二级分类
        facillySecondList = new ArrayList<FacillyChildsBean>();
        facillySecondList.addAll(facilluyFirstList.get(0).getChilds());
        final FacillySecondClassAdapter secondAdapter = new FacillySecondClassAdapter(SeekMachinistActivity.this, facillySecondList);
        facillyRightLV.setAdapter(secondAdapter);

        //左侧ListView点击事件
        facillyLeftLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //二级数据
                List<FacillyChildsBean> list2 = facilluyFirstList.get(position).getChilds();
                //如果没有二级类，则直接跳转
                if (list2 == null || list2.size() == 0) {
                    popupWindowFacilly.dismiss();

                    String firstId = facilluyFirstList.get(position).getValue();
                    String selectedName = facilluyFirstList.get(position).getText();
                    facillyHandleResult(firstId, "-1", selectedName);
                    return;
                }

                FacillyFirstClassAdapter adapter = (FacillyFirstClassAdapter) (parent.getAdapter());
                //如果上次点击的就是这一个item，则不进行任何操作
                if (adapter.getSelectedPosition() == position) {
                    return;
                }

                //根据左侧一级分类选中情况，更新背景色
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();

                //显示右侧二级分类
                facillyUpdateSecondListView(list2, secondAdapter);
            }
        });

        //右侧ListView点击事件
        facillyRightLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //关闭popupWindow，显示用户选择的分类
                popupWindowFacilly.dismiss();

                int firstPosition = firstAdapter.getSelectedPosition();
                String firstId = facilluyFirstList.get(firstPosition).getValue();
                String secondId = facilluyFirstList.get(firstPosition).getChilds().get(position).getValue();
                String selectedName = facilluyFirstList.get(firstPosition).getChilds().get(position)
                        .getText();
                facillyHandleResult(firstId, secondId, selectedName);
                seekMachinistListview.setRefreshing();
            }
        });
    }

    //顶部第一个标签的点击事件
    private void tabFacillynClick() {
        if (popupWindowFacilly.isShowing()) {
            popupWindowFacilly.dismiss();
        } else {
            popupWindowFacilly.showAsDropDown(SeekMachinistActivity.this.findViewById(R.id.main_div_line));
            popupWindowFacilly.setAnimationStyle(-1);
            //背景变暗
            darkView.startAnimation(animIn);
            darkView.setVisibility(View.VISIBLE);
        }
    }

    //刷新右侧ListView
    private void facillyUpdateSecondListView(List<FacillyChildsBean> list2,
                                             FacillySecondClassAdapter secondAdapter) {
        facillySecondList.clear();
        facillySecondList.addAll(list2);
        secondAdapter.notifyDataSetChanged();
    }

    //处理点击结果
    private void facillyHandleResult(String firstId, String secondId, String selectedName) {
        String text = "first id:" + firstId + ",second id:" + secondId;
        //Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();

        facillyText.setText(selectedName);
        facillyText.setTextColor(getResources().getColor(R.color.red_light));
        Drawable img = getResources().getDrawable(R.mipmap.select_arrow_cur);
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        facillyText.setCompoundDrawables(null, null, img, null);
        type = selectedName;
        sekkMachinisDataBeens.clear();
        seekMachinistListview.setRefreshing();
        initListData(pageIndex, type, city, year, order);

    }


    //点击事件
    class OnClickListenerImpl implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.main_tab1:
                    tab1OnClick();
                    break;
                default:
                    break;
            }
        }
    }

    private void initPopup() {
        popupWindow = new PopupWindow(SeekMachinistActivity.this);
        View view = LayoutInflater.from(SeekMachinistActivity.this).inflate(R.layout.popup_layout, null);
        leftLV = (ListView) view.findViewById(R.id.pop_listview_left);
        rightLV = (ListView) view.findViewById(R.id.pop_listview_right);

        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new PaintDrawable());
        popupWindow.setFocusable(true);

        popupWindow.setHeight(ScreenUtils.getScreenH(SeekMachinistActivity.this) * 2 / 3);
        popupWindow.setWidth(ScreenUtils.getScreenW(SeekMachinistActivity.this));

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkView.startAnimation(animOut);
                darkView.setVisibility(View.GONE);

                leftLV.setSelection(0);
                rightLV.setSelection(0);
            }
        });


        //为了方便扩展，这里的两个ListView均使用BaseAdapter.如果分类名称只显示一个字符串，建议改为ArrayAdapter.
        //加载一级分类
        final FirstClassAdapter firstAdapter = new FirstClassAdapter(SeekMachinistActivity.this, firstList);
        leftLV.setAdapter(firstAdapter);

        //加载左侧第一行对应右侧二级分类
        secondList = new ArrayList<ProvinceCityChildsBean>();
        secondList.addAll(firstList.get(0).getProvinceCityChilds());
        final SecondClassAdapter secondAdapter = new SecondClassAdapter(SeekMachinistActivity.this, secondList);
        rightLV.setAdapter(secondAdapter);

        //左侧ListView点击事件
        leftLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //二级数据
                List<ProvinceCityChildsBean> list2 = firstList.get(position).getProvinceCityChilds();
                //如果没有二级类，则直接跳转
                if (list2 == null || list2.size() == 0) {
                    popupWindow.dismiss();

                    String firstId = firstList.get(position).getId();
                    String selectedName = firstList.get(position).getName();
                    handleResult(firstId, "-1", selectedName);
                    return;
                }

                FirstClassAdapter adapter = (FirstClassAdapter) (parent.getAdapter());
                //如果上次点击的就是这一个item，则不进行任何操作
                if (adapter.getSelectedPosition() == position) {
                    return;
                }

                //根据左侧一级分类选中情况，更新背景色
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();

                //显示右侧二级分类
                updateSecondListView(list2, secondAdapter);
            }
        });

        //右侧ListView点击事件
        rightLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //关闭popupWindow，显示用户选择的分类
                popupWindow.dismiss();

                int firstPosition = firstAdapter.getSelectedPosition();
                String firstId = firstList.get(firstPosition).getId();
                String secondId = firstList.get(firstPosition).getProvinceCityChilds().get(position).getId();
                String selectedName = firstList.get(firstPosition).getProvinceCityChilds().get(position)
                        .getName();
                handleResult(firstId, secondId, selectedName);
            }
        });
    }

    //顶部第一个标签的点击事件
    private void tab1OnClick() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            popupWindow.showAsDropDown(findViewById(R.id.main_div_line));
            popupWindow.setAnimationStyle(-1);
            //背景变暗
            darkView.startAnimation(animIn);
            darkView.setVisibility(View.VISIBLE);
        }
    }

    //刷新右侧ListView
    private void updateSecondListView(List<ProvinceCityChildsBean> list2,
                                      SecondClassAdapter secondAdapter) {
        secondList.clear();
        secondList.addAll(list2);
        secondAdapter.notifyDataSetChanged();
    }

    //处理点击结果
    private void handleResult(String firstId, String secondId, String selectedName) {
        String text = "first id:" + firstId + ",second id:" + secondId;
        //Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();

        mainTab1TV.setText(selectedName);
        mainTab1TV.setTextColor(getResources().getColor(R.color.red_light));
        Drawable img = getResources().getDrawable(R.mipmap.select_arrow_cur);
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        mainTab1TV.setCompoundDrawables(null, null, img, null);
        city = selectedName;
        sekkMachinisDataBeens.clear();
        seekMachinistListview.setRefreshing();
        initListData(pageIndex, type, city, year, order);

    }


    public void showSortPopupWindow(View parent) {
        LinearLayout layout;
        ListView listView;
        list = new ArrayList<>();
        list.add("默认排序");
        list.add("按工作年限由低到高");
        list.add("按工作年限由高到低");
        list.add("按距离由近到远");
        list.add("按距离由远到近");


        //加载布局
        layout = (LinearLayout) LayoutInflater.from(SeekMachinistActivity.this).inflate(
                R.layout.pop_time_contant_layout, null);
        //找到布局的控件
        listView = (ListView) layout.findViewById(R.id.pop_listview);
        //设置适配器
        //popWinListAdapter=new PopWinListAdapter(list,HomeMoreProjectActivity.this,cur_pos);
        MyAdapter myAdapter = new MyAdapter(SeekMachinistActivity.this);
        listView.setAdapter(myAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);// 一定要设置这个属性，否则ListView不会刷新
        myAdapter.notifyDataSetChanged();
        // 实例化popupWindow
        popupWindowTime = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //控制键盘是否可以获得焦点
        popupWindowTime.setFocusable(true);
        //设置popupWindow弹出窗体的背景
        popupWindowTime.setBackgroundDrawable(new BitmapDrawable(null, ""));
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        popupWindowTime.setOutsideTouchable(true);
        popupWindowTime.setFocusable(true);
        @SuppressWarnings("deprecation")
        //获取xoff
                int xpos = manager.getDefaultDisplay().getWidth() / 30 - popupWindowTime.getWidth() / 30;
        //xoff,yoff基于anchor的左下角进行偏移。
        popupWindowTime.showAsDropDown(parent, xpos, 10);
        //popupWindow.showAtLocation(parent, Gravity.TOP, 200, 250);


        //监听

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //关闭popupWindow
                popupWindowTime.dismiss();
                popupWindowTime = null;
                //showPopupWindowOne(seekButtonA);
                sortButton.setText(list.get(arg2));
                sortButton.setTextColor(getResources().getColor(R.color.red_light));
                Drawable img = getResources().getDrawable(R.mipmap.select_arrow_cur);
                img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
                sortButton.setCompoundDrawables(null, null, img, null);
                cur_pos = arg2;
                if (list.get(arg2).equals("默认排序")) {
                    order = "";
                } else {
                    order = list.get(arg2);
                }

                sekkMachinisDataBeens.clear();
                seekMachinistListview.setRefreshing();
                initListData(pageIndex, type, city, year, order);
            }


        });


    }

    public void showEvolvePopupWindow(View parent) {
        LinearLayout layout;
        ListView listView;
        list = new ArrayList<>();
        list.add("默认排序");
        list.add("一年以下");
        list.add("1-3年");
        list.add("3-5年");
        list.add("5-10年");
        list.add("10年以上");


        //加载布局
        layout = (LinearLayout) LayoutInflater.from(SeekMachinistActivity.this).inflate(
                R.layout.pop_time_contant_layout, null);
        //找到布局的控件
        listView = (ListView) layout.findViewById(R.id.pop_listview);
        //设置适配器
        //popWinListAdapter=new PopWinListAdapter(list,HomeMoreProjectActivity.this,cur_pos);
        MyAdapter myAdapter = new MyAdapter(SeekMachinistActivity.this);
        listView.setAdapter(myAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);// 一定要设置这个属性，否则ListView不会刷新
        myAdapter.notifyDataSetChanged();
        // 实例化popupWindow
        popupWindowTime = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //控制键盘是否可以获得焦点
        popupWindowTime.setFocusable(true);
        //设置popupWindow弹出窗体的背景
        popupWindowTime.setBackgroundDrawable(new BitmapDrawable(null, ""));
        WindowManager manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        popupWindowTime.setOutsideTouchable(true);
        popupWindowTime.setFocusable(true);
        @SuppressWarnings("deprecation")
        //获取xoff
                int xpos = manager.getDefaultDisplay().getWidth() / 30 - popupWindowTime.getWidth() / 30;
        //xoff,yoff基于anchor的左下角进行偏移。
        popupWindowTime.showAsDropDown(parent, xpos, 10);
        //popupWindow.showAtLocation(parent, Gravity.TOP, 200, 250);


        //监听

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //关闭popupWindow
                popupWindowTime.dismiss();
                popupWindowTime = null;
                //showPopupWindowOne(seekButtonA);
                evolveButton.setText(list.get(arg2));
                evolveButton.setTextColor(getResources().getColor(R.color.red_light));
                Drawable img = getResources().getDrawable(R.mipmap.select_arrow_cur);
                img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
                evolveButton.setCompoundDrawables(null, null, img, null);
                cur_evolve_pos = arg2;

                if (list.get(arg2).equals("默认排序")) {
                    year = "";
                } else {
                    year = list.get(arg2);
                }

                sekkMachinisDataBeens.clear();
                seekMachinistListview.setRefreshing();
                initListData(pageIndex, type, city, year, order);

            }


        });


    }


    private class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public MyAdapter(Context context) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Log.e("TEST", "refresh once");
            convertView = inflater.inflate(R.layout.item_layout, null, false);

            TextView tv = (TextView) convertView
                    .findViewById(R.id.item_text);// 显示文字
            tv.setText(list.get(position));
            if (popTag == 1) {
                if (position == cur_pos) {// 如果当前的行就是ListView中选中的一行，就更改显示样式
                    convertView.setBackgroundColor(getResources().getColor(R.color.background));// 更改整行的背景色
                    tv.setTextColor(Color.RED);// 更改字体颜色
                }
            } else {
                if (position == cur_evolve_pos) {// 如果当前的行就是ListView中选中的一行，就更改显示样式
                    convertView.setBackgroundColor(getResources().getColor(R.color.background));// 更改整行的背景色
                    tv.setTextColor(Color.RED);// 更改字体颜色
                }
            }


            return convertView;
        }
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