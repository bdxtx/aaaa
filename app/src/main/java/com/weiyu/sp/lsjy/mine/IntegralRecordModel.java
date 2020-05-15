package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.bean.IntegralRecordBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

public class IntegralRecordModel implements IntegralRecordContract.Model {
    @Override
    public Flowable<BaseObjectBean<List<IntegralRecordBean>>> integralRecordList(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi().integralRecordList(map);
    }
}
