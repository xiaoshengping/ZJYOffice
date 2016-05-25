package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边
    private SVProgressHUD mSVProgressHUD;//loding
    private SecondHandHandler secondHandHandler;

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
        titleNemeTv.setText("");
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
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(false);
        //启用支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        if (!TextUtils.isEmpty(uid)){
            webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id="+uid+"&redir=home");

        }else {
            webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/");
        }
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
