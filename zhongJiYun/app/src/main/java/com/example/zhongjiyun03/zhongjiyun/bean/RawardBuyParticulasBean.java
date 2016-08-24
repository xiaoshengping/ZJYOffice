package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/8/24.
 */
public class RawardBuyParticulasBean implements Serializable {
    private String BossName	;//String	购买人姓名
    private String BossPhone	;//String	购买人手机号
    private String Province	;//String	施工所在省份
    private String City	;//String	施工所在城市
    private String Manufacture	;//String	设备厂商
    private String NoOfManufacture;//	String	出厂设备型号
    private String HourOfWork	;//String	工作小时范围
    private String DateOfManufacture;//	String	年限范围
    private String BuyCount	;//String	购买数量
    private String Remark	;//String	求购细节

    private String DateMonthOfManufacture;//	String	出厂月份
    private String DeviceNo;  //设备编号
    private String Price;//	String	价格


    public String getBossName() {
        return BossName;
    }

    public void setBossName(String bossName) {
        BossName = bossName;
    }

    public String getBossPhone() {
        return BossPhone;
    }

    public void setBossPhone(String bossPhone) {
        BossPhone = bossPhone;
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

    public String getHourOfWork() {
        return HourOfWork;
    }

    public void setHourOfWork(String hourOfWork) {
        HourOfWork = hourOfWork;
    }

    public String getDateOfManufacture() {
        return DateOfManufacture;
    }

    public void setDateOfManufacture(String dateOfManufacture) {
        DateOfManufacture = dateOfManufacture;
    }

    public String getBuyCount() {
        return BuyCount;
    }

    public void setBuyCount(String buyCount) {
        BuyCount = buyCount;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }


    public String getDateMonthOfManufacture() {
        return DateMonthOfManufacture;
    }

    public void setDateMonthOfManufacture(String dateMonthOfManufacture) {
        DateMonthOfManufacture = dateMonthOfManufacture;
    }

    public String getDeviceNo() {
        return DeviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        DeviceNo = deviceNo;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    @Override
    public String toString() {
        return "RawardBuyParticulasBean{" +
                "BossName='" + BossName + '\'' +
                ", BossPhone='" + BossPhone + '\'' +
                ", Province='" + Province + '\'' +
                ", City='" + City + '\'' +
                ", Manufacture='" + Manufacture + '\'' +
                ", NoOfManufacture='" + NoOfManufacture + '\'' +
                ", HourOfWork='" + HourOfWork + '\'' +
                ", DateOfManufacture='" + DateOfManufacture + '\'' +
                ", BuyCount='" + BuyCount + '\'' +
                ", Remark='" + Remark + '\'' +
                ", DateMonthOfManufacture='" + DateMonthOfManufacture + '\'' +
                ", DeviceNo='" + DeviceNo + '\'' +
                ", Price='" + Price + '\'' +
                '}';
    }
}
