package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.OrderBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.FieldMap;

public class OpinionContract {
    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

        void onPostOpinion();

    }
    interface Model {
        Flowable<BaseObjectBean<String>> postOpinion(Map<String,String> map);
    }

    interface Presenter{
        void postOpinion(Map<String,String> map);
    }
}
