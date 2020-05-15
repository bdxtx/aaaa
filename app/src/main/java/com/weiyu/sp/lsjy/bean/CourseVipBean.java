package com.weiyu.sp.lsjy.bean;

import java.util.List;

public class CourseVipBean {
    private CourseBean course;
    private List<PayChannel> pay;
    private String picture;

    public CourseBean getCourse() {
        return course;
    }

    public void setCourse(CourseBean course) {
        this.course = course;
    }

    public List<PayChannel> getPay() {
        return pay;
    }

    public void setPay(List<PayChannel> pay) {
        this.pay = pay;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
