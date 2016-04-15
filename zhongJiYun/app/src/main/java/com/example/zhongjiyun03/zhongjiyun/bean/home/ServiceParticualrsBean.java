package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/3/28.
 */
public class ServiceParticualrsBean implements Serializable {

    private String Id; //string 服务商唯一标识
    private String Thumbnail;// string 缩略图url
    private String Name;  // string 姓名
    private String PhoneNumber; // string 手机号码
    private String Tel;   // string 座机号码
    private String ProviderName;  // string 服务商公司名称
    private int ProviderType ;//int 服务商类型
    private String ProviderTypeStr;  // string 服务商类型
    private String  Province  ;//string 省份
    private String City ; //string 城市
    private String Address; // string 详细地址
    private String Summary ;//string 说明

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public String getProviderName() {
        return ProviderName;
    }

    public void setProviderName(String providerName) {
        ProviderName = providerName;
    }

    public int getProviderType() {
        return ProviderType;
    }

    public void setProviderType(int providerType) {
        ProviderType = providerType;
    }

    public String getProviderTypeStr() {
        return ProviderTypeStr;
    }

    public void setProviderTypeStr(String providerTypeStr) {
        ProviderTypeStr = providerTypeStr;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    @Override
    public String toString() {
        return "ServiceParticualrsBean{" +
                "Id='" + Id + '\'' +
                ", Thumbnail='" + Thumbnail + '\'' +
                ", Name='" + Name + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", Tel='" + Tel + '\'' +
                ", ProviderName='" + ProviderName + '\'' +
                ", ProviderType=" + ProviderType +
                ", ProviderTypeStr='" + ProviderTypeStr + '\'' +
                ", Province='" + Province + '\'' +
                ", City='" + City + '\'' +
                ", Address='" + Address + '\'' +
                ", Summary='" + Summary + '\'' +
                '}';
    }
}
