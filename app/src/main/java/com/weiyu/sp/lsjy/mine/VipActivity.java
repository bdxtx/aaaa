package com.weiyu.sp.lsjy.mine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseMvpActivity;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.bean.VipCardBean;
import com.weiyu.sp.lsjy.course.CourseDetailActivity;
import com.weiyu.sp.lsjy.other.ShareActivity;
import com.weiyu.sp.lsjy.vip.VipCenterActivity;
import com.weiyu.sp.lsjy.vip.VipCenterPresenter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class VipActivity extends BaseMvpActivity<VipPresenter>implements VipContract.View {

    @BindView(R.id.fl_vip)
    FrameLayout fl_vip;
    @BindView(R.id.fl_vip_un)
    FrameLayout fl_vip_un;
    @BindView(R.id.tv_buy)
    TextView tv_buy;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.vip_desc)
    LinearLayout vip_desc;
    @BindView(R.id.ll_teacher)
    LinearLayout ll_teacher;
    @BindView(R.id.ll_dx)
    LinearLayout ll_dx;
    @BindView(R.id.ll_xy)
    LinearLayout ll_xy;
    @BindView(R.id.ll_no_network)
    LinearLayout ll_no_network;
    private VipCardBean mBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    public void initView() {
//        Intent intent=getIntent();
//        boolean isVip=intent.getBooleanExtra("isVip",false);
        mPresenter.getMemberCenter();
//        if (isVip){
//            vip_desc.setVisibility(View.GONE);
//            fl_vip.setVisibility(View.VISIBLE);
//            fl_vip_un.setVisibility(View.GONE);
//            tv_buy.setText("立即续费");
//            tv_buy.setVisibility(View.GONE);
//        }else {
//            vip_desc.setVisibility(View.VISIBLE);
//            fl_vip.setVisibility(View.GONE);
//            fl_vip_un.setVisibility(View.VISIBLE);
//            tv_buy.setText("立即购买");
//        }
    }

    @Override
    protected VipPresenter setMPresenter() {
        VipPresenter vipPresenter=new VipPresenter();
        vipPresenter.attachView(this);
        return vipPresenter;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (hasToken()){
            if(mBean==null){
                mPresenter.getMemberCenter();
            }
        }else {
            finish();
        }
    }

    @Override
    public void onGetSuccess(VipCardBean bean) {
        ll_no_network.setVisibility(View.GONE);
        mBean = bean;
        date.setText(bean.getBeginDate()+"至"+bean.getEndDate());
        if (!TextUtils.isEmpty(bean.getBeginDate())){
            fl_vip.setVisibility(View.VISIBLE);
            fl_vip_un.setVisibility(View.GONE);
            tv_buy.setText("立即续费");
            tv_buy.setVisibility(View.GONE);
            tv_name.setText(bean.getCardName());
            vip_desc.setVisibility(View.GONE);
            if ("800".equals(bean.getCardCode())){
                ll_teacher.setVisibility(View.VISIBLE);
                ll_dx.setVisibility(View.GONE);
                ll_xy.setVisibility(View.GONE);
            }else if ("801".equals(bean.getCardCode())){
                ll_teacher.setVisibility(View.GONE);
                ll_dx.setVisibility(View.VISIBLE);
                ll_xy.setVisibility(View.GONE);
            }else if ("802".equals(bean.getCardCode())){
                ll_teacher.setVisibility(View.GONE);
                ll_dx.setVisibility(View.GONE);
                ll_xy.setVisibility(View.VISIBLE);
            }
        }else {
            fl_vip.setVisibility(View.GONE);
            fl_vip_un.setVisibility(View.VISIBLE);
            vip_desc.setVisibility(View.VISIBLE);
            tv_buy.setVisibility(View.VISIBLE);
            tv_buy.setText("立即购买");
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
        ll_no_network.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.back,R.id.tv_buy,R.id.tv_refresh,R.id.tv_share})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                EventBus.getDefault().post(new MessageEvent("", EventBusTag.MainRefresh));
                finish();
                break;
            case R.id.tv_buy:
                if (tv_buy.getText().toString().equals("立即购买")){
                    startActivity(new Intent(this, VipCenterActivity.class));
                }else {

                }
                break;
            case R.id.tv_refresh:
                mPresenter.getMemberCenter();
                break;
            case R.id.tv_share:
                startActivity(new Intent(VipActivity.this, ShareActivity.class));
                break;
        }
    }


}
