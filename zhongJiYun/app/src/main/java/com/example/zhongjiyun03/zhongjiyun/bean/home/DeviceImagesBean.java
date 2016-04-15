package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/6.
 */
public class DeviceImagesBean  implements Serializable{

        private String Url;

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    @Override
    public String toString() {
        return "DeviceImagesBean{" +
                "Url='" + Url + '\'' +
                '}';
    }
}
