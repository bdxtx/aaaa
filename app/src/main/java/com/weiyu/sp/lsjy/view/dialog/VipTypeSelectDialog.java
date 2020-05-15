package com.weiyu.sp.lsjy.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.utils.ToastUtil;
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

public class VipTypeSelectDialog extends DialogFragment {

    private Dialog dialog;
    private String isLowest;
    private Unbinder unbinder;

    @BindView(R.id.tagFlowLayout)
    TagFlowLayout tagFlowLayout;
    @BindView(R.id.tagFlowLayout2)
    TagFlowLayout tagFlowLayout2;

    String[] subjectName=new String[]{"语文","历史","地理","名著导读"};
    String[] schoolYearArr=new String[]{"一年级","二年级","三年级","四年级","五年级","六年级","七年级","八年级","九年级","十年级","十一年级","十二年级"};
    int subjectCode=0;
    String schoolYear="";
//    String booksNumber="";
//    String curriculum="";
//    private int flag;


//    public static VipTypeSelectDialog newInstance(int flag){
//        VipTypeSelectDialog courseSelectDialog=new VipTypeSelectDialog();
//        Bundle bundle=new Bundle();
//        bundle.putInt("flag",flag);
//        courseSelectDialog.setArguments(bundle);
//        return courseSelectDialog;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bundle bundle=getArguments();
//        flag = bundle.getInt("flag");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.vip_select_dialog);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        //控件初始化
        unbinder = ButterKnife.bind(this,dialog);

        LayoutInflater mInflater = LayoutInflater.from(dialog.getContext());

        tagFlowLayout.setAdapter(new TagAdapter<String>(subjectName)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.vip_condition,
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
                    }else if (value==3){
                        subjectCode=703;
                    }
                    schoolYear="";
                    tagFlowLayout2.setAdapter(new TagAdapter<String>(schoolYearArr)
                    {
                        @Override
                        public View getView(FlowLayout parent, int position, String s)
                        {
                            TextView tv = (TextView) mInflater.inflate(R.layout.vip_condition,
                                    tagFlowLayout, false);
                            tv.setText(s);
                            return tv;
                        }
                    });
                }else {
                    subjectCode=0;
                }
            }
        });
        tagFlowLayout2.setAdapter(new TagAdapter<String>(schoolYearArr)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.vip_condition,
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
                    subjectCode=0;
                    tagFlowLayout.setAdapter(new TagAdapter<String>(subjectName)
                    {
                        @Override
                        public View getView(FlowLayout parent, int position, String s)
                        {
                            TextView tv = (TextView) mInflater.inflate(R.layout.vip_condition,
                                    tagFlowLayout, false);
                            tv.setText(s);
                            return tv;
                        }
                    });
                }else {
                    schoolYear="";
                }
            }
        });

//        // 设置宽度为屏宽, 靠近屏幕底部。
//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.gravity = Gravity.BOTTOM; // 紧贴底部
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
//        window.setAttributes(lp);
        return dialog;
    }
    @OnClick({R.id.tv_ok})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_ok:
                if (TextUtils.isEmpty(schoolYear)&&subjectCode==0){
                    ToastUtil.show("请先选择一项");
                    return;
                }
                CourseBean courseBean=new CourseBean();
                courseBean.setSchoolYear(schoolYear);
                courseBean.setSubjectCode(subjectCode);
                EventBus.getDefault().post(new MessageEvent(courseBean, EventBusTag.VipSelectToActivity));
                dismiss();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


}
