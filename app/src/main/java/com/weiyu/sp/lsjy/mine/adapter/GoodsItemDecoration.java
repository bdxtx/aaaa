package com.weiyu.sp.lsjy.mine.adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者：陈思村 on 2018/10/25.
 * 邮箱：chensicun@51ganjie.com
 */
public class GoodsItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        float density = view.getContext().getResources().getDisplayMetrics().density;

        if (parent.getChildAdapterPosition(view)%2==1){
            outRect.left=(int)(15*density+0.5);
        }
        if (parent.getChildAdapterPosition(view) != 0&&parent.getChildAdapterPosition(view)%2==0){
            outRect.left=(int)(7*density+0.5);
        }
        outRect.top=(int)(20*density+0.5);

    }
}
