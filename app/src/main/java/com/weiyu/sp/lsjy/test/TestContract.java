package com.weiyu.sp.lsjy.test;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;

import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Flowable;
import retrofit2.http.FieldMap;

public class TestContract {
    interface View extends BaseView{
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

        void onSuccess();

        void onUserLoginCodeSuccess();
    }
    interface Model {
        Flowable<BaseObjectBean<String>> userLoginCode(Map<String,String> map);
    }

    interface Presenter{
        void userLoginCode(Map<String,String> map);
    }
}
