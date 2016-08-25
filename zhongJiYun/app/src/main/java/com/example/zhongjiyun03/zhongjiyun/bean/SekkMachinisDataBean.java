package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/7.
 */
public class SekkMachinisDataBean implements Serializable {

    private  String DriverId	;//String	机手ID
    private  String DriverHeader	;//String	机手头像
    private  String DriverName	;//String	机手名称
    private  String Address	;//String	期望地址
    private  String Wage	;//String	期望月薪
    private  String WorkingAge	;//String	工龄
    private  String DeviceNames	;//String	钻机厂商型号名称列表字符串
    private  String Distance	;//String	距离
    private  String LastUpdateTime	;//String	更新时间

    public String getDriverId() {
        return DriverId;
    }

    public void setDriverId(String driverId) {
        DriverId = driverId;
    }

    public String getDriverHeader() {
        return DriverHeader;
    }

    public void setDriverHeader(String driverHeader) {
        DriverHeader = driverHeader;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getWage() {
        return Wage;
    }

    public void setWage(String wage) {
        Wage = wage;
    }

    public String getWorkingAge() {
        return WorkingAge;
    }

    public void setWorkingAge(String workingAge) {
        WorkingAge = workingAge;
    }

    public String getDeviceNames() {
        return DeviceNames;
    }

    public void setDeviceNames(String deviceNames) {
        DeviceNames = deviceNames;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getLastUpdateTime() {
        return LastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        LastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return "SekkMachinisDataBean{" +
                "DriverId='" + DriverId + '\'' +
                ", DriverHeader='" + DriverHeader + '\'' +
                ", DriverName='" + DriverName + '\'' +
                ", Address='" + Address + '\'' +
                ", Wage='" + Wage + '\'' +
                ", WorkingAge='" + WorkingAge + '\'' +
                ", DeviceNames='" + DeviceNames + '\'' +
                ", Distance='" + Distance + '\'' +
                ", LastUpdateTime='" + LastUpdateTime + '\'' +
                '}';
    }
}
