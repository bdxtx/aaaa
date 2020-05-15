package com.weiyu.sp.lsjy.course;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class SearchCoursePresenter extends BasePresenter<SearchCourseContract.View> implements SearchCourseContract.Presenter {
    private SearchCourseContract.Model model;
    private int total=0;
    public SearchCoursePresenter(){
        model=new SearchCourseModel();
    }
    @Override
    public void hotSearch() {
        if (isViewAttached()){
            model.hotSearch()
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<List<String>>>() {
                        @Override
                        public void accept(BaseObjectBean<List<String>> listBaseObjectBean) throws Exception {
                            if (listBaseObjectBean.getCode()==200){
                                mView.onGetHotSearch(listBaseObjectBean.getRows());
                            }else if (listBaseObjectBean.getCode()==600||listBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(listBaseObjectBean.getMessage());
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
    public void selectCourseList(Map<String, String> map) {
        if (isViewAttached()){
            mView.showLoading();
            model.selectCourseList(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<List<CourseBean>>>() {
                        @Override
                        public void accept(BaseObjectBean<List<CourseBean>> listBaseObjectBean) throws Exception {
                            if (listBaseObjectBean.getCode()==200){
                                total=listBaseObjectBean.getTotal();
                                mView.onSelectCourseList(listBaseObjectBean.getRows());
                                mView.hideLoading();
                            }else if (listBaseObjectBean.getCode()==600||listBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(listBaseObjectBean.getMessage());
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mView.hideLoading();
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
