package com.weiyu.sp.lsjy.view.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.course.CourseDetailActivity;
import com.weiyu.sp.lsjy.vip.CourseVipActivity;
import com.weiyu.sp.lsjy.vip.VipCenterActivity;

public class StopVideoDialog extends DialogFragment {
    Dialog dialog;
    public static StopVideoDialog newInstance(long second, String money,String courseId){
        StopVideoDialog stopVideoDialog =new StopVideoDialog();
        Bundle bundle=new Bundle();
        bundle.putLong("time",second);
        bundle.putString("money",money);
        bundle.putString("courseId",courseId);
        stopVideoDialog.setArguments(bundle);
        return stopVideoDialog;

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.stop_dialog);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        TextView tvBuy=dialog.findViewById(R.id.tv_buy);
        TextView tv_show=dialog.findViewById(R.id.tv_show);
        Bundle bundle=getArguments();
        long time=bundle.getLong("time");
        String money=bundle.getString("money");
        String courseId=bundle.getString("courseId");
        tv_show.setText("本次试看"+time+"秒已结束");
        tvBuy.setText(money+"元购买本课程");
        tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), CourseVipActivity.class);
                intent.putExtra("courseId",courseId);
                startActivity(intent);
                dismiss();
            }
        });
        dialog.findViewById(R.id.tv_vip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), VipCenterActivity.class);
                intent.putExtra("courseId",courseId);
                startActivity(intent);
                dismiss();
            }
        });
        return dialog;
    }
}
