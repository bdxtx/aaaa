package com.weiyu.sp.lsjy.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseApplication;

public class ToastUtil {
    public static void show(String string){
        Toast toast=Toast.makeText(BaseApplication.getInstance(),string,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view=LayoutInflater.from(BaseApplication.getInstance()).inflate(R.layout.toast_view,null);
        TextView textView=view.findViewById(R.id.message);
        textView.setText(string);
        toast.setView(view);
        toast.getView().setPadding(30, 15, 30, 15);
        toast.show();
    }
}
