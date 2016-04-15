package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/4/6.
 */
public class DeviceBaseDtoBean  implements Serializable{

    private String ChassisMode;//	string	底盘型号
    private List<DeviceImagesBean> DeviceImages	;//array	钻机基础照片
    private String DrillingSpeed;//	string	钻孔转速
    private String EnginePowerRating;//	string	发动机额定功率
    private String EngineType;//	string	发动机型号
    private String Id;//	string	二手机ID
    private String MainHoistingForce;//	string	主卷场提升力
    private String Manufacture;//	string	设备厂商
    private String MaxHoleDepth;//	string	最大成孔深度
    private String MaxHoleDiameter;//	string	最大成孔直径
    private String MaxTorque;//	string	最大扭矩
    private String NoOfManufacture	;//string	出厂设备型号
    private String ProductIntroduction;//	string	产品介绍
    private String WebUrl	;//string	产品官网链接
    private String WorkingWeight;//	string	整机工作重量
    private String DeviceBasePhone;

    public String getDeviceBasePhone() {
        return DeviceBasePhone;
    }

    public void setDeviceBasePhone(String deviceBasePhone) {
        DeviceBasePhone = deviceBasePhone;
    }

    public String getChassisMode() {
        return ChassisMode;
    }

    public void setChassisMode(String chassisMode) {
        ChassisMode = chassisMode;
    }

    public List<DeviceImagesBean> getDeviceImages() {
        return DeviceImages;
    }

    public void setDeviceImages(List<DeviceImagesBean> deviceImages) {
        DeviceImages = deviceImages;
    }

    public String getDrillingSpeed() {
        return DrillingSpeed;
    }

    public void setDrillingSpeed(String drillingSpeed) {
        DrillingSpeed = drillingSpeed;
    }

    public String getEnginePowerRating() {
        return EnginePowerRating;
    }

    public void setEnginePowerRating(String enginePowerRating) {
        EnginePowerRating = enginePowerRating;
    }

    public String getEngineType() {
        return EngineType;
    }

    public void setEngineType(String engineType) {
        EngineType = engineType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMainHoistingForce() {
        return MainHoistingForce;
    }

    public void setMainHoistingForce(String mainHoistingForce) {
        MainHoistingForce = mainHoistingForce;
    }

    public String getManufacture() {
        return Manufacture;
    }

    public void setManufacture(String manufacture) {
        Manufacture = manufacture;
    }

    public String getMaxHoleDepth() {
        return MaxHoleDepth;
    }

    public void setMaxHoleDepth(String maxHoleDepth) {
        MaxHoleDepth = maxHoleDepth;
    }

    public String getMaxHoleDiameter() {
        return MaxHoleDiameter;
    }

    public void setMaxHoleDiameter(String maxHoleDiameter) {
        MaxHoleDiameter = maxHoleDiameter;
    }

    public String getMaxTorque() {
        return MaxTorque;
    }

    public void setMaxTorque(String maxTorque) {
        MaxTorque = maxTorque;
    }

    public String getNoOfManufacture() {
        return NoOfManufacture;
    }

    public void setNoOfManufacture(String noOfManufacture) {
        NoOfManufacture = noOfManufacture;
    }

    public String getProductIntroduction() {
        return ProductIntroduction;
    }

    public void setProductIntroduction(String productIntroduction) {
        ProductIntroduction = productIntroduction;
    }

    public String getWebUrl() {
        return WebUrl;
    }

    public void setWebUrl(String webUrl) {
        WebUrl = webUrl;
    }

    public String getWorkingWeight() {
        return WorkingWeight;
    }

    public void setWorkingWeight(String workingWeight) {
        WorkingWeight = workingWeight;
    }

    @Override
    public String toString() {
        return "DeviceBaseDtoBean{" +
                "ChassisMode='" + ChassisMode + '\'' +
                ", DeviceImages=" + DeviceImages +
                ", DrillingSpeed='" + DrillingSpeed + '\'' +
                ", EnginePowerRating='" + EnginePowerRating + '\'' +
                ", EngineType='" + EngineType + '\'' +
                ", Id='" + Id + '\'' +
                ", MainHoistingForce='" + MainHoistingForce + '\'' +
                ", Manufacture='" + Manufacture + '\'' +
                ", MaxHoleDepth='" + MaxHoleDepth + '\'' +
                ", MaxHoleDiameter='" + MaxHoleDiameter + '\'' +
                ", MaxTorque='" + MaxTorque + '\'' +
                ", NoOfManufacture='" + NoOfManufacture + '\'' +
                ", ProductIntroduction='" + ProductIntroduction + '\'' +
                ", WebUrl='" + WebUrl + '\'' +
                ", WorkingWeight='" + WorkingWeight + '\'' +
                ", DeviceBasePhone='" + DeviceBasePhone + '\'' +
                '}';
    }
}
