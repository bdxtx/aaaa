package com.weiyu.sp.lsjy.vip.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.bean.PayChannel;

import java.util.List;

public class PayChannelAdapter extends BaseQuickAdapter<PayChannel, BaseViewHolder> {
    public PayChannelAdapter( @Nullable List<PayChannel> data) {
        super(R.layout.pay_channel_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PayChannel item) {
        ImageView img=helper.getView(R.id.url);
        Glide.with(mContext).load(item.getPaymentUrl()).into(img);
        helper.setText(R.id.pay_name,item.getPaymentName());
        ImageView paySelect=helper.getView(R.id.pay_select);

        if (item.isSelect()){
            Glide.with(mContext).load(R.drawable.pay_select).into(paySelect);
        }else {
            Glide.with(mContext).load(R.drawable.pay_select_un).into(paySelect);
        }
    }
}
