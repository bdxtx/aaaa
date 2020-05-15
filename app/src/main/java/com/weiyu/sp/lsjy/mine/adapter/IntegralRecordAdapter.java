package com.weiyu.sp.lsjy.mine.adapter;

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.bean.AccountRecordBean;
import com.weiyu.sp.lsjy.bean.IntegralRecordBean;

import java.util.List;

public class IntegralRecordAdapter extends BaseQuickAdapter<IntegralRecordBean, BaseViewHolder> {
    public IntegralRecordAdapter(@Nullable List<IntegralRecordBean> data) {
        super(R.layout.integral_record_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IntegralRecordBean item) {
        helper.setText(R.id.titleName,item.getTitleName()+"")
                .setText(R.id.created,item.getCreated());
        TextView tv_amount=helper.getView(R.id.tv_amount);
        if (item.getAmount().startsWith("+")){
            tv_amount.setText(item.getAmount());
            tv_amount.setTextColor(ContextCompat.getColor(mContext,R.color.color_FC5656));
        }else {
            tv_amount.setText(item.getAmount());
            tv_amount.setTextColor(ContextCompat.getColor(mContext,R.color.color_6082E5));
        }
    }
}
