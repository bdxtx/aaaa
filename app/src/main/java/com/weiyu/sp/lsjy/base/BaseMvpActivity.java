package com.weiyu.sp.lsjy.base;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.weiyu.sp.lsjy.view.dialog.LoadingDialog;

public abstract class BaseMvpActivity<T extends BasePresenter> extends BaseActivity implements BaseView {
    protected T mPresenter;
    protected LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter=setMPresenter();
        super.onCreate(savedInstanceState);
    }

    protected abstract T setMPresenter();

    @Override
    public void showLoading() {
        loadingDialog = LoadingDialog.newInstance();
        loadingDialog.show(getSupportFragmentManager(),"load");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog!=null){
                    loadingDialog.dismiss();
                }
            }
        },2000);
    }

    @Override
    public void hideLoading() {
        loadingDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }
    /**
     * 绑定生命周期 防止MVP内存泄漏
     *
     * @param <T>
     * @return
     */
    @Override
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
    }
}
