package com.example.zhongjiyun03.zhongjiyun;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private TimeCount time;      //获取验证码计时线程
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        time = new TimeCount(3000, 1000);//构造CountDownTimer对象
        time.start();
    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            (WelcomeActivity.this).finish();
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示

        }

    }

}
