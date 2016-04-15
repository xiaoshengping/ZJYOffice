package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/2.
 */
public class ExtruderDataBean implements Serializable {

    private ExtruderDeviceBean Device;

    public ExtruderDeviceBean getDevice() {
        return Device;
    }

    public void setDevice(ExtruderDeviceBean device) {
        Device = device;
    }

    @Override
    public String toString() {
        return "ExtruderDataBean{" +
                "Device=" + Device +
                '}';
    }
}
