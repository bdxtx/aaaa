package com.weiyu.sp.lsjy.test;

import android.util.Log;
import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.net.RxScheduler;


import java.util.Map;

import io.reactivex.functions.Consumer;

public class TestPresenter extends BasePresenter implements TestContract.Presenter {
    TestContract.Model model=new TestModel();

    @Override
    public void userLoginCode(Map<String, String> map) {
        if (isViewAttached()){
            model.userLoginCode(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<String>>() {
                        @Override
                        public void accept(BaseObjectBean<String> stringBaseObjectBean) throws Exception {
                            Log.i("csc", stringBaseObjectBean.getMessage());
                            //重新获取token

                            //再请求该方法
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.i("csc", "s" + throwable);
                        }
                    });
        }
    }
}
