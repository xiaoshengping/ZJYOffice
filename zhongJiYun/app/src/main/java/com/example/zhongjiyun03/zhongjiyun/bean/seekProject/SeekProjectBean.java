package com.example.zhongjiyun03.zhongjiyun.bean.seekProject;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/8.
 */
public class SeekProjectBean implements Serializable  {

    private String Id;//	string	项目ID
    private String Title;//	string	项目标题
    private String Profile;	//string	项目简介
    private String DeviceRequirement;//	string	钻机要求描述
    private String BossStarLevel;	//string	可参与项目的机主星级
    private String Name;	//string	项目联系人姓名
    private String Phone;	//string	项目联系人手机号
    private String Province	;//string	省份
    private String City;//	string	城市
    private String TimeLimit;//	string	工期(月)
    private String GeologicReport;//	string	地质报告、地质状况 图片地址
    private String WorkAmount	;//string	工程量（方/米）
    private String Diameter;//	string	直径
    private String PileDepth;//	string	桩深
    private int ProjectRequirementType;//	int	需要机主类型
    private String ProjectRequirementTypeStr;//	string	需要机主类型
    private String CreateDateStr;//	string	添加时间
    private String Flag;//	string	水印图片
    private String ProjectCompany;//	string	业主公司名称
    private String ProjectOwnerPhone;//	string	业主号码
    private String Status	;//string	项目状态
    private String StatusStr;//	string	项目状态
    private String ReplyStatus;//	string	回复状态（1=已投标，2=已确认，3=已中标，4=已评价）
    private String IsCallOwnerFlag;//	string	是否可以呼叫业主
    private String IsAcceptReply;//	string	是否允许投标
    private int PayMarginStatus;//	int	支付保证金状态,业务逻辑先不管
    private int IsCollection;


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    public String getDeviceRequirement() {
        return DeviceRequirement;
    }

    public void setDeviceRequirement(String deviceRequirement) {
        DeviceRequirement = deviceRequirement;
    }

    public String getBossStarLevel() {
        return BossStarLevel;
    }

    public void setBossStarLevel(String bossStarLevel) {
        BossStarLevel = bossStarLevel;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
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

    public String getTimeLimit() {
        return TimeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        TimeLimit = timeLimit;
    }

    public String getGeologicReport() {
        return GeologicReport;
    }

    public void setGeologicReport(String geologicReport) {
        GeologicReport = geologicReport;
    }

    public String getWorkAmount() {
        return WorkAmount;
    }

    public void setWorkAmount(String workAmount) {
        WorkAmount = workAmount;
    }

    public String getDiameter() {
        return Diameter;
    }

    public void setDiameter(String diameter) {
        Diameter = diameter;
    }

    public String getPileDepth() {
        return PileDepth;
    }

    public void setPileDepth(String pileDepth) {
        PileDepth = pileDepth;
    }

    public int getProjectRequirementType() {
        return ProjectRequirementType;
    }

    public void setProjectRequirementType(int projectRequirementType) {
        ProjectRequirementType = projectRequirementType;
    }

    public String getProjectRequirementTypeStr() {
        return ProjectRequirementTypeStr;
    }

    public void setProjectRequirementTypeStr(String projectRequirementTypeStr) {
        ProjectRequirementTypeStr = projectRequirementTypeStr;
    }

    public String getCreateDateStr() {
        return CreateDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        CreateDateStr = createDateStr;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public String getProjectCompany() {
        return ProjectCompany;
    }

    public void setProjectCompany(String projectCompany) {
        ProjectCompany = projectCompany;
    }

    public String getProjectOwnerPhone() {
        return ProjectOwnerPhone;
    }

    public void setProjectOwnerPhone(String projectOwnerPhone) {
        ProjectOwnerPhone = projectOwnerPhone;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatusStr() {
        return StatusStr;
    }

    public void setStatusStr(String statusStr) {
        StatusStr = statusStr;
    }

    public String getReplyStatus() {
        return ReplyStatus;
    }

    public void setReplyStatus(String replyStatus) {
        ReplyStatus = replyStatus;
    }

    public String getIsCallOwnerFlag() {
        return IsCallOwnerFlag;
    }

    public void setIsCallOwnerFlag(String isCallOwnerFlag) {
        IsCallOwnerFlag = isCallOwnerFlag;
    }

    public String getIsAcceptReply() {
        return IsAcceptReply;
    }

    public void setIsAcceptReply(String isAcceptReply) {
        IsAcceptReply = isAcceptReply;
    }

    public int getPayMarginStatus() {
        return PayMarginStatus;
    }

    public void setPayMarginStatus(int payMarginStatus) {
        PayMarginStatus = payMarginStatus;
    }

    public int getIsCollection() {
        return IsCollection;
    }

    public void setIsCollection(int isCollection) {
        IsCollection = isCollection;
    }

    @Override
    public String toString() {
        return "SeekProjectBean{" +
                "Id='" + Id + '\'' +
                ", Title='" + Title + '\'' +
                ", Profile='" + Profile + '\'' +
                ", DeviceRequirement='" + DeviceRequirement + '\'' +
                ", BossStarLevel='" + BossStarLevel + '\'' +
                ", Name='" + Name + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Province='" + Province + '\'' +
                ", City='" + City + '\'' +
                ", TimeLimit='" + TimeLimit + '\'' +
                ", GeologicReport='" + GeologicReport + '\'' +
                ", WorkAmount='" + WorkAmount + '\'' +
                ", Diameter='" + Diameter + '\'' +
                ", PileDepth='" + PileDepth + '\'' +
                ", ProjectRequirementType=" + ProjectRequirementType +
                ", ProjectRequirementTypeStr='" + ProjectRequirementTypeStr + '\'' +
                ", CreateDateStr='" + CreateDateStr + '\'' +
                ", Flag='" + Flag + '\'' +
                ", ProjectCompany='" + ProjectCompany + '\'' +
                ", ProjectOwnerPhone='" + ProjectOwnerPhone + '\'' +
                ", Status='" + Status + '\'' +
                ", StatusStr='" + StatusStr + '\'' +
                ", ReplyStatus='" + ReplyStatus + '\'' +
                ", IsCallOwnerFlag='" + IsCallOwnerFlag + '\'' +
                ", IsAcceptReply='" + IsAcceptReply + '\'' +
                ", PayMarginStatus=" + PayMarginStatus +
                ", IsCollection=" + IsCollection +
                '}';
    }
}
