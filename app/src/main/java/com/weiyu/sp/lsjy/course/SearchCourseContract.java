package com.weiyu.sp.lsjy.course;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.CourseBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

public class SearchCourseContract {
    interface View extends BaseView{
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

        void onGetHotSearch(List<String>bean);
        void onSelectCourseList(List<CourseBean> beanList);

    }
    interface Model {
        Flowable<BaseObjectBean<List<String>>>hotSearch();
        Flowable<BaseObjectBean<List<CourseBean>>>selectCourseList(Map<String, String> map);
    }

    interface Presenter{
        void hotSearch();
        void selectCourseList(Map<String, String> map);
    }
}
