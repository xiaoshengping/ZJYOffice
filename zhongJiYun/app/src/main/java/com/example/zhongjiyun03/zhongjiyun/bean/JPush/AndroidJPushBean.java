package com.example.zhongjiyun03.zhongjiyun.bean.JPush;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/7/28.
 */
public class AndroidJPushBean implements Serializable {
       private  ExtrasBean extras;

    public ExtrasBean getExtras() {
        return extras;
    }

    public void setExtras(ExtrasBean extras) {
        this.extras = extras;
    }

    @Override
    public String toString() {
        return "AndroidJPushBean{" +
                "extras=" + extras +
                '}';
    }
}
