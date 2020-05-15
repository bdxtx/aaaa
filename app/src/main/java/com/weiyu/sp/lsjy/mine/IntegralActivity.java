package com.weiyu.sp.lsjy.mine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseMvpActivity;
import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.IntegralBean;
import com.weiyu.sp.lsjy.bean.IntegralExchangeBean;
import com.weiyu.sp.lsjy.bean.IntegralListBean;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.mine.adapter.GoodsItemDecoration;
import com.weiyu.sp.lsjy.mine.adapter.IntegralAdapter;
import com.weiyu.sp.lsjy.utils.GlideRoundImage;
import com.weiyu.sp.lsjy.utils.ToastUtil;
import com.weiyu.sp.lsjy.view.dialog.IntegralExchangeDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.OnClick;

public class IntegralActivity extends BaseMvpActivity<IntegralPresenter> implements IntegralContract.View, BaseQuickAdapter.RequestLoadMoreListener,IntegralExchangeDialog.Exchange {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.no_integral)
    LinearLayout no_integral;
    @BindView(R.id.content)
    LinearLayout content;

    @BindView(R.id.ll_no_network)
    LinearLayout ll_no_network;

    private ImageView img_header;
    private TextView phone;
    private TextView tv_integral;
    private int page=1;
    private IntegralAdapter integralAdapter;
    private IntegralBean integralBean;
    private IntegralListBean currentBean;
    private IntegralExchangeDialog integralExchangeDialog;
    private ImageView img_ad;

    @Override
    public int getLayoutId() {
        return R.layout.activity_integral;
    }

    @Override
    public void initView() {
        smart.autoRefresh();
        List<IntegralListBean>listBeans=new ArrayList<>();
        integralAdapter = new IntegralAdapter(listBeans);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(integralAdapter);
        rv.addItemDecoration(new GoodsItemDecoration());
        addHead();
//        mPresenter.selectIntegralInfo();
//        getData();
        smart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                mPresenter.selectIntegralInfo();
                getData();
                smart.finishRefresh(1000);
            }
        });
        integralAdapter.setOnLoadMoreListener(this,rv);
        integralAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                currentBean= (IntegralListBean) adapter.getData().get(position);
                if (view.getId()==R.id.exchange){
                    if (integralBean!=null){
                        if (Integer.parseInt(integralBean.getIntegral())<Integer.parseInt(currentBean.getPrice())){
                            ToastUtil.show("积分不够哦，兑换别的吧");
                            return;
                        }
                        integralExchangeDialog = IntegralExchangeDialog.newInstance(integralBean.getUname(),integralBean.getPhone(),integralBean.getAddress(),currentBean.getGoodsName(),currentBean.getPrice());
                        integralExchangeDialog.show(getSupportFragmentManager(),"ggg");
                    }
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (hasToken()){
            if(integralBean==null){
                page=1;
                mPresenter.selectIntegralInfo();
                getData();
            }
        }else {
            finish();
        }
    }

    private void addHead(){
        View view= getLayoutInflater().inflate(R.layout.integral_head,null);
        img_header = view.findViewById(R.id.img_header);
        phone = view.findViewById(R.id.phone);
        tv_integral = view.findViewById(R.id.tv_integral);
        integralAdapter.addHeaderView(view);
        img_ad = view.findViewById(R.id.img_ad);

    }
    private void getData(){
        Map<String,String>map=new HashMap<>();
        map.put("limit","10");
        map.put("page",page+"");
        mPresenter.selectGoods(map);
    }

    @Override
    protected IntegralPresenter setMPresenter() {
        IntegralPresenter integralPresenter=new IntegralPresenter();
        integralPresenter.attachView(this);
        return integralPresenter;
    }

    @Override
    public void onGetTop(IntegralBean bean) {
        content.setVisibility(View.VISIBLE);
        integralBean=bean;
        Glide.with(this).load(bean.getUrl()).apply(new RequestOptions().placeholder(R.drawable.header_default)
                .error(R.drawable.header_default)
                .fallback(R.drawable.header_default).circleCrop()).into(img_header);
        String name=bean.getLoginName();
        phone.setText(name.substring(0,3)+"****"+name.substring(7));
        tv_integral.setText("积分余额："+bean.getIntegral());
        RequestOptions options=new RequestOptions();
        GlideRoundImage glideRoundImage=new GlideRoundImage(10);
        options.centerCrop().transform(glideRoundImage);
        Glide.with(this).load(bean.getAdUrl()).apply(options).into(img_ad);
    }

    @Override
    public void onGetList(List<IntegralListBean> beanList) {
        content.setVisibility(View.VISIBLE);
        smart.finishRefresh();
        integralAdapter.loadMoreComplete();
        if (page==1){
            integralAdapter.setNewData(beanList);
        }else {
            integralAdapter.addData(beanList);
        }
    }

    @Override
    public void onChange(BaseObjectBean<IntegralExchangeBean> bean) {
        ll_no_network.setVisibility(View.GONE);
        if (bean.getCode()==200){
            if (integralExchangeDialog!=null&&integralExchangeDialog.getDialog()!=null&&integralExchangeDialog.getDialog().isShowing())
            integralExchangeDialog.dismiss();
            page=1;
            getData();
            mPresenter.selectIntegralInfo();
        }
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
            no_integral.setVisibility(View.VISIBLE);
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
            integralAdapter.loadMoreEnd();
        }
    }
    @OnClick({R.id.back,R.id.to_record,R.id.tv_refresh})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.to_record:
                startActivity(new Intent(this,IntegralRecordActivity.class));
                break;
            case R.id.tv_refresh:
                page=1;
                mPresenter.selectIntegralInfo();
                getData();
                break;
        }
    }

    @Override
    public void exchange(String name, String phone, String addr) {
        Map<String,String>map=new HashMap<>();
        map.put("goodId",currentBean.getId());
        map.put("address",addr);
        map.put("phone",phone);
        map.put("uname",name);
        mPresenter.exchangeGoods(map);
    }
}
