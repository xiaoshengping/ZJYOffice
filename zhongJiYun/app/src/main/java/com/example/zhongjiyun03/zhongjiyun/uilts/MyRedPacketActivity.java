package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.MyRedPatckListAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.main.RePackedListBean;
import com.example.zhongjiyun03.zhongjiyun.bean.main.RedPacketDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class MyRedPacketActivity extends AppCompatActivity implements View.OnClickListener {


    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边



      @ViewInject(R.id.red_patck_listview)
      private ListView redPatckListview;  //列表
    private SVProgressHUD mSVProgressHUD;//loding
    @ViewInject(R.id.get_packed_button)
    private Button getPackedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_red_packet);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        mSVProgressHUD = new SVProgressHUD(this);
        initData(); //获取列表数据

        initView();


    }

    private void initData() {
        SQLhelper sqLhelper=new SQLhelper(MyRedPacketActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id

        while (cursor.moveToNext()) {
            uid=cursor.getString(0);
        }
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("Id",uid);
        requestParams.addBodyParameter("PageIndex","1");
        requestParams.addBodyParameter("PageSize","10");
        mSVProgressHUD.showWithStatus("加载中...");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRedPacketListData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("我的红包",responseInfo.result);
                AppBean<RedPacketDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<RedPacketDataBean>>(){});
                if ((appBean.getResult()).equals("success")){
                    RedPacketDataBean redPacketDataBean=appBean.getData();
                    List<RePackedListBean> listData= redPacketDataBean.getPagerData();
                    if (redPacketDataBean.getTotal()!=0){
                        getPackedButton.setVisibility(View.VISIBLE);
                    }
                    if (listData!=null){
                        initListView(listData);


                    }

                    mSVProgressHUD.dismiss();


                }else if ((appBean.getResult()).equals("empty")){
                    mSVProgressHUD.dismiss();
                    MyAppliction.showToast("您还没有收到红包哦");
                    getPackedButton.setVisibility(View.GONE);
                }



            }

            @Override
            public void onFailure(HttpException e, String s) {
               Log.e("我的红包",s);
                mSVProgressHUD.dismiss();
                MyAppliction.showToast("网络异常,请稍后重试!");
            }
        });




    }


    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("我的红包");
        retrunText.setOnClickListener(this);
        getPackedButton.setOnClickListener(this);


    }

    private void initListView(List<RePackedListBean> listData) {

        MyRedPatckListAdapter myredAdapter=new MyRedPatckListAdapter(listData,this);
        redPatckListview.setAdapter(myredAdapter);
        myredAdapter.notifyDataSetChanged();




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;
            case R.id.get_packed_button:

                getPackedData();
                break;




        }
    }

    private void getPackedData() {
        SQLhelper sqLhelper=new SQLhelper(MyRedPacketActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id

        while (cursor.moveToNext()) {
            uid=cursor.getString(0);
        }
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        if (!TextUtils.isEmpty(uid)){
            requestParams.addBodyParameter("Id",uid);

            mSVProgressHUD.showWithStatus("领取中...");
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRedPacketData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("领取红包",responseInfo.result);
                /*AppBean<RedPacketDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<RedPacketDataBean>>(){});
                if ((appBean.getResult()).equals("success")){
                    RedPacketDataBean redPacketDataBean=appBean.getData();
                    List<RePackedListBean> listData= redPacketDataBean.getGiftBags();
                    initListView(listData);
                    mSVProgressHUD.dismiss();


                }else {

                    MyAppliction.showToast(appBean.getMsg());
                }*/
                    mSVProgressHUD.dismiss();


                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.e("我的红包",s);
                    mSVProgressHUD.dismiss();
                    MyAppliction.showToast("网络异常,请稍后重试!");
                }
            });
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
