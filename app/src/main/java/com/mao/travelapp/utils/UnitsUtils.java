package com.mao.travelapp.utils;

import java.text.DecimalFormat;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 单位相互转换工具类
 * 
 * @author mao
 *
 */
public class UnitsUtils {

	private UnitsUtils() {
		throw new UnsupportedOperationException("can't instantiate class UnitsUtils");
	}
	
	/**
	 * px转dp
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * dp转px
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * px转sp
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * sp转px
	 * 
	 * @param context
	 * @param spValue
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * sp转dp
	 * 
	 * @param context
	 * @param spValue
	 * @return
	 */
	public static int sp2dp(Context context, float spValue) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float scale = metrics.scaledDensity / metrics.density;
		return (int) (spValue / scale + 0.5f);
	}

	/**
	 * dp转sp
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dp2sp(Context context, float dpValue) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float scale = metrics.scaledDensity / metrics.density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	/**
	 * 将以字节为单位转换为合适的单位表示,保留两位小数
	 * 
	 * @param size 要转换的大小,以字节为单位
	 * @return 转换成功返回相应的字符串表示,失败返回null.
	 */
	public static String convertByteCount(long size) {
		if(size < 0) {
			return null;
		}
		long KB = 1024;
		long MB = 1024 * KB;
		long GB = 1024 * MB;
		long TB = 1024 * GB;
		DecimalFormat format = new DecimalFormat("#0.00");
		StringBuilder sb = new StringBuilder(8);
		if(size < KB) {
			sb.append(format.format(size));
			sb.append("B");
		} else if(size < MB) {
			sb.append(format.format(size / KB));
			sb.append("KB");
		} else if(size < GB) {
			sb.append(format.format(size / MB));
			sb.append("MB");
		} else if(size < TB) {
			sb.append(format.format(size / GB));
			sb.append("GB");
		} else {
			sb.append(format.format(size / TB));
			sb.append("TB");		
		}
		return sb.toString();
	}
}
