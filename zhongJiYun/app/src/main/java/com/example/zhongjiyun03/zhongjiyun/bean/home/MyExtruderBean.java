package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/1.
 */
public class MyExtruderBean implements Serializable {
    private String Id;//	string	钻机唯一标识
    private String Thumbnail; //	string	缩略图url
    private String DeviceNo	;//string	设备编号
    private String Manufacture ;//	string	厂商
    private String NoOfManufacture	;//string	型号
    private int HourOfWork;//	int	设备工作小时
    private String DateOfManufacture;//	string	出厂年份
    private String DateMonthOfManufacture;//	string	出厂月份
    private String Province	;//string	设备所在省份
    private String City	;//string	设备所在城市
    private String DeviceNoPhoto;//	string	设备出厂牌图片
    private String DevicePhoto;//	string	设备全景照片
    private String DeviceInvoicePhoto;//	string	设备发票照片
    private String DeviceContractPhoto	;//string	设备合同照片
    private String DeviceCertificatePhoto	;//string	设备合格证照片
    private int AuditStutas	;//int	审核状态：1，通过，2，不通过，0，未审核
    private int SecondHandState;
    private int SecondHandType;
    private String SecondHandId;
    private String PayMarginStatus;//是否交保证金

    public String getPayMarginStatus() {
        return PayMarginStatus;
    }

    public void setPayMarginStatus(String payMarginStatus) {
        PayMarginStatus = payMarginStatus;
    }

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

    public String getDeviceNo() {
        return DeviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        DeviceNo = deviceNo;
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

    public int getHourOfWork() {
        return HourOfWork;
    }

    public void setHourOfWork(int hourOfWork) {
        HourOfWork = hourOfWork;
    }

    public String getDateOfManufacture() {
        return DateOfManufacture;
    }

    public void setDateOfManufacture(String dateOfManufacture) {
        DateOfManufacture = dateOfManufacture;
    }

    public String getDateMonthOfManufacture() {
        return DateMonthOfManufacture;
    }

    public void setDateMonthOfManufacture(String dateMonthOfManufacture) {
        DateMonthOfManufacture = dateMonthOfManufacture;
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

    public String getDeviceNoPhoto() {
        return DeviceNoPhoto;
    }

    public void setDeviceNoPhoto(String deviceNoPhoto) {
        DeviceNoPhoto = deviceNoPhoto;
    }

    public String getDevicePhoto() {
        return DevicePhoto;
    }

    public void setDevicePhoto(String devicePhoto) {
        DevicePhoto = devicePhoto;
    }

    public String getDeviceInvoicePhoto() {
        return DeviceInvoicePhoto;
    }

    public void setDeviceInvoicePhoto(String deviceInvoicePhoto) {
        DeviceInvoicePhoto = deviceInvoicePhoto;
    }

    public String getDeviceContractPhoto() {
        return DeviceContractPhoto;
    }

    public void setDeviceContractPhoto(String deviceContractPhoto) {
        DeviceContractPhoto = deviceContractPhoto;
    }

    public String getDeviceCertificatePhoto() {
        return DeviceCertificatePhoto;
    }

    public void setDeviceCertificatePhoto(String deviceCertificatePhoto) {
        DeviceCertificatePhoto = deviceCertificatePhoto;
    }

    public int getAuditStutas() {
        return AuditStutas;
    }

    public void setAuditStutas(int auditStutas) {
        AuditStutas = auditStutas;
    }



    public String getSecondHandId() {
        return SecondHandId;
    }

    public void setSecondHandId(String secondHandId) {
        SecondHandId = secondHandId;
    }

    public int getSecondHandState() {
        return SecondHandState;
    }

    public void setSecondHandState(int secondHandState) {
        SecondHandState = secondHandState;
    }

    public int getSecondHandType() {
        return SecondHandType;
    }

    public void setSecondHandType(int secondHandType) {
        SecondHandType = secondHandType;
    }

    @Override
    public String toString() {
        return "MyExtruderBean{" +
                "Id='" + Id + '\'' +
                ", Thumbnail='" + Thumbnail + '\'' +
                ", DeviceNo='" + DeviceNo + '\'' +
                ", Manufacture='" + Manufacture + '\'' +
                ", NoOfManufacture='" + NoOfManufacture + '\'' +
                ", HourOfWork=" + HourOfWork +
                ", DateOfManufacture='" + DateOfManufacture + '\'' +
                ", DateMonthOfManufacture='" + DateMonthOfManufacture + '\'' +
                ", Province='" + Province + '\'' +
                ", City='" + City + '\'' +
                ", DeviceNoPhoto='" + DeviceNoPhoto + '\'' +
                ", DevicePhoto='" + DevicePhoto + '\'' +
                ", DeviceInvoicePhoto='" + DeviceInvoicePhoto + '\'' +
                ", DeviceContractPhoto='" + DeviceContractPhoto + '\'' +
                ", DeviceCertificatePhoto='" + DeviceCertificatePhoto + '\'' +
                ", AuditStutas=" + AuditStutas +
                ", SecondHandState='" + SecondHandState + '\'' +
                ", SecondHandType='" + SecondHandType + '\'' +
                ", SecondHandId='" + SecondHandId + '\'' +
                '}';
    }
}
