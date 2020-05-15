package com.weiyu.sp.lsjy.mine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseMvpActivity;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.AccountRecordBean;
import com.weiyu.sp.lsjy.bean.BalanceBean;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.course.CourseListFragment;
import com.weiyu.sp.lsjy.main.ClassFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyAccountActivity extends BaseMvpActivity<MyAccountPresenter>implements MyAccountContract.View {

    @BindView(R.id.balance)
    TextView balance;
    @BindView(R.id.k_balance)
    TextView k_balance;
    @BindView(R.id.d_balance)
    TextView d_balance;
    @BindView(R.id.l_balance)
    TextView l_balance;

    @BindView(R.id.vp)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    String[]titleArr={"收入明细","支出明细"};
    List<String> tabTitle;
    List<Fragment>fragments;
    private BalanceBean mBean;

    @BindView(R.id.ll_no_network)
    LinearLayout ll_no_network;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_account;
    }

    @Override
    public void initView() {
        mPresenter.selectAccountInfo();

        tabTitle= Arrays.asList(titleArr);
        fragments=new ArrayList<>();
        fragments.add(MyAccountFragment.newInstance(true));
        fragments.add(MyAccountFragment.newInstance(false));
        MyPagerAdapter myPagerAdapter=new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected MyAccountPresenter setMPresenter() {
        MyAccountPresenter myAccountPresenter=new MyAccountPresenter();
        myAccountPresenter.attachView(this);
        return myAccountPresenter;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (hasToken()){
            if(mBean==null){
                mPresenter.selectAccountInfo();
                EventBus.getDefault().post(new MessageEvent("", EventBusTag.MyAccountRefresh));
            }
        }else {
            finish();
        }
    }

    @Override
    public void onSuccess(BalanceBean bean) {
        ll_no_network.setVisibility(View.GONE);
        mBean = bean;
        balance.setText(bean.getTotalMoney());
        k_balance.setText(bean.getWithdrawableMoney());
        d_balance.setText(bean.getFrozenMoney());
        l_balance.setText(bean.getAddAccountMoney());
    }

    @Override
    public void onGetDetail(List<AccountRecordBean> beanList) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable throwable, int flag) {
        if (flag==0){
            ll_no_network.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.back,R.id.tv_refresh})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tv_refresh:
                mPresenter.selectAccountInfo();
                EventBus.getDefault().post(new MessageEvent("", EventBusTag.MyAccountRefresh));
                break;
        }
    }

    /**
     * fragment适配器
     */
    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle.get(position);
        }

    }
}
