package com.weiyu.sp.lsjy.login;

import android.util.Log;
import android.widget.Toast;

import com.weiyu.sp.lsjy.base.BaseApplication;
import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.bean.LoginBean;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.SPUtils;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import java.util.Map;

import io.reactivex.functions.Consumer;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    LoginContract.Model model=new LoginModel();


    @Override
    public void sendMsg(String loginName) {
        if (isViewAttached()){
            model.sendMsg(loginName)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<String>>() {
                        @Override
                        public void accept(BaseObjectBean<String> stringBaseObjectBean) throws Exception {
                            if (stringBaseObjectBean.getCode()==-1){
                                ToastUtil.show(stringBaseObjectBean.getMessage());
                            }else {
                                ToastUtil.show("短信发送成功");
                            }
                            mView.onSuccess(stringBaseObjectBean.getRows());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
        }
    }

    @Override
    public void doLogin(Map<String, String> map) {
        if (isViewAttached()){
            mView.showLoading();
            model.doLogin(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<LoginBean>>() {
                        @Override
                        public void accept(BaseObjectBean<LoginBean> loginBeanBaseObjectBean) throws Exception {
                            if (loginBeanBaseObjectBean.getCode()==200){
                                BaseApplication.getInstance().saveToken(loginBeanBaseObjectBean.getRows().getLoginToken());
                                SPUtils.saveData(Constant.REFRESH_LOGIN_TOKEN,loginBeanBaseObjectBean.getRows().getRefreshLoginToken());
                                mView.onLoginSuccess();
                            }else {
                                ToastUtil.show(loginBeanBaseObjectBean.getMessage());
                            }
                            mView.hideLoading();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mView.hideLoading();
                        }
                    });
        }
    }
}
