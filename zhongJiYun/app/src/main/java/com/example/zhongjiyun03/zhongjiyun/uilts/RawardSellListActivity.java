package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.RawardBuyListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.RawardBuyListBean;
import com.example.zhongjiyun03.zhongjiyun.bean.RawardBuyListPagerBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
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

public class RawardSellListActivity extends AppCompatActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2<ListView> {


    @ViewInject(R.id.register_tv)
    private TextView comtintJobText;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边


    @ViewInject(R.id.release_job_list_view)
    private PullToRefreshListView releaseJobListView;
    private int PageIndex=1;
    private List<RawardBuyListBean> releaseJobListBeens;
    private boolean isPullDownRefresh=true; //判断是下拉，还是上拉的标记
    private RawardBuyListAdapter releaseJobListAdapter; //adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raward_sell_list);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {
        titleNemeTv.setText("悬赏求卖");
        comtintJobText.setVisibility(View.GONE);
        retrunText.setOnClickListener(this);
        releaseJobListBeens=new ArrayList<>();
        intiPullToRefresh();
        initListView();


    }

    private void initListView() {

        releaseJobListAdapter = new RawardBuyListAdapter(releaseJobListBeens, RawardSellListActivity.this,2);
        releaseJobListView.setAdapter(releaseJobListAdapter);
        releaseJobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(RawardSellListActivity.this,RawardBuyListParticularsActivity.class);
                intent.putExtra("sellId",releaseJobListBeens.get(position-1).getId());
                intent.putExtra("tage","sell");
                startActivity(intent);
            }
        });



    }

    private void initListData(int pageIndex) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("pageIndex",pageIndex+"");
        requestParams.addBodyParameter("pageSize","10");
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRawardSellListData(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("我的招聘列表",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<RawardBuyListPagerBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<RawardBuyListPagerBean>>(){});
                    if (appBean.getResult().equals("success")){
                        RawardBuyListPagerBean releaseJobPagerBean=appBean.getData();
                        if (releaseJobPagerBean!=null){
                            List<RawardBuyListBean> releaseJobListBeen= releaseJobPagerBean.getPagerData();
                            if (releaseJobListBeen!=null){
                                if (isPullDownRefresh){
                                    releaseJobListBeens.clear();
                                }
                                releaseJobListBeens.addAll(releaseJobListBeen);
                                releaseJobListAdapter.notifyDataSetChanged();
                                releaseJobListView.onRefreshComplete();

                            }
                        }else {
                            releaseJobListAdapter.notifyDataSetChanged();
                            releaseJobListView.onRefreshComplete();
                        }
                        // notDataLayout.setVisibility(View.GONE);
                    }else if ((appBean.getResult()).equals("nomore")){
                        MyAppliction.showToast("已到最底了");
                        releaseJobListAdapter.notifyDataSetChanged();
                        releaseJobListView.onRefreshComplete();
                        //notDataLayout.setVisibility(View.GONE);
                    }else  if ((appBean.getResult()).equals("empty")){
                        //MyAppliction.showToast("没有更多数据");
                        if (isPullDownRefresh) {
                            releaseJobListBeens.clear();
                        }
                            /*notDataLayout.setVisibility(View.VISIBLE);
                            notDataImage.setBackgroundResource(R.mipmap.no_project_icon);
                            notDataText.setText("还没有找到项目哦");
                            homeProjectlsitAdapter.notifyDataSetChanged();
                            projectListView.onRefreshComplete();*/
                    }



                }



            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });



    }
    public void intiPullToRefresh(){
        releaseJobListView.setMode(PullToRefreshBase.Mode.BOTH);
        releaseJobListView.setOnRefreshListener(this);
        ILoadingLayout endLabels  = releaseJobListView
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = releaseJobListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        releaseJobListView.setRefreshing();

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

        finish();

    }
}
