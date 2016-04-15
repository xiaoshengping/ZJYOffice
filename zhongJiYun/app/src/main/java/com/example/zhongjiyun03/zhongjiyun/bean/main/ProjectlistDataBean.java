package com.example.zhongjiyun03.zhongjiyun.bean.main;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/11.
 */
public class ProjectlistDataBean implements Serializable {
             private String Id;
             private String BossId;
             private String BossImg;
             private String BossName;
             private String BossApplauseRate;
             private int BossType;
             private String BossTypeStr;
             private String CreateDateStr;
             private int Status;
             private String StatusStr;
             private String StatusFlag;
             private int StarRate;
             private int PayMarginStatus;
             private String ProjectTitle;
             private String Province;
             private String City;
             private String ProjectCompany;
             private int IsEvaluete;


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getBossId() {
        return BossId;
    }

    public void setBossId(String bossId) {
        BossId = bossId;
    }

    public String getBossImg() {
        return BossImg;
    }

    public void setBossImg(String bossImg) {
        BossImg = bossImg;
    }

    public String getBossName() {
        return BossName;
    }

    public void setBossName(String bossName) {
        BossName = bossName;
    }

    public String getBossApplauseRate() {
        return BossApplauseRate;
    }

    public void setBossApplauseRate(String bossApplauseRate) {
        BossApplauseRate = bossApplauseRate;
    }

    public int getBossType() {
        return BossType;
    }

    public void setBossType(int bossType) {
        BossType = bossType;
    }

    public String getBossTypeStr() {
        return BossTypeStr;
    }

    public void setBossTypeStr(String bossTypeStr) {
        BossTypeStr = bossTypeStr;
    }

    public String getCreateDateStr() {
        return CreateDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        CreateDateStr = createDateStr;
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

    public String getStatusFlag() {
        return StatusFlag;
    }

    public void setStatusFlag(String statusFlag) {
        StatusFlag = statusFlag;
    }

    public int getStarRate() {
        return StarRate;
    }

    public void setStarRate(int starRate) {
        StarRate = starRate;
    }

    public int getPayMarginStatus() {
        return PayMarginStatus;
    }

    public void setPayMarginStatus(int payMarginStatus) {
        PayMarginStatus = payMarginStatus;
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

    public String getProjectCompany() {
        return ProjectCompany;
    }

    public void setProjectCompany(String projectCompany) {
        ProjectCompany = projectCompany;
    }

    public int getIsEvaluete() {
        return IsEvaluete;
    }

    public void setIsEvaluete(int isEvaluete) {
        IsEvaluete = isEvaluete;
    }

    @Override
    public String toString() {
        return "ProjectlistDataBean{" +
                "Id='" + Id + '\'' +
                ", BossId='" + BossId + '\'' +
                ", BossImg='" + BossImg + '\'' +
                ", BossName='" + BossName + '\'' +
                ", BossApplauseRate='" + BossApplauseRate + '\'' +
                ", BossType=" + BossType +
                ", BossTypeStr='" + BossTypeStr + '\'' +
                ", CreateDateStr='" + CreateDateStr + '\'' +
                ", Status=" + Status +
                ", StatusStr='" + StatusStr + '\'' +
                ", StatusFlag='" + StatusFlag + '\'' +
                ", StarRate=" + StarRate +
                ", PayMarginStatus=" + PayMarginStatus +
                ", ProjectTitle='" + ProjectTitle + '\'' +
                ", Province='" + Province + '\'' +
                ", City='" + City + '\'' +
                ", ProjectCompany='" + ProjectCompany + '\'' +
                ", IsEvaluete=" + IsEvaluete +
                '}';
    }
}
