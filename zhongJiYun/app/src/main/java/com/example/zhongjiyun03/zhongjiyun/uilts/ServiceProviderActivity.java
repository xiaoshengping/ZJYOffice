package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.HomeServicetListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.ServiceDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.ServiceProviderBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.SelectData;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.popwindon.RootListViewAdapter;
import com.example.zhongjiyun03.zhongjiyun.popwindon.SubListViewAdapter;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.utils.ScreenUtils;
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


    /**
     * 筛选
     * @param savedInstanceState
     */
    private List<ProvinceCityBean> provinceCityBeens ;//地域数据
    /**
     * 项目进展筛选按钮
     */
    @ViewInject(R.id.evolve_llayout)
    private LinearLayout evolveLlayout;
    /**
     * popupwindow
     *
     */
    private PopupWindow mPopupWindow;
    /**
     * 二级菜单的根目录
     */
    private ListView rootListView;

    private ListView subListView;

    /**
     * 弹出的popupWindow布局
     */
    private LinearLayout popupLayout;

    /**
     * 子目录的布局
     */
    private FrameLayout subLayout;

    /**
     * 根目录被选中的节点
     */
    private int selectedPosition;
    /**
     * 点击关闭popwindow
     */
    private  LinearLayout popWindowDisminLayout;

    /**
     * 地域筛选数据
     *
     */
    private List<String> sortLists;

    /**
     * 地域筛选按钮
     *
     */
    @ViewInject(R.id.address_llayout)
    private LinearLayout addressLayout;
    @ViewInject(R.id.evolve_text)
    private TextView evolveText; //项目进展筛选text
    @ViewInject(R.id.address_text)
    private TextView addressText;  //地域筛选text
    private String province;  //省份
    private String city;  //城市
    //private AppDrillingDataBean<AppListPagerDataBean<AttentionDrillingDataBean>> appDataBean;
    private String orderText;//选择的排序内容
    private List<String> orlder;
    @ViewInject(R.id.order_llayout)
    private LinearLayout orderLlayout;//排序点击按钮
    @ViewInject(R.id.order_text)
    private TextView orderTextView;
    private String evolveTextString;

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
    @ViewInject(R.id.select_layout)
    private LinearLayout selectLayout; //筛选布局



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
        titleNemeTv.setText("服务商列表");
        retrunText.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);
        serviceProviderBean = new ArrayList<>();
        addressLayout.setOnClickListener(this);
        evolveLlayout.setOnClickListener(this);
        orderLlayout.setOnClickListener(this);
        sortLists=new ArrayList<>();
        orlder=new ArrayList<>();
        provinceCityBeens=new ArrayList<>();
        intiListView();

        if (getIntent().getStringExtra("tage").equals("matingFacily")){
            selectLayout.setVisibility(View.GONE);
        }else {
            selectLayout.setVisibility(View.VISIBLE);
        }

        initSelectData();


    }

    /**
     *
     * 筛选数据
     */
    private void initSelectData() {
        String[] strings={"默认排序","距离由近到远"};
        for (int i = 0; i <strings.length ; i++) {
            orlder.add(strings[i]);
        }
        //物流=0, 主机=1, 常用配套=2, 消耗配套=3, 维修=4, 现场支持=5
        String[] evstring={"物流","主机","常用配套","消耗配套","维修","现场支持"};
        for (int i = 0; i <evstring.length ; i++) {
            sortLists.add(evstring[i]);
        }
        ProvinceCityDataBean provinceCityDataBean=JSONObject.parseObject(SelectData.selectCityData+SelectData.selectCityDataOne+SelectData.selectCityDataTwo,new TypeReference<ProvinceCityDataBean>(){});
        if (provinceCityDataBean!=null){
            provinceCityBeens.addAll(provinceCityDataBean.getProvinceCity());
        }


    }


    private void showPopBtn( final int tage) {
        LayoutInflater inflater = LayoutInflater.from(ServiceProviderActivity.this);
        popupLayout = (LinearLayout) inflater.inflate(R.layout.project_popupwindow_layout, null, false);
        rootListView = (ListView) popupLayout.findViewById(R.id.root_listview);
        popWindowDisminLayout= (LinearLayout) popupLayout.findViewById(R.id.popupwindow_dismin);
        LinearLayout selectRightLayout= (LinearLayout) popupLayout.findViewById(R.id.select_right_layout);
        LinearLayout selectMiddleLayout= (LinearLayout) popupLayout.findViewById(R.id.select_middle_layout);
        LinearLayout selectLeftLayout= (LinearLayout) popupLayout.findViewById(R.id.select_left_layout);
        /**
         * 初始化mPopupWindow
         */
        mPopupWindow = new PopupWindow(popupLayout, ScreenUtils.getScreenWidth(ServiceProviderActivity.this),
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        /**
         * 有了mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
         * 这句可以使点击popupwindow以外的区域时popupwindow自动消失 但这句必须放在showAsDropDown之前
         */
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

        popWindowDisminLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                }

            }
        });
        /**
         * popupwindow的位置，第一个参数表示位于哪个控件之下 第二个参数表示向左右方向的偏移量，正数表示向左偏移，负数表示向右偏移
         * 第三个参数表示向上下方向的偏移量，正数表示向下偏移，负数表示向上偏移
         *
         */
        if (tage==3){
            mPopupWindow.showAsDropDown(addressLayout, -5, 0);// 在控件下方显示popwindow
            selectLeftLayout.setVisibility(View.VISIBLE);
            selectRightLayout.setVisibility(View.GONE);
            selectMiddleLayout.setVisibility(View.GONE);
        }else if (tage==2) {
            mPopupWindow.showAsDropDown(evolveLlayout, -5, 0);// 在控件下方显示popwindow

            selectMiddleLayout.setVisibility(View.VISIBLE);
            selectLeftLayout.setVisibility(View.GONE);
            selectRightLayout.setVisibility(View.GONE);
        }else if (tage==4){
            mPopupWindow.showAsDropDown(orderLlayout, -5, 0);// 在控件下方显示popwindow
            selectMiddleLayout.setVisibility(View.GONE);
            selectLeftLayout.setVisibility(View.GONE);
            selectRightLayout.setVisibility(View.VISIBLE);
        }
        mPopupWindow.update();

        final RootListViewAdapter adapter = new RootListViewAdapter(ServiceProviderActivity.this);
        /**
         * 子popupWindow
         */
        subLayout = (FrameLayout) popupLayout.findViewById(R.id.sub_popupwindow);
        /**
         * 初始化subListview
         */
        subListView = (ListView) popupLayout.findViewById(R.id.sub_listview);
        /**
         * 弹出popupwindow时，二级菜单默认隐藏，当点击某项时，二级菜单再弹出
         */
        if (tage==3){
            adapter.setTage(3);
            adapter.setItems(provinceCityBeens);
            subLayout.setVisibility(View.VISIBLE);
        }else if (tage==2){
            adapter.setTage(2);
            adapter.setItems(sortLists);
            subLayout.setVisibility(View.GONE);
        }else if (tage==4){
            adapter.setTage(4);
            adapter.setItems(orlder);
            subLayout.setVisibility(View.GONE);
        }
        rootListView.setAdapter(adapter);
        rootListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if (tage==3){

                    SubListViewAdapter subAdapter = new SubListViewAdapter(
                            ServiceProviderActivity.this, provinceCityBeens.get(position).getProvinceCityChilds(), position,3);
                    subListView.setAdapter(subAdapter);
                    /**
                     * 选中root某项时改变该ListView item的背景色
                     */
                    adapter.setSelectedPosition(position);
                    adapter.notifyDataSetInvalidated();
                    selectedPosition=position;
                    /**
                     * 选中某个根节点时，使显示相应的子目录可见
                     */
                    subListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(
                                AdapterView<?> parent, View view,
                                int position, long id) {
                            // TODO Auto-generated method stub
                            mPopupWindow.dismiss();
                            addressText.setTextColor(getResources().getColor(R.color.red_light));

                            Drawable drawable = getResources().getDrawable(R.mipmap.drop_down_icon_cur);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                            addressText.setCompoundDrawables(null,null , drawable, null);//画在右边
                            province=provinceCityBeens.get(selectedPosition).getName();
                            city=provinceCityBeens.get(selectedPosition).getProvinceCityChilds().get(position).getName();
                            if (city.equals("全部")){
                                if (province.equals("全部")){
                                    addressText.setText("全国项目");
                                }else {
                                    addressText.setText(provinceCityBeens.get(selectedPosition).getName());
                                }
                            }else {
                                addressText.setText(provinceCityBeens.get(selectedPosition).getName()+provinceCityBeens.get(selectedPosition).getProvinceCityChilds().get(position).getName());
                            }
                            pageIndext=1;
                            initData(pageIndext);
                            isPullDownRefresh=true;


                        }
                    });
                }else if (tage==2){
                    SubListViewAdapter  subAdapter = new SubListViewAdapter(
                            ServiceProviderActivity.this, sortLists, position,2);
                    subListView.setAdapter(subAdapter);
                    /**
                     * 选中root某项时改变该ListView item的背景色
                     */
                    adapter.setSelectedPosition(position);
                    adapter.notifyDataSetInvalidated();
                    mPopupWindow.dismiss();
                    evolveText.setTextColor(getResources().getColor(R.color.red_light));
                    evolveText.setText(sortLists.get(position));
                    Drawable drawable = getResources().getDrawable(R.mipmap.drop_down_icon_cur);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                    evolveText.setCompoundDrawables(null,null, drawable, null);//画在右边
                    evolveTextString=sortLists.get(position);
                    pageIndext=1;
                    initData(pageIndext);
                    isPullDownRefresh=true;


                }else if (tage==4){
                    SubListViewAdapter  subAdapter = new SubListViewAdapter(
                            ServiceProviderActivity.this, sortLists, position,4);
                    subListView.setAdapter(subAdapter);
                    /**
                     * 选中root某项时改变该ListView item的背景色
                     */
                    adapter.setSelectedPosition(position);
                    adapter.notifyDataSetInvalidated();
                    mPopupWindow.dismiss();
                    orderTextView.setTextColor(getResources().getColor(R.color.red_light));
                    orderTextView.setText(orlder.get(position));
                    Drawable drawable = getResources().getDrawable(R.mipmap.drop_down_icon_cur);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                    orderTextView.setCompoundDrawables(null,null, drawable, null);//画在右边
                    orderText=orlder.get(position);
                    pageIndext=1;
                    initData(pageIndext);
                    isPullDownRefresh=true;


                }





            }
        });
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

        if (!TextUtils.isEmpty(city)){
            if (city.equals("全部")){
                if (!province.equals("全部")){
                    requestParams.addBodyParameter("province",province);
                }
            }else {
                requestParams.addBodyParameter("province",province);
                requestParams.addBodyParameter("city",city);
            }
        }
        //物流=0, 主机=1, 常用配套=2, 消耗配套=3, 维修=4, 现场支持=5
        if (!TextUtils.isEmpty(evolveTextString)){
            if (evolveTextString.equals("物流")){
                requestParams.addBodyParameter("type","0");
            }else if (evolveTextString.equals("主机")){
                requestParams.addBodyParameter("type","1");
            }else if (evolveTextString.equals("常用配套")){
                requestParams.addBodyParameter("type","2");
            }else if (evolveTextString.equals("消耗配套")){
                requestParams.addBodyParameter("type","3");
            }else if (evolveTextString.equals("维修")){
                requestParams.addBodyParameter("type","4");
            }else if (evolveTextString.equals("现场支持")){
                requestParams.addBodyParameter("type","5");
            }
        }
        if (!TextUtils.isEmpty(orderText)){
            if (orderText.equals("默认排序")){
                requestParams.addBodyParameter("orderType","0");
            }else if (orderText.equals("距离由近到远")){
                requestParams.addBodyParameter("orderType","1");
            }
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
                //Log.e("服务商数据",responseInfo.result);
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
                         if (isPullDownRefresh){
                             serviceProviderBean.clear();
                         }
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
            case R.id.order_llayout:   //排序筛选
                showPopBtn(4);
                break;
            case R.id.evolve_llayout:  //项目进展
                showPopBtn(2);
                break;
            case R.id.address_llayout:  //地域筛选
                showPopBtn(3);
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
