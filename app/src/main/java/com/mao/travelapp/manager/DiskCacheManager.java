package com.mao.travelapp.manager;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

/**
 * 磁盘缓存管理器
 * 
 * @author mao
 *
 */
public class DiskCacheManager {

	private final static DiskCacheManager sInstance = new DiskCacheManager();
	
	private DiskCacheManager() {}
	
	public static DiskCacheManager getInstance() {
		return sInstance;
	}
	
	/**
	 * 获取缓存根目录
	 * 
	 * @param context 上下文
	 * @return 返回缓存根目录
	 */
	public String getCacheRootPath(Context context) {
		if(context == null) {
			return null;
		}
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return context.getExternalCacheDir().getAbsolutePath();
		} else {
			return context.getCacheDir().getAbsolutePath();
		}
	}
	
	/**
	 * 获取某GUID对应的图片缓存目录
	 * 
	 * @param context 上下文
	 * @param GUID GUID
	 * @return 获取成功返回对应的目录路径,失败返回null.
	 */
	public String getPictureCachePath(Context context, String GUID) {
		if(context == null || TextUtils.isEmpty(GUID)) {
			return null;
		}
		StringBuilder sb = new StringBuilder(DiskCacheManager.getInstance().getCacheRootPath(context));
		sb.append("/");
		sb.append(GUID);
		sb.append("/");
		return sb.toString();
	}
	
	/**
	 * 获取应用主要的存储目录
	 * 
	 * @return 返回应用主要的存储目录
	 */
	public String getMainDirectory() {
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return Environment.getExternalStorageDirectory().getPath() + "/TravelRecorder/";
		} else {
			return "";
		}
	}
}
