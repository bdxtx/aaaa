package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.PersonalBean;
import com.weiyu.sp.lsjy.net.RxScheduler;

import java.util.Map;

import io.reactivex.functions.Consumer;

public class UpdatePersonalPresenter extends BasePresenter<UpdatePersonalBaseDialog> {
    PersonalCenterModel personalCenterModel;
    public UpdatePersonalPresenter(){
        personalCenterModel=new PersonalCenterModel();
    }
    public void updateUserDetail(Map<String, String> map) {
        if (isViewAttached()){
            mView.showLoading();
            personalCenterModel.updateUserDetail(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<PersonalBean>>() {
                        @Override
                        public void accept(BaseObjectBean<PersonalBean> stringBaseObjectBean) throws Exception {
                            mView.onSuccess(stringBaseObjectBean);
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
