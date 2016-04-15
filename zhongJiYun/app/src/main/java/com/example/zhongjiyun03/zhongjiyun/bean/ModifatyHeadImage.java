package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/9.
 */
public class ModifatyHeadImage implements Serializable {

    private String Id;
    private String URL;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    @Override
    public String toString() {
        return "ModifatyHeadImage{" +
                "Id='" + Id + '\'' +
                ", URL='" + URL + '\'' +
                '}';
    }
}
