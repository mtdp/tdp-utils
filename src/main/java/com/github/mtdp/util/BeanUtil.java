package com.github.mtdp.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
/**
 * 
 *
 * @Description bean工具类,bean转map,驼峰下划线互转
 * @author wangguoqing
 * @date 2016年6月8日上午9:16:13
 *
 */
public class BeanUtil extends BeanUtils{
	
	/**
	 * bean转换成map
	 * 
	 * @param obj
	 * @param map
	 * @param isCamel2Underline true 将属性驼峰转下划线连接
	 */
	public static void beanTrans2Map(Object obj, Map<String,Object> map,boolean isCamel2Underline){
		if(obj == null){
			return;
		}
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propDesc = beanInfo.getPropertyDescriptors();
			for(PropertyDescriptor prop : propDesc){
				String key = prop.getName();
				if(isCamel2Underline){
					key = camel2Underline(key);
				}
				if("class".equals(key.toLowerCase())){
					continue;
				}
				Method getter = prop.getReadMethod();
				Object value = getter.invoke(obj);
				map.put(key, value);
			}
		} catch (IntrospectionException e) {
			throw new RuntimeException("Introspcetor获取bean信息出错",e);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("获取bean对应的值出错",e);
		}
	}
	
	/**
	 * 驼峰结构转下划线
	 * @param prop
	 * @return
	 */
	public static String camel2Underline(String prop){
		String camelProp = null;
		if(null == prop){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		char[] chs = prop.toCharArray();
		for(char ch : chs){
			//如果字符是大写
			if(Character.isUpperCase(ch)){
				sb.append("_").append(Character.toLowerCase(ch));
				continue;
			}
			sb.append(ch);
		}
		camelProp = sb.toString();
		return camelProp;
	}
	
	/**
	 * 下划线转驼峰
	 * @param prop
	 * @return
	 */
	public static String underline2Camel(String prop){
		String underlineProp = null;
		StringBuffer sb = new StringBuffer();
		int propLength = prop.length();
		for(int i = 0 ;i < propLength; i++){
			if('_' == prop.charAt(i)){
				++ i;
				sb.append(Character.toUpperCase(prop.charAt(i)));
				continue;
			}
			sb.append(prop.charAt(i));
		}
		underlineProp = sb.toString();
		return underlineProp;
	}
	
	public static void main(String[] args) {
		BeanTest rep = new BeanTest();
		rep.setBeanId("id");
		rep.setBeanName("test bean");
		Map<String,Object> map = new HashMap<String,Object>();
		beanTrans2Map(rep, map,true);
		System.out.println(map);
		System.out.println(camel2Underline("testBean"));
		System.out.println(underline2Camel("test_bean"));
		System.out.println(underline2Camel("test_2_bean"));
	}
}

class BeanTest {
	private String beanId;
	private String beanName;
	public String getBeanId() {
		return beanId;
	}
	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	
}
