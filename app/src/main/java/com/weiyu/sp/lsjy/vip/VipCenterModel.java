package com.weiyu.sp.lsjy.vip;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.bean.PayChannel;
import com.weiyu.sp.lsjy.bean.PayResultBean;
import com.weiyu.sp.lsjy.bean.VipBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;

import java.util.Map;

import io.reactivex.Flowable;

public class VipCenterModel implements VipCenterContract.Model {
    @Override
    public Flowable<BaseObjectBean<VipBean>> selectPayChannelList() {
        return RetrofitClient.getInstance().getApi().selectPayChannelList();
    }

    @Override
    public Flowable<BaseObjectBean<PayResultBean>> buyVip(Map<String,String> map) {
        return RetrofitClient.getInstance().getApi().buyVip(map);
    }

    @Override
    public Flowable<BaseObjectBean<String>> activeVip(String activeCode) {
        return RetrofitClient.getInstance().getApi().activeVip(activeCode);
    }
}
