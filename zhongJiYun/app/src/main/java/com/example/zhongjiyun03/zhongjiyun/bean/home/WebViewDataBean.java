package com.example.zhongjiyun03.zhongjiyun.bean.home;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/11/7.
 */

public class WebViewDataBean implements Serializable {

    private String result;/*返回结果状态。success：正常；fail：错误；unlogin：未登录；empty：没数据；nomore：没有更多数据；deploy：部署中；unregistered：未注册audit：账号审核中*/
    private String msg;  //返回结果描述
    private WebViewBean data; //数据内容

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public WebViewBean getData() {
        return data;
    }

    public void setData(WebViewBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WebViewDataBean{" +
                "result='" + result + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
