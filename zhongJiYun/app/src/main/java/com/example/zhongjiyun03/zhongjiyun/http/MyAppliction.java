package com.example.zhongjiyun03.zhongjiyun.http;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by xiaoshengping on 2015/9/8.
 */
public class MyAppliction extends Application {
    public static ImageLoader imageLoader = ImageLoader.getInstance();
    public static DisplayImageOptions options;
    public static DisplayImageOptions RoundedOptions;
    public static DisplayImageOptions RoundedOptionsOne;
    private static MyAppliction app = null;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        app=this;
        initImageLoader(getApplicationContext());
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_img)//加载等待时显示的图片
                .showImageForEmptyUri(R.mipmap.default_img)//加载数据为空时显示的图片
                .showImageOnFail(R.mipmap.default_img)//加载失败时显示的图片
                .build();
        RoundedOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.default_img)//加载等待 时显示的图片
                .showImageForEmptyUri(R.mipmap.default_img)//加载数据为空时显示的图片
                .showImageOnFail(R.mipmap.default_img)//加载失败时显示的图片
                .cacheInMemory()
                .cacheOnDisc()
                .displayer(new RoundedBitmapDisplayer(200))
                .build();
        RoundedOptionsOne = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.default_img)//加载等待 时显示的图片
                .showImageForEmptyUri(R.mipmap.default_img)//加载数据为空时显示的图片
                .showImageOnFail(R.mipmap.default_img)//加载失败时显示的图片
                .cacheInMemory()
                .cacheOnDisc()
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        //.enableLogging() // Not necessary in common

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
        //imageLoader.init(ImageLoaderConfiguration.createDefault(context));

    }
    /**
     * 拿到上下文对象
     *
     *
     */
    public static MyAppliction getContextInstance(){
        return app;
    }
    /**
     * 土司
     */
    public static void showToast(String msg){
        Toast.makeText(app, msg, Toast.LENGTH_SHORT).show();
    }

    //对话框
    public static void showExitGameAlert(String text,Context app) {
        final AlertDialog dlg = new AlertDialog.Builder(app).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.tishi_exit_dialog);
        TextView tailte = (TextView) window.findViewById(R.id.tailte_tv);
        tailte.setText(text);
        // 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText("确定");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }
}
