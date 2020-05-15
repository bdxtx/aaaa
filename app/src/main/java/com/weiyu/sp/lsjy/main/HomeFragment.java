package com.weiyu.sp.lsjy.main;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.base.WebActivity;
import com.weiyu.sp.lsjy.bean.BannerBean;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.CourseCollect;
import com.weiyu.sp.lsjy.bean.CurriculumResponse;
import com.weiyu.sp.lsjy.bean.HomeBean;
import com.weiyu.sp.lsjy.bean.UpdateBean;
import com.weiyu.sp.lsjy.course.ClassActivity;
import com.weiyu.sp.lsjy.course.CourseDetailActivity;
import com.weiyu.sp.lsjy.course.SearchCourseActivity;
import com.weiyu.sp.lsjy.main.adapter.ClassItemDecoration;
import com.weiyu.sp.lsjy.main.adapter.ClassesAdapter;
import com.weiyu.sp.lsjy.main.adapter.HotClassItemDecoration;
import com.weiyu.sp.lsjy.main.adapter.MainAdapter;
import com.weiyu.sp.lsjy.utils.GlideRoundImage;
import com.weiyu.sp.lsjy.utils.SPUtils;
import com.weiyu.sp.lsjy.utils.ToastUtil;
import com.weiyu.sp.lsjy.view.dialog.AgreementDialog;
import com.weiyu.sp.lsjy.view.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseMvpFragment<MainPresenter>implements MainContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.smart)
    SmartRefreshLayout smartRefreshLayout;

    private MainPresenter mainPresenter;
    private BGABanner bgaBanner;
    private MainAdapter adapter;
    private ImageView img_new;
    private TextView title_new;
    private TextView num_new;
    private TextView price_new;
    private TextView price_new_2;
    private RecyclerView rvClass;
    private ClassesAdapter classesAdapter;

    private CourseBean newestCourseBean;
    @BindView(R.id.ll_no_network)
    LinearLayout ll_no_network;
    String url="";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }
    @Override
    protected MainPresenter setMPresenter() {
        mainPresenter = new MainPresenter();
        mainPresenter.attachView(this);
        return mainPresenter;
    }

    @Override
    protected void initView(View view) {
        List<CourseBean>list=new ArrayList<>();
        adapter = new MainAdapter(list);
        smartRefreshLayout.autoRefresh();
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(),2);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.addItemDecoration(new HotClassItemDecoration());
        rvList.setHasFixedSize(true);
        rvList.setAdapter(adapter);
        addHeader();
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mainPresenter.getHomeList();
                mainPresenter.getBanner();
                smartRefreshLayout.finishRefresh(1000);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CourseBean courseBean= (CourseBean) adapter.getData().get(position);
                Intent intent=new Intent(getActivity(),CourseDetailActivity.class);
                intent.putExtra("courseId",courseBean.getId());
                intent.putExtra("title",courseBean.getTitle());
                startActivity(intent);
            }
        });
        if (!SPUtils.getFBooleanData(Constant.AGREEMENT)){
            AgreementDialog agreementDialog=new AgreementDialog();
            agreementDialog.show(getFragmentManager(),"agreement");
        }
//        adapter.setOnLoadMoreListener(this,rvList);
    }

    private void addHeader(){
        View headerView = getLayoutInflater().inflate(R.layout.header,null);
        bgaBanner = headerView.findViewById(R.id.banner);
        img_new = headerView.findViewById(R.id.img_new);
        title_new = headerView.findViewById(R.id.title_new);
        num_new = headerView.findViewById(R.id.num_new);
        price_new = headerView.findViewById(R.id.price_new);
        price_new_2 = headerView.findViewById(R.id.price_new_2);
        rvClass = headerView.findViewById(R.id.rv_class);
        classesAdapter = new ClassesAdapter(new ArrayList<>());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvClass.setLayoutManager(linearLayoutManager);
        rvClass.addItemDecoration(new ClassItemDecoration());
        rvClass.setAdapter(classesAdapter);
        adapter.addHeaderView(headerView);
        headerView.findViewById(R.id.tv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ClassActivity.class));
            }
        });
        headerView.findViewById(R.id.tv_more2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ClassActivity.class));
            }
        });
        headerView.findViewById(R.id.card_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), CourseDetailActivity.class);
                if (newestCourseBean!=null){
                    intent.putExtra("courseId",newestCourseBean.getId());
                    intent.putExtra("title",newestCourseBean.getTitle());
                    startActivity(intent);
                }
            }
        });
        classesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CurriculumResponse curriculumResponse= (CurriculumResponse) adapter.getItem(position);
                Intent intent=new Intent(getActivity(),ClassActivity.class);
                intent.putExtra("subjectCode",curriculumResponse.getSubjectCode());
                startActivity(intent);
            }
        });

    }
    @OnClick({R.id.img_search,R.id.tv_refresh,R.id.service})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_search:
                startActivity(new Intent(getActivity(),SearchCourseActivity.class));
                break;
            case R.id.tv_refresh:
                smartRefreshLayout.autoRefresh();
                break;
            case R.id.service:
                Intent intent=new Intent(getActivity(),WebActivity.class);
                if (!TextUtils.isEmpty(SPUtils.getStringData(Constant.SERVICE_URL))){
                    intent.putExtra(Constant.WEB_INTENT,SPUtils.getStringData(Constant.SERVICE_URL));
                    intent.putExtra("title","客服");
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onGotBanner(List<BannerBean> beans) {
        rvList.setVisibility(View.VISIBLE);
        List<String> imageIds = new ArrayList<>();
        for (BannerBean bannerBean:beans){
            imageIds.add(bannerBean.getPicture());
        }
        bgaBanner.setData(R.layout.item_fresco_t,imageIds,null);
        bgaBanner.setAdapter(new BGABanner.Adapter<CardView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, CardView itemView, @Nullable String model, int position) {
                ImageView simpleDraweeView = itemView.findViewById(R.id.sdv_item_fresco_content);
                RequestOptions options=new RequestOptions();
                options.placeholder(R.drawable.banner_default).fallback(R.drawable.banner_default).error(R.drawable.banner_default);
                options.centerCrop().dontAnimate();
                Glide.with(HomeFragment.this)
                        .load(model)
                        .apply(options)
                        .into(simpleDraweeView);
            }
        });
        bgaBanner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
                String url=beans.get(position).getHrefLink();
                if (121==beans.get(position).getInquire()&&!TextUtils.isEmpty(url)){
                    Intent intent=new Intent(getActivity(),WebActivity.class);
                    intent.putExtra(Constant.WEB_INTENT,url);
                    intent.putExtra("title",beans.get(position).getTitle());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onGotHomeList(HomeBean homeBean) {
//        Glide.with(HomeFragment.this).load(homeBean.getCourse().getUrl()).into(img_new);
        rvList.setVisibility(View.VISIBLE);
        ll_no_network.setVisibility(View.GONE);
        newestCourseBean=homeBean.getCourse();
        if (newestCourseBean!=null){
            title_new.setText(homeBean.getCourse().getTitle()+"");
            num_new.setText(homeBean.getCourse().getReadNumber()+"人学过");
            price_new.setText("￥"+homeBean.getCourse().getMinPrice());
            price_new_2.setText("￥"+homeBean.getCourse().getMaxPrice());
            price_new_2.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
            RoundedCorners roundedCorners=new RoundedCorners(15);
            RequestOptions options=new RequestOptions();
            options.placeholder(R.drawable.banner_default).error(R.drawable.banner_default).fallback(R.drawable.banner_default);
            options.transform(new MultiTransformation<>(new CenterCrop(),roundedCorners));
            Glide.with(getActivity()).load(homeBean.getCourse().getUrl()).apply(options).into(img_new);
        }
        classesAdapter.setNewData(homeBean.getCurriculumResponses());
        adapter.setNewData(homeBean.getCourseCollects());
        url=homeBean.getCustomer();
        if (!TextUtils.isEmpty(url)){
            SPUtils.saveData(Constant.SERVICE_URL,url);
        }
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void onGetUpdateMsg(UpdateBean updateBean) {

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
        if (newestCourseBean==null){
            ll_no_network.setVisibility(View.VISIBLE);
        }else {
            ToastUtil.show("当前网络不可用，请稍候再试");
        }
    }


    @Override
    public void onLoadMoreRequested() {

    }
}
