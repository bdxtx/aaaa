package com.weiyu.sp.lsjy.mine;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseMvpFragment;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.AccountRecordBean;
import com.weiyu.sp.lsjy.bean.BalanceBean;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.mine.adapter.AccountRecordAdapter;
import com.weiyu.sp.lsjy.mine.adapter.RecordItemDecoration;
import com.weiyu.sp.lsjy.view.dialog.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAccountFragment extends BaseMvpFragment<MyAccountPresenter>implements MyAccountContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    private AccountRecordAdapter accountRecordAdapter;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    private int page=1;
    private boolean redFlag;
    private LoadingDialog loadingDialog;

    public static MyAccountFragment newInstance(boolean redFlag) {
        MyAccountFragment fragment = new MyAccountFragment();
        Bundle args = new Bundle();
        args.putBoolean("redFlag",redFlag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected MyAccountPresenter setMPresenter() {
        MyAccountPresenter myAccountPresenter=new MyAccountPresenter();
        myAccountPresenter.attachView(this);
        return myAccountPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_account;
    }

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        Bundle bundle=getArguments();
        redFlag = bundle.getBoolean("redFlag");
        List<AccountRecordBean>list=new ArrayList<>();
        accountRecordAdapter = new AccountRecordAdapter(list, redFlag);
        rv.addItemDecoration(new RecordItemDecoration());
        rv.setAdapter(accountRecordAdapter);
        accountRecordAdapter.setOnLoadMoreListener(this,rv);

        smart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                getData();
                smart.finishRefresh(2000);
            }
        });
        getData();
    }

    private void getData(){
        Map<String,String>map=new HashMap<>();
        map.put("limit","10");
        map.put("page",page+"");
        if (redFlag){
            map.put("payState","592");
        }else {
            map.put("payState","591");
        }
        mPresenter.getAccountRecordDetail(map);
    }

    @Override
    public void onSuccess(BalanceBean bean) {

    }

    @Override
    public void onGetDetail(List<AccountRecordBean> beanList) {
        smart.finishRefresh();
        if (page==1){
            accountRecordAdapter.setNewData(beanList);
        }else {
            accountRecordAdapter.addData(beanList);
        }
        accountRecordAdapter.loadMoreComplete();
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
            ll_no_data.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (mPresenter.loadMoreAble(page)){
            page++;
            getData();
        }else {
            accountRecordAdapter.loadMoreEnd();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (EventBusTag.MyAccountRefresh.equals(messageEvent.getTag())){
            page=1;
            getData();
        }
    }
}
