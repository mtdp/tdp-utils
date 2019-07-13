package com.github.mtdp.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * 
 *
 * @Description freemark工具类
 * @author wangguoqing
 * @date 2016年7月18日下午7:28:32
 *
 */
public class FreeMarkerUtil {
	
	/**
	 * 根据模版内容返回填充后的内容
	 * @param map 模版中的key-value
	 * @param templateTxt 模版内容
	 * @return
	 */
	 public static String getFreeMarkerText(String templateTxt,Map<String,String> map) throws Exception {
		String result = null;
		Configuration config = new Configuration();
		try {
			StringTemplateLoader strTemplate = new StringTemplateLoader();
			strTemplate.putTemplate("t", templateTxt);
			config.setTemplateLoader(strTemplate);
			config.setDefaultEncoding("UTF-8");
			Template template = config.getTemplate("t","UTF-8");
			//输出流
			StringWriter out = new StringWriter();
			template.process(map, out);
			result = out.toString();
		} catch (IOException e) {
			throw new Exception("获取freemark模版出错",e);
		} catch (TemplateException e) {
			throw new Exception("freemark模版处理异常",e);
		}
		return result;
	}

}
