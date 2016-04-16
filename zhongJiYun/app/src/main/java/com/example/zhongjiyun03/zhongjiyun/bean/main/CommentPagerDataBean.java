package com.example.zhongjiyun03.zhongjiyun.bean.main;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/16.
 */
public class CommentPagerDataBean implements Serializable {
             private String Name;
             private String DateTime;
             private String Score;
             private String Content;
             private String Id;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @Override
    public String toString() {
        return "CommentPagerDataBean{" +
                "Name='" + Name + '\'' +
                ", DateTime='" + DateTime + '\'' +
                ", Score='" + Score + '\'' +
                ", Content='" + Content + '\'' +
                ", Id='" + Id + '\'' +
                '}';
    }
}
