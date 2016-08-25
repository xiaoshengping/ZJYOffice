package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.QuestionnaireListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.QuestionnaireListBean;
import com.example.zhongjiyun03.zhongjiyun.bean.QuestionnaireListDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
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

import cn.jpush.android.api.JPushInterface;

public class QuestionnaireListActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边

    @ViewInject(R.id.questionnaire_lsitview)
    private ListView questionnaireLsitview;


    private List<QuestionnaireListBean> list=new ArrayList<>();
    @ViewInject(R.id.not_data_layout)
    private LinearLayout notDataLayout;//没有数据提示
    @ViewInject(R.id.network_remind_layout)
    private LinearLayout networkRemindLayout; //网络提示
    @ViewInject(R.id.not_data_image)
    private ImageView notDataImage; //没有网络和没有数据显示
    @ViewInject(R.id.not_data_text)
    private TextView notDataText;
    private SVProgressHUD mSVProgressHUD;//loding
    private QuestionnaireListAdapter messageListAdapter;
    private List<QuestionnaireListBean> questionnaireListBeen;


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        intiData();

    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_questionnaire_list);
        ViewUtils.inject(this);
        init();
    }
    private void init() {

        initView();

    }

    private void intiData() {



        Log.e("jjsjfj","执行");
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        mSVProgressHUD.showWithStatus("正在加载中...");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getQuestionnaireListData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("系统消息列表",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<QuestionnaireListDataBean> appListDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<QuestionnaireListDataBean>>(){});
                    if (appListDataBean.getResult().equals("success")){
                        mSVProgressHUD.dismiss();
                        QuestionnaireListDataBean messageDataBeen=  appListDataBean.getData();
                        questionnaireListBeen=   messageDataBeen.getPagerData();
                        if (questionnaireListBeen!=null){
                            notDataLayout.setVisibility(View.GONE);

                            list.addAll(questionnaireListBeen);
                            //Log.e("title",list.get(0).getTitle());
                            InitListView(questionnaireListBeen);
                        }

                    }else if (appListDataBean.getResult().equals("unlogin")){

                        showExitGameAlertUnLonding("本次登录已过期");
                        mSVProgressHUD.dismiss();
                    }else if (appListDataBean.getResult().equals("empty")){
                         if (questionnaireListBeen!=null||questionnaireListBeen.size()!=0){
                             questionnaireListBeen.clear();
                             InitListView(questionnaireListBeen);
                         }

                        notDataLayout.setVisibility(View.VISIBLE);
                        notDataImage.setBackgroundResource(R.mipmap.no_other);
                        notDataText.setText("暂时没有调查问卷");
                        mSVProgressHUD.dismiss();
                    }else {
                        mSVProgressHUD.dismiss();
                    }
                }
                networkRemindLayout.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("系统消息列表",s);
                networkRemindLayout.setVisibility(View.VISIBLE);
                mSVProgressHUD.dismiss();

            }
        });


    }

    private void InitListView(final List<QuestionnaireListBean> messageDataBean) {
        messageListAdapter = new QuestionnaireListAdapter(messageDataBean, this);
        questionnaireLsitview.setAdapter(messageListAdapter);
        messageListAdapter.notifyDataSetChanged();
        questionnaireLsitview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(QuestionnaireListActivity.this,QuestionnaireParticularsActivity.class);
                intent.putExtra("url",messageDataBean.get(position).getUrl());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });


    }
    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("问卷调查列表");
        retrunText.setOnClickListener(this);
        networkRemindLayout.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(QuestionnaireListActivity.this);

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
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;



        }




    }
    //对话框
    private void showExitGameAlertUnLonding(String text) {
        final AlertDialog dlg = new AlertDialog.Builder(QuestionnaireListActivity.this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.shrew_exit_dialog);
        TextView tailte = (TextView) window.findViewById(R.id.tailte_tv);
        tailte.setText(text);
        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        ok.setText("去登录");
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(QuestionnaireListActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                dlg.cancel();
            }
        });

        // 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText("暂不去");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
// KeyEvent.KEYCODE_BACK代表返回操作.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 处理返回操作.
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);

        }
        return true;
    }
}
