package com.weiyu.sp.lsjy.mine;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.MeBean;
import com.weiyu.sp.lsjy.bean.PersonalBean;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;

public class PersonalCenterContract {
    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

        void userDetail(PersonalBean string);

        void updateHeader(String url);

    }
    interface Model {
        Flowable<BaseObjectBean<PersonalBean>>selectUserDetail(Map<String,String> map);
        Flowable<BaseObjectBean<PersonalBean>>updateUserDetail(Map<String,String> map);
        Flowable<BaseObjectBean<String>> loginOut(String refreshToken);
    }

    interface Presenter{
        void selectUserDetail(Map<String,String> map);
        void updateUserDetail(Map<String,String> map);
        void loginOut(String refreshToken);
    }
}
