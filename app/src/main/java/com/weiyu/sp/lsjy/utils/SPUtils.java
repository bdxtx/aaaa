package com.weiyu.sp.lsjy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.weiyu.sp.lsjy.base.BaseApplication;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.bean.MeBean;

public class SPUtils {
    private static SharedPreferences getSharedPreferences(){
        SharedPreferences sharedPreferences= BaseApplication.getInstance().getSharedPreferences(Constant.NATIVE_DATA, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static void saveData(String tag,String data){
        getSharedPreferences().edit().putString(tag,data).commit();
    }
    public static void saveData(String tag,boolean data){
        getSharedPreferences().edit().putBoolean(tag,data).commit();
    }
    public static void saveData(String tag,float data){
        getSharedPreferences().edit().putFloat(tag,data).commit();
    }
    public static void saveData(String tag,long data){
        getSharedPreferences().edit().putLong(tag,data).commit();
    }
    public static String getStringData(String key){
        return getSharedPreferences().getString(key,"");
    }
    public static boolean getBooleanData(String key){
        return getSharedPreferences().getBoolean(key,true);
    }
    public static boolean getFBooleanData(String key){
        return getSharedPreferences().getBoolean(key,false);
    }
    public static float getFloatData(String key){
        return getSharedPreferences().getFloat(key,0);
    }
    public static long getLongData(String key){
        return getSharedPreferences().getLong(key,0);
    }

    public static void savePersonal(MeBean meBean){
        saveData(Constant.header,meBean.getUrl());
        saveData(Constant.loginName,meBean.getLoginName());
    }
}
