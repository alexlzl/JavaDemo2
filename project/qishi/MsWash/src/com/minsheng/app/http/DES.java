package com.minsheng.app.http;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
/**
 * 
* @ClassName: DESUtil 
* @Description: des加解密工具类
* @author bushujie 
* @date 2014-12-9 下午5:57:14 
*
 */
public class DES {
	
	/**
	 * 密钥
	 */
	private static final String keyString="efd695c9";
	/**
	 * 虚拟密钥
	 */
	private static final String ivString="fe0a5cb0";
	
	/**
	 * 
	* @Title: desCrypto 
	* @Description: des加密
	* @param @param encryptString
	* @param @param encryptKey
	* @param @param ivString
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String encrypt(String content){  
		try {
			IvParameterSpec iv = new IvParameterSpec(ivString.getBytes());  
			DESKeySpec dks = new DESKeySpec(keyString.getBytes());  
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
			SecretKey key = keyFactory.generateSecret(dks);  
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");  
			cipher.init(Cipher.ENCRYPT_MODE, key, iv); 
			byte[] result=cipher.doFinal(content.getBytes("utf-8"));
			return DESPlus.byteArr2HexStr(result); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	* @Title: desCrypto 
	* @Description:DES解密
	* @param @param encryptString
	* @param @param encryptKey
	* @param @param ivString
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String decrypt(String content){  
		try {
			IvParameterSpec iv = new IvParameterSpec(ivString.getBytes());  
			DESKeySpec dks = new DESKeySpec(keyString.getBytes());  
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
			SecretKey key = keyFactory.generateSecret(dks);  
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");  
			cipher.init(Cipher.DECRYPT_MODE, key, iv); 
			byte[] result=cipher.doFinal(DESPlus.hexStr2ByteArr(content));
			return new String (result,"utf-8"); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
