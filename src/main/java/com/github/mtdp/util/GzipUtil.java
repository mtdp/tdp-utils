package com.github.mtdp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mtdp.TdpUtilConstants;

/**
 * 
 * 
 * @Description gzip 数据解压缩
 * @author wangguoqing
 * @date 2016年7月5日下午5:36:01
 * 
 */
public class GzipUtil {

	private static Logger logger = LoggerFactory.getLogger(GzipUtil.class);
	
	/**读取文件的缓存大小**/
	public static final int BUFFER = 8 * 1024;

	/**
	 * 数据压缩
	 * @param data
	 * @return
	 */
	public static byte[] compress(byte[] data) {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] byteArr = null;
		try {
			// 压缩
			compress(bis, bos);
			bos.flush();
			byteArr = bos.toByteArray();
		} catch (IOException e) {
			logger.error("压缩数据出错", e);
		} finally {
			try {
				bos.close();
				bis.close();
			} catch (IOException e) {
			}
		}
		return byteArr;
	}

	/**
	 * 数据压缩
	 * @param is
	 * @param os
	 */
	public static void compress(InputStream is, OutputStream os) {
		GZIPOutputStream gos = null;
		try {
			gos = new GZIPOutputStream(os);
			int count;
			byte[] data = new byte[BUFFER];
			while ((count = is.read(data, 0, BUFFER)) != -1) {
				gos.write(data, 0, count);
			}
			gos.finish();
			gos.flush();
		} catch (IOException e) {
			logger.error("压缩数据出错", e);
		} finally {
			try {
				gos.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 文件压缩
	 * @param file
	 */
	public static void compress(File file) {
		compress(file, true);
	}

	/**
	 * 文件压缩
	 * @param path
	 */
	public static void compress(String path) {
		compress(path, true);
	}

	/**
	 * 文件压缩
	 * @param path
	 * @param delete 是否删除原始文件
	 */
	public static void compress(String path, boolean delete) {
		File file = new File(path);
		compress(file, delete);
	}

	/**
	 * 文件压缩<br>
	 * 文件压缩后,存放路径与原文件一样,在原文件名称后添加.gz
	 * 
	 * @param file
	 * @param delete 是否删除原始文件
	 */
	public static void compress(File file, boolean delete) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(file);
			// 压缩文件后的全路径
			fos = new FileOutputStream(file.getPath()
					+ TdpUtilConstants.GZIP_EXT_NAME);
			compress(fis, fos);
			fos.flush();
			if(delete) {
				file.delete();
			}
		} catch (IOException e) {
			logger.error("文件压缩出错", e);
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 数据解压缩
	 * @param data
	 * @return
	 */
	public static byte[] decompress(byte[] data) {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] byteArr = null;
		try {
			// 解压缩
			decompress(bis, bos);
			bos.flush();
			byteArr = bos.toByteArray();
		} catch (Exception e) {
			logger.error("数据解压出错", e);
		} finally {
			try {
				bos.close();
				bis.close();
			} catch (IOException e) {
			}
		}
		return byteArr;
	}

	/**
	 * 数据解压缩
	 * @param is
	 * @param os
	 */
	public static void decompress(InputStream is, OutputStream os) {
		GZIPInputStream gis = null;
		try {
			gis = new GZIPInputStream(is);
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = gis.read(data, 0, BUFFER)) != -1) {
				os.write(data, 0, count);
			}
			os.flush();
		} catch (IOException e) {
			logger.error("数据解压出错", e);
		} finally {
			try {
				gis.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 文件解压缩
	 * @param file
	 */
	public static void decompress(File file){
		decompress(file, true);
	}

	/**
	 * 文件解压缩
	 * @param file
	 * @param delete 是否删除原始文件
	 */
	public static void decompress(File file, boolean delete) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(file);
			fos = new FileOutputStream(file.getPath().replace(TdpUtilConstants.GZIP_EXT_NAME, ""));
			decompress(fis, fos);
			fos.flush();
			if (delete) {
				file.delete();
			}
		} catch (Exception e) {
			logger.error("数据解压出错",e);
		}finally{
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {}
		}
	}

	/**
	 * 文件解压缩
	 * @param path
	 */
	public static void decompress(String path){
		decompress(path, true);
	}

	/**
	 * 文件解压缩
	 * @param path
	 * @param delete 是否删除原始文件
	 */
	public static void decompress(String path, boolean delete){
		File file = new File(path);
		decompress(file, delete);
	}
	
	
	public static void main(String[] args) {
		String fileName = "c:/tmp/android-upload-image.jpg.gz";
		File file = new File(fileName);
		//compress(file);
		decompress(file);
	}
}
