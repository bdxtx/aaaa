package com.weiyu.sp.lsjy.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseMvpFragment;
import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.bean.StudyBean;
import com.weiyu.sp.lsjy.bean.StudyListBean;
import com.weiyu.sp.lsjy.course.CourseDetailActivity;
import com.weiyu.sp.lsjy.course.CourseListFragment;
import com.weiyu.sp.lsjy.course.CourseListPresenter;
import com.weiyu.sp.lsjy.course.adapter.CourseAdapter;
import com.weiyu.sp.lsjy.course.adapter.CourseItemDecoration;
import com.weiyu.sp.lsjy.main.adapter.MainAdapter;
import com.weiyu.sp.lsjy.main.adapter.StudyItemDecoration;
import com.weiyu.sp.lsjy.utils.GlideRoundImage;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class StudyListFragment extends BaseMvpFragment<StudyPresenter> implements StudyContract.View, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.ll_no_data_home)
    LinearLayout ll_no_data_home;

    private int subjectCode;
    private int page=1;
    private int flag;
    private CourseBean mCourseBean;
    private List<CourseBean> mBeanList;
    private MainAdapter adapter;
    private boolean isAdded;
    private String url;

    public static StudyListFragment newInstance(int subjectCode, int flag){
        StudyListFragment courseListFragment=new StudyListFragment();
        Bundle args = new Bundle();
        args.putInt("subjectCode",subjectCode);
        args.putInt("flag",flag);
        courseListFragment.setArguments(args);
        return courseListFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        page=1;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_study_list;
    }

    @Override
    protected void initView(View view) {
        Bundle bundle=getArguments();
        subjectCode = bundle.getInt("subjectCode");
        flag = bundle.getInt("flag");
        getData();
        List<CourseBean>list=new ArrayList<>();
        adapter = new MainAdapter(list);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(),2);
        rv.addItemDecoration(new StudyItemDecoration());
        rv.setLayoutManager(linearLayoutManager);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this,rv);
        adapter.setOnLoadMoreListener(this,rv);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CourseBean courseBean= (CourseBean) adapter.getData().get(position);
                Intent intent=new Intent(getActivity(), CourseDetailActivity.class);
                intent.putExtra("courseId",courseBean.getId());
                intent.putExtra("title",courseBean.getTitle());
                startActivity(intent);
            }
        });
    }

    @Override
    protected StudyPresenter setMPresenter() {
        StudyPresenter courseListPresenter=new StudyPresenter();
        courseListPresenter.attachView(this);
        return courseListPresenter;
    }
    private void getData(){
        Map<String,String>map=new HashMap<>();
        map.put("limit","10");
        map.put("page",page+"");
        map.put("recent","true");
        if (subjectCode==0){
            mPresenter.selectCourseList(map);
        }else {
            map.put("subjectCode",subjectCode+"");
            mPresenter.selectCourseList(map);
        }
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
        if (flag==0){
//            rv.setVisibility(View.GONE);
            ll_no_data_home.setVisibility(View.VISIBLE);
        }
        if (flag==3){
            EventBus.getDefault().post(new MessageEvent("", EventBusTag.NoNetWork));
        }
    }

    @Override
    public void onSelectCourseList(BaseObjectBean<StudyListBean> bean) {
        EventBus.getDefault().post(new MessageEvent("",EventBusTag.NetWorkLink));
        mBeanList = bean.getRows().getShiPingListResponses();
        url = bean.getRows().getPicture();
        rv.setVisibility(View.VISIBLE);
        ll_no_data_home.setVisibility(View.GONE);
        adapter.loadMoreComplete();
        if (page==1){
            adapter.setNewData(mBeanList);
        }else {
            adapter.addData(mBeanList);
        }
        if (!mPresenter.loadMoreAble(page)){
            adapter.loadMoreEnd();
            if (!isAdded){
                adapter.addFooterView(getFoot());
            }
        }
    }

    @Override
    public void studyLength(StudyBean studyBean) {

    }
    private View getFoot(){
        isAdded = true;
        View view=getLayoutInflater().inflate(R.layout.study_footer,null);
        ImageView foot=view.findViewById(R.id.foot);
        RequestOptions options=new RequestOptions();
//        GlideRoundImage glideRoundImage=new GlideRoundImage(10);
        RoundedCorners roundedCorners=new RoundedCorners(25);
        options.fallback(R.drawable.banner_default).error(R.drawable.banner_default);
        options.transform(new MultiTransformation<>(new CenterCrop(),roundedCorners));
        Glide.with(this).load(url).apply(options).into(foot);
        return view;
    }

    @Override
    public void onLoadMoreRequested() {
        if (mPresenter.loadMoreAble(page)){
            page++;
            getData();
        }else {
            adapter.loadMoreEnd();
            if (!isAdded){
                adapter.addFooterView(getFoot());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (EventBusTag.RefreshStudyList.equals(messageEvent.getTag())){
            refresh();
        }
    }

    private void refresh(){
        page=1;
        getData();
    }
}
