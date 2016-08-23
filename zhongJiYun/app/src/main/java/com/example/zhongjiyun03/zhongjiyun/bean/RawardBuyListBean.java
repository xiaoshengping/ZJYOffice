package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/8/23.
 */
public class RawardBuyListBean implements Serializable {


    private  String Id	;//String	求购信息ID
    private  String BossName;//	String	购买人姓名
    private  String Province;//	String	施工所在省份
    private  String City;//	String	施工所在城市
    private  String HourOfWork;//	String	工作小时范围
    private  String DateOfManufacture;//	String	年限范围
    private  String AuditStutasStr;//	String	审核状态 1,通过, 2,不通过, 0,未审核
    private  String CreateDateStr;//	String	发布时间


   private String  Manufacture	;//String	设备厂商
   private String  NoOfManufacture	;//String	出厂设备型号
    private String Price	;//String	价格
    private String DateMonthOfManufacture;//	String	出厂月份
    private String StatusStr;//审核标记

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getBossName() {
        return BossName;
    }

    public void setBossName(String bossName) {
        BossName = bossName;
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

    public String getAuditStutasStr() {
        return AuditStutasStr;
    }

    public void setAuditStutasStr(String auditStutasStr) {
        AuditStutasStr = auditStutasStr;
    }

    public String getCreateDateStr() {
        return CreateDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        CreateDateStr = createDateStr;
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

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDateMonthOfManufacture() {
        return DateMonthOfManufacture;
    }

    public void setDateMonthOfManufacture(String dateMonthOfManufacture) {
        DateMonthOfManufacture = dateMonthOfManufacture;
    }

    public String getStatusStr() {
        return StatusStr;
    }

    public void setStatusStr(String statusStr) {
        StatusStr = statusStr;
    }

    @Override
    public String toString() {
        return "RawardBuyListBean{" +
                "Id='" + Id + '\'' +
                ", BossName='" + BossName + '\'' +
                ", Province='" + Province + '\'' +
                ", City='" + City + '\'' +
                ", HourOfWork='" + HourOfWork + '\'' +
                ", DateOfManufacture='" + DateOfManufacture + '\'' +
                ", AuditStutasStr='" + AuditStutasStr + '\'' +
                ", CreateDateStr='" + CreateDateStr + '\'' +
                ", Manufacture='" + Manufacture + '\'' +
                ", NoOfManufacture='" + NoOfManufacture + '\'' +
                ", Price='" + Price + '\'' +
                ", DateMonthOfManufacture='" + DateMonthOfManufacture + '\'' +
                ", StatusStr='" + StatusStr + '\'' +
                '}';
    }
}
