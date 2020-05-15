package com.weiyu.sp.lsjy.course;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseMvpFragment;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.course.adapter.CourseAdapter;
import com.weiyu.sp.lsjy.course.adapter.CourseItemDecoration;
import com.weiyu.sp.lsjy.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseListFragment extends BaseMvpFragment<CourseListPresenter> implements CourseListContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.ll_no_data_home)
    LinearLayout ll_no_data_home;

    private CourseAdapter courseAdapter;
    private int subjectCode;
    private int page=1;
    private int flag;
    private CourseBean mCourseBean;
    private List<CourseBean> mBeanList;

    public static CourseListFragment newInstance(int subjectCode,int flag){
        CourseListFragment courseListFragment=new CourseListFragment();
        Bundle args = new Bundle();
        args.putInt("subjectCode",subjectCode);
        args.putInt("flag",flag);
        courseListFragment.setArguments(args);
        return courseListFragment;
    }

    public CourseListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        page=1;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_course_list;
    }

    @Override
    protected void initView(View view) {
        smart.autoRefresh();
        Bundle bundle=getArguments();
        subjectCode = bundle.getInt("subjectCode");
        flag = bundle.getInt("flag");
//        getData();
        List<CourseBean>list=new ArrayList<>();
        courseAdapter = new CourseAdapter(list);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.addItemDecoration(new CourseItemDecoration());
        rv.setAdapter(courseAdapter);
        smart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smart.finishRefresh(1500);
                refresh();
            }
        });
        courseAdapter.setOnLoadMoreListener(this,rv);
        courseAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<CourseBean> list1=adapter.getData();
                mCourseBean = list1.get(position);
                Map<String,String>map=new HashMap<>();
                map.put("id",mCourseBean.getId());
                map.put("collection",mCourseBean.isCollection()+"");
                mPresenter.courseCollection(map,position);
                if (flag== Constant.SHOU_CANG){
                    list1.remove(mCourseBean);
                    courseAdapter.notifyDataSetChanged();
                }

            }
        });
        courseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CourseBean courseBean= (CourseBean) adapter.getData().get(position);
                Intent intent=new Intent(getActivity(),CourseDetailActivity.class);
                intent.putExtra("courseId",courseBean.getId());
                intent.putExtra("title",courseBean.getTitle());
                startActivity(intent);
            }
        });
    }

    @Override
    protected CourseListPresenter setMPresenter() {
        CourseListPresenter courseListPresenter=new CourseListPresenter();
        courseListPresenter.attachView(this);
        return courseListPresenter;
    }
    private void getData(){
        Map<String,String>map=new HashMap<>();
        map.put("limit","10");
        map.put("page",page+"");
        if (subjectCode==0){
            if (flag==1){
                mPresenter.selectCourseList2(map);
            }else{
                mPresenter.selectCourseList(map);
            }
        }else {
            map.put("subjectCode",subjectCode+"");
            if (flag==1){
                mPresenter.selectCourseList2(map);
            }else {
                mPresenter.selectCourseList(map);
            }
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
        smart.finishRefresh();
        courseAdapter.loadMoreFail();
        if (flag==0&&this.flag==1){
            rv.setVisibility(View.GONE);
            ll_no_data.setVisibility(View.VISIBLE);
        }
        if (flag==2){
            rv.setVisibility(View.GONE);
            ll_no_data_home.setVisibility(View.VISIBLE);
        }
        if (flag==3){
            if (mBeanList==null){
                EventBus.getDefault().post(new MessageEvent("",EventBusTag.NoNetWork));
            }else {
                ToastUtil.show("当前网络不可用，请稍候再试");
            }
        }
//        mBeanList=null;
    }

    @Override
    public void onSelectCourseList(List<CourseBean> beanList) {
        EventBus.getDefault().post(new MessageEvent("",EventBusTag.NetWorkLink));
        mBeanList = beanList;
        rv.setVisibility(View.VISIBLE);
        ll_no_data.setVisibility(View.GONE);
        ll_no_data_home.setVisibility(View.GONE);
        smart.finishRefresh();
        courseAdapter.loadMoreComplete();
        if (page==1){
            courseAdapter.setNewData(beanList);
        }else {
            courseAdapter.addData(beanList);
        }
        if (!mPresenter.loadMoreAble(page)){
            courseAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onClickCollection(int position) {
        if (mCourseBean.isCollection()){
            ToastUtil.show("已取消");
        }else {
            ToastUtil.show("已收藏");
        }
        if (flag!=Constant.SHOU_CANG){
            mCourseBean.setCollection(!mCourseBean.isCollection());
            courseAdapter.notifyItemChanged(position);
        }else {
        }

    }

    @Override
    public void onLoadMoreRequested() {
        if (mPresenter.loadMoreAble(page)){
            page++;
            getData();
        }else {
            courseAdapter.loadMoreEnd();
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
        if (EventBusTag.CourseSelectToActivity.equals(messageEvent.getTag())||EventBusTag.CourseSelectToFragment.equals(messageEvent.getTag())){
            CourseBean courseBean= (CourseBean) messageEvent.getMessage();
            if (courseBean!=null){
                if (courseBean.getSubjectCode()==subjectCode){
                    page=1;
                    Map<String,String>map=new HashMap<>();
                    map.put("limit","10");
                    map.put("page",page+"");
                    map.put("booksNumber",courseBean.getBooksNumber());
                    map.put("curriculum",courseBean.getCurriculum());
                    map.put("schoolYear",courseBean.getSchoolYear());
                    if (subjectCode==0){
                        mPresenter.selectCourseList(map);
                    }else {
                        map.put("subjectCode",subjectCode+"");
                        mPresenter.selectCourseList(map);
                    }
                }
            }
        }else if (EventBusTag.MainRefreshCourseList.equals(messageEvent.getTag())){
            if (EventBusTag.LoginRefresh.equals(messageEvent.getMessage())){
                if (mBeanList==null){
                    refresh();
                }
            }
        }
    }

    private void refresh(){
        page=1;
        getData();
    }
}
