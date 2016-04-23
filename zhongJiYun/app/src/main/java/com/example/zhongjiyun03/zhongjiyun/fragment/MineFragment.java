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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.SystemMessageDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.example.zhongjiyun03.zhongjiyun.http.SystemMessageSQLhelper;
import com.example.zhongjiyun03.zhongjiyun.uilts.AttentionExtrunActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.AttentionProjectActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.CommentActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.LoginActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.MessageActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.MyCompetitveTenderActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.MyRedPacketActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.PersonageInformationActivity;
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
    @ViewInject(R.id.rating_one)
    private TextView ratingOne;
    @ViewInject(R.id.rating_two)
    private TextView ratingTwo;
    @ViewInject(R.id.rating_three)
    private TextView ratingThree;
    @ViewInject(R.id.rating_four)
    private TextView ratingFour;
    @ViewInject(R.id.rating_five)
    private TextView ratingFive;
    @ViewInject(R.id.evaluate_remind_image)
    private ImageView evaluateRemindImage; //评论红点
    @ViewInject(R.id.projectReply_remind_image)
    private ImageView projectReplyeminDImage; //竞标红点
    @ViewInject(R.id.message_remind_image)
    private ImageView messageemindImage; //消息红点
    @ViewInject(R.id.giftBag_remind_image)
    private ImageView giftBagRemindImage; //红包红点
    private String date;


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
        //获取系统时间
        SimpleDateFormat sDateFormat    =   new    SimpleDateFormat("yyyy-MM-dd    HH:mm:ss");
        date=sDateFormat.format(new java.util.Date());

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
                phoneText.setText(phone);
            }
            if (!TextUtils.isEmpty(StarRate)){
                if (StarRate.equals("1")){
                    ratingOne.setBackgroundResource(R.mipmap.ratingbar_check);
                    ratingTwo.setBackgroundResource(R.mipmap.ratingbar_check_no);
                    ratingThree.setBackgroundResource(R.mipmap.ratingbar_check_no);
                    ratingFour.setBackgroundResource(R.mipmap.ratingbar_check_no);
                    ratingFive.setBackgroundResource(R.mipmap.ratingbar_check_no);
                }else if (StarRate.equals("2")){

                    ratingOne.setBackgroundResource(R.mipmap.ratingbar_check);
                    ratingTwo.setBackgroundResource(R.mipmap.ratingbar_check);
                    ratingThree.setBackgroundResource(R.mipmap.ratingbar_check_no);
                    ratingFour.setBackgroundResource(R.mipmap.ratingbar_check_no);
                    ratingFive.setBackgroundResource(R.mipmap.ratingbar_check_no);

                }else if (StarRate.equals("3")){

                    ratingOne.setBackgroundResource(R.mipmap.ratingbar_check);
                    ratingTwo.setBackgroundResource(R.mipmap.ratingbar_check);
                    ratingThree.setBackgroundResource(R.mipmap.ratingbar_check);
                    ratingFour.setBackgroundResource(R.mipmap.ratingbar_check_no);
                    ratingFive.setBackgroundResource(R.mipmap.ratingbar_check_no);

                }else if (StarRate.equals("4")){

                    ratingOne.setBackgroundResource(R.mipmap.ratingbar_check);
                    ratingTwo.setBackgroundResource(R.mipmap.ratingbar_check);
                    ratingThree.setBackgroundResource(R.mipmap.ratingbar_check);
                    ratingFour.setBackgroundResource(R.mipmap.ratingbar_check);
                    ratingFive.setBackgroundResource(R.mipmap.ratingbar_check_no);

                }else if (StarRate.equals("5")){

                    ratingOne.setBackgroundResource(R.mipmap.ratingbar_check);
                    ratingTwo.setBackgroundResource(R.mipmap.ratingbar_check);
                    ratingThree.setBackgroundResource(R.mipmap.ratingbar_check);
                    ratingFour.setBackgroundResource(R.mipmap.ratingbar_check);
                    ratingFive.setBackgroundResource(R.mipmap.ratingbar_check);

                }
            }

        }else {
            loginLayout.setVisibility(View.VISIBLE);
            loingXshiLayout.setVisibility(View.GONE);


        }
        initSystenMessage(uid); //获得系统消息提醒



    }

    private void initSystenMessage(String uid) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        if (!TextUtils.isEmpty(uid)){
            requestParams.addBodyParameter("id",uid);
            SystemMessageSQLhelper sqLhelper=new SystemMessageSQLhelper(getActivity());
            SQLiteDatabase db= sqLhelper.getWritableDatabase();
            Cursor cursor=db.query(SystemMessageSQLhelper.tableName, null, null, null, null, null, null);
            String messageRemindId=null;  //id
            String evaluate=null;  //我的评价数
            String message=null;  //消息数
            String giftBag=null;     //我的红包数
            String projectReply=null; //我的竞标数

            while (cursor.moveToNext()) {
                messageRemindId=cursor.getString(0);
                evaluate=cursor.getString(1);
                message= cursor.getString(2);
                giftBag = cursor.getString(3);
                projectReply=cursor.getString(4);

            }

            if (!TextUtils.isEmpty(evaluate)){
            requestParams.addBodyParameter("evaluate",evaluate);
            }
            if (!TextUtils.isEmpty(message)){
                requestParams.addBodyParameter("message",message);
            }
            if (!TextUtils.isEmpty(giftBag)){
                requestParams.addBodyParameter("giftBag",giftBag);
            }
            if (!TextUtils.isEmpty(projectReply)){
                requestParams.addBodyParameter("projectReply",projectReply);
            }
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = getActivity().getSharedPreferences("lock", getActivity().MODE_WORLD_READABLE);
        //步骤2：获取文件中的值
        String sesstionId = read.getString("code","");
        requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
            final String finalMessageRemindId = messageRemindId;
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getSystemMessageRemindData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("消息提醒",responseInfo.result);

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
                            if (systemMessageDataBean.getMessage()>0){
                                messageemindImage.setVisibility(View.VISIBLE);
                            }else {
                                messageemindImage.setVisibility(View.GONE);
                            }
                            if (systemMessageDataBean.getProjectReply()>0){
                                projectReplyeminDImage.setVisibility(View.VISIBLE);
                            }else {
                                projectReplyeminDImage.setVisibility(View.GONE);
                            }

                            if (TextUtils.isEmpty(finalMessageRemindId)){
                                SystemMessageSQLhelper sqLhelper=new SystemMessageSQLhelper(getActivity());
                                insertData(sqLhelper,SystemMessageSQLhelper.MESSAGEREMINDID,"hshhdhhdhdhs");
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
        //获取系统提醒数据
        SystemMessageSQLhelper systemMessageSQLhelper=new SystemMessageSQLhelper(getActivity());
        SQLiteDatabase sqLiteDatabase= systemMessageSQLhelper.getWritableDatabase();
        Cursor cursors=sqLiteDatabase.query(SystemMessageSQLhelper.tableName, null, null, null, null, null, null);

        String messageRemindId = null;  //id
        String evaluate=null;  //我的评价数
        String message=null;  //消息数
        String giftBag=null;     //我的红包数
        String projectReply=null; //我的竞标数

        while (cursors.moveToNext()) {
            messageRemindId=cursors.getString(0);
            evaluate=cursors.getString(1);
            message= cursors.getString(2);
            giftBag = cursors.getString(3);
            projectReply=cursors.getString(4);

        }

        switch (v.getId()){
            case R.id.loing_layout:
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;

            case R.id.attention_layout:
                if (!TextUtils.isEmpty(uid)){
                Intent attentionIntent=new Intent(getActivity(), AttentionProjectActivity.class);
                startActivity(attentionIntent);
                getActivity().overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                }else {
                    Intent intent1=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent1);
                    getActivity().overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                }
                break;
            case R.id.extruder_layout:
                if (!TextUtils.isEmpty(uid)){
                Intent attentionExtruderIntent=new Intent(getActivity(), AttentionExtrunActivity.class);
                startActivity(attentionExtruderIntent);
                getActivity().overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                }else {
                    Intent intent2=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent2);
                    getActivity().overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                }
                break;
            case R.id.competitive_layout:
                if (!TextUtils.isEmpty(uid)){
                Intent competitveTenderIntent=new Intent(getActivity(), MyCompetitveTenderActivity.class);
                startActivity(competitveTenderIntent);
                getActivity().overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                }else {
                    Intent intent3=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent3);
                    getActivity().overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                }
                break;
            case R.id.message_layout:
                Intent messageIntent=new Intent(getActivity(), MessageActivity.class);
                startActivity(messageIntent);
                getActivity().overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;
            case R.id.redpacket_layout:
                if (!TextUtils.isEmpty(uid)){
                    if (!TextUtils.isEmpty(giftBag)){
                        Log.e("giftBag",giftBag);
                        Log.e("messageRemindId",messageRemindId);
                        update(messageRemindId,SystemMessageSQLhelper.GIFTBAG,date);
                    }else {

                        if (!TextUtils.isEmpty(date)){
                            Log.e("添加数据了",date);
                            insertData(systemMessageSQLhelper,SystemMessageSQLhelper.GIFTBAG,date);
                        }

                    }
                    giftBagRemindImage.setVisibility(View.GONE);
                    Intent redPatckIntent=new Intent(getActivity(), MyRedPacketActivity.class);
                    startActivity(redPatckIntent);
                    getActivity().overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                }else {
                    Intent intent3=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent3);
                    getActivity().overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                }
                break;
            case R.id.loing_xshi_layout:
                Intent personagerIntent=new Intent(getActivity(), PersonageInformationActivity.class);
                startActivity(personagerIntent);
                getActivity().overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;
            case R.id.sting_layout:
                Intent stingIntent=new Intent(getActivity(), StingActivity.class);
                startActivity(stingIntent);
                getActivity().overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;
            case R.id.comment_layout:
                Intent commentIntent=new Intent(getActivity(), CommentActivity.class);
                startActivity(commentIntent);
                getActivity().overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;

        }
    }
    /**
     * 插入数据
     */
    public void insertData(SystemMessageSQLhelper sqLhelper,String key,String value){
        SQLiteDatabase db=sqLhelper.getWritableDatabase();
        // db.execSQL("insert into user(uid,userName,userIcon,state) values('战士',3,5,7)");
        ContentValues values=new ContentValues();
        values.put(key,value);
        db.insert(SystemMessageSQLhelper.tableName, SystemMessageSQLhelper.MESSAGEREMINDID, values);
        db.close();
    }
    /**
     * 更新数据
     */
    public void update(String id,String key,String value){
        SystemMessageSQLhelper sqLhelper= new SystemMessageSQLhelper(getActivity());
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(key, value);
        db.update(SystemMessageSQLhelper.tableName, contentValues,
                "messageRemindId=?", new String[]{id});
        Log.e("更新了数据","更新了数据");
    }

}
