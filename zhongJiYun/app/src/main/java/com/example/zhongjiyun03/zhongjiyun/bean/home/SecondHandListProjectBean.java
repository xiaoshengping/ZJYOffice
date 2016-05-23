package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;
import java.util.ArrayList;
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
    private int IsShowContract;//	bool	是否显示合同照
    private int IsShowInvoice;//	bool	是否显示发票照
    private boolean IsShowPrice;//	bool	是否显示价格
    private String BossPhoneNumber; //手机号码

    private String PriceStr;//	string	价格
    private String Province;//	string	省份
    private int SecondHandType;//	int	二手机类别（0=出租，1=出售）
    private String SecondHandTypeStr;//	string	二手机类别图片
    private int Status;//	int	二手机状态（0=未审核，1=审核通过，2=审核不通过，3=交易成功，4=交易取消）
    private String StatusStr;//	string	二手机状态图片
    private int Tenancy;//	int	租期
    private String UpdateDateStr;//	string	更新时间
    private int IsCollection;
    private String Price;//string	价格
    private String Image1Id; //图片1ID
    private String Image2Id; //图片2ID
    private String Image3Id; //图片3ID
    private String Image4Id; //图片4ID
    private String Image5Id; //图片5ID
    private String Image1; //图片1
    private String Image2; //图片2
    private String Image3; //图片3
    private String Image4; //图片4
    private String Image5; //图片5

    public String getBossPhoneNumber() {
        return BossPhoneNumber;
    }

    public void setBossPhoneNumber(String bossPhoneNumber) {
        BossPhoneNumber = bossPhoneNumber;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage1() {
        return Image1;
    }

    public void setImage1(String image1) {
        Image1 = image1;
    }

    public String getImage2() {
        return Image2;
    }

    public void setImage2(String image2) {
        Image2 = image2;
    }

    public String getImage3() {
        return Image3;
    }

    public void setImage3(String image3) {
        Image3 = image3;
    }

    public String getImage4() {
        return Image4;
    }

    public void setImage4(String image4) {
        Image4 = image4;
    }

    public String getImage5() {
        return Image5;
    }

    public void setImage5(String image5) {
        Image5 = image5;
    }

    public String getImage1Id() {
        return Image1Id;
    }

    public void setImage1Id(String image1Id) {
        Image1Id = image1Id;
    }

    public String getImage5Id() {
        return Image5Id;
    }

    public void setImage5Id(String image5Id) {
        Image5Id = image5Id;
    }

    public String getImage3Id() {
        return Image3Id;
    }

    public void setImage3Id(String image3Id) {
        Image3Id = image3Id;
    }

    public String getImage2Id() {
        return Image2Id;
    }

    public void setImage2Id(String image2Id) {
        Image2Id = image2Id;
    }

    public String getImage4Id() {
        return Image4Id;
    }

    public void setImage4Id(String image4Id) {
        Image4Id = image4Id;
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
       List<String> imgs=new ArrayList<>();
        for (int i = 0; i < DeviceImages.size(); i++) {
            if(DeviceImages.get(i)!=null){
                imgs.add(DeviceImages.get(i));
            }
        }
        return imgs;
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

    public int getIsShowContract() {
        return IsShowContract;
    }

    public void setIsShowContract(int isShowContract) {
        IsShowContract = isShowContract;
    }

    public int getIsShowInvoice() {
        return IsShowInvoice;
    }

    public void setIsShowInvoice(int isShowInvoice) {
        IsShowInvoice = isShowInvoice;
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
                ", Price='" + Price + '\'' +
                ", Image1Id='" + Image1Id + '\'' +
                ", Image2Id='" + Image2Id + '\'' +
                ", Image3Id='" + Image3Id + '\'' +
                ", Image4Id='" + Image4Id + '\'' +
                ", Image5Id='" + Image5Id + '\'' +
                ", Image1='" + Image1 + '\'' +
                ", Image2='" + Image2 + '\'' +
                ", Image3='" + Image3 + '\'' +
                ", Image4='" + Image4 + '\'' +
                ", Image5='" + Image5 + '\'' +
                '}';
    }
}
