package com.example.zhongjiyun03.zhongjiyun.bean.select;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/4/9.
 */
public class FacillyChildsBean implements Serializable {

             private String Text;
             private String Value;

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    @Override
    public String toString() {
        return "FacillyChildsBean{" +
                "Text='" + Text + '\'' +
                ", Value='" + Value + '\'' +
                '}';
    }
}
