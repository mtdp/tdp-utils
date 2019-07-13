package com.github.mtdp.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 *
 * @Description io 流工具类
 * @author wangguoqing
 * @date 2017年1月26日上午9:48:49
 *
 */
public class IOUtil {
	
	/**
	 * 流转字节数组
	 * @param in
	 * @return
	 */
	public static byte[] readStreamAsByteArray(InputStream in){
		if(in == null ){
			return new byte[0];
		}
		byte[] buf = new byte[8*1024];
		int len = -1;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try{
			while((len = in.read(buf)) != -1){
				bos.write(buf,0,len);
			}
			bos.flush();
		}catch(IOException e){
			try {
				bos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException("流转换出错",e);
		}
		return bos.toByteArray();
	}

}
