package com.weiyu.sp.lsjy.vip;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.bean.CourseVipBean;
import com.weiyu.sp.lsjy.bean.PayResultBean;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import java.util.Map;

import io.reactivex.functions.Consumer;

public class CourseVipPresenter extends BasePresenter<CourseVipContract.View>implements CourseVipContract.Presenter {
    CourseVipContract.Model model;
    public CourseVipPresenter(){
        model=new CourseVipModel();
    }
    @Override
    public void buyCourse(Map<String, String> map) {
        if (isViewAttached()){
            model.buyCourse(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<PayResultBean>>() {
                        @Override
                        public void accept(BaseObjectBean<PayResultBean> payResultBeanBaseObjectBean) throws Exception {
                            if (payResultBeanBaseObjectBean.getCode()==200){
                                mView.onBuyCourse(payResultBeanBaseObjectBean.getRows());
                            }else if (payResultBeanBaseObjectBean.getCode()==600||payResultBeanBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(payResultBeanBaseObjectBean.getMessage());
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
        }

    }

    @Override
    public void buyCourseDetail(Map<String, String> map) {
        if (isViewAttached()){
            model.buyCourseDetail(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<CourseVipBean>>() {
                        @Override
                        public void accept(BaseObjectBean<CourseVipBean> payResultBeanBaseObjectBean) throws Exception {
                            if (payResultBeanBaseObjectBean.getCode()==200){
                                mView.onGetChannel(payResultBeanBaseObjectBean.getRows());
                            }else if (payResultBeanBaseObjectBean.getCode()==600||payResultBeanBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(payResultBeanBaseObjectBean.getMessage());
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

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
}
