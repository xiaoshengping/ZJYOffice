package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
import android.location.LocationManager;
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
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.HomeServicetListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.ServiceDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.ServiceProviderBean;
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


/*
*   服务商
* */

public class ServiceProviderActivity extends AppCompatActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2<ListView> {


    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边


    @ViewInject(R.id.service_provider_listview)
    private PullToRefreshListView serviceProviderListview; //列表
    private SVProgressHUD mSVProgressHUD;//loding

    private LocationManager locationManager;
    private String locationProvider;
    private int pageIndext=1;
    private  List<ServiceProviderBean> serviceProviderBean;
    private HomeServicetListAdapter homeServiceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider);
        ViewUtils.inject(this);
        inti();

    }
      //初始化
    private void inti() {

        initView();
        intiPullToRefresh();
        //initLocation();
    }
    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("推荐的服务商");
        retrunText.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);
        serviceProviderBean=new ArrayList<>();
        intiListView();


    }




    private void initData(int pageIndex) {

        HttpUtils httpUtils=new HttpUtils();
        final RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("PageIndex",pageIndex+"");
        requestParams.addBodyParameter("PageSize","10");
        requestParams.addBodyParameter("Longitude","113.2759952545166");
        requestParams.addBodyParameter("Latitude","23.117055306224895");
        //mSVProgressHUD.showWithStatus("加载中...");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getServiceListData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("服务商数据",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<ServiceDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<ServiceDataBean>>(){});
                     if ((appBean.getResult()).equals("success")){

                         ServiceDataBean serviceProvider=appBean.getData();
                         List<ServiceProviderBean>  serviceProviderBeans=  serviceProvider.getPagerData();
                         serviceProviderBean.addAll(serviceProviderBeans);
                         serviceProviderListview.onRefreshComplete();
                        // mSVProgressHUD.dismiss();
                     }


                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                serviceProviderListview.onRefreshComplete();
                mSVProgressHUD.dismiss();
                Log.e("服务商数据",s);
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
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
            }
        });

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
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        serviceProviderBean.clear();
         pageIndext=1;
        initData(pageIndext);



    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
           pageIndext++;
        Log.e("pageIndext",pageIndext+"");
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
