package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.jpush.android.api.JPushInterface;

public class MoreHotActivity extends AppCompatActivity implements View.OnClickListener {


    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边


    @ViewInject(R.id.web_view)
    private WebView webView;
    private SVProgressHUD mSVProgressHUD;//loding
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE=1;


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

        setContentView(R.layout.activity_more_hot);
        ViewUtils.inject(this);
        inti();
    }

    private void inti() {

        initView();


    }


    private void initView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("每周爆款");
        retrunText.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);

        SQLhelper sqLhelper=new SQLhelper(MoreHotActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(false);
        webSettings.setDatabaseEnabled(false);

        LoadUrl(uid);


    }


    // 加载web
    private void LoadUrl(String uid) {
        // TODO Auto-generated method stubs
        // 设置WebView属性，能够执行Javascript脚本
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new myWebClient());

        //webView.setWebChromeClient(new DefaultWebChromeClient()); // 播放视频
        webView.setWebChromeClient(new WebChromeClient()
        {


            //The undocumented magic method override
            //Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                MoreHotActivity.this.startActivityForResult(Intent.createChooser(i,"File Chooserq"), 1);

            }

            // For Android 3.0+
            @SuppressWarnings("unused")
            public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                MoreHotActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"),	           1);
            }

            //For Android 4.1
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                MoreHotActivity.this.startActivityForResult( Intent.createChooser( i, "File Chooser" ), 1 );
            }
            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean dialog,
                                          boolean userGesture, Message resultMsg) {
                return super.onCreateWindow(view, dialog, userGesture, resultMsg);
            }

            /**
             * 覆盖默认的window.alert展示界面，避免title里显示为“：来自file:////”
             */
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("对话框")
                        .setMessage(message)
                        .setPositiveButton("确定", null);

                // 不需要绑定按键事件
                // 屏蔽keycode等于84之类的按键
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        Log.i("onJsAlert", "keyCode==" + keyCode + "event="+ event);
                        return true;
                    }
                });
                // 禁止响应按back键的事件
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
                return true;
                // return super.onJsAlert(view, url, message, result);
            }

            public boolean onJsBeforeUnload(WebView view, String url,
                                            String message, JsResult result) {
                return super.onJsBeforeUnload(view, url, message, result);
            }

            /**
             67.     * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
             68.     */
            public boolean onJsConfirm(WebView view, String url, String message,
                                       final JsResult result) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("中基云")
                        .setMessage(message)
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                result.confirm();
                            }
                        })
                        .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        result.cancel();
                    }
                });

                // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
                        Log.v("onJsConfirm", "keyCode==" + keyCode + "event="+ event);
                        return true;
                    }
                });
                // 禁止响应按back键的事件
                // builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
                // return super.onJsConfirm(view, url, message, result);
            }

            /**
             108.     * 覆盖默认的window.prompt展示界面，避免title里显示为“：来自file:////”
             109.     * window.prompt('请输入您的域名地址', '618119.com');
             110.     */
            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, final JsPromptResult result) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("对话框").setMessage(message);

                final EditText et = new EditText(view.getContext());
                et.setSingleLine();
                et.setText(defaultValue);
                builder.setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm(et.getText().toString());
                            }

                        })
                        .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        });

                // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
                        Log.v("onJsPrompt", "keyCode==" + keyCode + "event="+ event);
                        return true;
                    }
                });

                // 禁止响应按back键的事件
                // builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
                // return super.onJsPrompt(view, url, message, defaultValue,
                // result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onRequestFocus(WebView view) {
                super.onRequestFocus(view);
            }

            /**
             * 地图定位
             * @param origin
             * @param callback
             */

            /*public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }*/
        });

        if (!TextUtils.isEmpty(uid)){
            webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id="+uid+"&redir=hot");
        }else {
            webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id=''&redir=hot");
            //webView.loadUrl("http://dev.zhongjiyun.cn/app/#/userlogin?username=XKFA0AXRo%2bXmXtC4x4lWxLy5J9fhY6WOfuxZkNEgXPwOXyHFfVNdYpRbgTh5c5GIihUHxT44BPr0GUjqxzNkYIsl69jW3K4wrfKRXfs%2bgYrlcIG%2bdD%2bKP24zKwK0WbIfdBuizgjlMtiQIsM0Db1PsnrpVtlnqpVhPGmhlGRvCNA%3d&pwd=GaY%2bYlQbhyixFWU6%2fuKoQHgwStqMxAM78z1%2fbsFjXeq59yZUBouwHiX5fyCtmCOkutJQ7HGJUmFp%2fq7riHGR62cLyNEkITK63%2bkE1V0iAPuAALZs2dEhz7%2bsHkGSQqYkwhd9oB3zG5uvRIwAQfdz2WmVv43R0J59JW1eQRtg2vE%3d");
        }
        //定位
        //webView.getSettings().setGeolocationEnabled(true);
        //webView.getSettings().setDomStorageEnabled(true);
        //webView.loadUrl("http://h148a34804.iok.la/buluo/new_oauth.php?asp_user_id=72d2f160-0844-4a1e-9cf4-d1a25a413355");
        //webView.loadUrl("http://h148a34804.iok.la/store/mobile/selfreg.php?asp_user_id=72d2f160-0844-4a1e-9cf4-d1a25a413355&redir=home");
    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            mSVProgressHUD.showWithStatus("正在加载中...");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            //拨打电话
           /* if (url.startsWith("tel:")){
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(intent);
            } else if(url.startsWith("http:") || url.startsWith("https:")) {
                view.loadUrl(url);
            }*/
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            mSVProgressHUD.dismiss();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            mSVProgressHUD.dismiss();
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
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
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
