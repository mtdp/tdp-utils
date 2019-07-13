package com.github.mtdp.util;

import java.util.Random;

/**
 * 
 *
 * @Description id自动生成
 * @author wangguoqing
 * @date 2016年5月24日上午9:23:59
 *
 */
public class IdGenerateUtil {

	
	public static Long nextId(){
		Random r = new Random();
		IdWorker w = new IdWorker(r.nextInt(30), r.nextInt(30));
		return w.nextId();
	}
	
}
