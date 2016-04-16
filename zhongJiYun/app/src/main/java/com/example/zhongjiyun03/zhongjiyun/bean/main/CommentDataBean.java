package com.example.zhongjiyun03.zhongjiyun.bean.main;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/4/16.
 */
public class CommentDataBean implements Serializable {
            private int Total;
            private List<CommentPagerDataBean> PagerData;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public List<CommentPagerDataBean> getPagerData() {
        return PagerData;
    }

    public void setPagerData(List<CommentPagerDataBean> pagerData) {
        PagerData = pagerData;
    }

    @Override
    public String toString() {
        return "CommentDataBean{" +
                "Total=" + Total +
                ", PagerData=" + PagerData +
                '}';
    }
}
