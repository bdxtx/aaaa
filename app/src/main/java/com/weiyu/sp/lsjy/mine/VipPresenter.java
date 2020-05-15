package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.bean.VipCardBean;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import io.reactivex.functions.Consumer;

public class VipPresenter extends BasePresenter<VipContract.View>implements VipContract.Presenter {
    private VipContract.Model model;
    public VipPresenter(){
        model=new VipModel();
    }
    @Override
    public void getMemberCenter() {
        if (isViewAttached()){
            mView.showLoading();
            model.getMemberCenter()
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<VipCardBean>>() {
                        @Override
                        public void accept(BaseObjectBean<VipCardBean> vipCardBeanBaseObjectBean) throws Exception {
                            if (vipCardBeanBaseObjectBean.getCode() == 200) {
                                mView.onGetSuccess(vipCardBeanBaseObjectBean.getRows());
                            } else if (vipCardBeanBaseObjectBean.getCode() == 600||vipCardBeanBaseObjectBean.getCode() == 700){

                            }else {
                                ToastUtil.show(vipCardBeanBaseObjectBean.getMessage());
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
