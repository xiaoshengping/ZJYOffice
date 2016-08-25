package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.ResumeListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.ResumeListDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.ResumeListPagerBean;
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

public class ResumeListActivity extends AppCompatActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2<ListView> {

    @ViewInject(R.id.register_tv)
    private TextView comtintJobText;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边

    @ViewInject(R.id.resume_list_listview)
    private PullToRefreshListView resumeListListview;  //列表
    private int pageIndex = 1;
    private List<ResumeListDataBean> resumeListDataBeans;
    private boolean isPullDownRefresh = true; //判断是下拉，还是上拉的标记
    private ResumeListAdapter homeServiceListAdapter;
    @ViewInject(R.id.not_data_layout)
    private LinearLayout notDataLayout;
    @ViewInject(R.id.network_remind_layout)
    private LinearLayout networkRemindLayout;
    @ViewInject(R.id.not_data_image)
    private ImageView notDataImage; //没有网络和没有数据显示
    @ViewInject(R.id.not_data_text)
    private TextView notDataText;
    
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_list);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {
        networkRemindLayout.setOnClickListener(this);
        comtintJobText.setVisibility(View.GONE);
        titleNemeTv.setText("简历列表");
        retrunText.setOnClickListener(this);
        resumeListDataBeans = new ArrayList<>();
        initListView();
        intiPullToRefresh();
        
    }


    private void initListData(int pageIndex) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        SQLhelper sqLhelper=new SQLhelper(ResumeListActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        if (!TextUtils.isEmpty(uid)){
            requestParams.addBodyParameter("Id", uid);
            //步骤1：创建一个SharedPreferences接口对象
            SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
            //步骤2：获取文件中的值
            String sesstionId = read.getString("code","");
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        }
        requestParams.addBodyParameter("recruitId",getIntent().getStringExtra("recruitId").toString());
        requestParams.addBodyParameter("pageIndex", pageIndex +"");
        requestParams.addBodyParameter("pageSize", "10");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getResumeListData(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                //Log.e("找机手", responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)) {

                    AppBean<ResumeListPagerBean> appListDataBean = JSONObject.parseObject(responseInfo.result, new TypeReference<AppBean<ResumeListPagerBean>>() {
                    });
                    if ((appListDataBean.getResult()).equals("success")) {
                        ResumeListPagerBean resumeListPagerData = appListDataBean.getData();
                        List<ResumeListDataBean> resumeListDataBean = resumeListPagerData.getPagerData();
                        if (isPullDownRefresh) {
                            resumeListDataBeans.clear();
                        }
                        resumeListDataBeans.addAll(resumeListDataBean);

                        homeServiceListAdapter.notifyDataSetChanged();
                        resumeListListview.onRefreshComplete();
                        notDataLayout.setVisibility(View.GONE);
                    } else if ((appListDataBean.getResult()).equals("nomore")) {
                        homeServiceListAdapter.notifyDataSetChanged();
                        resumeListListview.onRefreshComplete();
                        MyAppliction.showToast("已到最底了");
                    } else if ((appListDataBean.getResult()).equals("empty")) {
                        homeServiceListAdapter.notifyDataSetChanged();
                        resumeListListview.onRefreshComplete();
                        notDataLayout.setVisibility(View.VISIBLE);
                        notDataImage.setBackgroundResource(R.mipmap.no_driver);
                        notDataText.setText("暂时没有机手投递");
                    }


                } else {
                    homeServiceListAdapter.notifyDataSetChanged();
                    resumeListListview.onRefreshComplete();
                }
                networkRemindLayout.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("找机手", s);
                networkRemindLayout.setVisibility(View.VISIBLE);
                resumeListListview.onRefreshComplete();
                if (resumeListDataBeans.size()==0){
                    notDataLayout.setVisibility(View.VISIBLE);
                    notDataImage.setBackgroundResource(R.mipmap.no_wifi_icon);
                    notDataText.setText("没有网络哦");
                }
            }
        });


    }

    private void initListView() {
        homeServiceListAdapter = new ResumeListAdapter(resumeListDataBeans, ResumeListActivity.this);
        resumeListListview.setAdapter(homeServiceListAdapter);
        resumeListListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ResumeListActivity.this, SeekMachinistParticulasActivity.class);
                intent.putExtra("seekData", resumeListDataBeans.get(position - 1).getId());
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                startActivity(intent);
            }
        });


    }


    public void intiPullToRefresh() {
        resumeListListview.setMode(PullToRefreshBase.Mode.BOTH);
        resumeListListview.setOnRefreshListener(this);
        ILoadingLayout endLabels = resumeListListview
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels = resumeListListview
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        resumeListListview.setRefreshing();

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndex = 1;
        isPullDownRefresh = true;
        initListData(pageIndex);  //列表数据


    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndex++;
        isPullDownRefresh = false;
        initListData(pageIndex);  //列表数据

    }

    @Override
    public void onClick(View v) {
           switch (v.getId()){
               case R.id.retrun_text_view:
                   finish();
                   overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                   break;
               case R.id.network_remind_layout:
                   //跳转到设置界面
                   Intent intent = new Intent(Settings.ACTION_SETTINGS);
                   startActivity(intent);
                   break;
           }
    }
}
