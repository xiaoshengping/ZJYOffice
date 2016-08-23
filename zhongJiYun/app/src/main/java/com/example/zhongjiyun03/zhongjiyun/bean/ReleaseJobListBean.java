package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/8/23.
 */
public class ReleaseJobListBean implements Serializable  {

    private String Id;//	String	需求Id
    private String Manufacture;//		String	钻机厂商
    private String NoOfManufacture;//		String	钻机型号
    private String PayLevel;//		String	月薪水平
    private String WorkingAge;//		String	工作年限
    private String Count;//		String	投递人数
    private String Province;//		String	工作省份
    private String City;//		String	工作城市
    private String CreateDateStr;//		String	创建时间
    private String StatusStr;//		String	审核状态 1,通过, 2,不通过, 0,未审核

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

    public String getPayLevel() {
        return PayLevel;
    }

    public void setPayLevel(String payLevel) {
        PayLevel = payLevel;
    }

    public String getWorkingAge() {
        return WorkingAge;
    }

    public void setWorkingAge(String workingAge) {
        WorkingAge = workingAge;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
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

    public String getCreateDateStr() {
        return CreateDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        CreateDateStr = createDateStr;
    }

    public String getStatusStr() {
        return StatusStr;
    }

    public void setStatusStr(String statusStr) {
        StatusStr = statusStr;
    }

    @Override
    public String toString() {
        return "ReleaseJobListBean{" +
                "Id='" + Id + '\'' +
                ", Manufacture='" + Manufacture + '\'' +
                ", NoOfManufacture='" + NoOfManufacture + '\'' +
                ", PayLevel='" + PayLevel + '\'' +
                ", WorkingAge='" + WorkingAge + '\'' +
                ", Count='" + Count + '\'' +
                ", Province='" + Province + '\'' +
                ", City='" + City + '\'' +
                ", CreateDateStr='" + CreateDateStr + '\'' +
                ", StatusStr='" + StatusStr + '\'' +
                '}';
    }
}
