package com.weiyu.sp.lsjy.mine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseApplication;
import com.weiyu.sp.lsjy.base.BaseMvpActivity;
import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.bean.PersonalBean;
import com.weiyu.sp.lsjy.login.LoginActivity;
import com.weiyu.sp.lsjy.net.RequestUtil;
import com.weiyu.sp.lsjy.net.RetrofitClient;
import com.weiyu.sp.lsjy.net.Url;
import com.weiyu.sp.lsjy.utils.EncryptionHelper;
import com.weiyu.sp.lsjy.utils.GlideEngine;
import com.weiyu.sp.lsjy.utils.RSAHelper;
import com.weiyu.sp.lsjy.utils.SPUtils;
import com.weiyu.sp.lsjy.utils.ToastUtil;
import com.weiyu.sp.lsjy.view.dialog.AgeDialog;
import com.weiyu.sp.lsjy.view.dialog.ExitDialog;
import com.weiyu.sp.lsjy.view.dialog.GrageDialog;
import com.weiyu.sp.lsjy.view.dialog.IdentityDialog;
import com.weiyu.sp.lsjy.view.dialog.LoadingDialog;
import com.weiyu.sp.lsjy.view.dialog.NickNameDialog;
import com.weiyu.sp.lsjy.view.dialog.PhoneDialog;
import com.weiyu.sp.lsjy.view.dialog.SchoolDialog;
import com.weiyu.sp.lsjy.view.dialog.SelectPictureDialog;
import com.weiyu.sp.lsjy.view.dialog.SexDialog;
import com.weiyu.sp.lsjy.view.dialog.TrueNameDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Contract;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PersonalCenterActivity extends BaseMvpActivity<PersonalCenterPresenter> implements PersonalCenterContract.View, SelectPictureDialog.OnclickListener, ExitDialog.Exit {
    @BindView(R.id.img_header)
    ImageView img_header;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.true_name)
    TextView true_name;
    @BindView(R.id.identity)
    TextView identity;
    @BindView(R.id.grade)
    TextView grade;
    @BindView(R.id.age)
    TextView age;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.school)
    TextView school;
    private LoadingDialog loadingDialog;
    private PersonalBean mPersonalBean;
    @BindView(R.id.ll_no_network)
    LinearLayout ll_no_network;

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_center;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mPresenter.selectUserDetail(new HashMap<>());
    }

    @Override
    protected PersonalCenterPresenter setMPresenter() {
        PersonalCenterPresenter personalCenterPresenter=new PersonalCenterPresenter();
        personalCenterPresenter.attachView(this);
        return personalCenterPresenter;
    }

    @Override
    public void userDetail(PersonalBean bean) {
        ll_no_network.setVisibility(View.GONE);
        mPersonalBean = bean;
        Glide.with(this).load(bean.getUrl()).apply(new RequestOptions().placeholder(R.drawable.header_default)
                .error(R.drawable.header_default)
                .fallback(R.drawable.header_default).circleCrop()).into(img_header);
        tv_name.setText(bean.getNickName());
        tv_sex.setText(bean.getSex());
        true_name.setText(bean.getUname());
        age.setText(bean.getAge());
        identity.setText(bean.getIdentity());
        school.setText(bean.getSchool());
        grade.setText(bean.getGrade());
        String phoneCode=bean.getPhone();
        if (phoneCode.length()==11){
            phone.setText(phoneCode.substring(0,3)+"****"+phoneCode.substring(7));
        }else {
            phone.setText("未设置");
        }

    }

    @Override
    public void updateHeader(String url) {
        if (!isFinishing()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                    Glide.with(PersonalCenterActivity.this).load(url)
                            .apply(new RequestOptions().centerCrop()
                                    .circleCrop().placeholder(R.drawable.header_default)
                                    .error(R.drawable.header_default)
                                    .fallback(R.drawable.header_default)).into(img_header);
                    EventBus.getDefault().post(new MessageEvent("",EventBusTag.MainRefresh));
                }
            });
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                    ToastUtil.show("上传失败");
                }
            });
        }else if (flag==1){
            ll_no_network.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (hasToken()){
            if(mPersonalBean==null){
                mPresenter.selectUserDetail(new HashMap<>());
            }
        }else {
            finish();
        }
    }

    @Override
    public void onGetListener() {
        PictureSelector.create(PersonalCenterActivity.this)
                .openCamera(PictureMimeType.ofImage())// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .selectionMode( PictureConfig.SINGLE)// 多选 or 单选
                .compress(true)// 是否压缩
                .compressQuality(60)// 图片压缩后输出质量
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .cutOutQuality(90)// 裁剪输出质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        for (LocalMedia media : result) {
                            showLoading();
                            mPresenter.uploadImage(media.getCompressPath());
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });

    }

    @Override
    public void onSelectListener() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(PersonalCenterActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)// 设置相册Activity方向，不设置默认使用系统
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .isSingleDirectReturn(true)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .cutOutQuality(90)// 裁剪输出质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        for (LocalMedia media : result) {
                            showLoading();
//                            String path=media.getCompressPath().endsWith(".jpg")?media.getCompressPath():media.getAndroidQToPath().endsWith(".jpg")?media.getAndroidQToPath():media.getRealPath();
                            mPresenter.uploadImage(media.getCompressPath());

                        }
                    }

                    @Override
                    public void onCancel() {
                    }
                });
    }
    @OnClick({R.id.img_header,R.id.name_more,R.id.tv_name,R.id.tv_sex,R.id.sex_more,R.id.true_name,R.id.true_name_more,R.id.identity_more,R.id.identity,
    R.id.phone_more,R.id.phone,R.id.school,R.id.school_more,R.id.grade,R.id.grade_more,R.id.age,R.id.age_more,R.id.back,R.id.logout,R.id.tv_refresh})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_header:
//                RxPermissions rxPermissions = new RxPermissions((FragmentActivity) PersonalCenterActivity.this);
//                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
//                        .subscribe(new Consumer<Boolean>() {
//                            @Override
//                            public void accept(Boolean aBoolean) throws Exception {
//                                if (aBoolean) {
//                                    SelectPictureDialog selectPictureDialog=new SelectPictureDialog();
//                                    selectPictureDialog.show(getSupportFragmentManager(),"照片选择");
//                                } else {
//                                    ToastUtil.show("需要授权读写外部存储权限!");
//                                }
//                            }
//                        });
                if (Build.VERSION.SDK_INT >= 23) {
                    int REQUEST_CODE_CONTACT = 101;
                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    //验证是否许可权限
                    for (String str : permissions) {
                        if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                            //申请权限
                            this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                            return;
                        }
                    }
                    SelectPictureDialog selectPictureDialog=new SelectPictureDialog();
                    selectPictureDialog.show(getSupportFragmentManager(),"照片选择");
                }
                break;
            case R.id.name_more:
            case R.id.tv_name:
                NickNameDialog nickNameDialog=new NickNameDialog();
                nickNameDialog.show(getSupportFragmentManager(),"昵称");
                break;
            case R.id.tv_sex:
            case R.id.sex_more:
                SexDialog sexDialog=new SexDialog();
                sexDialog.show(getSupportFragmentManager(),"性别");
                break;
            case R.id.true_name:
            case R.id.true_name_more:
                TrueNameDialog trueNameDialog=new TrueNameDialog();
                trueNameDialog.show(getSupportFragmentManager(),"姓名");
                break;
            case R.id.identity_more:
            case R.id.identity:
                IdentityDialog identityDialog=new IdentityDialog();
                identityDialog.show(getSupportFragmentManager(),"身份证");
                break;
            case R.id.phone:
            case R.id.phone_more:
                PhoneDialog phoneDialog=new PhoneDialog();
                phoneDialog.show(getSupportFragmentManager(),"手机");
                break;
            case R.id.school:
            case R.id.school_more:
                SchoolDialog schoolDialog=new SchoolDialog();
                schoolDialog.show(getSupportFragmentManager(),"学校");
                break;
            case R.id.grade:
            case R.id.grade_more:
                String gradeStr=grade.getText().toString();
                if (TextUtils.isEmpty(gradeStr)||gradeStr.equals("未设置")){
                    gradeStr="一年级";
                }
                GrageDialog grageDialog=GrageDialog.newInstance(gradeStr);
                grageDialog.show(getSupportFragmentManager(),"年级");
                break;
            case R.id.age:
            case R.id.age_more:
                AgeDialog ageDialog=new AgeDialog();
                ageDialog.show(getSupportFragmentManager(),"年龄");
                break;
            case R.id.back:
                finish();
                break;
            case R.id.logout:
                ExitDialog exitDialog=new ExitDialog();
                exitDialog.show(getSupportFragmentManager(),"exit");
                break;
            case R.id.tv_refresh:
                mPresenter.selectUserDetail(new HashMap<>());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (EventBusTag.PersonalUpdate.equals(messageEvent.getTag())){
            mPresenter.selectUserDetail(new HashMap<>());
        }
    }


    @Override
    public void exit() {
        mPresenter.loginOut(SPUtils.getStringData(Constant.REFRESH_LOGIN_TOKEN));
        SPUtils.saveData(Constant.LOGIN_TOKEN,"");
        SPUtils.saveData(Constant.REFRESH_LOGIN_TOKEN,"");
        LoginActivity.launchClearTask(getApplication().getApplicationContext());
        finish();
    }
}
