package com.mao.travelapp.utils;

import android.content.Context;

/**
 * 当前App相关信息工具类
 * 
 * @author mao
 *
 */
public class AppUtils {

	/**
	 * 获取当前应用的包名
	 * 
	 * @param context 
	 * @return
	 */
	public static String getPackageName(Context context) {
		if(context == null) {
			return null;
		}
		return context.getApplicationContext().getPackageName();
	}
}
