package com.mao.travelapp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.text.TextUtils;

/**
 * 文件工具类
 * 
 * @author mao
 */
public class FileUtils {

	private FileUtils() {
		throw new UnsupportedOperationException("can't instantiate class FileUtils");
	}
	
	/**
	 * 查询相关
	 * 
	 */
	
	/**
	 * 判断文件或者目录是否存在
	 * 
	 * @param path 要判断的路径
	 * @return 存在返回true，否则返回false
	 */
	public static boolean exist(String path) {
		if(TextUtils.isEmpty(path)) {
			return false;
		}
		return exist(new File(path));
	}
	
	/**
	 * 判断文件或者目录是否存在
	 * 
	 * @param file 要判断的文件对象
	 * @return 存在返回true，否则返回false
	 */
	public static boolean exist(File file) {
		if(file == null) {
			return false;
		}
		return file.exists();
	}
	
	/**
	 * 判断指定路径是否是文件
	 * 
	 * @param path 指定的路径
	 * @return 存在且是文件返回true，否则返回false
	 */
	public static boolean isFile(String path) {
		if(!exist(path)) {
			return false;
		}
		File f = new File(path);
		return f.isFile();
	}
	
	/**
	 * 判断指定路径是否是目录
	 * 
	 * @param path 指定的路径
	 * @return 存在且是目录返回true，否则返回false
	 */
	public static boolean isDirectory(String path) {
		if(!exist(path)) {
			return false;
		}
		File f = new File(path);
		return f.isDirectory();
	}
	
	/**
	 * 判断指定路径是否是隐藏文件
	 * 
	 * @param path 指定的路径
	 * @return 存在且是隐藏文件返回true，否则返回false
	 */
	public static boolean isHidden(String path) {
		if(!exist(path)) {
			return false;
		}
		File f = new File(path);
		return f.isHidden();
	}
	
	/**
	 * 获取指定目录下所有文件及文件夹名
	 * 
	 * @param path 指定的目录路径
	 * @return 如果指定的路径不存在或者不是目录那么返回null，否则返回该目录下所有文件及文件夹名
	 */
	public static String[] listFile(String path) {
		if(!isDirectory(path)) {
			return null;
		}
		File f = new File(path);
		return f.list();
	}
	
	/**
	 * 判断指定目录是否是空目录
	 * 
	 * @param path 要判断的目录路径
	 * @return 如果指定的目录存在而且目录为空，那么返回true，否则返回false
	 */
	public static boolean isEmptyDirectory(String path) {
		String[] childs = listFile(path);
		if(childs != null && childs.length > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 获取指定文件或者目录(递归)大小，单位：字节
	 * 
	 * @param path 指定的路径
	 * @return 返回指定的文件或者目录大小
	 */
	public static long getSize(String path) {
		return calculateSize(path);
	}
	
	//递归计算目录大小
	private static long calculateSize(String path) {
		if(isDirectory(path)) {
			String[] childs = listFile(path);
			if(childs != null) {
				long count = 0;
				for(String p : childs) {
					count += calculateSize(p);
				}
				return count;
			} else {
				return 0;
			}
		} else if(isFile(path)) {
			return getFileSize(path);
		} else {
			return 0;
		}
	}
	
	/**
	 * 获取指定文件大小，单位：字节
	 * 
	 * @param path 指定的文件路径
	 * @return 如果指定的文件不存在或者不是文件，那么返回0，否则返回文件大小
	 */
	public static long getFileSize(String path) {
		if(isFile(path)) {
			InputStream is = null;
			try {
				is = new FileInputStream(path);
				return is.available();
			} catch (IOException e) {
				e.printStackTrace();
				return 0;
			} finally {
				IoUtils.close(is);
			}
		} else {
			return 0;
		}
	}
	
	
	
	
	
	/**
	 * 删除相关
	 */
	
	/**
	 * 删除指定路径的文件或者目录，如果是目录会将整个目录删除掉
	 * 
	 * @param path 要删除的文件或目录路径
	 */
	public static void delete(String path) {
		if(isDirectory(path)) {
			deleteDirectory(path);
		} else if(isFile(path)) {
			deleteFile(path);
		}
	}
	
	//删除目录
	private static void deleteDirectory(String path) {
		if(isDirectory(path)) {
			if(isEmptyDirectory(path)) {
				deleteFile(path);
			} else {
				String[] childs = listFile(path);
				if(childs != null) {
					for(String p : childs) {
						deleteDirectory(p);
					}
				} else {
					
				}
			}
		} else if(isFile(path)) {
			deleteFile(path);
		} else {
			
		}
	}
	
	/**
	 * 删除指定的文件或者空目录
	 * 
	 * @param path 要删除的文件路径
	 * @return 如果指定的文件存在且删除成功那么返回true，否则返回false
	 */
	public static boolean deleteFile(String path) {
		if(isFile(path)) {
			File f = new File(path);
			return f.delete();
		}
		return false;
	}
	
	/**
	 * 清空文件或者目录，如果是文件会清空里面的所有内容，如果是目录会删除目录下的
	 * 所有文件和文件夹
	 * 
	 * @param path 要清空的文化或目录路径
	 */
	public static void clear(String path) {
		if(isDirectory(path)) {
			String[] childs = listFile(path);
			if(childs != null) {
				for(String p : childs) {
					delete(p);
				}
			}
		} else if(isFile(path)) {
			clearFile(path);
		}
	}
	
	/**
	 * 清空文件
	 * 
	 * @param path 要清空的文件路径
	 * @return 清空成功返回true，失败返回false
	 */
	public static boolean clearFile(String path) {
		if(isFile(path)) {
			delete(path);
			return createFile(path);
		}
		return false;
	}
	
	
	
	/**
	 * 创建相关 
	 */
	
	
	/**
	 * 创建指定文件，如果该文件所在的目录不存在会自动创建其目录
	 * 
	 * @param path 要创建的文件的路径
	 * @return 创建成功返回true，失败返回false
	 */
	public static boolean createFile(String path) {
		File f = new File(path);
		String parent = f.getParent();
		if(exist(parent) || createDirectory(parent)) {
			try {
				return f.createNewFile();	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 创建指定目录(会进行递归创建)
	 * 
	 * @param path 要创建的目录路径
	 * @return 如果指定的目录不存在且创建成功那么返回true，否则返回false
	 */
	public static boolean createDirectory(String path) {
		if(!TextUtils.isEmpty(path)) {
			File f = new File(path);
			return f.mkdirs();
		} else {
			return false;
		}
	}
}
