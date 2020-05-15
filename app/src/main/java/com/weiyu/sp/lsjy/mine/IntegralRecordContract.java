package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.IntegralRecordBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.FieldMap;

public class IntegralRecordContract {
    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

        void onGetList(List<IntegralRecordBean> beans);

    }
    interface Model {
        Flowable<BaseObjectBean<List<IntegralRecordBean>>>integralRecordList(Map<String,String> map);
    }

    interface Presenter{
        void integralRecordList(Map<String,String> map);
    }
}
