package com.example.zhongjiyun03.zhongjiyun.bean.main;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/30.
 */
public class RedPacketDataBean implements Serializable {

    private List<RePackedListBean> PagerData;
    private int Total;
    private boolean HaveNext;

    public List<RePackedListBean> getPagerData() {
        return PagerData;
    }

    public void setPagerData(List<RePackedListBean> pagerData) {
        PagerData = pagerData;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public boolean isHaveNext() {
        return HaveNext;
    }

    public void setHaveNext(boolean haveNext) {
        HaveNext = haveNext;
    }

    @Override
    public String toString() {
        return "RedPacketDataBean{" +
                "PagerData=" + PagerData +
                ", Total=" + Total +
                ", HaveNext=" + HaveNext +
                '}';
    }
}
