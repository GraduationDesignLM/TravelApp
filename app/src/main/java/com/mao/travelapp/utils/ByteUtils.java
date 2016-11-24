package com.mao.travelapp.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

/**
 * 文本工具类
 * 
 * @author mao
 *
 */
public class ByteUtils {

	private ByteUtils() {
		throw new UnsupportedOperationException("can't instantiate class ByteUtils");
	}
	
	/**
	 * 字节数组转十六进制表示形式字符串
	 * 
	 * @param b 要转换的字节数组
	 * 
	 * @return 如果data为null那么返回null，否则返回该字节数组的十六进制表示形式字符串
	 */
	public static String bytes2HexString(byte[] data) {
		if(data == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(2 * data.length);
		for(byte b : data) {
			int n = 0x00ff & b;
			if(n < 10) {
				sb.append("0");
			}
			sb.append(Integer.toHexString(n));
		}
		return sb.toString();
	}
	
	/**
	 * 按默认的字符集将字节数组转换为字符串
	 * 
	 * @param data 字节数组
	 * @return 转换成功返回转换成功的字符串，失败返回null
	 */
	public static String bytes2String(byte[] data) {
		return bytes2String(data, null);
	}
	
	/**
	 * 按指定字符集将字节数组转换为字符串
	 * 
	 * @param data 字节数组
	 * @param charset 指定的字符集
	 * @return 转换成功返回转换成功的字符串，失败返回null
	 */
	public static String bytes2String(byte[] data, Charset charset) {
		if(data == null) {
			return null;
		}
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(data);
			return IoUtils.inputStream2String(bais, charset);
		} finally {
			IoUtils.close(bais);
		}
	}
	
	/**
	 * 字符串转字节字节数组，编码方式使用默认编码
	 * 
	 * @param s 要转换的字符串
	 * @return s为null将返回null，否则返回采用默认编码转换后的字节数组
	 */
	public static byte[] string2Bytes(String s) {
		return string2Bytes(s, null);
	}
	
	/**
	 * 按指定编码将字符串转字节字节数组
	 * 
	 * @param s 要转换的字符串
	 * @param charsetName 指定的编码方式
	 * @return 如果s为null那么将返回null，charsetName为null将会使用默认编码进行转换，如果
	 * 		   指定的编码不支持将会返回null
	 */
	public static byte[] string2Bytes(String s, String charsetName) {
		if(s == null) {
			return null;
		}
		if(charsetName == null) {
			return s.getBytes();
		} else {
			try {
				return s.getBytes(charsetName);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	/**
	 * 位图转字节数组
	 * 
	 * @param bitmap 
	 * @return
	 */
	public static byte[] bitmap2Bytes(Bitmap bitmap) {
		if(bitmap == null) {
			return null;
		}
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream(BitmapUtils.sizeOfBitmap(bitmap));
			bitmap.compress(CompressFormat.JPEG, 100, baos);
			return baos.toByteArray();
		} finally {
			IoUtils.close(baos);
		}
	}
	
	/**
	 * 字节数组转位图
	 * 
	 * @param data 要转换的字节数组
	 * @return 转换成功返回一张位图，失败返回null
	 */
	public static Bitmap bytes2Bitmap(byte[] data) {
		if(data == null) {
			return null;
		}
		return BitmapFactory.decodeByteArray(data, 0, data.length);
	}

}
