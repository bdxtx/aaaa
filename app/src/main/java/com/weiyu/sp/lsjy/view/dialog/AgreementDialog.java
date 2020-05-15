package com.weiyu.sp.lsjy.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.base.WebActivity;
import com.weiyu.sp.lsjy.base.WebUrl;
import com.weiyu.sp.lsjy.login.LoginActivity;
import com.weiyu.sp.lsjy.utils.SPUtils;

public class AgreementDialog extends DialogFragment {
    Dialog dialog;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.agreement);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消

        dialog.findViewById(R.id.fuwu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), WebActivity.class);
                intent.putExtra(Constant.WEB_INTENT, WebUrl.AGREEMENT);
                startActivity(intent);
            }
        });
        dialog.findViewById(R.id.fuwu1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), WebActivity.class);
                intent.putExtra(Constant.WEB_INTENT, WebUrl.AGREEMENT);
                startActivity(intent);
            }
        });
        dialog.findViewById(R.id.yinsi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), WebActivity.class);
                intent.putExtra(Constant.WEB_INTENT, WebUrl.PRIVATE_AGREEMENT);
                intent.putExtra("title","隐私政策");
                startActivity(intent);
            }
        });
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.saveData(Constant.AGREEMENT,true);
                dismiss();
            }
        });
        dialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.saveData(Constant.AGREEMENT,true);
                dismiss();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    return true;
                }else if(keyCode == KeyEvent.KEYCODE_MENU) {

                    return true;
                }
                return false;
            }
        });
        return dialog;
    }
}
