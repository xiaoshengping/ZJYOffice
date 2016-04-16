package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/4/11.
 */
public class SecondHandListProjectBean implements Serializable {

    private String Address ;//string	详细地址
    private String City	;//string	设备所在城市
    private String Describing	;//string	机主描述
    private DeviceBaseDtoBean DeviceBaseDto	;//object	钻机基础数据对象
    private DeviceDtoBean DeviceDto	;//object	钻机数据对象
    private List<String> DeviceImages	;//array	钻机照片
    private int IsPayMargin	;//int	是否已缴纳保证金
    private boolean IsShowContract;//	bool	是否显示合同照
    private boolean IsShowInvoice;//	bool	是否显示发票照
    private boolean IsShowPrice;//	bool	是否显示价格

    private String PriceStr;//	string	价格
    private String Province;//	string	省份
    private int SecondHandType;//	int	二手机类别（0=出租，1=出售）
    private String SecondHandTypeStr;//	string	二手机类别图片
    private int Status;//	int	二手机状态（0=未审核，1=审核通过，2=审核不通过，3=交易成功，4=交易取消）
    private String StatusStr;//	string	二手机状态图片
    private int Tenancy;//	int	租期
    private String UpdateDateStr;//	string	更新时间
    private int IsCollection;

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

    public String getDescribing() {
        return Describing;
    }

    public void setDescribing(String describing) {
        Describing = describing;
    }

    public DeviceBaseDtoBean getDeviceBaseDto() {
        return DeviceBaseDto;
    }

    public void setDeviceBaseDto(DeviceBaseDtoBean deviceBaseDto) {
        DeviceBaseDto = deviceBaseDto;
    }

    public DeviceDtoBean getDeviceDto() {
        return DeviceDto;
    }

    public void setDeviceDto(DeviceDtoBean deviceDto) {
        DeviceDto = deviceDto;
    }

    public List<String> getDeviceImages() {
        return DeviceImages;
    }

    public void setDeviceImages(List<String> deviceImages) {
        DeviceImages = deviceImages;
    }

    public int getIsPayMargin() {
        return IsPayMargin;
    }

    public void setIsPayMargin(int isPayMargin) {
        IsPayMargin = isPayMargin;
    }

    public boolean isShowContract() {
        return IsShowContract;
    }

    public void setShowContract(boolean showContract) {
        IsShowContract = showContract;
    }

    public boolean isShowInvoice() {
        return IsShowInvoice;
    }

    public void setShowInvoice(boolean showInvoice) {
        IsShowInvoice = showInvoice;
    }

    public boolean isShowPrice() {
        return IsShowPrice;
    }

    public void setShowPrice(boolean showPrice) {
        IsShowPrice = showPrice;
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

    public int getIsCollection() {
        return IsCollection;
    }

    public void setIsCollection(int isCollection) {
        IsCollection = isCollection;
    }

    @Override
    public String toString() {
        return "SecondHandListProjectBean{" +
                "Address='" + Address + '\'' +
                ", City='" + City + '\'' +
                ", Describing='" + Describing + '\'' +
                ", DeviceBaseDto=" + DeviceBaseDto +
                ", DeviceDto=" + DeviceDto +
                ", DeviceImages=" + DeviceImages +
                ", IsPayMargin=" + IsPayMargin +
                ", IsShowContract=" + IsShowContract +
                ", IsShowInvoice=" + IsShowInvoice +
                ", IsShowPrice=" + IsShowPrice +
                ", PriceStr='" + PriceStr + '\'' +
                ", Province='" + Province + '\'' +
                ", SecondHandType=" + SecondHandType +
                ", SecondHandTypeStr='" + SecondHandTypeStr + '\'' +
                ", Status=" + Status +
                ", StatusStr='" + StatusStr + '\'' +
                ", Tenancy=" + Tenancy +
                ", UpdateDateStr='" + UpdateDateStr + '\'' +
                ", IsCollection=" + IsCollection +
                '}';
    }
}
