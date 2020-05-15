/**
* <p>Title: EncryptionHelper.java</p>
* <p>Description: </p>
* <p>Copyright: Copyright (c) 2017</p>
* <p>Company: http://www.zcai.pro/</p>
* @author donghui
* @version 1.0
*/
package com.weiyu.sp.lsjy.utils;
import android.text.TextUtils;

import com.weiyu.sp.lsjy.base.BaseApplication;
import com.weiyu.sp.lsjy.base.Constant;
import com.weiyu.sp.lsjy.net.RetrofitClient;


import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import javax.crypto.Cipher;

/**
* <p>Title: EncryptionHelper</p>
* <p>Description: RSA加密工具</p>
* @author zhangpp
*/
public class EncryptionHelper {

	/**
	 * <p>Title: loadPublicKey</p>
	 * <p>Description: 从字符串中加载公钥 </p>
	 * @param publicKey 公钥数据字符串
	 * @param encryptType 加密类型
	 * @return 公钥
	 * @throws Exception 加载公钥时产生的异常
	 */
    public static RSAPublicKey loadPublicKey(String publicKey,String encryptType) throws Exception{
        try {
            //byte[] buffer = Base64.decodeBase64(publicKey);
            byte[] buffer = Base64Utils.decode(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(encryptType);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        }  catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * <p>Title: loadPrivateKey</p>  
     * <p>Description:  从字符串中加载私钥</p>  
     * @param privateKey 私钥数据字符串
     * @param encryptType 加密类型
     * @return 私钥
     * @throws Exception 异常
     */
    public static RSAPrivateKey loadPrivateKey(String privateKey,String encryptType) throws Exception
    {
    	 try {
             byte[] buffer = Base64.decodeBase64(privateKey);
             PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
             KeyFactory keyFactory = KeyFactory.getInstance(encryptType);
             return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
         } catch (Exception ex) {
             throw ex;
         }
    }

    /**
     * <p>Title: RSA_GetEncrypt</p>  
     * <p>Description:  公钥加密 </p>  
     * @param publicKey 公钥 
     * @param encryptType 加密类型
     * @param body 明文数据 
     * @param encoding 编码
     * @return 加密后字符串
     * @throws Exception 加密时异常
     */
    public static String RSA_GetEncrypt(RSAPublicKey publicKey,String encryptType,String body,String encoding)
    		throws Exception {
    	try{
    		if(encoding==null || encoding == "") encoding="utf-8";
    		return RSA_GetEncrypt(publicKey,encryptType,body.getBytes(encoding));
    	}catch(Exception ex) {
    		throw ex;
    	}
    }

    /**
     * <p>Title: RSA_GetEncrypt</p>  
     * <p>Description: 公钥加密 </p>  
     * @param publicKey 公钥
     * @param encryptType 加密类型
     * @param bodyBytes 明文数据的字节数组
     * @return 加密后的字符串
     * @throws Exception 加密异常
     */
    public static String RSA_GetEncrypt(RSAPublicKey publicKey, String encryptType,byte[] bodyBytes)
    		throws Exception {
    	try{
    		// 使用默认RSA
    		Cipher cipher = Cipher.getInstance(encryptType);
    		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    		byte[] output = cipher.doFinal(bodyBytes);
    		return (new String(Base64.encodeBase64(output)));
    	}catch(Exception ex) {
    		throw ex;
    	}
    }

    /**
     * <p>Title: RSA_GetDencrypt</p>  
     * <p>Description: 私钥解密 </p>  
     * @param privateKey 私钥 
     * @param encryptType 加密类型
     * @param bodyBytes 密文数据  
     * @param encoding 编码
     * @return 解密后的字符串
     * @throws Exception 异常
     */
    public static String RSA_GetDencrypt(RSAPrivateKey privateKey,String encryptType, byte[] bodyBytes,String encoding)
            throws Exception {
        try {
            // 使用默认RSA  
        	Cipher cipher = Cipher.getInstance(encryptType);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(bodyBytes);
            return new String(output,encoding);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * <p>Title: RSA_GetDencrypt</p>  
     * <p>Description: 私钥解密</p>  
     * @param privateKey 私钥
     * @param encryptType 加密类型
     * @param body 密文数据  
     * @param encoding 编码
     * @return RSA解密后的字符串
     * @throws Exception RSA解密是异常
     */
    public static String RSA_GetDencrypt(RSAPrivateKey privateKey,String encryptType,String body,String encoding)
    		throws Exception {
    	try {
    		if(encoding==null || encoding == "") encoding="utf-8";
    		byte[] bodyBytes =Base64.decodeBase64(body);
    		return RSA_GetDencrypt(privateKey,encryptType,bodyBytes,encoding);
    	}catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * <p>Title: RSA_GetSign</p>  
     * <p>Description:RSA私钥签名 </p>  
     * @param privateKey 私钥 
     * @param body 签名数据
     * @param encryptType 加密类型
     * @param encoding 字符集编码 
     * @param HashType 计算Hash值时使用的算法类型 
     * @return 签名后的字符串
     * @throws Exception 签名
     */
     public static String RSA_GetSign(String privateKey,String body, String encryptType,String encoding,String HashType)throws Exception
     {
         try
         {
        	 if(encoding==null || encoding == "") encoding="utf-8";
        	 if(HashType==null || HashType == "") HashType="SHA1WithRSA";
        	 PKCS8EncodedKeySpec PKCS8Service = new PKCS8EncodedKeySpec(Base64Utils.decode(privateKey));
             KeyFactory keyFactory = KeyFactory.getInstance(encryptType);
             PrivateKey key = keyFactory.generatePrivate(PKCS8Service);
             java.security.Signature signature = java.security.Signature.getInstance(HashType);
             signature.initSign(key);
             signature.update(body.getBytes(encoding));
             byte[] bytes = signature.sign();
             return Base64Utils.encode(bytes);
         }
         catch (Exception ex)
         {
             throw ex;
         }
     }

     /**
      * <p>Title: RSA_GetVerifySign</p>
      * <p>Description: RSA公钥验签</p>
      * @param publicKey 公钥
      * @param encryptType 加密类型
      * @param body 签名明文数据
      * @param signedBody 签名密文数据
      * @param encoding 字符集编码
      * @param HashType 计算Hash值时使用的算法类型
      * @return 验签是否通过
      * @throws Exception 公钥验签时异常
      */
     public static Boolean RSA_GetVerifySign(String publicKey,String encryptType,String body,String signedBody,String encoding,String HashType)
    		 throws Exception {
    	 try
         {
    		 if(encoding==null || encoding == "") encoding="utf-8";
        	 if(HashType==null || HashType == "") HashType="MD5WithRSA";
             KeyFactory keyFactory = KeyFactory.getInstance(encryptType);
             byte[] keyBytes = Base64.decodeBase64(publicKey);
             PublicKey Key = keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
             java.security.Signature signature = java.security.Signature.getInstance(HashType);
             signature.initVerify(Key);
             signature.update(body.getBytes(encoding));
             return signature.verify(Base64.decodeBase64(signedBody));
         }
         catch (Exception ex)
         {
             throw ex;
         }
     }

     /**
      * <p>Title: getPrivateKey</p>
      * <p>Description: 获取私钥文件 </p>
      * @return 私钥字符串
      */
     public static String getPrivateKey()
     {
         String privateKey=SPUtils.getStringData(Constant.privateKey);
         if (TextUtils.isEmpty(privateKey)){
             try {
                 InputStream inputStream;
                 if (RetrofitClient.getInstance().getBaseUrl().equals(Constant.baseUrl)){
                     inputStream= BaseApplication.getInstance().getAssets().open("rsa_private_key.pem");
                 }else {
                     inputStream= BaseApplication.getInstance().getAssets().open("lsjy_rsa_private_key.pem");
                 }
                 InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
                 BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                 String readLine = null;
                 StringBuilder sb = new StringBuilder();
                 while ((readLine = bufferedReader.readLine()) != null) {
                     sb.append(readLine);
                 }
                 bufferedReader.close();
                 privateKey=sb.toString().replace(
                         "-----BEGIN PRIVATE KEY-----", "")
                         .replace("-----END PRIVATE KEY-----", "");
                 SPUtils.saveData(Constant.privateKey,privateKey);
                 return privateKey;
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }else {
             return privateKey;
         }

         return null;
     }

     /**
      * <p>Title: getPublicKey</p>
      * <p>Description: 获取公钥文件</p>
      * @return 公钥字符串
      */
     public static String getPublicKey()
     {
         String publicKey=SPUtils.getStringData(Constant.publicKey);
         if (TextUtils.isEmpty(publicKey)){
             try {
                 InputStream inputStream;
                 if (RetrofitClient.getInstance().getBaseUrl().equals(Constant.baseUrl)){
                     inputStream= BaseApplication.getInstance().getAssets().open("encrypt_public_key.pem");
                 }else {
                     inputStream= BaseApplication.getInstance().getAssets().open("lsjy_pass_rsa_public_key.pem");
                 }
                 InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
                 BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                 String readLine = null;
                 StringBuilder sb = new StringBuilder();
                 while ((readLine = bufferedReader.readLine()) != null) {
                     sb.append(readLine);
                 }
                 bufferedReader.close();
                 publicKey=sb.toString().replace(
                         "-----BEGIN PUBLIC KEY-----", "")
                         .replace("-----END PUBLIC KEY-----", "");
                 SPUtils.saveData(Constant.publicKey,publicKey);
                 return publicKey;
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }else {
             return publicKey;
         }
         return null;
     }
}
