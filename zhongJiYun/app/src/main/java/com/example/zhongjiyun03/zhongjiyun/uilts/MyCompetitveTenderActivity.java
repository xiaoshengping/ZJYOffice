package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.zhongjiyun03.zhongjiyun.adapter.MyCompetitveTenderListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.main.ProjectlistBean;
import com.example.zhongjiyun03.zhongjiyun.bean.main.ProjectlistDataBean;
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

public class MyCompetitveTenderActivity extends AppCompatActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2<ListView> {

    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边

    @ViewInject(R.id.competitve_tender_lsitview)
    private PullToRefreshListView competitveTenderLsitview;
    private int pageIndex=1;
    List<ProjectlistDataBean> projectlistDataBeanLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_competitve_tender);
        ViewUtils.inject(this);
        init();
        

    }

    private void init() {
        initView();
        intiPullToRefresh();
        initListView();


    }

    @Override
    protected void onResume() {
        super.onResume();
        competitveTenderLsitview.setRefreshing();
    }

    private void initListData(int pageIndex) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        SQLhelper sqLhelper=new SQLhelper(MyCompetitveTenderActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id

        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }

        if (!TextUtils.isEmpty(uid)){
            //步骤1：创建一个SharedPreferences接口对象
            SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
            //步骤2：获取文件中的值
            String sesstionId = read.getString("code","");
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
            requestParams.addBodyParameter("Id",uid);
            requestParams.addBodyParameter("pageIndex",pageIndex+"");
            requestParams.addBodyParameter("pageSize","10");
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getCompetitvetListData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("我的竞标",responseInfo.result);
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        AppBean<ProjectlistBean> appBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<ProjectlistBean>>(){});
                        if (( appBean.getResult()).equals("success")){
                            ProjectlistBean projectlistBean=  appBean.getData();
                            if (projectlistBean!=null){
                                projectlistDataBeanLists.addAll(projectlistBean.getPagerData());
                                competitveTenderLsitview.onRefreshComplete();
                            }

                        }else if (( appBean.getResult()).equals("nomore")){
                            MyAppliction.showToast("已到最底了");
                            competitveTenderLsitview.onRefreshComplete();
                        }else if ((appBean.getResult()).equals("empty")){
                            //secondHandBeen.clear();
                            competitveTenderLsitview.onRefreshComplete();
                            MyAppliction.showToast("没有更多数据");
                        }


                    }else {
                        competitveTenderLsitview.onRefreshComplete();
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.e("我的竞标",s);
                    competitveTenderLsitview.onRefreshComplete();
                }
            });
        }else {
            finish();
            MyAppliction.showToast("数据加载失败");
        }

    }

    private void initListView() {
        MyCompetitveTenderListAdapter myCompetitveAdapter=new MyCompetitveTenderListAdapter(projectlistDataBeanLists,this);
        competitveTenderLsitview.setAdapter(myCompetitveAdapter);
        myCompetitveAdapter.notifyDataSetChanged();
        competitveTenderLsitview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MyCompetitveTenderActivity.this, SeekProjectParticularsActivity.class);
                intent.putExtra("seekProjectId",projectlistDataBeanLists.get(position-1).getProjectId());
                startActivity(intent);
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
            }
        });



    }

    public void intiPullToRefresh(){
        competitveTenderLsitview.setMode(PullToRefreshBase.Mode.BOTH);
        competitveTenderLsitview.setOnRefreshListener(this);
        ILoadingLayout endLabels  = competitveTenderLsitview
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = competitveTenderLsitview
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        competitveTenderLsitview.setRefreshing();

    }


    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("我的竞标");
        retrunText.setOnClickListener(this);
        projectlistDataBeanLists=new ArrayList<>();


    }
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndex=1;
        projectlistDataBeanLists.clear();
        initListData(pageIndex);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndex++;

        initListData(pageIndex);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

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
