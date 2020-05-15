package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.AccountRecordBean;
import com.weiyu.sp.lsjy.bean.BalanceBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.FieldMap;

public class MyAccountContract {
    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

        void onSuccess(BalanceBean bean);

        void onGetDetail(List<AccountRecordBean>beanList);

    }
    interface Model {
        Flowable<BaseObjectBean<BalanceBean>>selectAccountInfo();
        Flowable<BaseObjectBean<List<AccountRecordBean>>>getAccountRecordDetail(Map<String,String> map);
    }

    interface Presenter{
        void selectAccountInfo();
        void getAccountRecordDetail(Map<String,String> map);
    }
}
