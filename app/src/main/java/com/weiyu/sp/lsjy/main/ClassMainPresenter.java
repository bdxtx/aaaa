package com.weiyu.sp.lsjy.main;

import android.util.Log;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.net.RxScheduler;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class ClassMainPresenter extends BasePresenter implements ClassMainContract.Presenter {
    ClassMainContract.Model model=new ClassMainModel();

    @Override
    public void selectCourseList(Map<String, String> map) {
        if (isViewAttached()){
            model.selectCourseList(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<List<CourseBean>>>() {
                        @Override
                        public void accept(BaseObjectBean<List<CourseBean>> listBaseObjectBean) throws Exception {

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
        }
    }
}
