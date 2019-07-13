package com.github.mtdp.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**   
 * @project: tdp-utils
 * @description: 数字工具类 
 * @author: kevin.wu
 * @create_time: 2015年11月3日
 * @modify_time: 2015年11月3日
 */
public class NumberUtil {

	/**
	 * 格式化数字<br/>
	 * 数字说明：<br/>
	 * 	定义BigDecimal类型，主要就是考虑金额处理场景。<br/>
	 * Pattern说明：<br/>
	 * 	1、小数点后保留几位。pattern：0.0 (小数点前面不会变化，后面的0的个数决定了小数点后面保留几位；四舍五入) --- 如：1234.456, 结果为：1234.5 ；<br/>
	 * 	2、数字前后格式化添加单位或货币符号。pattern：0.00元 或 ￥0.00 （也可以前后都加） --- 如：1234.456 ，结果为 1234.46元 或 ￥1234.46；<br/>
	 * @param num 待格式化的数字
	 * @param pattern
	 * @return
	 */
	public static String format(BigDecimal num, String pattern) {
		if (num == null) {
			return "";
		}

		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(num);
	}
	
	public static void main(String[] args) {
		String pattern = "￥0.00元";
		BigDecimal num = new BigDecimal(1234.446);
		
		System.out.println(format(num, pattern));
		
	}
}
