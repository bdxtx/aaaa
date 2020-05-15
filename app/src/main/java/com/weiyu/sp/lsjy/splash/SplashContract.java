package com.weiyu.sp.lsjy.splash;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.LoginBean;

import java.util.Map;

import io.reactivex.Flowable;

public class SplashContract {
    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

        void onSuccess();

    }
    interface Model {
        Flowable<BaseObjectBean<LoginBean>> refreshLogin(String refreshToken);
    }

    interface Presenter{
        void refreshLogin(String refreshToken);
    }
}
