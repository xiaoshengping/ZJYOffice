package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RegisterFishActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.register_tv)
    private TextView registerTv;  //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView tailtText;
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边

    @ViewInject(R.id.call_phone_layout)
    private LinearLayout callPhoneLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_fish);
        ViewUtils.inject(this);
        
        init();


    }

    private void init() {
        initView();
    }

    private void initView() {
        callPhoneLayout.setOnClickListener(this);
        retrunText.setOnClickListener(this);
        registerTv.setVisibility(View.GONE);
        tailtText.setText("等待认证");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.call_phone_layout:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "13500000000 ");
                intent.setData(data);
                startActivity(intent);

                break;
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
