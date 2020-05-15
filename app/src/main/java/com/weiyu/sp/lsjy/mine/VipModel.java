package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.bean.VipCardBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;

import io.reactivex.Flowable;

public class VipModel implements VipContract.Model {
    @Override
    public Flowable<BaseObjectBean<VipCardBean>> getMemberCenter() {
        return RetrofitClient.getInstance().getApi().getMemberCenter();
    }
}
