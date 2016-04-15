package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class PaymentStateActivity extends AppCompatActivity implements View.OnClickListener {
    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_state);
        ViewUtils.inject(this);
        inti();
    }

    private void inti() {
        initView();

    }

    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("支付说明");
        retrunText.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
