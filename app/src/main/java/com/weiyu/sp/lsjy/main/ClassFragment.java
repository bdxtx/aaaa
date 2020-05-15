package com.weiyu.sp.lsjy.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseFragment;
import com.weiyu.sp.lsjy.base.BaseMvpFragment;
import com.weiyu.sp.lsjy.base.BasePagerAdapter;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.course.CourseListFragment;
import com.weiyu.sp.lsjy.other.ShareActivity;
import com.weiyu.sp.lsjy.utils.IndicatorLineUtil;
import com.weiyu.sp.lsjy.view.dialog.CourseSelectDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassFragment extends BaseFragment {

    @BindView(R.id.vp)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.ll_no_network)
    LinearLayout ll_no_network;
    @BindView(R.id.smart)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.content)
    LinearLayout content;

    String[]titleArr={"全部","语文","历史","地理","名著导读"};
    List<String>tabTitle;
    ArrayList<BaseFragment>fragments;

    public ClassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_class_ui;
    }

    @Override
    protected void initView(View view) {
//        smartRefreshLayout.autoRefresh();
        tabTitle= Arrays.asList(titleArr);
        fragments=new ArrayList<>();
        fragments.add(CourseListFragment.newInstance(0,0));
        fragments.add(CourseListFragment.newInstance(700,0));
        fragments.add(CourseListFragment.newInstance(701,0));
        fragments.add(CourseListFragment.newInstance(702,0));
        fragments.add(CourseListFragment.newInstance(703,0));
//        mPresenter.selectCourseList(new HashMap<>());
//        MyPagerAdapter myPagerAdapter=new MyPagerAdapter(getChildFragmentManager());
//        viewPager.setAdapter(myPagerAdapter);
        viewPager.setAdapter(new BasePagerAdapter(getChildFragmentManager(),fragments,tabTitle));
        viewPager.setOffscreenPageLimit(titleArr.length);
        tabLayout.setupWithViewPager(viewPager);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                EventBus.getDefault().post(new MessageEvent(EventBusTag.LoginRefresh,EventBusTag.MainRefreshCourseList));
                smartRefreshLayout.finishRefresh(1000);
            }
        });


    }
    /**
     * fragment适配器
     */
    private class MyPagerAdapter extends FragmentPagerAdapter {

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @OnClick({R.id.img_search,R.id.tv_refresh,R.id.tv_share})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_search:
                CourseSelectDialog courseSelectDialog=CourseSelectDialog.newInstance(2);
                courseSelectDialog.show(getFragmentManager(),"筛选");
                break;
            case R.id.tv_refresh:
                smartRefreshLayout.autoRefresh();
                EventBus.getDefault().post(new MessageEvent(EventBusTag.LoginRefresh,EventBusTag.MainRefreshCourseList));
                break;
            case R.id.tv_share:
                startActivity(new Intent(getActivity(), ShareActivity.class));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (EventBusTag.CourseSelectToFragment.equals(messageEvent.getTag())){
            CourseBean courseBean= (CourseBean) messageEvent.getMessage();
            if (courseBean!=null){
                switch (courseBean.getSubjectCode()){
                    case 0:
                        viewPager.setCurrentItem(0);
                        break;
                    case 700:
                        viewPager.setCurrentItem(1);
                        break;
                    case 701:
                        viewPager.setCurrentItem(2);
                        break;
                    case 702:
                        viewPager.setCurrentItem(3);
                        break;
                    case 703:
                        viewPager.setCurrentItem(4);
                        break;
                }
            }
        }else if (EventBusTag.NoNetWork.equals(messageEvent.getTag())){
            ll_no_network.setVisibility(View.VISIBLE);
            smartRefreshLayout.setEnableRefresh(true);
            content.setVisibility(View.GONE);
        }else if (EventBusTag.NetWorkLink.equals(messageEvent.getTag())){
            ll_no_network.setVisibility(View.GONE);
            smartRefreshLayout.setEnableRefresh(false);
            content.setVisibility(View.VISIBLE);
        }
    }


}
