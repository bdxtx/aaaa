package com.weiyu.sp.lsjy.vip;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.bean.CourseVipBean;
import com.weiyu.sp.lsjy.bean.PayResultBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;

import java.util.Map;

import io.reactivex.Flowable;

public class CourseVipModel implements CourseVipContract.Model {
    @Override
    public Flowable<BaseObjectBean<PayResultBean>> buyCourse(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi().buyCourse(map);
    }

    @Override
    public Flowable<BaseObjectBean<CourseVipBean>> buyCourseDetail(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi().buyCourseDetail(map);
    }

    @Override
    public Flowable<BaseObjectBean<String>> courseCollection(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi().courseCollection(map);
    }
}
