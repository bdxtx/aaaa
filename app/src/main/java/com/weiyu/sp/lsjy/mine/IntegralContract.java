package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.IntegralBean;
import com.weiyu.sp.lsjy.bean.IntegralExchangeBean;
import com.weiyu.sp.lsjy.bean.IntegralListBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.FieldMap;

public class IntegralContract {
    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

        void onGetTop(IntegralBean bean);

        void onGetList(List<IntegralListBean> beanList);

        void onChange(BaseObjectBean<IntegralExchangeBean> bean);


    }
    interface Model {
        Flowable<BaseObjectBean<IntegralBean>>selectIntegralInfo();
        Flowable<BaseObjectBean<List<IntegralListBean>>>selectGoods(Map<String,String> map);
        Flowable<BaseObjectBean<IntegralExchangeBean>>exchangeGoods(Map<String,String> map);
    }

    interface Presenter{
        void selectIntegralInfo();
        void selectGoods(Map<String,String> map);
        void exchangeGoods(Map<String,String> map);
    }
}
