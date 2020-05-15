package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.bean.IntegralBean;
import com.weiyu.sp.lsjy.bean.IntegralExchangeBean;
import com.weiyu.sp.lsjy.bean.IntegralListBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

public class IntegralModel implements IntegralContract.Model {
    @Override
    public Flowable<BaseObjectBean<IntegralBean>> selectIntegralInfo() {
        return RetrofitClient.getInstance().getApi().selectIntegralInfo();
    }

    @Override
    public Flowable<BaseObjectBean<List<IntegralListBean>>> selectGoods(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi().selectGoods(map);
    }

    @Override
    public Flowable<BaseObjectBean<IntegralExchangeBean>> exchangeGoods(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi().exchangeGoods(map);
    }
}
