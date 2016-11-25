package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/11/22.
 */

public class CellPhoneBean implements Serializable {

    private String PhoneNumber;

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "CellPhoneBean{" +
                "PhoneNumber='" + PhoneNumber + '\'' +
                '}';
    }
}
