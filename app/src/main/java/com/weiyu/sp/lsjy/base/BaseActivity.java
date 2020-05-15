package com.weiyu.sp.lsjy.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.bean.LoginBean;
import com.weiyu.sp.lsjy.net.RetrofitClient;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.SPUtils;
import com.weiyu.sp.lsjy.utils.StatusBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder unbinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        //设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        inBaseCreate();

        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();// 隐藏ActionBar
        }
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        initView();
        //默认着色状态栏
        StatusBarUtil.setLightStatusBar(
                this,
                ContextCompat.getColor(this, R.color.white)
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        inBaseDestroy();
    }

    /**
     * 设置布局
     *
     * @return
     */
    public abstract int getLayoutId();

    @Override
    protected void onRestart() {
        super.onRestart();
        long time=SPUtils.getLongData(Constant.RefreshTokenTime);
        long secondTime = System.currentTimeMillis();
        if (time>0&&secondTime-time>2000*60*60&&!TextUtils.isEmpty(Constant.REFRESH_LOGIN_TOKEN)){
            RetrofitClient.getInstance().getApi(Constant.RefreshToken).refreshLogin(Constant.REFRESH_LOGIN_TOKEN).compose(RxScheduler.Flo_io_main())
                    .subscribe(new Consumer<BaseObjectBean<LoginBean>>() {
                        @Override
                        public void accept(BaseObjectBean<LoginBean> loginBeanBaseObjectBean) throws Exception {

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
            SPUtils.saveData(Constant.RefreshTokenTime,secondTime);
        }

    }

    /**
     * 初始化视图
     */
    public abstract void initView();

    protected void inBaseCreate(){

    }

    protected void inBaseDestroy(){

    }
    public boolean hasToken(){
        return !TextUtils.isEmpty(BaseApplication.getInstance().getToken());
    }
}
