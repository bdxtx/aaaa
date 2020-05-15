package com.weiyu.sp.lsjy.main;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.bean.BannerBean;
import com.weiyu.sp.lsjy.bean.HomeBean;
import com.weiyu.sp.lsjy.bean.UpdateBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

public class MainModel implements MainContract.Model {

    @Override
    public Flowable<BaseObjectBean<List<BannerBean>>> getBanner() {
        return RetrofitClient.getInstance().getApi().getBanner();
    }

    @Override
    public Flowable<BaseObjectBean<HomeBean>> getHomeList() {
        return RetrofitClient.getInstance().getApi().getHomeList();
    }

    @Override
    public Flowable<BaseObjectBean<UpdateBean>> update(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi().update(map);
    }
}
