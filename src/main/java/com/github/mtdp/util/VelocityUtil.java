package com.github.mtdp.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.github.mtdp.TdpUtilConstants;

/**   
 * @project: tdp-utils
 * @description: Velocity相关的工具类 
 * @author: kevin.wu
 * @create_time: 2015年12月3日
 * @modify_time: 2015年12月3日
 */
public class VelocityUtil {

	/**
	 * 映射返回内容
	 * 默认编码方式：utf-8
	 * @param templatePath
	 * @param params
	 * @return
	 */
	public static String merge(String templatePath, Map<String, Object> params) {
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(Velocity.RESOURCE_LOADER, "class");
		ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");  
		
		ve.init();
		Template t = ve.getTemplate(templatePath, TdpUtilConstants.CHARACTER_UTF_8);
		
		VelocityContext context = new VelocityContext();
		if(params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				context.put(key, params.get(key));
			}
		}
		
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		
		return writer.toString();
	}
	
	/**
	 * 
	 * @param templatePath
	 * @param params
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String merge(String templatePath, Map<String, Object> params, String filePath) throws IOException {
		//判断文件(含文件夹)是否存在
		File file = initFile(filePath);
		
		//映射内容
		String content = merge(templatePath, params);

		file.createNewFile();
		Writer writer = new FileWriter(file);
		
		try {
			writer.write(content);
			writer.flush();
		} finally {
			writer.close();
		}
		
		return content;
	}
	
	private static final File initFile(String filePath) {
		StringTokenizer st = new StringTokenizer(filePath, TdpUtilConstants.FILE_PATH_SEPARATOR);
		List<String> paths = new ArrayList<String>();
		while (st.hasMoreElements()) {
			paths.add(st.nextToken());
  		}
		
		String[] pathArray = paths.toArray(new String[paths.size()]);
		StringBuffer filePathExt = new StringBuffer(pathArray[0]);
		for (int i = 1; i < pathArray.length - 1; i++) {
			filePathExt.append(TdpUtilConstants.FILE_PATH_SEPARATOR);
			filePathExt.append(pathArray[i]);
			
			File fileExt = new File(filePathExt.toString());
			if (fileExt.exists() == false) {
				fileExt.mkdir();
			}
		}
		
		filePathExt.append(TdpUtilConstants.FILE_PATH_SEPARATOR);
		filePathExt.append(pathArray[pathArray.length - 1]);
		File file = new File(filePathExt.toString());
		if (file.exists()) {
			file.delete();
		}
		
		return file;
	}
	
	public static void main(String[] args) {
		merge("", new HashMap<String,Object>());
	}
}
