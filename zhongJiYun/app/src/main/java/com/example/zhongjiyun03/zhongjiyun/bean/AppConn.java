package com.example.zhongjiyun03.zhongjiyun.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * @作 用:
 * @创 建 人: zhangwei
 * @日 期: 15/3/29 14:54
 * @修 改 人:
 * @日 期:
 */
public class AppConn<T> implements Serializable {
    @JSONField(name = "r")
    private String state;

    private List<T> data;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
