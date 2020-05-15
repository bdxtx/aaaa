package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.AccountRecordBean;
import com.weiyu.sp.lsjy.bean.BalanceBean;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class MyAccountPresenter extends BasePresenter<MyAccountContract.View> implements MyAccountContract.Presenter {
    private MyAccountModel model;
    public MyAccountPresenter(){
        model=new MyAccountModel();
    }
    private int total=0;
    @Override
    public void selectAccountInfo() {
        if (isViewAttached()){
            model.selectAccountInfo()
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<BalanceBean>>() {
                        @Override
                        public void accept(BaseObjectBean<BalanceBean> balanceBeanBaseObjectBean) throws Exception {
                            if (balanceBeanBaseObjectBean.getCode()==200){
                                mView.onSuccess(balanceBeanBaseObjectBean.getRows());
                            }else if (balanceBeanBaseObjectBean.getCode()==600||balanceBeanBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(balanceBeanBaseObjectBean.getMessage());
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mView.onError(throwable,0);
                        }
                    });
        }
    }

    @Override
    public void getAccountRecordDetail(Map<String, String> map) {
        if (isViewAttached()){
            model.getAccountRecordDetail(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<List<AccountRecordBean>>>() {
                        @Override
                        public void accept(BaseObjectBean<List<AccountRecordBean>> listBaseObjectBean) throws Exception {
                            total=listBaseObjectBean.getTotal();
                            if (listBaseObjectBean.getCode()==200&&total!=0){
                                mView.onGetDetail(listBaseObjectBean.getRows());
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
