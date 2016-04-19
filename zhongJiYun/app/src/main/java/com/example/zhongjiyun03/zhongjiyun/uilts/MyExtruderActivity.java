package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
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
        intiPullToRefresh();
        intiListView();
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
        //mSVProgressHUD.showWithStatus("加载中...");
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
                            myExtruderBeens.addAll(myExtruderBeen);

                        }else {
                            MyAppliction.showToast("您还没有添加钻机,请添加钻机");
                        }

                        extruderListView.onRefreshComplete();
                    }else if ((appBean.getResult()).equals("nomore")){
                        MyAppliction.showToast("已到底部");
                        extruderListView.onRefreshComplete();

                    }else  if ((appBean.getResult()).equals("empty")){
                        MyAppliction.showToast("没有更多数据");
                        extruderListView.onRefreshComplete();
                    }


                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                extruderListView.onRefreshComplete();
                MyAppliction.showToast(s);
                Log.e("我的钻机列表",s);
            }
        });


    }



    private void intiListView() {

        HomeExtruderListAdapter homeExtruderAdapter=new HomeExtruderListAdapter(myExtruderBeens,this);
        extruderListView.setAdapter(homeExtruderAdapter);
        //homeExtruderAdapter.notifyDataSetChanged();
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




        }




    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndext=1;
        myExtruderBeens.clear();
        initData(pageIndext);


    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndext++;
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
