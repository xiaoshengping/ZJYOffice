package com.example.zhongjiyun03.zhongjiyun.bean.select;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZHONGJIYUN03 on 2016/4/8.
 */
public class ProvinceCityBean  implements Serializable{

      private String Id;
      private String Name;
      private int FatherNo;
      private int Number;
      private ArrayList<ProvinceCityChildsBean> ProvinceCityChilds;

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

    public int getFatherNo() {
        return FatherNo;
    }

    public void setFatherNo(int fatherNo) {
        FatherNo = fatherNo;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public ArrayList<ProvinceCityChildsBean> getProvinceCityChilds() {
        return ProvinceCityChilds;
    }

    public void setProvinceCityChilds(ArrayList<ProvinceCityChildsBean> provinceCityChilds) {
        ProvinceCityChilds = provinceCityChilds;
    }

    @Override
    public String toString() {
        return "ProvinceCityBean{" +
                "Id='" + Id + '\'' +
                ", Name='" + Name + '\'' +
                ", FatherNo=" + FatherNo +
                ", Number=" + Number +
                ", ProvinceCityChilds=" + ProvinceCityChilds +
                '}';
    }
}
