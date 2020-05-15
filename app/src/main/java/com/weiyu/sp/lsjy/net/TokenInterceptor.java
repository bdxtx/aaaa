package com.weiyu.sp.lsjy.net;

import android.os.Handler;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    private Handler mHandler = new Handler();
    private String resultStr;
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        return null;
    }
}
