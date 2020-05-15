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
import com.weiyu.sp.lsjy.bean.IntegralRecordBean;
import com.weiyu.sp.lsjy.bean.OrderBean;
import com.weiyu.sp.lsjy.mine.adapter.IntegralRecordAdapter;
import com.weiyu.sp.lsjy.mine.adapter.OrderAdapter;
import com.weiyu.sp.lsjy.mine.adapter.RecordItemDecoration;
import com.weiyu.sp.lsjy.view.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderListActivity extends BaseMvpActivity<OrderListPresenter>implements OrderListContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    private int page=1;
    private OrderAdapter orderAdapter;
    private LoadingDialog loadingDialog;
    private List<OrderBean> beans;
    @BindView(R.id.ll_no_network)
    LinearLayout ll_no_network;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_list;
    }

    @Override
    public void initView() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        List<OrderBean>list=new ArrayList<>();
        orderAdapter = new OrderAdapter(list);
        rv.setAdapter(orderAdapter);
        orderAdapter.setOnLoadMoreListener(this,rv);

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
        Map<String,String> map=new HashMap<>();
        map.put("limit","10");
        map.put("page",page+"");
        mPresenter.getOrderList(map);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (hasToken()){
            if(beans==null){
                page=1;
                getData();
            }
        }else {
            finish();
        }
    }

    @Override
    protected OrderListPresenter setMPresenter() {
        OrderListPresenter orderListPresenter=new OrderListPresenter();
        orderListPresenter.attachView(this);
        return orderListPresenter;
    }

    @Override
    public void onGetList(List<OrderBean> beans) {
        ll_no_network.setVisibility(View.GONE);
        this.beans = beans;
        smart.setVisibility(View.VISIBLE);
        smart.finishRefresh();
        if (page==1){
            orderAdapter.setNewData(beans);
        }else {
            orderAdapter.addData(beans);
        }
        orderAdapter.loadMoreComplete();
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    public void onError(Throwable throwable, int flag) {
        if (flag==0){
            smart.setVisibility(View.GONE);
            ll_no_data.setVisibility(View.VISIBLE);
        }else if (flag==1){
            ll_no_network.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (mPresenter.loadMoreAble(page)){
            page++;
            getData();
        }else {
            orderAdapter.loadMoreEnd();
        }
    }

    @OnClick({R.id.back,R.id.tv_refresh})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tv_refresh:
                page=1;
                getData();
                break;
        }
    }
}
