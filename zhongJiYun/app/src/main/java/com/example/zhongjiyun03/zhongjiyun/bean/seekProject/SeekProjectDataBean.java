package com.example.zhongjiyun03.zhongjiyun.bean.seekProject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/4/8.
 */
public class SeekProjectDataBean implements Serializable {

    private List<SeekProjectBean> PagerData;
    private int Total;
    private boolean HaveNext;

    public List<SeekProjectBean> getPagerData() {
        return PagerData;
    }

    public void setPagerData(List<SeekProjectBean> pagerData) {
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
        return "SeekProjectDataBean{" +
                "PagerData=" + PagerData +
                ", Total=" + Total +
                ", HaveNext=" + HaveNext +
                '}';
    }
}
