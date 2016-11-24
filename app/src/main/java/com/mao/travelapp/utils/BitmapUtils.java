package com.mao.travelapp.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;

/**
 * 图片处理工具类
 * 
 * @author mao
 *
 */
public class BitmapUtils {

	private BitmapUtils() {
		throw new UnsupportedOperationException("can't instantiate class BitmapUtils");
	}
	
	
	/**
	 * 位图处理相关
	 * 
	 */
	
	/**
	 * 创建圆形位图
	 * 
	 * @param bitmap 
	 * @return
	 */
	public static Bitmap createCircleBitmap(Bitmap bitmap) {
		if(bitmap == null) {
			return null;
		}
		
		int bmWidth = bitmap.getWidth();
		int bmHeight = bitmap.getHeight();
		int side = bmWidth < bmHeight ? bmWidth : bmHeight;
		int x = (bmWidth - side) / 2;
		int y = (bmHeight - side) / 2;
		Bitmap newBm = Bitmap.createBitmap(side, side, Bitmap.Config.ARGB_8888);
		if(newBm != null) {
			Canvas canvas = new Canvas(newBm);
		    Paint paint = new Paint();
		    paint.setAntiAlias(true);
		    paint.setFilterBitmap(true);
		    paint.setDither(true);
		   
		    Rect rect = new Rect(0, 0, newBm.getWidth(), newBm.getHeight());
		    canvas.drawARGB(0, 0, 0, 0);
		    canvas.drawCircle(newBm.getWidth()/2, newBm.getHeight()/2, newBm.getHeight()/2, paint);
		    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		    canvas.drawBitmap(bitmap, rect, rect, paint);
		    
		    return newBm;
		}
		return null;
	}
	
	
	
	
	
	/**
	 * 位图信息相关
	 * 
	 */
	
	/**
     * <p>获取Bitmap大小,该方法为版本兼容的.</p>
     * 
     * @param bitmap 指定的位图,注意不可为null.
     * 
     * @return 如果bitmap为null那么将返回-1,否则返回指定位图的大小,单位为字节.
     * 
     * @author 麦灿标
     * */
    @SuppressLint("NewApi")
	public static int sizeOfBitmap(Bitmap bitmap) {
    	if(bitmap == null) {
    		return -1;
    	}
    	//API 19及以上版本
    	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    		return bitmap.getAllocationByteCount();
    	//API 12 ~ API 18
    	} else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
    		return bitmap.getByteCount();
    	//API 12以下
    	} else {
    		return bitmap.getRowBytes() * bitmap.getHeight();
    	}
    }
    
    
    
    
    
    /**
     * 一系列转换
     */
    
    /**
     * Drawable转Bitmap
     * 
     * @param d 要转换的Drawable对象,不能为null.
     * @return 转换成功返回一幅位图,失败返回null.
     */
    public static Bitmap drawable2Bitmap(Drawable d) {
    	if(d == null) {
    		return null;
    	}
    	Bitmap bm = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    	Canvas canvas = new Canvas(bm);
    	d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
    	d.draw(canvas);
    	return bm;
    }
    
    /**
     * Bitmap转Drawable
     * 
     * @param res
     * @param bitmap
     * @return
     */
    public static Drawable bitmap2drawable(Resources res, Bitmap bitmap) {
    	if(res == null || bitmap == null) {
    		return null;
    	}
    	BitmapDrawable bd = new BitmapDrawable(res, bitmap);
    	return bd;
    }
    
    /**
     * Bitmap转字节数组
     * 
     * @param bitmap
     * @return
     */
    public static byte[] bitmap2Byte(Bitmap bitmap) {
    	if(bitmap == null) {
    		return null;
    	}
    	int size = bitmap.getWidth() * bitmap.getHeight();
    	ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
    	bitmap.compress(CompressFormat.JPEG, 100, baos);
    	return baos.toByteArray();
    }
    
    /**
     * 字节数组转Bitmap
     * 
     * @param data
     * @return
     */
    public static Bitmap byte2Bitmap(byte[] data) {
    	if(data == null) {
    		return null;
    	}
    	return BitmapFactory.decodeByteArray(data, 0, data.length);
    }
    
    
    
    
    /**
     * 位图创建保存相关
     * 
     */
    
    /**
     * 通过文件路径获取指定质量的位图
     * 
     * @param pathName 路径，不能为null
     * @param opts 指定位图的质量，如果为null表示获取原质量的位图
     * @return
     */
    public static Bitmap decodeBitmapFromFile(String pathName, BitmapFactory.Options opts) {
    	if(TextUtils.isEmpty(pathName)) {
    		return null;
    	}
    	return BitmapFactory.decodeFile(pathName, opts);
    }
    
    /**
     * 从网络获取Bitmap
     * 
     * @param url
     * @param opts
     * @return
     */
    public static Bitmap decodeBitmapFromNetwork(String url, BitmapFactory.Options opts) {
    	if(TextUtils.isEmpty(url)) {
    		return null;
    	}
    	InputStream is = null;
    	try {
    		URL networkUrl = new URL(url);
    		is = networkUrl.openStream();
    		return BitmapFactory.decodeStream(is, null, opts);
    	} catch (MalformedURLException e) {
    		e.printStackTrace();
    		return null;
    	} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			IoUtils.close(is);
		}
    }
    
    /**
     * 保存一张位图到指定路径
     * 
     * @param bitmap
     * @param path
     * @param quality
     * @return
     */
    public static boolean saveBitmap(Bitmap bitmap, String path, int quality) {
    	if(bitmap == null || TextUtils.isEmpty(path)
    					  || quality < 0
    					  || quality > 100) {
    		return false;
    	}
    	OutputStream stream = IoUtils.getOutputStream(path);
    	try {
	    	if(stream == null) {
	    		return false;
	    	} else {
	    		return bitmap.compress(CompressFormat.JPEG, quality, stream);
	    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	} finally {
			IoUtils.close(stream);
		}
    }
}
