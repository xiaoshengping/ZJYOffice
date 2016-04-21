package com.example.zhongjiyun03.zhongjiyun.bean;

import com.example.zhongjiyun03.zhongjiyun.bean.seekProject.AttentionProjectBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/4/21.
 */
public class AttentionProjectDataBean implements Serializable {
             private int Total;
             private List<AttentionProjectBean> PagerData;

    public List<AttentionProjectBean> getPagerData() {
        return PagerData;
    }

    public void setPagerData(List<AttentionProjectBean> pagerData) {
        PagerData = pagerData;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    @Override
    public String toString() {
        return "AttentionProjectDataBean{" +
                "Total=" + Total +
                ", PagerData=" + PagerData +
                '}';
    }
}
