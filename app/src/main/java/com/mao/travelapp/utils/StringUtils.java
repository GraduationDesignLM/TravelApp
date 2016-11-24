package com.mao.travelapp.utils;

import android.graphics.Paint;

/**
 * 字符串工具类
 * 
 * @author mao
 *
 */
public class StringUtils {

	private StringUtils() {
		throw new UnsupportedOperationException("can't instantiate class StringUtils");
	}
	
	/**
	 * 获取指定文本在指定字体大小下的宽度
	 * 
	 * @param text 指定的文本
	 * @param size 指定的字体大小
	 * @return 返回指定文本在指定字体大小下的宽度
	 */
	public static float getTextLength(String text, float size) {
		if(android.text.TextUtils.isEmpty(text) || size <= 0) {
			return 0;
		}
		Paint paint = new Paint();
		paint.setTextSize(size);
		return paint.measureText(text);
	}
}
