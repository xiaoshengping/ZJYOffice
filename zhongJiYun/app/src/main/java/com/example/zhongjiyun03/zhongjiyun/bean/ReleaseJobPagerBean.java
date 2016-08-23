package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/8/23.
 */
public class ReleaseJobPagerBean implements Serializable {

    private int Total; //zongshu
    private List<ReleaseJobListBean> PagerData;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public List<ReleaseJobListBean> getPagerData() {
        return PagerData;
    }

    public void setPagerData(List<ReleaseJobListBean> pagerData) {
        PagerData = pagerData;
    }

    @Override
    public String toString() {
        return "ReleaseJobPagerBean{" +
                "Total=" + Total +
                ", PagerData=" + PagerData +
                '}';
    }
}
