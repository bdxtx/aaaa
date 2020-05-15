package com.weiyu.sp.lsjy.base;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BasePagerAdapter extends FragmentPagerAdapter {

    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    private List<String> titles;

    public void setFragments(ArrayList<BaseFragment> fragments) {
        this.fragments = fragments;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        if (titles == null || titles.size() <= position)
//            return "";
//        return titles.get(position);
//    }

    public BasePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public BasePagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public BasePagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments, List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments == null)
            return null;
        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (titles == null || titles.size() == 0) {
            return "";
        }
        return titles.get(position);
    }

    @Override
    public int getCount() {
        if (fragments == null)
            return 0;
        return fragments.size();
    }
}
