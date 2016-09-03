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
import com.example.zhongjiyun03.zhongjiyun.adapter.HomeSecondHandListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.SecondHandDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.AppListDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.SecondHandBean;
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
import com.example.zhongjiyun03.zhongjiyun.popwindon.ScreenUtils;
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


    private HomeSecondHandListAdapter homeSecondHandListAdapter;
    private boolean isPullDownRefresh = true; //判断是下拉，还是上拉的标记
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
    private AppBean<SecondHandDataBean> appListDataBean;  //列表数据
    @ViewInject(R.id.drilling_select_layout)
    private RelativeLayout drillingSelectLayout; //筛选布局
    private int pageIndex = 1; //page
    private List<SecondHandBean> secondHandBeens;  //列表数据

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
        secondHandBeens = new ArrayList<>();
        addressLayout.setOnClickListener(this);
        evolveLlayout.setOnClickListener(this);
        orderLlayout.setOnClickListener(this);
        facillyLayout.setOnClickListener(this);
        shareTextView.setOnClickListener(this);
        titleName.setText("二手机");
        retrunText.setOnClickListener(this);
        intiListView();
        intiPullToRefresh();
        networkRemindLayout.setOnClickListener(this);
        secondHandHandler=new SecondHandHandler();

        if (getIntent().getStringExtra("tage").equals("matingFacily")){
            drillingSelectLayout.setVisibility(View.GONE);
        }else {
            drillingSelectLayout.setVisibility(View.VISIBLE);
        }
        initData();






    }


    public void intiPullToRefresh() {
        secondHandListview.setMode(PullToRefreshBase.Mode.BOTH);
        secondHandListview.setOnRefreshListener(this);
        ILoadingLayout endLabels = secondHandListview
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚上拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 上来达到一定距离时，显示的提示
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
            case R.id.order_llayout:   //排序筛选
                showPopBtn(4);
                break;
            case R.id.evolve_llayout:  //项目进展
                showPopBtn(2);
                break;
            case R.id.address_llayout:  //地域筛选
                showPopBtn(3);
                break;
            case R.id.facility_llayout:
                showPopBtn(1);

                break;
            default:
                break;



        }
    }
    
    
    

    private void initListData(final int pageIndex) {
        HttpUtils httpUtils = new HttpUtils();
        final RequestParams requestParams = new RequestParams();
        String uid= SQLHelperUtils.queryId(SecondHandActivity.this);
        if (!TextUtils.isEmpty(uid)) {
            requestParams.addBodyParameter("Id", uid);
            //步骤1：创建一个SharedPreferences接口对象
            SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
            //步骤2：获取文件中的值
            String sesstionId = read.getString("code", "");
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        }

        if (getIntent().getStringExtra("tage").equals("matingFacily")) {
            if (!TextUtils.isEmpty(getIntent().getStringExtra("data"))) {
                requestParams.addBodyParameter("province", getIntent().getStringExtra("data").toString());
            }

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
        if (!TextUtils.isEmpty(evolveTextString)) {
            if (!evolveTextString.equals("全部")) {
                requestParams.addBodyParameter("year", evolveTextString);
            }
        }
        if (!TextUtils.isEmpty(orderText)) {
            if (!orderText.equals("默认排序")) {
                requestParams.addBodyParameter("order", orderText);
            }
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
                //Log.e("二手钻机", responseInfo.result);
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
        initListData(pageIndex);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
         pageIndex++;
        isPullDownRefresh = false;
        initListData(pageIndex);


    }


    /**
     * 筛选
     * @param tage
     */

    private void showPopBtn( final int tage) {
        LayoutInflater inflater = LayoutInflater.from(SecondHandActivity.this);
        popupLayout = (LinearLayout) inflater.inflate(R.layout.project_popupwindow_layout, null, false);
        rootListView = (ListView) popupLayout.findViewById(R.id.root_listview);
        popWindowDisminLayout= (LinearLayout) popupLayout.findViewById(R.id.popupwindow_dismin);
        LinearLayout selectRightLayout= (LinearLayout) popupLayout.findViewById(R.id.select_right_layout);
        LinearLayout selectMiddleLayout= (LinearLayout) popupLayout.findViewById(R.id.select_middle_layout);
        LinearLayout selectLeftLayout= (LinearLayout) popupLayout.findViewById(R.id.select_left_layout);
        LinearLayout selectMiddleLayoutOne= (LinearLayout) popupLayout.findViewById(R.id.select_middle_layout_one);
        /**
         * 初始化mPopupWindow
         */
        mPopupWindow = new PopupWindow(popupLayout, ScreenUtils.getScreenWidth(SecondHandActivity.this), ViewGroup.LayoutParams.MATCH_PARENT, true);

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

        final RootListViewAdapter adapter = new RootListViewAdapter(SecondHandActivity.this);
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
                            SecondHandActivity.this, provinceCityBeens.get(position).getProvinceCityChilds(), position,3);
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
                            SecondHandActivity.this, sortLists, position,2);
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
                            SecondHandActivity.this, sortLists, position,4);
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
                            SecondHandActivity.this, facillyDataBeens.get(position).getChilds(), position,1);
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
        String[] strings={"默认排序","按价格从低到高","按价格从高到低","按距离由近到远","按距离由远到近"};
        for (int i = 0; i <strings.length ; i++) {
            orlder.add(strings[i]);
        }
        String[] evstring={"全部","2016","2015","2014","2013","2012","2011","2010","2009","2008","2007","2006","2005","2004","2003","2002","2001","2000"};
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
