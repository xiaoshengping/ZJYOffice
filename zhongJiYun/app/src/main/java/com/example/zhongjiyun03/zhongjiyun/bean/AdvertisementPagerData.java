package com.example.zhongjiyun03.zhongjiyun.bean;

import com.example.zhongjiyun03.zhongjiyun.bean.home.AdvertisementBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/11/23.
 */

public class AdvertisementPagerData implements Serializable {
    private int Total;
    private List<AdvertisementBean> PagerData;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public List<AdvertisementBean> getPagerData() {
        return PagerData;
    }

    public void setPagerData(List<AdvertisementBean> pagerData) {
        PagerData = pagerData;
    }

    @Override
    public String toString() {
        return "AdvertisementPagerData{" +
                "Total=" + Total +
                ", PagerData=" + PagerData +
                '}';
    }
}
