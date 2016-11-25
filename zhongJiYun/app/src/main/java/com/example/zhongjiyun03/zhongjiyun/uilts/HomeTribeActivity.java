package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.home.WebViewBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.WebViewDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.SQLHelperUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

public class HomeTribeActivity extends AppCompatActivity implements View.OnClickListener, ReWebChomeClient.OpenFileChooserCallBack,ReWebChomeClient.OpenFileChooserCallBacks {

    @ViewInject(R.id.retrun_text_view)
    private TextView retrunTextView; //返回
    @ViewInject(R.id.title_name_tv)
    private TextView titleNameTv; //标题
    @ViewInject(R.id.share_image)
    private ImageView shareImage;  //分享
    @ViewInject(R.id.webview)
    private WebView webView;  //webview

    // private static final String TAG = "MyActivity";
    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;
    private Intent mSourceIntent;
    private ValueCallback<Uri> mUploadMsg;
    private ValueCallback<Uri[]> mUploadMsgs;
    public static final String TAG = "HomeTribeActivity";

    private static SecondHandHandler secondHandHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tribe);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        initView();

    }

    private void initView() {
        retrunTextView.setOnClickListener(this);
        shareImage.setOnClickListener(this);
        secondHandHandler=new SecondHandHandler();
        //启用支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以访问文件
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebChromeClient(new ReWebChomeClient(HomeTribeActivity.this,this));
        webView.setWebViewClient(new ReWebViewClient(this,titleNameTv));
        fixDirPath();



        String tage = getIntent().getStringExtra("tage");
        String uid=SQLHelperUtils.queryId(HomeTribeActivity.this);
        String type=getIntent().getStringExtra("type");
        if (type.equals("Tribe")) {
            if (tage.equals("2")) {
                if (!TextUtils.isEmpty(SQLHelperUtils.queryId(HomeTribeActivity.this))) {
                    //Log.e("部落",AppUtilsUrl.BaseUrl+"buluo/new_oauth.php?asp_user_id="+SQLHelperUtils.queryId(HomeTribeActivity.this));
                    webView.loadUrl(AppUtilsUrl.BaseUrl + "buluo/new_oauth.php?asp_user_id=" + SQLHelperUtils.queryId(HomeTribeActivity.this));

                } else {
                    webView.loadUrl(AppUtilsUrl.BaseUrl + "buluo/new_oauth.php?asp_user_id=");
                }
            } else {
                String appShoppingUrl = getIntent().getStringExtra("appUrl");
                String text = "{" + "0" + "}";
                if (!TextUtils.isEmpty(appShoppingUrl)) {
                    if (!TextUtils.isEmpty(SQLHelperUtils.queryId(HomeTribeActivity.this))) {
                        webView.loadUrl(appShoppingUrl.replace(text, SQLHelperUtils.queryId(HomeTribeActivity.this)));
                    } else {
                        webView.loadUrl(appShoppingUrl.replace(text, ""));
                    }
                }
            }
        }else if (type.equals("HomeShoping")) {

            if (tage.equals("0")) {

                if (!TextUtils.isEmpty(uid)) {
                    webView.loadUrl(AppUtilsUrl.BaseUrl + "store/mobile/selfreg.php?asp_user_id=" + uid + "&redir=home");
                } else {
                    webView.loadUrl(AppUtilsUrl.BaseUrl + "store/mobile/sess_out.php");
                    //webView.loadUrl("http://dev.zhongjiyun.cn/app/#/userlogin?username=XKFA0AXRo%2bXmXtC4x4lWxLy5J9fhY6WOfuxZkNEgXPwOXyHFfVNdYpRbgTh5c5GIihUHxT44BPr0GUjqxzNkYIsl69jW3K4wrfKRXfs%2bgYrlcIG%2bdD%2bKP24zKwK0WbIfdBuizgjlMtiQIsM0Db1PsnrpVtlnqpVhPGmhlGRvCNA%3d&pwd=GaY%2bYlQbhyixFWU6%2fuKoQHgwStqMxAM78z1%2fbsFjXeq59yZUBouwHiX5fyCtmCOkutJQ7HGJUmFp%2fq7riHGR62cLyNEkITK63%2bkE1V0iAPuAALZs2dEhz7%2bsHkGSQqYkwhd9oB3zG5uvRIwAQfdz2WmVv43R0J59JW1eQRtg2vE%3d");
                }

            } else {
                String appShoppingUrl = getIntent().getStringExtra("appUrl");
                String text = "{" + "0" + "}";
                //webView.loadUrl(appShoppingUrl);
                if (!TextUtils.isEmpty(appShoppingUrl)) {
                    if (!TextUtils.isEmpty(uid)) {
                        webView.loadUrl(appShoppingUrl.replace(text, uid));
                    } else {
                        webView.loadUrl(appShoppingUrl.replace(text, ""));
                    }
                }

            }
        }else if (type.equals("game")){
            if (!TextUtils.isEmpty(uid)){
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id="+uid+"&redir=game");

            }else {
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id=''&redir=game");
            }
        }else if (type.equals("duoBao")){
            if (!TextUtils.isEmpty(uid)){
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id="+uid+"&redir=yun");
            }else {
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id=''&redir=yun");

            }
        }else if (type.equals("Conersion")){
            if (!TextUtils.isEmpty(uid)){
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id="+uid+"&redir=yun_change");
            }else {
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id=''&redir=yun_change");
            }
        }else if (type.equals("easy")){
            if (!TextUtils.isEmpty(uid)){
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id="+uid+"&redir=easy");
            }else {
                webView.loadUrl(AppUtilsUrl.BaseUrl + "store/mobile/selfreg.php?asp_user_id=''&redir=easy");
            }
        }else if (type.equals("MoreHot")){
            if (!TextUtils.isEmpty(uid)){
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id="+uid+"&redir=hot");
            }else {
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id=''&redir=hot");

            }
        }else if (type.equals("Discount")){
            if (!TextUtils.isEmpty(uid)){
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id="+uid+"&redir=coupon");

            }else {
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id=''&redir=coupon");
            }
        }else if (type.equals("blacklist")){

            if (!TextUtils.isEmpty(uid)){
                //Log.e("黑名单",AppUtilsUrl.BaseUrl+"buluo/new_oauth.php?b=4&asp_user_id="+uid);
                webView.loadUrl(AppUtilsUrl.BaseUrl+"buluo/new_oauth.php?b=4&asp_user_id="+uid);

            }else {
                webView.loadUrl(AppUtilsUrl.BaseUrl+"buluo/new_oauth.php?b=4&asp_user_id=");
            }
        }else if (type.equals("dicuntCoupon")){
            if (!TextUtils.isEmpty(uid)){
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id="+uid+"&redir=coupon");

            }else {
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id=''&redir=coupon");
            }
        }else if (type.equals("mainproject")){
            if (!TextUtils.isEmpty(uid)){
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id="+uid+"&redir=hot");
            }else {
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id=''&redir=hot");
                }
        }else if (type.equals("MoreEasyBuy")){
            if (!TextUtils.isEmpty(uid)){
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id="+uid+"&redir=easy");
            }else {
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id=''&redir=easy");
               }
        }else if (type.equals("MoreDuoBao")){
            if (!TextUtils.isEmpty(uid)){
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id="+uid+"&redir=yun");
            }else {
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id=''&redir=yun");
               }
        }else if (type.equals("MoreConersion")){
            if (!TextUtils.isEmpty(uid)){
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id="+uid+"&redir=yun_change");
            }else {
                webView.loadUrl(AppUtilsUrl.BaseUrl+"store/mobile/selfreg.php?asp_user_id=''&redir=yun_change");
                }
        }else if (type.equals("Cooperation")){

                webView.loadUrl(AppUtilsUrl.BaseUrl+"/app/index.html#/tab/partner");

        }



    }


    /**
     * 返回文件选择
     */


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            if (mUploadMsg != null) {
                mUploadMsg.onReceiveValue(null);
                mUploadMsg = null;
            }
            if (mUploadMsgs != null) {
                mUploadMsgs.onReceiveValue(null);
                mUploadMsgs = null;
            }
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_IMAGE_CAPTURE:
            case REQUEST_CODE_PICK_IMAGE: {
                try {
                    if (mUploadMsg == null&&mUploadMsgs==null) {
                        return;
                    }
                    String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);
                    if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                        Log.w(TAG, "sourcePath empty or not exists.");
                        break;
                    }
                    Uri uri = Uri.fromFile(new File(sourcePath));
                    if (mUploadMsg!=null){
                        mUploadMsg.onReceiveValue(uri);
                    }
                    if (mUploadMsgs!=null){
                        Uri[] uris=new Uri[1];
                        uris[0]=uri;
                        mUploadMsgs.onReceiveValue(uris);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
        mUploadMsg = uploadMsg;
        showPhontDialog();
    }
    @Override
    public void openFileChooserCallBacks(ValueCallback<Uri[]> uploadMsg, String acceptType) {
        mUploadMsgs=uploadMsg;
        showPhontDialog();
    }

    public void showOptions() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setOnCancelListener(new ReOnCancelListener());
        alertDialog.setTitle("请选择");
        alertDialog.setItems(R.array.options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            mSourceIntent = ImageUtil.choosePicture();
                            startActivityForResult(mSourceIntent, REQUEST_CODE_PICK_IMAGE);
                        } else {
                            mSourceIntent = ImageUtil.takeBigPicture();
                            startActivityForResult(mSourceIntent, REQUEST_CODE_IMAGE_CAPTURE);
                        }
                    }
                }
        );
        alertDialog.show();
    }


    /**
     * 拍照和相册弹出框
     *
     */
    private void showPhontDialog() {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        final Dialog dialog = new Dialog(HomeTribeActivity.this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Button pictureDialogButton= (Button) view.findViewById(R.id.picture_dialog_button);
        Button photographDialogButton= (Button) view.findViewById(R.id.photograph_dialog_button);
        Button cancelDialogButton= (Button) view.findViewById(R.id.cancel_dialog_button);
        pictureDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(HomeTribeActivity.this,Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(HomeTribeActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            0);

                } else {
                    mSourceIntent = ImageUtil.takeBigPicture();
                    startActivityForResult(mSourceIntent, REQUEST_CODE_IMAGE_CAPTURE);
                }

                dialog.dismiss();

            }
        });
        photographDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSourceIntent = ImageUtil.choosePicture();
                startActivityForResult(mSourceIntent, REQUEST_CODE_PICK_IMAGE);
                dialog.dismiss();


            }
        });
        cancelDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadMsg != null) {
                    mUploadMsg.onReceiveValue(null);
                    mUploadMsg = null;
                }
                if (mUploadMsgs != null) {
                    mUploadMsgs.onReceiveValue(null);
                    mUploadMsgs = null;
                }
                dialog.dismiss();
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == 0)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                mSourceIntent = ImageUtil.takeBigPicture();
                startActivityForResult(mSourceIntent, REQUEST_CODE_IMAGE_CAPTURE);
            }else {
                MyAppliction.showToast("您已经禁止了拍照权限，请打开应用权限");
            }
            return;
        }else if (requestCode==3){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
            }else {
                MyAppliction.showToast("您已经禁止了SD卡权限，请打开应用权限");
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void fixDirPath() {
        String path = ImageUtil.getDirPath();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private class ReOnCancelListener implements DialogInterface.OnCancelListener {

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if (mUploadMsg != null) {
                mUploadMsg.onReceiveValue(null);
                mUploadMsg = null;
            }
            if (mUploadMsgs != null) {
                mUploadMsgs.onReceiveValue(null);
                mUploadMsgs = null;
            }
        }
    }








    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.retrun_text_view:
                finish();
                break;
            case R.id.share_image:
                showShare(HomeTribeActivity.this,webView.getTitle(),webView.getUrl());
                break;
        }

    }


    public static void showShare(Context context, String tailt, final String url) {
        ShareSDK.initSDK(context);
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

        if (!TextUtils.isEmpty(url)) {
            // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
            //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
            oks.setTitle(tailt);
            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
            oks.setTitleUrl(url);
            // text是分享文本，所有平台都需要这个字段
            oks.setText(tailt);
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
            oks.setImageUrl("http://www.zhongjiyun.cn/app/img/logo1.png");
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl(url);
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setComment(tailt);
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(context.getString(R.string.app_name));
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl(url);
            oks.setCallback(new PlatformActionListener() {

                @Override
                public void onError(Platform arg0, int arg1, Throwable arg2) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                    // TODO Auto-generated method stub
                    //MyAppliction.showToast("经济数据节点节点");
                    Message msg = new Message();
                    Bundle b = new Bundle();// 存放数据
                    b.putString("color", url);
                    msg.setData(b);
                    secondHandHandler.sendMessage(msg); // 向Handler发送消息，更新UI

                }

                @Override
                public void onCancel(Platform arg0, int arg1) {
                    // TODO Auto-generated method stub

                }
            });



            // 启动分享GUI
            oks.show(context);
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
            Bundle b = msg.getData();
            String url = b.getString("color");
            shareRedPacket(url);


        }
    }
    //分享成功获得云币
    private void shareRedPacket(String url) {
        if (!TextUtils.isEmpty(SQLHelperUtils.queryId(getApplicationContext()))){
            HttpUtils httpUtils =new HttpUtils();
            RequestParams requestParams=new RequestParams();
            requestParams.addBodyParameter("aspid",SQLHelperUtils.queryId(getApplicationContext()));
            if (!TextUtils.isEmpty(url)){

                if (url.indexOf("id=")!=-1){
                    String newStr = url.substring(url.indexOf("id="),url.length());
                    String newStrOne=newStr.substring(3);
                    requestParams.addBodyParameter("gid",newStrOne);
                }


            }
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getMarketPackedData(), new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (!TextUtils.isEmpty(responseInfo.result)){
                        WebViewDataBean webViewDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<WebViewDataBean>(){});
                        if (webViewDataBean!=null){
                            WebViewBean webViewBean= webViewDataBean.getData();
                            if (!TextUtils.isEmpty(webViewBean.getCoinCount())){
                                if (Integer.valueOf(webViewBean.getCoinCount())>0){
                                    new AlertDialog.Builder(HomeTribeActivity.this)
                                            .setMessage("本次分享获得"+webViewBean.getCoinCount()+"云币")
                                            .setPositiveButton("确定", null)
                                            .show();
                                }else {
                                    new AlertDialog.Builder(HomeTribeActivity.this)
                                            .setMessage(webViewDataBean.getMsg())
                                            .setPositiveButton("确定", null)
                                            .show();
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });
        }else {
            MyAppliction.showToast("您还没有登录,请登录");
        }


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
