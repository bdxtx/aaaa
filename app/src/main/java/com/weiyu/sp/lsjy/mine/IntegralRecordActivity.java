package com.weiyu.sp.lsjy.mine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseMvpActivity;
import com.weiyu.sp.lsjy.bean.AccountRecordBean;
import com.weiyu.sp.lsjy.bean.IntegralRecordBean;
import com.weiyu.sp.lsjy.mine.adapter.AccountRecordAdapter;
import com.weiyu.sp.lsjy.mine.adapter.IntegralRecordAdapter;
import com.weiyu.sp.lsjy.mine.adapter.RecordItemDecoration;
import com.weiyu.sp.lsjy.view.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class IntegralRecordActivity extends BaseMvpActivity<IntegralRecordPresenter> implements IntegralRecordContract.View, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    private int page=1;
    private IntegralRecordAdapter integralRecordAdapter;
    private LoadingDialog loadingDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_integral_record;
    }

    @Override
    public void initView() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        List<IntegralRecordBean>list=new ArrayList<>();
        integralRecordAdapter = new IntegralRecordAdapter(list);
        rv.addItemDecoration(new RecordItemDecoration());
        rv.setAdapter(integralRecordAdapter);
        integralRecordAdapter.setOnLoadMoreListener(this,rv);

        smart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                getData();
            }
        });
        getData();
    }
    private void getData(){
        Map<String,String>map=new HashMap<>();
        map.put("limit","10");
        map.put("page",page+"");
        mPresenter.integralRecordList(map);
    }

    @Override
    protected IntegralRecordPresenter setMPresenter() {
        IntegralRecordPresenter integralRecordPresenter=new IntegralRecordPresenter();
        integralRecordPresenter.attachView(this);
        return integralRecordPresenter;
    }

    @Override
    public void onGetList(List<IntegralRecordBean> beans) {
        smart.setVisibility(View.VISIBLE);
        smart.finishRefresh();
        if (page==1){
            integralRecordAdapter.setNewData(beans);
        }else {
            integralRecordAdapter.addData(beans);
        }
        integralRecordAdapter.loadMoreComplete();
    }

    @Override
    public void showLoading() {
        loadingDialog = LoadingDialog.newInstance();
        loadingDialog.show(getSupportFragmentManager(),"loading");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()&& loadingDialog.getDialog()!=null&&loadingDialog.getDialog().isShowing()){
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
    public void onError(Throwable throwable, int flag) {
        if (flag==0){
            smart.setVisibility(View.GONE);
            ll_no_data.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (mPresenter.loadMoreAble(page)){
            page++;
            getData();
        }else {
            integralRecordAdapter.loadMoreEnd();
        }
    }
    @OnClick({R.id.back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
