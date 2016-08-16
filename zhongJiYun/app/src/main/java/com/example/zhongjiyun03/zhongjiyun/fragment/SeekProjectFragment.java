package com.example.zhongjiyun03.zhongjiyun.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.popwin.PopupWindowHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeekProjectFragment extends Fragment implements View.OnClickListener {



    @ViewInject(R.id.web_view)
    private WebView webView;
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边
    private SVProgressHUD mSVProgressHUD;//loding
    @ViewInject(R.id.register_tv)
    private TextView registerTv;


    public SeekProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_seek_project, container, false);
        ViewUtils.inject(this,view);
        PopupWindowHelper.init(getActivity());

        init();

        return view;
    }
    private void init() {
        initView();

    }

    private void initView() {
        mSVProgressHUD = new SVProgressHUD(getActivity());
        registerTv.setVisibility(View.GONE);

        retrunText.setOnClickListener(this);
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
        webView.loadUrl(AppUtilsUrl.BaseUrl+"App/index.html#/tab/my/owner-news");
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
               /* AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(message)
                        .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                            }
                        }).show();*/
                //result.cancel();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, final JsResult result) {
                // TODO Auto-generated method stub
                Log.i("m-----dmfmf", "onJsConfirm" + "," + "url: " + url);

               /* DialogUtils.dialogBuilder(mContext, "温馨提示", message,
                        new DialogCallBack() {

                            @Override
                            public void onCompate() {
                                Log.i(TAG, "onJsConfirm,onCompate");
                                result.confirm();
                            }

                            @Override
                            public void onCancel() {
                                Log.i(TAG, "onJsConfirm,onCancel");
                                result.cancel();
                            }
                        });*/
                return true;
            }
        });
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
                titleNemeTv.setText(webView.getTitle());
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
                webView.goBack();//返回上一页面
                break;




        }




    }
    /*//改写物理按键——返回的逻辑
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
                getActivity().finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }*/



}
