package com.weiyu.sp.lsjy.login;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.LoginBean;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Field;

public class LoginContract {
    interface View extends BaseView{
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

        void onSuccess(String code);

        void onLoginSuccess();

    }
    interface Model {
        Flowable<BaseObjectBean<String>>sendMsg(String loginName );
        Flowable<BaseObjectBean<LoginBean>>doLogin(Map<String,String> map);
    }

    interface Presenter{
        void sendMsg(String loginName );
        void doLogin(Map<String,String> map);
    }
}
