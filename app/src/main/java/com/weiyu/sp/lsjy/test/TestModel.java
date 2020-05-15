package com.weiyu.sp.lsjy.test;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;
import java.util.Map;

import io.reactivex.Flowable;

public class TestModel implements TestContract.Model {

    @Override
    public Flowable<BaseObjectBean<String>> userLoginCode(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi().userLoginCode(map);
    }
}
