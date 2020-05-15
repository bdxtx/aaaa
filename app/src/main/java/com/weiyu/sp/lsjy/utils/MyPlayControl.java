package com.weiyu.sp.lsjy.utils;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import chuangyuan.ycj.videolibrary.listener.DataSourceListener;
import chuangyuan.ycj.videolibrary.video.ExoUserPlayer;
import chuangyuan.ycj.videolibrary.widget.VideoPlayerView;

public class MyPlayControl extends ExoUserPlayer {
    private boolean locking=false;

    public boolean isLocking() {
        return locking;
    }

    public void setLocking(boolean locking) {
        this.locking = locking;
    }

    public MyPlayControl(@NonNull Context activity, @NonNull VideoPlayerView playerView, @Nullable DataSourceListener listener) {
        super(activity, playerView, listener);
    }

    @Override
    public void setStartOrPause(boolean value) {
        if (isLocking()){
            ToastUtil.show("试看结束");
        }else {
            super.setStartOrPause(value);
        }
    }
}
