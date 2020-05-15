package com.weiyu.sp.lsjy.vip;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.bean.PayResultBean;
import com.weiyu.sp.lsjy.bean.VipBean;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import java.util.Map;

import io.reactivex.functions.Consumer;

public class VipCenterPresenter extends BasePresenter<VipCenterContract.View> implements VipCenterContract.Presenter {
    VipCenterContract.Model model;
    public VipCenterPresenter(){
        model=new VipCenterModel();
    }
    @Override
    public void selectPayChannelList() {
        if (isViewAttached()){
            model.selectPayChannelList()
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<VipBean>>() {
                        @Override
                        public void accept(BaseObjectBean<VipBean> vipBeanBaseObjectBean) throws Exception {
                            if (vipBeanBaseObjectBean.getCode()==200){
                                mView.onGetChannel(vipBeanBaseObjectBean.getRows());
                            }else if (vipBeanBaseObjectBean.getCode()==600||vipBeanBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(vipBeanBaseObjectBean.getMessage());
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
    public void buyVip(Map<String,String> map) {
        if (isViewAttached()){
            model.buyVip(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<PayResultBean>>() {
                        @Override
                        public void accept(BaseObjectBean<PayResultBean> payResultBeanBaseObjectBean) throws Exception {
                            if (payResultBeanBaseObjectBean.getCode()==200){
                                mView.onBuyVip(payResultBeanBaseObjectBean.getRows());
                            }else if (payResultBeanBaseObjectBean.getCode()==600||payResultBeanBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(payResultBeanBaseObjectBean.getMessage());
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.i("csc", "aaaaaaaaaaa" + throwable);

                        }
                    });
        }
    }

    @Override
    public void activeVip(String activeCode) {
        if (isViewAttached()){
            model.activeVip(activeCode)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<String>>() {
                        @Override
                        public void accept(BaseObjectBean<String> stringBaseObjectBean) throws Exception {
                            if (stringBaseObjectBean.getCode()==200){
                                mView.onActiveVip();
                            }else if (stringBaseObjectBean.getCode()==600||stringBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(stringBaseObjectBean.getMessage());
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
