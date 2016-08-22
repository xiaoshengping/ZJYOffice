package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.jpush.android.api.JPushInterface;

public class HomeRewardActivity extends AppCompatActivity  implements View.OnClickListener {


    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边

    @ViewInject(R.id.boundy_buy_iamge)
    private ImageView boundyBuyIamge;  //求买按钮
    @ViewInject(R.id.boundy_sell_iamge)
    private ImageView boundySellIamge; //求卖按钮




    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_reward);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("中基云悬赏");
        retrunText.setOnClickListener(this);
        boundyBuyIamge.setOnClickListener(this);
        boundySellIamge.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.boundy_buy_iamge:
                Intent buyIntent=new Intent(HomeRewardActivity.this,RewardParticularsActivity.class);
                buyIntent.putExtra("tage","buy");
                startActivity(buyIntent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case R.id.boundy_sell_iamge:
                Intent sellIntent=new Intent(HomeRewardActivity.this,RewardParticularsActivity.class);
                sellIntent.putExtra("tage","sell");
                startActivity(sellIntent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;

        }




    }


}
