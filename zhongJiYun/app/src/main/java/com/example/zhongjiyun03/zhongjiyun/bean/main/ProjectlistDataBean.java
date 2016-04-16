package com.example.zhongjiyun03.zhongjiyun.bean.main;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/11.
 */
public class ProjectlistDataBean implements Serializable {
             private String ProjectId;
             private String ProjectTitle;
             private String Province;
             private String City;
             private String CreateDateStr;
             private String ProjectCompany;
             private int Status;
             private int IsEvaluete;
             private int PayMarginStatus;

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String projectId) {
        ProjectId = projectId;
    }

    public String getProjectTitle() {
        return ProjectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        ProjectTitle = projectTitle;
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

    public String getProjectCompany() {
        return ProjectCompany;
    }

    public void setProjectCompany(String projectCompany) {
        ProjectCompany = projectCompany;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getIsEvaluete() {
        return IsEvaluete;
    }

    public void setIsEvaluete(int isEvaluete) {
        IsEvaluete = isEvaluete;
    }

    public int getPayMarginStatus() {
        return PayMarginStatus;
    }

    public void setPayMarginStatus(int payMarginStatus) {
        PayMarginStatus = payMarginStatus;
    }

    @Override
    public String toString() {
        return "ProjectlistDataBean{" +
                "ProjectId='" + ProjectId + '\'' +
                ", ProjectTitle='" + ProjectTitle + '\'' +
                ", Province='" + Province + '\'' +
                ", City='" + City + '\'' +
                ", CreateDateStr='" + CreateDateStr + '\'' +
                ", ProjectCompany='" + ProjectCompany + '\'' +
                ", Status=" + Status +
                ", IsEvaluete=" + IsEvaluete +
                ", PayMarginStatus=" + PayMarginStatus +
                '}';
    }
}
