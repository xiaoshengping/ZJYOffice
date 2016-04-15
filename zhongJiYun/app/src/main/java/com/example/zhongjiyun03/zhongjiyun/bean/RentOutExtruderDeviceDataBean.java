package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/15.
 */
public class RentOutExtruderDeviceDataBean implements Serializable {

           private RentOutExtruderDeviceBean Device;

    public RentOutExtruderDeviceBean getDevice() {
        return Device;
    }

    public void setDevice(RentOutExtruderDeviceBean device) {
        Device = device;
    }

    @Override
    public String toString() {
        return "RentOutExtruderDeviceDataBean{" +
                "Device=" + Device +
                '}';
    }
}
