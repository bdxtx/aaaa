package com.weiyu.sp.lsjy.main.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weiyu.sp.lsjy.R;

/**
 * 作者：陈思村 on 2018/10/25.
 * 邮箱：chensicun@51ganjie.com
 */
public class StudyItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        float density = view.getContext().getResources().getDisplayMetrics().density;
        if (view.findViewById(R.id.foot)==null){
            if (parent.getChildAdapterPosition(view)%2==0){
                outRect.left=(int)(12*density+0.5);
            }else {
                outRect.left=(int)(3*density+0.5);
            }
        }


//        if (parent.getChildAdapterPosition(view) != 0&&parent.getChildAdapterPosition(view)%2==0){
//            outRect.left=(int)(3*density+0.5);
//        }

    }

//    @Override
//    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        super.onDraw(c, parent, state);
//        final int itemCount = parent.getChildCount();
//        final int left = parent.getPaddingLeft();
//        final int right = parent.getWidth() - parent.getPaddingRight();
//        for (int i = 0; i < itemCount-1; i++) {
//            final View child = parent.getChildAt(i);
//            if (child == null) return;
//            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
//            final int top = child.getBottom() + params.bottomMargin;
//            final int bottom = top +2;
//            mDivider.setBounds(left, top, right, bottom);
//            mDivider.draw(c);
//        }
//    }
}
