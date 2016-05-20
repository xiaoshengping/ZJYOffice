package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import cn.jpush.android.api.JPushInterface;

public class PaymentStateActivity extends AppCompatActivity implements View.OnClickListener {
    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边
    @ViewInject(R.id.cash_comtent_text_view)
    private TextView cashComtentTextView;

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
        setContentView(R.layout.activity_payment_state);
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
        titleNemeTv.setText("支付说明");
        retrunText.setOnClickListener(this);
        String zfuTage=getIntent().getStringExtra("tage");
        if (zfuTage.equals("project")){
            cashComtentTextView.setText("敬的用户，投标需要缴纳1000元的保证金，如投标不成功则全额退还给机主。如投标成功并且项目启动，全额退还保证金，并赠送500元平台代金券（5000云币）。如获得业主3星以上评价，则赠送三星100元、四星200元或五星500元的平台代金券（等值云币）。在机主投标并交纳保证金后，通过业主选择，项目启动后，由机主上传合同或协议等证据，后台审核成功后业主可以对机主进行打分评价。四星及以上机主无需缴纳保证金");
        }else if (zfuTage.equals("rentExturd")){
            cashComtentTextView.setText("尊敬的用户，发布二手钻机需要缴纳每台1000元的保证金，二手机信息会在平台上发布一个月，如二手机出租不成功，保证金全额退还。如二手机出租成功，交易双方各自需要上传租赁合同或协议等证据，平台审核成功后会全额退还保证金并给租方赠送100元平台代金劵（1000云币），平台同时会根据出租方的机主星级赠送相应的奖励。");
        }else if (zfuTage.equals("sellExturd")){
            cashComtentTextView.setText("敬的用户，发布二手机需要缴纳每台1000元的保证金，二手机信息会在平台上发布一个月，如二手机销售不成功，保证金全额退还。如二手机销售成功，买卖双方各自需要上传销售合同或协议等证据，平台审核成功后会全额退还保证金并给买方赠送500元平台代金券（5000云币），平台同时会根据卖方的机主星级赠送相对应的奖励。");
        }




    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
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
