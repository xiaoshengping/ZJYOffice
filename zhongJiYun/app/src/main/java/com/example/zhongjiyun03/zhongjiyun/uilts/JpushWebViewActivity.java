package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.http.SQLHelperUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class JpushWebViewActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.retrun_text_view)
    private TextView retrunTextView; //返回
    @ViewInject(R.id.title_name_tv)
    private TextView titleNameTv; //标题
    @ViewInject(R.id.share_image_icon)
    private ImageView shareImage;  //分享
    @ViewInject(R.id.webview)
    private WebView webView;  //webview
    private SVProgressHUD mSVProgressHUD;//loding



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jpush_web_view);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        initView();

    }

    private void initView() {
        retrunTextView.setOnClickListener(this);
        shareImage.setVisibility(View.GONE);
        mSVProgressHUD = new SVProgressHUD(this);
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(false);
        webSettings.setDatabaseEnabled(false);
        //启用支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        String appShoppingUrl=getIntent().getStringExtra("url");
        String text="{"+"0"+"}";
        if (!TextUtils.isEmpty(appShoppingUrl)){
            //Log.e("部落",AppUtilsUrl.BaseUrl+"buluo/new_oauth.php?asp_user_id="+SQLHelperUtils.queryId(TribeActivity.this));
            if (!TextUtils.isEmpty(SQLHelperUtils.queryId(JpushWebViewActivity.this))){
                webView.loadUrl(appShoppingUrl.replace(text,SQLHelperUtils.queryId(JpushWebViewActivity.this)));
            }else {
                webView.loadUrl(appShoppingUrl.replace(text,""));
            }
            webView.loadUrl(getIntent().getStringExtra("url"));

        }else {


        }
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mSVProgressHUD.showWithStatus("正在加载中...");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mSVProgressHUD.dismiss();
                titleNameTv.setText(webView.getTitle());
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
        finish();
    }

    /**
     * 改写物理按键——返回的逻辑
     * @param keyCode
     * @param event
     * @return
     */
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
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
