package com.weiyu.sp.lsjy.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.weiyu.sp.lsjy.R;

public class LoadingDialog extends DialogFragment {
    private Dialog dialog;
    private static LoadingDialog loadingDialog;
    private static final String SAVED_DIALOG_STATE_TAG = "android:savedDialogState";
    int loadTimes=0;

    public static LoadingDialog newInstance(){
        if (loadingDialog==null){
            loadingDialog = new LoadingDialog();
        }
        return loadingDialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (getShowsDialog()) {
            setShowsDialog(false);
        }
        super.onActivityCreated(savedInstanceState);
        setShowsDialog(true);

        View view = getView();
        if (view != null) {
            if (view.getParent() != null) {
                throw new IllegalStateException(
                        "DialogFragment can not be attached to a container view");
            }
            getDialog().setContentView(view);
        }
        final Activity activity = getActivity();
        if (activity != null) {
            getDialog().setOwnerActivity(activity);
        }
        if (savedInstanceState != null) {
            Bundle dialogState = savedInstanceState.getBundle(SAVED_DIALOG_STATE_TAG);
            if (dialogState != null) {
                getDialog().onRestoreInstanceState(dialogState);
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                loadingDialog.dismiss();
//            }
//        },2000);
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        loadTimes++;
        if (loadTimes==1&&!isAdded()&&!isVisible()&&!isRemoving()){
            try {
                super.show(manager, "loading");
            }catch (IllegalStateException ignore){

            }
        }

    }

    @Override
    public void dismiss() {
        if (loadingDialog!=null&&loadingDialog.getDialog()!=null&&loadingDialog.getDialog().isShowing()){
            loadTimes--;
            if (loadTimes==0){
                try {
                    super.dismiss();
                }catch (IllegalStateException ignore){

                }
            }

        }
    }

    @Override
    public void onDestroyView() {
        if (getView() instanceof ViewGroup) {
            ((ViewGroup)getView()).removeAllViews();
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        loadingDialog=null;
        super.onDestroy();

    }
}
