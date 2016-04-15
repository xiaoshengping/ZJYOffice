package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/7.
 */
public class WorkInfoItemDtosBean implements Serializable {

    private String Id;
    private String BeginYear;
    private String BeginMonth;
    private String EndYear;
    private String EndMonth;
    private String Describing;
    private String Manufacture;
    private String NoOfManufacture;
    private String DeviceType;
    private String NoOfManufactures;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getBeginYear() {
        return BeginYear;
    }

    public void setBeginYear(String beginYear) {
        BeginYear = beginYear;
    }

    public String getBeginMonth() {
        return BeginMonth;
    }

    public void setBeginMonth(String beginMonth) {
        BeginMonth = beginMonth;
    }

    public String getEndYear() {
        return EndYear;
    }

    public void setEndYear(String endYear) {
        EndYear = endYear;
    }

    public String getEndMonth() {
        return EndMonth;
    }

    public void setEndMonth(String endMonth) {
        EndMonth = endMonth;
    }

    public String getDescribing() {
        return Describing;
    }

    public void setDescribing(String describing) {
        Describing = describing;
    }

    public String getManufacture() {
        return Manufacture;
    }

    public void setManufacture(String manufacture) {
        Manufacture = manufacture;
    }

    public String getNoOfManufacture() {
        return NoOfManufacture;
    }

    public void setNoOfManufacture(String noOfManufacture) {
        NoOfManufacture = noOfManufacture;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }

    public String getNoOfManufactures() {
        return NoOfManufactures;
    }

    public void setNoOfManufactures(String noOfManufactures) {
        NoOfManufactures = noOfManufactures;
    }

    @Override
    public String toString() {
        return "WorkInfoItemDtosBean{" +
                "Id='" + Id + '\'' +
                ", BeginYear='" + BeginYear + '\'' +
                ", BeginMonth='" + BeginMonth + '\'' +
                ", EndYear='" + EndYear + '\'' +
                ", EndMonth='" + EndMonth + '\'' +
                ", Describing='" + Describing + '\'' +
                ", Manufacture='" + Manufacture + '\'' +
                ", NoOfManufacture='" + NoOfManufacture + '\'' +
                ", DeviceType='" + DeviceType + '\'' +
                ", NoOfManufactures='" + NoOfManufactures + '\'' +
                '}';
    }
}
