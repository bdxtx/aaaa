package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.bean.OrderBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

public class OrderListModel implements OrderListContract.Model {
    @Override
    public Flowable<BaseObjectBean<List<OrderBean>>> getOrderList(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi().getOrderList(map);
    }
}
