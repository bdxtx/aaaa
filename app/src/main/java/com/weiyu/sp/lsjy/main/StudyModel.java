package com.weiyu.sp.lsjy.main;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.StudyBean;
import com.weiyu.sp.lsjy.bean.StudyListBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

public class StudyModel implements StudyContract.Model {
    @Override
    public Flowable<BaseObjectBean<StudyListBean>> selectCourseList(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi("study").selectCourseList3(map);
    }

    @Override
    public Flowable<BaseObjectBean<StudyBean>> studyLength() {
        return RetrofitClient.getInstance().getApi("study").studyLength();
    }
}
