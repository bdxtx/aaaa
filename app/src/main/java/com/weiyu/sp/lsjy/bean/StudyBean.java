package com.weiyu.sp.lsjy.bean;

import java.util.List;

public class StudyBean {
    private List<StudyLengthBean> week;
    private String info;
    private String picture;

    public List<StudyLengthBean> getWeek() {
        return week;
    }

    public void setWeek(List<StudyLengthBean> week) {
        this.week = week;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
