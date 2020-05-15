package com.weiyu.sp.lsjy.other;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.bean.ShareBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;

import io.reactivex.Flowable;

public class ShareModel implements ShareContract.Model {
    @Override
    public Flowable<BaseObjectBean<ShareBean>> appShare() {
        return RetrofitClient.getInstance().getApi().appShare();
    }
}
