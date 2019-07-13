package com.github.mtdp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 * @Description 校验身份证<br/>
 * 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。<br/>
        排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位校验码。<br/>
   1、地址码<br/>
               表示编码对象常住户口所在县(市、旗、区)的行政区域划分代码，按GB/T2260的规定执行。<br/>
   2、出生日期码<br/>
             表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。<br/>
   3、顺序码<br/>
            表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配给女性。<br/>
   4、校验码计算步骤<br/>
    (1)十七位数字本体码加权求和公式<br/>
     S = Sum(Ai * Wi), i = 0, … , 16 ，先对前 17 位数字的权求和<br/>
     Ai：表示第i位置上的身份证号码数字值(0~9)
	 Wi：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 （表示第 i 位置上的加权因子）<br/>
	(2)计算模<br/>
	 Y = mod(S, 11)	<br/>
	(3)根据模，查找得到对应的校验码	<br/>
	 Y: 0 1 2 3 4 5 6 7 8 9 10<br/>
	 校验码: 1 0 X 9 8 7 6 5 4 3 2<br/>
 * @author wangguoqing
 * @date 2016年6月23日下午1:00:05
 *
 */
public class IdCardUtil {
	
	private static Logger logger = LoggerFactory.getLogger(IdCardUtil.class);
	
	/**
	 * 校验18位身份证号码是否正确 true=正确,false=不正确
	 * @param idCard
	 * @return
	 */
	public static boolean validate(String idCard){
		boolean t = false;
		if(StringUtil.isBlank(idCard) || idCard.length() < 18){
			return t;
		}
		int len = idCard.length();
		//获取身份证前17位
		String idCard17 = idCard.substring(0, len-1);
		String idCardOfLastOne = idCard.substring(len-1);
		//十七位数字本体码权重
		int[] weight={7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
		//mod11,对应校验码字符值    
	    char[] validate={ '1','0','X','9','8','7','6','5','4','3','2'};
	    //求身份证前17位数字之和
	    int sum = 0,mode = 0;
	    for(int i = 0; i < idCard17.length(); i++){
	    	sum += Integer.parseInt(String.valueOf(idCard17.charAt(i))) * weight[i];
	    }
	    mode = sum % 11;
	    //根据身份证前17位数字计算出的最后一位数字
	    char cal = validate[mode];
	    logger.info("计算出身份证[{}]最后一位是[{}]",idCard,cal);
	    if(cal == idCardOfLastOne.charAt(0)){
	    	t = true;
	    }
		return t;
	}

	public static void main(String[] args) {
		System.out.println(validate("340825198808072512"));
	}
	
}
