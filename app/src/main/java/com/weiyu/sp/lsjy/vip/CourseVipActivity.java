package com.weiyu.sp.lsjy.vip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseApplication;
import com.weiyu.sp.lsjy.base.BaseMvpActivity;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.CourseVipBean;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.bean.PayChannel;
import com.weiyu.sp.lsjy.bean.PayResult;
import com.weiyu.sp.lsjy.bean.PayResultBean;
import com.weiyu.sp.lsjy.bean.WxPayBean;
import com.weiyu.sp.lsjy.utils.GlideRoundImage;
import com.weiyu.sp.lsjy.utils.ToastUtil;
import com.weiyu.sp.lsjy.view.dialog.VipTypeSelectDialog;
import com.weiyu.sp.lsjy.vip.adapter.ChannelItemDecoration;
import com.weiyu.sp.lsjy.vip.adapter.PayChannelAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class CourseVipActivity extends BaseMvpActivity<CourseVipPresenter>implements CourseVipContract.View {
    @BindView(R.id.rv_channel)
    RecyclerView rv_channel;
    @BindView(R.id.img_class)
    ImageView img_class;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.subject)
    TextView subject;
    @BindView(R.id.subjectName)
    TextView subjectName;
    @BindView(R.id.schoolYear)
    TextView schoolYear;
    @BindView(R.id.booksNumber)
    TextView booksNumber;
    @BindView(R.id.curriculum)
    TextView curriculum;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.read_num)
    TextView read_num;
    @BindView(R.id.pay_vip)
    TextView pay_vip;
    @BindView(R.id.after_book)
    TextView after_book;
    @BindView(R.id.after_subject)
    TextView after_subject;
    @BindView(R.id.after_school)
    TextView after_school;
    @BindView(R.id.img_like)
    ImageView img_like;
    @BindView(R.id.img)
    ImageView img;


    private PayChannelAdapter payChannelAdapter;
    private String courseId;
    private  PayChannel payChannel;
    private CourseBean mCourseBean;
    private PayResultBean mPayResultBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_course_vip;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        Intent intent=getIntent();
        courseId = intent.getStringExtra("courseId");
        if (!TextUtils.isEmpty(courseId)){
            Map<String,String>map=new HashMap<>();
            map.put("courseId", courseId);
            mPresenter.buyCourseDetail(map);
        }

        List<PayChannel> list1=new ArrayList<>();
        payChannelAdapter = new PayChannelAdapter(list1);
        LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(this);
        rv_channel.addItemDecoration(new ChannelItemDecoration());
        rv_channel.setLayoutManager(linearLayoutManager2);
        rv_channel.setAdapter(payChannelAdapter);

        payChannelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<PayChannel>payChannelList=payChannelAdapter.getData();
                for (PayChannel payChannel:payChannelList){
                    if (payChannel.getPaymentCode().equals(payChannelList.get(position).getPaymentCode())){
                        CourseVipActivity.this.payChannel=payChannel;
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
    protected CourseVipPresenter setMPresenter() {
        CourseVipPresenter courseVipPresenter=new CourseVipPresenter();
        courseVipPresenter.attachView(this);
        return courseVipPresenter;
    }

    @Override
    public void onError(Throwable throwable, int flag) {

    }

    @Override
    public void onGetChannel(CourseVipBean courseVipBean) {
        if (courseVipBean.getCourse()!=null){
            mCourseBean = courseVipBean.getCourse();
            title.setText(courseVipBean.getCourse().getTitle()+"");
            read_num.setText(courseVipBean.getCourse().getReadNumber()+"人学过");
            price.setText("￥"+courseVipBean.getCourse().getMinPrice());
            booksNumber.setText(courseVipBean.getCourse().getBooksNumber());
            curriculum.setText(courseVipBean.getCourse().getCurriculum());
            if (TextUtils.isEmpty(mCourseBean.getSubjectName())){
                subjectName.setVisibility(View.GONE);
                after_subject.setVisibility(View.GONE);
            }else {
                subjectName.setVisibility(View.VISIBLE);
                after_subject.setVisibility(View.VISIBLE);
            }
            subjectName.setText(mCourseBean.getSubjectName());
            if (TextUtils.isEmpty(mCourseBean.getSchoolYear())){
                schoolYear.setVisibility(View.GONE);
                after_school.setVisibility(View.GONE);
            }else {
                schoolYear.setVisibility(View.VISIBLE);
                after_school.setVisibility(View.VISIBLE);
            }
            schoolYear.setText(mCourseBean.getSchoolYear());
            if (TextUtils.isEmpty(mCourseBean.getBooksNumber())){
                booksNumber.setVisibility(View.GONE);
                after_book.setVisibility(View.GONE);
            }else {
                booksNumber.setVisibility(View.VISIBLE);
                after_book.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(mCourseBean.getCurriculum())){
                if (after_book.getVisibility()==View.VISIBLE){
                    after_book.setVisibility(View.GONE);
                }else if (after_school.getVisibility()==View.VISIBLE){
                    after_school.setVisibility(View.GONE);
                }else if (after_subject.getVisibility()==View.VISIBLE){
                    after_subject.setVisibility(View.GONE);
                }
            }
            RequestOptions options=new RequestOptions();
            GlideRoundImage glideRoundImage=new GlideRoundImage(5);
            options.transform(glideRoundImage);
            options.fallback(R.drawable.banner_default).error(R.drawable.banner_default).placeholder(R.drawable.banner_default);
            options.transform(new MultiTransformation<>(new CenterCrop(),glideRoundImage));
            Glide.with(this).load(courseVipBean.getCourse().getUrl()).apply(options).into(img_class);
//        helper.setText(R.id.title,"螺蛳"+item.getSubjectName()+"-"+item.getSchoolYear()+item.getBooksNumber()+"-"+item.getCurriculum()+"-"+item.getSubject());
            subject.setText("《"+courseVipBean.getCourse().getSubject()+"》");
            if (courseVipBean.getCourse().isCollection()){
                Glide.with(this).load(R.drawable.like).into(img_like);
            }else {
                Glide.with(this).load(R.drawable.like_un).into(img_like);
            }
        }

        for (PayChannel payChannel:courseVipBean.getPay()){
            if (payChannel.isSelect()){
                this.payChannel=payChannel;
            }
        }

        payChannelAdapter.setNewData(courseVipBean.getPay());
        pay_vip.setText("支付"+courseVipBean.getCourse().getMinPrice()+"元 立即观看");
        RequestOptions options=new RequestOptions();
        GlideRoundImage glideRoundImage=new GlideRoundImage(5);
        options.centerCrop().transform(glideRoundImage);
        Glide.with(this).load(courseVipBean.getPicture()).apply(options).into(img);
    }

    @Override
    public void onBuyCourse(PayResultBean payResultBean) {
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
    public void onClickCollection(int position) {
        mCourseBean.setCollection(!mCourseBean.isCollection());
        if (mCourseBean.isCollection()){
            ToastUtil.show("已收藏");
            Glide.with(this).load(R.drawable.like).into(img_like);
        }else {
            ToastUtil.show("已取消");
            Glide.with(this).load(R.drawable.like_un).into(img_like);
        }
    }

    @OnClick({R.id.pay_vip,R.id.back,R.id.ll_collect})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.pay_vip:
                if (payChannel!=null){
                    Map<String,String>map=new HashMap<>();
                    map.put("courseId",courseId);
                    map.put("paymentCode",payChannel.getPaymentCode());
                    mPresenter.buyCourse(map);
                }else {
                    ToastUtil.show("请选择付款方式");
                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.ll_collect:
                Map<String,String> map=new HashMap<>();
                map.put("id",mCourseBean.getId());
                map.put("collection",mCourseBean.isCollection()+"");
                mPresenter.courseCollection(map,-1);
                break;
        }
    }
    private static final int SDK_PAY_FLAG = 1;
    private void doAliPayResult(String string){
        Runnable payRunnable = new Runnable() {//要在子线程
            @Override
            public void run() {
                PayTask alipay = new PayTask(CourseVipActivity.this);
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
    }
    private void gotoOrderPaySuccessActivity(){
        Intent intent=new Intent(CourseVipActivity.this,VipSuccessActivity.class);
        intent.putExtra("from","course");
        intent.putExtra("courseId",courseId);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
       if (EventBusTag.WX_PAY.equals(messageEvent.getTag())){
            gotoOrderPaySuccessActivity();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mCourseBean==null){
            finish();
        }
    }
}
