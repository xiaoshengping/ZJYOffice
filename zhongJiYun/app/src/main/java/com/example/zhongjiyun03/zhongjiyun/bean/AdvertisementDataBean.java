package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/11/23.
 */

public class AdvertisementDataBean implements Serializable {

    private String result;
    private String msg;
    private AdvertisementPagerData data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public AdvertisementPagerData getData() {
        return data;
    }

    public void setData(AdvertisementPagerData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AdvertisementDataBean{" +
                "result='" + result + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
