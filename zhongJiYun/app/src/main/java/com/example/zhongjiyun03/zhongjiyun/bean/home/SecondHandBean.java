package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/6.
 */
public class SecondHandBean implements Serializable {

    private String Address ;//string	详细地址
    private String City	;//string	设备所在城市
    private DeviceDtoBean DeviceDto;//	object	钻机数据对象
    private String Distance;//	double	二手机与机主的距离（单位：KM）
    private String DistanceStr;//	string	二手机与机主的距离（当未传ID时，该参数返回null）
    private String  Id;//	string	二手机ID
    private int IsPayMargin;//	int	是否已缴纳保证金
    private int IsShowPrice;//	int	是否显示价格
    private String PriceStr;//	string	价格
    private String Price;//	string	价格
    private String Province;//	string	省份
    private int SecondHandType;//	int	二手机类别（0=出租，1=出售）
    private String SecondHandTypeStr;//	string	二手机类别图片
    private int Status;	//int	二手机状态（0=未审核，1=审核通过，2=审核不通过，3=交易成功，4=交易取消）
    private String StatusStr;	//string	二手机状态图片
    private int Tenancy;	//int	租期
    private String UpdateDateStr;	//string	更新时间
    private String DevicePhoto;  //	string	设备全景照片
    private String Manufacture;  //	string	钻机厂商
    private String NoOfManufacture;//	string	钻机型号
    private String DateOfManufacture;
    private String HourOfWork;


    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
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

    public String getDevicePhoto() {
        return DevicePhoto;
    }

    public void setDevicePhoto(String devicePhoto) {
        DevicePhoto = devicePhoto;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public DeviceDtoBean getDeviceDto() {
        return DeviceDto;
    }

    public void setDeviceDto(DeviceDtoBean deviceDto) {
        DeviceDto = deviceDto;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getDistanceStr() {
        return DistanceStr;
    }

    public void setDistanceStr(String distanceStr) {
        DistanceStr = distanceStr;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getIsPayMargin() {
        return IsPayMargin;
    }

    public void setIsPayMargin(int isPayMargin) {
        IsPayMargin = isPayMargin;
    }

    public int getIsShowPrice() {
        return IsShowPrice;
    }

    public void setIsShowPrice(int isShowPrice) {
        IsShowPrice = isShowPrice;
    }

    public String getPriceStr() {
        return PriceStr;
    }

    public void setPriceStr(String priceStr) {
        PriceStr = priceStr;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public int getSecondHandType() {
        return SecondHandType;
    }

    public void setSecondHandType(int secondHandType) {
        SecondHandType = secondHandType;
    }

    public String getSecondHandTypeStr() {
        return SecondHandTypeStr;
    }

    public void setSecondHandTypeStr(String secondHandTypeStr) {
        SecondHandTypeStr = secondHandTypeStr;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getStatusStr() {
        return StatusStr;
    }

    public void setStatusStr(String statusStr) {
        StatusStr = statusStr;
    }

    public int getTenancy() {
        return Tenancy;
    }

    public void setTenancy(int tenancy) {
        Tenancy = tenancy;
    }

    public String getUpdateDateStr() {
        return UpdateDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        UpdateDateStr = updateDateStr;
    }

    @Override
    public String toString() {
        return "SecondHandBean{" +
                "Address='" + Address + '\'' +
                ", City='" + City + '\'' +
                ", DeviceDto=" + DeviceDto +
                ", Distance=" + Distance +
                ", DistanceStr='" + DistanceStr + '\'' +
                ", Id='" + Id + '\'' +
                ", IsPayMargin=" + IsPayMargin +
                ", IsShowPrice=" + IsShowPrice +
                ", PriceStr='" + PriceStr + '\'' +
                ", Price='" + Price + '\'' +
                ", Province='" + Province + '\'' +
                ", SecondHandType=" + SecondHandType +
                ", SecondHandTypeStr='" + SecondHandTypeStr + '\'' +
                ", Status=" + Status +
                ", StatusStr='" + StatusStr + '\'' +
                ", Tenancy=" + Tenancy +
                ", UpdateDateStr='" + UpdateDateStr + '\'' +
                ", DevicePhoto='" + DevicePhoto + '\'' +
                ", Manufacture='" + Manufacture + '\'' +
                ", NoOfManufacture='" + NoOfManufacture + '\'' +
                ", DateOfManufacture='" + DateOfManufacture + '\'' +
                ", HourOfWork='" + HourOfWork + '\'' +
                '}';
    }
}
