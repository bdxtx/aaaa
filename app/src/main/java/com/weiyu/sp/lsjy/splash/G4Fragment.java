package com.weiyu.sp.lsjy.splash;

import android.content.Intent;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseFragment;
import com.weiyu.sp.lsjy.main.MainActivity;

import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class G4Fragment extends BaseFragment {

    public G4Fragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_g4;
    }

    @Override
    protected void initView(View view) {

    }
    @OnClick({R.id.jump,R.id.enter})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.jump:
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                break;
            case R.id.enter:
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                break;
        }
    }
}
