package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/3/31.
 */
public class RegisterBean implements Serializable {

    private String Id;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @Override
    public String toString() {
        return "registerBean{" +
                "Id='" + Id + '\'' +
                '}';
    }


}
