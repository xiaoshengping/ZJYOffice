package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/6/28.
 */
public class PhotoDataBean implements Serializable {

         private String Id; //图片id

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @Override
    public String toString() {
        return "PhotoDataBean{" +
                "Id='" + Id + '\'' +
                '}';
    }
}
