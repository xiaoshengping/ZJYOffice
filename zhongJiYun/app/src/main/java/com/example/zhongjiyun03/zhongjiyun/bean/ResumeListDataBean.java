package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/8/24.
 */
public class ResumeListDataBean implements Serializable {
    private String Id	;//String	找工作明细ID
    private String DriverId	;//String	机手ID
    private String DriverHeader	;//String	机手头像
    private String DriverName	;//String	机手名称
    private String Wage	;//String	期望月薪
    private String WorkingAge;//	String	工龄
    private String DeviceNames;//	String	钻机厂商型号名称列表字符串
    private String CreateDateStr;//	String	投递时间

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

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

    public String getCreateDateStr() {
        return CreateDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        CreateDateStr = createDateStr;
    }

    @Override
    public String toString() {
        return "ResumeListDataBean{" +
                "Id='" + Id + '\'' +
                ", DriverId='" + DriverId + '\'' +
                ", DriverHeader='" + DriverHeader + '\'' +
                ", DriverName='" + DriverName + '\'' +
                ", Wage='" + Wage + '\'' +
                ", WorkingAge='" + WorkingAge + '\'' +
                ", DeviceNames='" + DeviceNames + '\'' +
                ", CreateDateStr='" + CreateDateStr + '\'' +
                '}';
    }
}
