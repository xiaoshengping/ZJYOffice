package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.HomeProjectListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.seekProject.SeekProjectBean;
import com.example.zhongjiyun03.zhongjiyun.bean.seekProject.SeekProjectDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.SelectData;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.SQLHelperUtils;
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
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class HomeMoreProjectActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener2<ListView>,View.OnClickListener {

    @ViewInject(R.id.retrun_text_view)
    private TextView retrunTextView;
    @ViewInject(R.id.title_name_tv)
    private TextView tailtNameTv;
    @ViewInject(R.id.register_tv)
    private TextView registerTv;

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
    private TextView orderTextView;
    private String evolveTextString;




    @ViewInject(R.id.project_list_view)
    private PullToRefreshListView projectListView;
    private int PageIndex=1;
    private List<SeekProjectBean> seekProjectBeans;
    private boolean isPullDownRefresh=true; //判断是下拉，还是上拉的标记
    private HomeProjectListAdapter homeProjectlsitAdapter;
    @ViewInject(R.id.not_data_layout)
    private LinearLayout notDataLayout; //没有数据
    @ViewInject(R.id.network_remind_layout)
    private LinearLayout networkRemindLayout; //没有网络
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
        setContentView(R.layout.activity_home_more_project);
        ViewUtils.inject(this);
        PopupWindowHelper.init(HomeMoreProjectActivity.this);
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
    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(MyAppliction.getProjectRequestTage())){
            if (MyAppliction.getProjectRequestTage().equals("login")){
                PageIndex=1;
                projectListView.setRefreshing();
                MyAppliction.setProjectRequestTage("project");
            }else if (MyAppliction.getProjectRequestTage().equals("competitive")){
                PageIndex=1;
                projectListView.setRefreshing();
                MyAppliction.setProjectRequestTage("project");
            }
        }else {
            MyAppliction.setProjectRequestTage("project");
        }
        JPushInterface.onResume(this);

    }

    private void inti() {
        initView();
        intiPullToRefresh();
        initListView();
        initFacility();

    }

    private void initView() {

        retrunTextView.setOnClickListener(this);
        tailtNameTv.setText("找活儿");
        registerTv.setVisibility(View.GONE);
        seekProjectBeans=new ArrayList<>();
        networkRemindLayout.setOnClickListener(this);
        addressLayout.setOnClickListener(this);
        evolveLlayout.setOnClickListener(this);
        orderLlayout.setOnClickListener(this);
        sortLists=new ArrayList<>();
        orlder=new ArrayList<>();
        provinceCityBeens=new ArrayList<>();
        evolveText.setText("项目进展");


    }

    private void initListData(int PageIndex) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        String uid= SQLHelperUtils.queryId(HomeMoreProjectActivity.this);  //用户id
        if (!TextUtils.isEmpty(uid)){
            requestParams.addBodyParameter("Id",uid);
            //步骤1：创建一个SharedPreferences接口对象
            SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
            //步骤2：获取文件中的值
            String sesstionId = read.getString("code","");
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        }
        requestParams.addBodyParameter("PageIndex",PageIndex+"");
        requestParams.addBodyParameter("PageSize","10");
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
        if (!TextUtils.isEmpty(evolveTextString)){
            if (evolveTextString.equals("招标中")){
                requestParams.addBodyParameter("State","1");
            }else if (evolveTextString.equals("已启动")){
                requestParams.addBodyParameter("State","2");
            }else if (evolveTextString.equals("已关闭")){
                requestParams.addBodyParameter("State","3");
            }
        }
        if (!TextUtils.isEmpty(orderText)){
            if (orderText.equals("时间由近到远")){
                requestParams.addBodyParameter("Order","0");
            }else if (orderText.equals("时间由远到近")){
                requestParams.addBodyParameter("Order","1");
            }
        }
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getProjecctListData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("找项目",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<SeekProjectDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<SeekProjectDataBean>>(){});
                    if ((appBean.getResult()).equals("success")){
                        SeekProjectDataBean seekProjectBeanList=appBean.getData();
                        if (seekProjectBeanList!=null){
                            List<SeekProjectBean> seekProjectBean= seekProjectBeanList.getPagerData();
                            if (seekProjectBean!=null){
                                if (isPullDownRefresh){
                                    seekProjectBeans.clear();
                                }
                                seekProjectBeans.addAll(seekProjectBean);
                                homeProjectlsitAdapter.notifyDataSetChanged();
                                projectListView.onRefreshComplete();

                            }
                        }else {
                            homeProjectlsitAdapter.notifyDataSetChanged();
                            projectListView.onRefreshComplete();
                        }
                        notDataLayout.setVisibility(View.GONE);
                    }else if ((appBean.getResult()).equals("nomore")){
                        MyAppliction.showToast("已到最底了");
                        homeProjectlsitAdapter.notifyDataSetChanged();
                        projectListView.onRefreshComplete();
                    }else  if ((appBean.getResult()).equals("empty")){
                        if (isPullDownRefresh) {
                            seekProjectBeans.clear();
                        }
                        notDataLayout.setVisibility(View.VISIBLE);
                        notDataImage.setBackgroundResource(R.mipmap.no_project_icon);
                        notDataText.setText("还没有找到项目哦");
                        homeProjectlsitAdapter.notifyDataSetChanged();
                        projectListView.onRefreshComplete();
                    }else {
                        MyAppliction.showToast(appBean.getMsg());
                    }


                }else {
                    homeProjectlsitAdapter.notifyDataSetChanged();
                    projectListView.onRefreshComplete();

                }
                networkRemindLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("找项目onFailure",s);
                networkRemindLayout.setVisibility(View.VISIBLE);
                homeProjectlsitAdapter.notifyDataSetChanged();
                projectListView.onRefreshComplete();
                if (seekProjectBeans.size()==0){
                notDataLayout.setVisibility(View.VISIBLE);
                notDataImage.setBackgroundResource(R.mipmap.no_wifi_icon);
                notDataText.setText("没有网络哦");
                }

            }
        });




    }
    private void initListView() {

       homeProjectlsitAdapter = new HomeProjectListAdapter(seekProjectBeans, HomeMoreProjectActivity.this);
        projectListView.setAdapter(homeProjectlsitAdapter);
        //homeProjectlsitAdapter.notifyDataSetChanged();
        projectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeMoreProjectActivity.this, SeekProjectParticularsActivity.class);
                intent.putExtra("seekProjectId",seekProjectBeans.get(position-1).getId());
                startActivity(intent);
            }
        });


    }
    public void intiPullToRefresh(){
        projectListView.setMode(PullToRefreshBase.Mode.BOTH);
        projectListView.setOnRefreshListener(this);
        ILoadingLayout endLabels  = projectListView
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚上拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 上来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = projectListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        projectListView.setRefreshing();

    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                 PageIndex=1;
                 isPullDownRefresh=true;
                 initListData(PageIndex);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        PageIndex++;
        isPullDownRefresh=false;
        initListData(PageIndex);

    }

    @Override
    public void onClick(View v) {

         switch (v.getId()){
             case R.id.retrun_text_view://返回
                 finish();
                 break;
             case R.id.network_remind_layout: //跳转到设置界面
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


    /**
     * 筛选
     * @param tage
     */

    private void showPopBtn( final int tage) {
        LayoutInflater inflater = LayoutInflater.from(HomeMoreProjectActivity.this);
        popupLayout = (LinearLayout) inflater.inflate(R.layout.project_popupwindow_layout, null, false);
        rootListView = (ListView) popupLayout.findViewById(R.id.root_listview);
        popWindowDisminLayout= (LinearLayout) popupLayout.findViewById(R.id.popupwindow_dismin);
        LinearLayout selectRightLayout= (LinearLayout) popupLayout.findViewById(R.id.select_right_layout);
        LinearLayout selectMiddleLayout= (LinearLayout) popupLayout.findViewById(R.id.select_middle_layout);
        LinearLayout selectLeftLayout= (LinearLayout) popupLayout.findViewById(R.id.select_left_layout);
        /**
         * 初始化mPopupWindow
         */
        mPopupWindow = new PopupWindow(popupLayout, ScreenUtils.getScreenWidth(HomeMoreProjectActivity.this),ViewGroup.LayoutParams.MATCH_PARENT, true);

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

        final RootListViewAdapter adapter = new RootListViewAdapter(HomeMoreProjectActivity.this);
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
                            HomeMoreProjectActivity.this, provinceCityBeens.get(position).getProvinceCityChilds(), position,3);
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
                            PageIndex=1;
                            initListData(PageIndex);
                            isPullDownRefresh=true;


                        }
                    });
                }else if (tage==2){
                    SubListViewAdapter  subAdapter = new SubListViewAdapter(
                            HomeMoreProjectActivity.this, sortLists, position,2);
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
                    PageIndex=1;
                    initListData(PageIndex);
                    isPullDownRefresh=true;


                }else if (tage==4){
                    SubListViewAdapter  subAdapter = new SubListViewAdapter(
                            HomeMoreProjectActivity.this, sortLists, position,4);
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
                    PageIndex=1;
                    initListData(PageIndex);
                    isPullDownRefresh=true;


                }





            }
        });
    }
    /**
     *
     * 筛选数据
     */
    private void initFacility() {
        String[] strings={"默认排序","时间由近到远","时间由远到近"};
        for (int i = 0; i <strings.length ; i++) {
            orlder.add(strings[i]);
        }
        String[] evstring={"默认排序","招标中","已启动","已关闭"};
        for (int i = 0; i <evstring.length ; i++) {
            sortLists.add(evstring[i]);
        }
        ProvinceCityDataBean provinceCityDataBean=JSONObject.parseObject(SelectData.selectCityData+SelectData.selectCityDataOne+SelectData.selectCityDataTwo,new TypeReference<ProvinceCityDataBean>(){});
        if (provinceCityDataBean!=null){
            provinceCityBeens.addAll(provinceCityDataBean.getProvinceCity());
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
