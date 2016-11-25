package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/11/22.
 */

public class CellPhoneDataBean implements Serializable {

    private String result;
    private String msg;
    private CellPhoneBean data;

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

    public CellPhoneBean getData() {
        return data;
    }

    public void setData(CellPhoneBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CellPhoneDataBean{" +
                "result='" + result + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
