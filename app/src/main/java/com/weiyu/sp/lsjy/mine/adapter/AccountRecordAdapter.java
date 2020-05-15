package com.weiyu.sp.lsjy.mine.adapter;

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.bean.AccountRecordBean;

import java.util.List;

public class AccountRecordAdapter extends BaseQuickAdapter<AccountRecordBean, BaseViewHolder> {
    private boolean redFlag;
    public AccountRecordAdapter(@Nullable List<AccountRecordBean> data, boolean redFlag) {
        super(R.layout.record_item, data);
        this.redFlag=redFlag;
    }

    @Override
    protected void convert(BaseViewHolder helper, AccountRecordBean item) {
        helper.setText(R.id.titleName,item.getTitleName())
                .setText(R.id.bsDesc,item.getBsDesc())
                .setText(R.id.successName,item.getSuccessName())
                .setText(R.id.created,item.getCreated());
        TextView tv_amount=helper.getView(R.id.tv_amount);
        if (redFlag){
            tv_amount.setText(item.getAmount());
            tv_amount.setTextColor(ContextCompat.getColor(mContext,R.color.color_FC5656));
        }else {
            tv_amount.setText(item.getAmount());
            tv_amount.setTextColor(ContextCompat.getColor(mContext,R.color.color_6082E5));
        }
    }
}
