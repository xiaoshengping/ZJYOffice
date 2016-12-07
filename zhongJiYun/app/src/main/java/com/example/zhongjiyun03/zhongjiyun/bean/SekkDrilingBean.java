package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/12/1.
 */

public class SekkDrilingBean implements Serializable {
          private String HaveBossPassDevice;

    public String getHaveBossPassDevice() {
        return HaveBossPassDevice;
    }

    public void setHaveBossPassDevice(String haveBossPassDevice) {
        HaveBossPassDevice = haveBossPassDevice;
    }

    @Override
    public String toString() {
        return "SekkDrilingBean{" +
                "HaveBossPassDevice='" + HaveBossPassDevice + '\'' +
                '}';
    }
}
