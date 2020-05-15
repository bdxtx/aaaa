package com.weiyu.sp.lsjy.other;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseApplication;
import com.weiyu.sp.lsjy.base.BaseMvpActivity;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.bean.ShareBean;
import com.weiyu.sp.lsjy.login.LoginActivity;
import com.weiyu.sp.lsjy.net.RetrofitClient;
import com.weiyu.sp.lsjy.utils.CutOffUtil;
import com.weiyu.sp.lsjy.utils.ToastUtil;
import com.weiyu.sp.lsjy.view.dialog.ShareDialog;
import com.weiyu.sp.lsjy.view.dialog.ShareSuccessDialog;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class ShareActivity extends BaseMvpActivity<SharePresenter>implements ShareContract.View, ShareDialog.SaveImg {
    @BindView(R.id.code)
    TextView code;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.copy)
    TextView copy;
    @BindView(R.id.ll_share)
    LinearLayout ll_share;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.img_header)
    ImageView img_header;

    ShareBean shareBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_shape;
    }

    @Override
    public void initView() {
        if (TextUtils.isEmpty(BaseApplication.getInstance().getToken())){
            LoginActivity.launchTask(BaseApplication.getInstance(),"");
            return;
        }
        mPresenter.appShare();
    }

    @Override
    protected SharePresenter setMPresenter() {
        SharePresenter sharePresenter =new SharePresenter();
        sharePresenter.attachView(this);
        return sharePresenter;
    }

    @Override
    public void onSuccess(ShareBean shareBean) {
        this.shareBean=shareBean;
        name.setText("您的好友“"+shareBean.getNickName()+"” 邀请您加入螺蛳学院");
        code.setText(shareBean.getInviteCode());
        final Bitmap[] bitmap = new Bitmap[1];
        String url;
        if (Constant.baseUrl.equals(RetrofitClient.getInstance().getBaseUrl())){
            url="https://vue.luosijiaoyu.com/#/invitationCode?inviteCode=";
        }else {
            url="https://opt.luosijiaoyu.com/lsjy-h5-vue/#/invitationCode=";
        }
        if (TextUtils.isEmpty(shareBean.getUrl())){
            bitmap[0] = BitmapFactory.decodeResource(getResources(),
                    R.drawable.share_code);
            float density = getResources().getDisplayMetrics().density;
            Bitmap mBitmap = CodeUtils.createImage(url+shareBean.getInviteCode(), (int)(100*density), (int)(100*density), bitmap[0]);
            img.setImageBitmap(mBitmap);
        }else {
            Target<Bitmap> target=Glide.with(this).asBitmap().load(shareBean.getUrl()).into(new SimpleTarget<Bitmap>(100,100) {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    bitmap[0] =resource;
                    float density = getResources().getDisplayMetrics().density;
                    Bitmap mBitmap = CodeUtils.createImage(url+shareBean.getInviteCode(), (int)(100*density), (int)(100*density), bitmap[0]);
                    img.setImageBitmap(mBitmap);
                }
            });

        }
//        Glide.with(this).load(shareBean.getUrl()).apply(new RequestOptions().placeholder(R.drawable.header_default)
//                .error(R.drawable.header_default)
//                .fallback(R.drawable.header_default).circleCrop()).into(img_header);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (hasToken()){
            if(shareBean==null){
                mPresenter.appShare();
            }
        }else {
            finish();
        }
    }

    @Override
    public void onError(Throwable throwable, int flag) {

    }
    @OnClick({R.id.to_share,R.id.back,R.id.copy})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.to_share:
                RxPermissions rxPermissions = new RxPermissions((FragmentActivity)ShareActivity.this);
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    ShareDialog shareDialog=new ShareDialog();
                                    shareDialog.show(getSupportFragmentManager(),"share");
                                    copy.setVisibility(View.GONE);
                                } else {
                                    ToastUtil.show("需要授权读写外部存储权限!");
                                }
                            }
                        });
                break;
            case R.id.back:
                finish();
                break;
            case R.id.copy:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("label", code.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                ToastUtil.show("复制成功");
                break;
        }
    }

    @Override
    public void save() {
        CutOffUtil.savePicture(ll_share);
//        ToastUtil.show("图片保存成功");
        ShareSuccessDialog shareSuccessDialog=new ShareSuccessDialog();
        shareSuccessDialog.show(getSupportFragmentManager(),"success");
//        copy.setVisibility(View.VISIBLE);
    }

    @Override
    public void dialogDestroy() {
//        copy.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
