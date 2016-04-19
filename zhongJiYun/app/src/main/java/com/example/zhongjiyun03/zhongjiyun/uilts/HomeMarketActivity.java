package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class HomeMarketActivity extends AppCompatActivity {


       @ViewInject(R.id.webview)
       private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_market);
        ViewUtils.inject(this);
        init();


    }

    private void init() {
        webView.loadUrl("http://dev.zhongjiyun.cn/store/mobile");


    }

}
