package com.weiyu.sp.lsjy.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Lifecycle;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BaseView;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.net.RetrofitClient;
import com.weiyu.sp.lsjy.net.RxScheduler;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.functions.Consumer;

public class InviteCodeDialog extends DialogFragment implements BaseView {
    Dialog dialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.invite_code_dialog);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        TextView tv_ok=dialog.findViewById(R.id.tv_ok);
        EditText et_code=dialog.findViewById(R.id.et_code);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_code.getText().toString().length()!=6){
                    ToastUtil.show("请填写6位邀请码");
                    return;
                }
                showLoading();
                RetrofitClient.getInstance().getApi().registerShareInfo(et_code.getText().toString()).compose(RxScheduler.Flo_io_main())
                        .as(bindAutoDispose())
                        .subscribe(new Consumer<BaseObjectBean<String>>() {
                            @Override
                            public void accept(BaseObjectBean<String> stringBaseObjectBean) throws Exception {
                                loadingDialog.dismiss();
                                if (stringBaseObjectBean.getCode()==200){
                                    EventBus.getDefault().post(new MessageEvent("",EventBusTag.Invite));
                                    ToastUtil.show("填写成功");
                                    dismiss();
                                }else if (stringBaseObjectBean.getCode()==600||stringBaseObjectBean.getCode()==700){

                                }else {
                                    ToastUtil.show(stringBaseObjectBean.getMessage());
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                loadingDialog.dismiss();
                            }
                        });
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
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
    }
}
