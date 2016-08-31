package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.ReleaseJobListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.AppDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.ReleaseJobListBean;
import com.example.zhongjiyun03.zhongjiyun.bean.ReleaseJobPagerBean;
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

public class ReleaseJobListActivity extends AppCompatActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2<ListView> {


    @ViewInject(R.id.release_job_list_view)
    private PullToRefreshListView releaseJobListView;
    private int PageIndex=1;
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunTextView;  //返回
    @ViewInject(R.id.title_name_tv)
    private TextView tailtNameTv;   //标题
    @ViewInject(R.id.register_tv)
    private TextView registerTv;   //右边按钮
    private List<ReleaseJobListBean> releaseJobListBeens;
    private boolean isPullDownRefresh=true; //判断是下拉，还是上拉的标记
    private ReleaseJobListAdapter releaseJobListAdapter; //adapter
    @ViewInject(R.id.not_data_layout)
    private LinearLayout notDataLayout; //没有数据
    @ViewInject(R.id.network_remind_layout)
    private LinearLayout networkRemindLayout; //没有网络
    @ViewInject(R.id.not_data_image)
    private ImageView notDataImage; //没有网络和没有数据显示
    @ViewInject(R.id.not_data_text)
    private TextView notDataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_job_list);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {
        tailtNameTv.setText("我的招聘");
        retrunTextView.setOnClickListener(this);
        registerTv.setVisibility(View.GONE);
        releaseJobListBeens=new ArrayList<>();
        intiPullToRefresh();
        initListView();


    }

    private void initListView() {

        releaseJobListAdapter = new ReleaseJobListAdapter(releaseJobListBeens, ReleaseJobListActivity.this);
        releaseJobListView.setAdapter(releaseJobListAdapter);
        ListView listView= releaseJobListView.getRefreshableView();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showExitGameAlert("确定要删除？",position-1);

                return true;
            }
        });
        releaseJobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Integer.valueOf(releaseJobListBeens.get(position-1).getCount())>0){
                    Intent resumeIntent=new Intent(ReleaseJobListActivity.this, ResumeListActivity.class)  ;
                    resumeIntent.putExtra("recruitId",releaseJobListBeens.get(position-1).getId());
                    startActivity(resumeIntent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }else {
                    MyAppliction.showToast("暂时没有机手投递简历");
                }

            }
        });



    }

    //对话框
    private void showExitGameAlert(String text, final int position) {
        final AlertDialog dlg = new AlertDialog.Builder(ReleaseJobListActivity.this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.shrew_exit_dialog);
        TextView tailte = (TextView) window.findViewById(R.id.tailte_tv);
        tailte.setText(text);
        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        ok.setText("确定");
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (releaseJobListBeens!=null&&releaseJobListBeens.size()!=0){
                    deleteReleaseJob(position);
                }else {
                    MyAppliction.showToast("删除失败");
                }

                dlg.cancel();
            }
        });

        // 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText("取消");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }

    /**
     * 删除招聘
     * @param position
     */
    private void deleteReleaseJob(int position) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        requestParams.addBodyParameter("recruitId",releaseJobListBeens.get(position).getId());
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getDeleteReleaseJobData(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppDataBean appDataBean=JSONObject.parseObject(responseInfo.result,new TypeReference<AppDataBean>(){});
                    if (appDataBean!=null){
                        if ( appDataBean.getResult().equals("success")){
                            MyAppliction.showToast("删除成功");
                            releaseJobListView.setRefreshing();
                            releaseJobListAdapter.notifyDataSetChanged();
                        }else {
                            MyAppliction.showToast(appDataBean.getMsg());
                        }

                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                      MyAppliction.showToast("网络异常，请稍后重试");
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
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getReleaseJobListData(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("我的招聘列表",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<ReleaseJobPagerBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<ReleaseJobPagerBean>>(){});
                        if (appBean.getResult().equals("success")){
                            ReleaseJobPagerBean releaseJobPagerBean=appBean.getData();
                            if (releaseJobPagerBean!=null){
                                List<ReleaseJobListBean> releaseJobListBeen= releaseJobPagerBean.getPagerData();
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
                            notDataLayout.setVisibility(View.GONE);
                        }else if ((appBean.getResult()).equals("nomore")){
                            MyAppliction.showToast("已到最底了");
                            releaseJobListAdapter.notifyDataSetChanged();
                            releaseJobListView.onRefreshComplete();
                        }else  if ((appBean.getResult()).equals("empty")){
                            //MyAppliction.showToast("没有更多数据");
                            if (isPullDownRefresh) {
                                releaseJobListBeens.clear();
                            }
                            notDataLayout.setVisibility(View.VISIBLE);
                            notDataImage.setBackgroundResource(R.mipmap.no_other);
                            notDataText.setText("您还没有发布招聘");
                            releaseJobListAdapter.notifyDataSetChanged();
                            releaseJobListView.onRefreshComplete();
                        }



                }



            }

            @Override
            public void onFailure(HttpException e, String s) {
                networkRemindLayout.setVisibility(View.VISIBLE);
                releaseJobListAdapter.notifyDataSetChanged();
                releaseJobListView.onRefreshComplete();
                if (releaseJobListBeens.size()==0){
                    notDataLayout.setVisibility(View.VISIBLE);
                    notDataImage.setBackgroundResource(R.mipmap.no_wifi_icon);
                    notDataText.setText("没有网络哦");
                }
            }
        });



    }

    @Override
    public void onClick(View v) {
        finish();

    }


    public void intiPullToRefresh(){
        releaseJobListView.setMode(PullToRefreshBase.Mode.BOTH);
        releaseJobListView.setOnRefreshListener(this);
        ILoadingLayout endLabels  = releaseJobListView
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚上拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 上来达到一定距离时，显示的提示
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
}
