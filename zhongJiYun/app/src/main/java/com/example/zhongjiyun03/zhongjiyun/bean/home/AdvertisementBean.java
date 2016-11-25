package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/6.
 */
public class AdvertisementBean implements Serializable {

    private String Url;
    private String ImageUrl;
    private String Title;
    private String LinkType;


    public String getLinkType() {
        return LinkType;
    }

    public void setLinkType(String linkType) {
        LinkType = linkType;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    @Override
    public String toString() {
        return "AdvertisementBean{" +
                "Url='" + Url + '\'' +
                ", ImageUrl='" + ImageUrl + '\'' +
                ", Title='" + Title + '\'' +
                ", LinkType='" + LinkType + '\'' +
                '}';
    }
}
