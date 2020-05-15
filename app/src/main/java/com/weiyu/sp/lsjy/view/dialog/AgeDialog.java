package com.weiyu.sp.lsjy.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.bean.PersonalBean;
import com.weiyu.sp.lsjy.mine.UpdatePersonalBaseDialog;
import com.weiyu.sp.lsjy.mine.UpdatePersonalPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import cn.addapp.pickers.listeners.OnSingleWheelListener;
import cn.addapp.pickers.picker.NumberPicker;
import cn.addapp.pickers.picker.SinglePicker;

public class AgeDialog extends UpdatePersonalBaseDialog {
    Dialog dialog;
    private String selectSex="6";
    private LoadingDialog loadingDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.age);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        ViewGroup viewGroup = dialog.findViewById(R.id.content);
        viewGroup.addView(onSinglePicker());
        TextView tv_ok=dialog.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePersonalPresenter updatePersonalPresenter=new UpdatePersonalPresenter();
                updatePersonalPresenter.attachView(AgeDialog.this);
                Map<String,String>map=new HashMap<>();
                map.put("age",selectSex);
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
        }
    }

    public View onSinglePicker() {
        NumberPicker picker = new NumberPicker(getActivity());
        picker.setOffset(2);//偏移量
        picker.setRange(6, 100, 1);//数字范围
        picker.setSelectedItem(6);
        picker.setCanLoop(false);//不禁用循环
        picker.setLineVisible(true);
        picker.setLineColor(ContextCompat.getColor(getActivity(),R.color.color_E5E5E5));
        picker.setTextSize(15);
        picker.setOuterLabelEnable(true);
        picker.setSelectedTextColor(ContextCompat.getColor(getActivity(),R.color.color_333333));//前四位值是透明度
        picker.setUnSelectedTextColor(ContextCompat.getColor(getActivity(),R.color.color_CCCCCC));
        picker.setOnSingleWheelListener(new OnSingleWheelListener() {
            @Override
            public void onWheeled(int index, String item) {
                selectSex=item;
            }
        });
        return picker.getContentView();
    }

}
