package com.weiyu.sp.lsjy.course;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

public class CourseListModel implements CourseListContract.Model {

    @Override
    public Flowable<BaseObjectBean<List<CourseBean>>> selectCourseList(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi().selectCourseList(map);
    }

    @Override
    public Flowable<BaseObjectBean<List<CourseBean>>> selectCourseList2(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi().selectCourseList2(map);
    }

    @Override
    public Flowable<BaseObjectBean<String>> courseCollection(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi().courseCollection(map);
    }
}
