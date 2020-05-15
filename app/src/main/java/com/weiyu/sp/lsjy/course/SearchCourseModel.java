package com.weiyu.sp.lsjy.course;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

public class SearchCourseModel implements SearchCourseContract.Model {
    @Override
    public Flowable<BaseObjectBean<List<String>>> hotSearch() {
        return RetrofitClient.getInstance().getApi().hotSearch();
    }

    @Override
    public Flowable<BaseObjectBean<List<CourseBean>>> selectCourseList(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi().selectCourseList(map);
    }
}
