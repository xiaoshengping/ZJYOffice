package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.HomeExtruderListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.MyExtruderBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.MyExtruderDataBean;
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

import java.util.ArrayList;
import java.util.List;

/*
* 我的钻机
* */
public class MyExtruderActivity extends AppCompatActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2<ListView> {
       //列表
      @ViewInject(R.id.extruder_list_view)
      private PullToRefreshListView extruderListView;
      //添加钻机
      @ViewInject(R.id.register_tv)
      private TextView addExtruderTv;
      @ViewInject(R.id.title_name_tv)
      private TextView titleNemeTv;
      @ViewInject(R.id.retrun_text_view)
      private TextView retrunText;
      private int pageIndext=1;
      private List<MyExtruderBean> myExtruderBeens;
      private boolean isPullDownRefresh=true; //判断是下拉，还是上拉的标记
      private HomeExtruderListAdapter homeExtruderAdapter;
      @ViewInject(R.id.not_data_layout)
      private LinearLayout notDataLayout;
      @ViewInject(R.id.network_remind_layout)
      private LinearLayout networkRemindLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_extruder);
        ViewUtils.inject(this);
        inti();



    }

    private void inti() {
        myExtruderBeens=new ArrayList<>();
        addExtruderTv.setOnClickListener(this);
        Drawable img = getResources().getDrawable(R.mipmap.add_icon);
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        addExtruderTv.setCompoundDrawables(img, null, null, null);
        addExtruderTv.setCompoundDrawablePadding(10);
        addExtruderTv.setText("新增");
        titleNemeTv.setText("我的钻机");
        retrunText.setOnClickListener(this);
        networkRemindLayout.setOnClickListener(this);
        intiListView();
        intiPullToRefresh();

    }

    @Override
    protected void onResume() {
        super.onResume();
        extruderListView.setRefreshing();

    }

    private void initData(int pageIndex) {
        SQLhelper sqLhelper=new SQLhelper(MyExtruderActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        HttpUtils httpUtils=new HttpUtils();
        final RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("PageIndex",pageIndex+"");
        requestParams.addBodyParameter("PageSize","10");
        requestParams.addBodyParameter("Id",uid);
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        if (!TextUtils.isEmpty(sesstionId)){
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getMyExtruderListData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("我的钻机列表",responseInfo.result);
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        AppBean<MyExtruderDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<MyExtruderDataBean>>(){});
                        if ((appBean.getResult()).equals("success")){
                            MyExtruderDataBean myExtruderDataBean=appBean.getData();
                            List<MyExtruderBean> myExtruderBeen= myExtruderDataBean.getPagerData();
                            if (myExtruderBeen!=null){
                                if (isPullDownRefresh){
                                    myExtruderBeens.clear();
                                }
                                myExtruderBeens.addAll(myExtruderBeen);
                                homeExtruderAdapter.notifyDataSetChanged();
                                extruderListView.onRefreshComplete();

                            }else {
                                homeExtruderAdapter.notifyDataSetChanged();
                                extruderListView.onRefreshComplete();
                               // MyAppliction.showToast("您还没有添加钻机,请添加钻机");
                            }
                            notDataLayout.setVisibility(View.GONE);

                        }else if ((appBean.getResult()).equals("nomore")){
                            MyAppliction.showToast("已到最底了");
                            homeExtruderAdapter.notifyDataSetChanged();
                            extruderListView.onRefreshComplete();
                            //notDataLayout.setVisibility(View.GONE);
                        }else  if ((appBean.getResult()).equals("empty")){
                            MyAppliction.showToast("没有更多数据");
                            if (isPullDownRefresh){
                                myExtruderBeens.clear();
                            }
                            homeExtruderAdapter.notifyDataSetChanged();
                            extruderListView.onRefreshComplete();
                            notDataLayout.setVisibility(View.VISIBLE);
                        }else if (appBean.getResult().equals("unlogin")){
                            MyAppliction.showToast(appBean.getMsg());
                        }


                    }
                    networkRemindLayout.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    extruderListView.onRefreshComplete();
                    networkRemindLayout.setVisibility(View.VISIBLE);
                    Log.e("我的钻机列表",s);
                }
            });
        }else {
            MyAppliction.showToast("数据加载失败");
        }
        //mSVProgressHUD.showWithStatus("加载中...");



    }



    private void intiListView() {

        homeExtruderAdapter=new HomeExtruderListAdapter(myExtruderBeens,this,extruderListView);
        extruderListView.setAdapter(homeExtruderAdapter);
        extruderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MyExtruderActivity.this,MyExtruderParticularsActivity.class);
                intent.putExtra("myExtruderData",myExtruderBeens.get(position-1));
                startActivity(intent);
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
            }
        });



    }
    public void intiPullToRefresh(){
        extruderListView.setMode(PullToRefreshBase.Mode.BOTH);
        extruderListView.setOnRefreshListener(this);
        ILoadingLayout endLabels  = extruderListView
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = extruderListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        extruderListView.setRefreshing();

    }

    @Override
    public void onClick(View v) {
        SQLhelper sqLhelper=new SQLhelper(MyExtruderActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);
        }
        switch (v.getId()){
            case R.id.register_tv:
                if (!TextUtils.isEmpty(uid)){
                    Intent addExtruderIntent=new Intent(MyExtruderActivity.this,AddExtruderActivity.class);
                    startActivity(addExtruderIntent);
                    overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                }else {
                    Intent intent=new Intent(MyExtruderActivity.this,LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                }

                break;
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
