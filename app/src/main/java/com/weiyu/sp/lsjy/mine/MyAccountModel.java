package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.bean.AccountRecordBean;
import com.weiyu.sp.lsjy.bean.BalanceBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

public class MyAccountModel implements MyAccountContract.Model {
    @Override
    public Flowable<BaseObjectBean<BalanceBean>> selectAccountInfo() {
        return RetrofitClient.getInstance().getApi().selectAccountInfo();
    }

    @Override
    public Flowable<BaseObjectBean<List<AccountRecordBean>>> getAccountRecordDetail(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi().getAccountRecordDetail(map);
    }
}
