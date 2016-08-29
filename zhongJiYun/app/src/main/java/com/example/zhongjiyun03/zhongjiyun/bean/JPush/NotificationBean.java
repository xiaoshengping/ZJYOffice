package com.example.zhongjiyun03.zhongjiyun.bean.JPush;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/7/28.
 */
public class NotificationBean implements Serializable {
        private AndroidJPushBean android;

    public AndroidJPushBean getAndroid() {
        return android;
    }

    public void setAndroid(AndroidJPushBean android) {
        this.android = android;
    }

    @Override
    public String toString() {
        return "NotificationBean{" +
                "android=" + android +
                '}';
    }
}
