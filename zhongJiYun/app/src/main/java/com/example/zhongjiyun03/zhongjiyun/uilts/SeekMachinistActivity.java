package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.SeekMachinistListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.SeekMachinisBean;
import com.example.zhongjiyun03.zhongjiyun.bean.SekkMachinisDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.AppListDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.FacillyDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.SelectData;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.SQLHelperUtils;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.example.zhongjiyun03.zhongjiyun.popwin.PopupWindowHelper;
import com.example.zhongjiyun03.zhongjiyun.popwindon.RootListViewAdapter;
import com.example.zhongjiyun03.zhongjiyun.popwindon.SubListViewAdapter;
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

public class SeekMachinistActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener {
    @ViewInject(R.id.register_tv)
    private TextView comtintJobText;   //头部右边
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
    private String orderText;//选择的排序内容
    private List<String> orlder;
    @ViewInject(R.id.order_llayout)
    private LinearLayout orderLlayout;//排序点击按钮
    @ViewInject(R.id.order_text)
    private TextView orderTextView;  //排序txtview
    private String evolveTextString;
    @ViewInject(R.id.facility_llayout)
    private LinearLayout facillyLayout;  //设备型号按钮
    @ViewInject(R.id.facility_text)
    private TextView facillyText; //品牌筛选text
    private String manufacture;
    private String type;

    
    private SeekMachinistListAdapter homeServiceListAdapter;
    @ViewInject(R.id.seek_machinist_listview)
    private PullToRefreshListView seekMachinistListview;  //列表
    private int pageIndex = 1;
    private List<SekkMachinisDataBean> sekkMachinisDataBeens;
    private boolean isPullDownRefresh = true; //判断是下拉，还是上拉的标记
    @ViewInject(R.id.not_data_layout)
    private LinearLayout notDataLayout;
    @ViewInject(R.id.network_remind_layout)
    private LinearLayout networkRemindLayout;
    @ViewInject(R.id.not_data_image)
    private ImageView notDataImage; //没有网络和没有数据显示
    @ViewInject(R.id.not_data_text)
    private TextView notDataText;
    private List<FacillyDataBean> facillyDataBeens;//设备厂商数据
    @ViewInject(R.id.drilling_select_layout)
    private RelativeLayout drillingSelectLayout;// 筛选布局


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
        setContentView(R.layout.activity_seek_machinist);
        ViewUtils.inject(this);
        PopupWindowHelper.init(this);
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
        
        initData();
        initListView();
        intiPullToRefresh();
        
        
    }

    /**
     * 初始化View
     */
    private void initView() {
        facillyDataBeens=new ArrayList<>();
        networkRemindLayout.setOnClickListener(this);
        comtintJobText.setOnClickListener(this);
        addressLayout.setOnClickListener(this);
        evolveLlayout.setOnClickListener(this);
        orderLlayout.setOnClickListener(this);
        facillyLayout.setOnClickListener(this);
        retrunText.setOnClickListener(this);
        sekkMachinisDataBeens = new ArrayList<>();
        if (getIntent().getStringExtra("tage").equals("matingFacily")){
            drillingSelectLayout.setVisibility(View.GONE);
            comtintJobText.setVisibility(View.GONE);
            titleNemeTv.setText("附近机手");
        }else {
            drillingSelectLayout.setVisibility(View.VISIBLE);
            comtintJobText.setText("发布招聘");
            comtintJobText.setVisibility(View.VISIBLE);
            titleNemeTv.setText("找机手");
        }
        evolveText.setText("年限");
        
    }

    /**
     * 列表数据
     * @param pageIndex
     */
    private void initListData(int pageIndex) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        String uid= SQLHelperUtils.queryId(SeekMachinistActivity.this);
        if (!TextUtils.isEmpty(uid)){
            requestParams.addBodyParameter("Id", uid);
            //步骤1：创建一个SharedPreferences接口对象
            SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
            //步骤2：获取文件中的值
            String sesstionId = read.getString("code","");
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        }

        requestParams.addBodyParameter("pageIndex", pageIndex + "");
        requestParams.addBodyParameter("pageSize", "10");
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

        if (!TextUtils.isEmpty(type)){
            if (!type.equals("全部")){
                requestParams.addBodyParameter("manufacture",manufacture);
                requestParams.addBodyParameter("type",type);
            }else {
                if (!TextUtils.isEmpty(manufacture)){
                    if (!manufacture.equals("全部")){
                        requestParams.addBodyParameter("manufacture",manufacture);
                    }

                }
            }
        }
        if (getIntent().getStringExtra("tage").equals("matingFacily")){
            if (!TextUtils.isEmpty(getIntent().getStringExtra("data"))){
                requestParams.addBodyParameter("province",getIntent().getStringExtra("data"));
            }
        }
        if (!TextUtils.isEmpty(evolveTextString)) {
            if (!evolveTextString.equals("默认排序")){
                requestParams.addBodyParameter("year", evolveTextString);
            }

        }
        if (!TextUtils.isEmpty(orderText)) {
            if (!orderText.equals("默认排序")){
                requestParams.addBodyParameter("order", orderText);
            }

        }
        if (!TextUtils.isEmpty(MyAppliction.getLatitude())) {
            requestParams.addBodyParameter("latitude", MyAppliction.getLatitude());
        }
        if (!TextUtils.isEmpty(MyAppliction.getLongitude())) {
            requestParams.addBodyParameter("longitude", MyAppliction.getLongitude());
        }

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getMachinisData(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                //Log.e("找机手", responseInfo.result);
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
                        notDataLayout.setVisibility(View.GONE);
                    } else if ((appListDataBean.getResult()).equals("nomore")) {
                        homeServiceListAdapter.notifyDataSetChanged();
                        seekMachinistListview.onRefreshComplete();
                        //notDataLayout.setVisibility(View.GONE);
                        MyAppliction.showToast("已到最底了");
                    } else if ((appListDataBean.getResult()).equals("empty")) {
                        if (isPullDownRefresh) {
                            sekkMachinisDataBeens.clear();
                        }
                        homeServiceListAdapter.notifyDataSetChanged();
                        seekMachinistListview.onRefreshComplete();
                        //MyAppliction.showToast("没有更多数据");
                        notDataLayout.setVisibility(View.VISIBLE);
                        notDataImage.setBackgroundResource(R.mipmap.no_driver);
                        notDataText.setText("还没有找到机手哦");
                    }


                } else {
                    homeServiceListAdapter.notifyDataSetChanged();
                    seekMachinistListview.onRefreshComplete();
                }
                networkRemindLayout.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("找机手", s);
                networkRemindLayout.setVisibility(View.VISIBLE);
                seekMachinistListview.onRefreshComplete();
                if (sekkMachinisDataBeens.size()==0){
                    notDataLayout.setVisibility(View.VISIBLE);
                    notDataImage.setBackgroundResource(R.mipmap.no_wifi_icon);
                    notDataText.setText("没有网络哦");
                }
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
                intent.putExtra("seekData", sekkMachinisDataBeens.get(position - 1).getDriverId());
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                startActivity(intent);
            }
        });


    }


    public void intiPullToRefresh() {
        seekMachinistListview.setMode(PullToRefreshBase.Mode.BOTH);
        seekMachinistListview.setOnRefreshListener(this);
        ILoadingLayout endLabels = seekMachinistListview
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚上拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 上来达到一定距离时，显示的提示
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
        initListData(pageIndex);  //列表数据


    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndex++;
        isPullDownRefresh = false;
        initListData(pageIndex);  //列表数据

    }

    /**
     * 筛选
     * @param tage
     */

    private void showPopBtn( final int tage) {
        LayoutInflater inflater = LayoutInflater.from(SeekMachinistActivity.this);
        popupLayout = (LinearLayout) inflater.inflate(R.layout.secondhand_popupwindow_layout, null, false);
        rootListView = (ListView) popupLayout.findViewById(R.id.root_listview);
        popWindowDisminLayout= (LinearLayout) popupLayout.findViewById(R.id.popupwindow_dismin);
        LinearLayout selectRightLayout= (LinearLayout) popupLayout.findViewById(R.id.select_right_layout);
        LinearLayout selectMiddleLayout= (LinearLayout) popupLayout.findViewById(R.id.select_middle_layout);
        LinearLayout selectLeftLayout= (LinearLayout) popupLayout.findViewById(R.id.select_left_layout);
        LinearLayout selectMiddleLayoutOne= (LinearLayout) popupLayout.findViewById(R.id.select_middle_layout_one);
        /**
         * 初始化mPopupWindow
         */
        mPopupWindow = new PopupWindow(popupLayout, com.example.zhongjiyun03.zhongjiyun.popwindon.ScreenUtils.getScreenWidth(SeekMachinistActivity.this), ViewGroup.LayoutParams.MATCH_PARENT, true);

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
            selectLeftLayout.setVisibility(View.GONE);
            selectRightLayout.setVisibility(View.GONE);
            selectMiddleLayout.setVisibility(View.GONE);
            selectMiddleLayoutOne.setVisibility(View.VISIBLE);
        }else if (tage==2) {
            mPopupWindow.showAsDropDown(evolveLlayout, -5, 0);// 在控件下方显示popwindow
            selectMiddleLayoutOne.setVisibility(View.GONE);
            selectMiddleLayout.setVisibility(View.VISIBLE);
            selectLeftLayout.setVisibility(View.GONE);
            selectRightLayout.setVisibility(View.GONE);
        }else if (tage==4){
            mPopupWindow.showAsDropDown(orderLlayout, -5, 0);// 在控件下方显示popwindow
            selectMiddleLayout.setVisibility(View.GONE);
            selectLeftLayout.setVisibility(View.GONE);
            selectRightLayout.setVisibility(View.VISIBLE);
            selectMiddleLayoutOne.setVisibility(View.GONE);
        }else {
            mPopupWindow.showAsDropDown(facillyLayout, -5, 0);// 在控件下方显示popwindow
            selectLeftLayout.setVisibility(View.VISIBLE);
            selectRightLayout.setVisibility(View.GONE);
            selectMiddleLayout.setVisibility(View.GONE);
            selectMiddleLayoutOne.setVisibility(View.GONE);
        }
        mPopupWindow.update();

        final RootListViewAdapter adapter = new RootListViewAdapter(SeekMachinistActivity.this);
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
        }else {
            adapter.setTage(1);
            adapter.setItems(facillyDataBeens);
            subLayout.setVisibility(View.VISIBLE);

        }
        rootListView.setAdapter(adapter);
        rootListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if (tage==3){

                    SubListViewAdapter subAdapter = new SubListViewAdapter(
                            SeekMachinistActivity.this, provinceCityBeens.get(position).getProvinceCityChilds(), position,3);
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
                            pageIndex=1;
                            initListData(pageIndex);
                            isPullDownRefresh=true;


                        }
                    });
                }else if (tage==2){
                    SubListViewAdapter  subAdapter = new SubListViewAdapter(
                            SeekMachinistActivity.this, sortLists, position,2);
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
                    pageIndex=1;
                    initListData(pageIndex);
                    isPullDownRefresh=true;


                }else if (tage==4){
                    SubListViewAdapter  subAdapter = new SubListViewAdapter(
                            SeekMachinistActivity.this, sortLists, position,4);
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
                    pageIndex=1;
                    initListData(pageIndex);
                    isPullDownRefresh=true;


                }else {


                    SubListViewAdapter subAdapter = new SubListViewAdapter(
                            SeekMachinistActivity.this, facillyDataBeens.get(position).getChilds(), position,1);
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
                            facillyText.setTextColor(getResources().getColor(R.color.red_light));

                            Drawable drawable = getResources().getDrawable(R.mipmap.drop_down_icon_cur);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                            facillyText.setCompoundDrawables(null,null , drawable, null);//画在右边
                            manufacture=facillyDataBeens.get(selectedPosition).getText();
                            type=facillyDataBeens.get(selectedPosition).getChilds().get(position).getText();
                            if (type.equals("全部")){
                                if (manufacture.equals("全部")){
                                    facillyText.setText("全部");
                                }else {
                                    facillyText.setText(facillyDataBeens.get(selectedPosition).getText());
                                }
                            }else {
                                facillyText.setText(facillyDataBeens.get(selectedPosition).getText()+facillyDataBeens.get(selectedPosition).getChilds().get(position).getText());
                            }
                            pageIndex=1;
                            initListData(pageIndex);
                            isPullDownRefresh=true;
                        }
                    });



                }





            }
        });
    }

    private void initData() {
        provinceCityBeens=new ArrayList<>();
        orlder=new ArrayList<>();
        facillyDataBeens=new ArrayList<>();
        sortLists=new ArrayList<>();
        String[] strings={"默认排序","按工作年限由低到高","按工作年限由高到低","按距离由近到远","按距离由远到近"};
        for (int i = 0; i <strings.length ; i++) {
            orlder.add(strings[i]);
        }
        String[] evstring={"默认排序","一年以下","1-3年","3-5年","5-10年","10年以上"};
        for (int i = 0; i <evstring.length ; i++) {
            sortLists.add(evstring[i]);
        }
        ProvinceCityDataBean provinceCityDataBean=JSONObject.parseObject(SelectData.selectCityData+SelectData.selectCityDataOne+SelectData.selectCityDataTwo,new TypeReference<ProvinceCityDataBean>(){});
        if (provinceCityDataBean!=null){
            provinceCityBeens.addAll(provinceCityDataBean.getProvinceCity());
        }

        //设备厂商
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("DeviceJsonType","4");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getFacillyData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("设备厂商",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppListDataBean<FacillyDataBean> appListDataBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppListDataBean<FacillyDataBean>>(){});
                    if (appListDataBean.getResult().equals("success")){
                        List<FacillyDataBean> facillyDataBeen=  appListDataBean.getData();
                        if (facillyDataBeen.size()!=0){
                            facillyDataBeens.addAll(facillyDataBeen);
                        }


                    }



                }



            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }


    

    @Override
    public void onClick(View v) {
        SQLhelper sqLhelper=new SQLhelper(SeekMachinistActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        switch (v.getId()) {


            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.network_remind_layout:
                //跳转到设置界面
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                break;
            case R.id.register_tv:
                if (!TextUtils.isEmpty(uid)){
                    Intent releaseintent = new Intent(SeekMachinistActivity.this,ReleaseJobActivity.class);
                    startActivity(releaseintent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }else {
                    Intent releaseintent = new Intent(SeekMachinistActivity.this,LoginActivity.class);
                    startActivity(releaseintent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }

                break;
            case R.id.order_llayout:   //排序筛选
                showPopBtn(4);
                break;
            case R.id.evolve_llayout:  //
                showPopBtn(2);
                break;
            case R.id.address_llayout:  //地域筛选
                showPopBtn(3);
                break;
            case R.id.facility_llayout:  //品牌
                showPopBtn(1);

                break;
            default:
                break;
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

}
