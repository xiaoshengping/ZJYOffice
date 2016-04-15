package com.example.zhongjiyun03.zhongjiyun.uilts;

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
import com.example.zhongjiyun03.zhongjiyun.bean.main.ProjectlistBean;
import com.example.zhongjiyun03.zhongjiyun.bean.main.ProjectlistDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
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

    private void initListData(int pageIndex) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("Id","3aef950d-8b27-46a7-a04b-3329faf5e9f6");
        requestParams.addBodyParameter("pageIndex",pageIndex+"");
        requestParams.addBodyParameter("pageSize","10");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getCompetitvetListData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("我的竞标",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    ProjectlistBean projectlistBean= JSONObject.parseObject(responseInfo.result,new TypeReference<ProjectlistBean>(){});
                    if (( projectlistBean.getResult()).equals("success")){
                      List<ProjectlistDataBean> projectlistDataBeanList=  projectlistBean.getProjectlist();
                        if (projectlistDataBeanList!=null){
                            projectlistDataBeanLists.addAll(projectlistDataBeanList);
                            competitveTenderLsitview.onRefreshComplete();
                        }

                    }


                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });





    }

    private void initListView() {



        MyCompetitveTenderListAdapter myCompetitveAdapter=new MyCompetitveTenderListAdapter(projectlistDataBeanLists,this);
        competitveTenderLsitview.setAdapter(myCompetitveAdapter);
        myCompetitveAdapter.notifyDataSetChanged();
        competitveTenderLsitview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Intent intent=new Intent(MyCompetitveTenderActivity.this, SeekProjectParticularsActivity.class);
                intent.putExtra("seekProjectData",projectlistDataBeanList.get(position-1));
                startActivity(intent);
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);*/
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
