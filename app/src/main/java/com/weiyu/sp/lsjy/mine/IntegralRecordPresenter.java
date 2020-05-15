package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.bean.IntegralRecordBean;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class IntegralRecordPresenter extends BasePresenter<IntegralRecordContract.View> implements IntegralRecordContract.Presenter {
    private IntegralRecordContract.Model model;
    private int total;
    public IntegralRecordPresenter(){
        model=new IntegralRecordModel();
    }
    @Override
    public void integralRecordList(Map<String, String> map) {
        if (isViewAttached()){
            mView.showLoading();
            model.integralRecordList(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<List<IntegralRecordBean>>>() {
                        @Override
                        public void accept(BaseObjectBean<List<IntegralRecordBean>> listBaseObjectBean) throws Exception {
                            total=listBaseObjectBean.getTotal();
                            if (listBaseObjectBean.getCode()==200&&total!=0){
                                mView.onGetList(listBaseObjectBean.getRows());
                            } else if (listBaseObjectBean.getCode()==200&&total==0){
                                mView.onError(new Throwable(),0);
                            }else if (listBaseObjectBean.getCode()==600||listBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(listBaseObjectBean.getMessage());
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
