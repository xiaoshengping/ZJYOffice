package com.example.zhongjiyun03.zhongjiyun.bean;

import java.io.Serializable;

/**
 * Created by ZHONGJIYUN03 on 2016/5/6.
 */
public class VersontDataBean implements Serializable {

    private String No	;//String	版本号
    private String Content	;//String	更新内容说明
    private String FileSize	;//String	文件大小
    private String DownloadUrl	;//String	下载地址
    private int UpdateLevel	;//Int	更新等级0，不更新，1，更新，2，强制更新

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getFileSize() {
        return FileSize;
    }

    public void setFileSize(String fileSize) {
        FileSize = fileSize;
    }

    public String getDownloadUrl() {
        return DownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        DownloadUrl = downloadUrl;
    }

    public int getUpdateLevel() {
        return UpdateLevel;
    }

    public void setUpdateLevel(int updateLevel) {
        UpdateLevel = updateLevel;
    }

    @Override
    public String toString() {
        return "VersontDataBean{" +
                "No='" + No + '\'' +
                ", Content='" + Content + '\'' +
                ", FileSize='" + FileSize + '\'' +
                ", DownloadUrl='" + DownloadUrl + '\'' +
                ", UpdateLevel=" + UpdateLevel +
                '}';
    }
}
