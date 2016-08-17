package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/3/28.
 */
public class ServiceProviderBean implements Serializable {

    private String id;
    private String Thumbnail;
    private String Name;       //姓名
    private String ProviderName;  //服务商公司名字
    private int ProviderType;    //服务商类型
    private String ProviderTypeStr;  //  服务商类型
    private String  Distance; //  距离：KM
    private String Summary;  //备注说明
    private String Province;//省份
    private String City;  //城市

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String  distance) {
        Distance = distance;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
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

    @Override
    public String toString() {
        return "ServiceProviderBean{" +
                "id='" + id + '\'' +
                ", Thumbnail='" + Thumbnail + '\'' +
                ", Name='" + Name + '\'' +
                ", ProviderName='" + ProviderName + '\'' +
                ", ProviderType=" + ProviderType +
                ", ProviderTypeStr='" + ProviderTypeStr + '\'' +
                ", Distance='" + Distance + '\'' +
                ", Summary='" + Summary + '\'' +
                ", Province='" + Province + '\'' +
                ", City='" + City + '\'' +
                '}';
    }
}
