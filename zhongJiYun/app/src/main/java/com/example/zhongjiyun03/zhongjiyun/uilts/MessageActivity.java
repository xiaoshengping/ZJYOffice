package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.MessageListAdapter;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ViewUtils.inject(this);
        init();

    }

    private void init() {
        InitListView();
        initView();
        intiData();
    }

    private void intiData() {
        HttpUtils httpUtils=new HttpUtils();
        SQLhelper sqLhelper=new SQLhelper(MessageActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String sesstionid=null;  //用户id

        while (cursor.moveToNext()) {
            sesstionid=cursor.getString(6);

        }
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("Id","77c504bd-b212-4822-bf5f-9909e593ece3");
       /* //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String value = read.getString("code","");*/
        Log.e("value",sesstionid);

        requestParams.setHeader("Cookie","ASP.NET_SessionId="+sesstionid);

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getMessageData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("系统消息列表",responseInfo.result);




            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("系统消息列表",s);
            }
        });
        /*HttpUtils httpUtils=new HttpUtils();
        *//*SQLhelper sqLhelper=new SQLhelper(MessageActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String sesstionid=null;  //用户id

        while (cursor.moveToNext()) {
            sesstionid=cursor.getString(6);

        }*//*
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("Id","77c504bd-b212-4822-bf5f-9909e593ece3");
       *//* //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String value = read.getString("code","");
        Log.e("value",value);
        requestParams.setHeader("Cookie","ASP.NET_SessionId="+value);*//*

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getMessageListData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("系统消息列表",responseInfo.result);




            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("系统消息列表",s);
            }
        });*/


    }

    private void InitListView() {
        List<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayList.add("审核通知");
        }
        MessageListAdapter messageListAdapter = new MessageListAdapter(arrayList, this);
        messageListView.setAdapter(messageListAdapter);
        messageListAdapter.notifyDataSetChanged();


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
