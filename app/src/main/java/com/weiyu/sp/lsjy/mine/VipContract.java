package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.VipCardBean;

import java.util.Map;

import io.reactivex.Flowable;

public class VipContract {
    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

        void onGetSuccess(VipCardBean bean);

    }
    interface Model {
        Flowable<BaseObjectBean<VipCardBean>> getMemberCenter();
    }

    interface Presenter{
        void getMemberCenter();
    }
}
