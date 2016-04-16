package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
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
import com.example.zhongjiyun03.zhongjiyun.adapter.HomeSecondHandListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.home.AppListDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.SecondHandBean;
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

import java.util.List;

public class AttentionExtrunActivity extends AppCompatActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2<ListView> {


    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边


    @ViewInject(R.id.attention_extrun_lsitview)
    private PullToRefreshListView attentionExtrunLsitview;
    private int pageIndex=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_extrun);
        ViewUtils.inject(this);
        inti();


    }

    private void inti() {
        initView();
        intiPullToRefresh();


    }

    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("关注的钻机");
        retrunText.setOnClickListener(this);


    }
    private void intiListData(int pageIndex) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("Id","3aef950d-8b27-46a7-a04b-3329faf5e9f6");
        requestParams.addBodyParameter("pageIndex",pageIndex+"");
        requestParams.addBodyParameter("pageSize","10");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAttentionExtrunListData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("关注钻机",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppListDataBean<SecondHandBean> appListDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppListDataBean<SecondHandBean>>(){});
                    if ((appListDataBean.getResult()).equals("success")){
                     List<SecondHandBean> secondHandBeen=  appListDataBean.getData();
                         if (secondHandBeen!=null){
                             initListView(secondHandBeen);
                             attentionExtrunLsitview.onRefreshComplete();
                         }

                    }else if ((appListDataBean.getResult()).equals("nomore")){
                        attentionExtrunLsitview.onRefreshComplete();
                        MyAppliction.showToast("已到最底了");
                    }

                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("关注钻机",s);
            }
        });



    }

    public void intiPullToRefresh(){
        attentionExtrunLsitview.setMode(PullToRefreshBase.Mode.BOTH);
        attentionExtrunLsitview.setOnRefreshListener(this);
        ILoadingLayout endLabels  = attentionExtrunLsitview
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = attentionExtrunLsitview
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        attentionExtrunLsitview.setRefreshing();

    }
    private void initListView(final List<SecondHandBean> secondHandBeen) {

        HomeSecondHandListAdapter homeSecondHandListAdapter = new HomeSecondHandListAdapter(secondHandBeen, this);
        attentionExtrunLsitview.setAdapter(homeSecondHandListAdapter);
        homeSecondHandListAdapter.notifyDataSetChanged();
        attentionExtrunLsitview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(AttentionExtrunActivity.this,ExturderParticularsActivity.class);
                intent.putExtra("secondHandData",secondHandBeen.get(position-1).getId());
                startActivity(intent);
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
            }
        });


    }
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

        pageIndex=1;
        intiListData(pageIndex);


    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndex++;
        intiListData(pageIndex);
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
