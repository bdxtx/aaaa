package com.weiyu.sp.lsjy.view.dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.bean.UpdateBean;
import com.weiyu.sp.lsjy.utils.ToastUtil;
import com.weiyu.sp.lsjy.utils.download.BGAUpgradeUtil;

import java.io.File;

import io.reactivex.functions.Consumer;
import rx.Subscriber;

public class UpdateDialog extends DialogFragment {
    Dialog dialog;
    private TextView progressTv;
    private TextView tv_update;
    private String ref;
    private String edition;

    public static UpdateDialog newInstance(UpdateBean updateBean){
        UpdateDialog updateDialog=new UpdateDialog();
        Bundle bundle=new Bundle();
        bundle.putString("content",updateBean.getUpdateContent());
        bundle.putString("ref",updateBean.getHref());
        bundle.putInt("modify",updateBean.getModify());
        bundle.putString("edition",updateBean.getEdition());
        updateDialog.setArguments(bundle);
        return updateDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.dialog_update);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消

        ImageView imageView=dialog.findViewById(R.id.close);
        progressTv = dialog.findViewById(R.id.progress_tv);
        Bundle bundle=getArguments();
        String content=bundle.getString("content");
        edition = bundle.getString("edition");
        TextView tv_version = dialog.findViewById(R.id.tv_version);
        tv_version.setText("最新版本:V"+edition);
        ref = bundle.getString("ref");
        int modify=bundle.getInt("modify");
        if (930==modify){
            imageView.setVisibility(View.VISIBLE);
        }else {
            imageView.setVisibility(View.GONE);
        }
        TextView textView=dialog.findViewById(R.id.content);
        textView.setText(content+"");
        tv_update = dialog.findViewById(R.id.tv_update);
        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(ref)) {
                    // 如果新版 apk 文件已经下载过了，直接 return，此时不需要开发者调用安装 apk 文件的方法，在 isApkFileDownloaded 里已经调用了安装」
                    if (BGAUpgradeUtil.isApkFileDownloaded( edition + "")) {
                        return;
                    }

                    RxPermissions rxPermissions = new RxPermissions((FragmentActivity) getContext());
                    rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) throws Exception {
                                    if (aBoolean) {
                                        downloadApkFile();
                                    } else {
                                        ToastUtil.show("需要授权读写外部存储权限!");
                                    }
                                }
                            });
                }
            }
        });
        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    return true;
                }else if(keyCode == KeyEvent.KEYCODE_MENU) {

                    return true;
                }
                return false;
            }
        });

        return dialog;
    }

    private void downloadApkFile() {
        String mApkUrl = ref+"";
        String mNewVersion = edition+ "";
        // 下载新版 apk 文件
        BGAUpgradeUtil.downloadApkFile(mApkUrl, mNewVersion)
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onStart() {
                        progressTv.setText("正在下载中...");
                        tv_update.setEnabled(false);
                    }

                    @Override
                    public void onCompleted() {
                        progressTv.setText("已下载完成请立即安装");
                    }

                    @Override
                    public void onError(Throwable e) {
                        BGAUpgradeUtil.deleteOldApk();
                        progressTv.setText("下载失败");
                        Log.e("csc","e______________="+e);
                    }

                    @Override
                    public void onNext(File apkFile) {
                        if (apkFile != null) {
                            BGAUpgradeUtil.installApk(apkFile);
                        }
                        tv_update.setEnabled(true);
                    }
                });
    }

}
