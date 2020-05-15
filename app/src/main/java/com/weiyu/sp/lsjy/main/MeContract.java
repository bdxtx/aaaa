package com.weiyu.sp.lsjy.main;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.MeBean;

import java.util.Map;

import io.reactivex.Flowable;

public class MeContract {
    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

        void onGetMeSuccess(MeBean meBean);

        void onRegister();

    }
    interface Model {
        Flowable<BaseObjectBean<MeBean>>meDetail();
        Flowable<BaseObjectBean<String>>registerShareInfo(String inviteCode);

    }

    interface Presenter{
        void meDetail();
        void registerShareInfo(String inviteCode);
    }
}
