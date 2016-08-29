package com.example.zhongjiyun03.zhongjiyun.bean.JPush;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/7/28.
 */
public class ExtrasBean implements Serializable {
        private String ApiParams;
        private String Type;
        private String ApiUrl;
        private String BLLCode;

    public String getApiParams() {
        return ApiParams;
    }

    public void setApiParams(String apiParams) {
        ApiParams = apiParams;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getApiUrl() {
        return ApiUrl;
    }

    public void setApiUrl(String apiUrl) {
        ApiUrl = apiUrl;
    }

    public String getBLLCode() {
        return BLLCode;
    }

    public void setBLLCode(String BLLCode) {
        this.BLLCode = BLLCode;
    }

    @Override
    public String toString() {
        return "ExtrasBean{" +
                "ApiParams='" + ApiParams + '\'' +
                ", Type='" + Type + '\'' +
                ", ApiUrl='" + ApiUrl + '\'' +
                ", BLLCode='" + BLLCode + '\'' +
                '}';
    }
}
