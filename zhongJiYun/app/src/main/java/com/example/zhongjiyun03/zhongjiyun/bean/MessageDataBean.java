package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/19.
 */
public class MessageDataBean implements Serializable {
         private String Id;
         private String Title;
         private String BeginDate;

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

    public String getBeginDate() {
        return BeginDate;
    }

    public void setBeginDate(String beginDate) {
        BeginDate = beginDate;
    }

    @Override
    public String toString() {
        return "MessageDataBean{" +
                "Id='" + Id + '\'' +
                ", Title='" + Title + '\'' +
                ", BeginDate='" + BeginDate + '\'' +
                '}';
    }
}
