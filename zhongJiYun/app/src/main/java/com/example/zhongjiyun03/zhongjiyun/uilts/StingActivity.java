package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;
import java.math.BigDecimal;

import cn.jpush.android.api.JPushInterface;

public class StingActivity extends AppCompatActivity implements View.OnClickListener {


       @ViewInject(R.id.sting_layout)
       private LinearLayout modificationLayout;
       @ViewInject(R.id.competitive_layout)
       private LinearLayout clearLayout;
       @ViewInject(R.id.attention_layout)
       private LinearLayout ideaLayout;
       @ViewInject(R.id.message_layout)
       private LinearLayout reputationLayout;
       @ViewInject(R.id.redpacket_layout)
       private LinearLayout newsMessageLayout;
       @ViewInject(R.id.exit_button)
       private Button exitButton;
       @ViewInject(R.id.register_tv)
       private TextView addExtruderTv;   //头部右边
       @ViewInject(R.id.title_name_tv)
       private TextView titleNemeTv;     //头部中间
       @ViewInject(R.id.retrun_text_view)
       private TextView retrunText;     //头部左边
       private String uids;
       @ViewInject(R.id.text_number)
       private TextView textNumber;
       @ViewInject(R.id.about_layout)
       private LinearLayout aboutLayout;
       @ViewInject(R.id.switch_button)
       private Switch switchButton;



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
        setContentView(R.layout.activity_sting);
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
        intiView();

    }

    @SuppressLint("NewApi")
    private void intiView() {

        modificationLayout.setOnClickListener(this);
        clearLayout.setOnClickListener(this);
        ideaLayout.setOnClickListener(this);
        exitButton.setOnClickListener(this);
        addExtruderTv.setVisibility(View.GONE);
        aboutLayout.setOnClickListener(this);
        titleNemeTv.setText("设置");
        retrunText.setOnClickListener(this);
        File file=new File(String.valueOf((StingActivity.this).getExternalCacheDir()));
        try {
            Log.e("文件大小",bytes2kb(getFolderSize(file))+"");
            textNumber.setText(bytes2kb(getFolderSize(file))+"M");
        } catch (Exception e) {
            e.printStackTrace();
        }
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    /*PushAgent mPushAgent = PushAgent.getInstance(StingActivity.this);
                    mPushAgent.enable();*/
                    JPushInterface.resumePush(getApplicationContext());
                    // 打开推送
                }else {
                    /*PushAgent mPushAgent = PushAgent.getInstance(StingActivity.this);
                    mPushAgent.disable();*/
                    JPushInterface.stopPush(getApplicationContext());
                    //关闭推送
                }
            }
        });
        SharedPreferences sharedPreferences = this.getSharedPreferences("switchButton", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFirstRun)
        {
            switchButton.setChecked(true);
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        } else{

        }

    }
    public String bytes2kb(long bytes) {

        BigDecimal filesize = new BigDecimal(bytes);

        BigDecimal megabyte = new BigDecimal(1024 * 1024);

        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();

        return returnValue + "";

    }
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }



    @Override
    protected void onResume() {
        super.onResume();
        SQLhelper sqLhelper=new SQLhelper(StingActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id

        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        if (!TextUtils.isEmpty(uid)){
            exitButton.setVisibility(View.VISIBLE);

        }else{

            exitButton.setVisibility(View.GONE);
        }
        uids=uid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sting_layout:
                if (!TextUtils.isEmpty(uids)){
                    Intent intent=new Intent(StingActivity.this,ModificationPhoneActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }else {
                  Intent intent=new Intent(StingActivity.this,LoginActivity.class);
                  startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;
            case R.id.competitive_layout:
                showExitGameAlert("是否清理缓存","1");
                break;
            case R.id.attention_layout:
                Intent ideaIntent=new Intent(StingActivity.this,IdeaActivity.class);
                startActivity(ideaIntent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case R.id.exit_button:
                showExitGameAlert("是否退出登录","2");

                break;
            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.about_layout:
                Intent aboutIntent=new Intent(StingActivity.this,AboutActivity.class);
                startActivity(aboutIntent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;



        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
// KeyEvent.KEYCODE_BACK代表返回操作.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 处理返回操作.
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);

        }
        return true;
    }

    //对话框
    private void showExitGameAlert(String text, final String tage) {
        final AlertDialog dlg = new AlertDialog.Builder(StingActivity.this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.shrew_exit_dialog);
        TextView tailte = (TextView) window.findViewById(R.id.tailte_tv);
        tailte.setText(text);
        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        ok.setText("确定");
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (tage.equals("2")){
                    SQLhelper sqLhelper = new SQLhelper(StingActivity.this);
                    SQLiteDatabase db = sqLhelper.getWritableDatabase();
                    Cursor cursor = db.query(SQLhelper.tableName, null, null, null, null, null, null);
                    while (cursor.moveToNext()) {
                        String uid = cursor.getString(0);
                        if (!TextUtils.isEmpty(uid)) {
                            db.delete(SQLhelper.tableName, null, null);
                        }
                    }
                   /* SharedPreferences.Editor editor = getSharedPreferences("lock", MODE_WORLD_WRITEABLE).edit();
                    editor.remove("code");
                    editor.commit();*/
                   /* SharedPreferences.Editor editor = getSharedPreferences("lock", MODE_WORLD_WRITEABLE).edit();
                    editor.clear().commit();*/
                    finish();
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                }else if (tage.equals("1")){
                   // cleanInternalCache(StingActivity.this);
                    textNumber.setText("0M");
                    //cleanCustomCache("/sdcard/zhongJiYunImage/");
                }

                dlg.cancel();
            }
        });

        // 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText("取消");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() &&
                directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

}
