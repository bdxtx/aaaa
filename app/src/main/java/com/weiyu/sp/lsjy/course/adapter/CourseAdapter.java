package com.weiyu.sp.lsjy.course.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.CourseCollect;
import com.weiyu.sp.lsjy.utils.GlideRoundImage;

import java.util.List;

public class CourseAdapter extends BaseQuickAdapter<CourseBean, BaseViewHolder> {
    public CourseAdapter(@Nullable List<CourseBean> data) {
        super(R.layout.course_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseBean item) {
        ImageView pic=helper.getView(R.id.img_class);
        RequestOptions options=new RequestOptions();
        GlideRoundImage glideRoundImage=new GlideRoundImage(5);
        options.fallback(R.drawable.default_3).error(R.drawable.default_3).placeholder(R.drawable.default_3);
        options.transform(new MultiTransformation<>(new CenterCrop(),glideRoundImage));
        Glide.with(mContext).load(item.getUrl()).apply(options).into(pic);
        helper.setText(R.id.curriculum,item.getCurriculum());
//        helper.setText(R.id.title,"螺蛳"+item.getSubjectName()+"-"+item.getSchoolYear()+item.getBooksNumber()+"-"+item.getCurriculum()+"-"+item.getSubject());
        helper.setText(R.id.subject,item.getSubject());
        helper.setText(R.id.title,item.getTitle());
        helper.setText(R.id.price,"￥"+item.getMinPrice()).setText(R.id.read_num,item.getReadNumber()+"人学过");
        helper.addOnClickListener(R.id.ll_collect);

        ImageView img_like=helper.getView(R.id.img_like);
        if (item.isCollection()){
            Glide.with(mContext).load(R.drawable.like).into(img_like);
        }else {
            Glide.with(mContext).load(R.drawable.like_un).into(img_like);
        }

        TextView subject=helper.getView(R.id.subject);
        if (item.isHasBeanClicked()){
            subject.setTextColor(Color.parseColor("#ffffa62f"));
//            subject.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
        }else {
            subject.setTextColor(Color.parseColor("#333333"));
//            subject.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//
        }
        TextView subjectName=helper.getView(R.id.subjectName);
        TextView after_subject=helper.getView(R.id.after_subject);
        if (TextUtils.isEmpty(item.getSubjectName())){
            subjectName.setVisibility(View.GONE);
            after_subject.setVisibility(View.GONE);
        }else {
            subjectName.setVisibility(View.VISIBLE);
            after_subject.setVisibility(View.VISIBLE);
        }
        subjectName.setText(item.getSubjectName());
        TextView schoolYear=helper.getView(R.id.schoolYear);
        TextView after_school=helper.getView(R.id.after_school);
        if (TextUtils.isEmpty(item.getSchoolYear())){
            schoolYear.setVisibility(View.GONE);
            after_school.setVisibility(View.GONE);
        }else {
            schoolYear.setVisibility(View.VISIBLE);
            after_school.setVisibility(View.VISIBLE);
        }
        schoolYear.setText(item.getSchoolYear());
        TextView booksNumber=helper.getView(R.id.booksNumber);
        TextView after_book=helper.getView(R.id.after_book);
        if (TextUtils.isEmpty(item.getBooksNumber())){
            booksNumber.setVisibility(View.GONE);
            after_book.setVisibility(View.GONE);
        }else {
            booksNumber.setVisibility(View.VISIBLE);
            after_book.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(item.getCurriculum())){
            if (after_book.getVisibility()==View.VISIBLE){
                after_book.setVisibility(View.GONE);
            }else if (after_school.getVisibility()==View.VISIBLE){
                after_school.setVisibility(View.GONE);
            }else if (after_subject.getVisibility()==View.VISIBLE){
                after_subject.setVisibility(View.GONE);
            }
        }
    }
}
