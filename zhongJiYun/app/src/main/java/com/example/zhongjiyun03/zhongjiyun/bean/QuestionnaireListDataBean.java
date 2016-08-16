package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/8/16.
 */
public class QuestionnaireListDataBean implements Serializable  {
        private int Total;
        private List<QuestionnaireListBean> PagerData;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public List<QuestionnaireListBean> getPagerData() {
        return PagerData;
    }

    public void setPagerData(List<QuestionnaireListBean> pagerData) {
        PagerData = pagerData;
    }

    @Override
    public String toString() {
        return "QuestionnaireListDataBean{" +
                "Total=" + Total +
                ", PagerData=" + PagerData +
                '}';
    }
}
