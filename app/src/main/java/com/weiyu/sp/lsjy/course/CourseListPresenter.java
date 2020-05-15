package com.weiyu.sp.lsjy.course;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class CourseListPresenter extends BasePresenter<CourseListContract.View> implements CourseListContract.Presenter {
    CourseListContract.Model model=new CourseListModel();
    private int total=0;

    @Override
    public void selectCourseList(Map<String, String> map) {
        if (isViewAttached()){
            mView.showLoading();
            model.selectCourseList(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<List<CourseBean>>>() {
                        @Override
                        public void accept(BaseObjectBean<List<CourseBean>> listBaseObjectBean) throws Exception {
                            total=listBaseObjectBean.getTotal();
                            if (listBaseObjectBean.getCode()==200&&total!=0){
                                mView.onSelectCourseList(listBaseObjectBean.getRows());
                            }else if (listBaseObjectBean.getCode()==200&&total==0){
                                mView.onError(new Throwable(),2);
                            }else if (listBaseObjectBean.getCode()==600||listBaseObjectBean.getCode()==700){

                            }else {
                                mView.onError(new Throwable(),2);
                                ToastUtil.show(listBaseObjectBean.getMessage());
                            }
                            mView.hideLoading();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mView.hideLoading();
                            mView.onError(new Throwable(),3);
                        }
                    });
        }
    }

    @Override
    public void selectCourseList2(Map<String, String> map) {
        if (isViewAttached()){
            mView.showLoading();
            model.selectCourseList2(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<List<CourseBean>>>() {
                        @Override
                        public void accept(BaseObjectBean<List<CourseBean>> listBaseObjectBean) throws Exception {
                            total=listBaseObjectBean.getTotal();
                            if (listBaseObjectBean.getCode()==200&&total!=0){
                                mView.onSelectCourseList(listBaseObjectBean.getRows());
                            }else if (listBaseObjectBean.getCode()==200&&total==0){
                                mView.onError(new Throwable(),0);
                            }else if (listBaseObjectBean.getCode()==600||listBaseObjectBean.getCode()==700){

                            }else {
                                mView.onError(new Throwable(),0);
                                ToastUtil.show(listBaseObjectBean.getMessage());
                            }
                            mView.hideLoading();

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mView.hideLoading();
                            mView.onError(new Throwable(),3);
                        }
                    });
        }
    }

    @Override
    public void courseCollection(Map<String, String> map,int position) {
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
                            mView.onError(new Throwable(),3);
                        }
                    });
        }
    }

    public boolean loadMoreAble(int page){
        if (page*10<total){
            return true;
        }
        return false;
    }
}
