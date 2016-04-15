package com.example.zhongjiyun03.zhongjiyun.bean.main;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/4/11.
 */
public class ProjectlistBean implements Serializable {

             private String result;
             private List<ProjectlistDataBean> projectlist;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<ProjectlistDataBean> getProjectlist() {
        return projectlist;
    }

    public void setProjectlist(List<ProjectlistDataBean> projectlist) {
        this.projectlist = projectlist;
    }

    @Override
    public String toString() {
        return "ProjectlistBean{" +
                "result='" + result + '\'' +
                ", projectlist=" + projectlist +
                '}';
    }
}
