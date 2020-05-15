package com.weiyu.sp.lsjy.vip.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.bean.CardType;

import java.util.List;

public class VipCardAdapter extends BaseQuickAdapter<CardType, BaseViewHolder> {
    public VipCardAdapter( @Nullable List<CardType> data) {
        super(R.layout.card_type_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CardType item) {
        helper.setText(R.id.tv_card_name,item.getVipTypeName())
                .setText(R.id.tv_max_price,item.getMaxPrice())
                .setText(R.id.tv_price,item.getMinPrice())
        .setText(R.id.discount,item.getDiscount()+"折");
        FrameLayout fl=helper.getView(R.id.fl);

        TextView discount=helper.getView(R.id.discount);
        TextView tv_max_price=helper.getView(R.id.tv_max_price);
        tv_max_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
        if (TextUtils.isEmpty(item.getDiscount())){
            discount.setVisibility(View.GONE);
            tv_max_price.setVisibility(View.GONE);
        }
        if (item.isSelect()){
            fl.setBackground(mContext.getResources().getDrawable(R.drawable.vip_select_box_btn));
        }else {
            fl.setBackground(mContext.getResources().getDrawable(R.drawable.vip_select_un_box_btn));
        }

    }
}
