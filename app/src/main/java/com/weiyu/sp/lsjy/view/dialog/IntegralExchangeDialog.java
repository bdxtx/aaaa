package com.weiyu.sp.lsjy.view.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.main.MainActivity;
import com.weiyu.sp.lsjy.utils.ToastUtil;

public class IntegralExchangeDialog extends DialogFragment {
    Dialog dialog;
    Exchange exchange;
    private long firstTime = 0;
    public static IntegralExchangeDialog newInstance(String nameStr,String phoneStr,String addrStr,String goodsName,String integral){
        IntegralExchangeDialog integralExchangeDialog=new IntegralExchangeDialog();
        Bundle bundle=new Bundle();
        bundle.putString("nameStr",nameStr);
        bundle.putString("goodsName",goodsName);
        bundle.putString("integral",integral);
        bundle.putString("phoneStr",phoneStr);
        bundle.putString("addrStr",addrStr);
        integralExchangeDialog.setArguments(bundle);
        return integralExchangeDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.integral_exchage_dialog);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        Bundle bundle=getArguments();
        String nameStr=bundle.getString("nameStr");
        String phoneStr=bundle.getString("phoneStr");
        String addrStr=bundle.getString("addrStr");
        String goodsName=bundle.getString("goodsName");
        String integral=bundle.getString("integral");
        TextView tv_integral=dialog.findViewById(R.id.integral);
        tv_integral.setText(integral);
        TextView tv_goodsName=dialog.findViewById(R.id.goodsName);
        tv_goodsName.setText(goodsName);


        EditText name=dialog.findViewById(R.id.name);
        EditText phone=dialog.findViewById(R.id.phone);
        EditText addr=dialog.findViewById(R.id.addr);

        name.setText(nameStr);
        phone.setText(phoneStr);
        addr.setText(addrStr);
        dialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(name.getText().toString())){
                    ToastUtil.show("姓名不能为空");
                    return;
                }
                if (phone.getText().toString().length()!=11){
                    ToastUtil.show("请输入11位手机号码");
                    return;
                }
                if (TextUtils.isEmpty(addr.getText().toString())){
                    ToastUtil.show("地址不能为空");
                    return;
                }
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 1000) {
                    Exchange exchange= (Exchange) getActivity();
                    exchange.exchange(name.getText().toString(),phone.getText().toString(),addr.getText().toString());
                    firstTime = secondTime;
                }

            }
        });
        return dialog;
    }

    public interface Exchange{
        void exchange(String name,String phone,String addr);
    }

}
