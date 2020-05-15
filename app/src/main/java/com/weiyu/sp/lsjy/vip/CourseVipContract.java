package com.weiyu.sp.lsjy.vip;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.CourseVipBean;
import com.weiyu.sp.lsjy.bean.PayResultBean;
import com.weiyu.sp.lsjy.bean.VipBean;

import java.util.Map;

import io.reactivex.Flowable;

public class CourseVipContract {
    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

        void onGetChannel(CourseVipBean courseVipBean);

        void onBuyCourse(PayResultBean payResultBean);

        void onClickCollection(int position);

    }
    interface Model {
        Flowable<BaseObjectBean<PayResultBean>>buyCourse(Map<String,String> map);
        Flowable<BaseObjectBean<CourseVipBean>>buyCourseDetail(Map<String,String> map);
        Flowable<BaseObjectBean<String>>courseCollection(Map<String, String> map);
    }

    interface Presenter{
        void buyCourse(Map<String,String> map);
        void buyCourseDetail(Map<String,String> map);
        void courseCollection(Map<String, String> map,int position);
    }
}
