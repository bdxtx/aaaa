package com.weiyu.sp.lsjy.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseFragment;
import com.weiyu.sp.lsjy.main.MainActivity;

import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class G1Fragment extends BaseFragment {

    public G1Fragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_g1;
    }

    @Override
    protected void initView(View view) {

    }
    @OnClick({R.id.jump})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.jump:
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                break;
        }
    }
}
