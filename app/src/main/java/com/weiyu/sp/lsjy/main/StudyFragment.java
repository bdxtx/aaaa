package com.weiyu.sp.lsjy.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseFragment;
import com.weiyu.sp.lsjy.base.BaseMvpFragment;
import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.BasePagerAdapter;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.base.WebActivity;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.bean.StudyBean;
import com.weiyu.sp.lsjy.bean.StudyLengthBean;
import com.weiyu.sp.lsjy.bean.StudyListBean;
import com.weiyu.sp.lsjy.course.CourseDetailActivity;
import com.weiyu.sp.lsjy.course.CourseListFragment;
import com.weiyu.sp.lsjy.login.LoginActivity;
import com.weiyu.sp.lsjy.main.adapter.HotClassItemDecoration;
import com.weiyu.sp.lsjy.main.adapter.MainAdapter;
import com.weiyu.sp.lsjy.main.adapter.StudyItemDecoration;
import com.weiyu.sp.lsjy.utils.GlideRoundImage;
import com.weiyu.sp.lsjy.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudyFragment extends BaseMvpFragment<StudyPresenter>implements StudyContract.View{

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.chart1)
    LineChart mLineChart;
    @BindView(R.id.tv_desc)
    TextView tv_desc;
    @BindView(R.id.ll_no_network)
    LinearLayout ll_no_network;
    @BindView(R.id.fl_title)
    FrameLayout fl_title;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;


    String[]titleArr={"全部","语文","历史","地理","名著导读"};
    List<String>tabTitle;
    ArrayList<BaseFragment>fragments;
    private ArrayList<Entry> mValues;

    private boolean isLoaded=false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_study;
    }

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);
        tabTitle= Arrays.asList(titleArr);
        fragments=new ArrayList<>();
        fragments.add(StudyListFragment.newInstance(0,0));
        fragments.add(StudyListFragment.newInstance(700,0));
        fragments.add(StudyListFragment.newInstance(701,0));
        fragments.add(StudyListFragment.newInstance(702,0));
        fragments.add(StudyListFragment.newInstance(703,0));
        viewPager.setAdapter(new BasePagerAdapter(getChildFragmentManager(),fragments,tabTitle));
        viewPager.setOffscreenPageLimit(titleArr.length);
        tabLayout.setupWithViewPager(viewPager);
        smart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smart.finishRefresh(1000);
                refresh();
            }
        });
        smart.autoRefresh();
        onInitChart();

    }

    @Override
    protected StudyPresenter setMPresenter() {
        StudyPresenter studyPresenter=new StudyPresenter();
        studyPresenter.attachView(this);
        return studyPresenter;
    }


    private void onInitChart(){
        mLineChart.getDescription().setEnabled(false);//取消描述文字
        mLineChart.setNoDataText("没有数据");//没有数据时显示的文字
        mLineChart.setNoDataTextColor(Color.WHITE);//没有数据时显示文字的颜色
        mLineChart.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
        mLineChart.setDrawBorders(false);//是否禁止绘制图表边框的线
        mLineChart.setBorderColor(Color.BLACK); //设置 chart 边框线的颜色。
        mLineChart.setBorderWidth(3f); //设置 chart 边界线的宽度，单位 dp。
        mLineChart.setTouchEnabled(true);     //能否点击
        mLineChart.setDragEnabled(false);   //能否拖拽
        mLineChart.setScaleEnabled(false);  //能否缩放
        mLineChart.animateX(1000);//绘制动画 从左到右
        mLineChart.setDoubleTapToZoomEnabled(false);//设置是否可以通过双击屏幕放大图表。默认是true
        mLineChart.setHighlightPerDragEnabled(false);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        mLineChart.setDragDecelerationEnabled(false);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）

//        MyMarkerView mv = new MyMarkerView(this,
//                R.layout.custom_marker_view);
//        mv.setChartView(mLineChart); // For bounds control
//        mLineChart.setMarker(mv);        //设置 marker ,点击后显示的功能 ，布局可以自定义

        XAxis xAxis = mLineChart.getXAxis();       //获取x轴线
        xAxis.setDrawAxisLine(true);//是否绘制轴线
        xAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setTextSize(11f);//设置文字大小
        xAxis.setAxisMinimum(0f);//设置x轴的最小值 //`
        xAxis.setAxisMaximum(6f);//设置最大值 //
//        xAxis.setLabelCount(10);  //设置X轴的显示个数
        xAxis.setAvoidFirstLastClipping(false);//图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int time=(int) value;
                if (mValues.size()>time){
                    return mValues.get(time).getData()+"";
                }else {
                    return "";
                }
            }
        });

        xAxis.setAxisLineColor(Color.parseColor("#FFA62F"));//设置x轴线颜色
        xAxis.setAxisLineWidth(0.5f);//设置x轴线宽度
        YAxis leftAxis = mLineChart.getAxisLeft();
        YAxis axisRight = mLineChart.getAxisRight();
        leftAxis.enableGridDashedLine(10f, 10f, 0f);  //设置Y轴网格线条的虚线，参1 实线长度，参2 虚线长度 ，参3 周期
        leftAxis.setGranularity(20f); // 网格线条间距
        axisRight.setEnabled(false);   //设置是否使用 Y轴右边的
        leftAxis.setEnabled(true);     //设置是否使用 Y轴左边的
        leftAxis.setGridColor(Color.parseColor("#7189a9"));  //网格线条的颜色
        leftAxis.setDrawLabels(false);        //是否显示Y轴刻度
        leftAxis.setStartAtZero(true);        //设置Y轴数值 从零开始
        leftAxis.setDrawGridLines(true);      //是否使用 Y轴网格线条
        leftAxis.setTextSize(12f);            //设置Y轴刻度字体
        leftAxis.setTextColor(Color.BLACK);   //设置字体颜色
        leftAxis.setAxisLineColor(Color.BLACK); //设置Y轴颜色
        leftAxis.setAxisLineWidth(0.5f);
        leftAxis.setDrawAxisLine(false);//是否绘制轴线
        leftAxis.setMinWidth(0f);
        leftAxis.setMaxWidth(200f);
        leftAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        Legend l = mLineChart.getLegend();//图例
        l.setEnabled(false);   //是否使用 图例

    }

    @Override
    public void studyLength(StudyBean studyBean) {
        coordinator.setVisibility(View.VISIBLE);
        ll_no_network.setVisibility(View.GONE);
        isLoaded=true;
        List<StudyLengthBean> studyLengthBeanList=studyBean.getWeek();
        tv_desc.setText(studyBean.getInfo());
        mValues = new ArrayList<>();
        for (int i=0;i<studyLengthBeanList.size();i++){
            float y=Float.parseFloat(studyLengthBeanList.get(i).getY());
            mValues.add(new Entry(i, y,studyLengthBeanList.get(i).getX()));
        }
        if (mValues.size() == 0) return;
        LineDataSet set1;
        //设置数据1  参数1：数据源 参数2：图例名称
        set1 = new LineDataSet(mValues, "测试数据1");
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setColor(Color.parseColor("#FFA62F"));
        set1.setCircleColor(Color.parseColor("#FFA62F"));
        set1.setHighLightColor(Color.WHITE);//设置点击交点后显示交高亮线的颜色
        set1.setHighlightEnabled(true);//是否使用点击高亮线
        set1.setDrawCircles(true);
        set1.setValueTextColor(Color.parseColor("#555555"));
        set1.setLineWidth(1f);//设置线宽
        set1.setCircleRadius(3f);//设置焦点圆心的大小
        set1.setDrawCircleHole(false);
        set1.setHighlightLineWidth(0.5f);//设置点击交点后显示高亮线宽
        set1.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
        set1.setValueTextSize(12f);//设置显示值的文字大小
        set1.setDrawFilled(false);//设置使用 范围背景填充

        set1.setDrawValues(true);
        //格式化显示数据
        final DecimalFormat mFormat = new DecimalFormat("###,###,##0");
//            set1.setValueFormatter(new IValueFormatter() {
//                @Override
//                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                    return mFormat.format(value);
//                }
//            });

        set1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return mFormat.format(value)+"'";
            }
        });
//            if (Utils.getSDKInt() >= 18) {
//                // fill drawable only supported on api level 18 and above
//                Drawable drawable = ContextCompat.getDrawable(mActivity, R.color.translucence);
//                set1.setFillDrawable(drawable);//设置范围背景填充
//            } else {
//                set1.setFillColor(R.color.translucence);
//            }
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the datasets
        //创建LineData对象 属于LineChart折线图的数据集合
        LineData data = new LineData(dataSets);
        // 添加到图表中
        mLineChart.setData(data);
        //绘制图表
        mLineChart.invalidate();
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    public void onError(Throwable throwable, int flag) {
        if (flag==1){
            ll_no_network.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSelectCourseList(BaseObjectBean<StudyListBean> beanList) {

    }


    @OnClick({R.id.service,R.id.tv_refresh})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.service:
                Intent intent=new Intent(getActivity(), WebActivity.class);
                if (!TextUtils.isEmpty(SPUtils.getStringData(Constant.SERVICE_URL))){
                    intent.putExtra(Constant.WEB_INTENT,SPUtils.getStringData(Constant.SERVICE_URL));
                    intent.putExtra("title","客服");
                    startActivity(intent);
                }
                break;
            case R.id.tv_refresh:
                smart.autoRefresh();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (EventBusTag.MainRefresh.equals(messageEvent.getTag())||EventBusTag.MainRefreshStudy.equals(messageEvent.getTag())){
            if (!isLoaded||ll_no_network.getVisibility()==View.VISIBLE){
                refresh();
            }
        }
    }
    private void refresh(){
        mPresenter.studyLength();
        EventBus.getDefault().post(new MessageEvent("",EventBusTag.RefreshStudyList));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
