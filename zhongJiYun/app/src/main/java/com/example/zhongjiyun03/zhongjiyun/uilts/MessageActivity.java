package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.zhongjiyun03.zhongjiyun.adapter.MessageListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.MessageDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.AppListDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
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

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {



    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边

    @ViewInject(R.id.message_lsitview)
    private ListView messageListView;
    /*@ViewInject(R.id.web_view)
    private WebView webView;*/
    private List<MessageDataBean> list=new ArrayList<>();
    @ViewInject(R.id.not_data_layout)
    private LinearLayout notDataLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ViewUtils.inject(this);
        init();

    }

    private void init() {

        initView();
        intiData();

    }

    private void intiData() {




        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getMessageListData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("系统消息列表",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppListDataBean<MessageDataBean> appListDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppListDataBean<MessageDataBean>>(){});
                    if (appListDataBean.getResult().equals("success")){
                      List<MessageDataBean> messageDataBeen=  appListDataBean.getData();
                        if (messageDataBeen!=null){
                            notDataLayout.setVisibility(View.GONE);
                           list.addAll(messageDataBeen);
                            //Log.e("title",list.get(0).getTitle());
                            InitListView(messageDataBeen);
                        }else {
                            notDataLayout.setVisibility(View.VISIBLE);
                        }

                    }
                }




            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("系统消息列表",s);
                MyAppliction.showToast("网络异常,请稍后重试");
            }
        });


    }

    private void InitListView(final List<MessageDataBean> messageDataBean) {
        MessageListAdapter messageListAdapter = new MessageListAdapter(messageDataBean, this);
        messageListView.setAdapter(messageListAdapter);
        messageListAdapter.notifyDataSetChanged();
        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MessageActivity.this,MessageParticularsActivity.class);
                intent.putExtra("id",messageDataBean.get(position).getId());
                startActivity(intent);
            }
        });


    }
    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("消息");
        retrunText.setOnClickListener(this);
        //webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadUrl(AppUtilsUrl.BaseUrl+"/app/index.html#/tab/system-message?id=50b7d6cb-809f-434c-aa06-79fb40681068");


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
