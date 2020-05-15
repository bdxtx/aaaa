package com.weiyu.sp.lsjy.main;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.StudyBean;
import com.weiyu.sp.lsjy.bean.StudyListBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

public class StudyContract {
    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

        void onSelectCourseList(BaseObjectBean<StudyListBean> beanList);

        void studyLength(StudyBean studyBean);

    }
    interface Model {
        Flowable<BaseObjectBean<StudyListBean>> selectCourseList(Map<String, String> map);
        Flowable<BaseObjectBean<StudyBean>> studyLength();
    }

    interface Presenter{
        void selectCourseList(Map<String, String> map);
        void studyLength();
    }
}
