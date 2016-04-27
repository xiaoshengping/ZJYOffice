package com.example.zhongjiyun03.zhongjiyun.bean.main;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/3/30.
 */
public class RePackedListBean implements Serializable {

   private String  Id;// string 礼包唯一标识
   private String  Key; // string 礼包Key
   private String Title; // string 礼包名称
   private String  Content; // string 礼包描述
   private String  BeginTime ;//string 开始时间
   private String  EndTime; // string 结束时间
   private String   CloudMoney;// int 云币数量
   private String  Mulriple; //  int 云币数量倍数
   private int  StarLevel ; // int 礼包星级
   private int GiftBagType ; //int 礼包类型：云币：1，优惠券：！=1
   private int  isGet; //是否领取
   private int IsRead;//是否已读

    public int getIsRead() {
        return IsRead;
    }

    public void setIsRead(int isRead) {
        IsRead = isRead;
    }

    public int getIsGet() {
        return isGet;
    }

    public void setIsGet(int isGet) {
        this.isGet = isGet;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getCloudMoney() {
        return CloudMoney;
    }

    public void setCloudMoney(String cloudMoney) {
        CloudMoney = cloudMoney;
    }

    public String getMulriple() {
        return Mulriple;
    }

    public void setMulriple(String mulriple) {
        Mulriple = mulriple;
    }

    public int getStarLevel() {
        return StarLevel;
    }

    public void setStarLevel(int starLevel) {
        StarLevel = starLevel;
    }

    public int getGiftBagType() {
        return GiftBagType;
    }

    public void setGiftBagType(int giftBagType) {
        GiftBagType = giftBagType;
    }

    @Override
    public String toString() {
        return "RePackedListBean{" +
                "Id='" + Id + '\'' +
                ", Key='" + Key + '\'' +
                ", Title='" + Title + '\'' +
                ", Content='" + Content + '\'' +
                ", BeginTime='" + BeginTime + '\'' +
                ", EndTime='" + EndTime + '\'' +
                ", CloudMoney='" + CloudMoney + '\'' +
                ", Mulriple='" + Mulriple + '\'' +
                ", StarLevel=" + StarLevel +
                ", GiftBagType=" + GiftBagType +
                ", isGet=" + isGet +
                '}';
    }
}
