package com.weiyu.sp.lsjy.main;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.LoginBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.FieldMap;

public class ClassMainContract {
    public interface View extends BaseView{
//        @Override
//        void showLoading();
//
//        @Override
//        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

//        void onSelectCourseList(List<CourseBean>beanList);
    }
    interface Model {
        Flowable<BaseObjectBean<List<CourseBean>>>selectCourseList(Map<String,String> map);
    }

    interface Presenter{
        void selectCourseList(Map<String,String> map);
    }
}
