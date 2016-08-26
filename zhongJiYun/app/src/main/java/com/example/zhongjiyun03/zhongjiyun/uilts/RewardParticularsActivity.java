package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.http.SQLHelperUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RewardParticularsActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.boundy_header_image)
    private ImageView boundyHeaderImage;//头部
    @ViewInject(R.id.boundy_butoon_iamge)
    private ImageView boundyButoonIamge;  //按钮
    @ViewInject(R.id.boundy_con_image)
    private ImageView boundyConImage;  //尾部

    @ViewInject(R.id.retrun_boundy_Text)
    private TextView retrunBoundyText;  //返回按钮
    @ViewInject(R.id.title_name_tv)
    private TextView titleNameTv; //标题


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_particulars);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        initView();

    }

    private void initView() {
        retrunBoundyText.setOnClickListener(this);
        boundyButoonIamge.setOnClickListener(this);
        if (getIntent().getStringExtra("tage").equals("buy")){
              boundyHeaderImage.setBackgroundResource(R.mipmap.bounty_header);
              boundyButoonIamge.setBackgroundResource(R.mipmap.bounty_btn);
              boundyConImage.setBackgroundResource(R.mipmap.buy_details_rule);
              titleNameTv.setText("悬赏求买详情");

        }else {
            boundyHeaderImage.setBackgroundResource(R.mipmap.boundy_buy_header);
            boundyButoonIamge.setBackgroundResource(R.mipmap.boundy_buy_btn);
            boundyConImage.setBackgroundResource(R.mipmap.boundy_buy_con);
            titleNameTv.setText("悬赏求卖详情");
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.boundy_butoon_iamge:
                if (!TextUtils.isEmpty(SQLHelperUtils.queryId(RewardParticularsActivity.this))){
                    if (getIntent().getStringExtra("tage").equals("buy")){
                        Intent buyIntent=new Intent(RewardParticularsActivity.this,RewardBuyAddActivity.class);
                        startActivity(buyIntent);
                    }else {
                        Intent buyIntent=new Intent(RewardParticularsActivity.this,RewardSellAddActivity.class);
                        startActivity(buyIntent);
                    }
                }else {
                    Intent loginIntent=new Intent(RewardParticularsActivity.this,LoginActivity.class);
                    startActivity(loginIntent);
                }

                break;
            case R.id.retrun_boundy_Text:
                finish();
                break;


        }

    }
}
