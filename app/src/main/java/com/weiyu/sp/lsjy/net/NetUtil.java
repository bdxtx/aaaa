package com.weiyu.sp.lsjy.net;

import com.weiyu.sp.lsjy.base.BaseView;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NetUtil {

//    public static <T> ObservableTransformer<T, T> applySchedulers(final BaseView view) {
//        return new ObservableTransformer<T, T>() {
//            @Override
//            public Observable<T> apply(Observable<T> observable) {
//                return observable.subscribeOn(Schedulers.io())
//                        .doOnSubscribe(new Consumer<Disposable>() {
//                            @Override
//                            public void accept(@NonNull Disposable disposable) throws Exception {
//                                if (view != null) {
//                                    view.showLoading();//显示进度条
//                                }
//                            }
//                        })
//                        .subscribeOn(AndroidSchedulers.mainThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .doFinally(new Action() {
//                            @Override
//                            public void run() {
//                                if (view != null) {
//                                    view.hideLoading();//隐藏进度条
//                                }
//                            }
//                        });
//            }
//        };
//    }

}
