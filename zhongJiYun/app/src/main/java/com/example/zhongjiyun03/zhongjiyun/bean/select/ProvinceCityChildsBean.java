package com.example.zhongjiyun03.zhongjiyun.bean.select;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/8.
 */
public class ProvinceCityChildsBean implements Serializable {
             private String Id;
             private String Name;
             private String FatherNo;
             private String Number;

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

    public String getFatherNo() {
        return FatherNo;
    }

    public void setFatherNo(String fatherNo) {
        FatherNo = fatherNo;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    @Override
    public String toString() {
        return "ProvinceCityChildsBean{" +
                "Id='" + Id + '\'' +
                ", Name='" + Name + '\'' +
                ", FatherNo='" + FatherNo + '\'' +
                ", Number='" + Number + '\'' +
                '}';
    }
}
