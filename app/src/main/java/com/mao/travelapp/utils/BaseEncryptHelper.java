package com.mao.travelapp.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.text.TextUtils;
import android.util.Base64;

/**
 * 基本的加密解密工具类
 * 
 * @author mao
 *
 */
public class BaseEncryptHelper {

	private BaseEncryptHelper() {
		throw new UnsupportedOperationException("can't instantiate class EncryptHelper");
	}
	
	/**
	 * MD5加密
	 * 
	 * @param raw 要加密的字符串
	 * @return 加密成功返回加密后的字符串，加密失败返回null
	 */
	public static String md5(String raw) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] data = md5.digest(raw.getBytes(Charset.defaultCharset()));
			String result = ByteUtils.bytes2HexString(data);
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Base64编码
	 * 
	 * @param data 要进行编码的字节数组
	 * @return 编码成功返回编码后的字节数组，失败返回null
	 */
	public static byte[] encodeBase64(byte[] data) {
		if(data == null) {
			return null;
		}
		return Base64.encode(data, Base64.DEFAULT);
	}
	
	/**
	 * Base64解码
	 * 
	 * @param data 要进行解码的字节数组
	 * @return 解码成功返回解码后的字节数组，失败返回null
	 */
	public static byte[] decodeBase64(byte[] data) {
		if(data == null) {
			return null;
		}
		return Base64.decode(data, Base64.DEFAULT);
	}
	
	/**
	 * 采用默认字符集进行URL编码
	 * 
	 * @param raw
	 * @return
	 */
	public static String encodeURL(String raw) {
		return encodeURL(raw, null);
	}
	
	/**
	 * 采用指定字符集进行URL编码
	 * 
	 * @param raw
	 * @param charsetName
	 * @return
	 */
	public static String encodeURL(String raw, String charsetName) {
		if(raw == null) {
			return null;
		}
		try {
			if(TextUtils.isEmpty(charsetName)) {
				charsetName = Charset.defaultCharset().displayName();
			}
			return URLEncoder.encode(raw, charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 采用指定字符集进行URL解码
	 * 
	 * @param s
	 * @param charsetName
	 * @return
	 */
	public static String decodeURL(String s) {
		return decodeURL(s, null);
	}
	
	/**
	 * 采用指定字符集进行URL解码
	 * 
	 * @param s
	 * @param charsetName
	 * @return
	 */
	public static String decodeURL(String s, String charsetName) {
		if(s == null) {
			return null;
		}
		try {
			if(TextUtils.isEmpty(charsetName)) {
				charsetName = Charset.defaultCharset().displayName();
			}
			return URLDecoder.decode(s, charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
