package com.weiyu.sp.lsjy.main;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.bean.MeBean;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import io.reactivex.functions.Consumer;

public class MePresenter extends BasePresenter<MeContract.View>implements MeContract.Presenter {
    private MeModel meModel;
    public MePresenter(){
        meModel=new MeModel();
    }
    @Override
    public void meDetail() {
        if (isViewAttached()){
//            mView.showLoading();
            meModel.meDetail().compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<MeBean>>() {
                        @Override
                        public void accept(BaseObjectBean<MeBean> meBeanBaseObjectBean) throws Exception {
                            if (meBeanBaseObjectBean.getCode() == 200) {
                                mView.onGetMeSuccess(meBeanBaseObjectBean.getRows());
//                                mView.hideLoading();
                            }else if (meBeanBaseObjectBean.getCode() == 600||meBeanBaseObjectBean.getCode() == 700){

                            }else {
                                ToastUtil.show(meBeanBaseObjectBean.getMessage());
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mView.onError(throwable,0);
//                            mView.hideLoading();
                        }
                    });
        }
    }

    @Override
    public void registerShareInfo(String inviteCode) {
        if (isViewAttached()){
            mView.showLoading();
            meModel.registerShareInfo(inviteCode)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<String>>() {
                        @Override
                        public void accept(BaseObjectBean<String> stringBaseObjectBean) throws Exception {
                            mView.onRegister();
                            mView.hideLoading();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mView.hideLoading();
                        }
                    });
        }
    }
}
