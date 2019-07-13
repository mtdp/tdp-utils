package com.github.mtdp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 * @Description tdp-utils
 * @author wangguoqing
 * @date 2016年7月31日上午11:17:53
 *
 */
public class DateUtil {
  
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
    
    /** 默认格式 yyyyMMddHHmmss **/
	public static String DEFAULT_PATTERN = "yyyyMMddHHmmss";
	/** 默认格式2 yyyy-MM-dd HH:mm:ss **/
	public static String DEFAULT_PATTERN_2 = "yyyy-MM-dd HH:mm:ss";
	/** 格式yyyy-MM-dd **/
	public static String DATE_PATTERN = "yyyy-MM-dd";
	/** 格式yyyyMMdd **/
	public static String DATE_PATTERN_2 = "yyyyMMdd";
    

	/**
	 * 按照格式串格式化Date对象
	 * @param pattern
	 * @param locale
	 * @param d
	 * @return
	 */
	public static String format(String pattern, Locale locale, Date d) {
		if (d == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
		return sdf.format(d);
	}

	/**
	 * 按照格式串格式化Date对象
	 * 
	 * @param pattern
	 * @param d
	 * @return
	 */
	public static String format(String pattern, Date d) {
		if (d == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(d);
	}
	
	/**
	 * 返回格式化的默认格式  yyyyMMddHHmmss 
	 * @param date
	 * @return
	 */
	public static String formatDefault(Date date){
		return format(DEFAULT_PATTERN,date);
	}
	/**
	 * 返回格式化的默认格式2 yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String formatDefault2(Date date){
		return format(DEFAULT_PATTERN_2,date);
	}
	
	/**
	 * 返回yyyy-MM-dd 格式的日期
	 * @param date
	 * @return
	 */
	public static String toDate(Date date){
		return format(DATE_PATTERN,date);
	}
	
	/**
	 * 返回yyyyMMdd 格式的日期
	 * @param date
	 * @return
	 */
	public static String toDate2(Date date){
		return format(DATE_PATTERN_2,date);
	}
	
	/**
	 * 获取日期的 年份
	 * @param date
	 * @return
	 */
	public static String toYear(Date date){
		return format("yyyy",date);
	}

	/**
	 * 获取日期的 月份
	 * @param date
	 * @return
	 */
	public static String toMonth(Date date){
		return format("MM", date);
	}
	
	/**
	 * 返回日期的 日
	 * @param date
	 * @return
	 */
	public static String toShortDay(Date date){
		return format("dd",date);
	}
	
	/**
	 * 返回日期的 时:分:秒
	 * @param date
	 * @return
	 */
	public static String toShortSeconds(Date date){
		return format("HH:mm:ss",date);
	}
	
	/**
	 * 将时分秒置0
	 * @param date
	 * @return
	 */
	public static Date roundToDay(Date date){
		Date d = null;
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		d = cal.getTime();
		return d;
	}
	
	/**
	 * 判断日期是否相同 精确到 日
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isSameDay(Date d1,Date d2){
		return DateUtil.roundToDay(d1).getTime() == DateUtil.roundToDay(d2).getTime();
	}
	
	/**
	 * 时间相减 返回天数
	 * @param big
	 * @param samll
	 * @return
	 * @throws Exception
	 */
	public static double dayInterval(Date big,Date samll) throws Exception{
		if(big.getTime() < samll.getTime()){
			throw new Exception("时间参数big小于samll");
		}
		return ((big.getTime()-samll.getTime())/(1000*60*60*24)); 
	}
	
	/**
	 * 时间相减 返回小时
	 * @param big
	 * @param samll
	 * @return
	 * @throws Exception
	 */
	public static double hourInterval(Date big,Date samll) throws Exception{
		if(big.getTime() < samll.getTime()){
			throw new Exception("时间参数big小于samll");
		}
		return ((big.getTime()-samll.getTime())/(1000*60*60)); 
	}
	
	/**
	 * 时间相减 返回分钟
	 * @param big
	 * @param samll
	 * @return
	 * @throws Exception
	 */
	public static double minuteInterval(Date big,Date samll) throws Exception{
		if(big.getTime() < samll.getTime()){
			throw new Exception("时间参数big小于samll");
		}
		return ((big.getTime()-samll.getTime())/(1000*60)); 
	}
	
	/**
	 * 时间相减 返回秒
	 * @param big
	 * @param samll
	 * @return
	 * @throws Exception
	 */
	public static double secondInterval(Date big,Date samll) throws Exception{
		if(big.getTime() < samll.getTime()){
			throw new Exception("时间参数big小于samll");
		}
		return ((big.getTime()-samll.getTime())/(1000)); 
	}
	
	/**
	 * 返回日期的下一天
	 * @param date
	 * @return
	 */
	public static Date nextDate(Date date){
		return nextNumDate(date,1);
	}
	
	public static Date lastDate(Date date){
		return nextNumDate(date,-1);
	}
	
	/**
	 * 返回日期的下(num)几天
	 * @param date
	 * @return
	 */
	public static Date nextNumDate(Date date,int num){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, num);
		return cal.getTime();
	}
	
	/**
	 * 根据date 增加 amount
	 * @param date
	 * @param field
	 * @param amount
	 * @return
	 */
	public static Date add(Date date,int field,int amount){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(field, amount);
		return cal.getTime();
	}
	
	/**
	 * 根据date 设置 amount
	 * @param date
	 * @param field
	 * @param amount
	 * @return
	 */
	public static Date set(Date date,int field,int amount){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(field, amount);
		return cal.getTime();
	}
	
	/**
	 * 格式化时间并补成14位
	 * @param time
	 * @return
	 */
	public static String formatDate(String time){
		//直接替换 看是否满足要求
		String sTime = time.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "").replaceAll("/", "");
		//替换后 不需要补位直接返回
		if(sTime.length() == 14){
			return sTime;
		}
		StringBuilder sb = new StringBuilder();
		String[] strArr = time.split(" ");
		if(strArr.length < 2){
			return sTime;
		}
		//用 - 切割 
		String[] dArr = strArr[0].split("-");
		if(dArr.length <= 1){
			//用 / 切割 
			dArr = strArr[0].split("/");
		}
		String[] hArr = strArr[1].split(":");
		String[] pArr = new String[dArr.length + hArr.length];
		System.arraycopy(dArr, 0, pArr, 0, dArr.length);
		System.arraycopy(hArr, 0, pArr, dArr.length, hArr.length);
		for(int i = 0; i < pArr.length; i++){
			String s = pArr[i];
			if( i == 0){
				sb.append(s);
				continue;
			}
			if(s.length() < 2){
				sb.append("0"+s);
			}else{
				sb.append(s);
			}
		}
		if(sb.toString().length() > 14){
			return sTime;
		}
		return sb.toString();
	
	}
	
	/**
	 * 按照传入的时间格式解析成时间对象
	 * @param pattern 传入的时间格式如 yyMMddHHmm
	 * @param d 传入的时间字符串
	 * @return
	 */
	public static Date parse(String pattern,String d){
	  if(d == null){
	    return null;
	  }
	  SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	  try{
	    return sdf.parse(d);
	  }catch(Exception e){
	    logger.error("解析时间异常",e);
	  }
	  return null;
	}
	
	/**
	 * 解析时间字符串成日期,字符串格式为yyyyMMddHHmmss 
	 * @param d
	 * @return
	 */
	public static Date parse(String d){
		return parse(DEFAULT_PATTERN, d);
	}
	
	/**
	 * 格式化当前时间并返回 
	 * yyyyMMddHHmmss
	 * @return
	 */
	public static String getCurrentTime(){
		return format(DEFAULT_PATTERN, new Date());
	}
	
	/**
	 * 格式化当前时间并返回 
	 * yyyy-MM-dd HH:mm:ss 
	 * @return
	 */
	public static String getCurrentTime2(){
		return format(DEFAULT_PATTERN_2, new Date());
	}
	
	/**
	 * 获取当天的凌晨时间
	 * @return
	 */
	public static Date getCurrentWeeHoursTime(){
	  Calendar cal = Calendar.getInstance();
	  cal.set(Calendar.HOUR_OF_DAY, 0);
	  cal.set(Calendar.MINUTE, 0);
	  cal.set(Calendar.SECOND, 0);
	  cal.set(Calendar.MILLISECOND, 0);
	  return cal.getTime();
	}
	
	/**
	 * 比较2个时间的大小
	 * @param d1
	 * @param d2
	 * @return true d1>d2 ,否则返回false
	 */
	public static boolean compareDateTime(Date d1,Date d2){
		if(d1.getTime() > d2.getTime()){
			return true;
		}
		return false;
	}
	
	/**
	 * 计算2个时间的差值,如果2个参数有一个为空返回-1
	 * @param maxDate
	 * @param minDate
	 * @return 相差毫秒数
	 */
	public static long calculateTwoTimeLag(Date maxDate,Date minDate){
		if(maxDate == null || minDate == null){
			return -1;
		}
		return maxDate.getTime() - minDate.getTime();
	}
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		Date d = new Date();
		d.setTime(1469931677778l);
		long l = calculateTwoTimeLag(new Date(), d);
		System.out.println(l);
		System.out.println(~calculateTwoTimeLag(d,new Date()));
		System.out.println(format(DEFAULT_PATTERN_2, d));
	}
}
