package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class CooperationActivity extends AppCompatActivity implements View.OnClickListener {


    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边


    @ViewInject(R.id.web_view)
    private WebView webView;
    private SVProgressHUD mSVProgressHUD;//loding

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooperation);
        ViewUtils.inject(this);
        inti();

    }

    private void inti() {

        initView();


    }


    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("合作伙伴");
        retrunText.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);
        String url= AppUtilsUrl.BaseUrl+"/app/index.html#/tab/partner";
        webView.getSettings().setSupportZoom(true);          //支持缩放
        webView.getSettings().setBuiltInZoomControls(true);  //启用内置缩放装置
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mSVProgressHUD.showWithStatus("正在加载中...");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mSVProgressHUD.dismiss();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mSVProgressHUD.dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;

        }
    }


    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();//返回上一页面
                return true;
            }
            else
            {
                finish();
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
