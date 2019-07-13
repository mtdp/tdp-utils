package com.github.mtdp.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mtdp.TdpUtilConstants;

/**
 * 
 *
 * @Description 加密工具类 
 * @author gqwang
 * @date 2016年1月26日下午3:29:24
 *
 */
public class EncryptUtil {
	
	private static Logger logger = LoggerFactory.getLogger(EncryptUtil.class);
	
	/**
	 * 用md5算法对src字符串进行加密并返回加密后的字符串
	 * @param src
	 * @return
	 */
	public static String md5(String src){
		String result = null;
        if (StringUtil.isNotBlank(src)) {
            try {
                // 指定加密的方式为MD5
                MessageDigest md = MessageDigest.getInstance("MD5");
                // 进行加密运算
                byte bytes[] = md.digest(src.getBytes());
                result = bytesToHexString(bytes);
            } catch (NoSuchAlgorithmException e) {
               logger.error("找不到md5算法",e);
            }
        }
        return result;
	}
	
	/**
	 * 使用sha加密字符串
	 * @param str
	 * @return
	 */
	public static String sha(String str){
		String result = null;
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA");
			byte[] encodeByteArr = sha.digest(str.getBytes(TdpUtilConstants.CHARACTER_UTF_8));
			result = bytesToHexString(encodeByteArr);
		} catch (NoSuchAlgorithmException e) {
			logger.error("获取sha加密方式出错",e);
			throw new RuntimeException("获取sha加密方式出错");
		}catch (UnsupportedEncodingException e) {
			logger.error("获取字符串编码出错,编码是="+TdpUtilConstants.CHARACTER_UTF_8,e);
			throw new RuntimeException("获取sha加密方式出错");
		}
		return result;
	}
	
	/**
	 * 字节数组转换成16进制字符串
	 * @param bytes
	 * @return
	 */
	public static String bytesToHexString(byte[] bytes){
		StringBuffer result = new StringBuffer();
		for(int i = 0; i < bytes.length; i++){
			int t = bytes[i] & 0xFF;
			String hexStr = Integer.toHexString(t);
			if(hexStr.length() < 2){
				result.append("0");
			}
			result.append(hexStr);
		}
		return result.toString();
	}
	
	/**
	 * 16进制字符串转换成字节数组
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString){
		if(StringUtil.isBlank(hexString)){
			return null;
		}
		byte[] byteArr = new byte[hexString.length()/2];
		hexString = hexString.toUpperCase();
		char[] chs = hexString.toCharArray();
		for(int i = 0; i < chs.length; i++){
			int p = i * 2;
			byteArr[i] = (byte)(charToByte(chs[p]) << 4 | charToByte(chs[p + 1]));
		}
		return byteArr;
	}
	
	/**
	 * 将字符串按照utf8edcode编码
	 * @param str
	 * @return
	 */
	public static String base64fEncodeByUtf8(String str){
		String result = null;
		if(StringUtil.isBlank(str)){
			return null;
		}
		try {
			result = Base64.encodeBase64String(str.getBytes(TdpUtilConstants.CHARACTER_UTF_8));
		} catch (UnsupportedEncodingException e) {
			logger.error("获取字符串编码出错,编码是="+TdpUtilConstants.CHARACTER_UTF_8,e);
		}
		return result;
	}
	
	/**
	 * 将byte数组按照utf8edcode编码
	 * @param str
	 * @return
	 */
	public static String base64fEncodeByByte(byte[] data){
		String result = null;
		if(data == null){
			return null;
		}
		result =Base64.encodeBase64String(data);
		return result;
	}
	
	private static byte charToByte(char ch){
		return (byte)"0123456789ABCDEF".indexOf(ch);
	}
	
	public static void main(String[] args) {
		//aff024fe4ab0fece4091de044c58c9ae4233383a
		String encode = sha("test");
		System.out.println(encode);
		System.out.println(Arrays.toString("w".getBytes()));
//		System.out.println(hexStringToBytes(bytesToHexString("wdd".getBytes())));
		System.out.println(bytesToHexString("password".getBytes()));
		System.out.println(md5("password"));
	}
	


}
