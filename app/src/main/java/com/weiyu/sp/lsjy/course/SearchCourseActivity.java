package com.weiyu.sp.lsjy.course;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseMvpActivity;
import com.weiyu.sp.lsjy.base.BasePresenter;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.course.adapter.CourseAdapter;
import com.weiyu.sp.lsjy.course.adapter.CourseItemDecoration;
import com.weiyu.sp.lsjy.utils.SPUtils;
import com.weiyu.sp.lsjy.utils.ToastUtil;
import com.weiyu.sp.lsjy.view.dialog.LoadingDialog;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchCourseActivity extends BaseMvpActivity<SearchCoursePresenter> implements SearchCourseContract.View, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;
    @BindView(R.id.id_flowlayout2)
    TagFlowLayout mFlowLayout2;
    @BindView(R.id.et_Search)
    EditText etSearch;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.fl_search)
    FrameLayout fl_search;

    private CourseAdapter courseAdapter;
    private int page=1;
    private boolean isVisiable=false;

    private String lastTxt;




    private String[] mVals = new String[]{};
    private LayoutInflater mInflater;
    private LoadingDialog loadingDialog;


    @Override
    public int getLayoutId() {
        return R.layout.activity_search_course;
    }

    @Override
    public void initView() {
        mInflater = LayoutInflater.from(this);
        showSearchBefore();
        mPresenter.hotSearch();
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH){
                    page=1;
                    getData();
                    return true;
                }
                return false;
            }
        });
        ll_search.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        List<CourseBean>list2=new ArrayList<>();
        courseAdapter = new CourseAdapter(list2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new CourseItemDecoration());
        recyclerView.setAdapter(courseAdapter);
        smart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                getData();
            }
        });
        courseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CourseBean courseBean= (CourseBean) adapter.getData().get(position);
                Intent intent=new Intent(SearchCourseActivity.this,CourseDetailActivity.class);
                intent.putExtra("courseId",courseBean.getId());
                intent.putExtra("title",courseBean.getTitle());
                startActivity(intent);
            }
        });
        courseAdapter.setOnLoadMoreListener(this,recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisiable=true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisiable=false;
    }

    private void getData(){
        Map<String,String> map=new HashMap<>();
        map.put("limit","10");
        map.put("page",page+"");
        map.put("keyword",etSearch.getText().toString());
        mPresenter.selectCourseList(map);
        if (!TextUtils.isEmpty(etSearch.getText().toString())){
            if (!etSearch.getText().toString().equals(lastTxt)){
                lastTxt=etSearch.getText().toString();
                saveSearch(etSearch.getText().toString());
            }
        }
    }

    private void showSearchBefore(){
        List<String> list=getSearchBefore();
        if (list==null){
            fl_search.setVisibility(View.GONE);
            list=new ArrayList<>();
        }else {
            fl_search.setVisibility(View.VISIBLE);
        }
        mVals=list.toArray(new String[0]);
        mFlowLayout.setAdapter(new TagAdapter<String>(mVals)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                        mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                etSearch.setText(mVals[position]);
                page=1;
                getData();
                return true;
            }
        });
    }

    private void saveSearch(String searchStr){
        String searchJson=SPUtils.getStringData("searchBefore");
        Gson gson=new Gson();
        List<String>list=new ArrayList<>();
        List<String>list1=gson.fromJson(searchJson, new TypeToken<List<String>>(){}.getType());
        if (list1!=null){
            list.addAll(list1);
        }
        if (list!=null&&list.contains(searchStr)){
            list.remove(searchStr);
        }
        list.add(0,searchStr);
        if (list.size()>9){
            list.remove(9);
        }
        SPUtils.saveData("searchBefore",gson.toJson(list));
        showSearchBefore();
    }
    private List<String> getSearchBefore(){
        String searchJson=SPUtils.getStringData("searchBefore");
        List<String>list=new Gson().fromJson(searchJson, new TypeToken<List<String>>(){}.getType());
        return list;
    }

    private void clearSearch(){
        SPUtils.saveData("searchBefore","");
        showSearchBefore();
    }

    @Override
    protected SearchCoursePresenter setMPresenter() {
        SearchCoursePresenter searchCoursePresenter=new SearchCoursePresenter();
        searchCoursePresenter.attachView(this);
        return searchCoursePresenter;
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

    }

    @Override
    public void onGetHotSearch(List<String> bean) {
        String[] arr= bean.toArray(new String[0]);
        mFlowLayout2.setAdapter(new TagAdapter<String>(arr)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                        mFlowLayout2, false);
                tv.setText(s);
                return tv;
            }
        });
        mFlowLayout2.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                etSearch.setText(bean.get(position));
                page=1;
                getData();
                return true;
            }
        });

    }

    @Override
    public void onSelectCourseList(List<CourseBean> beanList) {
        smart.finishRefresh();
        ll_search.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        if (page==1){
            if (beanList.size()==0){
                ll_search.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                ToastUtil.show("没搜到相关内容");
            }
            courseAdapter.setNewData(beanList);
        }else {
            courseAdapter.addData(beanList);
        }
        if (!mPresenter.loadMoreAble(page)){
            courseAdapter.loadMoreEnd();
        }
    }

    @OnClick({R.id.clear,R.id.back,R.id.img_search})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.clear:
                clearSearch();
                break;
            case R.id.back:
                if (courseAdapter.getData().size()>0){
                    List<CourseBean> list=new ArrayList<>();
                    courseAdapter.setNewData(list);
                    ll_search.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }else {
                    finish();
                }
                break;
            case R.id.img_search:
                page=1;
                getData();
                break;
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

}
