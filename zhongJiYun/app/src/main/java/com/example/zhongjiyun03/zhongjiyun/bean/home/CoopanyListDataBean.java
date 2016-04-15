package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/3/29.
 */
public class CoopanyListDataBean implements Serializable {


   private String Id; // string 合作伙伴唯一标识
   private String  Name;  // string 姓名
   private String  Content ;//string 说明
   private String  Logo ; //string Logo
   private String  Url; // string 合作客户网站链接

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    @Override
    public String toString() {
        return "CoopanyListDataBean{" +
                "Id='" + Id + '\'' +
                ", Name='" + Name + '\'' +
                ", Content='" + Content + '\'' +
                ", Logo='" + Logo + '\'' +
                ", Url='" + Url + '\'' +
                '}';
    }
}
