package com.weiyu.sp.lsjy.course;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.bean.CourseDetailBean;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import java.util.Map;

import io.reactivex.functions.Consumer;

public class CourseDetailPresenter extends BasePresenter<CourseDetailContract.View> implements CourseDetailContract.Presenter {
    CourseDetailContract.Model model;
    public CourseDetailPresenter(){
        model=new CourseDetailModel();
    }
    @Override
    public void courseDetail(String courseId) {
        if (isViewAttached()){
            mView.showLoading();
            model.courseDetail(courseId)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<CourseDetailBean>>() {
                        @Override
                        public void accept(BaseObjectBean<CourseDetailBean> courseDetailBeanBaseObjectBean) throws Exception {
                            if (courseDetailBeanBaseObjectBean.getCode()==200){
                                mView.getDetail(courseDetailBeanBaseObjectBean.getRows());
                            }else if (courseDetailBeanBaseObjectBean.getCode()==600||courseDetailBeanBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(courseDetailBeanBaseObjectBean.getMessage());
                            }
                            mView.hideLoading();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mView.hideLoading();
                            mView.onError(throwable,0);
                        }
                    });
        }

    }

    @Override
    public void courseCollection(Map<String, String> map, int position) {
        if (isViewAttached()){
            mView.showLoading();
            model.courseCollection(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<String>>() {
                        @Override
                        public void accept(BaseObjectBean<String> stringBaseObjectBean) throws Exception {
                            if (stringBaseObjectBean.getCode()==200){
                                mView.onClickCollection(position);
                            }else if (stringBaseObjectBean.getCode()==600||stringBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(stringBaseObjectBean.getMessage());
                            }
                            mView.hideLoading();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mView.hideLoading();
                            mView.onError(throwable,0);
                        }
                    });
        }
    }

    @Override
    public void startStudy(String courseId) {
        if (isViewAttached()){
            model.startStudy(courseId)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<String>>() {
                        @Override
                        public void accept(BaseObjectBean<String> stringBaseObjectBean) throws Exception {

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
        }
    }
}
