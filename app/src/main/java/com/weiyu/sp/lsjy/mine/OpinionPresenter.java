package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import java.util.Map;

import io.reactivex.functions.Consumer;

public class OpinionPresenter extends BasePresenter<OpinionContract.View> implements OpinionContract.Presenter {
    private OpinionContract.Model model;
    public OpinionPresenter(){
        model=new OpinionModel();
    }
    @Override
    public void postOpinion(Map<String, String> map) {
        if (isViewAttached()){
            mView.showLoading();
            model.postOpinion(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<String>>() {
                        @Override
                        public void accept(BaseObjectBean<String> stringBaseObjectBean) throws Exception {
                            if (stringBaseObjectBean.getCode() == 200) {
                                mView.onPostOpinion();
                            } else if (stringBaseObjectBean.getCode()==600||stringBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(stringBaseObjectBean.getMessage());
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
}
