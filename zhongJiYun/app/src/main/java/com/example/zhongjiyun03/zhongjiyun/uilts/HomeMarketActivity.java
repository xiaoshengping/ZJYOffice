package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class HomeMarketActivity extends AppCompatActivity implements View.OnClickListener {


     @ViewInject(R.id.webview)
     private WebView webView;
    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_market);
        ViewUtils.inject(this);
        init();


    }

    private void init() {
        //http://h148a34804.iok.la/store/mobile/selfreg.php?asp_user_id=2341&redir=home
        initView();




    }

    private void initView() {
        addExtruderTv.setOnClickListener(this);
        titleNemeTv.setText("");
        retrunText.setOnClickListener(this);
        SQLhelper sqLhelper=new SQLhelper(HomeMarketActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }//启用支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        if (!TextUtils.isEmpty(uid)){
            webView.loadUrl("http://h148a34804.iok.la/store/mobile/selfreg.php?asp_user_id="+uid+"&redir=home");

        }else {
            webView.loadUrl("http://h148a34804.iok.la/store/mobile/");
        }
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
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
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
