package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/6.
 */
public class DeviceDtoBean implements Serializable {
    private String BossHeadthumb;//	string	机主头像
    private String BossName	;//string	机主姓名
    private String City;//	string	城市
    private String DateMonthOfManufacture;//	string	出厂月份
    private String DateOfManufacture;//	string	出厂年份
    private String DeviceCertificatePhoto;//	string	设备合格证照片
    private String DeviceContractPhoto;//	string	设备合同照片
    private String DeviceInvoicePhoto;//	string	设备发票照片
    private String DeviceNo;//	string	钻机编号
    private String DeviceNoPhoto;//	string	设备编号照片
    private String DevicePhoto;//	string	设备全景照片
    private String Flag	;//string	状态图片
    private String HourOfWork;//	string	工作时间
    private String Id;//	string	钻机Id
    private String Manufacture;//	string	钻机厂商
    private String NoOfManufacture;//	string	钻机型号
    private String Province;//	string	省份

    public String getBossHeadthumb() {
        return BossHeadthumb;
    }

    public void setBossHeadthumb(String bossHeadthumb) {
        BossHeadthumb = bossHeadthumb;
    }

    public String getBossName() {
        return BossName;
    }

    public void setBossName(String bossName) {
        BossName = bossName;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getDateMonthOfManufacture() {
        return DateMonthOfManufacture;
    }

    public void setDateMonthOfManufacture(String dateMonthOfManufacture) {
        DateMonthOfManufacture = dateMonthOfManufacture;
    }

    public String getDateOfManufacture() {
        return DateOfManufacture;
    }

    public void setDateOfManufacture(String dateOfManufacture) {
        DateOfManufacture = dateOfManufacture;
    }

    public String getDeviceCertificatePhoto() {
        return DeviceCertificatePhoto;
    }

    public void setDeviceCertificatePhoto(String deviceCertificatePhoto) {
        DeviceCertificatePhoto = deviceCertificatePhoto;
    }

    public String getDeviceContractPhoto() {
        return DeviceContractPhoto;
    }

    public void setDeviceContractPhoto(String deviceContractPhoto) {
        DeviceContractPhoto = deviceContractPhoto;
    }

    public String getDeviceInvoicePhoto() {
        return DeviceInvoicePhoto;
    }

    public void setDeviceInvoicePhoto(String deviceInvoicePhoto) {
        DeviceInvoicePhoto = deviceInvoicePhoto;
    }

    public String getDeviceNo() {
        return DeviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        DeviceNo = deviceNo;
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

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public String getHourOfWork() {
        return HourOfWork;
    }

    public void setHourOfWork(String hourOfWork) {
        HourOfWork = hourOfWork;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    @Override
    public String toString() {
        return "DeviceDtoBean{" +
                "BossHeadthumb='" + BossHeadthumb + '\'' +
                ", BossName='" + BossName + '\'' +
                ", City='" + City + '\'' +
                ", DateMonthOfManufacture='" + DateMonthOfManufacture + '\'' +
                ", DateOfManufacture='" + DateOfManufacture + '\'' +
                ", DeviceCertificatePhoto='" + DeviceCertificatePhoto + '\'' +
                ", DeviceContractPhoto='" + DeviceContractPhoto + '\'' +
                ", DeviceInvoicePhoto='" + DeviceInvoicePhoto + '\'' +
                ", DeviceNo='" + DeviceNo + '\'' +
                ", DeviceNoPhoto='" + DeviceNoPhoto + '\'' +
                ", DevicePhoto='" + DevicePhoto + '\'' +
                ", Flag='" + Flag + '\'' +
                ", HourOfWork='" + HourOfWork + '\'' +
                ", Id='" + Id + '\'' +
                ", Manufacture='" + Manufacture + '\'' +
                ", NoOfManufacture='" + NoOfManufacture + '\'' +
                ", Province='" + Province + '\'' +
                '}';
    }
}
