package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/8/24.
 */
public class ResumeListPagerBean  implements Serializable{
    private int Total;
    private List<ResumeListDataBean> PagerData;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public List<ResumeListDataBean> getPagerData() {
        return PagerData;
    }

    public void setPagerData(List<ResumeListDataBean> pagerData) {
        PagerData = pagerData;
    }

    @Override
    public String toString() {
        return "ResumeListPagerBean{" +
                "Total=" + Total +
                ", PagerData=" + PagerData +
                '}';
    }
}
