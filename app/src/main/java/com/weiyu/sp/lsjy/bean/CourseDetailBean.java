package com.weiyu.sp.lsjy.bean;

import java.util.List;

public class CourseDetailBean {
    private boolean vip;
    private CourseBean shiPingListResponse;
    private List<CourseBean> shiPingCollectionResponses;
    private long second;

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public CourseBean getShiPingListResponse() {
        return shiPingListResponse;
    }

    public void setShiPingListResponse(CourseBean shiPingListResponse) {
        this.shiPingListResponse = shiPingListResponse;
    }

    public List<CourseBean> getShiPingCollectionResponses() {
        return shiPingCollectionResponses;
    }

    public void setShiPingCollectionResponses(List<CourseBean> shiPingCollectionResponses) {
        this.shiPingCollectionResponses = shiPingCollectionResponses;
    }

    public long getSecond() {
        return second;
    }

    public void setSecond(long second) {
        this.second = second;
    }
}
