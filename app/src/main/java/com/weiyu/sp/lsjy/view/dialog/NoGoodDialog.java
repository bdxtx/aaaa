package com.weiyu.sp.lsjy.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.weiyu.sp.lsjy.R;

public class NoGoodDialog extends DialogFragment {
    Dialog dialog;
//    public static NoGoodDialog newInstance(long second, String money){
//        NoGoodDialog stopVideoDialog =new NoGoodDialog();
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
        dialog.setContentView(R.layout.no_good_dialog);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        dialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return dialog;
    }
}
