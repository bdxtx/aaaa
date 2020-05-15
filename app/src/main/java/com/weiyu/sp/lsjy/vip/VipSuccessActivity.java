package com.weiyu.sp.lsjy.vip;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseActivity;
import com.weiyu.sp.lsjy.course.CourseDetailActivity;
import com.weiyu.sp.lsjy.main.MainActivity;
import com.weiyu.sp.lsjy.view.dialog.VipTypeSelectDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class VipSuccessActivity extends BaseActivity {
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.look)
    TextView look;
    @BindView(R.id.back_to_home)
    TextView back_to_home;
    private String courseId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_vip_success;
    }

    @Override
    public void initView() {
        Intent intent=getIntent();
        String from=intent.getStringExtra("from");
        courseId = intent.getStringExtra("courseId");
        if (courseId!=null){
            if ("vip".equals(from)){
                back_to_home.setVisibility(View.VISIBLE);
            }else {
                back_to_home.setVisibility(View.GONE);
            }
            look.setVisibility(View.VISIBLE);
        }else {
            back_to_home.setVisibility(View.VISIBLE);
            look.setVisibility(View.GONE);
        }

        queryStatus();
    }

    @OnClick({R.id.back,R.id.look,R.id.back_to_home})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.look:
                Intent intent=new Intent(VipSuccessActivity.this, CourseDetailActivity.class);
                intent.putExtra("courseId",courseId);
                startActivity(intent);
                break;
            case R.id.back_to_home:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    private Handler mHandler = new Handler();
    private int mCountTime = 10;
    /*
           倒计时，并处理点击事件
        */
    private void queryStatus() {
        mHandler.postDelayed(run, 0);
    }

    /*
        倒计时
     */
    private Runnable run=new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            if (mCountTime > 0) {
                time.setText(mCountTime+"s后自动跳转观看");
                mHandler.postDelayed(this, 1000);
            } else {
                time.setText("0s后自动跳转观看");
                removeRunable();
                if (TextUtils.isEmpty(courseId)){
                    startActivity(new Intent(VipSuccessActivity.this, MainActivity.class));
                }else {
                    Intent intent=new Intent(VipSuccessActivity.this, CourseDetailActivity.class);
                    intent.putExtra("courseId",courseId);
                    startActivity(intent);
                }

            }
            mCountTime--;
        }
    };

    private void  removeRunable() {
        mHandler.removeCallbacks(run);
    }

    @Override
    public void onDestroy() {
        removeRunable();
        super.onDestroy();
    }


}
