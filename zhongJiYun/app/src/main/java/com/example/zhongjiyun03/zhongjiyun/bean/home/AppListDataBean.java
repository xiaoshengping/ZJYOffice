package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/4/6.
 */
public class AppListDataBean<T> implements Serializable {

    private String result;
    private List<T> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AppListDataBean{" +
                "result='" + result + '\'' +
                ", data=" + data +
                '}';
    }
}
