package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/2.
 */
public class ExtruderDeviceBean implements Serializable {

    private String Id;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @Override
    public String toString() {
        return "ExtruderDeviceBean{" +
                "Id='" + Id + '\'' +
                '}';
    }
}
