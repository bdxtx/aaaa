package com.weiyu.sp.lsjy.bean;

import java.util.List;

public class StudyListBean {
    private List<CourseBean>shiPingListResponses;
    private String picture;

    public List<CourseBean> getShiPingListResponses() {
        return shiPingListResponses;
    }

    public void setShiPingListResponses(List<CourseBean> shiPingListResponses) {
        this.shiPingListResponses = shiPingListResponses;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
