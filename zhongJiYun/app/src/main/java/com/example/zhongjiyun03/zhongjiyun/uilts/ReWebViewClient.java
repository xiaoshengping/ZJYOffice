package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;

/**
 * Created by ZHONGJIYUN03 on 2016/10/27.
 */

public class ReWebViewClient extends WebViewClient {
    private SVProgressHUD mSVProgressHUD ;//loding
    private TextView views;
    private Context context;


    public ReWebViewClient(Context context, TextView views) {
        this.context=context;
        mSVProgressHUD = new SVProgressHUD(context);
        this.views=views;

    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub
        //拨打电话
        if (url.startsWith("tel:")){
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
            context.startActivity(intent);
        } else if(url.startsWith("http:") || url.startsWith("https:")) {
            view.loadUrl(url);
        }
        //view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        mSVProgressHUD.showWithStatus("正在加载中...", SVProgressHUD.SVProgressHUDMaskType.Black);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        mSVProgressHUD.dismiss();
        views.setText(view.getTitle());
    }
}
