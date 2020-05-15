package com.weiyu.sp.lsjy.other;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.bean.ShareBean;

import java.util.Map;

import io.reactivex.Flowable;

public class ShareContract {
    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, int flag);

        void onSuccess(ShareBean shareBean);

    }
    interface Model {
        Flowable<BaseObjectBean<ShareBean>> appShare();
    }

    interface Presenter{
        void appShare();
    }
}
