package com.weiyu.sp.lsjy.bean;

import java.util.List;

public class HomeBean {
    private CourseBean course;
    private List<CourseBean> courseCollects;
    private List<CurriculumResponse>curriculumResponses;
    private String customer;

    public CourseBean getCourse() {
        return course;
    }

    public void setCourse(CourseBean course) {
        this.course = course;
    }

    public List<CourseBean> getCourseCollects() {
        return courseCollects;
    }

    public void setCourseCollects(List<CourseBean> courseCollects) {
        this.courseCollects = courseCollects;
    }

    public List<CurriculumResponse> getCurriculumResponses() {
        return curriculumResponses;
    }

    public void setCurriculumResponses(List<CurriculumResponse> curriculumResponses) {
        this.curriculumResponses = curriculumResponses;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
