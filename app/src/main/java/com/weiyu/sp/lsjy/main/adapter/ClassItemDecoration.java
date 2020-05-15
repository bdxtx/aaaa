package com.weiyu.sp.lsjy.main.adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者：陈思村 on 2018/10/25.
 * 邮箱：chensicun@51ganjie.com
 */
public class ClassItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        float density = view.getContext().getResources().getDisplayMetrics().density;

        if (parent.getChildAdapterPosition(view)==0){
            outRect.left=(int)(8*density+0.5);
        }
//        //如果不是第一个，则设置top的值。
        if (parent.getChildAdapterPosition(view) != 0){
            //这里直接硬编码为1px
//            outRect.top = (int)(1*density+0.5);
            outRect.left=(int)(1*density+0.5);
//            outRect.right=(int)(10*density+0.5);
        }
        if (parent.getChildAdapterPosition(view)==3){
            outRect.right=(int)(8*density+0.5);
        }

    }
}
