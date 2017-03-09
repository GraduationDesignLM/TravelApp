package com.mao.travelapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 * 
 * @author mao
 *
 */
public class TimeUtils {

	private TimeUtils() {
		throw new UnsupportedOperationException("can't instantiate class TimeUtils");
	}
	
	/**
	 * 时间戳转Date对象
	 * 
	 * @param timestamp
	 * @return
	 */
	public static Date timestamp2Date(long timestamp) {
		return new Date(timestamp);
	}
	
	/**
	 * Date转时间戳
	 * 
	 * @param d
	 * @return
	 */
	public static long date2timestamp(Date d) {
		if(d == null) {
			return 0;
		}
		return d.getTime();
	}
	
	/**
	 * 时间戳转正常时间格式
	 * 
	 * @param timestamp 要转换的时间戳
	 * @return
	 */
	public static String timestamp2DateString(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
		return sdf.format(timestamp2Date(timestamp));
	}

	/**
	 * 发布时间间隔
	 *
	 * @param timestamp 要转换的时间戳
	 * @return
     */
	public static String timestamp2DesriptionString(long timestamp) {
		long time = System.currentTimeMillis();
		
		long minute = 60 * 1000;
		long hour = 60 * minute;
		
		long value = timestamp - time;
		//非法
		if(value < 0) {
			return "";
		//1分钟内
		} else if(value < minute) {
			return "刚刚";
		//1小时内
		} else if(value < hour) {
			return value / hour + "分钟前";
		} else if(value < 24 * hour) {
			return value / hour + "小时前";
		} else {
			return timestamp2DateString(timestamp);
		}
	}

	/**
	 * 获取两个时间的时间差，单位：毫秒
	 * 
	 * @param d1
	 * @param d2
	 * @return 返回d1-d2，单位：毫秒
	 */
	public static long getTimestampDelta(Date d1, Date d2) {
		long timestamp1 = date2timestamp(d1);
		long timestamp2 = date2timestamp(d2);
		return timestamp1 - timestamp2;
	}
	
	/**
	 * 获取指定日期的年份
	 * 
	 * @param d
	 * @return
	 */
	public static int getYear(Date d) {
		return getYear(date2timestamp(d));
	}
	
	/**
	 * 获取指定日期的年份
	 * 
	 * @param timestamp
	 * @return
	 */
	public static int getYear(long timestamp) {
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		return calendar.get(Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * 获取指定日期的月份
	 * 
	 * @param d
	 * @return
	 */
	public static int getMonth(Date d) {
		return getYear(date2timestamp(d));
	}
	
	/**
	 * 获取指定日期的月份
	 * 
	 * @param d
	 * @return
	 */
	public static int getMonth(long timestamp) {
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		return calendar.get(Calendar.MONTH);
	}
	
	/**
	 * 获取指定日期在所有星期第几天，从星期天开始
	 * 
	 * @param d
	 * @return
	 */
	public static int getDayOfWeek(Date d) {
		return getYear(date2timestamp(d));
	}
	
	/**
	 * 获取指定日期在所有星期第几天，从星期天开始
	 * 
	 * @param d
	 * @return
	 */
	public static int getDayOfWeek(long timestamp) {
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * 获取指定日期在所有月份第几天
	 * 
	 * @param d
	 * @return
	 */
	public static int getDayOfMonth(Date d) {
		return getYear(date2timestamp(d));
	}
	
	/**
	 * 获取指定日期在所有月份第几天
	 * 
	 * @param d
	 * @return
	 */
	public static int getDayOfMonth(long timestamp) {
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 获取指定日期在所有年份第几天
	 * 
	 * @param d
	 * @return
	 */
	public static int getDayOfYear(Date d) {
		return getYear(date2timestamp(d));
	}
	
	/**
	 * 获取指定日期在所有年份第几天
	 * 
	 * @param d
	 * @return
	 */
	public static int getDayOfYear(long timestamp) {
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		return calendar.get(Calendar.DAY_OF_YEAR);
	}
}
