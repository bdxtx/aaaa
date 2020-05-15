package com.weiyu.sp.lsjy.login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseMvpActivity;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.base.WebActivity;
import com.weiyu.sp.lsjy.base.WebUrl;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.main.MainActivity;
import com.weiyu.sp.lsjy.main.Utils;
import com.weiyu.sp.lsjy.utils.ChannelUtil;
import com.weiyu.sp.lsjy.utils.SPUtils;
import com.weiyu.sp.lsjy.utils.SystemUtil;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginContract.View {

    private static int startTag=0;
    @BindView(R.id.sendMsg)
    TextView sendMsg;
    @BindView(R.id.tv_login)
    TextView tv_login;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.cb)
    CheckBox cb;

    String tag;
    long firstTime=0;



    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        etCode.addTextChangedListener(textWatcher);
        etPhone.addTextChangedListener(textWatcher);
        tag=getIntent().getStringExtra("tag");

    }

    @Override
    protected LoginPresenter setMPresenter() {
        LoginPresenter loginPresenter=new LoginPresenter();
        loginPresenter.attachView(this);
        return loginPresenter;
    }

    @OnClick({R.id.sendMsg,R.id.tv_login,R.id.back,R.id.agreement})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.sendMsg:
                String phone=etPhone.getText().toString();
                if (TextUtils.isEmpty(phone)){
                    ToastUtil.show("手机号码不能为空");
                    return;
                }
                if (phone.length()!=11){
                    ToastUtil.show("请输入正确的11位手机号");
                    return;
                }
                if (mCountTime!=60){
//                    ToastUtil.show("短信已发送，请稍后再试");
                    return;
                }
                mPresenter.sendMsg(phone);
                startCount();
                break;
            case R.id.tv_login:
                if (etCode.getText().toString().length()!=6){
                    ToastUtil.show("请填写6位验证码");
                    return;
                }
                if (cb.isChecked()){
                    Map<String ,String> params=new HashMap<String,String>();
                    params.put("firmType", SystemUtil.getDeviceBrand()+SystemUtil.getSystemModel());
                    params.put("lckSystemEdition","V1.0.0");
                    params.put("loginName",etPhone.getText().toString());
                    params.put("newCode",etCode.getText().toString());
                    params.put("phoneType","Android");
                    params.put("resolution",SystemUtil.getWidth(this)+" x "+SystemUtil.getHeight(this));
                    params.put("systemOs", SystemUtil.getSystemVersion());//Android系统版本号
                    String channel= ChannelUtil.getChannel(getApplicationContext());
                    if (TextUtils.isEmpty(channel)){
                        channel="platform";
                    }
                    params.put("channel",channel);//"platform"
                    long secondTime = System.currentTimeMillis();
                    if (secondTime - firstTime > 3000) {
                        firstTime = secondTime;
                        mPresenter.doLogin(params);
                    }
                }else {
                    ToastUtil.show("请勾选用户协议");
                }
                break;
            case R.id.back:
                SPUtils.saveData(Constant.LOGIN_TOKEN,"");
                SPUtils.saveData(Constant.REFRESH_LOGIN_TOKEN,"");
                if ("study".equals(tag)){
                    EventBus.getDefault().post(new MessageEvent("",EventBusTag.MainToHome));
                }
                if (Constant.PersonCenterJump.equals(tag)){
                    EventBus.getDefault().post(new MessageEvent("",EventBusTag.MainToMe));
                }
                if (startTag==1){
                    startActivity(new Intent(this, MainActivity.class));
                }
                finish();
                break;
            case R.id.agreement:
                Intent intent=new Intent(LoginActivity.this, WebActivity.class);
                intent.putExtra(Constant.WEB_INTENT, WebUrl.AGREEMENT);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onSuccess(String code) {
        etCode.setText(code);
    }

    @Override
    public void onLoginSuccess() {
        if (startTag==0){
            EventBus.getDefault().post(new MessageEvent("", EventBusTag.MainRefresh));
        }else {
            startActivity(new Intent(this, MainActivity.class));
        }
        long secondTime = System.currentTimeMillis();
        SPUtils.saveData(Constant.RefreshTokenTime,secondTime);
        finish();
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

    private Handler mHandler = new Handler();
    private int mCountTime = 60;
    /*
           倒计时，并处理点击事件
        */
    private void startCount() {
        mHandler.postDelayed(run, 0);
    }

    /*
        倒计时
     */
    private Runnable run=new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            mCountTime--;
            if (mCountTime > 0) {
                sendMsg.setText("重新发送("+mCountTime+")");
                mHandler.postDelayed(this, 1000);
            } else {
                mCountTime=60;
                sendMsg.setText("获取验证码");
                removeRunable();
            }
        }
    };

    private void  removeRunable() {
        mHandler.removeCallbacks(run);
    }

    @Override
    protected void onDestroy() {
        removeRunable();
        super.onDestroy();
    }

    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (etPhone.getText().toString().length()==11&&!TextUtils.isEmpty(etCode.getText().toString())){
                tv_login.setBackground(getResources().getDrawable(R.drawable.shape_login_btn_sure));
                tv_login.setEnabled(true);
            }else {
                tv_login.setBackground(getResources().getDrawable(R.drawable.shape_login_btn));
                tv_login.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public static void launchTask(Context context,String tag) {
        startTag=0;
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("tag",tag);
            context.startActivity(intent);
        }
    }
    public static void launchClearTask(Context context) {
        startTag=1;
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, LoginActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if ("study".equals(tag)){
            EventBus.getDefault().post(new MessageEvent("",EventBusTag.MainToHome));
        }
        if (Constant.PersonCenterJump.equals(tag)){
            EventBus.getDefault().post(new MessageEvent("",EventBusTag.MainToMe));
        }
        if (startTag==1){
            startActivity(new Intent(this, MainActivity.class));
        }

    }
}
