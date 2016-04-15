package com.example.zhongjiyun03.zhongjiyun.bean.select;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/4/8.
 */
public class ProvinceCityDataBean implements Serializable {
           private List<ProvinceCityBean>  ProvinceCity;

    public List<ProvinceCityBean> getProvinceCity() {
        return ProvinceCity;
    }

    public void setProvinceCity(List<ProvinceCityBean> provinceCity) {
        ProvinceCity = provinceCity;
    }

    @Override
    public String toString() {
        return "ProvinceCityDataBean{" +
                "ProvinceCity=" + ProvinceCity +
                '}';
    }
}
