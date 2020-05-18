package com.weiyu.sp.lsjy.net;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

//import com.baronzhang.retrofit2.converter.FastJsonConverterFactory;
import com.alibaba.fastjson.JSON;
import com.baronzhang.retrofit2.converter.FastJsonConverterFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weiyu.sp.lsjy.base.BaseApplication;
import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.bean.LoginBean;
import com.weiyu.sp.lsjy.login.LoginActivity;
import com.weiyu.sp.lsjy.utils.EncryptionHelper;
import com.weiyu.sp.lsjy.utils.RSAHelper;
import com.weiyu.sp.lsjy.utils.RsaUtils;
import com.weiyu.sp.lsjy.utils.SPUtils;
import com.weiyu.sp.lsjy.utils.SystemUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static volatile RetrofitClient instance;
    private APIService apiService;

//    https://www.jianshu.com/p/66b59ad1fdc1
    private String baseUrl="http://192.168.0.111:7001/";
//    private String baseUrl="http://47.111.142.189:7001/";
//    private String baseUrl="http://192.168.0.114:7001/";
//    private String baseUrl="https://api8.luosijiaoyu.com/";
//    private String baseUrl="https://api.luosijiaoyu.com/";//正式环境
//    private String baseUrl="http:/192.168.101.31:7001/";
//    private String baseUrl="http:/192.168.0.102:7001/";
    private RSAPublicKey rsaPublicKey;


    public String getBaseUrl() {
        if (!baseUrl.equals(Constant.baseUrl)){
            String updateBaseUrl=SPUtils.getStringData(Constant.UpdateBaseUrl);
            if (!TextUtils.isEmpty(updateBaseUrl)){
                baseUrl=updateBaseUrl;
            }
        }
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }

    /**
     * 设置Header
     *
     * @return
     */
    private Interceptor getHeaderInterceptor(String tag) {
        return new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();
                String url=original.url().encodedPath();
                String time=System.currentTimeMillis()/1000+"";
                if (url.startsWith(Url.model)){
                    url=url.substring(4);
                }

                Log.i("csc","Url="+url);
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                //body中的参数
                Map<String,String>hashMap=new HashMap<>();
                if (original.body() instanceof FormBody) {
                    FormBody formBody = (FormBody) original.body();
                    for (int i = 0; i < formBody.size(); i++) {
                        hashMap.put(formBody.encodedName(i), URLDecoder.decode(formBody.encodedValue(i), "UTF-8"));
                    }
                }
                String uuid= SystemUtil.getUUID(BaseApplication.getInstance());
                //请求头中的headName
                String headName =getHeaderName(url,time,uuid);
//                Map<String,String> headerMaps = new HashMap<String,String>();
//                headerMaps.put("appId","e10adc3949ba59abbe56e057f20f883e");
//                headerMaps.put("method",url);
//                headerMaps.put("version","1.0.0");
//                headerMaps.put("timestamp",time);
//                headerMaps.put("encryptType","android");
//
//                rsaPublicKey=BaseApplication.getInstance().getPublicKey();
//                String headName = RsaUtils.encryptDataStr(JSON.toJSONString(headerMaps).getBytes(), rsaPublicKey,true);


                //请求头中的signature
                String signature=getSignature(url,time,hashMap,uuid);
//                StringBuffer buffer = new StringBuffer();
//                buffer.append("appId").append("=").append("e10adc3949ba59abbe56e057f20f883e").append("&");
//                buffer.append("method").append("=").append(url).append("&");
//                buffer.append("version").append("=").append("1.0.0").append("&");
//                buffer.append("timestamp").append("=").append(time).append("&");
//                buffer.append("loginToken").append("=").append(BaseApplication.getInstance().getToken()).append("&");
//                buffer.append("encryptType").append("=").append("android").append("&");
//                Map<String, String> resultMap = sortMapByKey(hashMap);
//                if(null != resultMap && !resultMap.isEmpty())
//                {
//                    for (Map.Entry<String, String> entry : resultMap.entrySet())
//                    {
//                        buffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
//                    }
//                }
//                String body = buffer.toString().substring(0,buffer.toString().length() - 1);
//                String signature="";
//                try {
//                    String private_key=BaseApplication.getInstance().getPrivateKey();
//                    signature = EncryptionHelper.RSA_GetSign(private_key,JSON.toJSONString(body),"RSA","utf-8","SHA1withRSA");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }


                Request.Builder requestBuilder = original.newBuilder()
                        .header("headerName",headName)
                        .header("loginToken",BaseApplication.getInstance().getToken())
                        .header("encryptType","android")
                        .header("signature",signature);
//                if (original.method()=="POST"){
//
//                }
                //把body中的值进行RSA加密
                try {
//                    RSAPublicKey rsaPublicKey = EncryptionHelper.loadPublicKey(public_key,"RSA");
//                    String paramstr = RsaUtils.encryptDataStr(JSON.toJSONString(hashMap).getBytes(),rsaPublicKey);
                    String public_key=EncryptionHelper.getPublicKey();
                    String paramstr = RSAHelper.encipher(JSON.toJSONString(hashMap),public_key,2048 / 8 - 11);
//                    Log.i("csc",JSON.toJSONString(headerMaps));
                    bodyBuilder.addEncoded("params",URLEncoder.encode(paramstr, "UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FormBody newBody = bodyBuilder.build();
                requestBuilder.post(newBody);
                Request request = requestBuilder.build();
                Response response = chain.proceed(request);
                MediaType mediaType = response.body().contentType();
                String content= response.body().string();
                if (isTokenExpired(content,tag)){
                    //同步请求方式，获取最新的Token
                    String newSession = getNewToken();
                    //使用新的Token，创建新的请求
                    if (null != newSession && newSession.length() > 0) {
                        String time2=System.currentTimeMillis()/1000+"";
                        String uuid2= SystemUtil.getUUID(BaseApplication.getInstance());
                        Request newRequest = chain.request()
                                .newBuilder()
                                .header("headerName",getHeaderName(url,time2,uuid2))
                                .header("loginToken",BaseApplication.getInstance().getToken())
                                .header("encryptType","android")
                                .header("signature",getSignature(url,time2,hashMap,uuid2)).post(newBody).build();
                        //重新请求上次的接口
                        Log.i("csc","已经刷新token");
                        return chain.proceed(newRequest.newBuilder().build());
                    }else {

                    }

                }
                return response.newBuilder()
                        .body(ResponseBody.create(mediaType, content))
                        .build();
            }
        };

    }

    public String getHeaderName(String url,String time,String uuid){
        Map<String,String> headerMaps = new HashMap<String,String>();
        headerMaps.put("appId","e10adc3949ba59abbe56e057f20f883e");
        headerMaps.put("method",url);
        headerMaps.put("version","1.0.0");
        headerMaps.put("timestamp",time);
        headerMaps.put("encryptType","android");
        headerMaps.put("randomuuid",uuid);

        rsaPublicKey=BaseApplication.getInstance().getPublicKey();
        return RsaUtils.encryptDataStr(JSON.toJSONString(headerMaps).getBytes(), rsaPublicKey,true);
    }

    public String getSignature(String url,String time,Map<String,String>hashMap,String uuid){
        StringBuffer buffer = new StringBuffer();
        buffer.append("appId").append("=").append("e10adc3949ba59abbe56e057f20f883e").append("&");
        buffer.append("method").append("=").append(url).append("&");
        buffer.append("version").append("=").append("1.0.0").append("&");
        buffer.append("timestamp").append("=").append(time).append("&");
        buffer.append("loginToken").append("=").append(BaseApplication.getInstance().getToken()).append("&");
        buffer.append("encryptType").append("=").append("android").append("&");
        buffer.append("randomuuid").append("=").append(uuid).append("&");
        Map<String, String> resultMap = sortMapByKey(hashMap);
        if(null != resultMap && !resultMap.isEmpty())
        {
            for (Map.Entry<String, String> entry : resultMap.entrySet())
            {
                buffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        String body = buffer.toString().substring(0,buffer.toString().length() - 1);
        String signature="";
        try {
            String private_key=BaseApplication.getInstance().getPrivateKey();
            signature = EncryptionHelper.RSA_GetSign(private_key,JSON.toJSONString(body),"RSA","utf-8","SHA1withRSA");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signature;
    }

    public boolean isTokenExpired(String  result,String tag) {
        String resultStr = result;
        BaseObjectBean requestCode = new Gson().fromJson(resultStr, BaseObjectBean.class);
        if (requestCode.getCode() == 700) {
            Log.e("OkHttpManager", "----requestCode,Token登录过期了");
            return true;
        }else if (requestCode.getCode()==600){
            if (!Constant.RefreshToken.equals(tag)){
                LoginActivity.launchTask(BaseApplication.getInstance(),tag);
            }
            return true;
        }

        return false;
    }

    public String getNewToken() {

//        /**
//         * 必须使用同步请求
//         */
//
        String time=System.currentTimeMillis()/1000+"";
        String url = getBaseUrl()+Url.token_refresh;
        Map<String,String>hashMap=new HashMap<>();
        hashMap.put("refreshToken",SPUtils.getStringData(Constant.REFRESH_LOGIN_TOKEN));
        Log.e("OkHttpManager", "重新请求---" + url);
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        try {
            //String paramstr = RsaUtils.encryptDataStr(JSON.toJSONString(hashMap).getBytes(),rsaPublicKey,true);
            String public_key=EncryptionHelper.getPublicKey();
            String paramstr = RSAHelper.encipher(JSON.toJSONString(hashMap),public_key,2048 / 8 - 11);
            bodyBuilder.addEncoded("params", URLEncoder.encode(paramstr, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        FormBody newBody = bodyBuilder.build();
        String uuid3= SystemUtil.getUUID(BaseApplication.getInstance());
        Request request = new Request.Builder().header("headerName",getHeaderName("/LoginUserController/refreshLogin.html",time,uuid3))
                .header("loginToken",BaseApplication.getInstance().getToken())
                .header("encryptType","android")
                .header("signature",getSignature("/LoginUserController/refreshLogin.html",time,hashMap,uuid3)).url(url).post(newBody).build();
        okhttp3.Call call = client.newCall(request);
        String token="";
        try {
            Response response = call.execute();
            String result=response.body().string();
            BaseObjectBean data = new Gson().fromJson(result, BaseObjectBean.class);
            Log.e("OkHttpManager", "重新请求---"+data.getCode());
            if (data.getCode() == 200) {
                BaseObjectBean<LoginBean> baseObjectBean=new Gson().fromJson(result,new TypeToken<BaseObjectBean<LoginBean>>(){}.getType());
                LoginBean bean= baseObjectBean.getRows();
                token=bean.getLoginToken();
                BaseApplication.getInstance().saveToken(bean.getLoginToken());
                SPUtils.saveData(Constant.REFRESH_LOGIN_TOKEN,bean.getRefreshLoginToken());
                long secondTime = System.currentTimeMillis();
                SPUtils.saveData(Constant.RefreshTokenTime,secondTime);
            }else if (data.getCode() == 600){
                LoginActivity.launchTask(BaseApplication.getInstance(),"");
            }else if (data.getCode() == 700) {
                getNewToken();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }


    public Map<String, String> sortMapByKey(Map<String, String> map)
    {
        // TODO Auto-generated method stub
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }


    class MapKeyComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {

            return str1.compareTo(str2);
        }
    }

    /**
     * 设置拦截器
     *
     * @return
     */
    private Interceptor getInterceptor() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //显示日志
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    public APIService getApi(String tag) {
        //初始化一个client,不然retrofit会自己默认添加一个
        OkHttpClient client = new OkHttpClient().newBuilder()
                //设置Header
                .addInterceptor(getHeaderInterceptor(tag))
                //设置拦截器
                .addInterceptor(getInterceptor())
                .addInterceptor(new StateInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                //设置网络请求的Url地址
                .baseUrl(getBaseUrl())
                //设置数据解析器
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create(   ))
                //设置网络请求适配器，使其支持RxJava与RxAndroid
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        //创建—— 网络请求接口—— 实例
        apiService = retrofit.create(APIService.class);
        return apiService;
    }
    public APIService getApi() {
        //初始化一个client,不然retrofit会自己默认添加一个
        OkHttpClient client = new OkHttpClient().newBuilder()
                //设置Header
                .addInterceptor(getHeaderInterceptor(""))
                //设置拦截器
                .addInterceptor(getInterceptor())
//                .addInterceptor(new StateInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                //设置网络请求的Url地址
                .baseUrl(getBaseUrl())
                //设置数据解析器
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create(   ))
                //设置网络请求适配器，使其支持RxJava与RxAndroid
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        //创建—— 网络请求接口—— 实例
        apiService = retrofit.create(APIService.class);
        return apiService;
    }
}
