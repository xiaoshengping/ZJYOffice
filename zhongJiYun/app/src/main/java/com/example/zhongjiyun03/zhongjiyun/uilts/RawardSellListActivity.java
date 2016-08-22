package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RawardSellListActivity extends AppCompatActivity implements View.OnClickListener {


    @ViewInject(R.id.register_tv)
    private TextView comtintJobText;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raward_sell_list);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {
        titleNemeTv.setText("悬赏求卖");
        comtintJobText.setVisibility(View.GONE);
        retrunText.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

            finish();

    }
}
