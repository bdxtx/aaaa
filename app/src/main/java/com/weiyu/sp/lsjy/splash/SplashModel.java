package com.weiyu.sp.lsjy.splash;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.bean.LoginBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;

import io.reactivex.Flowable;

public class SplashModel implements SplashContract.Model {
    @Override
    public Flowable<BaseObjectBean<LoginBean>> refreshLogin(String refreshToken) {
        return RetrofitClient.getInstance().getApi(Constant.RefreshToken).refreshLogin(refreshToken);
    }
}
