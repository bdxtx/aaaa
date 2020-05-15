package com.weiyu.sp.lsjy.splash;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.WindowManager;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GuideActivity extends BaseActivity {
    @BindView(R.id.vp)
    ViewPager vp;
    List<Fragment>fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView() {

        fragments=new ArrayList<>();
        fragments.add(new G1Fragment());
        fragments.add(new G2Fragment());
        fragments.add(new G3Fragment());
        fragments.add(new G4Fragment());
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
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

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return tabTitle.get(position);
//        }

    }
}
