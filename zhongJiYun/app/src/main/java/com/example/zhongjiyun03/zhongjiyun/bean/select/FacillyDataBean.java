package com.example.zhongjiyun03.zhongjiyun.bean.select;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/4/9.
 */
public class FacillyDataBean implements Serializable {
           private String Text;
            private String Value;
            private List<FacillyChildsBean> Childs;

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

    public List<FacillyChildsBean> getChilds() {
        return Childs;
    }

    public void setChilds(List<FacillyChildsBean> childs) {
        Childs = childs;
    }

    @Override
    public String toString() {
        return "FacillyDataBean{" +
                "Text='" + Text + '\'' +
                ", Value='" + Value + '\'' +
                ", Childs=" + Childs +
                '}';
    }
}
