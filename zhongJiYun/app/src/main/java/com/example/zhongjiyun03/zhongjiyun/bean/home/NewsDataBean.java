package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/7.
 */
public class NewsDataBean implements Serializable {

    private String CreateDateStr;//	string	创建时间
    private String FrontCover;//	string	新闻图片
    private String Id;//	string	新闻ID
    private int NewsType	;//int	新闻类型
    private String NewsTypeStr;//	string	新闻类型
    private String SubTitle;//	string	新闻副标题
    private String Title;//	string	新闻标题


    public String getCreateDateStr() {
        return CreateDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        CreateDateStr = createDateStr;
    }

    public String getFrontCover() {
        return FrontCover;
    }

    public void setFrontCover(String frontCover) {
        FrontCover = frontCover;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getNewsType() {
        return NewsType;
    }

    public void setNewsType(int newsType) {
        NewsType = newsType;
    }

    public String getNewsTypeStr() {
        return NewsTypeStr;
    }

    public void setNewsTypeStr(String newsTypeStr) {
        NewsTypeStr = newsTypeStr;
    }

    public String getSubTitle() {
        return SubTitle;
    }

    public void setSubTitle(String subTitle) {
        SubTitle = subTitle;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    @Override
    public String toString() {
        return "NewsDataBean{" +
                "CreateDateStr='" + CreateDateStr + '\'' +
                ", FrontCover='" + FrontCover + '\'' +
                ", Id='" + Id + '\'' +
                ", NewsType=" + NewsType +
                ", NewsTypeStr='" + NewsTypeStr + '\'' +
                ", SubTitle='" + SubTitle + '\'' +
                ", Title='" + Title + '\'' +
                '}';
    }
}
