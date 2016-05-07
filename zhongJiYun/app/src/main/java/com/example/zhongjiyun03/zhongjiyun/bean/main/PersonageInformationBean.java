package com.example.zhongjiyun03.zhongjiyun.bean.main;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/3/31.
 */
public class PersonageInformationBean implements Serializable {

    private String Id ;// string 机主ID
    private String PhoneNumber; // string 手机号
    private String Name; // string 姓名
    private String IdCard ;//string 身份证号码
    private String Province ;// string 省份
    private String City ;//string 地区
    private String ReferralPhone; // string 推荐人手机号
    private String IdCardImage1; // string 身份证正面
    private String IdCardImage2 ; //string 身份证反面
    private String Photo ; //string 本人照片
    private String Summary ;// string 简介
    private String Headthumb ;//string 头像
    private int BossType ;//int 个人：1，企业：2
    private String Role ;//string boss机主，owner业主，driver机手
    private String StarRate;

    public String getStarRate() {
        return StarRate;
    }

    public void setStarRate(String starRate) {
        StarRate = starRate;
    }

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

    public String getIdCard() {
        return IdCard;
    }

    public void setIdCard(String idCard) {
        IdCard = idCard;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getReferralPhone() {
        return ReferralPhone;
    }

    public void setReferralPhone(String referralPhone) {
        ReferralPhone = referralPhone;
    }

    public String getIdCardImage1() {
        return IdCardImage1;
    }

    public void setIdCardImage1(String idCardImage1) {
        IdCardImage1 = idCardImage1;
    }

    public String getIdCardImage2() {
        return IdCardImage2;
    }

    public void setIdCardImage2(String idCardImage2) {
        IdCardImage2 = idCardImage2;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public String getHeadthumb() {
        return Headthumb;
    }

    public void setHeadthumb(String headthumb) {
        Headthumb = headthumb;
    }

    public int getBossType() {
        return BossType;
    }

    public void setBossType(int bossType) {
        BossType = bossType;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    @Override
    public String toString() {
        return "PersonageInformationBean{" +
                "Id='" + Id + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", Name='" + Name + '\'' +
                ", IdCard='" + IdCard + '\'' +
                ", Province='" + Province + '\'' +
                ", City='" + City + '\'' +
                ", ReferralPhone='" + ReferralPhone + '\'' +
                ", IdCardImage1='" + IdCardImage1 + '\'' +
                ", IdCardImage2='" + IdCardImage2 + '\'' +
                ", Photo='" + Photo + '\'' +
                ", Summary='" + Summary + '\'' +
                ", Headthumb='" + Headthumb + '\'' +
                ", BossType=" + BossType +
                ", Role='" + Role + '\'' +
                '}';
    }
}
