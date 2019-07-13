package com.github.mtdp.util;

import java.util.Properties;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.web.context.ServletContextAware;


/**
* 
* @ClassName: ProPConfigurer 
* @Description: 自定义读取prop配置文件
* 				添加了_ctxPath变量值是servletContext.getContextPath()页面直接用 ${_ctxPath}获取path路径
* 				添加了jspvar.开头的变量,只需要在prop配置文件定义jsp页面可以直接获取 
* @author gqwang
* @date 2015年6月24日 下午3:13:25 
*
 */
public class ProPConfigurer extends PropertyPlaceholderConfigurer implements ServletContextAware {
	private static final String DEFAULT_KEY_CTXPATH = "_ctxPath";
	private static final String DEFAULT_JSPVARPREFIX = "jspvar.";
	
	private String keyCtxpath = DEFAULT_KEY_CTXPATH;
	private String jspvarPrefix = DEFAULT_JSPVARPREFIX;
	private String firstStr = DEFAULT_PLACEHOLDER_PREFIX;
	private String lastStr = DEFAULT_PLACEHOLDER_SUFFIX;
	
	private ServletContext servletContext;
	private Properties props; 
	
	@Override
	protected String resolvePlaceholder(String placeholder, Properties props) {
		this.props = props;
		initApplicationScopeVar();
		return super.resolvePlaceholder(placeholder, props);
	}
	
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	private void initApplicationScopeVar() {
		if (null != servletContext && null != props) {
			String ctxPath = servletContext.getContextPath();
			servletContext.setAttribute(keyCtxpath, ctxPath);
			props.put(keyCtxpath, ctxPath);
			for(Object keyo:props.keySet()){
				String key = String.valueOf(keyo);
				if(key.startsWith(jspvarPrefix)){
					String value = props.getProperty(key);
					value = convertValue(value);
					servletContext.setAttribute(key.substring(jspvarPrefix.length()), value);
				}
			}
		}
	}

	/**
	 * 如果配置文件引用了${xxx}配置从prop中取到xxx的值返回
	 * 否则原值返回
	 * @param value
	 * @return
	 */
	private String convertValue(String value) {
		int firstIndex = value.indexOf(firstStr);
		if(firstIndex!=-1){
			int lastIndex =  value.indexOf(lastStr);
			String key1 = value.substring(firstIndex+2,lastIndex);
			String value1 = props.getProperty(key1);
			
			value = value.replace(firstStr+key1+lastStr, value1);
			
			return convertValue(value);
		}else{
			return value;
		}
		
	}

	public void setKeyCtxpath(String keyCtxpath) {
		this.keyCtxpath = keyCtxpath;
	}

	public void setJspvarPrefix(String jspvarPrefix) {
		this.jspvarPrefix = jspvarPrefix;
	}
	
	public void setFirstStr(String firstStr) {
		this.firstStr = firstStr;
	}

	public void setLastStr(String lastStr) {
		this.lastStr = lastStr;
	}

}
