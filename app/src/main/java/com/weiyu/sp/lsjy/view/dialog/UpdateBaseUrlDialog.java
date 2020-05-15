package com.weiyu.sp.lsjy.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.net.Url;
import com.weiyu.sp.lsjy.utils.SPUtils;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

public class UpdateBaseUrlDialog extends DialogFragment {
    Dialog dialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.update_base_url);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        TextView tv_ok=dialog.findViewById(R.id.tv_ok);
        EditText et_code=dialog.findViewById(R.id.et_code);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url=et_code.getText().toString();
                if (TextUtils.isEmpty(url)){
                    ToastUtil.show("请输入url");
                    return;
                }
                if (Constant.baseUrl.equals(url)){
                    ToastUtil.show("正式环境不支持手动切换");
                    return;
                }
                SPUtils.saveData(Constant.UpdateBaseUrl,url);
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
