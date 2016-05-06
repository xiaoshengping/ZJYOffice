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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.AttentionProjectAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.AttentionProjectDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.seekProject.AttentionProjectBean;
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

public class AttentionProjectActivity extends AppCompatActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2<ListView> {

    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边


    @ViewInject(R.id.attention_project_listview)
    private PullToRefreshListView attentionProjectListview;
    private int pageIndex=1;//分页
    private boolean isPullDownRefresh=true; //判断是下拉，还是上拉的标记
    private List<AttentionProjectBean> attentionProjectBeens; //列表数据
    private AttentionProjectAdapter homeProjectlsitAdapter;
    @ViewInject(R.id.not_data_layout)
    private LinearLayout notDataLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_project);
        ViewUtils.inject(this);
        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        attentionProjectListview.setRefreshing();
    }

    private void init() {
        initView();
        initListView();
        intiPullToRefresh();


    }
    public void intiPullToRefresh(){
        attentionProjectListview.setMode(PullToRefreshBase.Mode.BOTH);
        attentionProjectListview.setOnRefreshListener(this);
        ILoadingLayout endLabels  = attentionProjectListview
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = attentionProjectListview
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        attentionProjectListview.setRefreshing();

    }

    private void initListData(int pageIndex) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        SQLhelper sqLhelper=new SQLhelper(AttentionProjectActivity.this);
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
            requestParams.addBodyParameter("collectType","1");
            requestParams.addBodyParameter("PageIndex",pageIndex+"");
            requestParams.addBodyParameter("PageSize","10");

            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAttentionProjectListData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("关注项目",responseInfo.result);
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        AppBean<AttentionProjectDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<AttentionProjectDataBean>>(){});
                        if ((appBean.getResult()).equals("success")){
                            AttentionProjectDataBean attentionProjectDataBean=appBean.getData();
                            if (attentionProjectDataBean!=null){
                                List<AttentionProjectBean> attentionProjectBeen=attentionProjectDataBean.getPagerData();
                                 if (attentionProjectBeen!=null){
                                     if (isPullDownRefresh){
                                         attentionProjectBeens.clear();
                                     }
                                     attentionProjectBeens.addAll(attentionProjectBeen);
                                 }
                                attentionProjectListview.onRefreshComplete();
                                homeProjectlsitAdapter.notifyDataSetChanged();
                            }else {
                                attentionProjectListview.onRefreshComplete();
                                homeProjectlsitAdapter.notifyDataSetChanged();
                            }
                            notDataLayout.setVisibility(View.GONE);
                        }else if ((appBean.getResult()).equals("empty")){
                            if (isPullDownRefresh){
                                attentionProjectBeens.clear();
                            }
                            notDataLayout.setVisibility(View.VISIBLE);
                            MyAppliction.showToast("没有更多数据");
                            attentionProjectListview.onRefreshComplete();
                            homeProjectlsitAdapter.notifyDataSetChanged();
                        }else if ((appBean.getResult()).equals("nomore")){
                            notDataLayout.setVisibility(View.GONE);
                            attentionProjectListview.onRefreshComplete();
                            MyAppliction.showToast("已到最低了");
                            homeProjectlsitAdapter.notifyDataSetChanged();
                        }


                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    attentionProjectListview.onRefreshComplete();
                    Log.e("找项目",s);
                }
            });

        }else {
            MyAppliction.showToast("数据加载失败");
            finish();
        }




    }

    private void initListView() {

        homeProjectlsitAdapter=new AttentionProjectAdapter(attentionProjectBeens,this);
        attentionProjectListview.setAdapter(homeProjectlsitAdapter);
        attentionProjectListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(AttentionProjectActivity.this, SeekProjectParticularsActivity.class);
                intent.putExtra("seekProjectId",attentionProjectBeens.get(position-1).getId());
                startActivity(intent);
            }
        });
         ListView listView= attentionProjectListview.getRefreshableView();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MyAppliction.showToast("长按了"+position);

                return false;
            }
        });



    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndex=1;
        isPullDownRefresh=true;
        initListData(pageIndex);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndex++;
        isPullDownRefresh=false;
        initListData(pageIndex);
    }
    private void initView() {
        attentionProjectBeens=new ArrayList<>();
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("关注的项目");
        retrunText.setOnClickListener(this);


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
