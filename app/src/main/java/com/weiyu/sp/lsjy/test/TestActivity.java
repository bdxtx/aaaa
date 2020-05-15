package com.weiyu.sp.lsjy.test;

import android.os.Bundle;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseMvpActivity;

import java.util.HashMap;
import java.util.Map;

public class TestActivity extends BaseMvpActivity<TestPresenter> implements TestContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected TestPresenter setMPresenter() {
        TestPresenter mainPresenter=new TestPresenter();
        mainPresenter.attachView(this);
        return mainPresenter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        Map<String ,String> params=new HashMap<String,String>();
        params.put("firmType","华为 P30 Pro");
        params.put("lckSystemEdition","V1.0.0");
        params.put("loginName","15158822934");
        params.put("newCode","123456");
        params.put("phoneType","Android");
        params.put("resolution","2340 x 1080");
        params.put("systemOs","9.1.0.210");
        mPresenter.userLoginCode(params);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable throwable, int flag) {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onUserLoginCodeSuccess() {

    }
}
