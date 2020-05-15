package com.weiyu.sp.lsjy.vip.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者：陈思村 on 2018/10/25.
 * 邮箱：chensicun@51ganjie.com
 */
public class ChannelItemDecoration extends RecyclerView.ItemDecoration {
    private final ColorDrawable mDivider;
    private float density=1;

    public ChannelItemDecoration(){
        mDivider = new ColorDrawable(Color.parseColor("#E5E5E5"));
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        density = view.getContext().getResources().getDisplayMetrics().density;

//        //如果不是第一个，则设置top的值。
        if (parent.getChildAdapterPosition(view) != 0){
            //这里直接硬编码为1px
            outRect.top = (int)(1* density +0.5);
//            outRect.left=(int)(10*density+0.5);
//            outRect.right=(int)(10*density+0.5);
        }

    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int itemCount = parent.getChildCount();
        final int left = (int) (52*density)+parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < itemCount; i++) {
            final View child = parent.getChildAt(i);
            if (child == null) return;
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top +2;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
