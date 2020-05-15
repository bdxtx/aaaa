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
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.CourseCollect;
import com.weiyu.sp.lsjy.utils.GlideRoundImage;

import java.util.List;

public class MainAdapter extends BaseQuickAdapter<CourseBean, BaseViewHolder> {
    public MainAdapter(@Nullable List<CourseBean> data) {
        super(R.layout.hot_class_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseBean item) {
        ImageView pic=helper.getView(R.id.img_class);
//        RequestOptions options=new RequestOptions();
//        GlideRoundImage glideRoundImage=new GlideRoundImage(5);
//        options.transform(glideRoundImage);
        RequestOptions options=new RequestOptions();
//        GlideRoundImage glideRoundImage=new GlideRoundImage(10);
        RoundedCorners roundedCorners=new RoundedCorners(15);
        options.fallback(R.drawable.default_1).error(R.drawable.default_1).placeholder(R.drawable.default_1);
        options.transform(new MultiTransformation<>(new CenterCrop(),roundedCorners));
        Glide.with(mContext).load(item.getUrl()).apply(options).into(pic);
//        helper.setText(R.id.title,"螺蛳"+item.getSubjectName()+"-"+item.getSchoolYear()+item.getBooksNumber()+"-"+item.getCurriculum()+"-"+item.getSubject());
        helper.setText(R.id.title,item.getTitle());
        helper.setText(R.id.price,"￥"+item.getMinPrice()).setText(R.id.read_num,item.getReadNumber()+"人学过");
    }
}
