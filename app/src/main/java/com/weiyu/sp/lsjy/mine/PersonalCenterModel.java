package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.bean.PersonalBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.Call;

public class PersonalCenterModel implements PersonalCenterContract.Model {
    @Override
    public Flowable<BaseObjectBean<PersonalBean>> selectUserDetail(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi(Constant.PersonCenterJump).selectUserDetail(map);
    }

    @Override
    public Flowable<BaseObjectBean<PersonalBean>> updateUserDetail(Map<String, String> map) {
        return RetrofitClient.getInstance().getApi().updateUserDetail(map);
    }

    @Override
    public Flowable<BaseObjectBean<String>> loginOut(String refreshToken) {
        return RetrofitClient.getInstance().getApi().loginOut(refreshToken);
    }

}
