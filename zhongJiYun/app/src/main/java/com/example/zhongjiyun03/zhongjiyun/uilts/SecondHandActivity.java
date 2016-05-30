package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.HomeSecondHandListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.SecondHandDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.AppListDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.SecondHandBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.FacillyChildsBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.FacillyDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityChildsBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.SelectData;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
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
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

public class SecondHandActivity extends AppCompatActivity implements OnClickListener, PullToRefreshBase.OnRefreshListener2<ListView> {

    @ViewInject(R.id.second_hand_listview)
    private PullToRefreshListView secondHandListview; //列表
    @ViewInject(R.id.shard_tv)
    private ImageView shareTextView; //分享
    @ViewInject(R.id.title_name_tv)
    private TextView titleName;
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


    private TextView mainTab1TV;
    /**左侧一级分类的数据*/
    private List<ProvinceCityBean> firstList;
    /**右侧二级分类的数据*/
    private List<ProvinceCityChildsBean> secondList;

    /**使用PopupWindow显示一级分类和二级分类*/
    private PopupWindow popupWindow;

    /**左侧和右侧两个ListView*/
    private ListView leftLV, rightLV;
    //弹出PopupWindow时背景变暗
    private View darkView;

    //弹出PopupWindow时，背景变暗的动画
    private Animation animIn, animOut;

    private int pageIndex = 1; //page
    private List<SecondHandBean> secondHandBeens;

    //设备产商
    private PopupWindow popupWindowFacilly;
    /**左侧和右侧两个ListView*/
    private ListView facillyLeftLV, facillyRightLV;
    @ViewInject(R.id.facilly_text_view)
    private TextView facillyText;
    /**左侧一级分类的数据*/
    // private List<FacillyDataBean> facilluyFirstList=new ArrayList<>();
    /**右侧二级分类的数据*/
    private List<FacillyChildsBean> facillySecondList;
    private String type; //型号
    private String facillyTaye;//商家
    private String city;   //城市
    private String year; //出厂时间
    private String order;  //排序

    @ViewInject(R.id.facilly_text_view)
    private TextView facillyTextView;
    private LocationManager lm;
    private static final String TAG = "GpsActivity";
    private String Longitude;
    private String Latitude;
    private HomeSecondHandListAdapter homeSecondHandListAdapter;
    private boolean isPullDownRefresh = true; //判断是下拉，还是上拉的标记
    private String province;
    @ViewInject(R.id.not_data_layout)
    private LinearLayout notDataLayout;//没有数据显示
    @ViewInject(R.id.network_remind_layout)
    private LinearLayout networkRemindLayout; //没有网络提示
    @ViewInject(R.id.not_data_image)
    private ImageView notDataImage; //没有网络和没有数据显示
    @ViewInject(R.id.not_data_text)
    private TextView notDataText;
    private List<FacillyDataBean> facillyDataBeens ;//设备厂商数据
    private SecondHandHandler secondHandHandler;
    private AppBean<SecondHandDataBean> appListDataBean;
    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        OnekeyShare oks = new OnekeyShare();
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                //Log.e("分享成功","分享回调成功");
                //MyAppliction.showToast("分享回调成功");
                Message msg = new Message();
                Bundle b = new Bundle();// 存放数据
                b.putString("color", "我的");
                msg.setData(b);
                SecondHandActivity.this.secondHandHandler.sendMessage(msg); // 向Handler发送消息，更新UI

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("分享onError","分享回调onError");
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });


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
        setContentView(R.layout.activity_second_hand);
        ViewUtils.inject(this);
        PopupWindowHelper.init(SecondHandActivity.this);
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
        facillyDataBeens=new ArrayList<>();
        secondHandBeens = new ArrayList<>();
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
        shareTextView.setOnClickListener(this);
        titleName.setText("二手机");
        retrunText.setOnClickListener(this);
        intiListView();
        intiPullToRefresh();
        networkRemindLayout.setOnClickListener(this);
        secondHandHandler=new SecondHandHandler();

        facillyTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (facillyDataBeens.size()!=0){
                    tabFacillynClick();
                }else {
                    MyAppliction.showToast("网络异常，请稍后重试");
                }

            }
        });




    }


    public void intiPullToRefresh() {
        secondHandListview.setMode(PullToRefreshBase.Mode.BOTH);
        secondHandListview.setOnRefreshListener(this);
        ILoadingLayout endLabels = secondHandListview
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels = secondHandListview
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时， 显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        secondHandListview.setRefreshing();
    }

    private void intiListView() {
        homeSecondHandListAdapter = new HomeSecondHandListAdapter(secondHandBeens, this);
        secondHandListview.setAdapter(homeSecondHandListAdapter);
        secondHandListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SecondHandActivity.this, ExturderParticularsActivity.class);
                intent.putExtra("secondHandData", secondHandBeens.get(position - 1).getId());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shard_tv:
                showShare();
                break;
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

    private void initListData(final int pageIndex, String type, String city, String year, String order) {
        HttpUtils httpUtils = new HttpUtils();
        final RequestParams requestParams = new RequestParams();
        SQLhelper sqLhelper = new SQLhelper(SecondHandActivity.this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        Cursor cursor = db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid = null;  //用户id

        while (cursor.moveToNext()) {
            uid = cursor.getString(0);

        }
        if (!TextUtils.isEmpty(uid)) {
            requestParams.addBodyParameter("Id", uid);
            //步骤1：创建一个SharedPreferences接口对象
            SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
            //步骤2：获取文件中的值
            String sesstionId = read.getString("code", "");
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        }

        if (!TextUtils.isEmpty(city)) {

            if (province.equals("全部")) {

            } else {
                if (city.equals("全部")) {
                    requestParams.addBodyParameter("province", province);
                } else {
                    requestParams.addBodyParameter("city", city);
                }

            }


        }
        if (getIntent().getStringExtra("tage").equals("matingFacily")) {
            if (!TextUtils.isEmpty(getIntent().getStringExtra("data"))) {
                requestParams.addBodyParameter("province", getIntent().getStringExtra("data").toString());
            }

        }
        if (!TextUtils.isEmpty(type)) {
            if (facillyTaye.equals("全部")){

            }else {
                if (type.equals("全部")){
                    requestParams.addBodyParameter("manufacture",facillyTaye);
                }else {
                    requestParams.addBodyParameter("type", type);
                }
            }

        }
        if (!TextUtils.isEmpty(year)) {
            requestParams.addBodyParameter("year", year);
        }
        if (!TextUtils.isEmpty(order)) {
            requestParams.addBodyParameter("order", order);
        }
        if (!TextUtils.isEmpty(MyAppliction.getLatitude())) {
            requestParams.addBodyParameter("latitude", MyAppliction.getLatitude());
        }
        if (!TextUtils.isEmpty(MyAppliction.getLongitude())) {
            requestParams.addBodyParameter("longitude", MyAppliction.getLongitude());
        }
        requestParams.addBodyParameter("pageIndex", pageIndex + "");
        requestParams.addBodyParameter("pageSize", "10");

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getSecondExtruderData(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("二手钻机", responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)) {
                     appListDataBean = JSONObject.parseObject(responseInfo.result, new TypeReference<AppBean<SecondHandDataBean>>() {
                    });
                    if ((appListDataBean.getResult()).equals("success")) {
                        SecondHandDataBean secondHandDataBean = appListDataBean.getData();
                        if (secondHandDataBean != null) {
                            List<SecondHandBean> secondHandBeen = secondHandDataBean.getPagerData();

                            if (secondHandBeen != null) {
                                if (isPullDownRefresh) {
                                    secondHandBeens.clear();
                                }
                                secondHandBeens.addAll(secondHandBeen);
                                homeSecondHandListAdapter.notifyDataSetChanged();
                            }
                        }


                        secondHandListview.onRefreshComplete();
                        notDataLayout.setVisibility(View.GONE);
                    } else if ((appListDataBean.getResult()).equals("nomore")) {

                        MyAppliction.showToast("已到最底了");
                        homeSecondHandListAdapter.notifyDataSetChanged();
                        secondHandListview.onRefreshComplete();
                        //notDataLayout.setVisibility(View.GONE);
                    } else if ((appListDataBean.getResult()).equals("empty")) {
                        if (isPullDownRefresh) {
                            secondHandBeens.clear();
                        }
                        homeSecondHandListAdapter.notifyDataSetChanged();
                        secondHandListview.onRefreshComplete();
                        //MyAppliction.showToast("没有更多数据");
                        notDataLayout.setVisibility(View.VISIBLE);
                        notDataImage.setBackgroundResource(R.mipmap.no_rig_icon);
                        notDataText.setText("还没有找到机手哦");
                    }


                }
                networkRemindLayout.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("二手钻机", s);
                networkRemindLayout.setVisibility(View.VISIBLE);
                homeSecondHandListAdapter.notifyDataSetChanged();
                secondHandListview.onRefreshComplete();
                if (secondHandBeens.size()==0){
                    notDataLayout.setVisibility(View.VISIBLE);
                    notDataImage.setBackgroundResource(R.mipmap.no_wifi_icon);
                    notDataText.setText("没有网络哦");
                }
            }
        });


    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndex = 1;
        isPullDownRefresh = true;
        initListData(pageIndex, type, city, year, order);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
         pageIndex++;
        isPullDownRefresh = false;
        initListData(pageIndex, type, city, year, order);


    }

    private void findView() {
        mainTab1TV = (TextView)findViewById(R.id.main_tab1);
        darkView =findViewById(R.id.main_darkview);

        animIn = AnimationUtils.loadAnimation(SecondHandActivity.this, R.anim.fade_in_anim);
        animOut = AnimationUtils.loadAnimation(SecondHandActivity.this, R.anim.fade_out_anim);
    }

    private void initData() {
        ProvinceCityDataBean provinceCityDataBean=JSONObject.parseObject(SelectData.selectCityData+SelectData.selectCityDataOne+SelectData.selectCityDataTwo,new TypeReference<ProvinceCityDataBean>(){});
        if (provinceCityDataBean!=null){
            firstList=provinceCityDataBean.getProvinceCity();
        }

        //设备厂商
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("DeviceJsonType","4");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getFacillyData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("设备厂商",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppListDataBean<FacillyDataBean> appListDataBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppListDataBean<FacillyDataBean>>(){});
                    if (appListDataBean.getResult().equals("success")){
                        List<FacillyDataBean> facillyDataBeen=  appListDataBean.getData();
                        //facilluyFirstList.addAll(facillyDataBeen);
                        if (facillyDataBeen.size()!=0){
                            facillyDataBeens.addAll(facillyDataBeen);
                            initPopupFacilly(facillyDataBeens);
                        }


                    }



                }



            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }

    private void initPopupFacilly(final List<FacillyDataBean> facilluyFirstList) {
        popupWindowFacilly = new PopupWindow(SecondHandActivity.this);
        View view = LayoutInflater.from(SecondHandActivity.this).inflate(R.layout.facilly_popup_layout, null);
        facillyLeftLV = (ListView) view.findViewById(R.id.facilly_pop_listview_left);
        facillyRightLV = (ListView) view.findViewById(R.id.facilly_pop_listview_right);



        popupWindowFacilly.setContentView(view);
        popupWindowFacilly.setBackgroundDrawable(new PaintDrawable());
        popupWindowFacilly.setFocusable(true);

        popupWindowFacilly.setHeight(ScreenUtils.getScreenH(SecondHandActivity.this) * 2 / 3);
        popupWindowFacilly.setWidth(ScreenUtils.getScreenW(SecondHandActivity.this));

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
        final FacillyFirstClassAdapter firstAdapter = new FacillyFirstClassAdapter(SecondHandActivity.this, facilluyFirstList);
        facillyLeftLV.setAdapter(firstAdapter);

        //加载左侧第一行对应右侧二级分类
        facillySecondList = new ArrayList<FacillyChildsBean>();
        facillySecondList.addAll(facilluyFirstList.get(0).getChilds());
        final FacillySecondClassAdapter secondAdapter = new FacillySecondClassAdapter(SecondHandActivity.this, facillySecondList);
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

                    String  firstId = facilluyFirstList.get(position).getText();
                    String selectedName = facilluyFirstList.get(position).getText();
                    facillyHandleResult(firstId, "-1", selectedName);
                    return;
                }

                FacillyFirstClassAdapter adapter = (FacillyFirstClassAdapter) (parent.getAdapter());
                //如果上次点击的就是这一个item，则不进行任何操作
                if (adapter.getSelectedPosition() == position){
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
                String  firstId = facilluyFirstList.get(firstPosition).getText();
                String secondId = facilluyFirstList.get(firstPosition).getChilds().get(position).getValue();
                String selectedName = facilluyFirstList.get(firstPosition).getChilds().get(position)
                        .getText();
                facillyHandleResult(firstId, secondId, selectedName);


            }
        });
    }
    //顶部第一个标签的点击事件
    private void tabFacillynClick() {
        if (popupWindowFacilly.isShowing()) {
            popupWindowFacilly.dismiss();
        } else {
            popupWindowFacilly.showAsDropDown(SecondHandActivity.this.findViewById(R.id.main_div_line));
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
    private void facillyHandleResult(String firstId, String  secondId, String selectedName){
        String text = "first id:" + firstId + ",second id:" + secondId;
        //Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();

        facillyText.setText(selectedName);
        facillyText.setTextColor(getResources().getColor(R.color.red_light));
        Drawable img = getResources().getDrawable(R.mipmap.select_arrow_cur);
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        facillyText.setCompoundDrawables(null, null, img, null);
        facillyTaye=firstId;
        type=selectedName;
        secondHandBeens.clear();
        isPullDownRefresh=true;
        secondHandListview.setRefreshing(true);
        pageIndex=1;
        initListData(pageIndex,selectedName,city,year,order);
        //secondHandListview.setRefreshing();
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
        popupWindow = new PopupWindow(SecondHandActivity.this);
        View view = LayoutInflater.from(SecondHandActivity.this).inflate(R.layout.popup_layout, null);
        leftLV = (ListView) view.findViewById(R.id.pop_listview_left);
        rightLV = (ListView) view.findViewById(R.id.pop_listview_right);

        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new PaintDrawable());
        popupWindow.setFocusable(true);

        popupWindow.setHeight(ScreenUtils.getScreenH(SecondHandActivity.this) * 2 / 3);
        popupWindow.setWidth(ScreenUtils.getScreenW(SecondHandActivity.this));

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
        final FirstClassAdapter firstAdapter = new FirstClassAdapter(SecondHandActivity.this, firstList);
        leftLV.setAdapter(firstAdapter);

        //加载左侧第一行对应右侧二级分类
        secondList = new ArrayList<ProvinceCityChildsBean>();
        secondList.addAll(firstList.get(0).getProvinceCityChilds());
        final SecondClassAdapter secondAdapter = new SecondClassAdapter(SecondHandActivity.this, secondList);
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

                    String  firstId = firstList.get(position).getName();
                    String selectedName = firstList.get(position).getName();
                    handleResult(firstId, "-1", selectedName);
                    return;
                }

                FirstClassAdapter adapter = (FirstClassAdapter) (parent.getAdapter());
                //如果上次点击的就是这一个item，则不进行任何操作
                if (adapter.getSelectedPosition() == position){
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
                String firstId = firstList.get(firstPosition).getName();
                String secondId = firstList.get(firstPosition).getProvinceCityChilds().get(position).getId();
                String selectedName = firstList.get(firstPosition).getProvinceCityChilds().get(position)
                        .getName();
                handleResult(firstId, secondId, selectedName);
                //secondHandListview.setRefreshing();
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
    private void handleResult(String firstId, String secondId, String selectedName){
        String text = "first id:" + firstId + ",second id:" + secondId;
        //Toast.makeText(SecondHandActivity.this, text, Toast.LENGTH_SHORT).show();
            mainTab1TV.setText(selectedName);
            mainTab1TV.setTextColor(getResources().getColor(R.color.red_light));
            Drawable img = getResources().getDrawable(R.mipmap.select_arrow_cur);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            mainTab1TV.setCompoundDrawables(null, null, img, null);
        province=firstId;
        city=selectedName;
        secondHandBeens.clear();
        isPullDownRefresh=true;
        secondHandListview.setRefreshing();
        pageIndex=1;
        initListData(pageIndex,type,selectedName,year,order);

    }



    public void showSortPopupWindow(View parent) {
        LinearLayout layout;
        ListView listView;
        list=new ArrayList<>();
        list.add("默认排序");
        list.add("按价格从低到高");
        list.add("按价格从高到低");
        list.add("按距离由近到远");
        list.add("按距离由远到近");

        //加载布局
        layout = (LinearLayout) LayoutInflater.from(SecondHandActivity.this).inflate(
                R.layout.pop_time_contant_layout, null);
        //找到布局的控件
        listView = (ListView) layout.findViewById(R.id.pop_listview);
        //设置适配器
        //popWinListAdapter=new PopWinListAdapter(list,HomeMoreProjectActivity.this,cur_pos);
        MyAdapter myAdapter=new MyAdapter(SecondHandActivity.this);
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
        popupWindowTime.showAsDropDown(parent, xpos, 1);
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
                cur_pos=arg2;
                 if (list.get(arg2).equals("默认排序")){
                     order="";
                 }else {
                     order=list.get(arg2);
                 }
                secondHandListview.onRefreshComplete();
                secondHandBeens.clear();
                isPullDownRefresh=true;
                secondHandListview.setRefreshing();
                pageIndex=1;
                initListData(pageIndex,type,city,year,order);

            }


        });



    }
    public void showEvolvePopupWindow(View parent) {
        LinearLayout layout;
        ListView listView;
        list=new ArrayList<>();
        list.add("全部");
        list.add("2016");
        list.add("2015");
        list.add("2014");
        list.add("2013");
        list.add("2012");
        list.add("2011");
        list.add("2010");
        list.add("2009");
        list.add("2008");
        list.add("2007");
        list.add("2006");
        list.add("2005");
        list.add("2004");
        list.add("2003");
        list.add("2002");
        list.add("2001");
        list.add("2000");
        //加载布局
        layout = (LinearLayout) LayoutInflater.from(SecondHandActivity.this).inflate(
                R.layout.pop_time_contant_layout, null);
        //找到布局的控件
        listView = (ListView) layout.findViewById(R.id.pop_listview);
        //设置适配器
        //popWinListAdapter=new PopWinListAdapter(list,HomeMoreProjectActivity.this,cur_pos);
        MyAdapter myAdapter=new MyAdapter(SecondHandActivity.this);
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
        popupWindowTime.showAsDropDown(parent, xpos, 4);
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
                cur_evolve_pos=arg2;
                  if (list.get(arg2).equals("全部")){
                      year="";
                  }else {
                      year=list.get(arg2);

                  }
                secondHandListview.onRefreshComplete();
                secondHandBeens.clear();
                secondHandListview.setRefreshing();
                pageIndex=1;
                initListData(pageIndex,type,city,year,order);



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
            }else {
                if (position == cur_evolve_pos) {// 如果当前的行就是ListView中选中的一行，就更改显示样式
                    convertView.setBackgroundColor(getResources().getColor(R.color.background));// 更改整行的背景色
                    tv.setTextColor(Color.RED);// 更改字体颜色
                }
            }


            return convertView;
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

    /**
     * 接受消息，处理消息 ，此Handler会与当前主线程一块运行
     * */

    class SecondHandHandler extends Handler {
        public SecondHandHandler() {
        }

        public SecondHandHandler(Looper L) {
            super(L);
        }

        // 子类必须重写此方法，接受数据
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            //Log。d("MyHandler"， "handleMessage。。。。。。");
            super.handleMessage(msg);
            // 此处可以更新UI
           /* Bundle b = msg.getData();
            String color = b.getString("color");*/
            shareRedPacket();


        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();

        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform,cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
                if ("Wechat".equals(platform.getName())) {
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);

                }
                if ("WechatMoments".equals(platform.getName())) {
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                }
            }
        });
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("中基云二手钻机市场海量二手钻，实名认证机主等你来拿");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(AppUtilsUrl.BaseUrl+"App/Index.html#/tab/my/boss-used-rig");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("中基云二手钻机市场海量二手钻，实名认证机主等你来拿");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl("http://www.zhongjiyun.cn/app/img/logo1.png");
        // url仅在微信（包括好友和朋友圈）中使用
         oks.setUrl(AppUtilsUrl.BaseUrl+"App/Index.html#/tab/my/boss-used-rig");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("中基云二手钻机市场海量二手钻，实名认证机主等你来拿");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(AppUtilsUrl.BaseUrl+"App/Index.html#/tab/my/boss-used-rig");


        // 启动分享GUI
        oks.show(this);

    }
      //分享成功获得云币
    private void shareRedPacket() {
        //MyAppliction.showToast("分享回调成功");
        SQLhelper sqLhelper=new SQLhelper(SecondHandActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }

        if (!TextUtils.isEmpty(uid)){
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("id",uid);

        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getShareRedPacketData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("二手机列表分享获取红包",responseInfo.result);
                MyAppliction.showToast("分享成功");

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("二手机列表分享获取红包onFailure",s);
            }
        });
        }


    }


}
