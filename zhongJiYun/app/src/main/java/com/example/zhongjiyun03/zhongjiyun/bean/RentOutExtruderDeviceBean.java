package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/14.
 */
public class RentOutExtruderDeviceBean implements Serializable {
        private String OwnId;

    public String getOwnId() {
        return OwnId;
    }

    public void setOwnId(String ownId) {
        OwnId = ownId;
    }

    @Override
    public String toString() {
        return "RentOutExtruderDeviceBean{" +
                "OwnId='" + OwnId + '\'' +
                '}';
    }
}
