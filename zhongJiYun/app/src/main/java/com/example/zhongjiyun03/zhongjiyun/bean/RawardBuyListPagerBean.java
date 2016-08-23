package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/8/23.
 */
public class RawardBuyListPagerBean implements Serializable {

    private int Total; //zongshu
    private List<RawardBuyListBean> PagerData;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public List<RawardBuyListBean> getPagerData() {
        return PagerData;
    }

    public void setPagerData(List<RawardBuyListBean> pagerData) {
        PagerData = pagerData;
    }

    @Override
    public String toString() {
        return "RawardBuyListPagerBean{" +
                "Total=" + Total +
                ", PagerData=" + PagerData +
                '}';
    }
}
