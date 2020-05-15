package com.weiyu.sp.lsjy.vip;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.PayChannel;
import com.weiyu.sp.lsjy.bean.PayResultBean;
import com.weiyu.sp.lsjy.bean.VipBean;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Field;

public class VipCenterContract {
    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

        void onGetChannel(VipBean vipBean);

        void onBuyVip(PayResultBean payResultBean);

        void onActiveVip();

    }
    interface Model {
        Flowable<BaseObjectBean<VipBean>>selectPayChannelList();
        Flowable<BaseObjectBean<PayResultBean>>buyVip(Map<String,String> map);
        Flowable<BaseObjectBean<String>>activeVip(String activeCode);
    }

    interface Presenter{
        void selectPayChannelList();
        void buyVip(Map<String,String> map);
        void activeVip(String activeCode);
    }
}
