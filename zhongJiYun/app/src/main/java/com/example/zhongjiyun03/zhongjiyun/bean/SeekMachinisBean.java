package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/4/14.
 */
public class SeekMachinisBean implements Serializable {
          private int Total;
          private List<SekkMachinisDataBean> PagerData;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public List<SekkMachinisDataBean> getPagerData() {
        return PagerData;
    }

    public void setPagerData(List<SekkMachinisDataBean> pagerData) {
        PagerData = pagerData;
    }

    @Override
    public String toString() {
        return "SeekMachinisBean{" +
                "Total=" + Total +
                ", PagerData=" + PagerData +
                '}';
    }
}
