package com.weiyu.sp.lsjy.mine;

import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseApplication;
import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.bean.PersonalBean;
import com.weiyu.sp.lsjy.net.RequestUtil;
import com.weiyu.sp.lsjy.net.RetrofitClient;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PersonalCenterPresenter extends BasePresenter<PersonalCenterContract.View> implements PersonalCenterContract.Presenter {
    PersonalCenterContract.Model personalCenterModel;
    private OkHttpClient.Builder httpBuilder;
    private OkHttpClient okHttpClient;
    public PersonalCenterPresenter(){
        personalCenterModel=new PersonalCenterModel();
    }

    /**
     * 上传图片
     */
    public void uploadImage(String videoPath) {
        if (httpBuilder == null) {
            httpBuilder = new OkHttpClient.Builder();
            okHttpClient = httpBuilder
                    .connectTimeout(1000, TimeUnit.SECONDS)          // 设置请求超时时间
                    .writeTimeout(1000, TimeUnit.SECONDS)
                    .readTimeout(1000, TimeUnit.SECONDS)
                    .build();
        }
        try {
            okHttpClient.newCall(RequestUtil.uploadImg(videoPath)).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    mView.onError(e,0);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String result=response.body().string();
                        BaseObjectBean<String>bean=new Gson().fromJson(result,new TypeToken<BaseObjectBean<String>>(){}.getType());
                        if (bean!=null){
                            if (RetrofitClient.getInstance().isTokenExpired(result,"")){
                                //同步请求方式，获取最新的Token
                                Log.e("csc",bean.getMessage());
                                String newSession = RetrofitClient.getInstance().getNewToken();
                               uploadImage(videoPath);
                            }else {
                                mView.updateHeader(bean.getRows());
                            }
                        }else {
                            mView.onError(new Throwable(),0);
                        }
                    } catch (Exception e) {
                        Log.i("csc","e="+e);
                    }
                }
            });
        } catch (Exception e) {
            Log.e("csc","e="+e);
        }

    }

    @Override
    public void selectUserDetail(Map<String, String> map) {
        if (isViewAttached()){
            mView.showLoading();
            personalCenterModel.selectUserDetail(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<PersonalBean>>() {
                        @Override
                        public void accept(BaseObjectBean<PersonalBean> stringBaseObjectBean) throws Exception {
                            if (stringBaseObjectBean.getCode()==200){
                                mView.userDetail(stringBaseObjectBean.getRows());
                            }else if (stringBaseObjectBean.getCode()==600||stringBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(stringBaseObjectBean.getMessage());
                            }
                            mView.hideLoading();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mView.hideLoading();
                            mView.onError(throwable,1);
                        }
                    });
        }
    }

    @Override
    public void updateUserDetail(Map<String, String> map) {
        if (isViewAttached()){
            mView.showLoading();
            personalCenterModel.selectUserDetail(map)
                    .compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<PersonalBean>>() {
                        @Override
                        public void accept(BaseObjectBean<PersonalBean> stringBaseObjectBean) throws Exception {
                            if (stringBaseObjectBean.getCode()==200){
                                mView.userDetail(stringBaseObjectBean.getRows());
                            }else if (stringBaseObjectBean.getCode()==600||stringBaseObjectBean.getCode()==700){

                            }else {
                                ToastUtil.show(stringBaseObjectBean.getMessage());
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

    @Override
    public void loginOut(String refreshToken) {
        if (isViewAttached()){
            mView.showLoading();
            personalCenterModel.loginOut(refreshToken).compose(RxScheduler.Flo_io_main())
                    .as(mView.bindAutoDispose())
                    .subscribe(new Consumer<BaseObjectBean<String>>() {
                        @Override
                        public void accept(BaseObjectBean<String> stringBaseObjectBean) throws Exception {
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
