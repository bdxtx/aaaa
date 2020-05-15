/**  
* <p>Title: RsaUtils.java</p>
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2017</p>  
* <p>Company: www.luosijiaoyu.com</p>  
* @author zhangpp  
* @date 2020年4月10日  
* @version 1.0  
*/ 
package com.weiyu.sp.lsjy.utils;
import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**  
* <p>Title: RsaUtils</p>  
* <p>Description: </p>  
* @author zhangpp  
* @date 2020年4月10日  
*/
public class RsaUtils {

	   public static String encryptDataStr(byte[] data, PublicKey publicKey) {
	         try {
	             Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	             // 编码前设定编码方式及密钥
	             cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	             // 传入编码数据并返回编码结果
				 String result = Base64Utils.encode(cipher.doFinal(data));
	             return URLEncoder.encode(result, "UTF-8");
	         } catch (Exception e) {
	             e.printStackTrace();
	             return null;
	         }
	     }

	public static String encryptDataStr(byte[] data, PublicKey publicKey,boolean b) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			// 编码前设定编码方式及密钥
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			// 传入编码数据并返回编码结果
			String result = Base64Utils.encode(cipher.doFinal(data));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
