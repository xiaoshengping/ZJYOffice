package com.example.zhongjiyun03.zhongjiyun.bean;

import com.example.zhongjiyun03.zhongjiyun.bean.home.SecondHandBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/4/21.
 */
public class SecondHandDataBean implements Serializable {
          private int Total;
          private List<SecondHandBean> PagerData;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }


    public List<SecondHandBean> getPagerData() {
        return PagerData;
    }

    public void setPagerData(List<SecondHandBean> pagerData) {
        PagerData = pagerData;
    }

    @Override
    public String toString() {
        return "SecondHandDataBean{" +
                "Total=" + Total +
                ", PagerData=" + PagerData +
                '}';
    }
}
