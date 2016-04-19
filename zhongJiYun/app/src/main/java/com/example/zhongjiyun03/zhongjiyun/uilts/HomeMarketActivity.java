package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
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
        //http://h148a34804.iok.la/store/mobile/selfreg.php?asp_user_id=2341&redir=home
        SQLhelper sqLhelper=new SQLhelper(HomeMarketActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id

        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        webView.getSettings().setJavaScriptEnabled(true);
        if (!TextUtils.isEmpty(uid)){
            webView.loadUrl("http://h148a34804.iok.la/store/mobile/selfreg.php?asp_user_id="+uid+"&redir=home");

        }else {
            webView.loadUrl("http://h148a34804.iok.la/store/mobile/");
        }



    }

}
