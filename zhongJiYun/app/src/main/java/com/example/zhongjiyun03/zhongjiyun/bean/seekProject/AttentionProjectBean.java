package com.example.zhongjiyun03.zhongjiyun.bean.seekProject;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/8.
 */
public class AttentionProjectBean implements Serializable {

    private String Id;//	string	项目ID
    private String Title;//	string	项目标题
    private String Profile;	//string	项目简介
    private String DeviceRequirement;//	string	钻机要求描述
    private int BossStarLevel;	//string	可参与项目的机主星级
    private String Name;	//string	项目联系人姓名
    private String Phone;	//string	项目联系人手机号
    private String Province	;//string	省份
    private String City;//	string	城市
    private int TimeLimit;//	string	工期(月)
    private String GeologicReport;//	string	地质报告、地质状况 图片地址
    private String WorkAmount	;//string	工程量（方/米）
    private String Diameter;//	string	直径
    private String PileDepth;//	string	桩深
    private int ProjectRequirementType;//	int	需要机主类型
    private String ProjectRequirementTypeStr;//	string	需要机主类型
    private int AuditStutas;
    private String CreateDateStr;//	string	添加时间
    private String Flag;//	string	水印图片
    private String ProjectCompany;//	string	业主公司名称
    private String ProjectOwnerPhone;//	string	业主号码
    private int Status	;//string	项目状态
    private String StatusStr;//	string	项目状态
    private int ReplyStatus;//	string	回复状态（1=已投标，2=已确认，3=已中标，4=已评价）
    private boolean IsCallOwnerFlag;//	string	是否可以呼叫业主
    private int IsAcceptReply;//	string	是否允许投标
    private int PayMarginStatus;//	int	支付保证金状态,业务逻辑先不管

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

    public int getBossStarLevel() {
        return BossStarLevel;
    }

    public void setBossStarLevel(int bossStarLevel) {
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

    public int getTimeLimit() {
        return TimeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        TimeLimit = timeLimit;
    }

    public String getGeologicReport() {
        return GeologicReport;
    }

    public void setGeologicReport(String geologicReport) {
        GeologicReport = geologicReport;
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

    public int getAuditStutas() {
        return AuditStutas;
    }

    public void setAuditStutas(int auditStutas) {
        AuditStutas = auditStutas;
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

    public int getReplyStatus() {
        return ReplyStatus;
    }

    public void setReplyStatus(int replyStatus) {
        ReplyStatus = replyStatus;
    }

    public boolean isCallOwnerFlag() {
        return IsCallOwnerFlag;
    }

    public void setCallOwnerFlag(boolean callOwnerFlag) {
        IsCallOwnerFlag = callOwnerFlag;
    }

    public String getDiameter() {
        return Diameter;
    }

    public void setDiameter(String diameter) {
        Diameter = diameter;
    }

    public String getWorkAmount() {
        return WorkAmount;
    }

    public void setWorkAmount(String workAmount) {
        WorkAmount = workAmount;
    }

    public String getPileDepth() {
        return PileDepth;
    }

    public void setPileDepth(String pileDepth) {
        PileDepth = pileDepth;
    }

    public int getIsAcceptReply() {
        return IsAcceptReply;
    }

    public void setIsAcceptReply(int isAcceptReply) {
        IsAcceptReply = isAcceptReply;
    }

    public int getPayMarginStatus() {
        return PayMarginStatus;
    }

    public void setPayMarginStatus(int payMarginStatus) {
        PayMarginStatus = payMarginStatus;
    }

    @Override
    public String toString() {
        return "AttentionProjectBean{" +
                "Id='" + Id + '\'' +
                ", Title='" + Title + '\'' +
                ", Profile='" + Profile + '\'' +
                ", DeviceRequirement='" + DeviceRequirement + '\'' +
                ", BossStarLevel=" + BossStarLevel +
                ", Name='" + Name + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Province='" + Province + '\'' +
                ", City='" + City + '\'' +
                ", TimeLimit=" + TimeLimit +
                ", GeologicReport='" + GeologicReport + '\'' +
                ", WorkAmount='" + WorkAmount + '\'' +
                ", Diameter='" + Diameter + '\'' +
                ", PileDepth='" + PileDepth + '\'' +
                ", ProjectRequirementType=" + ProjectRequirementType +
                ", ProjectRequirementTypeStr='" + ProjectRequirementTypeStr + '\'' +
                ", AuditStutas=" + AuditStutas +
                ", CreateDateStr='" + CreateDateStr + '\'' +
                ", Flag='" + Flag + '\'' +
                ", ProjectCompany='" + ProjectCompany + '\'' +
                ", ProjectOwnerPhone='" + ProjectOwnerPhone + '\'' +
                ", Status=" + Status +
                ", StatusStr='" + StatusStr + '\'' +
                ", ReplyStatus=" + ReplyStatus +
                ", IsCallOwnerFlag=" + IsCallOwnerFlag +
                ", IsAcceptReply=" + IsAcceptReply +
                ", PayMarginStatus=" + PayMarginStatus +
                '}';
    }
}
