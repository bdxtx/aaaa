package com.weiyu.sp.lsjy.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

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

import cn.addapp.pickers.listeners.OnSingleWheelListener;
import cn.addapp.pickers.picker.SinglePicker;

public class ToBeVipDialog extends DialogFragment {
    Dialog dialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.to_be_vip);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        TextView tv_ok=dialog.findViewById(R.id.tv_ok);
        EditText et_code=dialog.findViewById(R.id.et_code);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_code.getText().toString())){
                    ToastUtil.show("请输入验证码");
                    return;
                }
                EventBus.getDefault().post(new MessageEvent(et_code.getText().toString(),EventBusTag.ActiveVip));
                dismiss();
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

}
