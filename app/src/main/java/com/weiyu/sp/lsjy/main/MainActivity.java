package com.weiyu.sp.lsjy.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseApplication;
import com.weiyu.sp.lsjy.base.BaseMvpActivity;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.BannerBean;
import com.weiyu.sp.lsjy.bean.HomeBean;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.bean.UpdateBean;
import com.weiyu.sp.lsjy.login.LoginActivity;
import com.weiyu.sp.lsjy.utils.SPUtils;
import com.weiyu.sp.lsjy.view.dialog.UpdateDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindViews;
import butterknife.OnClick;


public class MainActivity extends BaseMvpActivity<MainPresenter> implements MainContract.View {

    @BindViews({R.id.rb_home, R.id.rb_class, R.id.rb_study,R.id.rb_me})
    List<RadioButton> bottomViews;

    private MainPresenter mainPresenter;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private static final int INDEX_HOME = 0;
    private static final int INDEX_CLASS = 1;
    private static final int INDEX_STUDY = 2;
    private static final int INDEX_ME = 3;
    private String homeFragmentTag="homeFragmentTag";
    private String classFragmentTag="classFragmentTag";
    private String studyFragmentTag="studyFragmentTag";
    private String meFragmentTag="meFragmentTag";
    private HomeFragment homeFragment;
    private ClassFragment classFragment;
    private StudyFragment studyFragment;
    private MeFragment meFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.remove("android:support:fragments"); //注意：基类是Activity时参数为android:fragments
//            bottomViews.get(0).setChecked(true);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected MainPresenter setMPresenter() {
        mainPresenter = new MainPresenter();
        mainPresenter.attachView(this);
        return mainPresenter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        manager=getSupportFragmentManager();
        transaction = manager.beginTransaction();
        homeFragment = new HomeFragment();
        transaction.add(R.id.fl_container, homeFragment);
        transaction.commit();
        Map<String,String>map=new HashMap<>();
        map.put("editionCode",BaseApplication.getAppVersionCode(this)+"");
        map.put("encryptType","android");
        mPresenter.update(map);
    }

    @OnClick({R.id.rb_home, R.id.rb_class, R.id.rb_study,R.id.rb_me})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_home://首页
                setTabSelection(INDEX_HOME);
                break;
            case R.id.rb_class://课程
                setTabSelection(INDEX_CLASS);
                break;
            case R.id.rb_study://学习
                if (TextUtils.isEmpty(BaseApplication.getInstance().getToken())){
                    LoginActivity.launchTask(BaseApplication.getInstance(),"study");
                }
                setTabSelection(INDEX_STUDY);
                break;
            case R.id.rb_me://我的
                setTabSelection(INDEX_ME);
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable throwable, int flag) {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onGotBanner(List<BannerBean> beans) {
//        List<String> imageIds = new ArrayList<>();
//        for (BannerBean bannerBean:beans){
//            imageIds.add(bannerBean.getPicture());
//        }
//        bgaBanner.setData(R.layout.item_fresco_t,imageIds,null);
//        bgaBanner.setAdapter(new BGABanner.Adapter<CardView, String>() {
//            @Override
//            public void fillBannerItem(BGABanner banner, CardView itemView, @Nullable String model, int position) {
//                ImageView simpleDraweeView = itemView.findViewById(R.id.sdv_item_fresco_content);
//                Glide.with(MainActivity.this)
//                        .load(model)
//                        .apply(new RequestOptions().centerCrop().dontAnimate())
//                        .into(simpleDraweeView);
//            }
//        });
    }

    @Override
    public void onGotHomeList(HomeBean bean) {

    }

    @Override
    public void onGetUpdateMsg(UpdateBean updateBean) {
        String updateTime = SPUtils.getStringData(Constant.UPDATE_TIME);
        String currentDate = Utils.getCurrentDate();
        if (currentDate.equals(updateTime)&&930==updateBean.getModify()){
            return;
        }
        UpdateDialog updateDialog=UpdateDialog.newInstance(updateBean);
        updateDialog.show(getSupportFragmentManager(),"update");
        SPUtils.saveData(Constant.UPDATE_TIME,currentDate);
    }

    private void setTabSelection(int index){
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);
        switch (index){
            case INDEX_HOME:
                if (homeFragment!=null){
                    transaction.show(homeFragment);
                }else {
                    homeFragment=new HomeFragment();
                    transaction.add(R.id.fl_container, homeFragment,homeFragmentTag);
                }
                break;
            case INDEX_CLASS:
                if (classFragment!=null){
                    transaction.show(classFragment);
                }else {
                    classFragment=new ClassFragment();
                    transaction.add(R.id.fl_container, classFragment,classFragmentTag);
                }
                break;
            case INDEX_STUDY:
                if (studyFragment!=null){
                    transaction.show(studyFragment);
                }else {
                    studyFragment=new StudyFragment();
                    transaction.add(R.id.fl_container, studyFragment,studyFragmentTag);
                }
                EventBus.getDefault().post(new MessageEvent("", EventBusTag.MainRefreshStudy));
                break;
            case INDEX_ME:
                if (meFragment!=null){
                    transaction.show(meFragment);
                }else {
                    meFragment=new MeFragment();
                    transaction.add(R.id.fl_container, meFragment,meFragmentTag);
                }
                break;
        }
        transaction.commitAllowingStateLoss();
    }
    private void hideFragment(FragmentTransaction transaction){
        if (homeFragment!=null){
            transaction.hide(homeFragment);
        }
        if (classFragment!=null){
            transaction.hide(classFragment);
        }
        if (studyFragment!=null){
            transaction.hide(studyFragment);
        }
        if (meFragment!=null){
            transaction.hide(meFragment);
        }
    }

    private long firstTime = 0;

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
//            ActivityController.exitApp();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (EventBusTag.MainToHome.equals(messageEvent.getTag())){
            bottomViews.get(0).setChecked(true);
            setTabSelection(0);
        }else if (EventBusTag.MainToMe.equals(messageEvent.getTag())){
            bottomViews.get(3).setChecked(true);
            setTabSelection(3);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

}
