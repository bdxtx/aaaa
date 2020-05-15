package com.weiyu.sp.lsjy.mine.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.bean.IntegralListBean;
import com.weiyu.sp.lsjy.utils.GlideLeftRoundTransform;

import java.util.List;

public class IntegralAdapter extends BaseQuickAdapter<IntegralListBean, BaseViewHolder> {
    public IntegralAdapter(@Nullable List<IntegralListBean> data) {
        super(R.layout.integral_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IntegralListBean item) {
        ImageView img=helper.getView(R.id.img);
        GlideLeftRoundTransform glideLeftRoundTransform=new GlideLeftRoundTransform(10);
        glideLeftRoundTransform.setExceptCorner(false,false,true,true);
        RequestOptions options=new RequestOptions();
        options.placeholder(R.drawable.no_order_pic);
        options.error(R.drawable.no_order_pic).fallback(R.drawable.no_order_pic);
        options.transform(new MultiTransformation<>(new CenterCrop(),glideLeftRoundTransform));
        Glide.with(mContext).load(item.getPicture()).apply(options).into(img);
        helper.setText(R.id.name,item.getGoodsName())
                .setText(R.id.limit,"剩余"+item.getNumber()+"件")
                .setText(R.id.tv_integral,item.getPrice());

        helper.addOnClickListener(R.id.exchange);
    }
}
