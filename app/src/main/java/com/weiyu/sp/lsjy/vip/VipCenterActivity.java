package com.weiyu.sp.lsjy.vip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseApplication;
import com.weiyu.sp.lsjy.base.BaseMvpActivity;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.CardType;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.bean.PayChannel;
import com.weiyu.sp.lsjy.bean.PayResult;
import com.weiyu.sp.lsjy.bean.PayResultBean;
import com.weiyu.sp.lsjy.bean.VipBean;
import com.weiyu.sp.lsjy.bean.VipCardBean;
import com.weiyu.sp.lsjy.bean.WxPayBean;
import com.weiyu.sp.lsjy.course.CourseDetailActivity;
import com.weiyu.sp.lsjy.utils.ToastUtil;
import com.weiyu.sp.lsjy.view.dialog.ToBeVipDialog;
import com.weiyu.sp.lsjy.view.dialog.VipTypeSelectDialog;
import com.weiyu.sp.lsjy.vip.adapter.CardItemDecoration;
import com.weiyu.sp.lsjy.vip.adapter.ChannelItemDecoration;
import com.weiyu.sp.lsjy.vip.adapter.PayChannelAdapter;
import com.weiyu.sp.lsjy.vip.adapter.VipCardAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class VipCenterActivity extends BaseMvpActivity<VipCenterPresenter>implements VipCenterContract.View {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.rv_channel)
    RecyclerView rv_channel;
    @BindView(R.id.ll_teacher)
    LinearLayout ll_teacher;
    @BindView(R.id.ll_dx)
    LinearLayout ll_dx;
    @BindView(R.id.ll_xy)
    LinearLayout ll_xy;
    private VipCardAdapter vipCardAdapter;
    private PayChannelAdapter payChannelAdapter;
    private static final int SDK_PAY_FLAG = 1;

    private CardType cardType;
    private PayChannel payChannel;
    private String courseId;
    private String from;
    private PayResultBean mPayResultBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_vip_center;
    }

    @Override
    public void initView() {
        courseId = getIntent().getStringExtra("courseId");
        from = getIntent().getStringExtra("from");
        EventBus.getDefault().register(this);
        mPresenter.selectPayChannelList();
        List<CardType>list=new ArrayList<>();
        vipCardAdapter = new VipCardAdapter(list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rv.addItemDecoration(new CardItemDecoration());
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(vipCardAdapter);

        List<PayChannel>list1=new ArrayList<>();
        payChannelAdapter = new PayChannelAdapter(list1);
        LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(this);
        rv_channel.addItemDecoration(new ChannelItemDecoration());
        rv_channel.setLayoutManager(linearLayoutManager2);
        rv_channel.setAdapter(payChannelAdapter);

        vipCardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<CardType>cardTypeList=vipCardAdapter.getData();
                for (CardType cardType:cardTypeList){
                    if (cardType.getVipTypeCode().equals(cardTypeList.get(position).getVipTypeCode())){
                        cardType.setSelect(true);
                        VipCenterActivity.this.cardType=cardType;
                    }else {
                        cardType.setSelect(false);
                    }
                }
                vipCardAdapter.notifyDataSetChanged();
                if (VipCenterActivity.this.cardType.getVipTypeCode().equals("800")){
                    ll_teacher.setVisibility(View.VISIBLE);
                    ll_dx.setVisibility(View.GONE);
                    ll_xy.setVisibility(View.GONE);
                }else if (VipCenterActivity.this.cardType.getVipTypeCode().equals("801")){
                    ll_teacher.setVisibility(View.GONE);
                    ll_dx.setVisibility(View.VISIBLE);
                    ll_xy.setVisibility(View.GONE);
                }else if (VipCenterActivity.this.cardType.getVipTypeCode().equals("802")){
                    ll_teacher.setVisibility(View.GONE);
                    ll_dx.setVisibility(View.GONE);
                    ll_xy.setVisibility(View.VISIBLE);
                }

            }
        });
        payChannelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<PayChannel>payChannelList=payChannelAdapter.getData();
                for (PayChannel payChannel:payChannelList){
                    if (payChannel.getPaymentCode().equals(payChannelList.get(position).getPaymentCode())){
                        VipCenterActivity.this.payChannel=payChannel;
                        payChannel.setSelect(true);
                    }else {
                        payChannel.setSelect(false);
                    }
                }
                payChannelAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected VipCenterPresenter setMPresenter() {
        VipCenterPresenter vipCenterPresenter=new VipCenterPresenter();
        vipCenterPresenter.attachView(this);
        return vipCenterPresenter;
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

    }

    @Override
    public void onGetChannel(VipBean vipBean) {
        List<CardType>cardTypeList=vipBean.getCard();
        if (cardTypeList.size()>1){
            cardTypeList.get(1).setSelect(true);
            this.cardType=cardTypeList.get(1);
            if (VipCenterActivity.this.cardType.getVipTypeCode().equals("800")){
                ll_teacher.setVisibility(View.VISIBLE);
                ll_dx.setVisibility(View.GONE);
                ll_xy.setVisibility(View.GONE);
            }else if (VipCenterActivity.this.cardType.getVipTypeCode().equals("801")){
                ll_teacher.setVisibility(View.GONE);
                ll_dx.setVisibility(View.VISIBLE);
                ll_xy.setVisibility(View.GONE);
            }else if (VipCenterActivity.this.cardType.getVipTypeCode().equals("802")){
                ll_teacher.setVisibility(View.GONE);
                ll_dx.setVisibility(View.GONE);
                ll_xy.setVisibility(View.VISIBLE);
            }
        }
        vipCardAdapter.setNewData(vipBean.getCard());
        payChannelAdapter.setNewData(vipBean.getPay());
        for (PayChannel payChannel:vipBean.getPay()){
            if (payChannel.isSelect()){
                this.payChannel=payChannel;
            }
        }
    }

    @Override
    public void onBuyVip(PayResultBean payResultBean) {
        mPayResultBean = payResultBean;
        if ("3000".equals(payChannel.getPaymentCode())){
            doAliPayResult(payResultBean.getAlibody());
        }else if ("3001".equals(payChannel.getPaymentCode())){
            doWxPay(payResultBean.getWxbody());
        }else {
            ToastUtil.show("暂未开通此支付方式");
        }
    }

    @Override
    public void onActiveVip() {
        ToastUtil.show("激活成功");
        if ("detail".equals(from)){
            Intent intent=new Intent(this, CourseDetailActivity.class);
            intent.putExtra("courseId",courseId);
            startActivity(intent);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @OnClick({R.id.pay_vip,R.id.back,R.id.active})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.pay_vip:
                if (cardType!=null){
                    if (cardType.getVipTypeCode().equals("802")){
                        VipTypeSelectDialog vipTypeSelectDialog=new VipTypeSelectDialog();
                        vipTypeSelectDialog.show(getSupportFragmentManager(),"vip");
                    }else {
                        if (payChannel!=null){
                            Map<String,String>map=new HashMap<>();
                            map.put("subjectCode","");
                            map.put("vipTypeCode",cardType.getVipTypeCode());
                            map.put("paymentCode",payChannel.getPaymentCode());
                            mPresenter.buyVip(map);
                        }else {
                            ToastUtil.show("请选择付款方式");
                        }

                    }
                }else {
                    ToastUtil.show("请选择会员卡");
                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.active:
                ToBeVipDialog toBeVipDialog=new ToBeVipDialog();
                toBeVipDialog.show(getSupportFragmentManager(),"tobeVip");
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        gotoOrderPaySuccessActivity();
//                        ToastUtil.show(GiftOrderActivity.this,"支付成功");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        ToastUtil.show(GiftOrderActivity.this,"支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    private void doAliPayResult(String string){
        Runnable payRunnable = new Runnable() {//要在子线程
            @Override
            public void run() {
                PayTask alipay = new PayTask(VipCenterActivity.this);
                Map<String, String> result = alipay.payV2(string, true);// orderInfo加签后的订单信息
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    private void doWxPay(WxPayBean wxPayBean){
        PayReq pRequest = new PayReq();
        pRequest.appId = Constant.APP_ID;
        pRequest.partnerId = wxPayBean.getPartnerId();
        pRequest.prepayId = wxPayBean.getPrepayId();
        pRequest.packageValue = wxPayBean.getPackageValue();
        pRequest.nonceStr = wxPayBean.getNonceStr();
        pRequest.timeStamp = wxPayBean.getTimeStamp();
        pRequest.sign = wxPayBean.getPaySign();
//        pRequest.signType = wxPayBean.getSignType();
        if (!BaseApplication.getInstance().getApi().sendReq(pRequest)){
            ToastUtil.show("微信支付异常,请使用其它方式或稍后再试");
        }
        Log.i("csc", JSON.toJSONString(pRequest));
    }
//    private void doWxPay(WxPayBean wxPayBean){
//        PayReq pRequest = new PayReq();
//        pRequest.appId = "wx8483a828cccbb532";
//        pRequest.partnerId = "14698sdfs402dsfdew402";
//        pRequest.prepayId = "wx09182912998440438f9287f91388952500";
//        pRequest.packageValue = "Sign=WXPay";
//        pRequest.nonceStr =  "787afca6b6dd1f06fc22e4b52b0b89bf";
//        pRequest.timeStamp = "1589020153";
//        pRequest.sign = "F4B58A8C25D1AF0D54779FB9EB4DACD6";
//        boolean flag=BaseApplication.getInstance().getApi().sendReq(pRequest);
//        Log.i("csc","flag="+flag);
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (EventBusTag.VipSelectToActivity.equals(messageEvent.getTag())){
            CourseBean courseBean= (CourseBean) messageEvent.getMessage();
            if (courseBean!=null){
                if (payChannel!=null){
                    Map<String,String>map=new HashMap<>();
                    map.put("subjectCode",courseBean.getSubjectCode()==0?"":courseBean.getSubjectCode()+"");
                    map.put("vipTypeCode","802");
                    map.put("schooleYear",courseBean.getSchoolYear());
                    map.put("paymentCode",payChannel.getPaymentCode());
                    mPresenter.buyVip(map);
                }else {
                    ToastUtil.show("请选择付款方式");
                }
            }
        }else if (EventBusTag.ActiveVip.equals(messageEvent.getTag())){
            String code= (String) messageEvent.getMessage();
            mPresenter.activeVip(code);
        }else if (EventBusTag.WX_PAY.equals(messageEvent.getTag())){
            gotoOrderPaySuccessActivity();
        }
    }

    private void gotoOrderPaySuccessActivity(){
        Intent intent=new Intent(VipCenterActivity.this,VipSuccessActivity.class);
        intent.putExtra("from","vip");
        intent.putExtra("courseId",courseId);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mPayResultBean==null){
            finish();
        }
    }
}
