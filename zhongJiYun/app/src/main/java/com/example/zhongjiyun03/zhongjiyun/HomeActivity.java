package com.example.zhongjiyun03.zhongjiyun;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.zhongjiyun03.zhongjiyun.fragment.FragmentTabAdapter;
import com.example.zhongjiyun03.zhongjiyun.fragment.HomeFragment;
import com.example.zhongjiyun03.zhongjiyun.fragment.MineFragment;
import com.example.zhongjiyun03.zhongjiyun.fragment.SeekMachinistFragment;
import com.example.zhongjiyun03.zhongjiyun.fragment.SeekProjectFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private ArrayList<Fragment> fragments=new ArrayList<Fragment>();
    @ViewInject(R.id.home_rg)
    private RadioGroup homeRG;
    private long mExitTime;
    public Boolean isFirstIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        ViewUtils.inject(this);
        init();
        /*SharedPreferences pref = this.getSharedPreferences("myActivityName", 0);
        //取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        isFirstIn = pref.getBoolean("isFirstIn", true);
        if(isFirstIn) {
            Intent intent = new Intent().setClass(HomeActivity.this,MainActivity.class);
            startActivityForResult(intent,0);
        }*/
        //HomeFragment.setStart(0);
        //startPage();

        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
        String device_token = UmengRegistrar.getRegistrationId(this);
        Log.e("shdhdhdh",device_token);
        //开启推送并设置注册的回调处理
        mPushAgent.enable(new IUmengRegisterCallback() {

            @Override
            public void onRegistered(String registrationId) {
                //onRegistered方法的参数registrationId即是device_token
                Log.d("device_token", registrationId);
            }
        });

        PushAgent.getInstance(this).onAppStart();
    }




    private void init() {
        addFragment();
        FragmentTabAdapter fragmentTabAdapter=new FragmentTabAdapter(HomeActivity.this,fragments,R.id.home_layout,homeRG);


    }



    private void addFragment() {
        fragments.add(new HomeFragment());
        fragments.add(new SeekProjectFragment());
        fragments.add(new SeekMachinistFragment());
        fragments.add(new MineFragment());
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                HomeActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
