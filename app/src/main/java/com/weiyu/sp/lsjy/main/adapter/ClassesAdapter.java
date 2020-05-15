package com.weiyu.sp.lsjy.main.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.bean.CurriculumResponse;
import com.weiyu.sp.lsjy.utils.GlideRoundImage;

import java.util.List;

public class ClassesAdapter extends BaseQuickAdapter<CurriculumResponse, BaseViewHolder> {
    public ClassesAdapter(@Nullable List<CurriculumResponse> data) {
        super(R.layout.class_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CurriculumResponse item) {
        ImageView img_class=helper.getView(R.id.img_class);
        RequestOptions options=new RequestOptions();
        options.placeholder(R.drawable.banner_default).fallback(R.drawable.banner_default).error(R.drawable.banner_default);
        Glide.with(mContext).load(item.getUrl()).apply(options).into(img_class);
    }
}
