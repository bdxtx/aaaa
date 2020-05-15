package com.weiyu.sp.lsjy.splash;

import com.weiyu.sp.lsjy.base.BaseApplication;
import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.bean.LoginBean;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.SPUtils;

import io.reactivex.functions.Consumer;

public class SplashPresenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter{
    SplashContract.Model model;
    public SplashPresenter(){
        model=new SplashModel();
    }
    @Override
    public void refreshLogin(String refreshToken) {
        if (isViewAttached()){
            model.refreshLogin(refreshToken)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<LoginBean>>() {
                        @Override
                        public void accept(BaseObjectBean<LoginBean> loginBeanBaseObjectBean) throws Exception {
                            if (loginBeanBaseObjectBean.getCode()==200){
                                BaseApplication.getInstance().saveToken(loginBeanBaseObjectBean.getRows().getLoginToken());
                                SPUtils.saveData(Constant.REFRESH_LOGIN_TOKEN,loginBeanBaseObjectBean.getRows().getRefreshLoginToken());
                                long secondTime = System.currentTimeMillis();
                                SPUtils.saveData(Constant.RefreshTokenTime,secondTime);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
        }
    }
}
