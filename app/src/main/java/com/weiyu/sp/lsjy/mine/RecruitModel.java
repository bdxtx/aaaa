package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.bean.OrderBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

public class RecruitModel implements RecruitContract.Model {

    @Override
    public Flowable<BaseObjectBean<String>> postOpinion(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi().postOpinion(map);
    }
}
