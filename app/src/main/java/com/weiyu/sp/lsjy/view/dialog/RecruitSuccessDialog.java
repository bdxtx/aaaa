package com.weiyu.sp.lsjy.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.fragment.app.DialogFragment;

import com.weiyu.sp.lsjy.R;

public class RecruitSuccessDialog extends DialogFragment {
    Dialog dialog;
//    public static OpinionSuccessDialog newInstance(long second, String money){
//        OpinionSuccessDialog stopVideoDialog =new OpinionSuccessDialog();
//        Bundle bundle=new Bundle();
//        bundle.putLong("time",second);
//        bundle.putString("money",money);
//        stopVideoDialog.setArguments(bundle);
//        return stopVideoDialog;
//
//    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.rercuit_success_dialog);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        dialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                dismiss();
            }
        });
        return dialog;
    }
}
