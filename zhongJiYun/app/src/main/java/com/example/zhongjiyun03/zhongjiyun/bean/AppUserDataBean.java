package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/3/30.
 */
public class AppUserDataBean<T> implements Serializable {


    private T User;


    public T getUser() {
        return User;
    }

    public void setUser(T user) {
        User = user;
    }

    @Override
    public String toString() {
        return "AppUserDataBean{" +
                "User=" + User +
                '}';
    }
}
