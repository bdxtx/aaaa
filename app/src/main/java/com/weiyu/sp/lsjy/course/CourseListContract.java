package com.weiyu.sp.lsjy.course;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.CourseBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Field;

public class CourseListContract {
    interface View extends BaseView{
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

        void onSelectCourseList(List<CourseBean> beanList);

        void onClickCollection(int position);
    }
    interface Model {
        Flowable<BaseObjectBean<List<CourseBean>>>selectCourseList(Map<String, String> map);
        Flowable<BaseObjectBean<List<CourseBean>>>selectCourseList2(Map<String, String> map);
        Flowable<BaseObjectBean<String>>courseCollection(Map<String, String> map);
    }

    interface Presenter{
        void selectCourseList(Map<String, String> map);
        void selectCourseList2(Map<String, String> map);
        void courseCollection(Map<String, String> map,int position);
    }
}
