package com.github.mtdp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 * @Description 文件工具类
 * @author wangguoqing
 * @date 2016年7月18日下午6:04:03
 *
 */
public class FileUtil {
	
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	/**
	 * 根据传入的文件路径读取文件内容,返回文件内容字符串
	 * @param filePath 文件路径+名称
	 * @return
	 */
	public static String readFile(String filePath){
		String r = "";
		BufferedReader reader = null;
		try {
			StringBuffer sb = new StringBuffer();
			reader = new BufferedReader(new FileReader(new File(filePath)));
			String txt = null;
			while((txt=reader.readLine()) != null){
				sb.append(txt);
			}
			r = sb.toString();
		} catch (FileNotFoundException e) {
			logger.error("读取文件出错",e);
			throw new RuntimeException("读取文件出错");
		} catch (IOException e) {
			logger.error("读取文件出错",e);
			throw new RuntimeException("读取文件出错");
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
		return r;
	}
	
	
	/**
	 * 写文件
	 * @param byteArr 文件的字节数组
	 * @param filePath 文件保存的路径+名称
	 */
	public static void writeFile(byte[] byteArr,String filePath){
		File file = new File(filePath);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(byteArr);
			fos.flush();
		} catch (FileNotFoundException e) {
			logger.error("保存文件的路径不存在",e);
			throw new RuntimeException("保存文件的路径不存在");
		} catch (IOException e) {
			logger.error("写文件失败",e);
			throw new RuntimeException("写文件失败");
		}finally{
			try {
				fos.close();
			} catch (IOException e) {
			}
		}
	}
	
	/**
	 * 创建目录
	 * @param path
	 */
	public static boolean createDirectory(String path) {
		File file = new File(path);
		if(!file.exists()) {
			return file.mkdirs();
		}
		return false;
	}

}
