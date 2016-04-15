package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/28.
 */
public class ServiceDataBean implements Serializable {

    private List<ServiceProviderBean> PagerData;
    private ServiceParticualrsBean User;
    private int Total;
    private boolean HaveNext;

    public List<ServiceProviderBean> getPagerData() {
        return PagerData;
    }

    public void setPagerData(List<ServiceProviderBean> pagerData) {
        PagerData = pagerData;
    }

    public ServiceParticualrsBean getUser() {
        return User;
    }

    public void setUser(ServiceParticualrsBean user) {
        User = user;
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
        return "ServiceDataBean{" +
                "PagerData=" + PagerData +
                ", User=" + User +
                ", Total=" + Total +
                ", HaveNext=" + HaveNext +
                '}';
    }
}
