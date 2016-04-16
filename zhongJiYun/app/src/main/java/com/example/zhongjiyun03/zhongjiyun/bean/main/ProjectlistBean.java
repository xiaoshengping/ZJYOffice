package com.example.zhongjiyun03.zhongjiyun.bean.main;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/4/11.
 */
public class ProjectlistBean implements Serializable {

             private String Total;
             private List<ProjectlistDataBean> PagerData;

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public List<ProjectlistDataBean> getPagerData() {
        return PagerData;
    }

    public void setPagerData(List<ProjectlistDataBean> pagerData) {
        PagerData = pagerData;
    }

    @Override
    public String toString() {
        return "ProjectlistBean{" +
                "Total='" + Total + '\'' +
                ", PagerData=" + PagerData +
                '}';
    }
}
