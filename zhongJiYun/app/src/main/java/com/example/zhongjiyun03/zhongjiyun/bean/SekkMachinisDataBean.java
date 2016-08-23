package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/4/7.
 */
public class SekkMachinisDataBean implements Serializable {

    private String City	;//string;//	市
    private double Distance	;//double	与机主的距离（km）
    private String DistanceStr;//	string	与机主的距离（km）
    private String DriverHeader;//	string	机手头像
    private String DriverId	;//string	机手ID
    private String DriverName	;//string	机手姓名
    private String Id;//	string	找工作ID
    private String LastUpdateTimeStr;//	string	最后更新时间
    private String LastUpdateTimeSubStr;//	string	最后更新时间（月-天）
    private String Lat;//	string	纬度
    private String Lng	;//string	经度
    private String Province;//	string	省
    private int Wage;//	int	期望月薪
    private List<WorkInfoItemDtosBean> WorkInfoItemDtos;//	object	工作经验
    private String WorkInfoStatusStr;//	string	找工作状态（1=正在找工作，2=已找到工作，3=失效）
    private String WorkingAge;//	string	工龄
    private String DriverPhoneNumber;
    private String DeviceNames; //钻机型号字符串

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public double getDistance() {
        return Distance;
    }

    public void setDistance(double distance) {
        Distance = distance;
    }

    public String getDistanceStr() {
        return DistanceStr;
    }

    public void setDistanceStr(String distanceStr) {
        DistanceStr = distanceStr;
    }

    public String getDriverHeader() {
        return DriverHeader;
    }

    public void setDriverHeader(String driverHeader) {
        DriverHeader = driverHeader;
    }

    public String getDriverId() {
        return DriverId;
    }

    public void setDriverId(String driverId) {
        DriverId = driverId;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getLastUpdateTimeStr() {
        return LastUpdateTimeStr;
    }

    public void setLastUpdateTimeStr(String lastUpdateTimeStr) {
        LastUpdateTimeStr = lastUpdateTimeStr;
    }

    public String getLastUpdateTimeSubStr() {
        return LastUpdateTimeSubStr;
    }

    public void setLastUpdateTimeSubStr(String lastUpdateTimeSubStr) {
        LastUpdateTimeSubStr = lastUpdateTimeSubStr;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLng() {
        return Lng;
    }

    public void setLng(String lng) {
        Lng = lng;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public int getWage() {
        return Wage;
    }

    public void setWage(int wage) {
        Wage = wage;
    }

    public List<WorkInfoItemDtosBean> getWorkInfoItemDtos() {
        return WorkInfoItemDtos;
    }

    public void setWorkInfoItemDtos(List<WorkInfoItemDtosBean> workInfoItemDtos) {
        WorkInfoItemDtos = workInfoItemDtos;
    }

    public String getWorkInfoStatusStr() {
        return WorkInfoStatusStr;
    }

    public void setWorkInfoStatusStr(String workInfoStatusStr) {
        WorkInfoStatusStr = workInfoStatusStr;
    }

    public String getWorkingAge() {
        return WorkingAge;
    }

    public void setWorkingAge(String workingAge) {
        WorkingAge = workingAge;
    }

    public String getDriverPhoneNumber() {
        return DriverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        DriverPhoneNumber = driverPhoneNumber;
    }

    public String getDeviceNames() {
        return DeviceNames;
    }

    public void setDeviceNames(String deviceNames) {
        DeviceNames = deviceNames;
    }

    @Override
    public String toString() {
        return "SekkMachinisDataBean{" +
                "City='" + City + '\'' +
                ", Distance=" + Distance +
                ", DistanceStr='" + DistanceStr + '\'' +
                ", DriverHeader='" + DriverHeader + '\'' +
                ", DriverId='" + DriverId + '\'' +
                ", DriverName='" + DriverName + '\'' +
                ", Id='" + Id + '\'' +
                ", LastUpdateTimeStr='" + LastUpdateTimeStr + '\'' +
                ", LastUpdateTimeSubStr='" + LastUpdateTimeSubStr + '\'' +
                ", Lat='" + Lat + '\'' +
                ", Lng='" + Lng + '\'' +
                ", Province='" + Province + '\'' +
                ", Wage=" + Wage +
                ", WorkInfoItemDtos=" + WorkInfoItemDtos +
                ", WorkInfoStatusStr='" + WorkInfoStatusStr + '\'' +
                ", WorkingAge='" + WorkingAge + '\'' +
                ", DriverPhoneNumber='" + DriverPhoneNumber + '\'' +
                ", DeviceNames='" + DeviceNames + '\'' +
                '}';
    }
}
