package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/6.
 */
public class AdvertisementBean implements Serializable {

    private String Url;
    private String Img;


    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    @Override
    public String toString() {
        return "AdvertisementBean{" +
                "Url='" + Url + '\'' +
                ", Img='" + Img + '\'' +
                '}';
    }
}
