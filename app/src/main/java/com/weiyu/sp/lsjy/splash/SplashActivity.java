package com.weiyu.sp.lsjy.splash;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseActivity;
import com.weiyu.sp.lsjy.base.BaseMvpActivity;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.main.MainActivity;
import com.weiyu.sp.lsjy.utils.SPUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class SplashActivity extends BaseMvpActivity<SplashPresenter>implements SplashContract.View {
    @BindView(R.id.num)
    TextView num;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////        new Handler().postDelayed(new Runnable() {
////            @Override
////            public void run() {
////
////            }
////        },3000);
//    }

    @Override
    protected SplashPresenter setMPresenter() {
        SplashPresenter splashPresenter=new SplashPresenter();
        splashPresenter.attachView(this);
        return splashPresenter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        Log.i("csc",SPUtils.getStringData(Constant.REFRESH_LOGIN_TOKEN)+"|||||||||||"+SPUtils.getStringData(Constant.LOGIN_TOKEN));
        if (hasToken()&& !TextUtils.isEmpty(SPUtils.getStringData(Constant.REFRESH_LOGIN_TOKEN))){
            Log.i("csc",SPUtils.getStringData(Constant.REFRESH_LOGIN_TOKEN)+"|||||||||||"+SPUtils.getStringData(Constant.LOGIN_TOKEN));
            mPresenter.refreshLogin(SPUtils.getStringData(Constant.REFRESH_LOGIN_TOKEN));
        }
        startCount();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(Throwable throwable, int flag) {

    }
    @OnClick(R.id.jump)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.jump:
                jump();
                break;
        }
    }


    private Handler mHandler = new Handler();
    private int mCountTime = 3;
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
                num.setText(mCountTime+"");
                mHandler.postDelayed(this, 1000);
            } else {
                jump();
                num.setText("0");
                removeRunable();
            }
        }
    };

    private void  removeRunable() {
        mHandler.removeCallbacks(run);
    }

    private void jump(){
        boolean isFirst= SPUtils.getBooleanData(Constant.IS_FIRST);
        if (isFirst){
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
            SPUtils.saveData(Constant.IS_FIRST,false);
        }else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        removeRunable();
        super.onDestroy();
    }
}
