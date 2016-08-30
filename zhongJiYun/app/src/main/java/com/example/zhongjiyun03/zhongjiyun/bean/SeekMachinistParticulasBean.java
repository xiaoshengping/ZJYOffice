package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/8/25.
 */
public class SeekMachinistParticulasBean implements Serializable {

    private String DriverHeader	;//String	头像
    private String DriverName	;//String	名称
    private String WorkingAge	;//String	工龄
    private String LastUpdateTimeStr	;//String	最后更新时间
    private String Address	;//String	期望工作地址
    private String Wage	;//String	期望月薪
    private String DriverPhoneNumber	;//String	机手手机号码
    private String Age;// 机手年龄
    private List<SeekWorkExperiencesBean> WorkExperiences;//	object	工作经验

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getDriverHeader() {
        return DriverHeader;
    }

    public void setDriverHeader(String driverHeader) {
        DriverHeader = driverHeader;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public String getWorkingAge() {
        return WorkingAge;
    }

    public void setWorkingAge(String workingAge) {
        WorkingAge = workingAge;
    }

    public String getLastUpdateTimeStr() {
        return LastUpdateTimeStr;
    }

    public void setLastUpdateTimeStr(String lastUpdateTimeStr) {
        LastUpdateTimeStr = lastUpdateTimeStr;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getWage() {
        return Wage;
    }

    public void setWage(String wage) {
        Wage = wage;
    }

    public String getDriverPhoneNumber() {
        return DriverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        DriverPhoneNumber = driverPhoneNumber;
    }

    public List<SeekWorkExperiencesBean> getWorkExperiences() {
        return WorkExperiences;
    }

    public void setWorkExperiences(List<SeekWorkExperiencesBean> workExperiences) {
        WorkExperiences = workExperiences;
    }

    @Override
    public String toString() {
        return "SeekMachinistParticulasBean{" +
                "DriverHeader='" + DriverHeader + '\'' +
                ", DriverName='" + DriverName + '\'' +
                ", WorkingAge='" + WorkingAge + '\'' +
                ", LastUpdateTimeStr='" + LastUpdateTimeStr + '\'' +
                ", Address='" + Address + '\'' +
                ", Wage='" + Wage + '\'' +
                ", DriverPhoneNumber='" + DriverPhoneNumber + '\'' +
                ", Age='" + Age + '\'' +
                ", WorkExperiences=" + WorkExperiences +
                '}';
    }
}
