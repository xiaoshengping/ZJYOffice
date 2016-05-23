package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import cn.jpush.android.api.JPushInterface;

public class CommitCashDepositActivity extends AppCompatActivity implements View.OnClickListener {


    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边
    @ViewInject(R.id.zfu_state_layout)
    private LinearLayout zfuStateLayout;
    @ViewInject(R.id.zfu_button)
    private Button zFuButton;
    @ViewInject(R.id.contract_check)
    private RadioButton cntractCheck;
    private String zfuTage;
    @ViewInject(R.id.pety_text)
    private TextView petyText;
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
        //改变状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.red_light);//通知栏所需颜色
        }
        setContentView(R.layout.activity_commit_cash_deposit);
        ViewUtils.inject(this);
        inti();


    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    private void inti() {
        initView();

    }

    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("缴纳保证金");
        retrunText.setOnClickListener(this);
        zfuStateLayout.setOnClickListener(this);
        zFuButton.setOnClickListener(this);
       zfuTage= getIntent().getStringExtra("zfuTage");
        if (zfuTage.equals("rentExturd")){
            petyText.setText("二手机保证金");
        }else if (zfuTage.equals("sellExturd")){
            petyText.setText("二手机保证金");
        }else if (zfuTage.equals("project")){
            petyText.setText("项目保证金");

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.zfu_state_layout:
                Intent intent=new Intent(CommitCashDepositActivity.this,PaymentStateActivity.class);
                intent.putExtra("tage",zfuTage);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case R.id.zfu_button:
                Intent zfuIntent=new Intent(CommitCashDepositActivity.this,PaymentStateActivity.class);
                zfuIntent.putExtra("tage",zfuTage);
                startActivity(zfuIntent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
        }
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
