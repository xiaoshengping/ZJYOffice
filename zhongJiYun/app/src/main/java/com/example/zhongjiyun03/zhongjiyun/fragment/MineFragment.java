package com.example.zhongjiyun03.zhongjiyun.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
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
    @ViewInject(R.id.rating_bar)
    private RatingBar ratingBar;
    @ViewInject(R.id.comment_layout)
    private LinearLayout commentLayout;





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
                ratingBar.setRating(Integer.parseInt(StarRate));
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
        }
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getSystemMessageRemindData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });



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


}
