package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.bean.IntegralBean;
import com.weiyu.sp.lsjy.bean.IntegralExchangeBean;
import com.weiyu.sp.lsjy.bean.IntegralListBean;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class IntegralPresenter extends BasePresenter<IntegralContract.View> implements IntegralContract.Presenter {
    private IntegralModel model;
    public IntegralPresenter(){
        model=new IntegralModel();
    }
    private int total=0;
    @Override
    public void selectIntegralInfo() {
        if (isViewAttached()){
            model.selectIntegralInfo()
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<IntegralBean>>() {
                        @Override
                        public void accept(BaseObjectBean<IntegralBean> integralBeanBaseObjectBean) throws Exception {
                            if (integralBeanBaseObjectBean.getCode()==200){
                                mView.onGetTop(integralBeanBaseObjectBean.getRows());
                            }else if (integralBeanBaseObjectBean.getCode()==600||integralBeanBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(integralBeanBaseObjectBean.getMessage());
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mView.onError(throwable,1);
                        }
                    });
        }
    }

    @Override
    public void selectGoods(Map<String, String> map) {
        if (isViewAttached()){
            model.selectGoods(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<List<IntegralListBean>>>() {
                        @Override
                        public void accept(BaseObjectBean<List<IntegralListBean>> listBaseObjectBean) throws Exception {
                            total = listBaseObjectBean.getTotal();
                            if (listBaseObjectBean.getCode() == 200 && total != 0) {
                                total = listBaseObjectBean.getTotal();
                                mView.onGetList(listBaseObjectBean.getRows());
                            } else if (listBaseObjectBean.getCode() == 200 && total == 0) {
                                mView.onError(new Throwable(), 0);
                            } else if (listBaseObjectBean.getCode() == 600||listBaseObjectBean.getCode() == 700){

                            }else {
                                ToastUtil.show(listBaseObjectBean.getMessage());
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mView.onError(throwable,1);
                        }
                    });
        }
    }

    @Override
    public void exchangeGoods(Map<String, String> map) {
        if (isViewAttached()){
            mView.showLoading();
            model.exchangeGoods(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<IntegralExchangeBean>>() {
                        @Override
                        public void accept(BaseObjectBean<IntegralExchangeBean> integralExchangeBeanBaseObjectBean) throws Exception {
                            if (integralExchangeBeanBaseObjectBean.getCode()==200){
                                mView.onChange(integralExchangeBeanBaseObjectBean);
                                ToastUtil.show(integralExchangeBeanBaseObjectBean.getMessage());
                            }else if (integralExchangeBeanBaseObjectBean.getCode()==600||integralExchangeBeanBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(integralExchangeBeanBaseObjectBean.getMessage());
                            }
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

    public boolean loadMoreAble(int page){
        if (page*10<total){
            return true;
        }
        return false;
    }
}
