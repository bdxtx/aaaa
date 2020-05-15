package com.weiyu.sp.lsjy.course;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.bean.CourseDetailBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;

import java.util.Map;

import io.reactivex.Flowable;

public class CourseDetailModel implements CourseDetailContract.Model {
    @Override
    public Flowable<BaseObjectBean<CourseDetailBean>> courseDetail(String courseId) {
        return RetrofitClient.getInstance().getApi().courseDetail(courseId);
    }
    @Override
    public Flowable<BaseObjectBean<String>> courseCollection(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi().courseCollection(map);
    }

    @Override
    public Flowable<BaseObjectBean<String>> startStudy(String courseId) {
        return RetrofitClient.getInstance().getApi().startStudy(courseId);
    }
}
