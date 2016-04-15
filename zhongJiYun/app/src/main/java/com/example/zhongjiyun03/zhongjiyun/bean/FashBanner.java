package com.example.zhongjiyun03.zhongjiyun.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * @Package com.qianfeng.zw.meilishuo.bean
 * @作 用:首页-流行-广告栏
 * @创 建 人: zhangwei
 * @日 期: 15/4/4 17:25
 * @修 改 人:
 * @日 期:
 */


public class FashBanner implements Serializable {
    @JSONField(name = "activity_id")
    private String activityId;
    @JSONField(name = "image_url")
    private String imageUrl;
    private String title;
    @JSONField(name = "image_height")
    private String imageHeight;
    @JSONField(name = "image_width")
    private String imageWidth;
    private String url;
    private String sort;
    
    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
