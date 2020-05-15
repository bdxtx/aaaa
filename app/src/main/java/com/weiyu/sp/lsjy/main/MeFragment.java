package com.weiyu.sp.lsjy.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseApplication;
import com.weiyu.sp.lsjy.base.BaseFragment;
import com.weiyu.sp.lsjy.base.BaseMvpFragment;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.MeBean;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.course.ClassActivity;
import com.weiyu.sp.lsjy.login.LoginActivity;
import com.weiyu.sp.lsjy.mine.IntegralActivity;
import com.weiyu.sp.lsjy.mine.MyAccountActivity;
import com.weiyu.sp.lsjy.mine.OpinionActivity;
import com.weiyu.sp.lsjy.mine.OrderListActivity;
import com.weiyu.sp.lsjy.mine.PersonalCenterActivity;
import com.weiyu.sp.lsjy.mine.RecruitActivity;
import com.weiyu.sp.lsjy.mine.VipActivity;
import com.weiyu.sp.lsjy.net.RetrofitClient;
import com.weiyu.sp.lsjy.other.ShareActivity;
import com.weiyu.sp.lsjy.utils.GlideRoundImage;
import com.weiyu.sp.lsjy.utils.SPUtils;
import com.weiyu.sp.lsjy.utils.ToastUtil;
import com.weiyu.sp.lsjy.view.dialog.InviteCodeDialog;
import com.weiyu.sp.lsjy.view.dialog.UpdateBaseUrlDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends BaseMvpFragment<MePresenter> implements MeContract.View {
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_days)
    TextView tv_days;
    @BindView(R.id.account)
    TextView account;
    @BindView(R.id.integral)
    TextView integral;
    @BindView(R.id.coupon)
    TextView coupon;
    @BindView(R.id.tv_invite_code)
    TextView tv_invite_code;
    @BindView(R.id.img_header)
    ImageView img_header;
    @BindView(R.id.img_ad)
    ImageView img_ad;
    @BindView(R.id.vip_tag)
    ImageView vip_tag;
    private MeBean currentBean;
    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.tv_version)
    TextView tv_version;
    @BindView(R.id.baseurl)
    FrameLayout baseurl;

    public MeFragment() {
        // Required empty public constructor

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);
        mPresenter.meDetail();
        smart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.meDetail();
                smart.finishRefresh(2000);
            }
        });
        tv_version.setText("V"+BaseApplication.getAppVersionName(getContext())+"(最新版本)");
        String name=SPUtils.getStringData(Constant.loginName);
        if (!TextUtils.isEmpty(name)){
            tv_name.setText(name);
        }
        String picture=SPUtils.getStringData(Constant.header);
        if (!TextUtils.isEmpty(picture)){
            Glide.with(this).load(picture).apply(new RequestOptions().placeholder(R.drawable.header_default)
                    .error(R.drawable.header_default)
                    .fallback(R.drawable.header_default).circleCrop()).into(img_header);
        }

        if (!Constant.baseUrl.equals(RetrofitClient.getInstance().getBaseUrl())){
            baseurl.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected MePresenter setMPresenter() {
        MePresenter mePresenter=new MePresenter();
        mePresenter.attachView(this);
        return mePresenter;
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
    public void onGetMeSuccess(MeBean meBean) {
        smart.finishRefresh();
        currentBean = meBean;
        SPUtils.savePersonal(meBean);
        if (!TextUtils.isEmpty(meBean.getId())){
            String phoneCode=meBean.getLoginName();
            if (Integer.parseInt(meBean.getDays())>0){
                Glide.with(this).load(R.drawable.vip_in).into(vip_tag);
            }else {
                Glide.with(this).load(R.drawable.me_vip).into(vip_tag);
            }
            tv_name.setText(phoneCode.substring(0,3)+"****"+phoneCode.substring(7));
            tv_days.setText("剩余"+meBean.getDays()+"天");
            account.setText(meBean.getAccount());
            integral.setText(meBean.getIntegral());
            coupon.setText(meBean.getCoupon());
            Glide.with(this).load(meBean.getUrl()).apply(new RequestOptions().placeholder(R.drawable.header_default)
                    .error(R.drawable.header_default)
                    .fallback(R.drawable.header_default).circleCrop()).into(img_header);
            RequestOptions options=new RequestOptions();
            GlideRoundImage glideRoundImage=new GlideRoundImage(5);
            options.centerCrop().transform(glideRoundImage);
            Glide.with(this).load(meBean.getAdUrl()).apply(options).into(img_ad);
            if (meBean.isHasTop()){
                tv_invite_code.setVisibility(View.VISIBLE);
            }else {
                tv_invite_code.setVisibility(View.GONE);
            }
        }else {
            Glide.with(this).load(R.drawable.me_vip).into(vip_tag);
        }
    }

    @Override
    public void onRegister() {
        ToastUtil.show("邀请码绑定成功");
        tv_invite_code.setVisibility(View.GONE);
    }

    @OnClick({R.id.tv_name,R.id.order,R.id.personal_center,R.id.addr,R.id.shou_cang,R.id.yi_jian,R.id.about,R.id.version,R.id.img_center,R.id.ll_account,R.id.ll_integral,R.id.tv_invite_code,R.id.tv_share,R.id.baseurl})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_name:
                if (tv_name.getText().toString().startsWith("点击登录")){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.order:
                if (TextUtils.isEmpty(BaseApplication.getInstance().getToken())){
                    LoginActivity.launchTask(BaseApplication.getInstance(),"");
                    return;
                }
                startActivity(new Intent(getActivity(), OrderListActivity.class));
                break;
            case R.id.personal_center:
                if (TextUtils.isEmpty(BaseApplication.getInstance().getToken())){
                    LoginActivity.launchTask(BaseApplication.getInstance(),"");
                    return;
                }
                startActivity(new Intent(getActivity(), VipActivity.class));
                break;
            case R.id.addr:
                break;
            case R.id.shou_cang:
                if (TextUtils.isEmpty(BaseApplication.getInstance().getToken())){
                    LoginActivity.launchTask(BaseApplication.getInstance(),"");
                    return;
                }
                Intent intent=new Intent(getActivity(), ClassActivity.class);
                intent.putExtra("flag", Constant.SHOU_CANG);
                startActivity(intent);
                break;
            case R.id.yi_jian:
                startActivity(new Intent(getActivity(), OpinionActivity.class));
                break;
            case R.id.about:
                startActivity(new Intent(getActivity(), RecruitActivity.class));
                break;
            case R.id.version:
                break;
            case R.id.img_center:
                if (TextUtils.isEmpty(BaseApplication.getInstance().getToken())){
                    LoginActivity.launchTask(BaseApplication.getInstance(),"");
                    return;
                }
                startActivity(new Intent(getActivity(), PersonalCenterActivity.class));
                break;
            case R.id.ll_account:
                if (TextUtils.isEmpty(BaseApplication.getInstance().getToken())){
                    LoginActivity.launchTask(BaseApplication.getInstance(),"");
                    return;
                }
                startActivity(new Intent(getActivity(), MyAccountActivity.class));
                break;
            case R.id.ll_integral:
                if (TextUtils.isEmpty(BaseApplication.getInstance().getToken())){
                    LoginActivity.launchTask(BaseApplication.getInstance(),"");
                    return;
                }
                startActivity(new Intent(getActivity(), IntegralActivity.class));
                break;
            case R.id.tv_invite_code:
                InviteCodeDialog inviteCodeDialog=new InviteCodeDialog();
                inviteCodeDialog.show(getFragmentManager(),"invite");
                break;
            case R.id.tv_share:
                startActivity(new Intent(getActivity(), ShareActivity.class));
                break;
            case R.id.baseurl:
                UpdateBaseUrlDialog updateBaseUrlDialog=new UpdateBaseUrlDialog();
                updateBaseUrlDialog.show(getFragmentManager(),"updateUrl");
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (EventBusTag.Invite.equals(messageEvent.getTag())){
            tv_invite_code.setVisibility(View.GONE);
        }else if (EventBusTag.MainRefresh.equals(messageEvent.getTag())){
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentBean!=null&&TextUtils.isEmpty(currentBean.getId())){
            refresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    private void refresh(){
        mPresenter.meDetail();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (TextUtils.isEmpty(currentBean.getId())){
//            refresh();
//        }
    }
}
