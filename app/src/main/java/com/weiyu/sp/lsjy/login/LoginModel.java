package com.weiyu.sp.lsjy.login;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.bean.LoginBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;

import java.util.Map;

import io.reactivex.Flowable;

public class LoginModel implements LoginContract.Model {

    @Override
    public Flowable<BaseObjectBean<String>> sendMsg(String loginName) {
        return RetrofitClient.getInstance().getApi().sendMsg(loginName);
    }

    @Override
    public Flowable<BaseObjectBean<LoginBean>> doLogin(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi().doLogin(map);
    }
}
