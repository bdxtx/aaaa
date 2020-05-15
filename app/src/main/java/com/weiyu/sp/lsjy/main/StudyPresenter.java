package com.weiyu.sp.lsjy.main;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.StudyBean;
import com.weiyu.sp.lsjy.bean.StudyListBean;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class StudyPresenter extends BasePresenter<StudyContract.View>implements StudyContract.Presenter {
    private StudyContract.Model model;
    int total=0;
    public StudyPresenter(){
        model=new StudyModel();
    }
    @Override
    public void selectCourseList(Map<String, String> map) {
        if (isViewAttached()){
//            mView.showLoading();
            model.selectCourseList(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<StudyListBean>>() {
                        @Override
                        public void accept(BaseObjectBean<StudyListBean> listBaseObjectBean) throws Exception {
//                            mView.hideLoading();
                            total=listBaseObjectBean.getTotal();
                            if (listBaseObjectBean.getCode()==200&&total!=0){
                                mView.onSelectCourseList(listBaseObjectBean);
                            }else if (listBaseObjectBean.getCode()==200&&total==0){
                                mView.onError(new Throwable(),0);
                            }else if (listBaseObjectBean.getCode()==600||listBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(listBaseObjectBean.getMessage());
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
//                            mView.hideLoading();
                            mView.onError(throwable,1);
                        }
                    });
        }
    }

    @Override
    public void studyLength() {
        if (isViewAttached()){
//            mView.showLoading();
            model.studyLength()
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<StudyBean>>() {
                        @Override
                        public void accept(BaseObjectBean<StudyBean> stringBaseObjectBean) throws Exception {
//                            mView.hideLoading();
                            if (stringBaseObjectBean.getCode()==200){
                                mView.studyLength(stringBaseObjectBean.getRows());
                            }else if (stringBaseObjectBean.getCode()==600||stringBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(stringBaseObjectBean.getMessage());
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
//                            mView.hideLoading();
                            mView.onError(throwable,1);
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
