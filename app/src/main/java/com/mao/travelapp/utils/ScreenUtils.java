package com.mao.travelapp.utils;

import java.lang.reflect.Field;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * 屏幕相关工具类
 * 
 * @author mao
 *
 */
public class ScreenUtils {

	private ScreenUtils() {
		throw new UnsupportedOperationException("can't instantiate class ScreenUtils");
	}
	
	/**
	 * 获取屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidthPixels(Activity at) {
		if(at == null) {
			return 0;
		}
		DisplayMetrics metric = new DisplayMetrics();
		at.getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric.widthPixels;
	}
	
	/**
	 * 获取屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeightPixels(Activity at) {
		if(at == null) {
			return 0;
		}
		DisplayMetrics metric = new DisplayMetrics();
		at.getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric.heightPixels;
	}
	
	/**
	 * 获取状态栏高度，单位：dp
	 * 
	 * @return 返回状态栏高度，注意单位为dp
	 */
	public static int getStatusBarHeight() {
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object obj = clazz.newInstance();
			Field field = clazz.getField("status_bar_height");
			int height = Integer.parseInt(((String)field.get(obj)));
			if(height >= 0) {
				return height;
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
