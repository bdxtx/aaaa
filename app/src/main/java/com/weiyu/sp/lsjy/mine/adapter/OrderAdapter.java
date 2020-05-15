package com.weiyu.sp.lsjy.mine.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.bean.IntegralRecordBean;
import com.weiyu.sp.lsjy.bean.OrderBean;

import java.util.List;

public class OrderAdapter extends BaseQuickAdapter<OrderBean, BaseViewHolder> {
    public OrderAdapter(@Nullable List<OrderBean> data) {
        super(R.layout.order_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderBean item) {
        helper.setText(R.id.title,item.getTitleName());
        helper.setText(R.id.price,item.getAmount());
        helper.setText(R.id.tv_time,item.getCreated());
        ImageView img=helper.getView(R.id.img);
        RequestOptions options=new RequestOptions();
        options.placeholder(R.drawable.no_order_pic_zw).fallback(R.drawable.no_order_pic_zw).error(R.drawable.no_order_pic_zw);
        options.transform(new CircleCrop());
        Glide.with(mContext).load(item.getUrl()).apply(options).into(img);
    }
}
