package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/1.
 */
public class MyExtruderHistoryList implements Serializable {

   private int State;//	int	审核状态：1，通过，2，不通过，0，未审核 ,3, 交易完成,4, 交易取消
   private int  SecondHandType	;//int	1， 出租，0，出售
   private String Id;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public int getSecondHandType() {
        return SecondHandType;
    }

    public void setSecondHandType(int secondHandType) {
        SecondHandType = secondHandType;
    }

    @Override
    public String toString() {
        return "MyExtruderHistoryList{" +
                "State=" + State +
                ", SecondHandType=" + SecondHandType +
                ", Id='" + Id + '\'' +
                '}';
    }
}
