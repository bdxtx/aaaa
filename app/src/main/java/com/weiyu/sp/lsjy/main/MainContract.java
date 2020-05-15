package com.weiyu.sp.lsjy.main;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.BannerBean;
import com.weiyu.sp.lsjy.bean.HomeBean;
import com.weiyu.sp.lsjy.bean.UpdateBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.FieldMap;

public class MainContract {
    interface View extends BaseView{
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

        void onSuccess();

        void onGotBanner(List<BannerBean> beans);
        void onGotHomeList(HomeBean homeBean);
        void onGetUpdateMsg(UpdateBean updateBean);

    }
    interface Model {
        Flowable<BaseObjectBean<List<BannerBean>>>getBanner();
        Flowable<BaseObjectBean<HomeBean>>getHomeList();
        Flowable<BaseObjectBean<UpdateBean>> update(Map<String,String> map);
    }

    interface Presenter{
        void getBanner();
        void getHomeList();
        void update(Map<String,String> map);
    }
}
