package com.weiyu.sp.lsjy.mine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseMvpActivity;
import com.weiyu.sp.lsjy.utils.ToastUtil;
import com.weiyu.sp.lsjy.view.dialog.CourseSelectDialog;
import com.weiyu.sp.lsjy.view.dialog.LoadingDialog;
import com.weiyu.sp.lsjy.view.dialog.OpinionSuccessDialog;
import com.weiyu.sp.lsjy.view.dialog.RecruitSuccessDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RecruitActivity extends BaseMvpActivity<RecruitPresenter>implements RecruitContract.View {
    @BindView(R.id.et_opinion)
    EditText et_opinion;
    private LoadingDialog loadingDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_recruit;
    }

    @Override
    public void initView() {

    }

    @Override
    protected RecruitPresenter setMPresenter() {
        RecruitPresenter recruitPresenter=new RecruitPresenter();
        recruitPresenter.attachView(this);
        return recruitPresenter;
    }

    @OnClick({R.id.back,R.id.send})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.send:
                if (et_opinion.getText().toString().length()!=11){
                    ToastUtil.show("请输入11位手机号码");
                    return;
                }
                if (!TextUtils.isEmpty(et_opinion.getText().toString())){
                    Map<String,String> map=new HashMap<>();
                    map.put("content",et_opinion.getText().toString());
                    mPresenter.postOpinion(map);
                }
                break;
        }
    }

    @Override
    public void onPostOpinion() {
        RecruitSuccessDialog recruitSuccessDialog=new RecruitSuccessDialog();
        recruitSuccessDialog.show(getSupportFragmentManager(),"fff");
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
