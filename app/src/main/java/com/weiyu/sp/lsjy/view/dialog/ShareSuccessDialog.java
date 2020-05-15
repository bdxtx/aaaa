package com.weiyu.sp.lsjy.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.weiyu.sp.lsjy.R;

public class ShareSuccessDialog extends DialogFragment {
    Dialog dialog;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.share_success);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog!=null){
                    dismiss();
                }
            }
        },3000);
        return dialog;
    }
}
