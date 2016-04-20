package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/20.
 */
public class MyRedPackedDataBean implements Serializable {

      private int TotalCloudMoney;
      private String GetCloudMoney;


    public int getTotalCloudMoney() {
        return TotalCloudMoney;
    }

    public void setTotalCloudMoney(int totalCloudMoney) {
        TotalCloudMoney = totalCloudMoney;
    }

    public String getGetCloudMoney() {
        return GetCloudMoney;
    }

    public void setGetCloudMoney(String getCloudMoney) {
        GetCloudMoney = getCloudMoney;
    }

    @Override
    public String toString() {
        return "MyRedPackedDataBean{" +
                "TotalCloudMoney=" + TotalCloudMoney +
                ", GetCloudMoney='" + GetCloudMoney + '\'' +
                '}';
    }
}
