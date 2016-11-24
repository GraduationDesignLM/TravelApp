package com.mao.travelapp.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * IO流工具类
 * 
 * @author mao
 *
 */
public class IoUtils {

	private IoUtils() {
		throw new UnsupportedOperationException("can't instantiate class IoUtils");
	}
	
	/**
	 * 关闭流
	 * 
	 * @param stream 要关闭的流对象
	 * @return 关闭成功返回true，失败返回false
	 */
	public static boolean close(Closeable stream) {
		if(stream != null) {
			try {
				stream.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 采用对象序列化实现彻底深度对象复制
	 * 
	 * @param obj 要复制的对象
	 * @return 复制成功相应新的对象,复制失败返回null.
	 */
	public static <T extends Serializable> T copyObject(Object obj) {
		if(obj == null) {
			return null;
		}
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			bais = new ByteArrayInputStream(baos.toByteArray());
			ois = new ObjectInputStream(bais);
			Object object = ois.readObject();
			return (T) object;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			close(oos);
			close(baos);
			close(ois);
			close(bais);
		}
	}
	
	
	/**
	 * 通过路径获取输出流，如果指定的路径不存在会自动创建
	 * 
	 * @param path
	 * @return 
	 * 
	 * @see #getOutputStream(String, boolean)
	 */
	public static OutputStream getOutputStream(String path) {
		return getOutputStream(path, true);
	}
	
	/**
	 * 通过路径获取输出流
	 * 
	 * @param path
	 * @param autoCreated true表示当路径不存在时自动创建，false表示不创建
	 * @return
	 */
	public static OutputStream getOutputStream(String path, boolean autoCreated) {
		if(!FileUtils.exist(path)) {
			if(!autoCreated || !FileUtils.createFile(path)) {
				return null;
			}
		}
		OutputStream os = null;
		try {
			os = new FileOutputStream(path);
			return os;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 按默认字符集将输入流转换为字符串
	 * 
	 * @param is 输入流
	 * @return 转换成功返回对应的字符串，失败返回null
	 */
	public static String inputStream2String(InputStream is) {
		return inputStream2String(is, null);
	}
	
	/**
	 * 按指定字符集将输入流转换为字符串
	 * 
	 * @param is 输入流
	 * @param charset 指定的字符集
	 * @return 转换成功返回对应的字符串，失败返回null
	 */
	public static String inputStream2String(InputStream is, Charset charset) {
		if(is == null) {
			return null;
		}
		byte[] buffer = new byte[1024];
		int length;
		StringBuilder sb = new StringBuilder();
		try {
			while((length = is.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, length, charset));
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
