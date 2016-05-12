package com.example.zhongjiyun03.zhongjiyun.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.HomeProjectListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.seekProject.SeekProjectBean;
import com.example.zhongjiyun03.zhongjiyun.bean.seekProject.SeekProjectDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityChildsBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.ProvinceCityDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.select.SelectData;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.example.zhongjiyun03.zhongjiyun.popwin.FirstClassAdapter;
import com.example.zhongjiyun03.zhongjiyun.popwin.PopupWindowHelper;
import com.example.zhongjiyun03.zhongjiyun.popwin.ScreenUtils;
import com.example.zhongjiyun03.zhongjiyun.popwin.SecondClassAdapter;
import com.example.zhongjiyun03.zhongjiyun.uilts.SeekProjectParticularsActivity;
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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeekProjectFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2<ListView>,View.OnClickListener {


       //排序
      @ViewInject(R.id.time_text_view)
      private TextView sortButton; //排序选项
      PopupWindow popupWindowTime;
      private List<String> list;
      int cur_pos = -1;// 当前显示的一行
      int popTag=1;

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


    @ViewInject(R.id.project_list_view)
    private PullToRefreshListView projectListView;
    private int PageIndex=1;  //页数
    private String provinceName;  //省份
    private String cityName;  //城市
    private String State;//项目状态
    private String Order ;//排序
    private RequestParams requestParams;
    private SeekProjectDataBean seekProjectBeanList;
    private List<SeekProjectBean> seekProjectBeens;
    private HomeProjectListAdapter homeProjectlsitAdapter;
    private boolean isPullDownRefresh=true; //判断是下拉，还是上拉的标记
    private String province;
    @ViewInject(R.id.not_data_layout)
    private LinearLayout notDataLayout;
    @ViewInject(R.id.network_remind_layout)
    private LinearLayout networkRemindLayout;




    public SeekProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_seek_project, container, false);
        ViewUtils.inject(this,view);
        PopupWindowHelper.init(getActivity());
        inti(view);

        return view;
    }

    private void inti(final View view) {
        seekProjectBeens=new ArrayList<>();
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSortPopupWindow(sortButton);
                popTag=1;

            }
        });
        evolveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showEvolvePopupWindow(evolveButton);
                popTag=2;
            }
        });
        networkRemindLayout.setOnClickListener(this);
        findView(view);
        initData();
        initPopup();
        OnClickListenerImpl l = new OnClickListenerImpl();
        mainTab1TV.setOnClickListener(l);
        initListView();
        intiPullToRefresh();






    }

    @Override
    public void onResume()  {
        super.onResume();
        if (!TextUtils.isEmpty(MyAppliction.getProjectRequestTage())){
            if (MyAppliction.getProjectRequestTage().equals("login")){
                projectListView.setRefreshing();
                MyAppliction.setProjectRequestTage("project");
            }else if (MyAppliction.getProjectRequestTage().equals("competitive")){
                projectListView.setRefreshing();
                MyAppliction.setProjectRequestTage("project");
            }
        }else {
            MyAppliction.setProjectRequestTage("project");
        }


    }

    private void initListData(int PageIndex , String cityName, String State , String Order) {
        HttpUtils httpUtils=new HttpUtils();
        requestParams=new RequestParams();
        SQLhelper sqLhelper=new SQLhelper(getActivity());
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id

        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        if (!TextUtils.isEmpty(uid)){
            requestParams.addBodyParameter("Id",uid);
            //步骤1：创建一个SharedPreferences接口对象
            SharedPreferences read = getActivity().getSharedPreferences("lock", getActivity().MODE_WORLD_READABLE);
            //步骤2：获取文件中的值
            String sesstionId = read.getString("code","");
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        }

        requestParams.addBodyParameter("PageIndex",PageIndex+"");
        requestParams.addBodyParameter("PageSize","10");

       if (!TextUtils.isEmpty(cityName)){
           if (province.equals("全部")){

           }else {
               if (cityName.equals("全部")){
                   requestParams.addBodyParameter("province",province);
               }else {
                   requestParams.addBodyParameter("City",cityName);
               }

           }


        }

        if (!TextUtils.isEmpty(State)){
            Log.e("项目状态",State);
            requestParams.addBodyParameter("State",State);
        }
        if (!TextUtils.isEmpty(Order)){
            requestParams.addBodyParameter("Order",Order);
        }

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getProjecctListData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("找项目",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<SeekProjectDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<SeekProjectDataBean>>(){});
                    if ((appBean.getResult()).equals("success")){
                        seekProjectBeanList=appBean.getData();
                        if (seekProjectBeanList!=null){
                            List<SeekProjectBean> seekProjectBean= seekProjectBeanList.getPagerData();
                            if (isPullDownRefresh){
                                seekProjectBeens.clear();
                                //Log.e("sdhdhdh","jjdfj");
                            }
                            seekProjectBeens.addAll(seekProjectBean);
                            homeProjectlsitAdapter.notifyDataSetChanged();
                            projectListView.onRefreshComplete();
                        }
                        notDataLayout.setVisibility(View.GONE);
                    }else  if ((appBean.getResult()).equals("empty")){
                        //MyAppliction.showToast("没有更多数据");
                        projectListView.onRefreshComplete();
                        homeProjectlsitAdapter.notifyDataSetChanged();
                        notDataLayout.setVisibility(View.VISIBLE);
                    }else if ((appBean.getResult()).equals("nomore")){
                        MyAppliction.showToast("已到最底了");
                        projectListView.onRefreshComplete();
                        homeProjectlsitAdapter.notifyDataSetChanged();
                        //notDataLayout.setVisibility(View.GONE);
                    }


                }else {
                    projectListView.onRefreshComplete();
                }
                networkRemindLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("找项目",s);
                networkRemindLayout.setVisibility(View.VISIBLE);
                projectListView.onRefreshComplete();
            }
        });




    }
    public void intiPullToRefresh(){
        projectListView.setMode(PullToRefreshBase.Mode.BOTH);
        projectListView.setOnRefreshListener(this);
        ILoadingLayout endLabels  = projectListView
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = projectListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        projectListView.setRefreshing();

    }

    private void initListView() {
        homeProjectlsitAdapter=new HomeProjectListAdapter(seekProjectBeens,getActivity());
        projectListView.setAdapter(homeProjectlsitAdapter);
        projectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), SeekProjectParticularsActivity.class);
                intent.putExtra("seekProjectId",seekProjectBeens.get(position-1).getId());
                startActivity(intent);
                //getActivity().overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
            }
        });



    }
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        PageIndex=1;
        isPullDownRefresh=true;
        initListData(PageIndex,cityName,State,Order);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
         PageIndex++;
        isPullDownRefresh=false;
        initListData(PageIndex,cityName,State,Order);
    }
    private void findView(View view) {
        mainTab1TV = (TextView) view.findViewById(R.id.main_tab1);
        darkView = view.findViewById(R.id.main_darkview);
        animIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_anim);
        animOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_anim);
    }

    private void initData() {

        ProvinceCityDataBean provinceCityDataBean=JSONObject.parseObject(SelectData.selectCityData+SelectData.selectCityDataOne+SelectData.selectCityDataTwo,new TypeReference<ProvinceCityDataBean>(){});
        if (provinceCityDataBean!=null){
            firstList=provinceCityDataBean.getProvinceCity();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.network_remind_layout:
                //跳转到设置界面
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                break;

        }
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
        popupWindow = new PopupWindow(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_layout, null);
        leftLV = (ListView) view.findViewById(R.id.pop_listview_left);
        rightLV = (ListView) view.findViewById(R.id.pop_listview_right);

        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new PaintDrawable());
        popupWindow.setFocusable(true);

        popupWindow.setHeight(ScreenUtils.getScreenH(getActivity()) * 2 / 3);
        popupWindow.setWidth(ScreenUtils.getScreenW(getActivity()));

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
        final FirstClassAdapter firstAdapter = new FirstClassAdapter(getActivity(), firstList);
        leftLV.setAdapter(firstAdapter);

        //加载左侧第一行对应右侧二级分类
        secondList = new ArrayList<ProvinceCityChildsBean>();
        secondList.addAll(firstList.get(0).getProvinceCityChilds());
        final SecondClassAdapter secondAdapter = new SecondClassAdapter(getActivity(), secondList);
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

                    String firstId = firstList.get(position).getName();
                    String selectedName = firstList.get(position).getName();
                    handleResult(firstId, "-1", selectedName);
                    return;
                }

                FirstClassAdapter adapter = (FirstClassAdapter) (parent.getAdapter());
                //如果上次点击的就是这一个item，则不进行任何操作
                if (adapter.getSelectedPosition() == position){
                    return;
                }
                 provinceName=firstList.get(position).getName();
               /* if (!TextUtils.isEmpty(provinceName)){
                    requestParams.addBodyParameter("Province",firstList.get(position).getName());
                }*/

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
                String  secondId = firstList.get(firstPosition).getProvinceCityChilds().get(position).getId();
                String selectedName =firstList.get(firstPosition).getProvinceCityChilds().get(position)
                        .getName();
                handleResult(firstId, secondId, selectedName);
                /*if (!TextUtils.isEmpty(cityName)){
                    requestParams.addBodyParameter("City",selectedName);
                }*/


            }
        });
    }

    //顶部第一个标签的点击事件
    private void tab1OnClick() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            popupWindow.showAsDropDown(getActivity().findViewById(R.id.main_div_line));
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
        //Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        province=firstId;
        cityName=selectedName;
        mainTab1TV.setText(selectedName);
        mainTab1TV.setTextColor(getResources().getColor(R.color.red_light));
        Drawable img = getResources().getDrawable(R.mipmap.select_arrow_cur);
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        mainTab1TV.setCompoundDrawables(null, null, img, null);
        seekProjectBeens.clear();
        PageIndex=1;
        initListData(PageIndex,cityName,State,Order);
        projectListView.setRefreshing();
    }



    public void showSortPopupWindow(View parent) {
        LinearLayout layout;
        ListView listView;
        list=new ArrayList<>();
        list.add("默认排序");
        list.add("按距离由近到远");
        list.add("按距离由远到近");


        //加载布局
        layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
                R.layout.pop_time_contant_layout, null);
        //找到布局的控件
        listView = (ListView) layout.findViewById(R.id.pop_listview);
        //设置适配器
        //popWinListAdapter=new PopWinListAdapter(list,HomeMoreProjectActivity.this,cur_pos);
        MyAdapter myAdapter=new MyAdapter(getActivity());
        listView.setAdapter(myAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);// 一定要设置这个属性，否则ListView不会刷新
        myAdapter.notifyDataSetChanged();
        // 实例化popupWindow
        popupWindowTime = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //控制键盘是否可以获得焦点
        popupWindowTime.setFocusable(true);
        //设置popupWindow弹出窗体的背景
        popupWindowTime.setBackgroundDrawable(new BitmapDrawable(null, ""));
        WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
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
                cur_pos=arg2;

                if (list.get(arg2).equals("默认排序")){
                    Order="";

                }else {
                    Order=arg2+"";
                }
                seekProjectBeens.clear();
                PageIndex=1;
                initListData(PageIndex,cityName,State,Order);
                projectListView.setRefreshing();

            }


        });



    }
    public void showEvolvePopupWindow(View parent) {
        LinearLayout layout;
        ListView listView;
        list=new ArrayList<>();
        list.add("默认排序");
        list.add("招标中");
        list.add("已启动");


        //加载布局
        layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
                R.layout.pop_time_contant_layout, null);
        //找到布局的控件
        listView = (ListView) layout.findViewById(R.id.pop_listview);
        //设置适配器
        //popWinListAdapter=new PopWinListAdapter(list,HomeMoreProjectActivity.this,cur_pos);
        MyAdapter myAdapter=new MyAdapter(getActivity());
        listView.setAdapter(myAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);// 一定要设置这个属性，否则ListView不会刷新
        myAdapter.notifyDataSetChanged();
        // 实例化popupWindow
        popupWindowTime = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //控制键盘是否可以获得焦点
        popupWindowTime.setFocusable(true);
        //设置popupWindow弹出窗体的背景
        popupWindowTime.setBackgroundDrawable(new BitmapDrawable(null, ""));
        WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
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
                cur_evolve_pos=arg2;
                if (list.get(arg2).equals("默认排序")){
                    State="";

                }else {
                    State=arg2+"";
                }
                seekProjectBeens.clear();
                PageIndex=1;
                initListData(PageIndex,cityName,State,Order);
                projectListView.setRefreshing();

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



}
