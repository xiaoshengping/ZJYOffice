package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/4/1.
 */
public class MyExtruderDataBean implements Serializable {

    private List<MyExtruderBean>  PagerData;
    private int Total;
    private boolean HaveNext;

    public List<MyExtruderBean> getPagerData() {
        return PagerData;
    }

    public void setPagerData(List<MyExtruderBean> pagerData) {
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
        return "MyExtruderDataBean{" +
                "PagerData=" + PagerData +
                ", Total=" + Total +
                ", HaveNext=" + HaveNext +
                '}';
    }
}
