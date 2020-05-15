package com.weiyu.sp.lsjy.mine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseMvpActivity;
import com.weiyu.sp.lsjy.utils.ToastUtil;
import com.weiyu.sp.lsjy.view.dialog.LoadingDialog;
import com.weiyu.sp.lsjy.view.dialog.OpinionSuccessDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class OpinionActivity extends BaseMvpActivity<OpinionPresenter> implements OpinionContract.View {
    @BindView(R.id.et_opinion)
    EditText et_opinion;
    private LoadingDialog loadingDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_opinion;
    }

    @Override
    public void initView() {
        et_opinion.addTextChangedListener(textWatcher);
    }
    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length()>200){
                editable.delete(200, editable.length());
                ToastUtil.show("最多输入"+200+"个字");
            }
        }
    };

    @Override
    protected OpinionPresenter setMPresenter() {
        OpinionPresenter opinionPresenter=new OpinionPresenter();
        opinionPresenter.attachView(this);
        return opinionPresenter;
    }

    @OnClick({R.id.back,R.id.submit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                if (et_opinion.getText().toString().length()<10){
                    ToastUtil.show("至少输入10个汉字");
                    return;
                }
                if (!TextUtils.isEmpty(et_opinion.getText().toString())){
                    Map<String,String>map=new HashMap<>();
                    map.put("content",et_opinion.getText().toString());
                    mPresenter.postOpinion(map);
                }
                break;
        }
    }


    @Override
    public void onPostOpinion() {
        OpinionSuccessDialog opinionSuccessDialog=new OpinionSuccessDialog();
        opinionSuccessDialog.show(getSupportFragmentManager(),"fff");
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    public void onError(Throwable throwable, int flag) {

    }
}
