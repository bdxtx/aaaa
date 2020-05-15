package com.weiyu.sp.lsjy.main;

import android.util.Log;

import com.google.android.exoplayer2.C;
import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.bean.BannerBean;
import com.weiyu.sp.lsjy.bean.HomeBean;
import com.weiyu.sp.lsjy.bean.UpdateBean;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {
    MainContract.Model model=new MainModel();

    @Override
    public void getBanner() {
        if (isViewAttached()){
            mView.showLoading();
            model.getBanner()
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<List<BannerBean>>>() {
                        @Override
                        public void accept(BaseObjectBean<List<BannerBean>> listBaseObjectBean) throws Exception {
                            if (listBaseObjectBean.getCode()==200){
                                mView.onGotBanner(listBaseObjectBean.getRows());
                            }else {
                                ToastUtil.show(listBaseObjectBean.getMessage());
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
    public void getHomeList() {
        if (isViewAttached()){
            mView.showLoading();
            model.getHomeList()
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<HomeBean>>() {
                        @Override
                        public void accept(BaseObjectBean<HomeBean> homeBeanBaseObjectBean) throws Exception {
                            mView.onGotHomeList(homeBeanBaseObjectBean.getRows());
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
    public void update(Map<String, String> map) {
        if (isViewAttached()){
            model.update(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<UpdateBean>>() {
                        @Override
                        public void accept(BaseObjectBean<UpdateBean> updateBeanBaseObjectBean) throws Exception {
                            if (updateBeanBaseObjectBean.getCode()==200){
                                mView.onGetUpdateMsg(updateBeanBaseObjectBean.getRows());
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
        }
    }
}
