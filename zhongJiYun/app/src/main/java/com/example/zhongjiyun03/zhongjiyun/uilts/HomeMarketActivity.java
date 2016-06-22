package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

public class HomeMarketActivity extends AppCompatActivity implements View.OnClickListener {


     @ViewInject(R.id.webview)
     private WebView webView;
    @ViewInject(R.id.register_tv)
    private ImageView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边
    private SVProgressHUD mSVProgressHUD;//loding
    private SecondHandHandler secondHandHandler;

    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE=1;


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        webView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        webView.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if(requestCode==FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage) return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
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
        setContentView(R.layout.activity_home_market);
        ViewUtils.inject(this);
        init();


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
    private void init() {
        //http://h148a34804.iok.la/store/mobile/selfreg.php?asp_user_id=2341&redir=home
        initView();




    }

    private void initView() {
        mSVProgressHUD = new SVProgressHUD(this);
        addExtruderTv.setOnClickListener(this);
        titleNemeTv.setText("商城");
        retrunText.setOnClickListener(this);
        secondHandHandler=new SecondHandHandler();
        SQLhelper sqLhelper=new SQLhelper(HomeMarketActivity.this);
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
        //启用支持javascript
        //webView.getSettings().setJavaScriptEnabled(true);

        //webView.setWebChromeClient(new WebChromeClient());
        /*webView.setWebViewClient(new WebViewClient(){
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
                //Log.e("wz",webView.getUrl());
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mSVProgressHUD.dismiss();
            }
        });*/

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
                HomeMarketActivity.this.startActivityForResult(Intent.createChooser(i,"File Chooserq"), 1);

            }

            // For Android 3.0+
            @SuppressWarnings("unused")
            public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                HomeMarketActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"),	           1);
            }

            //For Android 4.1
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                HomeMarketActivity.this.startActivityForResult( Intent.createChooser( i, "File Chooser" ), 1 );
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
            webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id="+uid+"&redir=home");
        }else {
            webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/sess_out.php");
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
            case R.id.register_tv:
                showShare();
                break;



        }




    }



    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
                if ("Wechat".equals(platform.getName())) {
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);

                }
                if ("WechatMoments".equals(platform.getName())) {
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                }
            }
        });
        if (!TextUtils.isEmpty(webView.getUrl())) {
            // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
            //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
            oks.setTitle(webView.getTitle());
            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
            oks.setTitleUrl(webView.getUrl());
            // text是分享文本，所有平台都需要这个字段
            oks.setText(webView.getTitle());
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
            oks.setImageUrl("http://www.zhongjiyun.cn/app/img/logo1.png");
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl(webView.getUrl());
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setComment(webView.getTitle());
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(getString(R.string.app_name));
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl(webView.getUrl());
            oks.setCallback(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                    Log.e("分享成功","分享回调成功");
                    //MyAppliction.showToast("分享回调成功");
                    Message msg = new Message();
                    Bundle b = new Bundle();// 存放数据
                    b.putString("color", "我的");
                    msg.setData(b);
                    HomeMarketActivity.this.secondHandHandler.sendMessage(msg); // 向Handler发送消息，更新UI

                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    Log.e("分享onError","分享回调onError");
                }

                @Override
                public void onCancel(Platform platform, int i) {

                }
            });
            // 启动分享GUI
            oks.show(this);
        }
    }
    /**
     * 接受消息，处理消息 ，此Handler会与当前主线程一块运行
     * */

    class SecondHandHandler extends Handler {
        public SecondHandHandler() {
        }

        public SecondHandHandler(Looper L) {
            super(L);
        }

        // 子类必须重写此方法，接受数据
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            //Log。d("MyHandler"， "handleMessage。。。。。。");
            super.handleMessage(msg);
            // 此处可以更新UI
           /* Bundle b = msg.getData();
            String color = b.getString("color");*/
            shareRedPacket();


        }
    }
    //分享成功获得云币
    private void shareRedPacket() {
        //MyAppliction.showToast("分享回调成功");
        SQLhelper sqLhelper=new SQLhelper(HomeMarketActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }

        if (!TextUtils.isEmpty(uid)){
            HttpUtils httpUtils=new HttpUtils();
            RequestParams requestParams=new RequestParams();
            requestParams.addBodyParameter("aspid",uid);
            if (!TextUtils.isEmpty(webView.getUrl())){
                String str=webView.getUrl();
                if (str.indexOf("id=")!=-1){
                    String newStr = str.substring(str.indexOf("id="),str.length());
                    String newStrOne=newStr.substring(3);
                    requestParams.addBodyParameter("gid",newStrOne);
                   /* Log.e("gid",newStrOne);
                    Log.e("url",webView.getUrl());*/
                }


            }
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getMarketPackedData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("分享获取红包",responseInfo.result);
                    MyAppliction.showToast("分享成功");

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.e("分享获取红包onFailure",s);
                }
            });
        }else {
            MyAppliction.showToast("您还没有登录哦");
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
