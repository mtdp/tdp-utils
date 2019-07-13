package com.github.mtdp.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @project: tdp-utils
 * @description: 字符串工具类
 * @author: kevin.wu
 * @create_time: 2015年11月3日
 * @modify_time: 2015年11月3日
 */
public class StringUtil {

	/**
	 * 是否是空字符串
	 * 
	 * @param str
	 *            字符串
	 * @return boolean
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.length() == 0);
	}

	/**
	 * 是否不是空字符串
	 * 
	 * @param str
	 *            字符串
	 * @return boolean
	 */
	public static boolean isNotEmpty(String str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * 去除两端空白字符后是否是空字符串
	 * 
	 * @param str
	 *            字符串
	 * @return boolean
	 */
	public static boolean isBlank(String str) {
		return (str == null || str.trim().length() == 0);
	}

	/**
	 * 去除两端空白字符后是否不是空字符串
	 * 
	 * @param str
	 *            字符串
	 * @return boolean
	 */
	public static boolean isNotBlank(String str) {
		return (str != null && str.trim().length() > 0);
	}

	/**
	 * 两个字符串是否相同
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static boolean isSame(String value1, String value2) {
		if (value1 == null && value2 == null) {
			return true;
		}

		if (value1 == null && value2 != null) {
			return false;
		}

		if (value1 != null && value2 == null) {
			return false;
		}

		return value1.equals(value2);
	}

	/**
	 * 空串转null，非空串则不变化
	 * 
	 * @param value
	 * @return
	 */
	public static String emptyAsNull(String value) {
		if (value == null) {
			return value;
		}

		if (value.trim().equals("")) {
			return null;
		} else {
			return value;
		}
	}

	public static Integer asInteger(String value) {
		value = emptyAsNull(value);
		if (value == null) {
			return null;
		}

		return Integer.valueOf(value, 10);
	}

	public static int asInt(String value, int whenNull) {
		value = emptyAsNull(value);
		if (value == null) {
			return whenNull;
		}

		return Integer.valueOf(value, 10);
	}

	/**
	 * 获得日期格式
	 * 
	 * @param dateSep
	 * @return
	 */
	public static String datePattern(String dateSep) {
		return "yyyy" + dateSep + "MM" + dateSep + "dd";
	}

	/**
	 * 获得时间格式串
	 * 
	 * @param timeSep
	 * @return
	 */
	public static String timePattern(String timeSep) {
		return "HH" + timeSep + "mm" + timeSep + "ss";
	}

	/**
	 * 获得日期时间格式串
	 * 
	 * @param dateSep
	 * @param dateTimeSep
	 * @param timeSep
	 * @return
	 */
	public static String dateTimePattern(String dateSep, String dateTimeSep,
			String timeSep) {
		return datePattern(dateSep) + dateTimeSep + timePattern(timeSep);
	}

	/**
	 * 按照格式串格式化BigDecimal对象
	 * 
	 * @param pattern
	 * @param n
	 * @return
	 */
	public static String format(String pattern, BigDecimal n) {
		if (n == null) {
			return "";
		}

		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(n);
	}

	private StringUtil() {
		// DO nothing
	}

}
