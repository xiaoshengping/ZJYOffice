package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/12/1.
 */

public class SekkDrilingDataBean implements Serializable {

    private String result;
    private String msg;
    private SekkDrilingBean data;

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

    public SekkDrilingBean getData() {
        return data;
    }

    public void setData(SekkDrilingBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SekkDrilingDataBean{" +
                "result='" + result + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
