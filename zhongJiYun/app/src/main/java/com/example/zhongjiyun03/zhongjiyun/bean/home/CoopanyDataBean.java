package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/29.
 */
public class CoopanyDataBean implements Serializable {

       private List<CoopanyListDataBean> CooperativeBusinessList;


    public List<CoopanyListDataBean> getCooperativeBusinessList() {
        return CooperativeBusinessList;
    }

    public void setCooperativeBusinessList(List<CoopanyListDataBean> cooperativeBusinessList) {
        CooperativeBusinessList = cooperativeBusinessList;
    }
}
