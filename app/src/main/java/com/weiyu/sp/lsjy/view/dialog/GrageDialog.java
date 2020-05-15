package com.weiyu.sp.lsjy.view.dialog;

import android.app.Dialog;
import android.graphics.Color;
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
import cn.addapp.pickers.picker.SinglePicker;

public class GrageDialog extends UpdatePersonalBaseDialog {
    Dialog dialog;
    private String selectSex="一年级";
    public static GrageDialog newInstance(String grade){
        GrageDialog grageDialog=new GrageDialog();
        Bundle bundle=new Bundle();
        bundle.putString("grade",grade);
        grageDialog.setArguments(bundle);
        return  grageDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.grade);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消

        Bundle bundle=getArguments();
        selectSex=bundle.getString("grade");
        ViewGroup viewGroup = dialog.findViewById(R.id.content);
        viewGroup.addView(onSinglePicker());
        TextView tv_ok=dialog.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePersonalPresenter updatePersonalPresenter=new UpdatePersonalPresenter();
                updatePersonalPresenter.attachView(GrageDialog.this);
                Map<String,String>map=new HashMap<>();
                map.put("grade",selectSex);
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
    public void onSuccess(BaseObjectBean<PersonalBean> stringBaseObjectBean) {
        if (stringBaseObjectBean.getCode()==200){
            EventBus.getDefault().post(new MessageEvent("",EventBusTag.PersonalUpdate));
            dismiss();
        }
    }

    public View onSinglePicker() {
        SinglePicker<String> picker = new SinglePicker<String>(getActivity(), new String[]{"一年级","二年级","三年级","四年级","五年级","六年级","七年级","八年级","九年级","十年级","十一年级","十二年级"});
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
        picker.setSelectedItem(selectSex);
        return picker.getContentView();
    }

}
