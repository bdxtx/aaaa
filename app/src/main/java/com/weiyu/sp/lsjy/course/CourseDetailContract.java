package com.weiyu.sp.lsjy.course;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.CourseDetailBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

public class CourseDetailContract {
    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

        void getDetail(CourseDetailBean courseDetailBean);

        void onClickCollection(int position);

    }
    interface Model {
        Flowable<BaseObjectBean<CourseDetailBean>>courseDetail(String courseId);
        Flowable<BaseObjectBean<String>>courseCollection(Map<String, String> map);
        Flowable<BaseObjectBean<String>>startStudy(String courseId);
    }

    interface Presenter{
        void courseDetail(String courseId);
        void courseCollection(Map<String, String> map,int position);
        void startStudy(String courseId);
    }
}
