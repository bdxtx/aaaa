package com.weiyu.sp.lsjy.other;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.bean.ShareBean;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import io.reactivex.functions.Consumer;

public class SharePresenter extends BasePresenter<ShareContract.View> implements ShareContract.Presenter {
    ShareContract.Model model;
    public SharePresenter(){
        model=new ShareModel();
    }
    @Override
    public void appShare() {
        if (isViewAttached()){
            model.appShare()
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<ShareBean>>() {
                        @Override
                        public void accept(BaseObjectBean<ShareBean> shareBeanBaseObjectBean) throws Exception {
                            if (shareBeanBaseObjectBean.getCode()==200){
                                mView.onSuccess(shareBeanBaseObjectBean.getRows());
                            }else if (shareBeanBaseObjectBean.getCode()==600||shareBeanBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(shareBeanBaseObjectBean.getMessage());
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
