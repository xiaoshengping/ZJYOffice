package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/11/7.
 */

public class WebViewBean implements Serializable {

    private String CoinCount;

    public String getCoinCount() {
        return CoinCount;
    }

    public void setCoinCount(String coinCount) {
        CoinCount = coinCount;
    }

    @Override
    public String toString() {
        return "WebViewBean{" +
                "CoinCount='" + CoinCount + '\'' +
                '}';
    }
}
