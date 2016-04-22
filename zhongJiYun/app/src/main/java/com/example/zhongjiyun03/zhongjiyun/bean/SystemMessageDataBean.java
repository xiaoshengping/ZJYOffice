package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/22.
 */
public class SystemMessageDataBean implements Serializable {
    private int Evaluate;//	Int	我的评价数
    private int Message	;//Int	消息数
    private int GiftBag	;//Int	我的红包数
    private int ProjectReply;//	Int	我的竞标数
    private int TotalCount;//	Int	全部消息总和

    public int getEvaluate() {
        return Evaluate;
    }

    public void setEvaluate(int evaluate) {
        Evaluate = evaluate;
    }

    public int getMessage() {
        return Message;
    }

    public void setMessage(int message) {
        Message = message;
    }

    public int getGiftBag() {
        return GiftBag;
    }

    public void setGiftBag(int giftBag) {
        GiftBag = giftBag;
    }

    public int getProjectReply() {
        return ProjectReply;
    }

    public void setProjectReply(int projectReply) {
        ProjectReply = projectReply;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }

    @Override
    public String toString() {
        return "SystemMessageDataBean{" +
                "Evaluate=" + Evaluate +
                ", Message=" + Message +
                ", GiftBag=" + GiftBag +
                ", ProjectReply=" + ProjectReply +
                ", TotalCount=" + TotalCount +
                '}';
    }
}
