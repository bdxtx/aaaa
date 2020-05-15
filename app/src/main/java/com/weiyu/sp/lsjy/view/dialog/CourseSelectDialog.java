package com.weiyu.sp.lsjy.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CourseSelectDialog extends DialogFragment {

    private Dialog dialog;
    private String isLowest;
    private Unbinder unbinder;

    @BindView(R.id.tagFlowLayout)
    TagFlowLayout tagFlowLayout;
    @BindView(R.id.tagFlowLayout2)
    TagFlowLayout tagFlowLayout2;
    @BindView(R.id.tagFlowLayout3)
    TagFlowLayout tagFlowLayout3;
    @BindView(R.id.tagFlowLayout4)
    TagFlowLayout tagFlowLayout4;

    String[] subjectName=new String[]{"语文","历史","地理","名著导读"};
    String[] schoolYearArr=new String[]{"一年级","二年级","三年级","四年级","五年级","六年级","七年级","八年级","九年级","十年级","十一年级","十二年级"};
    String[] booksNumberArr=new String[]{"上册","下册"};
    String[] curriculumArr=new String[]{"第一课","第二课","第三课","第四课","第五课","第六课","第七课","第八课","八课以上"};
    int subjectCode=0;
    String schoolYear="";
    String booksNumber="";
    String curriculum="";
    private int flag;


    public static CourseSelectDialog newInstance(int flag){
        CourseSelectDialog courseSelectDialog=new CourseSelectDialog();
        Bundle bundle=new Bundle();
        bundle.putInt("flag",flag);
        courseSelectDialog.setArguments(bundle);
        return courseSelectDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        flag = bundle.getInt("flag");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.select_dialog);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消

        //控件初始化
        unbinder = ButterKnife.bind(this,dialog);

        LayoutInflater mInflater = LayoutInflater.from(dialog.getContext());

        tagFlowLayout.setAdapter(new TagAdapter<String>(subjectName)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.course_condition,
                        tagFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });
        tagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                Iterator<Integer> it =selectPosSet.iterator();
                if (it.hasNext()){
                    int value=it.next();
                    if (value==0){
                        subjectCode=700;
                    }else if (value==1){
                        subjectCode=701;
                    }else if (value==2){
                        subjectCode=702;
                    }else {
                        subjectCode=703;
                    }
                }
            }
        });
        tagFlowLayout2.setAdapter(new TagAdapter<String>(schoolYearArr)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.course_condition,
                        tagFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });
        tagFlowLayout2.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                Iterator<Integer> it =selectPosSet.iterator();
                if (it.hasNext()){
                    int value=it.next();
                    schoolYear=schoolYearArr[value];
                }
            }
        });
        tagFlowLayout3.setAdapter(new TagAdapter<String>(booksNumberArr)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.course_condition,
                        tagFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });
        tagFlowLayout3.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                Iterator<Integer> it =selectPosSet.iterator();
                if (it.hasNext()){
                    int value=it.next();
                    booksNumber=booksNumberArr[value];
                }
            }
        });
        tagFlowLayout4.setAdapter(new TagAdapter<String>(curriculumArr)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.course_condition,
                        tagFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });
        tagFlowLayout4.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                Iterator<Integer> it =selectPosSet.iterator();
                if (it.hasNext()){
                    int value=it.next();
                    curriculum=curriculumArr[value];
                }
            }
        });

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        return dialog;
    }
    @OnClick({R.id.tv_cancel,R.id.tv_ok})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_ok:
                CourseBean courseBean=new CourseBean();
                courseBean.setBooksNumber(booksNumber);
                courseBean.setCurriculum(curriculum);
                courseBean.setSchoolYear(schoolYear);
                courseBean.setSubjectCode(subjectCode);
                if (flag==1){
                    EventBus.getDefault().post(new MessageEvent(courseBean, EventBusTag.CourseSelectToActivity));
                }else {
                    EventBus.getDefault().post(new MessageEvent(courseBean, EventBusTag.CourseSelectToFragment));
                }
                dismiss();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onDestroyView() {
        if (getView() instanceof ViewGroup) {
            ((ViewGroup)getView()).removeAllViews();
        }
        super.onDestroyView();
    }


}
