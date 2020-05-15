package com.weiyu.sp.lsjy.main;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.bean.MeBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;

import io.reactivex.Flowable;

public class MeModel implements MeContract.Model {
    @Override
    public Flowable<BaseObjectBean<MeBean>> meDetail() {
        return RetrofitClient.getInstance().getApi().meDetail();
    }

    @Override
    public Flowable<BaseObjectBean<String>> registerShareInfo(String inviteCode) {
        return RetrofitClient.getInstance().getApi().registerShareInfo(inviteCode);
    }
}
