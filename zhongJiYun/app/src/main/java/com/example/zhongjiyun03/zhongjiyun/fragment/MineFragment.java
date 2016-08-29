package com.example.zhongjiyun03.zhongjiyun.fragment;


import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.MessageDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.SystemMessageDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.AppListDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.main.PersonageInformationBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.example.zhongjiyun03.zhongjiyun.http.SystemEvaluateSQLhelper;
import com.example.zhongjiyun03.zhongjiyun.http.SystemMessageSQLhelper;
import com.example.zhongjiyun03.zhongjiyun.uilts.AttentionExtrunActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.AttentionProjectActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.CommentActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.LoginActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.MessageActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.MyCompetitveTenderActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.MyExtruderActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.MyRedPacketActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.PersonageInformationActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.RatingHelpActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.RawardBuyListActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.RawardSellListActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.ReleaseJobListActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.StingActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 * 个人中心Fragment
 */
public class MineFragment extends Fragment implements View.OnClickListener {


    @ViewInject(R.id.loing_layout)
    private RelativeLayout loginLayout;
    @ViewInject(R.id.attention_layout)
    private LinearLayout attentionLayout;
    @ViewInject(R.id.extruder_layout)
    private LinearLayout attentionExtruderLayout;
    @ViewInject(R.id.competitive_layout)
    private LinearLayout competitivteLayout;
    @ViewInject(R.id.message_layout)
    private LinearLayout messageLayout;
    @ViewInject(R.id.redpacket_layout)
    private LinearLayout redPacketLayout;
    @ViewInject(R.id.loing_xshi_layout)
    private RelativeLayout loingXshiLayout; //头像显示布局
    @ViewInject(R.id.loing_xshi_user)
    private ImageView loingXshiUser; //头像imageview
    @ViewInject(R.id.name_text)
    private TextView nameText;   //名字textview
    @ViewInject(R.id.phone_text)
    private TextView phoneText;
    @ViewInject(R.id.sting_layout)
    private LinearLayout stingLayout;

    @ViewInject(R.id.comment_layout)
    private LinearLayout commentLayout;
    @ViewInject(R.id.evaluate_remind_image)
    private ImageView evaluateRemindImage; //评论红点
    @ViewInject(R.id.projectReply_remind_image)
    private ImageView projectReplyeminDImage; //竞标红点
    @ViewInject(R.id.message_remind_image)
    private ImageView messageemindImage; //消息红点
    @ViewInject(R.id.giftBag_remind_image)
    private ImageView giftBagRemindImage; //红包红点
    private String date;
    private PersonageInformationBean personageInformation;
    @ViewInject(R.id.rating_bar)
    private RatingBar ratingBar;

    @ViewInject(R.id.drilling_layout)
    private LinearLayout drillingLayout;  //我的钻机
    @ViewInject(R.id.help_layout)
    private LinearLayout helpLayout;  //帮助中心
    @ViewInject(R.id.release_job_layout)
    private LinearLayout releaseJobLayout;  //我的招聘
    @ViewInject(R.id.xshang_qiumai_layout)
    private LinearLayout xshangQiumaiLayout;  //悬赏求卖
    @ViewInject(R.id.qiumai_layout)
    private LinearLayout qiumaiLayout;  //悬赏求买


    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_mine,container, false);
        ViewUtils.inject(this,view);
        init();
        return view;
    }

    private void init() {

        initView();

    }

    private void initView() {
        attentionLayout.setOnClickListener(this);
        attentionExtruderLayout.setOnClickListener(this);
        competitivteLayout.setOnClickListener(this);
        messageLayout.setOnClickListener(this);
        redPacketLayout.setOnClickListener(this);
        loginLayout.setOnClickListener(this);
        loingXshiLayout.setOnClickListener(this);
        stingLayout.setOnClickListener(this);
        commentLayout.setOnClickListener(this);
        releaseJobLayout.setOnClickListener(this);
        qiumaiLayout.setOnClickListener(this);
        xshangQiumaiLayout.setOnClickListener(this);
        //获取系统时间
        SimpleDateFormat sDateFormat    =   new    SimpleDateFormat("yyyy-MM-dd    HH:mm:ss");
        date=sDateFormat.format(new java.util.Date());
        helpLayout.setOnClickListener(this);
        drillingLayout.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        SQLhelper sqLhelper=new SQLhelper(getActivity());
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        String phone=null;  //手机号码
        String name=null;     //名字
        String StarRate=null; //等级
        String headtHumb=null;  //头像路径
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);
            phone= cursor.getString(1);
            name = cursor.getString(2);
            StarRate=cursor.getString(3);
            headtHumb=cursor.getString(4);
        }
        if (!TextUtils.isEmpty(uid)){
            loginLayout.setVisibility(View.GONE);
            loingXshiLayout.setVisibility(View.VISIBLE);
             if (!TextUtils.isEmpty(headtHumb)){
                 MyAppliction.imageLoader.displayImage(headtHumb,loingXshiUser,MyAppliction.RoundedOptionsOne);
             }
            if (!TextUtils.isEmpty(name)){
                nameText.setText(name);

            }
            if (!TextUtils.isEmpty(phone)){
                StringBuffer stringBuffer=new StringBuffer(phone);
                stringBuffer.replace(3,7,"****");
                phoneText.setText(stringBuffer.toString());
            }
            initPersonageInformationData();//获取用户消息
        }else {
            loginLayout.setVisibility(View.VISIBLE);
            loingXshiLayout.setVisibility(View.GONE);


        }
        initSystenMessage(uid); //获得系统消息提醒


    }

    private void initPersonageInformationData() {
        SQLhelper sqLhelper=new SQLhelper(getActivity());
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        if (!TextUtils.isEmpty(uid)){
            HttpUtils httpUtils=new HttpUtils();
            RequestParams requestParams=new RequestParams();
            requestParams.addBodyParameter("ID",uid);
            //步骤1：创建一个SharedPreferences接口对象
            SharedPreferences read = getActivity().getSharedPreferences("lock", getActivity().MODE_WORLD_READABLE);
            //步骤2：获取文件中的值
            String sesstionId = read.getString("code","");
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
            //Log.e("uid",uids+"");

            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getUserInformationData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (!TextUtils.isEmpty(responseInfo.result)) {
                        AppBean<PersonageInformationBean> appBean = JSONObject.parseObject(responseInfo.result, new TypeReference<AppBean<PersonageInformationBean>>() {
                        });
                        if (appBean.getResult().equals("success")) {

                            personageInformation = appBean.getData();
                            if (personageInformation != null) {
                                if (!TextUtils.isEmpty(personageInformation.getStarRate())) {
                                    String StarRate = personageInformation.getStarRate();
                                    ratingBar.setRating(Integer.valueOf(StarRate));

                                }

                            } else {
                                MyAppliction.showToast(appBean.getMsg());


                            }


                        }


                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {

                    MyAppliction.showToast("网络异常，请稍后重试！");
                }
            });


        }






    }

    private void initSystenMessage(String uid) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        if (!TextUtils.isEmpty(uid)){
            if (MyAppliction.isProjectMessage()==true){
                projectReplyeminDImage.setVisibility(View.VISIBLE);
            }else {
                projectReplyeminDImage.setVisibility(View.GONE);
            }
            intiMessageData();
            requestParams.addBodyParameter("id",uid);
            SystemMessageSQLhelper sqLhelper=new SystemMessageSQLhelper(getActivity());
            SQLiteDatabase db= sqLhelper.getWritableDatabase();
            Cursor cursor=db.query(SystemMessageSQLhelper.tableName, null, null, null, null, null, null);
            String messageRemindId=null;  //id
            String giftBag=null;     //我的红包数
            while (cursor.moveToNext()) {
                messageRemindId=cursor.getString(0);
                giftBag=cursor.getString(1);
            }
            SystemEvaluateSQLhelper sqLhelpers=new SystemEvaluateSQLhelper(getActivity());
            SQLiteDatabase dbs= sqLhelpers.getWritableDatabase();
            Cursor cursors=dbs.query(SystemEvaluateSQLhelper.tableName, null, null, null, null, null, null);
            String messageEvaluateId=null;  //id
            String evaluate=null;  //我的评价数

            while (cursors.moveToNext()) {
                messageEvaluateId=cursors.getString(0);
                evaluate=cursors.getString(1);


            }

            if (!TextUtils.isEmpty(evaluate)){
            requestParams.addBodyParameter("evaluate",evaluate);
                //Log.e("evaluate",evaluate);
            }

            if (!TextUtils.isEmpty(giftBag)){
                requestParams.addBodyParameter("giftBag",giftBag);
                //Log.e("giftBag11",giftBag);
            }


        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getActivity().getSharedPreferences("lock", getActivity().MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);

            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getSystemMessageRemindData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("消息提醒",responseInfo.result);

                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<SystemMessageDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<SystemMessageDataBean>>(){});
                    if (appBean.getResult().equals("success")){
                        SystemMessageDataBean systemMessageDataBean=  appBean.getData();
                        if (systemMessageDataBean!=null){
                            if (systemMessageDataBean.getEvaluate()>0){
                                evaluateRemindImage.setVisibility(View.VISIBLE);
                            }else {
                                evaluateRemindImage.setVisibility(View.GONE);
                            }
                            if (systemMessageDataBean.getGiftBag()>0){
                                giftBagRemindImage.setVisibility(View.VISIBLE);
                            }else {
                                giftBagRemindImage.setVisibility(View.GONE);
                            }





                        }

                    }else {

                    }

                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("消息提醒",s);
            }
        });
        }


    }


    @Override
    public void onClick(View v) {
        SQLhelper sqLhelper=new SQLhelper(getActivity());
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        //获取红包系统提醒数据
        SystemMessageSQLhelper systemMessageSQLhelper=new SystemMessageSQLhelper(getActivity());
        SQLiteDatabase sqLiteDatabase= systemMessageSQLhelper.getWritableDatabase();
        Cursor cursors=sqLiteDatabase.query(SystemMessageSQLhelper.tableName, null, null, null, null, null, null);
        String messageRemindId = null;  //id
        String giftBag=null;     //我的红包数
        while (cursors.moveToNext()) {
            messageRemindId=cursors.getString(0);
            giftBag = cursors.getString(1);

        }
        //获取评论系统提醒数据
        SystemEvaluateSQLhelper systemEvaluateSQLhelper=new SystemEvaluateSQLhelper(getActivity());
        SQLiteDatabase sqLiteDatabases= systemEvaluateSQLhelper.getWritableDatabase();
        Cursor cursorss=sqLiteDatabases.query(SystemEvaluateSQLhelper.tableName, null, null, null, null, null, null);
        String messageEvaluateId = null;  //id
        String evaluate=null;     //我的红包数
        while (cursorss.moveToNext()) {
            messageEvaluateId=cursorss.getString(0);
            evaluate = cursorss.getString(1);

        }

        switch (v.getId()){
            case R.id.loing_layout:// 登录
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;

            case R.id.attention_layout:  //关注项目
                if (!TextUtils.isEmpty(uid)){
                Intent attentionIntent=new Intent(getActivity(), AttentionProjectActivity.class);
                startActivity(attentionIntent);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }else {
                    Intent intent1=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent1);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;
            case R.id.extruder_layout:  // 关注钻机
                if (!TextUtils.isEmpty(uid)){
                    Intent attentionExtruderIntent=new Intent(getActivity(), AttentionExtrunActivity.class);
                    startActivity(attentionExtruderIntent);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }else {
                    Intent intent2=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent2);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;
            case R.id.competitive_layout:  //我的竞标
                if (!TextUtils.isEmpty(uid)){

                    Intent competitveTenderIntent=new Intent(getActivity(), MyCompetitveTenderActivity.class);
                    startActivity(competitveTenderIntent);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                    MyAppliction.setIsProjectMessage(false);
                }else {
                    Intent intent3=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent3);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;
            case R.id.message_layout:   //系统消息
                if (!TextUtils.isEmpty(uid)){
                    Intent messageIntent=new Intent(getActivity(), MessageActivity.class);
                    startActivity(messageIntent);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }else {
                    Intent intent3=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent3);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;
            case R.id.redpacket_layout:   //我的红包
                if (!TextUtils.isEmpty(uid)){
                        if (!TextUtils.isEmpty(giftBag)){
                            update(systemMessageSQLhelper,SystemMessageSQLhelper.tableName,SystemMessageSQLhelper.MESSAGEREMINDID,messageRemindId,SystemMessageSQLhelper.GIFTBAG,date);
                        }else {
                            if (!TextUtils.isEmpty(date)){
                                insertData(systemMessageSQLhelper,SystemMessageSQLhelper.tableName,SystemMessageSQLhelper.MESSAGEREMINDID,SystemMessageSQLhelper.GIFTBAG,date);
                            }

                        }
                    giftBagRemindImage.setVisibility(View.GONE);
                    Intent redPatckIntent=new Intent(getActivity(), MyRedPacketActivity.class);
                    startActivity(redPatckIntent);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }else {
                    Intent intent3=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent3);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;
            case R.id.loing_xshi_layout:  //个人资料
                Intent personagerIntent=new Intent(getActivity(), PersonageInformationActivity.class);
                startActivity(personagerIntent);
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case R.id.sting_layout:   //设置
                Intent stingIntent=new Intent(getActivity(), StingActivity.class);
                startActivity(stingIntent);
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case R.id.comment_layout:   //评论
                if (!TextUtils.isEmpty(uid)){

                    if (!TextUtils.isEmpty(evaluate)){
                        updateEvaluate(systemEvaluateSQLhelper,SystemEvaluateSQLhelper.tableName,SystemEvaluateSQLhelper.MESSAGEEVALUATEID,messageEvaluateId,SystemEvaluateSQLhelper.EVALUATE,date);
                    }else {
                        if (!TextUtils.isEmpty(date)){
                            insertEvaluateData(systemEvaluateSQLhelper,SystemEvaluateSQLhelper.tableName,SystemEvaluateSQLhelper.MESSAGEEVALUATEID,SystemEvaluateSQLhelper.EVALUATE,date);
                        }
                    }
                    Intent commentIntent=new Intent(getActivity(), CommentActivity.class);
                    startActivity(commentIntent);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }else {
                    Intent intent3=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent3);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }

                break;
            case R.id.help_layout:  //帮助
                Intent ratingHelpIntent=new Intent(getActivity(), RatingHelpActivity.class);
                getActivity().startActivity(ratingHelpIntent);
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);

                break;
            case R.id.drilling_layout:  //我的钻机
                 if (!TextUtils.isEmpty(uid)){
                        Intent MyExtruderIntent=new Intent(getActivity(), MyExtruderActivity.class)  ;
                        startActivity(MyExtruderIntent);
                        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                  }else {
                      Intent loginIntent=new Intent(getActivity(),LoginActivity.class);
                      startActivity(loginIntent);
                      getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                                }
                break;
            case R.id.release_job_layout:   //我的招聘
                if (!TextUtils.isEmpty(uid)){
                    Intent releaseJobListIntent=new Intent(getActivity(), ReleaseJobListActivity.class)  ;
                    startActivity(releaseJobListIntent);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }else {
                    Intent loginIntent=new Intent(getActivity(),LoginActivity.class);
                    startActivity(loginIntent);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;
            case R.id.qiumai_layout:    //悬赏求买

                if (!TextUtils.isEmpty(uid)){
                    Intent releaseJobListIntent=new Intent(getActivity(), RawardBuyListActivity.class)  ;
                    startActivity(releaseJobListIntent);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }else {
                    Intent loginIntent=new Intent(getActivity(),LoginActivity.class);
                    startActivity(loginIntent);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }

                break;
            case R.id.xshang_qiumai_layout:// 悬赏求卖
                if (!TextUtils.isEmpty(uid)){
                    Intent releaseJobListIntent=new Intent(getActivity(), RawardSellListActivity.class)  ;
                    startActivity(releaseJobListIntent);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }else {
                    Intent loginIntent=new Intent(getActivity(),LoginActivity.class);
                    startActivity(loginIntent);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;


        }
    }
    /**
     * 插入数据
     */
    public void insertData(SystemMessageSQLhelper sqLhelper,String tableName,String idName,String key,String value){
        SQLiteDatabase db=sqLhelper.getWritableDatabase();
        // db.execSQL("insert into user(uid,userName,userIcon,state) values('战士',3,5,7)");
        ContentValues values=new ContentValues();
        values.put(key,value);
        db.insert(tableName, idName, values);
        db.close();
    }
    /**
     * 更新数据
     */
    public void update(SystemMessageSQLhelper sqLhelper,String tableName,String idName,String id,String key,String value){
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(key, value);
        db.update(tableName, contentValues,
                idName+"=?", new String[]{id});
        //Log.e("更新了数据","更新了数据");
    }

    /**
     * 评论插入数据
     */
    public void insertEvaluateData(SystemEvaluateSQLhelper sqLhelper,String tableName,String idName,String key,String value){
        SQLiteDatabase db=sqLhelper.getWritableDatabase();
        // db.execSQL("insert into user(uid,userName,userIcon,state) values('战士',3,5,7)");
        ContentValues values=new ContentValues();
        values.put(key,value);
        db.insert(tableName, idName, values);
        db.close();
    }
    /**
     * 评论更新数据
     */
    public void updateEvaluate(SystemEvaluateSQLhelper sqLhelper,String tableName,String idName,String id,String key,String value){
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(key, value);
        db.update(tableName, contentValues,
                idName+"=?", new String[]{id});
        //Log.e("更新了数据","更新了数据");
    }

   /* *//**
     * 消息插入数据
     *//*
    public void insertMessageData(MianMessageSQLhelper sqLhelper,String tableName,String idName,String key,String value){
        SQLiteDatabase db=sqLhelper.getWritableDatabase();
        // db.execSQL("insert into user(uid,userName,userIcon,state) values('战士',3,5,7)");
        ContentValues values=new ContentValues();
        values.put(key,value);
        db.insert(tableName, idName, values);
        db.close();
    }
    *//**
     * 消息更新数据
     *//*
    public void updateMessage(MianMessageSQLhelper sqLhelper,String tableName,String idName,String id,String key,String value){
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(key, value);
        db.update(tableName, contentValues,
                idName+"=?", new String[]{id});
        //Log.e("更新了数据","更新了数据");
    }*/


    private void intiMessageData() {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getActivity().getSharedPreferences("lock", getActivity().MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getMessageListData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppListDataBean<MessageDataBean> appListDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppListDataBean<MessageDataBean>>(){});
                    if (appListDataBean.getResult().equals("success")){
                        List<MessageDataBean> messageDataBeen=  appListDataBean.getData();
                        if (messageDataBeen!=null){
                           if (MyAppliction.getMessageSize()!=0){
                               if (messageDataBeen.size()>MyAppliction.getMessageSize()){
                                  MyAppliction.setMessageSize(messageDataBeen.size());
                                   messageemindImage.setVisibility(View.VISIBLE);

                               }else {
                                   messageemindImage.setVisibility(View.GONE);
                               }
                           }else {
                               MyAppliction.setMessageSize(messageDataBeen.size());
                           }

                        }else {

                        }

                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {


            }
        });


    }


}
