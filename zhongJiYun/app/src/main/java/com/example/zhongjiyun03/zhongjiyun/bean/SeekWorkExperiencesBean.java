package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/8/25.
 */
public class SeekWorkExperiencesBean implements Serializable {

    private String BeginYear	;//String	开始年份
    private String BeginMonth	;//String	开始月份
    private String EndYear	;//String	结束年份
    private String EndMonth	;//String	结束月份
    private String Manufacture	;//String	厂商
    private String NoOfManufacture	;//String	型号
    private String Describing	;//String	工作描述

    public String getBeginYear() {
        return BeginYear;
    }

    public void setBeginYear(String beginYear) {
        BeginYear = beginYear;
    }

    public String getBeginMonth() {
        return BeginMonth;
    }

    public void setBeginMonth(String beginMonth) {
        BeginMonth = beginMonth;
    }

    public String getEndYear() {
        return EndYear;
    }

    public void setEndYear(String endYear) {
        EndYear = endYear;
    }

    public String getEndMonth() {
        return EndMonth;
    }

    public void setEndMonth(String endMonth) {
        EndMonth = endMonth;
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

    public String getDescribing() {
        return Describing;
    }

    public void setDescribing(String describing) {
        Describing = describing;
    }

    @Override
    public String toString() {
        return "SeekWorkExperiencesBean{" +
                "BeginYear='" + BeginYear + '\'' +
                ", BeginMonth='" + BeginMonth + '\'' +
                ", EndYear='" + EndYear + '\'' +
                ", EndMonth='" + EndMonth + '\'' +
                ", Manufacture='" + Manufacture + '\'' +
                ", NoOfManufacture='" + NoOfManufacture + '\'' +
                ", Describing='" + Describing + '\'' +
                '}';
    }
}
