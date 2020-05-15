package com.weiyu.sp.lsjy.net;

import com.alibaba.fastjson.JSON;
import com.weiyu.sp.lsjy.base.BaseApplication;
import com.weiyu.sp.lsjy.utils.EncryptionHelper;
import com.weiyu.sp.lsjy.utils.RSAHelper;
import com.weiyu.sp.lsjy.utils.SystemUtil;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RequestUtil {
    //图片上传
    public static Request uploadImg(String filePath) throws Exception {
        String uuid= SystemUtil.getUUID(BaseApplication.getInstance());
        String time=System.currentTimeMillis()/1000+"";
        Map<String,String> hashMap=new HashMap<>();
        File file  = new File(filePath);
        String name=file.getName();
        String public_key= EncryptionHelper.getPublicKey();
        String paramstr = RSAHelper.encipher(JSON.toJSONString(hashMap),public_key,2048 / 8 - 11);
        RequestBody requestFile =               // 根据文件格式封装文件
                RequestBody.create(MediaType.parse("image/jpg"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)              // multipart/form-data
                .addFormDataPart("file", file.getName(), requestFile)
                .addFormDataPart("params", paramstr)
                .build();
        Request request = new Request.Builder()
                .url(RetrofitClient.getInstance().getBaseUrl()+Url.upload_img)
                .post(requestBody)
                .header("headerName", RetrofitClient.getInstance().getHeaderName(Url.upload_img_url,time,uuid))
                .header("loginToken", BaseApplication.getInstance().getToken())
                .header("encryptType","android")
                .header("signature",RetrofitClient.getInstance().getSignature(Url.upload_img_url,time,hashMap,uuid))
                .build();
        return request;
    }
}
