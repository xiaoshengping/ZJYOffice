package com.example.zhongjiyun03.zhongjiyun.bean.main;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/3/30.
 */
public class LoginDataBean implements Serializable {

    private String Id ;//string 唯一标识
    private String PhoneNumber;// string 手机号
    private String Name ;//string 姓名
    private int StarRate ;//int 等级
    private String Headthumb ;// string 头像
    private String Role ;// string boss机主，owner业主，driver机手

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getStarRate() {
        return StarRate;
    }

    public void setStarRate(int starRate) {
        StarRate = starRate;
    }

    public String getHeadthumb() {
        return Headthumb;
    }

    public void setHeadthumb(String headthumb) {
        Headthumb = headthumb;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    @Override
    public String toString() {
        return "LoginDataBean{" +
                "Id='" + Id + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", Name='" + Name + '\'' +
                ", StarRate=" + StarRate +
                ", Headthumb='" + Headthumb + '\'' +
                ", Role='" + Role + '\'' +
                '}';
    }
}
