package com.weiyu.sp.lsjy.course;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseActivity;
import com.weiyu.sp.lsjy.base.BaseMvpActivity;
import com.weiyu.sp.lsjy.base.BaseMvpFragment;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.main.ClassFragment;
import com.weiyu.sp.lsjy.main.ClassMainContract;
import com.weiyu.sp.lsjy.main.ClassMainPresenter;
import com.weiyu.sp.lsjy.other.ShareActivity;
import com.weiyu.sp.lsjy.view.dialog.CourseSelectDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ClassActivity extends BaseActivity {

    @BindView(R.id.vp)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.to_share)
    TextView to_share;
    @BindView(R.id.line)
    TextView line;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_search)
    ImageView img_search;

    String[]titleArr={"全部","语文","历史","地理","名著导读"};
    List<String>tabTitle;
    List<Fragment>fragments;
    @BindView(R.id.ll_no_network)
    LinearLayout ll_no_network;

    @Override
    public int getLayoutId() {
        return R.layout.activity_class;
    }

    @Override
    public void initView() {
        Intent intent=getIntent();
        int flag=intent.getIntExtra("flag",0);
        String subjectCode=intent.getStringExtra("subjectCode");
        if (flag== Constant.SHOU_CANG){//代表收藏页面
            to_share.setVisibility(View.INVISIBLE);
            line.setVisibility(View.INVISIBLE);
            img_search.setVisibility(View.INVISIBLE);
            tv_title.setText("我的收藏");
        }
        EventBus.getDefault().register(this);
        tabTitle= Arrays.asList(titleArr);
        fragments=new ArrayList<>();
        fragments.add(CourseListFragment.newInstance(0,flag));
        fragments.add(CourseListFragment.newInstance(700,flag));
        fragments.add(CourseListFragment.newInstance(701,flag));
        fragments.add(CourseListFragment.newInstance(702,flag));
        fragments.add(CourseListFragment.newInstance(703,flag));
        MyPagerAdapter myPagerAdapter=new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setOffscreenPageLimit(titleArr.length);
        tabLayout.setupWithViewPager(viewPager);
        if (!TextUtils.isEmpty(subjectCode)){
            switch (subjectCode){
                case "0":
                    viewPager.setCurrentItem(0);
                    break;
                case "700":
                    viewPager.setCurrentItem(1);
                    break;
                case "701":
                    viewPager.setCurrentItem(2);
                    break;
                case "702":
                    viewPager.setCurrentItem(3);
                    break;
                case "703":
                    viewPager.setCurrentItem(4);
                    break;
                default:
                    viewPager.setCurrentItem(0);
                    break;
            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (hasToken()){
            EventBus.getDefault().post(new MessageEvent(EventBusTag.LoginRefresh,EventBusTag.MainRefreshCourseList));
        }else {
            finish();
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
    @OnClick({R.id.back,R.id.img_search,R.id.tv_refresh,R.id.to_share})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.img_search:
                CourseSelectDialog courseSelectDialog=CourseSelectDialog.newInstance(1);
                courseSelectDialog.show(getSupportFragmentManager(),"筛选");
                break;
            case R.id.tv_refresh:
                EventBus.getDefault().post(new MessageEvent(EventBusTag.LoginRefresh,EventBusTag.MainRefreshCourseList));
                break;
            case R.id.to_share:
                startActivity(new Intent(ClassActivity.this, ShareActivity.class));
                break;
        }
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
        if (EventBusTag.CourseSelectToActivity.equals(messageEvent.getTag())){
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
        }else if (EventBusTag.NetWorkLink.equals(messageEvent.getTag())){
            ll_no_network.setVisibility(View.GONE);
        }
    }
}
