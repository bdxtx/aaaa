package com.weiyu.sp.lsjy.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.bean.PersonalBean;
import com.weiyu.sp.lsjy.mine.UpdatePersonalBaseDialog;
import com.weiyu.sp.lsjy.mine.UpdatePersonalPresenter;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class PhoneDialog extends UpdatePersonalBaseDialog {
    Dialog dialog;
    private TextView tip;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.phone);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        EditText et_name=dialog.findViewById(R.id.et_name);
        tip = dialog.findViewById(R.id.tip);
        TextView tv_ok=dialog.findViewById(R.id.tv_ok);
//        Bundle bundle=getArguments();
//        long time=bundle.getLong("time");
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_name.getText().toString().length()!=11){
                    ToastUtil.show("请输入11位手机号");
                    return;
                }
                UpdatePersonalPresenter updatePersonalPresenter=new UpdatePersonalPresenter();
                updatePersonalPresenter.attachView(PhoneDialog.this);
                Map<String,String>map=new HashMap<>();
                map.put("phone",et_name.getText().toString());
                updatePersonalPresenter.updateUserDetail(map);
            }
        });
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return dialog;
    }

    private LoadingDialog loadingDialog;

    @Override
    public void showLoading() {
        loadingDialog = LoadingDialog.newInstance();
        loadingDialog.show(getChildFragmentManager(),"load");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
            }
        },2000);
    }

    @Override
    public void hideLoading() {
        loadingDialog.dismiss();
    }

    @Override
    public void onError(Throwable throwable, int flag) {

    }

    @Override
    public void onSuccess(BaseObjectBean<PersonalBean> stringBaseObjectBean) {
        if (stringBaseObjectBean.getCode()==200){
            EventBus.getDefault().post(new MessageEvent("",EventBusTag.PersonalUpdate));
            dismiss();
        }else if (stringBaseObjectBean.getCode()==-1){
            tip.setText(stringBaseObjectBean.getMessage());
            tip.setVisibility(View.VISIBLE);
        }else {
            tip.setVisibility(View.INVISIBLE);
        }
    }

}
