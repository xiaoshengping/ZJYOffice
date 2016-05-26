package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/6.
 */
public class AdvertisementBean implements Serializable {

    private String Url;
    private String Img;
    private String Name;
    private int LinkType;

    public int getLinkType() {
        return LinkType;
    }

    public void setLinkType(int linkType) {
        LinkType = linkType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

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
                ", Name='" + Name + '\'' +
                ", LinkType=" + LinkType +
                '}';
    }
}
