package com.github.mtdp.util;

import java.util.UUID;

/**   
 * @project: tdp-utils
 * @description: 
 * @author: kevin.wu
 * @create_time: 2015年12月3日
 * @modify_time: 2015年12月3日
 */
public class UUIDUtil {

	/**
	 * 返回随机的UUID
	 * @return
	 */
	public static String genUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid.replace("-", "");
	}
	
	private UUIDUtil() {
		//DO NOTHIING
	}
}
