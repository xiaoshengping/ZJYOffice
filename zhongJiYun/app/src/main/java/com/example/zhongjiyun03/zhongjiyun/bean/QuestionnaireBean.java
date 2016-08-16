package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/8/16.
 */
public class QuestionnaireBean implements Serializable {

    private String Count;//	String	数量


    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    @Override
    public String toString() {
        return "QuestionnaireBean{" +
                "Count='" + Count + '\'' +
                '}';
    }
}
