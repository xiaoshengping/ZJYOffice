package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/8/16.
 */
public class QuestionnaireListBean implements Serializable  {

    private String Url	;//String	问卷列表url（web直接访问）
    private String Title	;//String	问卷标题
    private String CloudMoney	;//String	获得云币
    private String ExpireDateTime	;//String	过期时间

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCloudMoney() {
        return CloudMoney;
    }

    public void setCloudMoney(String cloudMoney) {
        CloudMoney = cloudMoney;
    }

    public String getExpireDateTime() {
        return ExpireDateTime;
    }

    public void setExpireDateTime(String expireDateTime) {
        ExpireDateTime = expireDateTime;
    }

    @Override
    public String toString() {
        return "QuestionnaireListBean{" +
                "Url='" + Url + '\'' +
                ", Title='" + Title + '\'' +
                ", CloudMoney='" + CloudMoney + '\'' +
                ", ExpireDateTime='" + ExpireDateTime + '\'' +
                '}';
    }
}
